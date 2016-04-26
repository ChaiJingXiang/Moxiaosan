package com.utils.upyun;

import com.utils.api.IUploadFileCallback;
import com.utils.common.EUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class UploadPic {
	private final static String BUCKET_NAME = "quncao-app";
	private final static String OPERATOR_NAME = "quncaoapp";
	private final static String OPERATOR_PWD = "quncaoapp";


	/**
	 *
	 * @param filePath 文件的完整路径文件名， 如 /storge/sdcard/dicm/camera/1.jpg
	 * @param suffixName 文件后缀名， 如 ".jpg", ".png"
	 * @param callback
	 * @return
	 */
	public static MUploadFile uploadFile(String filePath, String suffixName, IUploadFileCallback callback) {
		// 初始化空间
		UpYun upyun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
		MUploadFile result;
		result = new MUploadFile(suffixName);
		result.setFile(filePath);
		try {
			// 设置是否开启debug模式，默认不开启
			upyun.setDebug(true);
			/*
			 * 上传方法3：采用数据流模式上传文件（节省内存），可自动创建父级目录（最多10级）
			 */
			String upName = EUtil.genUpyunFileName(filePath) + suffixName;
			
			File file = new File(filePath);
			Map<String,String> map = new HashMap<String,String>();
			map.put(UpYun.PARAMS.KEY_X_GMKERL_QUALITY.getValue(),"90");//90%图片质量
			map.put(UpYun.PARAMS.KEY_X_GMKERL_TYPE.getValue(),"fix_max");//限制最大边
			map.put(UpYun.PARAMS.KEY_X_GMKERL_VALUE.getValue(),"720");//设置最大边720
			map.put(UpYun.PARAMS.KEY_X_GMKERL_ROTATE.getValue(),"auto");//设置旋转
			boolean result3 = upyun.writeFile(upName, file, true, map,callback);
			System.out.println("upName " + filePath + result3);
			result.setFile(filePath);
			if(result3){
				result.setUrl(upName);
				result.setResult(true);
			}else{
				result.setResult(false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
