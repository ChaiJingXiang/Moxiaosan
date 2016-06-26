package com.moxiaosan.both.consumer.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.consumer.DrivingRouteOverlay;
import com.moxiaosan.both.common.ui.activity.ShareActivity;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.log.LLog;
import com.utils.ui.base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Cancelexpress;
import consumer.model.Payment;
import consumer.model.Userorderinfo;
import consumer.model.obj.RespUserOrder;
import consumer.model.obj.RespUserOrderInfo;

public class ShunFengOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private RespUserOrder respUserOrder;
    private TextView tvEnsure, tvCancle;
    private boolean isPayed = false;

    private TextView tvTime1, tvTime2, tvQuJian, tvQueRen;
    private TextView tvPrice, tvWard;
    private ImageView img1, img2, imgLine1;
    private TextView tvFromPlace, tvToPlace, tvJiedanRen, tvJieDanPhone;
    //    private ImageView imgMap;
    private LinearLayout layoutShare;

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    // 路线规划对象
    private RoutePlanSearch mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shun_feng_order_detail);
        showActionBar(true);
        respUserOrder = (RespUserOrder) getIntent().getSerializableExtra("respUserOrder");
        isPayed = getIntent().getBooleanExtra("isPayed", false);
        setActionBarName("订单编号：" + respUserOrder.getOrderid());
        initView();
        initMapView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ConsumerReqUtil.userorderinfo(this, iApiCallback, null, new Userorderinfo(), respUserOrder.getOrderid(), true, "orderid=" + respUserOrder.getOrderid());
//        ConsumerReqUtil.userorderinfo(this, iApiCallback, null, new Userorderinfo(), respUserOrder.getOrderid(), true, "orderid=2016052154495048");
        showLoadingDialog();
    }

    private void initView() {
        tvTime1 = (TextView) findViewById(R.id.shun_feng_order_detail_time1);
        tvTime2 = (TextView) findViewById(R.id.shun_feng_order_detail_time2);
        tvQuJian = (TextView) findViewById(R.id.shun_feng_order_detail_qujian);
        tvQueRen = (TextView) findViewById(R.id.shun_feng_order_detail_queren);

        img1 = (ImageView) findViewById(R.id.shun_feng_order_detail_img1);
        img2 = (ImageView) findViewById(R.id.shun_feng_order_detail_img2);
        imgLine1 = (ImageView) findViewById(R.id.shun_feng_order_detail_line1);

        tvFromPlace = (TextView) findViewById(R.id.shun_feng_order_detail_from_place);
        tvToPlace = (TextView) findViewById(R.id.shun_feng_order_detail_to_place);
        tvJiedanRen = (TextView) findViewById(R.id.shun_feng_order_detail_jiedan_name);
        tvJieDanPhone = (TextView) findViewById(R.id.shun_feng_order_detail_jiedan_phone);
        tvPrice = (TextView) findViewById(R.id.shun_feng_order_detail_price);
        tvWard = (TextView) findViewById(R.id.shun_feng_order_detail_ward_moeny);

//        imgMap = (ImageView) findViewById(R.id.shun_feng_order_detail_map_img);
        layoutShare = (LinearLayout) findViewById(R.id.shun_feng_order_detail_share_layout);
        layoutShare.setOnClickListener(this);

        tvEnsure = (TextView) findViewById(R.id.shun_feng_order_detail_ensure);
        tvEnsure.setOnClickListener(this);
        tvCancle = (TextView) findViewById(R.id.shun_feng_order_detail_cancel);
        tvCancle.setOnClickListener(this);
        if (isPayed) {
            tvEnsure.setVisibility(View.GONE);
            tvCancle.setText("评价");
        }
    }

    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.shun_feng_order_detail_map_img);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mMapView.removeViewAt(1); //隐藏百度logo
        mMapView.setLongClickable(true);
        mBaiduMap = mMapView.getMap();
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(13.0f);
//        mBaiduMap.setMapStatus(msu);

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
    }

    private void setData(RespUserOrderInfo respUserOrderInfo) {
//        ImageLoader.getInstance().displayImage("https://www.baidu.com/img/bd_logo1.png", imgMap);
        tvFromPlace.setText(respUserOrderInfo.getBeginningplace());
        tvToPlace.setText(respUserOrderInfo.getDestination());
        tvJiedanRen.setText(respUserOrderInfo.getCommentsid());
        tvJieDanPhone.setText(respUserOrderInfo.getCom_tel());
        if (!TextUtils.isEmpty(tvJieDanPhone.getText().toString())) {
            tvJieDanPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //用intent启动拨打电话
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvJieDanPhone.getText().toString()));
                    startActivity(intent);
                }
            });
        }
        tvPrice.setText(respUserOrderInfo.getEstcost());
        tvWard.setText(respUserOrderInfo.getDashang());
        if (!TextUtils.isEmpty(respUserOrderInfo.getServicestatus())) {
            tvCancle.setVisibility(View.GONE);
            int status = Integer.valueOf(respUserOrderInfo.getServicestatus());
            if (status > 0) { //1
                tvTime1.setText(respUserOrderInfo.getPickuptime());
                img1.setImageResource(R.mipmap.order_state_light);
                tvQuJian.setTextColor(getResources().getColor(R.color.main_color));
            }
            if (status > 1) {  //2
                tvTime2.setText(respUserOrderInfo.getDeliverytime());
                imgLine1.setBackgroundColor(getResources().getColor(R.color.main_color));
                img2.setImageResource(R.mipmap.order_state_light);
                tvQueRen.setTextColor(getResources().getColor(R.color.main_color));
            }
        }

        if (!TextUtils.isEmpty(respUserOrderInfo.getD_lat()) && !TextUtils.isEmpty(respUserOrderInfo.getB_lat())
                && !TextUtils.isEmpty(respUserOrderInfo.getD_lng()) && !TextUtils.isEmpty(respUserOrderInfo.getB_lng())) {
            //计算中间值
            double mLat = (Double.valueOf(respUserOrderInfo.getD_lat()) + Double.valueOf(respUserOrderInfo.getB_lat())) / 2;
            double mLng = (Double.valueOf(respUserOrderInfo.getD_lng()) + Double.valueOf(respUserOrderInfo.getB_lng())) / 2;
            LatLng middleLatLng = new LatLng(mLat, mLng);

            LatLng beginLatLng = new LatLng(Double.valueOf(respUserOrderInfo.getB_lat()), Double.valueOf(respUserOrderInfo.getB_lng()));

            LatLng dLatLng = new LatLng(Double.valueOf(respUserOrderInfo.getD_lat()), Double.valueOf(respUserOrderInfo.getD_lng()));

            if (!TextUtils.isEmpty(respUserOrderInfo.getCar_lat()) && !TextUtils.isEmpty(respUserOrderInfo.getCar_lng())) {
                LatLng carLatLng = new LatLng(Double.valueOf(respUserOrderInfo.getCar_lat()), Double.valueOf(respUserOrderInfo.getCar_lng()));
                OverlayOptions ooCar = new MarkerOptions().position(carLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_indicator)).zIndex(4).draggable(false);
                mBaiduMap.addOverlay(ooCar);
            }

            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(middleLatLng, 18.0f);
            mBaiduMap.animateMapStatus(u);
            routePlan(beginLatLng, dLatLng);
        } else {
            EUtil.showToast("坐标参数有误");
        }

    }

    /**
     * 发起路线规划
     */
    public void routePlan(LatLng bLatLng, LatLng dLatLng) {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
        // 起点与终点
        PlanNode stNode = PlanNode.withLocation(bLatLng);
        PlanNode enNode = PlanNode.withLocation(dLatLng);
//        PlanNode stNode = PlanNode.withLocation(new LatLng(39.909843, 116.434452));
//        PlanNode enNode = PlanNode.withLocation(new LatLng(39.915599, 116.402831));
        // 驾车路线规划
        mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
    }

    /**
     * 路线规划结果监听
     */
    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            // 获取步行线路规划结果
        }

        public void onGetTransitRouteResult(TransitRouteResult result) {
            // 获取公交换乘路径规划结果
        }

        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            // 获取驾车线路规划结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                EUtil.showToast("抱歉，未找到结果");
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            // 获取骑行路径规划结果
        }

    };

    // 驾车路线 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
