package consumer.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.utils.api.EAPIConsts;
import com.utils.api.IApiCallback;
import com.utils.api.ModelBase;
import com.utils.api.ModelBaseCache;
import com.utils.api.ReqBase;

/**
 * Created by chris on 16/3/10.
 */
public class CarReqUtils extends ReqBase {

    public static final String TAG = CarReqUtils.class.getSimpleName();

    /**
     * @des   29  添加设备
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache addEquipment(Context context, IApiCallback callback,
                                               Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("addequipment.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des   30 顺风车详情
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache hitchhikeInfo(Context context, IApiCallback callback,
                                              Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("hitchhikeinfo.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   31  车主申请
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache apply(Context context, IApiCallback callback,
                                              Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("apply.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   32  新订单列表
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache orderList(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("orderlist.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   33  商业白板
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache businessList(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("businesslist.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des   34  车主修改地址
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache updateAddress(Context context, IApiCallback callback,
                                          Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("updateaddress.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   35  车主修改车的品牌
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache modifCarBrand(Context context, IApiCallback callback,
                                               Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("carbrand.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des   36  车主车的照片
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache modifCarPic(Context context, IApiCallback callback,
                                               Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("carpic.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des   37  接单
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache order(Context context, IApiCallback callback,
                                               Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("order.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des   38  请求接力接单
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache relOrder(Context context, IApiCallback callback,
                                               Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("relorder.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   39  接力接单
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache relayOrder(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("relayorder.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   45  请求接力预估费用
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache expressost(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("map/expresscost.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   51  接力预估费用
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache relexpressost(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork,String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("map/relexpresscost.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }






    /**
     * @des   49  个人资料
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache personalInfo(Context context, IApiCallback callback,
                                          Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("personalinfo.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   52  订单详情（车主）
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache orderInfo(Context context, IApiCallback callback,
                                              Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;

        try{
            url = getFullUrl("orderinfo.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {

            Log.d(TAG, e.getMessage());
            return null;

        }
    }

    /**
     * @des   53  我的订单列表（车主）
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache myOrderList(Context context, IApiCallback callback,
                                              Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("myorderlist.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   54  今日业绩
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache Achivement(Context context, IApiCallback callback,
                                             Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("achievement.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   55 已取货
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache pickup(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("pickup.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   56 已送达
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache delivery(Context context, IApiCallback callback,
                                        Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("delivery.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des   63 订单详情，车主接单后
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache orderedInfo(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("orderedinfo.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   62 当前位置
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache currentposi(Context context, IApiCallback callback,
                                             Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("currentposi.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   61 行驶轨迹
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache travelingtrack(Context context, IApiCallback callback,
                                             Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("travelingtrack.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des   60 我的钱包
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache cuserbalance(Context context, IApiCallback callback,
                                                Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("cuserbalance.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   66、开启/停止接单
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache setuorder(Context context, IApiCallback callback,
                                           Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("setuorder.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   67、开启/关闭防盗模式
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache guard(Context context, IApiCallback callback,
                                           Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("guard.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des  68、	更新防盗模式状态
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache setguard(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("setguard.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des   69、	查询防盗模式状态
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache getguard(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("getguard.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   70、丢失找回
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache recoverlost(Context context, IApiCallback callback,
                                          Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("recoverlost.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   71、	查询断电状态
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache getcut(Context context, IApiCallback callback,
                                             Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("getcut.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   75、	保养里程
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache mmlieage(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("mmlieage.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   76、	恢复出厂设置
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache factory(Context context, IApiCallback callback,
                                          Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("factory.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 78、	查询设备设置状态
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache getdeviceinfo(Context context, IApiCallback callback,
                                         Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("getdeviceinfo.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 79、	设备设置
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache setdevice(Context context, IApiCallback callback,
                                               Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("setdevice.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 82、	现金支付
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache cashpayment(Context context, IApiCallback callback,
                                           Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("cashpayment.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 85、	报警数据列表
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache alarmlist(Context context, IApiCallback callback,
                                             Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("alarmlist.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 86、	报警数据数量
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */
    public static ModelBaseCache alarmnums(Context context, IApiCallback callback,
                                             Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {

        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("alarmnums.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des 87、	判断用户是否绑定了设备
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */

    public static ModelBaseCache checkdeviced(Context context, IApiCallback callback,
                                           Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {
        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("checkdeviced.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * @des 72、	设置灵敏度
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */

    public static ModelBaseCache vbsen(Context context, IApiCallback callback,
                                              Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {
        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("vbsen.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   73、	设置SOS号码
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */

    public static ModelBaseCache sos(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {
        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("sos.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @des   74、	设置取电
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */

    public static ModelBaseCache power(Context context, IApiCallback callback,
                                     Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {
        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("power.php?"+str);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input,isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }
    /**
     * @des   77、	电子围栏
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     */

    public static ModelBaseCache circle(Context context, IApiCallback callback,
                                       Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String str) {
        String requestStr = "";
        String url = null;
        try{
            url = getFullUrl("circle.php?"+str);

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
