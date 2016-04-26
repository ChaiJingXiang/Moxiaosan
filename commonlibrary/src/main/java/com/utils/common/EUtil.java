package com.utils.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.utils.log.LLog;
import com.utils.string.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Character.UnicodeBlock;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import com.moxiaosan.commonlibrary.R;

/**
 * <p>Title: EUtil.java<／p>
 * <p>Description: <／p>
 *
 * @author Michael
 * @version 1.0
 * @date 2014-11-28
 */
public class EUtil {

    public static int IMAGE_TYPE_BIG = 0;    //680*680
    public static int IMAGE_TYPE_MID = 1;    //480*480
    public static int IMAGE_TYPE_SMALL = 2;  //320*320
    private static String URI_POSTFIX_BIG = "!column1";
    private static String URI_POSTFIX_MID = "!column2";
    private static String URI_POSTFIX_SMALL = "!column3";

    // 显示消息
    public static void showToast(String text) {
        Toast.makeText(KeelApplication.getApp(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String text) {
        Toast.makeText(KeelApplication.getApp(), text, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static String genUpyunFileName(String filePath) {
        String upPath;
        upPath = KeelApplication.getCookie() + filePath + getRandom();
        upPath = StringUtils.md5(upPath);
        return upPath;
    }

    public static String getLocalMacAddress(Context context, boolean isWifi) {
        String macAddr = null;
        if (!isWifi) {
            macAddr = getWifiMacAddr(context, macAddr);
        } else {
            //获取有线网卡的mac地址
            macAddr = getLocalEthernetMacAddress();
        }

        if (null == macAddr) {
            macAddr = getWifiMacAddr(context, macAddr);
        }

        //macAddr="00:92:d1:0b:93:14";
        return macAddr;
    }

    private static String getWifiMacAddr(Context context, String macAddr) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (null != info) {
            String addr = info.getMacAddress();
            if (null != addr) {
                LLog.d("getWifiMacAddr:" + addr);
                macAddr = addr;
            }
        }
        return macAddr;
    }

    /**
     * 获取有线mac.同州.不一定适合.
     *
     * @return
     */
    public static String getLocalEthernetMacAddress() {
        String mac = null;
//        try {
//            Enumeration localEnumeration=NetworkInterface.getNetworkInterfaces();
//
//            while (localEnumeration.hasMoreElements()) {
//                NetworkInterface localNetworkInterface=(NetworkInterface) localEnumeration.nextElement();
//                String interfaceName=localNetworkInterface.getDisplayName();
//
//                if (interfaceName==null) {
//                    continue;
//                }
//
//                if (interfaceName.equals("eth0")) {
//                    // MACAddr = convertMac(localNetworkInterface
//                    // .getHardwareAddress());
//                    mac=convertToMac(localNetworkInterface.getHardwareAddress());
//                    if (mac!=null&&mac.startsWith("0:")) {
//                        mac="0"+mac;
//                    }
//                    break;
//                }
//
//                // byte[] address =
//                // localNetworkInterface.getHardwareAddress();
//                // Log.i(TAG, "mac=" + address.toString());
//                // for (int i = 0; (address != null && i < address.length);
//                // i++)
//                // {
//                // Log.i("Debug", String.format("  : %x", address[i]));
//                // }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
        return mac;
    }

    private static String convertToMac(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            byte b = mac[i];
            int value = 0;
            if (b >= 0 && b <= 16) {
                value = b;
                sb.append("0" + Integer.toHexString(value));
            } else if (b > 16) {
                value = b;
                sb.append(Integer.toHexString(value));
            } else {
                value = 256 + b;
                sb.append(Integer.toHexString(value));
            }
            if (i != mac.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    /**
     * 获取有线的mac地址,测试无效.
     *
     * @return
     * @throws SocketException
     */
    public static String getEthMacAddress() {
        String ipaddress = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::")) {//ipV6的地址
                                LLog.d("getEthMacAddress,ipV6:" + ipaddress);  //结果是ip
                                return ipaddress;
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        LLog.d("getEthMacAddress:" + ipaddress);
        return ipaddress;
    }

    public String getEth0MacAddress() {
        String strMacAddr = null;
        /*try {
            InetAddress ip=getLocalInetAddress();

            byte[] b=NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer=new StringBuffer();
            for (int i=0; i<b.length; i++) {
                if (i!=0) {
                    buffer.append(':');
                }

                String str=Integer.toHexString(b&0xFF);
                buffer.append(str.length()==1 ? 0+str : str);
            }
            strMacAddr=buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return strMacAddr;
    }

    /**
     * 啟動第三方應用
     *
     * @param packName
     */
    public static boolean startAppByPackageName(Context c, String packName) {

        List<ResolveInfo> mAllApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager mPackageManager = c.getPackageManager();

        mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo res : mAllApps) {
            // 该应用的包名和主Activity
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;
            LLog.d("pkg:" + pkg + "===cls==" + cls);
            if (pkg.compareTo(packName) == 0) {
                LLog.d("start pkg:" + pkg + "===cls==" + cls);
                ComponentName componet = new ComponentName(pkg, cls);

                Intent i = new Intent();
                i.setComponent(componet);
                c.startActivity(i);
                return true;
            }
        }
        return false;
    }

    /**
     * 将像素转为dp
     *
     * @param px
     * @return
     */
    public static int convertPxToDp(int px) {
        WindowManager wm = (WindowManager) KeelApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float logicalDensity = metrics.density;
        int dp = Math.round(px / logicalDensity);
        return dp;
    }

    /**
     * 将dp转为像素
     *
     * @param dp
     * @return
     */
    public static int convertDpToPx(int dp) {
        return Math.round(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, KeelApplication.getApp().getResources().getDisplayMetrics())
        );
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getRandom() {
        Random ran = new Random(System.currentTimeMillis());
        int ret = ran.nextInt();
        ret = Math.abs(ret);
        return ret;
    }


    // 判断字符串是否纯数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 中国电信发布中国3G号码段:中国联通185,186;中国移动188,187;中国电信189,180共6个号段。
     * 3G业务专属的180-189号段已基本分配给各运营商使用,
     * 其中180、189分配给中国电信,187、188归中国移动使用,185、186属于新联通。
     * 中国移动拥有号码段：139、138、137、136、135
     * 、134、159、158、157（3G）、152、151、150、188（3G）、187（3G）;14个号段
     * 中国联通拥有号码段：130、131、132、155、156（3G）、186（3G）、185（3G）;6个号段
     * 中国电信拥有号码段：133、153、189（3G）、180（3G）;4个号码段 移动:
     * 2G号段(GSM网络)有139,138,137,136,135,134(0-8),159,158,152,151,150
     * 3G号段(TD-SCDMA网络)有157,188,187 147是移动TD上网卡专用号段. 联通:
     * 2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185 电信:
     * 2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,180
     */
    // 是否是正确的手机号码号段
    public static boolean isMobileNO(String mobile) {

        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();

		/*
        if (mobile != null && EUtil.isNumeric(mobile) && mobile.length() == 11) {
			return true;
		} 
		else {
			return false;
		}
		*/
    }

    /**
     * 是否是手机号格式（11位全数字）
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileNOFormat(String mobile) {

        if (mobile != null && mobile.length() > 0 && isNumeric(mobile)) {
            return true;
        } else {
            return false;
        }
    }


    //判断email格式是否正确
    public static boolean isEmail(String email) {

        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
		
		/*
		if (email != null && email.contains("@")) {
			return true;
		} 
		else {
			return false;
		}
		*/
    }

    // 将Uri转换为文件路径
    public static String uri2Path(ContentResolver resolver, Uri uri) {
        // 两种方法获取文件路径
        String img_path = "";
        try {
            int actual_image_column_index;
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = resolver.query(uri, proj, null, null, null);
            actual_image_column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            img_path = cursor.getString(actual_image_column_index);
            Log.d("", img_path);
        } catch (Exception e) {
            img_path = uri.getPath();
        } finally {
            if (img_path == null) {
                img_path = "";
            }
        }
        return img_path;
    }

    // 用户名是否符合规范
    public static boolean isUsernameFormatCorrect(Context context, String username) {
        if (username == null
                || username.length() == 0) {
            Toast.makeText(context, "账号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 密码是否符合规范
    public static boolean isPasswordFormatCorrect(Context context, String password) {
        if (password == null || password.length() == 0) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            Toast.makeText(context, "请输入6-16位密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 手机号是否符合规范
    public static boolean isMobileFormatCorrect(Context context, String mobile) {
        if (mobile == null || mobile.length() == 0) {
            Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mobile.length() != 11) {
            Toast.makeText(context, "手机号格式有误,请输入11位大陆手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!EUtil.isNumeric(mobile)
                || EUtil.isMobileNO(mobile)) {
            Toast.makeText(context, "手机号格式有误,请输入11位大陆手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 邮箱是否符合规范
    public static boolean isEmailFormatCorrect(Context context, String email) {
        if (email == null || email.length() == 0) {
            Toast.makeText(context, "邮箱地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (EUtil.isEmail(email)) {
            Toast.makeText(context, "邮箱格式有误,请重新输入", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 设备是否有SD卡
    public static boolean isSDCardExist() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // 发送验证码提示
    public static void showSendVerifyCodeDialog(Context context, String mobile,
                                                DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle("确认手机号码")
                .setMessage("我们将发送验证码到这个号码：" + mobile)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    // 发送调用相机的Intent
    public static void dispatchTakePictureIntent(Activity activity, Uri fileUri, int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(takePictureIntent, requestCode);
    }

    /**
     * 发送调用相机的Intent
     *
     * @param activity
     * @param fileUri
     */
    public static void dispatchTakePictureIntent(Activity activity, Uri fileUri) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(takePictureIntent, EConsts.REQ_CODE_TAKE_PICTURE);
    }

    // 发送裁切图片的Intent（不限制宽高比和输出尺寸）
    public static void dispatchCropPictureIntent(Activity activity,
                                                 Uri fileUri, int requestCode) {

        Intent cropPictureIntent = new Intent("com.android.camera.action.CROP");
        cropPictureIntent.setDataAndType(fileUri, "image/*");
        cropPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        cropPictureIntent.putExtra("crop", true);
        cropPictureIntent.putExtra("noFaceDetection", true);
        cropPictureIntent.putExtra("return-data", true); // 是否返回数据
        activity.startActivityForResult(cropPictureIntent, requestCode);
    }

    // 发送裁切图片的Intent（1:1）
    public static void dispatchCropPictureIntent(Activity activity,
                                                 Uri fileUri, int requestCode, int picSize) {

        Intent cropPictureIntent = new Intent("com.android.camera.action.CROP")
                .setDataAndType(fileUri, "image/*")
                .putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                .putExtra("crop", true)
                .putExtra("aspectX", 1) // 图片比例1:1
                .putExtra("aspectY", 1)
                        //.putExtra("outputX", 200)
                        //.putExtra("outputY", 200)
                        //.putExtra("scale", false) //设置是否允许拉伸
                .putExtra("outputFormat", CompressFormat.JPEG.toString()) // 输出jpg格式文件
                .putExtra("noFaceDetection", true)
                .putExtra("return-data", false); // 是否返回数据（小米手机不支持）
        activity.startActivityForResult(cropPictureIntent, requestCode);
    }

    // 发送裁切图片的Intent（1:1）
    public static void dispatchCropPictureIntent(Activity activity, Uri sourceUri,
                                                 Uri outputUri, int requestCode) {

        Intent cropPictureIntent = new Intent("com.android.camera.action.CROP")
                .setDataAndType(sourceUri, "image/*")
                .putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
                .putExtra("crop", true)
                .putExtra("aspectX", 1)
                .putExtra("aspectY", 1)
                .putExtra("outputFormat", CompressFormat.JPEG.toString())
                .putExtra("noFaceDetection", true)
                .putExtra("return-data", false); // 是否返回数据（小米手机不支持）
        activity.startActivityForResult(cropPictureIntent, requestCode);
    }

    // 发送裁切图片的Intent（1:1）
    public static void dispatchCropSquarePictureIntent(Activity activity,
                                                       Uri fileUri, int requestCode, int picSize) {

        Intent cropPictureIntent = new Intent("com.android.camera.action.CROP")
                .setDataAndType(fileUri, "image/*")
                .putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                .putExtra("crop", true)
                .putExtra("aspectX", 1) // 图片比例1:1
                .putExtra("aspectY", 1)
                        // .putExtra("outputX", picSize)
                        // .putExtra("outputY", picSize)
                        // .putExtra("outputFormat",
                        // Bitmap.CompressFormat.JPEG.toString())
                        // .putExtra("scaleUpIfNeeded", false)
                        // .putExtra("scale", false)
                .putExtra("noFaceDetection", true)
                .putExtra("return-data", true); // 是否返回数据
        activity.startActivityForResult(cropPictureIntent, requestCode);
    }

	/*
	// 发送裁切图片的Intent（长宽比1:1）
	public static void dispatchCropPictureIntent(Activity activity,
			Uri fileUri, int requestCode, int picWidth,int picHeight) {

		Intent cropPictureIntent = new Intent("com.android.camera.action.CROP");
		cropPictureIntent.setDataAndType(fileUri, "image/*");
		cropPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		cropPictureIntent.putExtra("crop", true);
		cropPictureIntent.putExtra("aspectX", ratio); // 图片比例1:1
			cropPictureIntent.putExtra("aspectY", 1);
		cropPictureIntent.putExtra("outputX", picWidth);
		cropPictureIntent.putExtra("outputY", picHeight);
		cropPictureIntent.putExtra("noFaceDetection", true);
		cropPictureIntent.putExtra("return-data", true); // 是否返回数据
		activity.startActivityForResult(cropPictureIntent, requestCode);
	}
	*/

    // 发送选择图片的Intent
    public static void dispatchPickPictureIntent(Activity activity,
                                                 int requestCode) {

        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intentWrapper = Intent.createChooser(pickPictureIntent, null);
        activity.startActivityForResult(intentWrapper, requestCode);
    }

    /**
     * 发送选择图片的Intent
     */
    public static void dispatchPickPictureIntent(Activity activity) {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intentWrapper = Intent.createChooser(pickPictureIntent, null);
        activity.startActivityForResult(intentWrapper, EConsts.REQ_CODE_PICK_PICTURE);
    }


    // 缩放Bitmap图片
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    // 保存图片
    public static void saveBitmapToFile(Bitmap bitmap, File file) {

        try {
            BufferedOutputStream bStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(CompressFormat.JPEG, 100, bStream);
            bStream.flush();
            bStream.close();
            bitmap.recycle();
        } catch (IOException e) {
            // Log.d(TAG, e.toString());
        }
    }

    // 拷贝文件（主要是相册中的文件）
    public static void copyFile(File desFile, ContentResolver resolver, Uri srcUri) {

        // 使用ContentProvider通过URI获取原始图片
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver,
                    srcUri);
            EUtil.saveBitmapToFile(bitmap, desFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.d("EUtil", e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d("EUtil", e.getMessage());
        }
    }

    /**
     * 发送拍摄视频的Intent
     *
     * @param activity
     * @param requestCode
     */
    public static void dispatchPickVideoIntent(Activity activity, int requestCode) {
        //视频
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("video/*");  // 选择视频（mp4 3gp 是android支持的视频格式）
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        activity.startActivityForResult(wrapperIntent, requestCode);
    }

    /**
     * 发送拍摄视频的Intent
     *
     * @param activity
     */
    public static void dispatchPickVideoIntent(Activity activity) {
        //视频
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("video/*");  // 选择视频（mp4 3gp 是android支持的视频格式）
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        activity.startActivityForResult(wrapperIntent, EConsts.REQ_CODE_PICK_VIDEO);
    }

    /**
     * 拍摄视频（默认参数）
     *
     * @param activity
     * @param fileUri
     * @param requestCode
     */
    public static void dispatchTakeVideoIntent(Activity activity, Uri fileUri, int requestCode) {
        //拍摄视频
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 设置视频录制的质量，0为低质量，1为高质量。
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍摄视频（默认参数）
     *
     * @param activity
     * @param fileUri
     */
    public static void dispatchTakeVideoIntent(Activity activity, Uri fileUri) {
        //拍摄视频
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 设置视频录制的质量，0为低质量，1为高质量。
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, EConsts.REQ_CODE_TAKE_VIDEO);
    }

    /**
     * 拍摄视频
     *
     * @param activity
     * @param quality
     * @param sizeLimit
     * @param durationLimit
     * @param fileUri
     * @param requestCode
     */
    public static void dispatchTakeVideoIntent(Activity activity, int quality,
                                               long sizeLimit, long durationLimit, Uri fileUri, int requestCode) {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality); // 设置视频录制的质量，0为低质量，1为高质量。
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeLimit); // 指定视频最大允许的尺寸，单位为byte。
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit); // 设置视频最大允许录制的时长，单位为毫秒。
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 选择文件
     *
     * @param activity
     */
    public static void dispatchPickFileIntent(Activity activity) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        Intent intentWrapper = Intent.createChooser(intent, null);
        activity.startActivityForResult(intentWrapper, EConsts.REQ_CODE_PICK_FILE);
    }


    /**
     * 设置ScrollView中的ListView
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 获取手机的IMEI
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        String imei = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        // 处理DeviceId为空的情况

        return imei;
    }

    /**
     * 获取程序版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 拷贝文件
     *
     * @param srcFile
     * @param desFile
     * @return
     */
    public static boolean copyFile(String srcFile, String desFile) {

        try {
            InputStream inputStream = new FileInputStream(srcFile);
            OutputStream outputStream = new FileOutputStream(desFile);
            byte bytes[] = new byte[1024];
            int count;
            while ((count = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, count);
            }
            inputStream.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcFile
     * @param desFile
     * @return
     */
    public static boolean copyFile(File srcFile, File desFile) {

        try {
            InputStream inputStream = new FileInputStream(srcFile);
            OutputStream outputStream = new FileOutputStream(desFile);
            byte bytes[] = new byte[1024];
            int count;
            while ((count = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, count);
            }
            inputStream.close();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取指定文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {

        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        // bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
        // 		ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 保存字符型变量
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setStringToAppSetting(Context context,
                                             String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(EConsts.Key.APP_SETTING, 0);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 获取字符型变量
     *
     * @param context
     * @param key
     * @return
     */
    public static String getStringFromAppSetting(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(EConsts.Key.APP_SETTING, 0);
        return sp.getString(key, "");
    }


    /**
     * 保存整形变量
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setIntToAppSetting(Context context, String key,
                                          int value) {
        SharedPreferences sp = context.getSharedPreferences(EConsts.Key.APP_SETTING, 0);
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 获取整形变量
     *
     * @param context
     * @param key
     * @return
     */
    public static int getIntFromAppSetting(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(EConsts.Key.APP_SETTING, 0);
        return sp.getInt(key, 0);
    }

    /**
     * 设置布尔型变量
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static void setBooleanToAppSetting(Context context, String key
            , boolean value) {
        SharedPreferences sp = context.getSharedPreferences(EConsts.Key.APP_SETTING, 0);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取布尔型变量
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBooleanFromAppSetting(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(EConsts.Key.APP_SETTING, 0);
        return sp.getBoolean(key, false);
    }

    /**
     * 弹出删除文件提示
     *
     * @param context
     * @param listener
     */
    public static void showFileDeleteDialog(Context context, DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("确定要删除该文件？")
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .create().show();
    }

    /**
     * 弹出终止文件上传提示
     *
     * @param context
     * @param listener
     */
    public static void showFileUploadCancelDialog(Context context, DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("您确定要终止上传该文件吗？")
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .create().show();
    }

    //获取固定格式的时间字符串，目前为  yyyy-MM-dd hh:mm:ss
    public static String getFormatFromDate(Date now) {
        now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//HH为24小时制, hh为12小时制
        //formatDate.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));


        String str = formatDate.format(now);
        return str;
    }


    /**
     * 获取文件大小
     *
     * @param fileSize
     * @return
     */
    public static String getFileSize(long fileSize) {
        String result = "";
        if (fileSize < 1024) { // B
            result = fileSize + "B";
        } else if (fileSize < 1024 * 1024) { // KB
            result = String.format("%.1f", fileSize * 1.0 / 1024) + "KB";
        } else if (fileSize < 1024 * 1024 * 1024) { // MB
            result = String.format("%.1f", fileSize * 1.0 / 1024 * 1024) + "MB";
        } else {
            result = "未知";
        }
        return result;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = String.format("%.1fB", (double) fileS);
        } else if (fileS < 1048576) {
            // fileSizeString = df.format((double) fileS / 1024) + "K";
            fileSizeString = String.format("%.1fK", (double) fileS / 1024);
        } else if (fileS < 1073741824) {
            //fileSizeString = df.format((double) fileS / 1048576) + "M";
            fileSizeString = String.format("%.1fM", (double) fileS / 1048576);
        } else {
            //fileSizeString = df.format((double) fileS / 1073741824) + "G";
            fileSizeString = String.format("%.1fG", (double) fileS / 1073741824);
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 获取版本号
     *
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context) {

        String version = "未知";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取App缓存文件存放路径（根据用户区分）
     *
     * @param context
     * @return
     */
    public static File getAppCacheFileDir(Context context) {

        File rootDir = context.getExternalFilesDir(null); // 对外可见
        if (rootDir != null) {
            File cacheDir = new File(rootDir, KeelApplication.getUserID() + "/cache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            return cacheDir;
        } else {
            return rootDir;
        }
    }

    /**
     * 获取语音聊天缓存文件
     *
     * @param context
     * @param chatId
     * @return
     */
    public static File getChatVoiceCacheDir(Context context, String chatId) {

        File rootDir = getAppCacheFileDir(context);
        if (rootDir != null) {
            File cacheDir = new File(rootDir, "chat/" + chatId);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            return cacheDir;
        } else {
            return rootDir;
        }
    }

    /**
     * 获取语音聊天缓存文件
     *
     * @param context
     * @return
     */
    public static File getMeetingVoiceCacheDir(Context context, String mucId, String topicId) {

        File rootDir = getAppCacheFileDir(context);
        if (rootDir != null) {
            File cacheDir = new File(rootDir, "chat/" + mucId + "/" + topicId);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            return cacheDir;
        } else {
            return rootDir;
        }
    }

    /**
     * 获取App缓存文件存放路径
     * @param context
     * @return
     */
	/*
	public static File getAppCacheDir(Context context) {

		File rootDir = context.getExternalCacheDir();
		if (rootDir != null) {
			File cacheDir = new File(rootDir, "user/" + KeelApplication.getUserID());
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			return cacheDir;
		}
		return rootDir;
	}
	*/

    /**
     * 获取App文件存放路径（根据用户区分）
     *
     * @param context
     * @return
     */
    public static File getAppFileDir(Context context) {

        File rootDir = context.getExternalFilesDir(null); // 对外可见
        if (rootDir != null) {
            File fileDir = new File(rootDir, KeelApplication.getUserID() + "/jtfile");

            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            return fileDir;
        } else {
            return rootDir;
        }
    }

    /**
     * 获取App文件存放路径（根据用户区分）
     *
     * @param context
     * @return
     */
    public static File getAppFileDirEx(Context context) {

        File rootDir = Environment.getExternalStorageDirectory();
        if (rootDir != null) {
            File fileDir = new File(rootDir, context.getPackageName() + "/user/" + KeelApplication.getUserID());
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            return fileDir;
        }

        return rootDir;
    }

    /**
     * 聊天缓存文件记录
     * @param context
     * @param chatId
     * @return
     */
	/*
	public static File getAppChatCacheDir(Context context,String chatId, int type){

		File rootDir = EUtil.getAppCacheDir(context);
		if(rootDir != null){
			File chatDir = null;
			switch(type){
			case JTFile.TYPE_IMAGE:
				chatDir = new File(rootDir, "chat/" +  chatId + "/image");
				break;
			case JTFile.TYPE_AUDIO:
				chatDir = new File(rootDir, "chat/" +  chatId + "/audio");
				break;
			case JTFile.TYPE_VIDEO:
				chatDir = new File(rootDir, "chat/" + chatId + "/video");
				break;
			default :
				chatDir = new File(rootDir, "chat/" + chatId + "/file");
				break;
			}
			if(!chatDir.exists()){
				chatDir.mkdirs();
			}
		}
		return rootDir;
	}
	*/

//	/**
//	 * 获取指定目录下的所有一级文件
//	 * @param fileDir
//	 * @return
//	 */
//	public static List<JTFile> getListJTFile(File fileDir) {
//		List<JTFile> listJTFile = new ArrayList<JTFile>();
//		if (fileDir != null && fileDir.isDirectory()) {
//			File[] listFile = fileDir.listFiles();
//			if (listFile != null) {
//				for (int i = 0; i < listFile.length; i++) {
//					JTFile jtfile = EUtil.file2JTFile(listFile[i]);
//					if (jtfile != null) {
//						listJTFile.add(jtfile);
//					}
//				}
//			}
//		}
//		return listJTFile;
//	}
//
//	/**
//	 * File 转 JTFile
//	 * @param file
//	 * @return
//	 */
//	public static JTFile file2JTFile(File file) {
//		JTFile jtfile = null;
//		if (file != null && file.isFile()) {
//			jtfile = new JTFile();
//			jtfile.mLocalFilePath = file.getAbsolutePath();
//			jtfile.mFileName = file.getName();
//			if (file.getName().lastIndexOf(".") < 0) {
//				jtfile.mSuffixName = "无";
//			}
//			else{
//				jtfile.mSuffixName = file.getName().substring(
//						file.getName().lastIndexOf(".") + 1);
//			}
//			jtfile.mFileSize = file.length();
//			jtfile.mCreateTime = file.lastModified();
//		}
//		return jtfile;
//	}

    /**
     * 加密相关
     */
    private final static int JELLY_BEAN_4_2 = 17;

    /**
     * 加密
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String encrypt(String src) {
        String encryptKey = "";
        try {
            byte[] rawKey = getRawKey(EConsts.PASSWORD_KEY.getBytes());
            byte[] result = encrypt(rawKey, src.getBytes());
            encryptKey = toHex(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptKey;
    }

    /**
     * 解密
     *
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypted) {
        String decryptKey = "";
        try {
            byte[] rawKey = getRawKey(EConsts.PASSWORD_KEY.getBytes());
            byte[] enc = toByte(encrypted);
            byte[] result = decrypt(rawKey, enc);
            decryptKey = new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptKey;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_4_2) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        kgen.init(256, sr); //256 bits or 128 bits,192bits
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

//    /**
//     *
//     * @param filePath
//     * @return
//     */
//	public static JTFile createJTFile(String filePath) {
//		JTFile jtfile = null;
//		if (filePath == null || filePath.length() <= 0) {
//			return null;
//		}
//		File file = new File(filePath);
//		if (!file.exists() || !file.isFile()) {
//			return null;
//		}
//		jtfile = new JTFile();
//		jtfile.mLocalFilePath = filePath;
//		jtfile.mFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
//		jtfile.mFileSize = file.length();
//		jtfile.mTaskId = TaskIDMaker.getTaskId(KeelApplication.getUser().mUserName);
//		return jtfile;
//	}

    /**
     * @return
     */
	/*
	public static JTFile createJTFile(Context context,Uri uri) {
		JTFile jtfile = null;
		String filePath = EUtil.uri2Path(context.getContentResolver(), uri);
		if (filePath == null || filePath.length() <= 0) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		jtfile = new JTFile();
		// jtfile.mUri = uri;
		jtfile.mLocalFilePath = filePath;
		jtfile.mFileName = filePath.substring(filePath
				.lastIndexOf(File.separator) + 1);
		jtfile.mFileSize = file.length();
		jtfile.mTaskId = TaskIDMaker.getTaskId(KeelApplication.getUser().mUserName);
		return jtfile;
	}
	*/


    // @SuppressLint("NewApi")
//    public static String getThumbUploadPath(Context context, String oldPath, int bitmapMaxWidth) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(oldPath, options);
//        int height = options.outHeight;
//        int width = options.outWidth;
//        int reqHeight = 0;
//        // 设置为最大宽度为800
//        bitmapMaxWidth = 800;
//        int reqWidth = bitmapMaxWidth;
//        reqHeight = (reqWidth * height) / width;
//        // 在内存中创建bitmap对象，这个对象按照缩放大小创建的
//        options.inSampleSize = calculateInSampleSize(options, bitmapMaxWidth,
//                reqHeight);
//        options.inJustDecodeBounds = false;
//        Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
//        if (bitmap == null) {
//            return "";
//        }
//        // Log.d("压缩前", bitmap.getByteCount() + "");
//        // Log.e("asdasdas", "reqWidth->"+reqWidth+"---reqHeight->"+reqHeight);
//        Bitmap bbb = compressImage(Bitmap.createScaledBitmap(bitmap,
//                bitmapMaxWidth, reqHeight, false));
//        if (bbb == null) {
//            return "";
//        }
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                .format(new Date());
//        return saveImg(context, bbb, timeStamp);
//    }


    // 上传的文件是否存在
	/*
	public static boolean isUploadedFileExist(JTFile jtfile){

		if(jtfile == null || jtfile.mUrl == null){
			return false;
		}
		if(KeelApplication.getApp().mAppData.getListUploadedJTFile() != null
				&& KeelApplication.getApp().mAppData.getListUploadedJTFile().size() > 0){
			for(JTFile file:KeelApplication.getApp().mAppData.getListUploadedJTFile()){
				if(file.mUrl.equals(jtfile.mUrl)){
					return true;
				}
			}
		}
		return false;
	}
	*/


    /**
     * 是否支持此视频格式
     *
     * @param format
     * @return
     */
    public static boolean isVideoFormatSupport(String format) {
        boolean result = false;
        String[] formatSet = new String[]{"mp4", "3gp"}; // "avi", "wmv",
        if (format != null) {
            for (int i = 0; i < formatSet.length; i++) {
                if (formatSet[i].equalsIgnoreCase(format)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 是否支持此图片格式
     *
     * @param format
     * @return
     */
    public static boolean isPictureFormatSupport(String format) {
        boolean result = false;
        String[] formatSet = new String[]{"bmp", "jpg", "jpeg", "png"};
        if (format != null) {
            for (int i = 0; i < formatSet.length; i++) {
                if (formatSet[i].equalsIgnoreCase(format)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取视频的截图
     *
     * @param videoPath
     * @return
     */
    public static String getVideoThumbnail(Context context, String videoPath) {
        String thumbnailPath = "";
        String extension = EUtil.getExtensionName(videoPath);
        if (isVideoFormatSupport(extension)) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath);
            Bitmap bmpOriginal = mediaMetadataRetriever.getFrameAtTime(0);
            if (bmpOriginal != null) {
                File dir = EUtil.getAppCacheFileDir(context);
                if (dir == null) {
                    return thumbnailPath;
                }
                File file = new File(dir, DateFormat.format("yyyyMMddkkmmss", System.currentTimeMillis()) + ".jpg");
                if (file.exists()) {
                    file.delete();
                }
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bmpOriginal.compress(CompressFormat.JPEG, 50, bos);
                    bos.flush();
                    bos.close();
                    thumbnailPath = file.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return thumbnailPath;
    }

    /**
     * 计算每个月的天数
     *
     * @param year  年份
     * @param month 月份
     * @return days 每个月的天数
     */
    public static int getDaysOfMonth(int year, int month) {

        int days = 0;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            days = 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            days = 30;
        } else { // 2月份，闰年29天、平年28天
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        }
        return days;
    }

//	/**
//	 * 获取ImageLoader默认显示配置
//	 * @return
//	 */
//	public static DisplayImageOptions getDefaultImageLoaderDisplayOptions(){
//
//		return new DisplayImageOptions.Builder()
//		.bitmapConfig(Bitmap.Config.RGB_565)
//		.imageScaleType(ImageScaleType.EXACTLY)
//		.cacheInMemory(true)
//		.cacheOnDisc(true)
//		.build();
//	}

    /**
     * 将文件保存到用户目录
     *
     * @return
     */
    public static boolean saveFileToUserDir(Context context, File srcFile) {

        if (srcFile == null || !srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        File dir = EUtil.getAppFileDir(context);
        if (dir == null) {
            return false;
        }
        File desFile = new File(dir, srcFile.getName());
        if (desFile.exists()) {
            desFile.delete();
        }
        return EUtil.copyFile(srcFile, desFile);
    }

    /**
     * 将图片保存到用户目录
     *
     * @return
     */
    public static boolean saveImageToUserDir(Context context, File srcFile) {

        if (srcFile == null || !srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        File dir = EUtil.getAppFileDir(context);
        if (dir == null) {
            return false;
        }
        File desFile = null;
        if (!srcFile.getName().contains(".")) { // 没有后缀名
            desFile = new File(dir, srcFile.getName() + ".jpg");
        } else {
            desFile = new File(dir, srcFile.getName());
        }
        if (desFile.exists()) {
            desFile.delete();
        }
        return EUtil.copyFile(srcFile, desFile);
    }

    /**
     * 禁用表情
     *
     * @param source
     * @return
     */
    public static char switchChar(char source) {
        UnicodeBlock ub = UnicodeBlock.of(source);
        if (Arrays.asList(ubs).contains(ub)) {
            return source;
        }
        return '.';
    }

    private static UnicodeBlock[] ubs = new UnicodeBlock[]{
            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS,
            UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION, UnicodeBlock.BASIC_LATIN,
            UnicodeBlock.LATIN_1_SUPPLEMENT, UnicodeBlock.LATIN_EXTENDED_A,
            UnicodeBlock.LATIN_EXTENDED_B, UnicodeBlock.KATAKANA,
            UnicodeBlock.HIRAGANA, UnicodeBlock.BOPOMOFO
    };

    /**
     * 异步加载图片
     *
     * @param imageView 显示图片的view
     * @param uri       图片地址
     * @param options   显示图片时需要的配置信息
     * @param listener  图片加载过程监听
     */

    public static void displayImage(ImageView imageView, String uri, DisplayImageOptions options, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
    }

    public static void displayImage(ImageView imageView, String uri, int stubImageRes, int emptyImageRes, int errorImageRes, ImageLoadingListener listener) {
        displayImage(imageView, uri, getSampleDisplayImageOptions(stubImageRes, emptyImageRes, errorImageRes), listener);
    }

    public static void displayImage(ImageView imageView, String uri, int stubImageRes, int errorImageRes, ImageLoadingListener listener) {
        displayImage(imageView, uri, getSampleDisplayImageOptions(stubImageRes, errorImageRes, errorImageRes), listener);
    }

    public static void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), null);
    }

    public static String displayImage(ImageView imageView, String uri, int type) {
        if (!uri.contains("!bac")) {
            String uriPostfix = "";
            if (type == IMAGE_TYPE_BIG) {
                uriPostfix = URI_POSTFIX_BIG;
            } else if (type == IMAGE_TYPE_MID) {
                uriPostfix = URI_POSTFIX_MID;
            } else if (type == IMAGE_TYPE_SMALL) {
                uriPostfix = URI_POSTFIX_SMALL;
            }
            displayImage(imageView, uri + uriPostfix, getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), null);
            return uri + uriPostfix;
        } else {
            displayImage(imageView, uri, getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), null);
            return uri;
        }
    }

    /**
     * 异步加载图片方法
     *
     * @param imageView     显示图片的view
     * @param uri           图片地址
     * @param stubImageRes  默认背景图片
     * @param errorImageRes 加载错误图片
     */
    public static void displayImage(ImageView imageView, String uri, int stubImageRes, int errorImageRes) {
        displayImage(imageView, uri, getSampleDisplayImageOptions(stubImageRes, errorImageRes, errorImageRes), null);
    }

    /**
     * 返回ImageLoader加载图片时需要的参数
     *
     * @param stubImageRes  默认背景图片
     * @param emptyImageRes 加载地址为空的图片
     * @param errorImageRes 加载错误时显示的图片
     * @return
     */
    public static DisplayImageOptions getSampleDisplayImageOptions(int stubImageRes, int emptyImageRes, int errorImageRes) {
        return new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showStubImage(stubImageRes)
                .showImageForEmptyUri(emptyImageRes)
                .showImageOnFail(errorImageRes)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }
}
 