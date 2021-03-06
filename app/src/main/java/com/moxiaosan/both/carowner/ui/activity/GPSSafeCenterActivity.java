package com.moxiaosan.both.carowner.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.moxiaosan.both.APP;
import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.fragment.LeftFragment_two;
import com.moxiaosan.both.common.ui.activity.CityPositionActivity;
import com.moxiaosan.both.mqtt.MqttService;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.log.LLog;
import com.utils.ui.base.ActivityHolder;
import com.utils.ui.base.BaseFragmentActivity;

import java.util.Timer;
import java.util.TimerTask;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.BindDevice;
import consumer.model.Mqtt;
import consumer.model.RespAlarmnums;
import consumer.model.RespCut;
import consumer.model.RespGuard;

/**
 * Created by chris on 16/3/1.
 */
public class GPSSafeCenterActivity extends BaseFragmentActivity implements View.OnClickListener, IApiCallback {

    public final static int CITY_GET_CODE = 1;
    public final static String ALRM_NOTITY = "alrm_notify";
    private SlidingMenu slidingMenu;
    private FragmentManager fm;
    private LeftFragment_two lf;
    private FragmentTransaction ft;
    private CheckBox checkBox;
    private TextView tvLocation,tvDistrict;
    private ExitDialog dialog;
    private int type;
    private ImageView imgWarn;
    private TextView tvMessage, tvNote;
    private TextView tvStutas;

    private AlrmBroadReceiver alrmBroadReceiver;
    private SharedPreferences spAlarmNum = null;  //警情数量
    private SharedPreferences sp;  //定位的
    private SharedPreferences.Editor editorLocation = null;//定位的

    private boolean isFromMain = false; //判断从哪个页面跳转过来的
    private ImageView imgUserPhoto;

