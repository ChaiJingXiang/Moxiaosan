package com.utils.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.utils.api.EAPIConsts;
import com.utils.common.KeelApplication;
import com.utils.log.LLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;


/**
 * @version 1.00.00
 * @description: http访问接口，包括get和post,disconnect
 * @author: Michael 13-04-19
 */
public class EHttpAgent {
    private final static String TAG = "EHttpAgent";
    public static final int TIMEOUT = 45000;
    private Context mContext;

    private InputStream is;

    public static final int CODE_HTTP_CODE_SUCCEED = 200;
    public static final int CODE_HTTP_CODE_LOCATION = 300;
    public static final String CODE_HTTP_FAIL = "-1";
    public static final String CODE_HTTP_SUCCEED = "200";

//    public static final String CMWAP = "cmwap";
//    public static final String CMNET = "cmnet";
//    public static final String WAP_3G = "3gwap";
//    public static final String UNIWAP = "uniwap";
//    public static final String CTWAP = "ctwap";
//    public static final int TYPE_CM_CU_WAP = 4;// 10.0.0.172移动
//    public static final int TYPE_CT_WAP = 5;//    10.0.0.200联通
//    public static final int TYPE_OTHER_NET = 6;// wifi，数据net
//    //
//    public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private HttpURLConnection conn;

    public EHttpAgent(Context context) {
        this.mContext = context;
    }

