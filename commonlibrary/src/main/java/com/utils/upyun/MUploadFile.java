package com.utils.upyun;

import com.utils.api.ModelBase;


/**
 * 
* <p>Title: .java<／p>
* <p>Description: 上传文件返回对象，包含文件相对路径等信息,因为这个是upyun专用的， 所以就放到upyun模块里<／p>

* @author Michael
* @date 2014-11-28
* @version 1.0
 */
public class MUploadFile  extends ModelBase {
    private static final long serialVersionUID = 198009944223064454L;
    private String url;//相对路径， 比如/lark/1.png,该值为空或者空串， 表示上传失败,后端需要将相对路径拼接成绝对路径
    private String file;//文件绝对路径， 比如 /lark/1.png
    private boolean result;//上传成功或者失败
	private String suffixName;//文件后缀
	//final private static String preUrl = "http://quncao-app.b0.upaiyun.com/";

	public MUploadFile(String suffix){
		this.suffixName = suffix;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public ModelBase parseData(String strData) {
		return null;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}
}
 