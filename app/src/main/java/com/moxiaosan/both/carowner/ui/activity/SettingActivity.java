package com.moxiaosan.both.carowner.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.Mqtt;
import consumer.model.RespDeviceStatus;

/**
 * Created by chris on 16/3/3.
 */
public class SettingActivity extends BaseActivity implements IApiCallback, View.OnClickListener {

    private RadioGroup selectGroup;
    private CheckBox factoryCB;
    private int selectType;
    private ExitDialog dialog;
    private DeviceSettingDialog dialog1, dialog2, dialog3, dialog4;
    private RadioButton buttonOne, buttonTwo, buttonThree, buttonFour;
    private RespDeviceStatus status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_setting_layout);
        showActionBar(true);
        setActionBarName("设备设置");

        showLoadingDialog();
        CarReqUtils.getdeviceinfo(SettingActivity.this, SettingActivity.this, null, new RespDeviceStatus(), "getdeviceinfo", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

        selectGroup = (RadioGroup) findViewById(R.id.selectRadioGroup);
        factoryCB = (CheckBox) findViewById(R.id.factory_checkbox);

        buttonOne = (RadioButton) findViewById(R.id.radioOne);
        buttonTwo = (RadioButton) findViewById(R.id.radioTwo);
        buttonThree = (RadioButton) findViewById(R.id.radioThree);
        buttonFour = (RadioButton) findViewById(R.id.radioFour);

        findViewById(R.id.sleep_layout).setOnClickListener(this);
        findViewById(R.id.sos_layout).setOnClickListener(this);
        findViewById(R.id.weilan_layout).setOnClickListener(this);
        findViewById(R.id.baoyang_layout).setOnClickListener(this);
//        SharedPreferences sp =getSharedPreferences("request", Activity.MODE_PRIVATE);
//
//        boolean flag =sp.getBoolean("sleep",false);
//
//        if(flag){
//            sleepCB.setChecked(true);
//        }else{
//            sleepCB.setChecked(false);
//        }
//        sleepCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                SharedPreferences sp =getSharedPreferences("request", Activity.MODE_PRIVATE);
//
//                SharedPreferences.Editor editor =sp.edit();
//
//                if(isChecked){
//                    hour =etHour.getText().toString();
//                    editor.putBoolean("sleep",true);
//                }else{
//                    hour ="0";
////                    etHour.setText(0+"");
//                    editor.putBoolean("sleep",false);
//                }
//
//                editor.commit();
//
//            }
//        });
//
        factoryCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    dialog = new ExitDialog(SettingActivity.this, 2);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {
        if (isLoadingDialogShowing()) {
            dismissLoadingDialog();
        }
        if (output == null) {
            return;
        }
        if (output instanceof Mqtt) {
            Mqtt mqtt = (Mqtt) output;
            EUtil.showToast(mqtt.getErr());
            if (mqtt.getRes().equals("0")) {
                if (input.equals("factory")) {
                    dialog.dismiss();
                    finish();
                } else if (input.equals("vbsen")) {

                } else if (input.equals("sos")) {
                    dialog2.dismiss();
                } else if (input.equals("power")) {
                    dialog1.dismiss();
                } else if (input.equals("circle")) {
                    dialog3.dismiss();
                } else if (input.equals("mmlieage")) {
                    dialog4.dismiss();
                }
            }
        }
        if (output instanceof RespDeviceStatus) {
            status = (RespDeviceStatus) output;
            if (status.getRes().equals("0")) {
                if (!TextUtils.isEmpty(status.getData().getVbsen())) {
                    selectType = Integer.parseInt(status.getData().getVbsen());
                } else {
                    selectType = 3;
                }
                if (selectType == 1) {
                    buttonOne.setChecked(true);
                } else if (selectType == 2) {
                    buttonTwo.setChecked(true);
                } else if (selectType == 3) {
                    buttonThree.setChecked(true);
                } else {
                    buttonFour.setChecked(true);
                }
                selectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.radioOne) {
                            selectType = 1;
                        } else if (checkedId == R.id.radioTwo) {
                            selectType = 2;
                        } else if (checkedId == R.id.radioThree) {
                            selectType = 3;
                        } else {
                            selectType = 4;
                        }
                        CarReqUtils.vbsen(SettingActivity.this, SettingActivity.this, null, new Mqtt(), "vbsen", true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername())
                                        .putValue("type", selectType).createMap()));
                    }
                });

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sleep_layout:
                dialog1 = new DeviceSettingDialog(SettingActivity.this, 1);
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.show();
                break;
            case R.id.sos_layout:
                dialog2 = new DeviceSettingDialog(SettingActivity.this, 2);
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.show();
                break;
            case R.id.weilan_layout:
                dialog3 = new DeviceSettingDialog(SettingActivity.this, 3);
                dialog3.setCanceledOnTouchOutside(false);
                dialog3.show();
                break;
            case R.id.baoyang_layout:
                dialog4 = new DeviceSettingDialog(SettingActivity.this, 4);
                dialog4.setCanceledOnTouchOutside(false);
                dialog4.show();
                break;
        }
    }

    class DeviceSettingDialog extends AlertDialog {
        int index;
        View view = null;

        public DeviceSettingDialog(Context context, int index) {
            super(context);
            this.index = index;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_device_setting);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.device_setting_main_layout);
            TextView tvTitle= (TextView) findViewById(R.id.device_setting_title);
            if (index == 1) { //熄火后取电
                tvTitle.setText("电瓶取电时长");
                view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.shutdown_time, null);
                final EditText etTime = (EditText) view.findViewById(R.id.shut_down_time_edit);
                if (!TextUtils.isEmpty(status.getData().getHour())) {
                    etTime.setText(status.getData().getHour());
                    etTime.setSelection(etTime.getText().length());
                }
                findViewById(R.id.device_setting_dialog_ensure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CarReqUtils.power(SettingActivity.this, SettingActivity.this, null, new Mqtt(), "power", true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername())
                                        .putValue("hour", etTime.getText().toString()).createMap()));
                    }
                });
            } else if (index == 2) { //报警电话
                tvTitle.setText("报警电话设置");
                view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.alarm_phone, null);
                final EditText etOwnPhone, etfamilyPhoe, etFriendsPhone;
                etOwnPhone = (EditText) view.findViewById(R.id.alarm_phone_car_owner_phone);
                etfamilyPhoe = (EditText) view.findViewById(R.id.alarm_phone_family_phone);
                etFriendsPhone = (EditText) view.findViewById(R.id.alarm_phone_car_friend_phone);
                etOwnPhone.setText(status.getData().getSos1());
                etOwnPhone.setSelection(etOwnPhone.getText().length());
                etfamilyPhoe.setText(status.getData().getSos2());
                etFriendsPhone.setText(status.getData().getSos3());
                findViewById(R.id.device_setting_dialog_ensure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CarReqUtils.sos(SettingActivity.this, SettingActivity.this, null, new Mqtt(), "sos", true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername())
                                        .putValue("sos1", etOwnPhone.getText().toString())
                                        .putValue("sos2", etfamilyPhoe.getText().toString())
                                        .putValue("sos3", etFriendsPhone.getText().toString()).createMap()));
                    }
                });
            } else if (index == 3) { //电子围栏
                tvTitle.setText("电子围栏设置");
                view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dianzi_weilan, null);
                final EditText etLong = (EditText) view.findViewById(R.id.dianzi_weilan_edit);
                etLong.setText(status.getData().getCircle());
                etLong.setSelection(etLong.getText().length());
                findViewById(R.id.device_setting_dialog_ensure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CarReqUtils.circle(SettingActivity.this, SettingActivity.this, null, new Mqtt(), "circle", true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername())
                                        .putValue("mileage", etLong.getText().toString()).createMap()));
                    }
                });
            } else if (index == 4) { // 保养里程
                tvTitle.setText("保养里程设置");
                view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.take_care_mileage, null);
                final EditText etMileage= (EditText) view.findViewById(R.id.take_care_mileage_edit);
                etMileage.setText(status.getData().getMlieage());
                etMileage.setSelection(etMileage.getText().length());
                findViewById(R.id.device_setting_dialog_ensure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CarReqUtils.mmlieage(SettingActivity.this,SettingActivity.this,null,new Mqtt(),"mmlieage",true,
                                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                        putValue("mlieage",etMileage.getText().toString().trim()).createMap()));
                    }
                });
            }
            linearLayout.addView(view);
            findViewById(R.id.device_setting_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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
            if (index == 2) {
                textView.setText("恢复设备出厂设置，请确认！");
            }
            ViewGroup.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showLoadingDialog();
                    if (index == 2) {
                        CarReqUtils.factory(SettingActivity.this, SettingActivity.this, null, new Mqtt(), "factory", true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));
                    }