//            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            }
            return null;
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shun_feng_order_detail_ensure:
                startActivity(new Intent(ShunFengOrderDetailActivity.this, SelectPayMethodActivity.class).putExtra("respUserOrder", respUserOrder));
                finish();
                break;
            case R.id.shun_feng_order_detail_cancel:
                if (isPayed) { //评价
                    Intent intent = new Intent(ShunFengOrderDetailActivity.this, OrderCommentActivity.class);
                    intent.putExtra("respUserOrder", respUserOrder);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                } else {  //取消订单
                    CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(ShunFengOrderDetailActivity.this, respUserOrder.getOrderid());
                    cancelOrderDialog.show();
                }
                break;

            case R.id.shun_feng_order_detail_share_layout:
                //设置截屏监听
                final File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".png");
                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                            EUtil.showLongToast("请插入SD卡");
                            return;
                        }
                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(file);
                            if (bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)) {
                                out.flush();
                                out.close();
                            }
                            LLog.i("屏幕截图成功，图片存在: " + file.toString());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Intent shareIntent = new Intent(ShunFengOrderDetailActivity.this, ShareActivity.class);
                shareIntent.putExtra("title", "推荐应用摩小三给你");
                shareIntent.putExtra("content", "分享摩小三截图给你");
                shareIntent.putExtra("imgPath", file.toString());
                shareIntent.putExtra("targetUrl", "http://www.moxiaosan.com");
                this.startActivity(shareIntent);
                overridePendingTransition(R.anim.share_pop_in, 0);
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
            if (output instanceof Cancelexpress) {
                Cancelexpress cancelexpress = (Cancelexpress) output;
                EUtil.showToast(cancelexpress.getErr());
                if (cancelexpress.getRes().equals("0")) {
                    finish();
                }
            }
            if (output instanceof Userorderinfo) {
                Userorderinfo userorderinfo = (Userorderinfo) output;
                setData(userorderinfo.getData());
            }
            if (output instanceof Payment) {
                Payment payment = (Payment) output;
                EUtil.showToast(payment.getErr());
                if (payment.getRes().equals("0")) {
                    isPayed = true;
                    if (isPayed) {
                        tvEnsure.setVisibility(View.GONE);
                        tvCancle.setText("评价");
                    }
                }
            }
        }
    };

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
                    //网络
                    ConsumerReqUtil.cancelexpress(ShunFengOrderDetailActivity.this, iApiCallback, null, new Cancelexpress(), orderId, true,
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
