package com.utils.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.utils.api.EAPIConsts;
import com.utils.api.ModelBase;
import com.utils.log.LLog;


/**
 * @Filename 联网数据结果处理
 * @Author fengyongiang
 * @Date 2014-3-13
 * @description 后台接口访问数据类
 */

public class EAPIDataMode {
	private EHttpAgent httpAgent;
	private Context mContext;
	private String  mRequest;
	
    
	public EAPIDataMode(Context context) {
		this.httpAgent = new EHttpAgent(context);
		mContext = context;
	}

	//获取数据类
	public Object getCommonObject(ModelBase model, String url,String request, Handler handler)
	{
//		LLog.v(model.getClass().getName() + "==url==" + url);
//		String[] responses = httpAgent.getPostNetMessage(url,request);
		String[] responses = httpAgent.getNetMessage(url);
//		String sessionid=App.getApp().mAppData.getSessionID();
//		String[] responses = httpAgent.sendMessage(url, request, sessionid);
		
		mRequest = request;
        LLog.v("request:" + request + "  respose:" + responses[1]);
//        if(LLog.needLog()) {
//            BaseCacheUtils.writeObjectAppend(FileConstants.getApiSaveFilePath(), "apiresp.txt", "url:" + url + "\r\nRequest:\r\n" + request + "\r\n");
//            BaseCacheUtils.writeObjectAppend(FileConstants.getApiSaveFilePath(), "apiresp.txt", model.getClass().getName() + "  Resp:\r\n" + responses[1] + "\r\n\n");
//        }
    	if (comparisonNetworkStatus(responses, handler)) //数据是否有错
    	{
    		return createMsgObject(model, responses[1]);
        }
        LLog.d(" error .");
        showErrorMessage(handler, responses);
        return null;
	}
	//数据处理
	private Object createMsgObject(ModelBase model, String response)
	{
		//返回json数据解析由传入的model对象处理
        try {
//            LLog.e(model.getClass().getName() + ":" + response);

            ModelBase req = model.parseData(response);
            if (req == null) {
                Gson gson = new Gson();
                req = gson.fromJson(response, model.getClass());
//            req = JSON.parseObject(response, model.getClass());
            }
            return req;
        }catch (Exception e){
            LLog.e(e);
            return  null;
        }
	}
      
    /**
     * 显示errMessage
     * 
     * @param err
     */
    public void showErrorMessage(Handler handler, String[]  err) {
        showErrorMessage(handler, err, "");
    }
    public void showErrorMessage(Handler handler, String[]  err, String loginString) {
        String errCode = err[0];
        String errMessage = err[1];
        
        
        LLog.e("errCode," + errCode + ",errMessage :" + errMessage);

        if(TextUtils.isEmpty(errMessage)){
        	errMessage = "网络异常";
        }
        
        if (handler == null 
                || TextUtils.isEmpty(errMessage) 
                || "null".equals(errMessage)) {
            return;
        }
        
        if (errMessage.indexOf("<html>") > -1 
                || errMessage.indexOf("<head>") > -1 
                || errMessage.indexOf("<body>") > -1
                || errMessage.indexOf("</") > -1) {
        }
        if(handler!=null){
        	final Bundle bundle = new Bundle();
            bundle.putString(EAPIConsts.Header.ERRORCODE, errCode);
            bundle.putString(EAPIConsts.Header.ERRORMESSAGE, errMessage);
            final Message msg = new Message();
            msg.what = EAPIConsts.handler.show_err;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }
    
    
    
    /**
     * 
     * @des: 检查返回的http码是否正确，正确为200
     * @author Michael
     * @param responses
     * @return
     */
    private boolean comparisonNetworkStatus(String[] responses, Handler handler) {
        if (responses != null && responses.length == 2) {
            final String code = responses[0];
            final String message = responses[1];
            LLog.v("CODE_HTTP " + code);
           if (EHttpAgent.CODE_HTTP_SUCCEED.equals(code) && (message != null)) {
               LLog.d("sucuessfull.");
                return true;
            } 
        }
        return false;
    }

    /**
     *断开网络请求
     */
    public void disconnect(){
        httpAgent.disconnect();
    }

}