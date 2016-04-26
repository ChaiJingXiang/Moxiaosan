package com.utils.common;

public class EConsts {
		
	// 拍照请求码
	public static final int REQ_CODE_TAKE_PICTURE = 1001;
	// 选照请求码
	public static final int REQ_CODE_PICK_PICTURE = 1002;
	// 选视频请求码
	public static final int REQ_CODE_PICK_VIDEO = 1004;
	// 拍视频请求码
	public static final int REQ_CODE_TAKE_VIDEO = 1005;
	// 选择文件请求码
	public static final int REQ_CODE_PICK_FILE = 1006;
//


	//验证码倒计时
	public static final int IDENTIFYING_CODE = 60;

	// 键值
	public static class Key{
		public static final String APP_SETTING = "app_setting";
		public static final String SESSION_ID = "session_id"; //会话id
		public static final String USER_ID = "user_id";
		// 用户昵称
		// 用户名
		public static final String USER_NAME = "user_name";
		// 密码
		public static final String PASSWORD = "password";
        // Cookie
        public static final String COOKIE = "cookie";
	}


	// 密码加密秘钥
	public static final String PASSWORD_KEY = "test2015.0";

}
