package com.moxiaosan.both.common.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.GPSSafeCenterActivity;
import com.moxiaosan.both.consumer.ui.activity.ConsumerMainActivity;
import com.moxiaosan.both.zxing.MipcaActivityCapture;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.ActivityHolder;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.AddEquipment;
import consumer.model.PhoneCode;
import consumer.model.obj.RespUserInfo;

/**
 * Created by chris on 16/3/2.
 */
public class AddGPSPhoneActivity extends BaseActivity implements IApiCallback{

    private EditText etIMEA,etSIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_addgpsphone_layout);

        showActionBar(true);
        setActionBarName("添加设备");

        etIMEA =(EditText)findViewById(R.id.etIMEI);
        etSIM =(EditText)findViewById(R.id.etSIM);

        findViewById(R.id.imgCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddGPSPhoneActivity.this, MipcaActivityCapture.class);
                startActivityForResult(intent, 1);

            }
        });

        findViewById(R.id.sureId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etIMEA.getText().toString().trim())){

                    if(!TextUtils.isEmpty(etSIM.getText().toString().trim())){

                        CarReqUtils.addEquipment(AddGPSPhoneActivity.this,AddGPSPhoneActivity.this,null,new AddEquipment(),"AddEquipment",true,
                                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("equipmentid",etIMEA.getText().toString().trim())
                                .putValue("sim",etSIM.getText().toString().trim()).createMap()));
                    }else{

                        EUtil.showToast("请输入设备SIM卡号");

                    }
                }else{
                    EUtil.showToast("请输入IMEI号");
                }
            }
        });
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){
            if(data !=null){

                Bundle bundle =data.getBundleExtra("bundle");
                Log.i("info+++",bundle.getString("IMEA").toString());
                String str =bundle.getString("IMEA");
                Log.i("info+++",str);
                etIMEA.setText(str);

            }
        }
    }

    @Override
    public void onData(Object output, Object input) {

        if(output!=null){
            if (output instanceof AddEquipment){
                AddEquipment equipment= (AddEquipment) output;
                EUtil.showToast(equipment.getErr());
                if (equipment.getRes() == 0){
                    if(AppData.getInstance().getUserEntity().getUserType()==1){
                        startActivity(new Intent(AddGPSPhoneActivity.this, GPSSafeCenterActivity.class));
                        RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                        userInfo.setType(2);
                        userInfo.setUserType(2);
                        AppData.getInstance().saveUserEntity(userInfo);
                        finish();
                        ActivityHolder.getInstance().pop(new ConsumerMainActivity());


                    }else{

                        finish();
                    }

                }
            }
        }else{
            EUtil.showToast("网络错误，请稍后重试");
        }

    }
}