//                    if(index ==1){
//                        if(sleepCB.isChecked()){
//                            hour =etHour.getText().toString();
//                        }else{
//                            hour ="0";
//                        }
//
//                        String sos1,sos2,sos3;
//
//                        if(!TextUtils.isEmpty(etOwnPhone.getText().toString().trim())){
//                            sos1 =etOwnPhone.getText().toString();
//                        }else{
//                            sos1="";
//                        }
//
//                        if(!TextUtils.isEmpty(etfamilyPhoe.getText().toString().trim())){
//                            sos2 =etfamilyPhoe.getText().toString();
//                        }else{
//                            sos2="";
//                        }
//
//                        if(!TextUtils.isEmpty(etFriendsPhone.getText().toString().trim())){
//                            sos3 =etFriendsPhone.getText().toString();
//                        }else{
//                            sos3="";
//                        }
//
//                        CarReqUtils.setdevice(SettingActivity.this,SettingActivity.this,null,new Mqtt(),"setdevice",true,
//                                StringUrlUtils.geturl(new HashMapUtils().putValue("username",AppData.getInstance().getUserEntity().getUsername()).
//                                        putValue("mileage",etMilter.getText().toString()).
//                                        putValue("hour",hour).
//                                        putValue("vbsen",selectType).
//                                        putValue("sos1",sos1).
//                                        putValue("sos2",sos2).
//                                        putValue("sos3",sos3).createMap()));
//
//                    }else{
//
//
//                    }
//
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
}