    /**
     * 检查当前网络状态是否可用
     */
    public static boolean isAvailable(Context context) {
        final ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager != null) {
            final NetworkInfo[] info = cwjManager.getAllNetworkInfo();
            if (info != null) {
                final int size = info.length;
                for (int i = 0; i < size; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查网络类型
     */
//    private static int checkNetworkType(Context mContext) {
//        try {
//            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            final NetworkInfo mobNetInfoActivity = connectivityManager
//                    .getActiveNetworkInfo();
//            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
//                return TYPE_OTHER_NET;
//            } else {
//                final int netType = mobNetInfoActivity.getType();
//                if (netType == ConnectivityManager.TYPE_WIFI) {
//                    return TYPE_OTHER_NET;
//                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
//
//                    final Cursor c = mContext.getContentResolver().query(
//                            PREFERRED_APN_URI, null, null, null, null);
//                    if (c != null) {
//                        c.moveToFirst();
//                        final String user = c.getString(c
//                                .getColumnIndex("user"));
//                        if (!TextUtils.isEmpty(user)) {
//                            if (user.startsWith(CTWAP)) {
//                                return TYPE_CT_WAP;
//                            }
//                        }
//                        c.close();
//                    }
//
//                    final String netMode = mobNetInfoActivity.getExtraInfo();
//                    if (netMode != null) {
//                        if (netMode.equals(CMWAP)
//                                || netMode.equals(WAP_3G)
//                                || netMode.equals(UNIWAP)) {
//                            return TYPE_CM_CU_WAP;
//                        }
//
//                    }
//
//                }
//            }
//        } catch (Exception ex) {
//            return TYPE_OTHER_NET;
//        }
//        return TYPE_OTHER_NET;
//    }


    private static boolean initSSLSocket = false;

    /**
     * 提交POST请求
     */
    public String[] getPostNetMessage(String url, String request) {
        System.gc();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        String state = null;
        String response = null;
        String cookieStr = KeelApplication.getApp().getCookie();
        String actionUrl = url;
        LLog.d("url: " + url);

        if (!isAvailable(mContext)) {
            return new String[]{CODE_HTTP_FAIL, ""};
        }
        try {
//            if (initSSLSocket == false) {
//                final TLSOnlySocketFactory tlsOnlySocketFactory = new TLSOnlySocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());
//                HttpsURLConnection.setDefaultSSLSocketFactory(tlsOnlySocketFactory);
//                initSSLSocket = true;
//            }
            OutputStream outStream = null;
            final URL conUrl = new URL(actionUrl);
            conn = (HttpURLConnection) conUrl.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Close");
            conn.setRequestProperty("Content-Type", "application/json;Charset=UTF-8");
            conn.setRequestProperty("Accept-Language", "zh,zh-cn");
            conn.setRequestProperty("Accept-Encoding", "gzip,default");
//        conn.setRequestProperty("Content-Encoding", "gzip");
//        byte[] targetRequest = GZIPByteEncoder.encodeByteArray(request.getBytes());
            byte[] targetRequest = request.getBytes();
            //发送Cookie
            if (cookieStr != null && "".equals(cookieStr)) {
                conn.setRequestProperty("Cookie", cookieStr);
            }
            outStream = conn.getOutputStream();
            outStream.write(targetRequest);
            outStream.flush();
            outStream.close();
            conn.connect();

            //判断httpCode
            int httpCode = conn.getResponseCode();
            LLog.d("ResponseCode=" + httpCode);
            //MODIFY LYS 300 302 304 都是 跳转指令
            if (CODE_HTTP_CODE_SUCCEED != httpCode && (httpCode < CODE_HTTP_CODE_LOCATION || httpCode >= 400)) {
                return new String[]{String.valueOf(httpCode), "网络错误，请稍后重试"};
            }

            //保存Cookie
            /**
             * 这里未对cookie做系统处理，建议稍后完善，修改请求地址需要注意， cookie中Domain与请求地址需要做判断 即 ：str[j].contains("Domain") && str[j].contains(s)
             */
            Map<String, List<String>> map = conn.getHeaderFields();
            if (map.get("set-cookie") != null) {
                for (int i = 0; i < map.get("set-cookie").size(); i++) {
                    String cookie = map.get("set-cookie").get(i);
                    if (cookie != null && !"".equals(cookie)) {
                        String str[] = cookie.split(";");
                        String s;
                        int l = EAPIConsts.getActivityUrl().length() - 1;
                        if (EAPIConsts.getAgentUrl().contains("https")) {
                            s = EAPIConsts.getActivityUrl().substring(8, l);
                        } else {
                            s = EAPIConsts.getActivityUrl().substring(7, l);
                        }

                        for (int j = 0; j < str.length; j++) {
                            if (str[j].contains("Domain") && str[j].contains(s)) {
                                KeelApplication.getApp().saveCookie(cookie);
                            }
                        }
                    }
                }
            }

            final long len = conn.getContentLength();
            LLog.e("DXHttpAgent==>sendMessage==>len=" + len);
            is = conn.getInputStream();
            state = httpCode + "";
            String contentEncoding = conn.getRequestProperty("Content-Encoding");
            try {
                if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                    LLog.e("send gzip");
                    response = readDataForZgip(is, "UTF-8");
                } else {
                    LLog.e("send unzip");
                    response = readData(is, "UTF-8");
                }
            } catch (OutOfMemoryError ex) {
                LLog.e(ex.toString());
            }
            LLog.i(response);
        } catch (OutOfMemoryError e) {
            LLog.e(e.toString());
            return new String[]{CODE_HTTP_FAIL, ""};
        } catch (Exception e) {
            LLog.e(e);
            return new String[]{CODE_HTTP_FAIL, ""};
        } finally {
            LLog.i("DXHttpAgent==>sendMessage==>finally");
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (Exception e) {
                    LLog.e(e);
                }
            }
            if (conn != null) {
                try {
                    conn.disconnect();
                    conn = null;
                } catch (Exception e) {
                    LLog.e(e);
                }
            }
        }
        return new String[]{state, response};
    }


    /**
     * 提交GET请求
     */

    public String[] getNetMessage(String url) {
        //回收内存
        System.gc();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        String state = null;
        String response = null;
        String actionUrl = url;
        LLog.d("url: " + url);

        if (!isAvailable(mContext)) {
            return new String[]{CODE_HTTP_FAIL, ""};
        }
        try {
            final URL conUrl = new URL(actionUrl);
            conn = (HttpURLConnection) conUrl.openConnection();
            conn.connect();

            //判断httpCode
            int httpCode = conn.getResponseCode();
            LLog.d("ResponseCode=" + httpCode);
            //MODIFY LYS 300 302 304 都是 跳转指令
            if (CODE_HTTP_CODE_SUCCEED != httpCode && (httpCode < CODE_HTTP_CODE_LOCATION || httpCode >= 400)) {
                return new String[]{String.valueOf(httpCode), "网络错误，请稍后重试"};
            }
//            Map<String, List<String>> map = conn.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }

            final long len = conn.getContentLength();
            LLog.e("DXHttpAgent==>sendMessage==>len=" + len);
            is = conn.getInputStream();
            state = httpCode + "";
            String contentEncoding = conn.getRequestProperty("Content-Encoding");
            try {
                if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                    LLog.e("send gzip");
                    response = readDataForZgip(is, "UTF-8");
                } else {
                    LLog.e("send unzip");
                    response = readData(is, "UTF-8");
                }
            } catch (OutOfMemoryError ex) {
                LLog.e(ex.toString());
            }
            LLog.i(response);
        } catch (OutOfMemoryError e) {
            LLog.e(e.toString());
            return new String[]{CODE_HTTP_FAIL, ""};
        } catch (Exception e) {
            LLog.e(e);
            return new String[]{CODE_HTTP_FAIL, ""};
        } finally {
            LLog.i("DXHttpAgent==>sendMessage==>finally");
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (Exception e) {
                    LLog.e(e);
                }
            }
            if (conn != null) {
                try {
                    conn.disconnect();
                    conn = null;
                } catch (Exception e) {
                    LLog.e(e);
                }
            }
        }
        return new String[]{state, response};
    }

    private String readData(InputStream inSream, String charsetName) throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inSream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }

        final byte[] data = outStream.toByteArray();
        outStream.close();
        inSream.close();

        return new String(data, charsetName);
    }

    private String readDataForZgip(InputStream inStream, String charsetName) throws Exception {
        return readDataForZgip(inStream, charsetName, null);
    }

    private String readDataForZgip(InputStream inStream, String charsetName, Handler handler) throws Exception {
        String data1 = null;
        byte[] data = null;
        try {
            final GZIPInputStream gzipStream = new GZIPInputStream(inStream);
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            final byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = gzipStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }

            data = outStream.toByteArray();
            outStream.close();
            gzipStream.close();
            inStream.close();
            data1 = new String(data, charsetName);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        if (data != null) {
            data = null;
        }

        return data1;
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (is != null) {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

    }
}
