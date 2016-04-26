package com.utils.common;


import android.text.TextUtils;

import com.utils.file.BaseCacheUtils;
import com.utils.file.FileConstants;
import com.utils.file.FileHelper;

import consumer.model.obj.RespUserEntity;
import consumer.model.obj.RespUserInfo;

/**
 * AppData.java
 * 保存app的公用的持久化数据，如 用户对象， cookie等。为单件类
 */
public class AppData {
    private static AppData mInstance = null;
    private final static String MSG_FILE = "msgSetting";
    private final static String USER_FILE = "userinfo";
    private final static String HXUSER_FILE = "hxUserInfo";
    private final static String COOKIE_FILE = "cookie";
    private final static String USER_COOKIE_FILE = "userCookie";
    private final static String UNREAD_NOTIFY_FILE = "unreadNotify";

    private RespUserInfo userEntity;
//    private HXUserEntity hxUserEntity;
//    private RespUserMsgSeting userMsgSeting;
    private String cookie;
    private String userCookie;

    public static AppData getInstance() {
        if (mInstance == null) {
            mInstance = new AppData();
        }
        return mInstance;
    }

    private AppData() {
    }

    /**
     * 将用户用户信息保存到本地文件中
     *
     * @param userEntity 如果为空， 表示退出登陆
     */
    public void saveUserEntity(RespUserInfo userEntity) {
        this.userEntity = userEntity;
        if (userEntity == null) {
            FileHelper.deleteFile(FileConstants.getUserInfoSaveFilePath() + USER_FILE + BaseCacheUtils.FILE_SUFFIX);

        } else {
            BaseCacheUtils.writeObject(FileConstants.getUserInfoSaveFilePath(), USER_FILE, userEntity);
        }
    }

    /**
     * 从文件中读取用户信息xxxx
     *
     * @return
     */
    public RespUserInfo getUserEntity() {
        if (userEntity != null)
            return userEntity;

        userEntity = (RespUserInfo) BaseCacheUtils.readObject(FileConstants.getUserInfoSaveFilePath(), USER_FILE);
        return userEntity;
    }


    /**
     * 将消息信息保存到本地文件中
     *
     * @param userMsgSeting
     */
//    public void saveUserMsg(RespUserMsgSeting userMsgSeting) {
//        this.userMsgSeting = userMsgSeting;
//
//        if (userMsgSeting == null) {
//            FileHelper.deleteFile(FileConstants.getMsgSettingSaveFilePath() + MSG_FILE + BaseCacheUtils.FILE_SUFFIX);
//
//        } else {
//            BaseCacheUtils.writeObject(FileConstants.getMsgSettingSaveFilePath(), MSG_FILE, userMsgSeting);
//        }
//    }

    /**
     * 从文件中读取信息
     *
     * @return
     */
//    public RespUserMsgSeting getUserMsg() {
//        if (userMsgSeting != null)
//            return userMsgSeting;
//
//        userMsgSeting = (RespUserMsgSeting) BaseCacheUtils.readObject(FileConstants.getMsgSettingSaveFilePath(), MSG_FILE);
//        return userMsgSeting;
//    }

    /**
     * 将环信用户用户信息保存到本地文件中
     *
     * @param hxUserEntity 如果为空， 表示退出登陆
     */
//    public void saveHXUserEntity(HXUserEntity hxUserEntity) {
//        this.hxUserEntity = hxUserEntity;
//        if (hxUserEntity == null) {
//            FileHelper.deleteFile(FileConstants.getHxUserInfoSaveFilePath() + HXUSER_FILE + BaseCacheUtils.FILE_SUFFIX);
//
//        } else {
//            BaseCacheUtils.writeObject(FileConstants.getHxUserInfoSaveFilePath(), HXUSER_FILE, hxUserEntity);
//        }
//    }

    /**
     * 从文件中读取用户信息xxxx
     *
     * @return
     */
//    public HXUserEntity getHxUserEntity() {
//        if (hxUserEntity != null) {
//
//            return hxUserEntity;
//        }
//
//        hxUserEntity = (HXUserEntity) BaseCacheUtils.readObject(FileConstants.getHxUserInfoSaveFilePath(), HXUSER_FILE);
//        return hxUserEntity;
//    }

    /**
     * @des 保存用户Cookie
     */
    public void saveUserCookie(String cookieStr) {
        if (TextUtils.isEmpty(cookieStr)) {
            cookieStr = "";
        }
        this.userCookie = cookieStr;
        BaseCacheUtils.writeObject(FileConstants.getUserInfoSaveFilePath(), USER_COOKIE_FILE, userCookie);
    }

    /**
     * @des 获取用户Cookie
     */
    public String getUserCookie() {
        if (this.userCookie == null)
            userCookie = (String) BaseCacheUtils.readObject(FileConstants.getUserInfoSaveFilePath(), USER_COOKIE_FILE);

        return this.userCookie;
    }

    /**
     * @param cookieStr
     * @author dingjun.he
     * @data 2015-8-28
     * @des 保存Cookie
     */
    public void saveCookie(String cookieStr) {
        if (TextUtils.isEmpty(cookieStr)) {
            cookieStr = "";
        }
        this.cookie = cookieStr;
        BaseCacheUtils.writeObject(FileConstants.getUserInfoSaveFilePath(), COOKIE_FILE, cookie);
    }

    /**
     * @author dingjun.he
     * @data 2015-8-28
     * @des 获取Cookie
     */
    public String getCookie() {
        if (this.cookie == null)
            cookie = (String) BaseCacheUtils.readObject(FileConstants.getUserInfoSaveFilePath(), COOKIE_FILE);

        return this.cookie;
    }

    //判断用户是否登陆
    public boolean hasLogin() {
        if (getUserEntity() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 将“消息中心”的未读通知类消息保存到文件中
     *
     * @param
     */
//    public void saveUnreadNotifyMessage(UnreadNotifyMessage unreadNotifyMessage) {
//        if (unreadNotifyMessage == null) {
//            FileHelper.deleteFile(FileConstants.getUnreadNotifySaveFilePath() + UNREAD_NOTIFY_FILE + BaseCacheUtils.FILE_SUFFIX);
//        } else {
//            BaseCacheUtils.writeObject(FileConstants.getUnreadNotifySaveFilePath(), UNREAD_NOTIFY_FILE, unreadNotifyMessage);
//        }
//    }

    /**
     * 获取未读通知类消息
     *
     * @return
     */
//    public UnreadNotifyMessage getUnreadNotifyMessage() {
//
//        return (UnreadNotifyMessage) BaseCacheUtils.readObject(FileConstants.getUnreadNotifySaveFilePath(), UNREAD_NOTIFY_FILE);
//    }

}
