package com.utils.http;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.utils.api.IApiCallback;
import com.utils.api.IUploadFileCallback;
import com.utils.api.ModelBase;
import com.utils.api.ModelBaseCache;
import com.utils.api.ReqBase;
import com.utils.common.ApolloUtils;
import com.utils.log.LLog;
import com.utils.upyun.MUploadFile;
import com.utils.upyun.UploadPic;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 
* <p>Title: EAPITask.java<／p>
* <p>Description: 后台API的访问接口方法，所有获取后台api数据都从该类访问<／p>

* @author Michael
* @date 2014-12-2
* @version 1.0
 */


public class EAPITask extends AsyncTask<Object, Integer, Object> {

	private final static String TAG = "EAPITask";
	private static WeakReference<AsyncTask<Object, ?, ?>> taskWeakReference;
	private EAPIDataMode mode;
	private Context mContext;
	private Object mInputObject;//输入的对象， 输出也用，用来关联输入和输出
	private IApiCallback mCallback = null;
	private ModelBase mModel;

	

	public EAPITask(IApiCallback callback) {
		mCallback = callback;
	}
	
	@Override
	protected Object doInBackground(Object... params) {

		if (params[0] instanceof Context) {
			mContext = (Context) params[0];
		}else{
			return null;
		}
		
		if (params[1] instanceof IApiCallback) {
			mCallback = (IApiCallback) params[1];
		}else{
			return null;
		}
		
		if(params[2] instanceof ModelBase){
			mModel = (ModelBase) params[2];
		}
		
		String url=null;
		if( params[3] instanceof String){
			url =(String) params[3];
		}
		
		String request=null;
		if( params[4] instanceof String){
			 request =(String) params[4];
		}
		
		Handler handler=null;
		if(params[5] instanceof Handler){
			handler = (Handler) params[5];
		}
		
		mInputObject = (Object) params[6];
//		if (params[6] instanceof Object) {
//			mInputObject = (Object) params[6];
//		}else{
//			return null;
//		}
		publishProgress(0);
		
		LLog.d("task:" + mModel.getClass().getName());

		if(mModel instanceof MUploadFile){
			//上传文件
			MUploadFile ret =  UploadPic.uploadFile(url, ((MUploadFile)mModel).getSuffixName(), new IUploadFileCallback() {
				@Override
				public void onProgress(int percent) {
					publishProgress(percent);
				}

				@Override
				public void onData(Object output, Object input) {

				}
			});
			publishProgress(100);
			return ret;
		}else{
			// 其他常规命令
			mode = new EAPIDataMode(mContext);
			Object retObj = mode.getCommonObject(mModel,url ,request, handler);

			return retObj;
		}


	}

	@Override
	protected void onPostExecute(Object result) {
	    //super.onPostExecute(result);

//        LLog.d("post:" + mCallback + " tag:" + mModel.getClass().getName());
		if (mCallback != null) {
			mCallback.onData(result, mInputObject);//将数据发送给注册的回调函数
		}

		if(result != null && (result instanceof ModelBaseCache)){
			ReqBase.writeModelBaseCache((ModelBaseCache) result);
		}
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if(mCallback instanceof IUploadFileCallback){
			//如果是上传文件类型，则通知上载进度
			IUploadFileCallback progress = (IUploadFileCallback)mCallback;
			progress.onProgress(values[0]);
		}
		super.onProgressUpdate(values);
	}
	
	private static JSONObject makeCommonJson(){
		JSONObject jb=new JSONObject();
		return jb;
	}

	/*
	private static String jsonObject2String(JSONObject jsonObject){
		
		String result = "";
		try {
			byte[] tempBytes = jsonObject.toString().getBytes("UTF-8");
			result = tempBytes.toString();
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	*/
	/************************************************************************
	 api入口处，新的api在下面添加
	 */
	/*
	// 登录
	public static void doLogin(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject2String(jsonObject);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.LOGIN;
		doExecute(context, bind, EAPIConsts.ReqType.LOGIN, url, requestStr,
				handler);
	}
	
	// 注册
	public static void doRegister(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject2String(jsonObject);
		String url = EAPIConsts.TMS_URL_TEST + EAPIConsts.ReqUrl.REGISTER;
		doExecute(context, bind, EAPIConsts.ReqType.REGISTER, url, requestStr,
				handler);
	}
	
	// 获取验证码
	public static void doGetVerifyCode(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject2String(jsonObject);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GET_VERIFY_CODE;
		doExecute(context, bind, EAPIConsts.ReqType.GET_VERIFY_CODE, url,
				requestStr, handler);
	}
	
	// 完善个人会员信息
	public static void doFullPersonMemberInfo(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject2String(jsonObject);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FULL_PERSON_MEMBER_INFO;
		doExecute(context, bind, EAPIConsts.ReqType.FULL_PERSON_MEMBER_INFO, url,
				requestStr, handler);
	}
	
	// 完善机构会员联系人信息
	public static void doFullContactInfo(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject2String(jsonObject);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FULL_CONTACT_INFO;
		doExecute(context, bind, EAPIConsts.ReqType.FULL_CONTACT_INFO, url,
				requestStr, handler);
	}
	
	// 上传机构验证信息
	public static void doFullOrganizationAuth(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject2String(jsonObject);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FULL_ORGANIZATION_AUTH;
		doExecute(context, bind, EAPIConsts.ReqType.FULL_ORGANIZATION_AUTH, url,
				requestStr, handler);
	}
	
	// 设置新密码
	public static void doSetNewPassword(Context context, IBindData bind,
			JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject2String(jsonObject);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.SET_NEW_PASSWORD;
		doExecute(context, bind, EAPIConsts.ReqType.SET_NEW_PASSWORD, url,
				requestStr, handler);
	}
	*/
	public static void doExecute(Context context, IApiCallback callback,ModelBase model,
			String url, String request, Object inputObject) {
		doExecute(context, callback, model, url, request, null, inputObject);
	}

	public static void doExecute(Context context, IApiCallback callback,ModelBase model,
			String url, String request, Handler handler, Object inputObject) {
		taskWeakReference = ApolloUtils.execute(false, new EAPITask(callback), context, callback, model,
				url, request, handler, inputObject);
	}

	/**
	 * 关闭Task
	 * 关闭网络请求在可关闭的情况下,如果不可关闭就等待网络请求超时
 	 */
	public static void dismisTask(){
		if(taskWeakReference == null || taskWeakReference.get() == null){
			return;
		}

		if(!taskWeakReference.get().isCancelled()){
			taskWeakReference.get().cancel(true);

//			if(mode != null) {
//				mode.disconnect();
//			}

		}
	}
}
