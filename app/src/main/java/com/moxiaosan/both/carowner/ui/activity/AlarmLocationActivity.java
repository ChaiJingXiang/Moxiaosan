package com.moxiaosan.both.carowner.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
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

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.Mqtt;
import consumer.model.RespLocation;

/**
 * Created by chris on 16/3/4.
 */
public class AlarmLocationActivity extends BaseActivity {

    private MapView mMapView;
    private TextView tvLocation;
    private BaiduMap mBaiduMap;
    private InfoWindow currentInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_nowlocation_layout);

        showActionBar(true);

        setActionBarName("警情位置");

        Intent intent = getIntent();

        double lat =intent.getDoubleExtra("lat",0.0);
        double lng =intent.getDoubleExtra("lng",0.0);
        String name =intent.getStringExtra("name");
        String speed =intent.getStringExtra("speed");
        String time =intent.getStringExtra("time");
        String address =intent.getStringExtra("address");


        tvLocation = (TextView) findViewById(R.id.nowLocationId);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mapViewId);

        initMapView();

        LatLng llA =new LatLng(lat,lng);


        OverlayOptions ooA = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_indicator)).zIndex(4).draggable(false);
        if (mBaiduMap != null) {

            mBaiduMap.addOverlay(ooA);

        }

        View view = LayoutInflater.from(AlarmLocationActivity.this).inflate(R.layout.map_window_layout, null);

        TextView tvName = (TextView) view.findViewById(R.id.nameId);
        TextView tvSpeed = (TextView) view.findViewById(R.id.speedId);
        TextView tvTime = (TextView) view.findViewById(R.id.timeId);

//        ImageView img = (ImageView) view.findViewById(R.id.openId);

        tvName.setText(name);
        tvSpeed.setText(speed);
        tvTime.setText(time);
        tvLocation.setText(address);

        currentInfoWindow = new InfoWindow(view, llA, DisplayUtil.dip2px(AlarmLocationActivity.this, -60.0f)); //第三个参数表示要显示的View和设置的坐标(position)之间的y轴偏移量
        mBaiduMap.showInfoWindow(currentInfoWindow);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llA, 18.0f);
        mBaiduMap.animateMapStatus(u);

    }


    private void initMapView() {
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mMapView.removeViewAt(1); //隐藏百度logo
        mMapView.setLongClickable(true);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
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


}
