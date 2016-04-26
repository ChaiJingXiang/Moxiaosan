package com.moxiaosan.both.common.ui.activity;

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
import consumer.api.UserReqUtil;
import consumer.model.UpdateName;

public class ModifyNickNameActivity extends BaseActivity implements View.OnClickListener,IApiCallback{
    private EditText etNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nick_name);
        showActionBar(true);
        setActionBarName("修改昵称");
        etNickName = (EditText) findViewById(R.id.modify_nick_name_edit);
        etNickName.setText(getIntent().getStringExtra("nickname"));
        findViewById(R.id.modify_nick_name_ensure).setOnClickListener(this);
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_nick_name_ensure:
                if (!TextUtils.isEmpty(etNickName.getText().toString().trim())) {

                    showLoadingDialog();
                    UserReqUtil.updatename(this,this,null,new UpdateName(),"updatename",true, StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername())
                            .putValue("nickname",etNickName.getText().toString()).createMap()));

                } else {
                    EUtil.showToast("昵称不能为空");
                }
                break;
        }
    }

    @Override
    public void onData(Object output, Object input) {
        if (output != null) {
            if (output instanceof UpdateName) {
                dismissLoadingDialog();

                UpdateName updateName = (UpdateName) output;
                EUtil.showToast(updateName.getErr());
                if (updateName.getRes() == 0) {
                    finish();
                }
            }
        }else{
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }
    }
}
