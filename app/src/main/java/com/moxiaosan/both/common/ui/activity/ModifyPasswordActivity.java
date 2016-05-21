package com.moxiaosan.both.common.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.ActivityHolder;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.PhoneCode;
import consumer.model.UpdatePassword;
import consumer.model.obj.RespUserInfo;

public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener, IApiCallback {
    private EditText etPhoneNum, etVerifyCode, etPassword, etRepassword;
    private static int time = 60;
    private static TextView tvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        showActionBar(true);
        setActionBarName("修改密码");
        etPhoneNum = (EditText) findViewById(R.id.etPhoneNumId);
        etVerifyCode = (EditText) findViewById(R.id.modify_password_code_edit);
        etPassword = (EditText) findViewById(R.id.modify_password_password_edit);
        etRepassword = (EditText) findViewById(R.id.modify_password_repassword_edit);
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etRepassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tvCode = (TextView) findViewById(R.id.modify_password_get_code_txt);
        tvCode.setOnClickListener(this);
        findViewById(R.id.modify_password_ensure_txt).setOnClickListener(this);

        etPhoneNum.setText(AppData.getInstance().getUserEntity().getUsername());

    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_password_get_code_txt:
                if (etPhoneNum.getText().toString().trim().length() == 11) {
                    UserReqUtil.getPhoneCode(this, this, null, new PhoneCode(), "Code", true, StringUrlUtils.geturl(hashMapUtils.putValue("username", etPhoneNum.getText().toString())
                            .putValue("type", 2).createMap()));

                } else {
                    EUtil.showToast("请输入正确格式的手机号");
                }
                break;
            case R.id.modify_password_ensure_txt:
                if (!TextUtils.isEmpty(etVerifyCode.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(etRepassword.getText().toString().trim())) {
                            if (!etPassword.getText().toString().trim().equals(etRepassword.getText().toString().trim())) {
                                EUtil.showToast("两次输入密码不一致");
                            } else {
                                //网络接口
                                showLoadingDialog();
                                UserReqUtil.updatePassword(this, this, null, new UpdatePassword(),
                                        "UpdatePassword", true, StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername())
                                                .putValue("password", etPassword.getText().toString()).putValue("code", etVerifyCode.getText().toString()).createMap()));


                            }
                        } else {
                            EUtil.showToast("重复密码不能为空");
                        }
                    } else {
                        EUtil.showToast("密码不能为空");
                    }
                } else {
                    EUtil.showToast("验证码不能为空");
                }
                break;
        }
    }

    //handler定时器postdelayed
    static Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @SuppressLint("NewApi")
        @Override
        public void run() {

            try {
                if (time > 0) {
                    handler.postDelayed(this, 1000);
                    tvCode.setText(Integer.toString(time--) + "s");
                    tvCode.setClickable(false);
                    tvCode.setBackground(ModifyPasswordActivity.this.getResources().getDrawable(R.mipmap.get_verify_code));
                } else {
                    tvCode.setEnabled(true);
                    tvCode.setClickable(true);
                    tvCode.setText("重新获取");
                    tvCode.setBackground(ModifyPasswordActivity.this.getResources().getDrawable(R.mipmap.register_register_back));
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
                UpdatePassword password = (UpdatePassword) output;
                EUtil.showToast(password.getErr());
                if (password.getRes() == 0) {
                    ActivityHolder.getInstance().finishAllActivity();
                    RespUserInfo respUserInfo = null;
                    AppData.getInstance().saveUserEntity(respUserInfo);
                    startActivity(new Intent(this, LoginActivity.class));
                }
            }

        } else {
            EUtil.showToast("网络错误，请稍后重试");
        }

    }
}
