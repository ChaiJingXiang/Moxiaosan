package com.utils.share;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.utils.api.IApiCallback;

/**
 * Created by fengyongqiang on 15-9-28.
 * Email:850513335@qq.com
 * 分享功能公共模块
 */
public class ShareManager {
    private static ShareManager shareManager;
    private Activity mActivity;
    public final static UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

    private ShareManager(Activity activity) {
        this.mActivity = activity;
    }

    public static ShareManager getInstance(Activity activity) {
        if (shareManager == null) {
            shareManager = new ShareManager(activity);
        }
        return shareManager;
    }

    public void init(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 如果需要第三方登录则需第三方授权
     */
    public void doOauthVerify(SHARE_MEDIA shareMedia, SocializeListeners.UMAuthListener umAuthListener) {
        if (!OauthHelper.isAuthenticated(mActivity, shareMedia)) {
            UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

            SocializeConfig config = mController.getConfig();
            config.setSsoHandler(new SinaSsoHandler());

            mController.doOauthVerify(mActivity, shareMedia, umAuthListener);
        } else {
            umAuthListener.onComplete(null, shareMedia);
        }
    }

    //检测QQ是否安装
    public boolean isQQInstalled() {
        if (mController.getConfig().getSsoHandler(HandlerRequestCode.QQ_REQUEST_CODE).isClientInstalled()) {
            return true;
        } else {
            return false;
        }
    }

    //检测微信是否安装
    public boolean isWeixinInstalled() {
        if (mController.getConfig().getSsoHandler(HandlerRequestCode.WX_REQUEST_CODE).isClientInstalled()) {
            return true;
        } else {
            return false;
        }
    }

    //检测新浪微博是否安装
    public boolean isSinaInstalled() {
        if (isAvilible(mActivity, "com.sina.weibo")) {
            return true;
        } else {
            return false;
        }
    }

    //检测是否安装微信（用包名检测）

    public boolean isWeixin() {
        if (isAvilible(shareManager.mActivity, "com.tencent.mm")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 分享活动
     */
//    public void shareActivity(RespActiveDetail respActiveDetail) {
//        UMImage urlImage = new UMImage(mActivity, respActiveDetail.getImage().getImageUrl());
//        String targetUrl = respActiveDetail.getShareUrl();
//        if (TextUtils.isEmpty(respActiveDetail.getTimeDescribe())) {
//            String strContent = null;
//            if (respActiveDetail.getMultiple() == 1) { //多场次
//                strContent = "时间： " + DateTimeFormateUtil.getMonthDay(respActiveDetail.getStartTime(), respActiveDetail.getEndTime());
//            } else { //单场次
//                strContent = "时间： " + DateTimeFormateUtil.getShareTime(respActiveDetail.getStartTime(), respActiveDetail.getEndTime());
//            }
//            share(respActiveDetail.getTitle(), strContent, urlImage, targetUrl);
//        } else {
//            share(respActiveDetail.getTitle(), respActiveDetail.getTimeDescribe(), urlImage, targetUrl);
//        }
//    }

    /**
     * 分享APP
     */
    public void shareApp(int mipmapId) {
        UMImage localImage = new UMImage(mActivity, mipmapId);
        String content = "农村移动互联网众包服务第一平台，同城速递，农村顺风车，找劳力，卖农货，请用摩小三。详情访问:http://www.moxiaosan.com";
        String title = "推荐应用摩小三给你";
        String targetUrl = "http://www.moxiaosan.com";
//        if (defaultConfig != null) {
//            targetUrl = defaultConfig.getData().getRespAndroidDefaultConfig().getUserDownloadUrl();
//        }
        share(title, content, localImage, targetUrl);
    }



    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {

        }
    };

    /**
     * 分享
     * targetUrl此参数可以不使用  根据需求设置不同的url
     */
    private void share(String title, String content, UMImage umImage, String targetUrl) {
        if (!TextUtils.isEmpty(content)) {
            if (content.length() > 120) {
                content = content.substring(0, 120);
            }
        }

        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        //关闭Toast
        mController.getConfig().closeToast();

        // 添加QQ支持, 并且设置QQ分享内容的target url
        String appIdQQ = "1105141200";
        String appKey = "42LT3D9nuprSrqnN";
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, appIdQQ, appKey);
        qqSsoHandler.addToSocialSDK();

        String appIdWX = "wx638d7fe3ae70df04";
        String appSecret = "1e476444b1d74a961cc893e289e77b7e";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mActivity, appIdWX, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appIdWX, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //设置QQ分享内容
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        qqShareContent.setShareImage(umImage);
        qqShareContent.setTargetUrl(targetUrl);
        mController.setShareMedia(qqShareContent);

        //设置微信分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(targetUrl);
        weixinContent.setShareImage(umImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareImage(umImage);
        circleMedia.setTargetUrl(targetUrl);
        mController.setShareMedia(circleMedia);

        //新浪
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(title + content + targetUrl);
        sinaContent.setTitle(title);
        sinaContent.setShareImage(umImage);
        sinaContent.setTargetUrl(targetUrl);
        mController.setShareMedia(sinaContent);
    }

//    /**
//     * 判断手机已安装某程序的方法
//     * @param context
//     * @param packageName
//     * @return
//     */
//    public static boolean isAvilible(Context context, String packageName){
//        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
//        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
//        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
//        //从pinfo中将包名字逐一取出，压入pName list中
//        if(pinfo != null){
//            for(int i = 0; i < pinfo.size(); i++){
//                String pn = pinfo.get(i).packageName;
//                //Log.i("ShareManager","包名="+pn);
//                pName.add(pn);
//            }
//        }
//        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
//    }

    /**
     * 判断手机已安装某程序的方法
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            //e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
