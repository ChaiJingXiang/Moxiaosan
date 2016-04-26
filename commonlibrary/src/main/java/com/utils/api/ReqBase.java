package com.utils.api;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.utils.file.BaseCacheUtils;
import com.utils.file.FileConstants;
import com.utils.http.EAPITask;

/**
 *
 * <p>Title: ReqBase.java<／p>
 * <p>Description: api请求基类<／p>

 * @author fengyongqiang
 * @date 2014-11-28
 * @version 1.0
 */
public class ReqBase {
	public static boolean USE_DEV = true;//正式的开发环境
	private static String testUlr =  "http://192.168.30.9:8180/epservice/api.do?";

	/**
	 *
	 * @param context
	 * @param callback
	 * @param model 如果是需要缓存api里的数据岛文件， 则需要将model从ModelBaseCache继承并实现getKeyValue方法，否则直接从ModelBase继承即可
	 * @param url
	 * @param request
	 * @param handler
	 * @param inputObject
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送alse
	 * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
	 */
	public static ModelBaseCache doExecute(Context context, IApiCallback callback,ModelBase model,
										   String url, String request, Handler handler, Object inputObject,boolean isNetWork) {
		if(handler == null){
			handler =  new HandlerAPI();
		}

        //判断是否为测试模式
        if(!USE_DEV){
            url = testUlr+url+request;
        }

        if(isNetWork) {
            //启动线程请求数据
            EAPITask.doExecute(context, callback, model, url, request, handler, inputObject);
        }

		//判断是否有缓存， 如果有， 则返回缓存数据
		if(model instanceof ModelBaseCache){
			//如果是需要缓存的， 则先读取缓存
			return readModelBaseCache(((ModelBaseCache) model).getKeyValue());
		}else{
			return null;
		}

	}

	/**
	 *
	 * @param modelKey 根据model的key返回model对象
	 * @return
	 */
	private static ModelBaseCache readModelBaseCache(String modelKey){
		//根据
		if(TextUtils.isEmpty(modelKey))
			return null;

		ModelBaseCache retObj = (ModelBaseCache) BaseCacheUtils.readObject(FileConstants.getApiSaveFilePath(), genCacheFilePath(modelKey));
		return retObj;
	}


	/**
	 *
	 * @param model 要缓存的model对象
	 */
	public static void writeModelBaseCache(ModelBaseCache model){
		if(TextUtils.isEmpty(model.getKeyValue()))
			return ;

		BaseCacheUtils.writeObject(FileConstants.getApiSaveFilePath(), genCacheFilePath(model.getKeyValue()), model);
	}

	/**
	 * @des 根据key生成要缓存的model的绝对路径
	 * @param modelKey
	 * @return
	 */
	private static String genCacheFilePath(String modelKey){
		return modelKey;
	}


}
