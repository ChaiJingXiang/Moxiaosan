package com.moxiaosan.both.carowner.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.moxiaosan.both.R;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

/**
 * Created by chris on 16/3/11.
 */
public class SelectLocationActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton imgBack;
    private EditText etLocation;
    private TextView tvSure;
    private MapView mMapView;

    LocationClient locClient;
    private BaiduMap mBaiduMap;
    private static BDLocation lastLocation = null;
    private boolean isFirstIn = true; //
    private BaiduSDKReceiver baiduReceiver;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_selectlocation_layout);

        imgBack =(ImageButton)findViewById(R.id.title_back_btn);
        imgBack.setOnClickListener(this);
        etLocation =(EditText)findViewById(R.id.etLocation);
        tvSure =(TextView)findViewById(R.id.tvSure);
        tvSure.setOnClickListener(this);


        // 地图初始化
        mMapView =(MapView)findViewById(R.id.mapViewId);

        initMapView();
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        baiduReceiver = new BaiduSDKReceiver();
        registerReceiver(baiduReceiver, iFilter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_back:
                finish();
                break;

            case R.id.tvSure:

                break;

        }
    }


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class BaiduSDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                EUtil.showToast("网络异常！");
            }
        }
    }

    private void initMapView() {
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mMapView.removeViewAt(1); //隐藏百度logo
        mMapView.setLongClickable(true);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(13.0f);
        mBaiduMap.setMapStatus(msu);

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.NORMAL, true, null));
        showMapWithLocationClient();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void showMapWithLocationClient() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在确定你的位置...");
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface arg0) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                finish();
            }
        });

        progressDialog.show();
        locClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("gcj02"); //设置坐标类型//bd09ll  //gcj02
        option.setScanSpan(30000);
        option.setAddrType("all");
        locClient.setLocOption(option);
        locClient.registerLocationListener(new MyLocationListenner());
    }

    /**
     * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
     */
    class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (!SelectLocationActivity.this.isFinishing()) {  //当前Activity没有被销毁
                if (location == null) {
                    return;
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (lastLocation != null) {
                    if (lastLocation.getLatitude() == location.getLatitude() && lastLocation.getLongitude() == location.getLongitude()) {
                        // mMapView.refresh(); //need this refresh?
                        return;
                    }
                }
                lastLocation = location;

                if (isFirstIn) {
                    if (mBaiduMap != null) {
                        mBaiduMap.clear();
                    }
                }
                isFirstIn = false;

                LatLng llA = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                CoordinateConverter converter = new CoordinateConverter();
                converter.coord(llA);
                converter.from(CoordinateConverter.CoordType.COMMON);
                LatLng convertLatLng = converter.convert();
                OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.chufa_small)).zIndex(4).draggable(false);
                if (mBaiduMap != null) {
                    mBaiduMap.addOverlay(ooA);
                }
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
                mBaiduMap.animateMapStatus(u);
            }

        }

//        @Override
//        public void onReceivePoi(BDLocation poiLocation) {
//            if (poiLocation == null) {
//                return;
//            }
//        }

    }

    @Override
    public void onDestroy() {
        if (locClient != null) {
            locClient.stop();
        }
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        unregisterReceiver(baiduReceiver);
    }

    @Override
    public void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        if (locClient != null) {
            locClient.start();  //开始定位自己
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        if (locClient != null) {
            locClient.stop();
        }
        super.onPause();
        lastLocation = null;
    }
}
