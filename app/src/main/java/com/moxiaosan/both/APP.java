package com.moxiaosan.both;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.moxiaosan.both.common.ui.activity.CityPositionActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.KeelApplication;
import com.utils.file.FileConstants;

import java.io.File;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.Updateposi;

/**
 * Created by qiangfeng on 16/2/29.
 */
public class APP extends KeelApplication {
    private static APP mInstance = null;
    public static final String TAG = "App";

    private final static String USER_FILE = "userinfo";

    public LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();

    @Override
    public void onCreate() {
        mInstance = this;
        super.onCreate();
        //初始化百度地图  在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        mLocationClient = new LocationClient(this);

        mLocationClient.registerLocationListener(myListener);
        requestLocationInfo();

        if (!ImageLoader.getInstance().isInited()) {
            File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), FileConstants.getImageSaveFilePath());
            // 初始化ImageLoader
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .delayBeforeLoading(100)
                    .build();
            // 参数配置
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    getApplicationContext()).defaultDisplayImageOptions(options)
                    .discCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCacheExtraOptions(480, 800)
                    .threadPoolSize(3)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .memoryCacheSize(5 * 1024 * 1024)
                    .discCacheSize(5 * 1024 * 1024)
                    .discCacheFileCount(100)
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .build();
            ImageLoader.getInstance().init(config);
        }

    }

    /**
     * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sp.edit();
//            stopLocationClient();
            if (location == null) {
                return;
            }
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            String city = "";
            if (location.getCity().contains("市")) {
                city = location.getCity().replace("市", "");
            }
            editor.putString("province", location.getProvince());
            editor.putString("city", city);
            editor.putString("district", location.getDistrict());
            editor.putString("latitude", String.valueOf(latitude));
            editor.putString("longitude", String.valueOf(longitude));
            editor.commit();
            sendBroadCast(location.getCity(),location.getDistrict(), latitude, longitude);
            if (AppData.getInstance().getUserEntity() != null){
                updateLocation(latitude,longitude);
            }
//            LLog.i("latitude=="+latitude+"==longitude=="+longitude);
        }
    }

    /**
     * 得到发送广播
     *
     * @param address
     */
    public void sendBroadCast(String address,String district, Double latitude, Double longitude) {
//        stopLocationClient();

        Intent intent = new Intent(CityPositionActivity.LOCATION_BCR);
        intent.putExtra("address", address);
        intent.putExtra("district", district);
        intent.putExtra("latitude", latitude + "");
        intent.putExtra("longitude", longitude + "");
        sendBroadcast(intent);
    }

    /**
     * 停止定位
     */
    public void stopLocationClient() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    /**
     * 发起定位
     */
    public void requestLocationInfo() {
        setLocationOption();

        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }

        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }



    /**
     * 设置相关参数
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setServiceName("com.baidu.location.service_v2.9");
        option.setScanSpan(40*1000);  //40秒定位一次
//        option.setPoiExtraInfo(true);
        option.setAddrType("all");
//        option.setPoiNumber(10);
        option.disableCache(true);
        mLocationClient.setLocOption(option);
    }

    public static APP getInstance() {
        return mInstance;
    }

    private void updateLocation(double lat,double lng){
        CarReqUtils.updateposi(this,iApiCallback,null,new Updateposi(),"updateposi",true,
                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername())
                        .putValue("lat", lat).putValue("lng",lng).createMap())
        );
    }

    IApiCallback iApiCallback=new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (output == null){
                return;
            }
            if (output instanceof Updateposi){
                Updateposi updateposi= (Updateposi) output;
                if (updateposi.getRes().equals("0")){
//                    LLog.i("username=="+AppData.getInstance().getUserEntity().getUsername()+"成功更新定位");
                }
            }
        }
    };
}
