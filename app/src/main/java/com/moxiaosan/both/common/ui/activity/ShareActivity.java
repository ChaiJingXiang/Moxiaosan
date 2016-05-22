package com.moxiaosan.both.common.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.moxiaosan.both.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMSsoHandler;
import com.utils.common.EUtil;
import com.utils.share.ShareManager;

import java.io.File;

public class ShareActivity extends Activity implements View.OnClickListener {
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    private ShareListener shareListener = new ShareListener();
    private ImageView imgWX, imgQQ, imgSina, imgWXCircle, imgMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须设置为NOTILE
        setContentView(R.layout.activity_share);
        this.setFinishOnTouchOutside(true);
        initView();
//        initLocation();
        initAnimation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = dm.widthPixels;
        getWindow().setAttributes(params);
    }

    private void initView() {
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        UMImage localImage = null;
        if (getIntent().getStringExtra("imgPath").equals("app_log")) {
            localImage = new UMImage(this, R.mipmap.ic_launcher);
        } else {
            localImage = new UMImage(this, getIntent().getStringExtra("imgPath"));
        }
        String targetUrl = getIntent().getStringExtra("targetUrl");
        //设置分享内容
        ShareManager.getInstance(ShareActivity.this).share(title, content, localImage, targetUrl);

        imgWX = (ImageView) findViewById(R.id.share_weixin_img);
        imgWX.setOnClickListener(this);
        imgWXCircle = (ImageView) findViewById(R.id.share_weixin_circle_img);
        imgWXCircle.setOnClickListener(this);
        imgQQ = (ImageView) findViewById(R.id.share_qq_img);
        imgQQ.setOnClickListener(this);
        imgSina = (ImageView) findViewById(R.id.share_weibo_img);
        imgSina.setOnClickListener(this);
        imgMore = (ImageView) findViewById(R.id.share_more_img);
        imgMore.setOnClickListener(this);
        findViewById(R.id.share_cancel_txt).setOnClickListener(this);
    }

    /**
     * 初始化分享弹出框的动画效果
     */
    public void initAnimation() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        if (activityStyle != null) {
            activityStyle.recycle();
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = ShareManager.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void performShare(SHARE_MEDIA platform) {
        ShareManager.mController.postShare(ShareActivity.this, platform, shareListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_weixin_img:
                performShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_weixin_circle_img:
                performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_qq_img:
                performShare(SHARE_MEDIA.QQ);
                break;
            case R.id.share_weibo_img:
                if (ShareManager.getInstance(ShareActivity.this).isSinaInstalled()) {
                    performShare(SHARE_MEDIA.SINA);
                } else {
                    EUtil.showToast("你还没有安装微博");
                }
                break;
            case R.id.share_more_img:

                break;
            case R.id.share_cancel_txt:
                finish();
                break;
        }
    }

    private class ShareListener implements SocializeListeners.SnsPostListener {

        @Override
        public void onStart() {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
            String showText = platform.toString();
            if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                showText = "分享成功";
                EUtil.showToast(showText);
                if (!getIntent().getStringExtra("imgPath").equals("app_log")){
                    File file=new File(getIntent().getStringExtra("imgPath"));
                    file.delete();
                }
                finish();
            } else if (eCode == StatusCode.ST_CODE_ERROR_CANCEL) {

            } else {
                showText = "分享失败";
                EUtil.showToast(showText);
            }

            ShareManager.mController.getConfig().cleanListeners();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareManager.mController.getConfig().cleanListeners();
    }
}
