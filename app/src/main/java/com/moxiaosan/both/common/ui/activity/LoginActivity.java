package com.moxiaosan.both.common.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.BusinessMainActivity;
import com.moxiaosan.both.carowner.ui.activity.GPSSafeCenterActivity;
import com.moxiaosan.both.consumer.ui.activity.ConsumerMainActivity;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.HashMapUtils;
import consumer.Interface;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.api.UserReqUtil;
import consumer.model.BindDevice;
import consumer.model.Login;
import consumer.model.obj.RespUserInfo;

public class LoginActivity extends BaseActivity implements View.OnClickListener, IApiCallback {
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected String getUrl(String nameUrl) {

        hashMapUtils.putValue("username", etUsername.getText().toString())
                .putValue("password", etPassword.getText().toString());
//                .putValue("type", AppData.getInstance().getUserEntity().getType());

        return StringUrlUtils.geturl(nameUrl, hashMapUtils.createMap());
    }

    private void initView() {
        etUsername = (EditText) findViewById(R.id.login_username_et);
        etPassword = (EditText) findViewById(R.id.login_password_et);
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        findViewById(R.id.login_login_txt).setOnClickListener(this);
        findViewById(R.id.login_forget_password_txt).setOnClickListener(this);
        findViewById(R.id.login_register_txt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_txt:
                if (!TextUtils.isEmpty(etUsername.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                        //网络接口
                        showLoadingDialog();
                        UserReqUtil.reqNetData(this, this, null, new Login(), "Login", true, getUrl(Interface.LOGIN));

                    } else {
                        EUtil.showToast("密码不能为空");
                    }
                } else {
                    EUtil.showToast("账户名不能为空");
                }
                break;
            case R.id.login_forget_password_txt:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.login_register_txt:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    @Override
    public void onData(Object output, Object input) {

        if (output != null) {
            dismissLoadingDialog();
            if (output instanceof Login) {
                dismissLoadingDialog();
                Login login = (Login) output;
                EUtil.showToast(login.getErr());
                if (login.getRes() == 0) {
                    AppData.getInstance().saveUserEntity(login.getData());
                    if (login.getData().getType() == 1) {
                        startActivity(new Intent(LoginActivity.this, ConsumerMainActivity.class));
                    } else if (login.getData().getType() == 3) {
                        startActivity(new Intent(LoginActivity.this, BusinessMainActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, GPSSafeCenterActivity.class));
                    }

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setUserType(AppData.getInstance().getUserEntity().getType());
                    AppData.getInstance().saveUserEntity(userInfo);


                    if (login.getData().getAppstatus().equals("1") || login.getData().getAppstatus().equals("4")) {
                        SharedPreferences sp = getSharedPreferences("request", Activity.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("carer", false);
                        editor.commit();

                    }

                    CarReqUtils.checkdeviced(this, this, null, new BindDevice(), "checkdeviced", true, StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

                } else {
                    dismissLoadingDialog();
                    EUtil.showToast(login.getErr());
                }
            }

            if (output instanceof BindDevice) {
                BindDevice device = (BindDevice) output;

                if (device.getRes().equals("0")) {

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setBind(1);
                    AppData.getInstance().saveUserEntity(userInfo);

                } else {

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setBind(2);
                    AppData.getInstance().saveUserEntity(userInfo);

                }

                finish();

            }

        } else {
            dismissLoadingDialog();
            EUtil.showToast("用户名或密码错误");
            return;

        }
    }
}
