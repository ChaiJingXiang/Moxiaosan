package com.moxiaosan.both.consumer.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.moxiaosan.both.R;
import com.moxiaosan.both.common.model.MyPoiInfo;
import com.moxiaosan.both.common.ui.activity.SelectFromAddressActivity;
import com.moxiaosan.both.common.ui.activity.SelectToAddessActivity;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Cancelexpress;
import consumer.model.Hitchhiking;
import consumer.model.Ranging;
import consumer.model.Resend;
import consumer.model.mqttobj.MQArrivaltime;
import consumer.model.mqttobj.MQNotifyOwner;
import consumer.model.mqttobj.MQOrdernotify;

public class ShunFengCheActivity extends BaseActivity implements View.OnClickListener {
    public final static int SHUN_FENG_CHE_FROM_ADDRESS = 9999;
    public final static int SHUN_FENG_CHE_TO_ADDRESS = 8888;


    public final static String NOTIFY_CAROWER_NUM = "notify_carower_num";
    public final static String ARRIVAL_TIME = "arrival_time";
    public final static String ORDER_NOTIFY = "order_notify";
    public final static String NO_ORDERED = "no_ordered";

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private ProgressDialog progressDialog;
    private LocationClient locClient;  // 定位相关
    private static BDLocation lastLocation = null;
    private boolean isFirstIn = true; //
    private BaiduSDKReceiver baiduReceiver;

    private EditText etReward;
    private TextView tvFromAddress, tvToAddress;
    private EditText etFromAddressDetail, etToAddressDetail;//详细信息
    private MyPoiInfo fromPoiInfo;  //出发地
    private MyPoiInfo toPoiInfo;  //到达地
    private String fromCity;  //  出发城市

    private Marker markerFrom = null; //在地图上出发地标识
    private Marker markerTo = null;//在地图上目的地标识

    private FrameLayout fLayoutMain, fLayoutGuessFree, fLayoutEnsureOrder;
    private LinearLayout layoutLeaveTime, layoutPhone;
    private TextView tvGuessEnsure, tvGuessCancle, tvEnsurePut, tvEnsureCancel, tvPhone, tvMoney, tvLeaveTime, tvEnsureCarNum, tvEnsureName;

    private Ranging ranging;