    private class AlrmBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ALRM_NOTITY)) {
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(spAlarmNum.getInt("alarmNum", 0) + "");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_gpssafecenter_layout);
        spAlarmNum = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
        sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        editorLocation = sp.edit();

        isFromMain = getIntent().getBooleanExtra("isFromMain", false);
        checkBox = (CheckBox) findViewById(R.id.checkBoxSafeId);

        showLoadingDialog();


        CarReqUtils.getguard(this, this, null, new RespGuard(), "getguard", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

        CarReqUtils.getcut(this, this, null, new RespCut(), "getcut", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

        CarReqUtils.alarmnums(this, this, null, new RespAlarmnums(), "alarmnums", true, StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));


        tvLocation = (TextView) findViewById(R.id.tvLocationId);
        tvDistrict = (TextView) findViewById(R.id.tvDistrictId);
        if (!TextUtils.isEmpty(sp.getString("city", ""))) {
            tvLocation.setText(sp.getString("city", ""));
        }
        if (!TextUtils.isEmpty(sp.getString("district", ""))) {
            tvDistrict.setText(sp.getString("district", ""));
        }

        imgUserPhoto = (ImageView) findViewById(R.id.main_user_photo);
        if (isFromMain) {
            imgUserPhoto.setPadding(20, 0, 0, 0);
            imgUserPhoto.setImageResource(R.mipmap.back_big);
        }
        imgUserPhoto.setOnClickListener(this);
        findViewById(R.id.main_city_location).setOnClickListener(this);

        findViewById(R.id.imgLocationId).setOnClickListener(this);
        findViewById(R.id.imgGUiJiId).setOnClickListener(this);
        findViewById(R.id.imgFindId).setOnClickListener(this);
        findViewById(R.id.warningId).setOnClickListener(this);
        findViewById(R.id.warning_bottom_layout).setOnClickListener(this);
        tvMessage = (TextView) findViewById(R.id.main_my_message_count);
        tvNote = (TextView) findViewById(R.id.noteId);
        tvStutas = (TextView) findViewById(R.id.open_close);

        showSlidingMenu();
        initMqttServer();

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(ALRM_NOTITY);
        alrmBroadReceiver = new AlrmBroadReceiver();
        registerReceiver(alrmBroadReceiver, iFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        CarReqUtils.checkdeviced(this, this, null, new BindDevice(), "checkdeviced", true, StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

        if (spAlarmNum.getInt("alarmNum", 0) > 0) {
            tvMessage.setText(spAlarmNum.getInt("alarmNum", 0) + "");
        } else {
            tvMessage.setVisibility(View.GONE);
        }

    }

    /**
     * 启动 mqtt 服务
     */
    private void initMqttServer() {
        String mDeviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);  //设备ID保存
        SharedPreferences.Editor editor = getSharedPreferences(MqttService.TAG, MODE_PRIVATE).edit();
        editor.putString(MqttService.PREF_DEVICE_ID, mDeviceID);
        editor.commit();
        this.startService(new Intent(GPSSafeCenterActivity.this, MqttService.class));
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_user_photo:
                if (isFromMain) {
                    finish();
                } else {
                    if (!slidingMenu.isShown()) {
                        slidingMenu.showMenu();
                    } else {
                        slidingMenu.toggle();//关闭菜单
                    }
                }
                break;
            case R.id.main_city_location:
                Intent intent = new Intent(GPSSafeCenterActivity.this, CityPositionActivity.class);
                startActivityForResult(intent, CITY_GET_CODE); //requestCode
                break;


            case R.id.imgLocationId:
                Intent intent1 = new Intent(this, NowLocationActivity.class);
                startActivity(intent1);
                break;

            case R.id.imgGUiJiId:
                startActivity(new Intent(GPSSafeCenterActivity.this, GuiJiActivity.class));
                break;

            case R.id.imgFindId:

                if (AppData.getInstance().getUserEntity().getBind() == 1) {   //已经绑定设备
//                    if (type == 1) {
//
////                        Toast.makeText(GPSSafeCenterActivity.this, "已经断电断油", Toast.LENGTH_SHORT).show();
//                        Intent intent2 = new Intent(GPSSafeCenterActivity.this, FindLocationActivity.class);
//                        startActivity(intent2);
//                    } else {

                    dialog = new ExitDialog(this, 3);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
//                    }

                } else {
                    EUtil.showToast("未绑定设备,请先绑定设备");
                }
                break;

            case R.id.warningId:
                startActivity(new Intent(GPSSafeCenterActivity.this, WarningActivity.class));
                break;

            case R.id.warning_bottom_layout:
                startActivity(new Intent(GPSSafeCenterActivity.this, WarningActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CITY_GET_CODE) {
                tvLocation.setText(data.getStringExtra("city"));
                data.getDoubleExtra("lng", 0.0);
                data.getDoubleExtra("lat", 0.0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showSlidingMenu() {

        slidingMenu = new SlidingMenu(this);

        slidingMenu.setMode(SlidingMenu.LEFT);

        slidingMenu.setMenu(R.layout.slidingmenu);

        // 第四步:设置菜单显示的宽度
        int sw = getResources().getDisplayMetrics().widthPixels;
        slidingMenu.setBehindWidth((int) (sw * 4 / 5));// 单位:px

        // 设置启用菜单渐变动画
        slidingMenu.setFadeEnabled(true);
        // 设置菜单动画的渐变度
        slidingMenu.setFadeDegree(0.9f);

        // 设置菜单层与内容滚动比例
        slidingMenu.setBehindScrollScale(0.25f);

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        fm = getSupportFragmentManager();

        ft = fm.beginTransaction();

        lf = new LeftFragment_two(slidingMenu);

        ft.replace(R.id.menuId, lf);

        ft.commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (AppData.getInstance().getUserEntity().getType() == 2) {
                exitBy2Click();      //调用双击退出函数
            } else {
                finish();
            }
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            ActivityHolder.getInstance().finishAllActivity();
            System.exit(0);
        }
    }

    @Override
    public void onData(Object output, Object input) {
        if (output != null) {
            dismissLoadingDialog();
            if (output instanceof Mqtt) {
                Mqtt mqtt = (Mqtt) output;
                if (input.equals("Close")) {
                    if (mqtt.getRes().equals("0")) {
                        dialog.dismiss();
                        EUtil.showToast(mqtt.getErr());
                        tvStutas.setText("防盗模式关闭");
                    } else {
                        EUtil.showToast(mqtt.getErr());
                    }
                } else if (input.equals("open")) {

                    if (mqtt.getRes().equals("0")) {
                        tvStutas.setText("防盗模式开启");

                        EUtil.showToast(mqtt.getErr());
                    } else {
                        EUtil.showToast(mqtt.getErr());
                    }

                } else if (input.equals("r_close")) { //切断供油电  返回

                    if (mqtt.getRes().equals("0")) {

                        dialog.dismiss();
                        Intent intent2 = new Intent(GPSSafeCenterActivity.this, FindLocationActivity.class);
                        intent2.putExtra("type", 2);
                        startActivity(intent2);

                    }
                }

            }

            if (output instanceof RespCut) {

                RespCut cut = (RespCut) output;
                if (cut.getRes().equals("0")) {
                    if (cut.getData().getCut().equals("1")) {
                        type = 1;
                    } else {
                        type = 2;
                    }
                }
            }

            if (output instanceof RespGuard) {
                RespGuard guard = (RespGuard) output;
                if (guard.getRes().equals("0")) {

                    if (AppData.getInstance().getUserEntity().getBind() == 1) {
                        if (guard.getData().getGuard().equals("1")) {
                            checkBox.setChecked(false);
                            tvStutas.setText("防盗模式开启");

                        } else {

                            checkBox.setChecked(true);
                            tvStutas.setText("防盗模式关闭");

                        }

                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if (isChecked) {
                                    dialog = new ExitDialog(GPSSafeCenterActivity.this, 2);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                } else {
                                    showLoadingDialog();
                                    CarReqUtils.guard(GPSSafeCenterActivity.this, GPSSafeCenterActivity.this, null, new Mqtt(), "open", true,
                                            StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                                    putValue("type", 1).createMap()));


                                }
                            }
                        });
                    } else {
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                checkBox.setChecked(true);
                                EUtil.showToast("未绑定设备,请先绑定设备");
                            }
                        });

                    }

                }
            }

            if (output instanceof RespAlarmnums) {
                RespAlarmnums alarmnums = (RespAlarmnums) output;

                if (alarmnums.getRes().equals("0")) {
                    if (!alarmnums.getData().getNums().equals("0")) {

                        tvNote.setText(alarmnums.getData().getAlarminfo());
                    }
                }
            }
        }

    }

    // dialog
    class ExitDialog extends AlertDialog {

        int index;

        public ExitDialog(Context context, int index) {
            super(context);
            this.index = index;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);

            TextView textView = (TextView) findViewById(R.id.tvDialogActivity);

            if (index == 1) {
                textView.setText("开启防盗模式，请确认！");
            } else if (index == 2) {
                textView.setText("关闭防盗模式，请确认！");
            } else {   //3
                textView.setText("本操作会执行远程切断供\n油供电操作，请确认！");
            }


            ViewGroup.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showLoadingDialog();
//                    dismiss();
                    if (index == 2) {

                        CarReqUtils.guard(GPSSafeCenterActivity.this, GPSSafeCenterActivity.this, null, new Mqtt(), "Close", true,
                                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                        putValue("type", 2).createMap()));

                    } else if (index == 3) {  //切断供油电
                        CarReqUtils.recoverlost(GPSSafeCenterActivity.this, GPSSafeCenterActivity.this, null, new Mqtt(), "r_close", true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                        putValue("type", "1").createMap()));

                    }

                }
            });

            findViewById(R.id.setting_exit_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(alrmBroadReceiver);
        APP.getInstance().stopLocationClient(); //关闭定位
        //关闭 mqtt 服务
        if (MqttService.wasStarted()) {
            LLog.i("===ConsumerMainActivity===onDestroy()==stopService()");
            stopService(new Intent(this, MqttService.class));
        }
    }
}
