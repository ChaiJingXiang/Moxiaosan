package com.moxiaosan.both.utils;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

/**
 * @ClassName:     JTFile.java
 * @Description:   附件对象
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-10
 * @LastEdit       2014-04-10
 */
public class JTFile implements Serializable{

	private static final long serialVersionUID = 19834212142324422L;

	public final static int TYPE_VIDEO = 0;
	public final static int TYPE_AUDIO = 1;
	public final static int TYPE_FILE  = 2;
	public final static int TYPE_IMAGE = 3;
	public final static int TYPE_OTHER = 4;
	public final static int TYPE_WEBURL = 5;
	public final static int TYPE_JTCONTACT_OFFLINE = 6;
	public final static int TYPE_ORG_OFFLINE = 8;
	public final static int TYPE_ORG_ONLINE = 9;
	public final static int TYPE_JTCONTACT_ONLINE = 10;
	public final static int TYPE_KNOWLEDGE = 7;
	public final static int TYPE_REQUIREMENT = 11;
	public final static int TYPE_TEXT = 12; // 文本
	public final static int TYPE_KNOWLEDGE2 = 13; // 新知识
	public final static int TYPE_CONFERENCE = 14; // 会议

	private String id = "";  // 文件id（本地使用）
	public String mUrl = ""; // 图片url
	public String mFileName = ""; // 人脉和机构title
	public String mSuffixName = ""; // 人脉公司名称 人脉和机构 第一字段
	public int mType = 0;
	public String mLocalFilePath = ""; // 本地存储路径（本地使用）  
	public String mScreenshotFilePath = ""; // 视频缩略图地址（本地使用） 
	public String mTaskId = ""; // id
	public long mFileSize = 0; 
	public long mDownloadSize = 0; // 已下载的文件大小（本地使用）
	public long mCreateTime = 0; // 文件创建时间（本地使用）
	public String reserved1 = ""; // 备用字段 人脉和机构第二字段
	public String reserved2 = ""; // 备用字段，视频缩略图地址，语音文件时长
	public String reserved3 = ""; // 备用字段
	
	// 0:需求、1：业务需求、2：公司客户、3：公司项目、4：会员、5：名片 、6 公司名片 、7资讯、8客户、9人脉分享 、10机构
	public int mModuleType = 0; // 类型

	
	public JTFile(){

	}
	