    private ShunFengBroadReceiver shunFengBroadReceiver;
    private String orderId; //订单编号
    private CancelOrderDialog againDialog;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shun_feng_che);
        showActionBar(true);
        setActionBarName(getString(R.string.shun_feng_che));
        initView();
        initMapView();
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        baiduReceiver = new BaiduSDKReceiver();
        registerReceiver(baiduReceiver, iFilter);

        IntentFilter iFilter2 = new IntentFilter();  //mqtt接受的广播
        iFilter2.addAction(NOTIFY_CAROWER_NUM);
        iFilter2.addAction(ARRIVAL_TIME);
        iFilter2.addAction(ORDER_NOTIFY);
        iFilter2.addAction(NO_ORDERED);
        shunFengBroadReceiver = new ShunFengBroadReceiver();
        registerReceiver(shunFengBroadReceiver, iFilter2);

        if (locClient != null) {
            locClient.start();  //开始定位自己
            showLoadingDialog();
        }
    }

    private void initView() {
        findViewById(R.id.shun_feng_che_guess_free_txt_layout).setOnClickListener(this);
        tvFromAddress = (TextView) findViewById(R.id.shun_feng_che_from_location);
        etFromAddressDetail = (EditText) findViewById(R.id.shun_feng_che_from_location_detail);//详细信息
        tvFromAddress.setOnClickListener(this);
        tvToAddress = (TextView) findViewById(R.id.shun_feng_che_to_location);
        etToAddressDetail = (EditText) findViewById(R.id.shun_feng_che_to_location_detail);//详细信息
        tvToAddress.setOnClickListener(this);
        etReward = (EditText) findViewById(R.id.shun_feng_che_reward);
        tvMoney = (TextView) findViewById(R.id.shun_feng_che_guess_free_money);

        fLayoutMain = (FrameLayout) findViewById(R.id.shun_feng_che_main_framelayout);
        fLayoutGuessFree = (FrameLayout) findViewById(R.id.shun_feng_che_guess_free);
        fLayoutEnsureOrder = (FrameLayout) findViewById(R.id.shun_feng_che_ensure_order_layout);
        layoutLeaveTime = (LinearLayout) findViewById(R.id.shun_feng_che_leave_time_layout);
        tvLeaveTime = (TextView) findViewById(R.id.shun_feng_che_leave_time);

        tvGuessEnsure = (TextView) findViewById(R.id.shun_feng_che_guess_free_ensure);
        tvGuessEnsure.setOnClickListener(this);
        tvGuessCancle = (TextView) findViewById(R.id.shun_feng_che_guess_free_cancel);
        tvGuessCancle.setOnClickListener(this);

        layoutPhone = (LinearLayout) findViewById(R.id.shun_feng_che_ensure_order_phone_layout);
        layoutPhone.setOnClickListener(this);
        tvEnsurePut = (TextView) findViewById(R.id.shun_feng_che_order_put);
        tvEnsurePut.setOnClickListener(this);
        tvEnsureCancel = (TextView) findViewById(R.id.shun_feng_che_order_cancle);
        tvEnsureCancel.setOnClickListener(this);
        tvPhone = (TextView) findViewById(R.id.shun_feng_che_ensure_order_phone_num);
        tvEnsureCarNum = (TextView) findViewById(R.id.shun_feng_che_ensure_order_car_num);
        tvEnsureName = (TextView) findViewById(R.id.shun_feng_che_ensure_order_car_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shun_feng_che_guess_free_txt_layout:
                if (!TextUtils.isEmpty(tvFromAddress.getText().toString())) {
                    if (!TextUtils.isEmpty(tvToAddress.getText().toString())) {
                        ConsumerReqUtil.ranging(ShunFengCheActivity.this, iApiCallback, null, new Ranging(), "ShunFengCheActivity", true,
                                StringUrlUtils.geturl(hashMapUtils.putValue("origin_region", fromCity).putValue("destination_region", toPoiInfo.getCity())
                                        .putValue("lat_o", fromPoiInfo.getLatitude()).putValue("lng_o", fromPoiInfo.getLongitude()).
                                                putValue("lat_d", toPoiInfo.getLatitude()).putValue("lng_d", toPoiInfo.getLongitude()).createMap())
                        );
                        showLoadingDialog();
                    } else {
                        EUtil.showToast("目的地不能为空");
                    }
                } else {
                    EUtil.showToast("出发地不能为空");
                }
                break;
            case R.id.shun_feng_che_from_location:
                Intent intent = new Intent(ShunFengCheActivity.this, SelectFromAddressActivity.class);
                startActivityForResult(intent, SHUN_FENG_CHE_FROM_ADDRESS);
                break;
            case R.id.shun_feng_che_to_location:
                Intent intent2 = new Intent(ShunFengCheActivity.this, SelectToAddessActivity.class);
                startActivityForResult(intent2, SHUN_FENG_CHE_TO_ADDRESS);
                break;
            case R.id.shun_feng_che_guess_free_ensure:
//                if (!TextUtils.isEmpty(etReward.getText().toString())) {
                ConsumerReqUtil.hitchhiking(this, iApiCallback, null, new Hitchhiking(), "ShunFengCheActivity", true,
                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("b_x", fromPoiInfo.getLongitude()).putValue("b_y", fromPoiInfo.getLatitude())
                                .putValue("beginningplace", tvFromAddress.getText().toString()).putValue("begin_specific", etFromAddressDetail.getText().toString())
                                .putValue("destination", tvToAddress.getText().toString()).putValue("dest_specific", etToAddressDetail.getText().toString()).putValue("reward", etReward.getText().toString())
                                .putValue("d_x", toPoiInfo.getLongitude()).putValue("d_y", toPoiInfo.getLatitude()).putValue("origin_region", fromCity).putValue("destination_region", toPoiInfo.getCity())
                                .putValue("estcost", ranging.getData().getCost()).putValue("reward", etReward.getText().toString()).createMap()));
                showLoadingDialog();
//                } else {
//                    EUtil.showToast("打赏金额不能为空");
//                }
                break;
            case R.id.shun_feng_che_guess_free_cancel:
                fLayoutMain.setVisibility(View.VISIBLE);
                fLayoutGuessFree.setVisibility(View.GONE);
                break;
            case R.id.shun_feng_che_ensure_order_phone_layout:
                Intent consultPhoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhone.getText().toString()));
                startActivity(consultPhoneIntent);
                break;
            case R.id.shun_feng_che_order_cancle:
                CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(ShunFengCheActivity.this, orderId, 2);
                cancelOrderDialog.show();
                break;
            case R.id.shun_feng_che_order_put:
                layoutLeaveTime.setVisibility(View.GONE);
                fLayoutEnsureOrder.setVisibility(View.GONE);
                fLayoutMain.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SHUN_FENG_CHE_FROM_ADDRESS) {
                fromPoiInfo = (MyPoiInfo) data.getSerializableExtra("fromPosition");
                fromCity = data.getStringExtra("fromCity");
                tvFromAddress.setText(fromPoiInfo.getAddress());
                LatLng fromLaylng = new LatLng(fromPoiInfo.getLatitude(), fromPoiInfo.getLongitude());
                OverlayOptions ooFrom = new MarkerOptions().position(fromLaylng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.chufa_small)).zIndex(4).draggable(false);
                if (markerFrom != null) {
                    markerFrom.remove();
                    markerFrom = null;
                }
                if (mBaiduMap != null) {
                    markerFrom = (Marker) mBaiduMap.addOverlay(ooFrom);
                }
