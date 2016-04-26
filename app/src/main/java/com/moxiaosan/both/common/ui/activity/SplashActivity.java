package com.moxiaosan.both.common.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.BusinessMainActivity;
import com.moxiaosan.both.carowner.ui.activity.GPSSafeCenterActivity;
import com.moxiaosan.both.consumer.ui.activity.ConsumerMainActivity;
import com.utils.common.AppData;
import com.utils.ui.base.BaseActivity;

import consumer.model.obj.RespUserInfo;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                if (AppData.getInstance().getUserEntity() != null) {
                    if(AppData.getInstance().getUserEntity().getUserType()==1){
                        startActivity(new Intent(SplashActivity.this, ConsumerMainActivity.class));
                        SplashActivity.this.finish();
                    }else if(AppData.getInstance().getUserEntity().getUserType()==3){
                        startActivity(new Intent(SplashActivity.this, BusinessMainActivity.class));
                        SplashActivity.this.finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, GPSSafeCenterActivity.class));
                        SplashActivity.this.finish();
                    }

                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }
}
