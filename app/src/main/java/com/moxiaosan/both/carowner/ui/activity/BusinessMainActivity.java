package com.moxiaosan.both.carowner.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.fragment.LeftFragment_two;
import com.moxiaosan.both.carowner.ui.fragment.OrderFragment;
import com.moxiaosan.both.carowner.ui.fragment.TakeOrderFragment;
import com.moxiaosan.both.common.ui.activity.CityPositionActivity;
import com.moxiaosan.both.mqtt.MqttService;
import com.umeng.update.UmengUpdateAgent;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.log.LLog;
import com.utils.ui.base.ActivityHolder;
import com.utils.ui.base.BaseFragmentActivity;

import java.util.Timer;
import java.util.TimerTask;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.Achievement;
import consumer.model.BindDevice;
import consumer.model.SetOrder;
import consumer.model.obj.RespUserInfo;

/**
 * Created by chris on 16/2/29.
 */

public class BusinessMainActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,IApiCallback {
    public final static int CITY_GET_CODE = 1;

    private SlidingMenu slidingMenu;
    private FragmentManager fm;
    private LeftFragment_two lf;
    private FragmentTransaction ft;
    private TakeOrderFragment takeOrderFragment;
    private OrderFragment orderFragment;
    private RadioGroup radioGroup;
    private RadioButton takeOrderButton, orderButton;
    private LinearLayout messageLinear,safeLinear;
    private TextView tvOrder,tvMoney;
    private Animation animation;
    private ImageView imgZhuan;
    private CheckBox checkBox;
    private TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_activity_main);
        showActionBar(false);

        CarReqUtils.checkdeviced(this,this,null,new BindDevice(),"checkdeviced",true,StringUrlUtils.geturl(new HashMapUtils().putValue("username",AppData.getInstance().getUserEntity().getUsername()).createMap()));

        //友盟自动更新
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setDeltaUpdate(true);
        initMqttServer();

        findViewById(R.id.main_user_photo).setOnClickListener(this);
        findViewById(R.id.main_city_location).setOnClickListener(this);
        findViewById(R.id.businessMessageId).setOnClickListener(this);
        findViewById(R.id.safeCenterId).setOnClickListener(this);

        tvLocation =(TextView)findViewById(R.id.tvLocationId);

        imgZhuan =(ImageView)findViewById(R.id.zhuandongId);
        checkBox =(CheckBox)findViewById(R.id.orderBox);

        tvOrder =(TextView)findViewById(R.id.tvDanId);
        tvMoney =(TextView)findViewById(R.id.tvMoneyId);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupId);
        takeOrderButton = (RadioButton) findViewById(R.id.jiedanId);
        orderButton = (RadioButton) findViewById(R.id.dingdanId);

        radioGroup.setOnCheckedChangeListener(this);
        takeOrderButton.setChecked(true);

        showSlidingMenu();

        animation = AnimationUtils.loadAnimation(this, R.anim.round_zhaun);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (animation != null) {

                        CarReqUtils.setuorder(BusinessMainActivity.this, BusinessMainActivity.this, null, new SetOrder(), "start", true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                        putValue("type", "1").createMap()));

                    }
                } else {
                    if (animation != null) {

                        CarReqUtils.setuorder(BusinessMainActivity.this, BusinessMainActivity.this, null, new SetOrder(), "close", true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                        putValue("type", "2").createMap()));

                    }
                }
            }
        });

    }


    /**
     * 启动 mqtt 服务
     */
    private void initMqttServer() {
        String mDeviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);  //设备ID保存
        SharedPreferences.Editor editor = getSharedPreferences(MqttService.TAG, MODE_PRIVATE).edit();
        editor.putString(MqttService.PREF_DEVICE_ID, mDeviceID);
        editor.commit();
        this.startService(new Intent(this, MqttService.class));
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
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

        takeOrderFragment = new TakeOrderFragment();

        ft.replace(R.id.fragmentId, takeOrderFragment);

        ft.replace(R.id.menuId, lf);

        ft.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        CarReqUtils.Achivement(this, this, null, new Achievement(), "Achievement", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_user_photo:
                if (!slidingMenu.isShown()) {
                    slidingMenu.showMenu();
                } else {
                    slidingMenu.toggle();//关闭菜单
                }

                break;
            case R.id.main_city_location:
                Intent intent = new Intent(BusinessMainActivity.this, CityPositionActivity.class);
                startActivityForResult(intent, CITY_GET_CODE); //requestCode
                break;

            case R.id.businessMessageId:
                startActivity(new Intent(BusinessMainActivity.this, BusinessInfoActivity.class));
                break;

            case R.id.safeCenterId:
                startActivity(new Intent(BusinessMainActivity.this, GPSSafeCenterActivity.class).putExtra("isFromMain",true));
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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        fm = getSupportFragmentManager();

        ft = fm.beginTransaction();

        hideFragments(ft);

        if (checkedId == R.id.jiedanId) {

            if (takeOrderFragment == null) {
                takeOrderFragment = new TakeOrderFragment();
                ft.add(R.id.fragmentId, takeOrderFragment);
            } else {
                ft.show(takeOrderFragment);
            }

        } else if (checkedId == R.id.dingdanId) {
            if (orderFragment == null) {
                orderFragment = new OrderFragment();
                ft.add(R.id.fragmentId, orderFragment);
            } else {
                ft.show(orderFragment);
            }

        } else {
            return;
        }

        ft.commit();

    }

    //隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(android.support.v4.app.FragmentTransaction transaction) {
        if (takeOrderFragment != null) {
            transaction.hide(takeOrderFragment);
        }
        if (orderFragment != null) {
            transaction.hide(orderFragment);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
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
            ActivityHolder.getInstance().finishAllActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭 mqtt 服务
        if (MqttService.wasStarted()){
            LLog.i("===BusinessMainActivity===onDestroy()==stopService()");
            stopService(new Intent(this,MqttService.class));
        }
    }

    @Override
    public void onData(Object output, Object input) {

        if(output!=null){
            if(output instanceof Achievement){
                Achievement achievement =(Achievement)output;
//                EUtil.showToast(achievement.getErr());
                if(achievement.getRes().equals("0")){
                    tvOrder.setText(achievement.getData().getOrders());
                    tvMoney.setText(achievement.getData().getIncome());

                }
            }

            if(output instanceof BindDevice){
                BindDevice device =(BindDevice)output;

                if(device.getRes().equals("0")){

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setBind(1);
                    AppData.getInstance().saveUserEntity(userInfo);

                }else{

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setBind(2);
                    AppData.getInstance().saveUserEntity(userInfo);

                }
            }


            if(output instanceof SetOrder){
                SetOrder setOrder =(SetOrder)output;
                if(setOrder.getRes().equals("0")){
                    if(input.equals("start")){
                        imgZhuan.startAnimation(animation);

                    }else{
                        imgZhuan.clearAnimation();

                    }
                }
            }
        }else{

//            EUtil.showToast("网络错误，请稍后重试");
        }

    }
}
