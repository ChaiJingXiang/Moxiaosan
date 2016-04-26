package com.utils.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.log.LLog;
import com.utils.ui.base.ActivityHolder;

import java.util.LinkedList;
import java.util.List;

import consumer.model.obj.RespUserEntity;

public class KeelApplication extends Application {
	private static final String TAG = "KeelApplication";  
	public static final String KEEL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Keel/";
	//自定义的变量  
	protected int version = 1;

	protected static KeelApplication mDemoApp;

	public boolean isLogined = false;

	final Handler eHandler = new Handler();

	//protected AppData mAppData; // 应用程序级数据对象

	// 退出时用来销毁Activity
	protected List<Activity> activityList = new LinkedList<Activity>();


	public static Context getApplicationConxt() {
		return getApp().getApplicationContext();
	}

	@Override  
	public void onCreate() {  
		super.onCreate();  
		LLog.v("onCreate:KeelPath==" + KeelApplication.KEEL_PATH);

		mDemoApp = this;


		if(LLog.needLog()) {
			HandlerCrash.getInstance().init(this);
		}


//		int dpvalue = DisplayUtil.px2dip(this, 320);
//		int width = DisplayUtil.getScreenWidth(this);
//		LLog.d("dpvalue:" + dpvalue + "  width:" + width  );
		caughtUncaughtException();
	}

	/**
	 * 处理未捕获异常
	 */
	private void caughtUncaughtException() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				ex.printStackTrace();
				// 退出程序
				//finishAllActivity();
				ActivityHolder.getInstance().finishAllActivity();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(-1);
			}
		});
	}

	public static KeelApplication getApp() {
		return (KeelApplication) mDemoApp;
	}

    /**
     * @author fengqongqiang
     * @des 获取Cookie
     */
    public static String getCookie(){
		return AppData.getInstance().getCookie();
    }

    /**
     * @author fengqongqiang
     * @des 保存Cookie
     * @param cookieStr
     */
    public static void saveCookie(String cookieStr){
        AppData.getInstance().saveCookie(cookieStr);
    }


	public AppData getmAppData() {
		return AppData.getInstance();
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}







	public static String getUserID(){
		return "1";
	}

	//获取缩略图后缀，如 !small , _72等
	public static String getThumbName(){
		return "_72";
	}

	@Override
	public void onLowMemory() {
		ImageLoader.getInstance().clearMemoryCache();
		super.onLowMemory();
	}

//	public static LoginHxInterface getLoginHxInterface() {
//		return getApp().loginHxInterface;
//	}
//
//	public static void setLoginHxInterface(LoginHxInterface loginHxInterface) {
//		getApp().loginHxInterface = loginHxInterface;
//	}
}
