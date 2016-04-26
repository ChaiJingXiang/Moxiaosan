package com.moxiaosan.both.carowner.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
public class SettingActivity extends BaseActivity implements IApiCallback{

    private RadioGroup selectGroup;
    private CheckBox sleepCB,factoryCB;
    private EditText etOwnPhone,etfamilyPhoe,etFriendsPhone;
    private int selectType;
    private EditText etHour,etMilter;
    private ExitDialog dialog,dialog2;
    private String hour =null;
    private RadioButton buttonOne,buttonTwo,buttonThree,buttonFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_setting_layout);
        showActionBar(true);
        setActionBarName("设备设置");

        showLoadingDialog();
        CarReqUtils.getdeviceinfo(SettingActivity.this,SettingActivity.this,null,new RespDeviceStatus(),"getdeviceinfo",true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username",AppData.getInstance().getUserEntity().getUsername()).createMap()));


        etHour =(EditText)findViewById(R.id.hourId);
        etMilter =(EditText)findViewById(R.id.mileterId);

        selectGroup =(RadioGroup)findViewById(R.id.selectRadioGroup);
        sleepCB =(CheckBox)findViewById(R.id.sleep_checkbox);
        factoryCB =(CheckBox)findViewById(R.id.factory_checkbox);

        etOwnPhone =(EditText)findViewById(R.id.chezhuPhoneId);
        etfamilyPhoe =(EditText)findViewById(R.id.jiarenPhoneId);
        etFriendsPhone =(EditText)findViewById(R.id.pengyouPhoneId);

        etOwnPhone.setText(AppData.getInstance().getUserEntity().getUsername());

        buttonOne =(RadioButton)findViewById(R.id.radioOne);
        buttonTwo =(RadioButton)findViewById(R.id.radioTwo);
        buttonThree =(RadioButton)findViewById(R.id.radioThree);
        buttonFour =(RadioButton)findViewById(R.id.radioFour);

        selectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId ==R.id.radioOne){

                    selectType =1;
                }else if(checkedId ==R.id.radioTwo){
                    selectType =2;

                }else if(checkedId ==R.id.radioThree){
                    selectType =3;
                }else{
                    selectType =4;
                }
            }
        });

        sleepCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    hour =etHour.getText().toString();
                }else{
                    hour ="0";
                    etHour.setText(0+"");
                }

            }
        });

        factoryCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    dialog2 = new ExitDialog(SettingActivity.this,2);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.show();
                }
            }
        });

        getActionBarRightTXT("确定").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(!TextUtils.isEmpty(etHour.getText().toString().trim())){

                        if(!TextUtils.isEmpty(etMilter.getText().toString().trim())){
                            dialog = new ExitDialog(SettingActivity.this,1);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                        }else{
                            EUtil.showToast("设置电子围栏");
                        }
                    }else{

                        EUtil.showToast("设置取电时长");
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

        if(output!=null){
            dismissLoadingDialog();
            if(output instanceof RespDeviceStatus){
                RespDeviceStatus status =(RespDeviceStatus)output;
                if(status.getRes().equals("0")){

                    etHour.setText(status.getData().getHour());
                    etOwnPhone.setText(status.getData().getSos1());
                    etOwnPhone.setText(status.getData().getSos2());
                    etOwnPhone.setText(status.getData().getSos3());
                    etMilter.setText(status.getData().getCircle());
                    if(status.getData().getHour().equals("0")){
                        sleepCB.setChecked(false);
                    }

                    selectType =Integer.parseInt(status.getData().getVbsen());

                    if(selectType ==1){
                        buttonOne.setChecked(true);
                    }else if(selectType ==2){
                        buttonTwo.setChecked(true);
                    }else if(selectType ==3){
                        buttonThree.setChecked(true);
                    }else{
                        buttonFour.setChecked(true);
                    }
                }

            }

            if(output instanceof Mqtt){
               Mqtt mqtt =(Mqtt)output;
                if(mqtt.getRes().equals("0")){
                    if(input.equals("factory")){
                        EUtil.showToast(mqtt.getErr());
                        dialog2.dismiss();
                        finish();
                    }else{
                        dialog.dismiss();
                        EUtil.showToast(mqtt.getErr());
                        finish();
                    }

                }else{
                    EUtil.showToast(mqtt.getErr());
                }
            }

        }else{
            dismissLoadingDialog();
        }
    }

    // dialog
    class ExitDialog extends AlertDialog {

        int index;

        public ExitDialog(Context context,int index) {
            super(context);
            this.index =index;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);

            TextView textView =(TextView)findViewById(R.id.tvDialogActivity);

            if(index ==1){
                textView.setText("修改设备设置，请确认！");
            }else{
                textView.setText("恢复设备出厂设置，请确认！");
            }

            ViewGroup.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showLoadingDialog();

                    if(index ==1){
                        hour =etHour.getText().toString();
                        String sos1,sos2,sos3;

                        if(!TextUtils.isEmpty(etOwnPhone.getText().toString().trim())){
                            sos1 =etOwnPhone.getText().toString();
                        }else{
                            sos1="";
                        }

                        if(!TextUtils.isEmpty(etfamilyPhoe.getText().toString().trim())){
                            sos2 =etfamilyPhoe.getText().toString();
                        }else{
                            sos2="";
                        }

                        if(!TextUtils.isEmpty(etFriendsPhone.getText().toString().trim())){
                            sos3 =etFriendsPhone.getText().toString();
                        }else{
                            sos3="";
                        }

                        CarReqUtils.setdevice(SettingActivity.this,SettingActivity.this,null,new Mqtt(),"setdevice",true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username",AppData.getInstance().getUserEntity().getUsername()).
                                        putValue("mileage",etMilter.getText().toString()).
                                        putValue("hour",hour).
                                        putValue("vbsen",selectType).
                                        putValue("sos1",sos1).
                                        putValue("sos2",sos2).
                                        putValue("sos3",sos3).createMap()));

                    }else{

                        CarReqUtils.factory(SettingActivity.this,SettingActivity.this,null,new Mqtt(),"factory",true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username",AppData.getInstance().getUserEntity().getUsername()).createMap()));

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
}
