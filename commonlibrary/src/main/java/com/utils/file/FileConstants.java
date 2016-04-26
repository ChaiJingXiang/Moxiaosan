package com.utils.file;

import android.os.Environment;

import com.utils.common.KeelApplication;

//import com.utils.common.KeelApplication;

public class FileConstants
{
    // 文件保存路径
	private static final String SAVE_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    //获取文件存储的根目录
    public static String getApiSaveFilePath(){

        return  SAVE_FILE_PATH + "/moxiaosancache/api2/";
    }

    //获取用户信息存储目录,如用户对象，cookie
    public static String getUserInfoSaveFilePath() {

        return KeelApplication.getApplicationConxt().getFilesDir() + "/moxiaosancache/user/";
    }
//
//    //获取用户信息存储目录,如用户对象，cookie
//    public static String getHxUserInfoSaveFilePath() {
//
//        return  KeelApplication.getApplicationConxt().getFilesDir() + "/larkcache/hxuser/";
//    }

    //获取崩溃信息存储目录,用来存放最后一次的崩溃日志
    public static String getCrashSaveFilePath() {

        return  SAVE_FILE_PATH + "/moxiaosancache/crash/";
    }

    //获取图片存储的根目录
    public static String getImageSaveFilePath(){

        return  SAVE_FILE_PATH + "/moxiaosancache/image/";
    }

    //获取消息设置的根目录
    public static String getMsgSettingSaveFilePath(){

        return SAVE_FILE_PATH + "/moxiaosancache/msg_setting/";
    }

    //获取消息中心未读通知条数的根目录
    public static String getUnreadNotifySaveFilePath(){

        return SAVE_FILE_PATH + "/moxiaosancache/user/unread_notify/";
    }

}
