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
 * Created by qiangfeng on 16/3/8.
 */
public class ConsumerReqUtil extends ReqBase {
    public static final String TAG = UserReqUtil.class.getSimpleName();

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 40  首页轮播图片列表
     */
    public static ModelBaseCache carousel(Context context, IApiCallback callback,
                                          Handler handler, ModelBase modelBase, Object input, boolean isNetWork) {

        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("carousel.php");

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 28  我的消息
     */
    public static ModelBaseCache mynews(Context context, IApiCallback callback,
                                        Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {

        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("mynews.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 41  更新消息状态为已读
     */
    public static ModelBaseCache updatenews(Context context, IApiCallback callback,
                                            Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {

        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("updatenews.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 3、	门到门速递
     */
    public static ModelBaseCache express(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("express.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 7、	找劳力列表
     */
    public static ModelBaseCache labourlist(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            Log.i("-----","reqParam=="+reqParam);
            url = getFullUrl("labourlist.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 7、	我的找劳力列表     用户端
     */
    public static ModelBaseCache mylabourlist(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("mylabourlist.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 6、	找劳力
     */
    public static ModelBaseCache labour(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            //reqParam= URLEncoder.encode(reqParam,"UTF-8");
            url = getFullUrl("labour.php?" + reqParam);
//            url = "http://114.215.158.238:8004/test.php?a=测试";

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 10、	修改找劳力
     */
    public static ModelBaseCache modiylabour(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            //reqParam= URLEncoder.encode(reqParam,"UTF-8");
            url = getFullUrl("modiylabour.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 19、	出售商品列表
     */
    public static ModelBaseCache shoplist(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("shoplist.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 13、	发布出售商品
     */
    public static ModelBaseCache shopping(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("shopping.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 16、	删除出售商品
     */
    public static ModelBaseCache delshop(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("delshop.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 17、	修改出售商品
     */
    public static ModelBaseCache modiyshop(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("modiyshop.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 25、	我的评价
     */
    public static ModelBaseCache mycomments(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("mycomments.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 5、	顺丰车
     */
    public static ModelBaseCache hitchhiking(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("hitchhiking.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 9、	删除找劳力
     */
    public static ModelBaseCache dellabour(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("dellabour.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 4、	取消订单
     */
    public static ModelBaseCache cancelexpress(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("cancelexpress.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 92、	重新发送该订单
     */
    public static ModelBaseCache resend(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("resend.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des  11、	找劳力评论
     */
    public static ModelBaseCache labourcomments(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("labourcomments.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des   12、	找劳力评论列表
     */
    public static ModelBaseCache labourcomlist(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("labourcomlist.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des   14、	 出售商品评论
     */
    public static ModelBaseCache shopcomments(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("shopcomments.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des   27、	 我的钱包
     */
    public static ModelBaseCache mybalance(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("mybalance.php?" + reqParam);

            return doExecute(context, callback, modelBase,url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des  57、	我的单列表(用户)
     */
    public static ModelBaseCache userorderlist(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("userorderlist.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des  58、	订单详情（用户）
     */
    public static ModelBaseCache userorderinfo(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("userorderinfo.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 24、	评价
     */
    public static ModelBaseCache drivercomments(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("drivercomments.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 59、	订单付款（用户余额）
     */
    public static ModelBaseCache payment(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("payment.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param context
     * @param callback
     * @param handler
     * @param modelBase
     * @param input
     * @param isNetWork 当需要获取网络数据是的时候发送true,当只需要获取缓存数据的时候放发送
     * @return 如果是需要换成的， 返回ModelBaseCache对象，否则返回null；如果当前没缓存数据， 也返回null
     * @des 44、	顺风车预估费用
     */
    public static ModelBaseCache ranging(Context context, IApiCallback callback, Handler handler, ModelBase modelBase, Object input, boolean isNetWork, String reqParam) {
        String requestStr = "";
        String url = null;
        try {
            url = getFullUrl("map/ranging.php?" + reqParam);

            return doExecute(context, callback, modelBase,
                    url, requestStr, handler, input, isNetWork);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }
    private static String getFullUrl(String method) {
        return EAPIConsts.getUserUrl() + method;
    }
}
