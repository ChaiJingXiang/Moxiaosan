package com.moxiaosan.both.common.ui.activity;

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
        Intent intent=new Intent(UseHelpActivity.this,WebViewActivity.class);
        switch (v.getId()){
            case R.id.user_help_rules:
                intent.putExtra("title","法律条款");
                intent.putExtra("webUrl","https://www.baidu.com");
                break;
            case R.id.user_help_service:
                intent.putExtra("title","客服信息");
                intent.putExtra("webUrl","https://www.baidu.com");
                break;
            case R.id.user_help_question:
                intent.putExtra("title","常见问题");
                intent.putExtra("webUrl","https://www.baidu.com");
                break;
        }
        startActivity(intent);
    }
}
