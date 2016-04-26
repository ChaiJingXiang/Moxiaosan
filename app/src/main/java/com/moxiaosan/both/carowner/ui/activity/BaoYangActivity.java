package com.moxiaosan.both.carowner.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.CarBrand;
import consumer.model.Mqtt;

/**
 * Created by chris on 16/4/17.
 */
public class BaoYangActivity extends BaseActivity implements IApiCallback{


    private EditText etKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.b_baoyang_km_layout);

        showActionBar(true);
        setActionBarName("保养里程");

        etKm =(EditText)findViewById(R.id.carKmId);

        findViewById(R.id.sureId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etKm.getText().toString().trim())){

                    showLoadingDialog();
                    CarReqUtils.mmlieage(BaoYangActivity.this,BaoYangActivity.this,null,new Mqtt(),"mmlieage",true,
                            StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                    putValue("mlieage",etKm.getText().toString().trim()).createMap()));
                }else{

                    EUtil.showToast("请输入保养里程");
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
            if (output instanceof Mqtt){
                Mqtt mqtt= (Mqtt) output;
                if (mqtt.getRes().equals("0")){

                    EUtil.showToast(mqtt.getErr());
                    finish();

                }
            }
        }else{
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }
    }
}
