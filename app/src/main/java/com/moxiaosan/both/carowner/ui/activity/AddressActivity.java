package com.moxiaosan.both.carowner.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.UpdateAddress;

/**
 * Created by chris on 16/3/3.
 */
public class AddressActivity extends BaseActivity implements IApiCallback{

    private EditText etProvice,etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_address_layout);

        showActionBar(true);
        setActionBarName("填写地址");

        etProvice =(EditText)findViewById(R.id.modify_proviceId);
        etAddress =(EditText)findViewById(R.id.modify_address);

        findViewById(R.id.sureId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etProvice.getText().toString().trim())){

                    if(!TextUtils.isEmpty(etAddress.getText().toString().trim())){

                        showLoadingDialog();

                        CarReqUtils.updateAddress(AddressActivity.this,AddressActivity.this,null,new UpdateAddress(),"UpdateAddress",true,
                                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                        putValue("province",etProvice.getText().toString()).putValue("address",etAddress.getText().toString()).createMap()));

                    }else{
                        EUtil.showToast("地址不能为空");
                    }
                }else{
                    EUtil.showToast("省份不能为空");
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
            if(output instanceof UpdateAddress){
                dismissLoadingDialog();
                UpdateAddress address =(UpdateAddress)output;
                EUtil.showToast(address.getErr());
                if(address.getRes()==0){
                    finish();
                }
            }
        }else{
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }

    }
}
