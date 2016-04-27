package com.moxiaosan.both.consumer.ui.activity;

import android.app.AlertDialog;
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
import consumer.api.CarReqUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Cancelexpress;
import consumer.model.Express;
import consumer.model.ExpressCost;
import consumer.model.mqttobj.MQArrivaltime;
import consumer.model.mqttobj.MQNotifyOwner;
import consumer.model.mqttobj.MQOrdernotify;

public class GateToGateActivity extends BaseActivity implements View.OnClickListener {
    public final static int GATE_TO_GATE_FROM_ADDRESS = 9;
    public final static int GATE_TO_GATE_TO_ADDRESS = 8;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient locClient;  // 定位相关
    private static BDLocation lastLocation = null;
    private boolean isFirstIn = true; //
    private BaiduSDKReceiver baiduReceiver;

    private TextView tvFromAddress, tvToAddress;
    private EditText etPeopleName, etPhoneNum, etGoodsName, etGoodsPrice, etGoodsLong, etGoodsWidth, etGoodsheight, etGoodsWeight, etGoodsReward;
    private FrameLayout orderLayout, fLayoutGuessFree, fLayoutAfterLayout, fLayoutEnsureLayout;
    private TextView tvGuessMoney, tvGuessEnsure, tvGuessCancel, tvAfterOrderEnsure, tvAfterOrderCancel, tvNotifyNums;
    private TextView tvEnsureCarNum, tvEnsureName, tvEnsurePhone, tvEnsurePut, tvEnsureCancel;
    private LinearLayout layoutPhone, layoutNotifyNums;


    private MyPoiInfo fromPoiInfo;  //出发地
    private MyPoiInfo toPoiInfo;  //到达地
    private String fromCity;  //  出发城市
    //    private String toCity;  //  到达城市
    private Marker markerFrom = null; //在地图上出发地标识
    private Marker markerTo = null;//在地图上目的地标识

    private ExpressCost expressCost;//预估费用

    public final static String NOTIFY_CAROWER_NUM = "notify_carower_num";
    public final static String ARRIVAL_TIME = "arrival_time";
    public final static String ORDER_NOTIFY = "order_notify";
    private String orderId; //订单编号
    private GateToGateBroadReceiver gateToGateBroadReceiver;

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
        setContentView(R.layout.activity_gate_to_gate);
        showActionBar(true);
        setActionBarName("门到门速递");
        initMapView();
        initView();
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        baiduReceiver = new BaiduSDKReceiver();
        registerReceiver(baiduReceiver, iFilter);

        IntentFilter iFilter2 = new IntentFilter();
        iFilter2.addAction(NOTIFY_CAROWER_NUM);
        iFilter2.addAction(ARRIVAL_TIME);
        iFilter2.addAction(ORDER_NOTIFY);
        gateToGateBroadReceiver = new GateToGateBroadReceiver();
        registerReceiver(gateToGateBroadReceiver, iFilter2);

