package consumer.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.utils.api.EAPIConsts;
import com.utils.api.IApiCallback;
import com.utils.api.ModelBase;
import com.utils.api.ModelBaseCache;
import com.utils.api.ReqBase;

import consumer.Interface;

/**
 * Created by qiangfeng on 16/2/25.
 */
public class UserReqUtil extends ReqBase {
    public static final String TAG = UserReqUtil.class.getSimpleName();

    /**
     * @des 0 获取手机验证码
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache getPhoneCode(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("dayu_dx/code_an.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {

            Log.d(TAG, e.getMessage());
            return null;
        }
    }



    /**
     * @des 1 注册 vakudCode
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache reqNetData(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
                url = getFullUrl(str);
            
            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 20 找回密码
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache updatePassword(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";

        String url = null;
        try{
            url = getFullUrl(Interface.UPDATEPASSWORD+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des 21 修改手机 updatetel
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache updatetel(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl(Interface.UPDATETEL+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 22 修改头像 updatehead
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache updatehead(Context context, IApiCallback callback,
                                           Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl(Interface.UPDATEHEAD+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des 23 修改昵称 updatename
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache updatename(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl(Interface.UPDATENAME+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 47、	用户信息
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache userinfo(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl("userinfo.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 城市列表接口
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache getcityname(Context context, IApiCallback callback,
                                          Handler handler, ModelBase modelBase, Object input, boolean isNetWork) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl("getcityname.php");

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 支付接口
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache wxpay(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl("pay_mxs/wxpay.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 充值接口
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache payMxs(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl("pay_mxs/wxpay.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des 88、	申请提现(微信支付宝)
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache cashapp(Context context, IApiCallback callback,
                                        Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl("cashapp.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des 89、	申请提现(银行)

     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache cashappbank(Context context, IApiCallback callback,
                                        Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl("cashappbank.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des 90、	申请提现记录
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache cashapprec(Context context, IApiCallback callback,
                                        Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl("cashapprec.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }



    private static String getFullUrl(String method) {
        return EAPIConsts.getUserUrl() + method;
    }

}
