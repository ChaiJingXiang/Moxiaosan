package com.moxiaosan.both.common.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moxiaosan.both.R;
import com.utils.ui.base.BaseActivity;

public class UseHelpActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_help);
        showActionBar(true);
        setActionBarName("使用帮助");

        findViewById(R.id.user_help_rules).setOnClickListener(this);
        findViewById(R.id.user_help_service).setOnClickListener(this);
        findViewById(R.id.user_help_question).setOnClickListener(this);
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_help_service:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
                builder.setTitle("客服信息"); //设置标题
                builder.setMessage("客服热线：400-8925-108"); //设置内容
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                    }
                });
                builder.create().show();
                break;
            case R.id.user_help_rules:
                Intent intent = new Intent(UseHelpActivity.this, WebViewActivity.class);
                intent.putExtra("title", "用户注册协议");
                intent.putExtra("webUrl", "file:///android_asset/agree.html");
                startActivity(intent);
                break;

            case R.id.user_help_question:
                startActivity(new Intent(UseHelpActivity.this, QuestionsActivity.class));
                break;
        }

    }
}
