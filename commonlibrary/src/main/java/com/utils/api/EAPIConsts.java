package com.utils.api;

import android.content.Context;

import com.moxiaosan.commonlibrary.R;
import com.utils.common.KeelApplication;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

public class EAPIConsts {

	// 正式(生产 ONLINE)-0，开发(DEV)-1
	private final static int ONLINE = 0;
	private final static int DEV = 1;
	private final static int QA = 2;

	public static int HOME_ENVIRONMENT = DEV;
//	private final static String HOME_URL_DEV = "http://192.168.30.211:8080/"; 120.76.101.90  114.215.158.238       //开发环境
	private final static String HOME_URL_DEV = "http://114.215.158.238:8005/";   //开发环境
    private final static String TEST_URL_DEV = "https://beta.quncaotech.com/";//测试地址

	private final static String HOME_URL_ONLINE = "https://app.quncaotech.com/";

	// 自定义headQA
	public static class Header{
		public static final String ERRORCODE = "errorCode"; // 生产环境
		public static final String ERRORMESSAGE = "errorMessage"; // 生产环境
	}

	public static String getHomeHUrl(){
		switch(HOME_ENVIRONMENT){
		case DEV:
			return HOME_URL_DEV;
		case QA:
			return TEST_URL_DEV;
		default: // 默认线上
			return HOME_URL_ONLINE;
		}
	}

	public static String getAgentUrl(){
		switch(HOME_ENVIRONMENT){
		case DEV:
			return HOME_URL_DEV + "agent/";

		case QA:
			return  TEST_URL_DEV + "agent/";

		default: // 默认线上
			return HOME_URL_ONLINE + "agent/";
		}
	}

    public static String getTestUrl(){
        return TEST_URL_DEV;
    }

    public static String getActivityUrl(){
		String serverUrl = getServerUrl(KeelApplication.getApplicationConxt());
//		Log.i("aa", "==serverUrl=="+serverUrl);
		if(!serverUrl.trim().isEmpty()){
			serverUrl = serverUrl.replace("\r\n","");
			serverUrl = serverUrl.replace("\r","");
			serverUrl = serverUrl.replace("\n","");
			return serverUrl;
		}else{
			switch(HOME_ENVIRONMENT){
				case DEV:
//					Log.i("aa", "==DEV=="+HOME_URL_DEV);
					return HOME_URL_DEV;

				case QA:
//					Log.i("aa", "==QA=="+TEST_URL_DEV);
					return TEST_URL_DEV;

				default: // 默认线上
//					Log.i("aa", "==default=="+HOME_URL_ONLINE);
					return HOME_URL_ONLINE;
			}
		}

    }

    public static String getUserUrl(){
        switch(HOME_ENVIRONMENT){
            case DEV:
                return HOME_URL_DEV;

			case QA:
				return TEST_URL_DEV;

            default: // 默认线上
                return HOME_URL_ONLINE;
        }
    }

    public static String getPayUrl(){
			switch(HOME_ENVIRONMENT){
            case DEV:
                return HOME_URL_DEV;

			case QA:
				return TEST_URL_DEV;

            default: // 默认线上
                return HOME_URL_ONLINE;
        }
    }

	public static class handler{
		public static final byte show_err=1;
	}

	//获取服务地址
	private static String getServerUrl(Context context) {
		try {
			InputStream in = context.getResources().openRawResource(R.raw.serverurl);
			byte[]  buffer = new byte[in.available()];
			in.read(buffer);
			String result = EncodingUtils.getString(buffer, "UTF-8");
			return  result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
		
}