	public JSONObject toJson(){
		JSONObject obj = null;
		try {
			obj = new JSONObject();
			obj.put("id", this.id);
			obj.put("url", this.mUrl);
			obj.put("suffixName", this.mSuffixName);
			obj.put("type", this.mType);
			obj.put("fileName", this.mFileName);
			obj.put("fileSize", this.mFileSize);
			obj.put("taskId", this.mTaskId);
			obj.put("moduleType", this.mModuleType);
			obj.put("reserved1", this.reserved1);
			obj.put("reserved2", this.reserved2);
			obj.put("reserved3", this.reserved3);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return obj;
	}
	
	public static JTFile createFactory(JSONObject obj){
		JTFile self;
		try{
			self = new JTFile();
			self.initWithJson(obj);
			return self;
		}
		catch(Exception e){
			return null;
		}
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String strKey = null;
		
		// 文件id
		strKey = "id";
		if (!jsonObject.isNull(strKey)) {
			id = jsonObject.getString(strKey);
		}

		// 文件地址
		strKey = "url";
		if (!jsonObject.isNull(strKey)) {
			mUrl = jsonObject.getString(strKey);
		}
		
		// 文件名
		strKey = "fileName";
		if(!jsonObject.isNull(strKey)){
			mFileName = jsonObject.optString(strKey);
		}
		
		// 文件大小
		strKey = "fileSize";
		if(!jsonObject.isNull(strKey)){
			mFileSize = jsonObject.optLong(strKey);
		}

		// 文件后缀 
		strKey = "suffixName";
		if (!jsonObject.isNull(strKey)) {
			mSuffixName = jsonObject.getString(strKey);
		}

		// 文件类型
		strKey = "type";
		if (!jsonObject.isNull(strKey)) {
			mType = jsonObject.getInt(strKey);
		}
		
		// taskId
		strKey = "taskId";
		if(!jsonObject.isNull(strKey)){
			mTaskId = jsonObject.optString(strKey);
		}
		
		// 保留字段1
		strKey = "reserved1";
		if(!jsonObject.isNull(strKey)){
			reserved1 = jsonObject.optString(strKey);
		}
		
		// 保留字段2
		strKey = "reserved2";
		if (!jsonObject.isNull(strKey)) {
			reserved2 = jsonObject.optString(strKey);
		}
		
		// 保留字段3
		strKey = "reserved3";
		if (!jsonObject.isNull(strKey)) {
			reserved3 = jsonObject.optString(strKey);
		}
		
		// 附件所属类型
		strKey = "moduleType";
		if(!jsonObject.isNull(strKey)){
			mModuleType = jsonObject.optInt(strKey);
		}
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getmSuffixName() {
		return mSuffixName;
	}

	public void setmSuffixName(String mSuffixName) {
		this.mSuffixName = mSuffixName;
	}

	public int getmType() {
		return mType;
	}

	public void setmType(int mType) {
		this.mType = mType;
	}

	public String getmLocalFilePath() {
		return mLocalFilePath;
	}

	public void setmLocalFilePath(String mLocalFilePath) {
		this.mLocalFilePath = mLocalFilePath;
	}
	
	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	/*
	public int getmProgress() {
		return mProgress;
	}

	public void setmProgress(int mProgress) {
		this.mProgress = mProgress;
	}

	public int getmDownloadStatus() {
		return mDownloadStatus;
	}

	public void setmDownloadStatus(int mDownloadStatus) {
		this.mDownloadStatus = mDownloadStatus;
	}
	*/
	
	
	/**
	 * 创建分享对象
	 * @param intent
	 * @return
	 */
	public static JTFile createFromIntent(Intent intent) {

		JTFile jtFile = new JTFile();
		jtFile.mFileName = intent.getExtras().getString(Intent.EXTRA_TITLE);
		jtFile.mSuffixName = intent.getExtras().getString(Intent.EXTRA_TEXT);
		if (jtFile.mSuffixName != null) {
			
			int urlIndex = jtFile.mSuffixName.indexOf("http");
			
			if(urlIndex >= 0){
				String subStr = jtFile.mSuffixName.substring(urlIndex);
				jtFile.mUrl = subStr;
				// 网址结束标记
				String[] suffixSet = new String[] { " ", "\n" };
				int spaceIndex = subStr.indexOf(suffixSet[0]);
				int returnIndex = subStr.indexOf(suffixSet[1]);
				if((returnIndex >=0 && spaceIndex < returnIndex && spaceIndex >= 0) || (returnIndex < 0 && spaceIndex >= 0)){
					jtFile.mUrl = subStr.substring(0, spaceIndex);
				}
				else if((spaceIndex >= 0 && returnIndex < spaceIndex && returnIndex >= 0) || (spaceIndex < 0 && returnIndex >= 0)){
					jtFile.mUrl = subStr.substring(0, returnIndex);
				}
				jtFile.mSuffixName = jtFile.mSuffixName.substring(0, urlIndex);
				jtFile.mType = JTFile.TYPE_KNOWLEDGE;
			}
			else{
				jtFile.mUrl = ""; // 不包含链接
				jtFile.mType = JTFile.TYPE_TEXT;
			}
		}
		else{
			jtFile.mSuffixName = "";
			jtFile.mType = JTFile.TYPE_TEXT;
		}
		// jtFile.mType = JTFile.TYPE_KNOWLEDGE;
		return jtFile;
	}

}
