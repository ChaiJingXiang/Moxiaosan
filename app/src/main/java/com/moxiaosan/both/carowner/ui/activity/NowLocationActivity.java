package com.moxiaosan.both.carowner.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.moxiaosan.both.R;
import com.moxiaosan.both.utils.BaiduLocation;
import com.moxiaosan.both.utils.BaiduapiOffline;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.DisplayUtil;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.Mqtt;
import consumer.model.RespLocation;

/**
 * Created by chris on 16/3/4.
 */
public class NowLocationActivity extends BaseActivity implements IApiCallback {

    private MapView mMapView;
    private TextView tvLocation;
    private BaiduMap mBaiduMap;
    private InfoWindow currentInfoWindow;
    private GeoCoder geoCoder;
    private ExitDialog dialog;
    private Timer timer ;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            mBaiduMap.clear();

            CarReqUtils.currentposi(NowLocationActivity.this,NowLocationActivity.this, null, new RespLocation(), "Position", true,
                    StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_nowlocation_layout);

        showActionBar(true);

        setActionBarName("当前位置");

        tvLocation = (TextView) findViewById(R.id.nowLocationId);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mapViewId);

        initMapView();

        showLoadingDialog();
        CarReqUtils.currentposi(this, this, null, new RespLocation(), "Position", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));


        // 创建地理编码检索实例
        geoCoder = GeoCoder.newInstance();
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);

        timer =new Timer(true);

        timer.schedule(task,30000,30000);

    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        // 反地理编码查询结果回调函数
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检测到结果
                Toast.makeText(NowLocationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
                tvLocation.setText("");
            }

//            Toast.makeText(NowLocationActivity.this, "位置：" + result.getAddress(), Toast.LENGTH_LONG).show();
            tvLocation.setText(result.getAddress());
        }

        // 地理编码查询结果回调函数
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null
                    || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检测到结果
            }
        }
    };

    TimerTask task = new TimerTask(){
        public void run() {

//            Log.i("info==--===","wwwwwwww");
            handler.sendEmptyMessage(1);

        }
    };


    @Override
    public void onData(Object output, Object input) {

        if (output != null) {
            dismissLoadingDialog();
            if (output instanceof RespLocation) {
                RespLocation location = (RespLocation) output;

                if (location.getRes().equals("0")) {

//                    Log.i("info---====", location.getData().getLat()+","+location.getData().getLng());
//
//                    Log.i("info---====", BaiduapiOffline.transform(location.getData().getLat(),location.getData().getLng()).toString());
//
                    List<Map> baidu = BaiduapiOffline.transform(location.getData().getLat(), location.getData().getLng());

                    Map map = baidu.get(0);

                    Log.i("info---=====1", map.get("y") + "," + map.get("x"));


                    double[] db = BaiduLocation.wgs2bd(location.getData().getLat(), location.getData().getLng());

//                    Log.i("info---=====2", db[0] + "," + db[1]);

                    LatLng llA = new LatLng(db[0], db[1]);

//                    OverlayOptions ooA = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory
//                            .fromResource(R.mipmap.location_indicator)).zIndex(4).draggable(false);
//                    if (mBaiduMap != null) {
//                        mBaiduMap.addOverlay(ooA);
//                    }
                    if (mBaiduMap != null) {
                        // 构造定位数据
                        MyLocationData locData = new MyLocationData.Builder()
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(Float.parseFloat(location.getData().getDirection()))//Float.parseFloat(location.getData().getDirection())
                                .latitude(llA.latitude)
                                .longitude(llA.longitude).build();
                        // 设置定位数据
                        mBaiduMap.setMyLocationData(locData);
                        // 设置自定义图标
                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.location_indicator);
                        MyLocationConfiguration config = new MyLocationConfiguration(
                                MyLocationConfiguration.LocationMode.NORMAL , true, mCurrentMarker);
                        mBaiduMap.setMyLocationConfigeration(config);
                    }


                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(llA));

                    View view = LayoutInflater.from(NowLocationActivity.this).inflate(R.layout.map_window_layout, null);

                    TextView tvName = (TextView) view.findViewById(R.id.nameId);
                    TextView tvSpeed = (TextView) view.findViewById(R.id.speedId);
                    TextView tvTime = (TextView) view.findViewById(R.id.timeId);

                    ImageView img = (ImageView) view.findViewById(R.id.openId);

//                    img.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            dialog = new ExitDialog(NowLocationActivity.this);
//                            dialog.setCanceledOnTouchOutside(false);
//                            dialog.show();
//                        }
//                    });

                    tvName.setText(location.getData().getName());
                    tvSpeed.setText(location.getData().getSpeed());
                    tvTime.setText(location.getData().getDatetime());

                    currentInfoWindow = new InfoWindow(view, llA, DisplayUtil.dip2px(NowLocationActivity.this, -60.0f)); //第三个参数表示要显示的View和设置的坐标(position)之间的y轴偏移量
                    mBaiduMap.showInfoWindow(currentInfoWindow);

                    MapStatusUpdate u= MapStatusUpdateFactory.newLatLngZoom(llA, 18.0f);

                    mBaiduMap.animateMapStatus(u);


                } else {

                    EUtil.showToast(location.getErr());
                    finish();

                }

            }

            if (output instanceof Mqtt) {
                Mqtt mqtt = (Mqtt) output;
                if (mqtt.getRes().equals("0")) {
                    EUtil.showToast(mqtt.getErr());
                    dialog.dismiss();
                }
            }
        } else {
            dismissLoadingDialog();
            EUtil.showToast("网络错误");
        }
    }


    private void initMapView() {
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mMapView.removeViewAt(1); //隐藏百度logo
        mMapView.setLongClickable(true);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

//        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        timer.cancel();
    }

    @Override
    public void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        super.onPause();
    }


    // dialog
    class ExitDialog extends AlertDialog {

        public ExitDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);

            TextView textView = (TextView) findViewById(R.id.tvDialogActivity);

            textView.setText("恢复设备油电，请确认！");


            ViewGroup.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CarReqUtils.recoverlost(NowLocationActivity.this, NowLocationActivity.this, null, new Mqtt(), "r_close", true,
                            StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                    putValue("type", "2").createMap()));


                }
            });

            findViewById(R.id.setting_exit_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();

                }
            });
        }
    }

}
