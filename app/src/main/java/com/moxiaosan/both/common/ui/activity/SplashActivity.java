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
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.http.EHttpAgent;
import com.utils.ui.base.BaseActivity;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.BindDevice;
import consumer.model.obj.RespUserInfo;

public class SplashActivity extends BaseActivity implements IApiCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);

        if(AppData.getInstance().getUserEntity() != null){
//            if (!EHttpAgent.isAvailable(this)){
//                EUtil.showLongToast("请开启网络后重启进入！");
//            }
            CarReqUtils.checkdeviced(this,this,null,new BindDevice(),"checkdeviced",true, StringUrlUtils.geturl(new HashMapUtils().putValue("username",AppData.getInstance().getUserEntity().getUsername()).createMap()));

        }else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            SplashActivity.this.finish();
        }

        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {

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

    @Override
    public void onData(Object output, Object input) {
        if(output !=null){
            if(output instanceof BindDevice){
                BindDevice device =(BindDevice)output;

                if(device.getRes().equals("0")){

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setBind(1);
                    AppData.getInstance().saveUserEntity(userInfo);

                }else{

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setBind(2);
                    AppData.getInstance().saveUserEntity(userInfo);

                }

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

                }

            }else{
                startActivity(new Intent(SplashActivity.this, ConsumerMainActivity.class));
                SplashActivity.this.finish();

            }
        }else{
            startActivity(new Intent(SplashActivity.this, ConsumerMainActivity.class));
            SplashActivity.this.finish();

        }
    }
}
