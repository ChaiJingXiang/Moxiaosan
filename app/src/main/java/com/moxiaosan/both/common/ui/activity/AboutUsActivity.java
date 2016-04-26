package com.moxiaosan.both.common.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.ui.base.BaseActivity;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        showActionBar(true);
        setActionBarName("关于我们");

        tvVersionName = (TextView) findViewById(R.id.versionNameId);
        try {
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            tvVersionName.setText("V " + packInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        findViewById(R.id.about_us_official).setOnClickListener(this);
        findViewById(R.id.about_us_weixin).setOnClickListener(this);
        findViewById(R.id.about_us_weibo).setOnClickListener(this);

    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void openLocalBrowser(String url) {
        //urlText是一个文本输入框，输入网站地址
        //Uri  是统一资源标识符
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_us_official:
                openLocalBrowser("www.moxiaosan.com");
                break;
            case R.id.about_us_weixin:
                openLocalBrowser("https://www.baidu.com");
                break;
            case R.id.about_us_weibo:
                openLocalBrowser("https://www.baidu.com");
                break;
        }
    }
}