//                Log.i("===fromCity", fromCity);
            } else if (requestCode == SHUN_FENG_CHE_TO_ADDRESS) {
                toPoiInfo = (MyPoiInfo) data.getSerializableExtra("toPosition");
                tvToAddress.setText(toPoiInfo.getAddress());
                LatLng toLaylng = new LatLng(toPoiInfo.getLatitude(), toPoiInfo.getLongitude());
                OverlayOptions ooTo = new MarkerOptions().position(toLaylng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.mudi)).zIndex(4).draggable(false);
                if (markerTo != null) {
                    markerTo.remove();
                    markerTo = null;
                }
                if (mBaiduMap != null) {
                    markerTo = (Marker) mBaiduMap.addOverlay(ooTo);
                }
//                Log.i("===toCity", locationBean.getCity());
            }
        }
    }

    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (isLoadingDialogShowing()) {
                dismissLoadingDialog();
            }
            if (output == null) {
                return;
            }
            if (output instanceof Hitchhiking) { //下单
                Hitchhiking hitchhiking = (Hitchhiking) output;
                EUtil.showToast(hitchhiking.getErr());
                if ("0".equals(hitchhiking.getRes())) {
                    fLayoutGuessFree.setVisibility(View.GONE);
                    layoutLeaveTime.setVisibility(View.VISIBLE);
                    tvLeaveTime.setText("已通知0位车主");
                }
            }
            if (output instanceof Ranging) {  //预估费用
                ranging = (Ranging) output;
                //EUtil.showToast(ranging.getErr());
                if (ranging.getRes().equals("0")) {
                    fLayoutMain.setVisibility(View.GONE);
                    fLayoutGuessFree.setVisibility(View.VISIBLE);
                    tvMoney.setText("¥" + ranging.getData().getCost());
                }
            }
            if (output instanceof Cancelexpress) {   //取消订单
                Cancelexpress cancelexpress = (Cancelexpress) output;
                EUtil.showToast(cancelexpress.getErr());
                if (cancelexpress.getRes().equals("0")) {
                    finish();
                }
            }
            if (output instanceof Resend) { //重新发送订单
                Resend resend = (Resend) output;
                EUtil.showToast(resend.getErr());
                if (resend.getRes().equals("0")) {
                    againDialog.dismiss();
                }
            }
        }
    };

    public class ShunFengBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NOTIFY_CAROWER_NUM)) {
                MQNotifyOwner notifyOwner = (MQNotifyOwner) intent.getSerializableExtra("mqNotifyOwner");
                tvLeaveTime.setText("已通知" + notifyOwner.getData() + "位车主");
            } else if (intent.getAction().equals(ARRIVAL_TIME)) {
                MQArrivaltime mqArrivaltime = (MQArrivaltime) intent.getSerializableExtra("mqArrivaltime");
                tvLeaveTime.setText("预计" + mqArrivaltime.getData() + "分钟到达");

            } else if (intent.getAction().equals(ORDER_NOTIFY)) {
                MQOrdernotify mqOrdernotify = (MQOrdernotify) intent.getSerializableExtra("mqOrdernotify");
                fLayoutEnsureOrder.setVisibility(View.VISIBLE);
                tvEnsureCarNum.setText(mqOrdernotify.getPlatenum());
                tvEnsureName.setText(mqOrdernotify.getSurname());
                tvPhone.setText(mqOrdernotify.getContact());
                orderId = mqOrdernotify.getOrderid();
            } else if (intent.getAction().equals(NO_ORDERED)) {
                againDialog = new CancelOrderDialog(ShunFengCheActivity.this, orderId, 1);
                againDialog.show();
            }
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.shun_feng_che_mapview);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mMapView.removeViewAt(1); //隐藏百度logo
        mMapView.setLongClickable(true);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(13.0f);
        mBaiduMap.setMapStatus(msu);

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                LatLng latlng = mBaiduMap.getMapStatus().target;
//                LLog.i("*****************lat = " + latlng.latitude);
//                LLog.i("*****************lng = " + latlng.longitude);

            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                // TODO Auto-generated method stub

            }
        });
        showMapWithLocationClient();
    }

    private void showMapWithLocationClient() {
        locClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("gcj02"); //设置坐标类型//bd09ll  //gcj02
        option.setScanSpan(30000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setNeedDeviceDirect(true);
        option.setIsNeedAltitude(true);
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
            if (!ShunFengCheActivity.this.isFinishing()) {  //当前Activity没有被销毁
                if (isLoadingDialogShowing()) {
                    dismissLoadingDialog();
                }
                if (location == null) {
                    return;
                }
                if (fromPoiInfo == null) {
                    fromPoiInfo = new MyPoiInfo();
                }
                String address = location.getAddress().city + location.getAddress().district + location.getAddress().street;
                if (!TextUtils.isEmpty(location.getAddress().streetNumber)) {
                    address = address + location.getAddress().streetNumber + "号";
                }
                if (!TextUtils.isEmpty(location.getSemaAptag())) {
                    address = address + "(" + location.getSemaAptag() + ")";
                }
                fromPoiInfo.setAddress(address);
                fromPoiInfo.setLatitude(location.getLatitude());
                fromPoiInfo.setLongitude(location.getLongitude());
                fromCity = location.getCity();
                tvFromAddress.setText(address);

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
                        .fromResource(R.mipmap.location_indicator)).zIndex(4).draggable(false);
                if (mBaiduMap != null) {
                    mBaiduMap.addOverlay(ooA);
                }
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 13.0f);
                mBaiduMap.animateMapStatus(u);
            }
        }
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
        unregisterReceiver(shunFengBroadReceiver);
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
        if (locClient != null) {
            locClient.stop();
        }
        super.onPause();
        lastLocation = null;
    }

    //取消订单
    class CancelOrderDialog extends AlertDialog {

        String orderId;
        int index;

        public CancelOrderDialog(Context context, String id, int indexId) {
            super(context);
            this.orderId = id;
            this.index = indexId;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);
            TextView tv = (TextView) findViewById(R.id.tvDialogActivity);
            if (index == 1) {
                tv.setText("暂无车主接单，是否重新发送该订单");
                findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConsumerReqUtil.resend(ShunFengCheActivity.this, iApiCallback, null, new Resend(), orderId, true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("orderid", orderId).createMap()));
                    }
                });
                findViewById(R.id.setting_exit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConsumerReqUtil.cancelexpress(ShunFengCheActivity.this, iApiCallback, null, new Cancelexpress(), orderId, true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("expid", orderId).createMap())
                        );
                        dismiss();
                    }
                });
            } else { // =2
                tv.setText("确认取消该订单");
                findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        fLayoutMain.setVisibility(View.VISIBLE);
                        fLayoutEnsureOrder.setVisibility(View.GONE);
                        layoutLeaveTime.setVisibility(View.GONE);
                        //网络
                        ConsumerReqUtil.cancelexpress(ShunFengCheActivity.this, iApiCallback, null, new Cancelexpress(), orderId, true,
                                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("expid", orderId).createMap())
                        );

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
}
