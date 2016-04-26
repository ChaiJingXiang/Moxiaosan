package com.moxiaosan.both.common.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.ModifPhone;
import consumer.model.PhoneCode;

public class ModifyPhoneActivity extends BaseActivity implements View.OnClickListener,IApiCallback {
    private EditText etPhoneNum, etVerifyCode;
    private static int time =60;
    private static TextView tvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        showActionBar(true);
        setActionBarName("修改手机");
        etPhoneNum = (EditText) findViewById(R.id.modify_phone_phonenum_edit);
        etVerifyCode = (EditText) findViewById(R.id.modify_phone_verify_code_edit);
        tvCode =(TextView)findViewById(R.id.modify_phone_get_code_txt);
        tvCode.setOnClickListener(this);
        findViewById(R.id.modify_phone_ensure_txt).setOnClickListener(this);
        etPhoneNum.setText(getIntent().getStringExtra("phone"));
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modify_phone_get_code_txt:

                if(etPhoneNum.getText().toString().trim().length() ==11){
                    UserReqUtil.getPhoneCode(this,this,null,new PhoneCode(),"Code",true,StringUrlUtils.geturl(hashMapUtils.putValue("username",etPhoneNum.getText().toString())
                            .putValue("type",2).createMap()));

                }else{
                    EUtil.showToast("请输入正确格式的手机号");
                }
            break;
            case R.id.modify_phone_ensure_txt:
                if (!TextUtils.isEmpty(etPhoneNum.getText().toString().trim())){
                    if (!TextUtils.isEmpty(etVerifyCode.getText().toString().trim()) && etVerifyCode.getText().toString().trim().length() == 4){
                        showLoadingDialog();
                        UserReqUtil.updatetel(ModifyPhoneActivity.this, ModifyPhoneActivity.this, null, new ModifPhone(), "ModifPhone", true,
                                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                        putValue("tel",etPhoneNum.getText().toString())
                                .putValue("code", etVerifyCode.getText().toString()).createMap()));

                    }else {
                        EUtil.showToast("验证码不能为空");
                    }
                }else {
                    EUtil.showToast("手机号码格式不正确");
                }
                break;
        }
    }

    //handler定时器postdelayed
    static Handler handler = new Handler();
    static Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                if (time > 0) {
                    handler.postDelayed(this, 1000);
                    tvCode.setText(Integer.toString(time--) + "s");
                    tvCode.setClickable(false);
                } else {
                    tvCode.setEnabled(true);
                    tvCode.setClickable(true);
                    tvCode.setText("重新获取");
                    handler.removeCallbacks(runnable);
                    time = 60;
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onData(Object output, Object input) {

        if(output!=null){
            if (output instanceof PhoneCode){
                PhoneCode code= (PhoneCode) output;
                EUtil.showToast(code.getErr());
                if (code.getRes() == 0){
                    tvCode.setText("已发送");

                    tvCode.setEnabled(false);
                    tvCode.setClickable(false);
                    //60s
                    handler.postDelayed(runnable, 1000);

                }
            }

            if(output instanceof ModifPhone){
                dismissLoadingDialog();
                ModifPhone phone =(ModifPhone)output;
                EUtil.showToast(phone.getErr());
                if(phone.getRes() ==0){
                    finish();
                }

            }
        }else{
            EUtil.showToast("网络错误，请稍后重试");
        }

    }
}
