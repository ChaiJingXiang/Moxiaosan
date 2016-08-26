package com.moxiaosan.both.common.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.utils.Security;
import com.utils.api.IApiCallback;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.PhoneCode;
import consumer.model.UpdatePassword;

public class ForgetPasswordActivity extends BaseActivity implements IApiCallback {

    private static TextView tvCode;

    private EditText etPhone, etSureCode, etNewPassword, etResurePassword;

    private static int time = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        showActionBar(true);
        setActionBarName("找回密码");
        initView();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void initView() {

        tvCode = (TextView) findViewById(R.id.forget_password_get_code_txt);
        tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送验证码
                if (etPhone.getText().toString().trim().length() == 11) {
                    UserReqUtil.getPhoneCode(ForgetPasswordActivity.this, ForgetPasswordActivity.this, null, new PhoneCode(), "Code", true, StringUrlUtils.geturl(hashMapUtils.putValue("username", etPhone.getText().toString())
                            .putValue("type", 3).putValue("encryption", Security.encrypt(etPhone.getText().toString(), etPhone.getText().toString() + "12345")).createMap()));

                } else {
                    EUtil.showToast("请输入正确格式的手机号");
                }
            }
        });

        etPhone = (EditText) findViewById(R.id.etPhoneId);
        etSureCode = (EditText) findViewById(R.id.sureCodeId);
        etNewPassword = (EditText) findViewById(R.id.newPasswordId);
        etResurePassword = (EditText) findViewById(R.id.resurePasswordId);
        etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etResurePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        findViewById(R.id.okId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etPhone.getText().toString()) && etPhone.getText().toString().length() == 11) {
                    //确认按钮
                    if (!TextUtils.isEmpty(etNewPassword.getText().toString().trim())) {

                        if (!TextUtils.isEmpty(etResurePassword.getText().toString().trim())) {
                            if (!TextUtils.isEmpty(etSureCode.getText().toString().trim())) {
                                showLoadingDialog();
                                UserReqUtil.updatePassword(ForgetPasswordActivity.this, ForgetPasswordActivity.this, null, new UpdatePassword(),
                                        "UpdatePassword", true, StringUrlUtils.geturl(hashMapUtils.putValue("username", etPhone.getText().toString())
                                                .putValue("password", etNewPassword.getText().toString()).putValue("code", etSureCode.getText().toString()).createMap()));
                            } else {
                                EUtil.showToast("验证码不能为空");
                            }
                        } else {
                            EUtil.showToast("请输入新密码");
                        }
                    } else {

                        EUtil.showToast("请输入新密码");
                    }
                } else {
                    EUtil.showToast("手机号格式不正确");
                }
            }
        });

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
        if (output != null) {
            if (output instanceof PhoneCode) {
                PhoneCode code = (PhoneCode) output;
                EUtil.showToast(code.getErr());
                if (code.getRes() == 0) {
                    tvCode.setText("已发送");

                    tvCode.setEnabled(false);
                    tvCode.setClickable(false);
                    //60s
                    handler.postDelayed(runnable, 1000);

                }
            }

            if (output instanceof UpdatePassword) {
                dismissLoadingDialog();
                UpdatePassword updatePassword = (UpdatePassword) output;
                EUtil.showToast(updatePassword.getErr());
                if (updatePassword.getRes() == 0) {

                    finish();
                }

            }
        } else {
            EUtil.showToast("网络错误，请稍后重试");
        }

    }
}
