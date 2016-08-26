package com.moxiaosan.both.common.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.utils.Security;
import com.utils.api.IApiCallback;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.Interface;
import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.PhoneCode;
import consumer.model.Register;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, IApiCallback {
    private RadioGroup radioGroup;
    private EditText etPhoneNum, etverifyCode, etPassword, etRePassWord;
    private int chooseType = 1;
    private static TextView tvCode;
    private static int time = 60;

    private String province = null;
    private String qu;

    private static String Huabei = "Huabei";
    private static String Dongbei = "Dongbei";
    private static String Huadong = "Huadong";
    private static String Huanan = "Huanan";
    private static String Xinan = "Xinan";
    private static String Xibei = "Xibei";
    private static String Huazhong = "Huazhong";

    private CheckBox checkBoxRule;
    private boolean isAgree = true; //是否同意协议

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        showActionBar(true);
        setActionBarName("注册");
        initView();

        SharedPreferences sp = getSharedPreferences("location", Activity.MODE_PRIVATE);
        province = sp.getString("province", "深圳");
        Log.i("info--==--", province);
//        UserReqUtil.reqNetData(this, iApiCallback, null, new Register(), "RegisterActivity", true, getUrl(Interface.REGISTER));

//        getQu();
//        Log.i("info--==--",qu);
    }

    private void initView() {
        findViewById(R.id.register_agree_rule_txt).setOnClickListener(this);
        checkBoxRule = (CheckBox) findViewById(R.id.register_agree_rule_chebox);
        checkBoxRule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAgree = true;
                } else {
                    isAgree = false;
                }
            }
        });

        etPhoneNum = (EditText) findViewById(R.id.register_phonenum_edit);
        etverifyCode = (EditText) findViewById(R.id.register_code_edit);
        etPassword = (EditText) findViewById(R.id.register_password_edit);
        etRePassWord = (EditText) findViewById(R.id.register_repassword_edit);
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etRePassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tvCode = (TextView) findViewById(R.id.register_get_code_txt);
        tvCode.setOnClickListener(this);
        findViewById(R.id.register_register_txt).setOnClickListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.register_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.register_radio_button_1) {
                    chooseType = 1;
                } else {
                    chooseType = 2;
                }
            }
        });
    }

    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (output == null) {
                return;
            }
            if (output instanceof Register) {
                dismissLoadingDialog();
                Register register = (Register) output;
                EUtil.showToast(register.getErr());
                if (register.getRes() == 0) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {

                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_get_code_txt:

                if (etPhoneNum.getText().toString().trim().length() == 11) {
                    UserReqUtil.getPhoneCode(this, this, null, new PhoneCode(), "Code", true, StringUrlUtils.geturl(hashMapUtils.putValue("username", etPhoneNum.getText().toString())
                            .putValue("type", 1).putValue("encryption", Security.encrypt(etPhoneNum.getText().toString(), etPhoneNum.getText().toString() + "12345")).createMap()));

                } else {
                    EUtil.showToast("请输入正确格式的手机号");
                }
                break;
            case R.id.register_register_txt:
                if (!TextUtils.isEmpty(etPhoneNum.getText().toString().trim()) && etPhoneNum.getText().toString().trim().length() == 11) {
                    if (!TextUtils.isEmpty(etverifyCode.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                            if (!TextUtils.isEmpty(etRePassWord.getText().toString().trim())) {
                                if (etPassword.getText().toString().trim().equals(etRePassWord.getText().toString().trim())) {
                                    if (isAgree) {
                                        //网络接口
                                        showLoadingDialog();
                                        UserReqUtil.reqNetData(this, iApiCallback, null, new Register(), "RegisterActivity", true, getUrl(Interface.REGISTER));
                                    } else {
                                        EUtil.showToast("未同意用户协议");
                                    }
                                } else {
                                    EUtil.showToast("两次输入密码不一致");
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
                } else {
                    EUtil.showToast("手机格式不正确");
                }
                break;
            case R.id.register_agree_rule_txt:
                startActivity(new Intent(RegisterActivity.this, WebViewActivity.class).putExtra("webUrl", "file:///android_asset/agree.html").putExtra("title", "用户协议"));
                break;
        }
    }

    @Override
    protected String getUrl(String nameUrl) {

        getQu();

        hashMapUtils.putValue("username", etPhoneNum.getText().toString())
                .putValue("password", etPassword.getText().toString())
                .putValue("type", chooseType)
                .putValue("code", etverifyCode.getText().toString())
                .putValue("region", qu);

        return StringUrlUtils.geturl(nameUrl, hashMapUtils.createMap());
    }

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
        } else {
            EUtil.showToast("网络错误，请稍后重试");
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
                    tvCode.setBackground(RegisterActivity.this.getResources().getDrawable(R.mipmap.get_verify_code));
                } else {
                    tvCode.setEnabled(true);
                    tvCode.setClickable(true);
                    tvCode.setText("重新获取");
                    tvCode.setBackground(RegisterActivity.this.getResources().getDrawable(R.mipmap.register_register_back));
                    handler.removeCallbacks(runnable);
                    time = 60;
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void getQu() {

        if ("北京、天津、河北省、山西省、内蒙古".contains(province)) {
            qu = Huabei;
        } else if ("山东省、江苏省、安徽省、浙江省、福建省、江西省、上海".contains(province)) {
            qu = Huadong;
        } else if ("湖北省、湖南省、河南省".contains(province)) {
            qu = Huazhong;
        } else if ("广东省、广西省、海南省".contains(province)) {
            qu = Huanan;
        } else if ("四川省、云南省、贵州省、西藏、重庆".contains(province)) {
            qu = Xinan;
        } else if ("宁夏、新疆、青海省、陕西省、甘肃省".contains(province)) {
            qu = Xibei;
        } else if ("辽宁省、吉林省、黑龙江省".contains(province)) {
            qu = Dongbei;
        }
    }

}