        if (locClient != null) {
            locClient.start();  //开始定位自己
            showLoadingDialog();
        }
    }

    private void initView() {
        findViewById(R.id.gate_to_gate_weight_add).setOnClickListener(this);
        findViewById(R.id.gate_to_gate_weight_delete).setOnClickListener(this);
        findViewById(R.id.gate_to_gate_guess_free_txt_layout).setOnClickListener(this);
        etPeopleName = (EditText) findViewById(R.id.gate_to_gate_people_name);
        etPhoneNum = (EditText) findViewById(R.id.gate_to_gate_phone);
        etGoodsName = (EditText) findViewById(R.id.gate_to_gate_goods_name);
        etGoodsPrice = (EditText) findViewById(R.id.gate_to_gate_goods_price);
        etGoodsLong = (EditText) findViewById(R.id.gate_to_gate_goods_long);
        etGoodsWidth = (EditText) findViewById(R.id.gate_to_gate_goods_weight);
        etGoodsheight = (EditText) findViewById(R.id.gate_to_gate_goods_height);
        etGoodsWeight = (EditText) findViewById(R.id.gate_to_gate_goods_weight);
        etGoodsReward = (EditText) findViewById(R.id.gate_to_gate_reward);

        tvFromAddress = (TextView) findViewById(R.id.gate_to_gate_from_location);
        tvFromAddress.setOnClickListener(this);
        tvToAddress = (TextView) findViewById(R.id.gate_to_gate_to_location);
        tvToAddress.setOnClickListener(this);
        orderLayout = (FrameLayout) findViewById(R.id.gate_to_gate_order_layout);

        fLayoutGuessFree = (FrameLayout) findViewById(R.id.gate_to_gate_guess_free_layout);
        tvGuessMoney = (TextView) findViewById(R.id.gate_to_gate_guess_free_money);
        tvGuessEnsure = (TextView) findViewById(R.id.gate_to_gate_guess_free_ensure);
        tvGuessEnsure.setOnClickListener(this);
        tvGuessCancel = (TextView) findViewById(R.id.gate_to_gate_guess_free_cancel);
        tvGuessCancel.setOnClickListener(this);

        fLayoutAfterLayout = (FrameLayout) findViewById(R.id.gate_to_gate_after_order_layout);
        tvAfterOrderEnsure = (TextView) findViewById(R.id.gate_to_gate_after_order_put);
        tvAfterOrderEnsure.setOnClickListener(this);
        tvAfterOrderCancel = (TextView) findViewById(R.id.gate_to_gate_after_order_cancel);
        tvAfterOrderCancel.setOnClickListener(this);

        layoutNotifyNums = (LinearLayout) findViewById(R.id.gate_to_gate_notify_nums_layout);
        tvNotifyNums = (TextView) findViewById(R.id.gate_to_gate_notify_nums);

        fLayoutEnsureLayout = (FrameLayout) findViewById(R.id.gate_to_gate_ensure_order_layout);
        tvEnsureCarNum = (TextView) findViewById(R.id.gate_to_gate_ensure_order_car_num);
        tvEnsureName = (TextView) findViewById(R.id.gate_to_gate_ensure_order_car_name);
        tvEnsurePhone = (TextView) findViewById(R.id.gate_to_gate_ensure_order_phone_num);
        layoutPhone = (LinearLayout) findViewById(R.id.gate_to_gate_ensure_order_phone_layout);
        layoutPhone.setOnClickListener(this);
        tvEnsurePut = (TextView) findViewById(R.id.gate_to_gate_ensure_order_put);
        tvEnsurePut.setOnClickListener(this);
        tvEnsureCancel = (TextView) findViewById(R.id.gate_to_gate_ensure_order_cancel);
        tvEnsureCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gate_to_gate_weight_add:
                etGoodsWeight.setText(Double.valueOf(etGoodsWeight.getText().toString().trim()) + 1 + "");
                break;
            case R.id.gate_to_gate_weight_delete:
                if ((Double.valueOf(etGoodsWeight.getText().toString().trim()) - 1) >= 0) {
                    etGoodsWeight.setText(Double.valueOf(etGoodsWeight.getText().toString().trim()) - 1 + "");
                } else {
                    EUtil.showToast("不能再减了哦~");
                }
                break;
            case R.id.gate_to_gate_guess_free_txt_layout:
                if (!TextUtils.isEmpty(tvFromAddress.getText().toString())) {
                    if (!TextUtils.isEmpty(tvToAddress.getText().toString())) {
                        if (!TextUtils.isEmpty(etPeopleName.getText().toString())) {
                            if (!TextUtils.isEmpty(etPhoneNum.getText().toString()) && etPhoneNum.getText().toString().trim().length() == 11) {
                                if (!TextUtils.isEmpty(etGoodsName.getText().toString())) {
                                    if (!TextUtils.isEmpty(etGoodsPrice.getText().toString())) {
                                        if (!TextUtils.isEmpty(etGoodsLong.getText().toString())) {
                                            if (!TextUtils.isEmpty(etGoodsWidth.getText().toString())) {
                                                if (!TextUtils.isEmpty(etGoodsheight.getText().toString())) {
                                                    if (Double.valueOf(etGoodsWeight.getText().toString()) <= 0) {
                                                        EUtil.showToast("重量必须大于0");
                                                    } else {
                                                        //网络  预估费用
                                                        CarReqUtils.expressost(this, iApiCallback, null, new ExpressCost(), "GateToGateActivity", true,
                                                                StringUrlUtils.geturl(hashMapUtils.putValue("lat_o", fromPoiInfo.getLatitude()).putValue("lng_o", fromPoiInfo.getLongitude())
                                                                        .putValue("lat_d", toPoiInfo.getLatitude()).putValue("lng_d", toPoiInfo.getLongitude())
                                                                        .putValue("origin_region", fromCity).putValue("destination_region", toPoiInfo.getCity())
                                                                        .putValue("length", etGoodsLong.getText().toString()).putValue("width", etGoodsWidth.getText().toString())
                                                                        .putValue("height", etGoodsheight.getText().toString()).putValue("weight", etGoodsWeight.getText().toString())
                                                                        .createMap())
                                                        );
                                                        showLoadingDialog();
                                                    }
                                                } else {
                                                    EUtil.showToast("货物高不能为空");
                                                }
                                            } else {
                                                EUtil.showToast("货物宽不能为空");
                                            }
                                        } else {
                                            EUtil.showToast("货物长不能为空");
                                        }
                                    } else {
                                        EUtil.showToast("申报价值不能为空");
                                    }
                                } else {
                                    EUtil.showToast("货物名称不能为空");
                                }
                            } else {
                                EUtil.showToast("电话号码格式不正确");
                            }
                        } else {
                            EUtil.showToast("姓名不能为空");
                        }
                    } else {
                        EUtil.showToast("目的地不能为空");
                    }
                } else {
                    EUtil.showToast("出发地不能为空");
                }
                break;
            case R.id.gate_to_gate_from_location:
                Intent intent = new Intent(GateToGateActivity.this, SelectFromAddressActivity.class);
                startActivityForResult(intent, GATE_TO_GATE_FROM_ADDRESS);
                break;
            case R.id.gate_to_gate_to_location:
                Intent intent2 = new Intent(GateToGateActivity.this, SelectToAddessActivity.class);
                startActivityForResult(intent2, GATE_TO_GATE_TO_ADDRESS);
                break;
            case R.id.gate_to_gate_guess_free_ensure:
                if (!TextUtils.isEmpty(etGoodsReward.getText().toString())) {
                    ConsumerReqUtil.express(this, iApiCallback, null, new Express(), "GateToGateActivity", true,
                            StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("b_x", fromPoiInfo.getLongitude()).putValue("b_y", fromPoiInfo.getLatitude())
                                    .putValue("beginningplace", tvFromAddress.getText().toString()).putValue("destination", tvToAddress.getText().toString()).putValue("goodsname", etGoodsName.getText().toString()).putValue("weight", etGoodsWeight.getText().toString())
                                    .putValue("reward", etGoodsReward.getText().toString()).putValue("d_x", toPoiInfo.getLongitude()).putValue("d_y", toPoiInfo.getLatitude()).putValue("rec_tel", etPhoneNum.getText().toString())
                                    .putValue("declared", etGoodsPrice.getText().toString()).putValue("length", etGoodsLong.getText().toString()).putValue("width", etGoodsWidth.getText().toString())
                                    .putValue("height", etGoodsheight.getText().toString()).putValue("addressee", etPeopleName.getText().toString()).putValue("origin_region", fromCity).putValue("destination_region", toPoiInfo.getCity())
                                    .putValue("estcost", expressCost.getData().getCost()).putValue("reward", etGoodsReward.getText().toString()).createMap()));
                    showLoadingDialog();
                } else {
                    EUtil.showToast("打赏金额不能为空");
                }

                break;
            case R.id.gate_to_gate_guess_free_cancel:
                orderLayout.setVisibility(View.VISIBLE);
                fLayoutGuessFree.setVisibility(View.GONE);
                break;
            case R.id.gate_to_gate_after_order_put:  //下单后  放入订单
                orderLayout.setVisibility(View.VISIBLE);
                fLayoutAfterLayout.setVisibility(View.GONE);
                layoutNotifyNums.setVisibility(View.GONE);
                break;
            case R.id.gate_to_gate_after_order_cancel:  //下单后  取消订单
                CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(GateToGateActivity.this, orderId);
                cancelOrderDialog.show();
                break;
            case R.id.gate_to_gate_ensure_order_phone_layout:  //确认单后
                Intent consultPhoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvEnsurePhone.getText().toString()));
                startActivity(consultPhoneIntent);
                break;
            case R.id.gate_to_gate_ensure_order_put:  //确认单后  放入订单
                orderLayout.setVisibility(View.VISIBLE);
                layoutNotifyNums.setVisibility(View.GONE);
                fLayoutEnsureLayout.setVisibility(View.GONE);
                break;
            case R.id.gate_to_gate_ensure_order_cancel:  //确认单后   取消订单
                CancelOrderDialog cancelOrderDialog2 = new CancelOrderDialog(GateToGateActivity.this, orderId);
                cancelOrderDialog2.show();
                break;
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
            if (output instanceof Express) {
                Express express = (Express) output;
                EUtil.showToast(express.getErr());
                if ("0".equals(express.getRes())) {
                    fLayoutGuessFree.setVisibility(View.GONE);
                    fLayoutAfterLayout.setVisibility(View.VISIBLE);
                    layoutNotifyNums.setVisibility(View.VISIBLE);  //通知0位车主。。。
                    orderId = express.getData().getOrderid();
                }
            }
            if (output instanceof ExpressCost) {
                expressCost = (ExpressCost) output;
                if ("0".equals(expressCost.getRes())) {
                    orderLayout.setVisibility(View.GONE);
                    fLayoutGuessFree.setVisibility(View.VISIBLE);
                    tvGuessMoney.setText("¥" + expressCost.getData().getCost());
                }
            }
            if (output instanceof Cancelexpress) {
                Cancelexpress cancelexpress = (Cancelexpress) output;
                EUtil.showToast(cancelexpress.getErr());
                if (cancelexpress.getRes().equals("0")) {
                    finish();
                }
            }
        }
    };

    public class GateToGateBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NOTIFY_CAROWER_NUM)) {
                MQNotifyOwner notifyOwner = (MQNotifyOwner) intent.getSerializableExtra("mqNotifyOwner");
                tvNotifyNums.setText("已通知" + notifyOwner.getData() + "位车主");
            } else if (intent.getAction().equals(ARRIVAL_TIME)) {
                MQArrivaltime mqArrivaltime = (MQArrivaltime) intent.getSerializableExtra("mqArrivaltime");
                tvNotifyNums.setText("预计" + mqArrivaltime.getData() + "分钟到达");

            } else if (intent.getAction().equals(ORDER_NOTIFY)) {
                MQOrdernotify mqOrdernotify = (MQOrdernotify) intent.getSerializableExtra("mqOrdernotify");
                fLayoutAfterLayout.setVisibility(View.GONE);
                fLayoutEnsureLayout.setVisibility(View.VISIBLE);
                tvEnsureCarNum.setText(mqOrdernotify.getPlatenum());
                tvEnsureName.setText(mqOrdernotify.getSurname());
                tvEnsurePhone.setText(mqOrdernotify.getContact());
                orderId = mqOrdernotify.getOrderid();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GATE_TO_GATE_FROM_ADDRESS) {
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
            } else if (requestCode == GATE_TO_GATE_TO_ADDRESS) {
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

    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.gate_to_gate_mapview);
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

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void showMapWithLocationClient() {
        locClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("gcj02"); //设置坐标类型//bd09ll  //gcj02
        option.setScanSpan(30000);
        option.setIsNeedAddress(true);
//        option.setPriority(LocationClientOption.NetWorkFirst);
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
            if (!GateToGateActivity.this.isFinishing()) {  //当前Activity没有被销毁
                if (isLoadingDialogShowing()){
                    dismissLoadingDialog();
                }
                if (location == null) {
                    return;
                }
                if (fromPoiInfo == null) {
                    fromPoiInfo = new MyPoiInfo();
                }
                fromPoiInfo.setAddress(location.getAddress().city+location.getAddress().district+location.getAddress().street+location.getAddress().streetNumber+"号");
                fromPoiInfo.setLatitude(location.getLatitude());
                fromPoiInfo.setLongitude(location.getLongitude());
                fromCity = location.getCity();
                tvFromAddress.setText(location.getAddress().city+location.getAddress().district+location.getAddress().street+location.getAddress().streetNumber+"号");

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
        unregisterReceiver(gateToGateBroadReceiver);
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

        public CancelOrderDialog(Context context, String id) {
            super(context);
            this.orderId = id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);
            TextView tv = (TextView) findViewById(R.id.tvDialogActivity);
            tv.setText("确认取消该订单");
            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    orderLayout.setVisibility(View.VISIBLE);
                    fLayoutAfterLayout.setVisibility(View.GONE);
                    fLayoutEnsureLayout.setVisibility(View.GONE);
                    //网络
                    ConsumerReqUtil.cancelexpress(GateToGateActivity.this, iApiCallback, null, new Cancelexpress(), orderId, true,
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
