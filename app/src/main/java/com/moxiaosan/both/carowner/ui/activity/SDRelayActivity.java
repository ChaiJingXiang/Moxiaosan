package com.moxiaosan.both.carowner.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.adapter.SDListViewAdapter;
import com.moxiaosan.both.carowner.ui.consumer.MyListView;
import com.moxiaosan.both.common.model.LocationBean;
import com.moxiaosan.both.common.model.MyPoiInfo;
import com.moxiaosan.both.common.ui.activity.SelectToAddessActivity;
import com.moxiaosan.both.common.utils.DateTimePickDialogUtil;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.ExpressCost;
import consumer.model.RespOrderDetail;
import consumer.model.RespSDOrderDetail;
import consumer.model.TakeOrder;
import consumer.model.obj.RelayInfo;
import consumer.model.obj.RespUserInfo;
import lecho.lib.hellocharts.model.Line;

/**
 * Created by chris on 16/3/1.
 */
public class SDRelayActivity extends BaseActivity implements IApiCallback, OnClickListener {

    private TextView tvDistance, tvStart, tvEnd, tvTakeOrderPerson, tvTakeOrderPhone, tvGoodsName, tvGoodsPrice, tvGoodsSize, tvGoodsWeight,
            tvReceiveName, tvReceivePhone;

    private TextView tvTypeName;

    private TextView tvAddress;
    private String tvAllMoney;
    private EditText etTime;
    private MyPoiInfo locationBean;  //到达地
    private String lat_o, lng_o, origin_region;
    private RespOrderDetail respOrderDetail;
    private RespSDOrderDetail respSDOrderDetail;

    private LinearLayout linearLayout;
    private MyListView listView;
    private SDListViewAdapter adapter;

    private String lat, lng;
    private String city;
    private String orderId;
    private String name;
    private int type;
    private TextView tvSure;
    private String lat_d;
    private String lng_d;
    private String destination_region;
    private LinearLayout addressLinear;
    private List<RelayInfo> lists =new ArrayList<>();
    //    private LinearLayout bottomLinear;
//    private TextView tvQuHuo,tvSongDa;
    private ExitDialog dialog;
    private TextView tvMudidi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_sdieli_layout);

        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);

        city = sp.getString("city", "");
        lng = sp.getString("longitude", "");
        lat = sp.getString("latitude", "");
        Log.i("info", city + "," + lat + "," + lng);

        Intent intent = getIntent();

        type = intent.getIntExtra("type", 1);
        Log.i("info===---===", type + "");
        name = intent.getStringExtra("name");
        orderId = intent.getStringExtra("orderId");

        showActionBar(true);
        setActionBarName("订单号：" + orderId);

        reqData();

    }


    private void initView() {

        tvTypeName = (TextView) findViewById(R.id.typeNameId);
        tvDistance = (TextView) findViewById(R.id.distance);
        tvStart = (TextView) findViewById(R.id.shifadiId);
        tvEnd = (TextView) findViewById(R.id.mudidiId);
        tvTakeOrderPerson = (TextView) findViewById(R.id.xiadanrenId);
        tvTakeOrderPhone = (TextView) findViewById(R.id.xiadanPhoneId);
        tvGoodsName = (TextView) findViewById(R.id.huowuNameId);
        tvGoodsPrice = (TextView) findViewById(R.id.shenbaoPriceId);
        tvGoodsSize = (TextView) findViewById(R.id.daxiaoId);
        tvGoodsWeight = (TextView) findViewById(R.id.weightId);
        tvReceiveName = (TextView) findViewById(R.id.shoujianrenId);
        tvReceivePhone = (TextView) findViewById(R.id.shoujianPhoneId);
        linearLayout = (LinearLayout) findViewById(R.id.linearId);
        addressLinear = (LinearLayout) findViewById(R.id.addressLinearId);

        tvMudidi =(TextView)findViewById(R.id.songdaMuDiDiId);

//
//        bottomLinear =(LinearLayout)findViewById(R.id.bottomLinearId);
//        tvQuHuo =(TextView)findViewById(R.id.yisonghuoId);
//        tvSongDa =(TextView)findViewById(R.id.yisongdaId);

        tvAddress = (TextView) findViewById(R.id.sd_jiaojiedidiId);
        etTime = (EditText) findViewById(R.id.sd_shijianId);

        tvAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SDRelayActivity.this, SelectToAddessActivity.class);
                startActivityForResult(intent, 8);
            }
        });

        etTime.requestFocus();
        etTime.setInputType(InputType.TYPE_NULL);

        etTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                String time = format.format(date);

                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        SDRelayActivity.this, time);
                dateTimePicKDialog.dateTimePicKDialog(etTime);
            }
        });

        tvSure = (TextView) findViewById(R.id.sureId);
        tvSure.setOnClickListener(this);

        if(type ==2){
            tvMudidi.setVisibility(View.VISIBLE);
            tvMudidi.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    tvAddress.setText(respSDOrderDetail.getData().getDestination());
                    lat_d =respSDOrderDetail.getData().getLat_d();
                    lng_d =respSDOrderDetail.getData().getLng_d();
                    destination_region =respSDOrderDetail.getData().getDestination_region();

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 8) {
                locationBean = (MyPoiInfo) data.getSerializableExtra("toPosition");
                tvAddress.setText(locationBean.getAddress());
                Log.i("===toCity", locationBean.getCity());

                lat_d =locationBean.getLatitude()+"";
                lng_d =locationBean.getLongitude()+"";
                destination_region =locationBean.getCity();

            }
        }
    }

    //请求详情信息
    private void reqData() {

        showLoadingDialog();

        if (type == 1) {
            CarReqUtils.orderInfo(this, this, null, new RespOrderDetail(), "SDOrderDetail", true,
                    StringUrlUtils.geturl(hashMapUtils.putValue("orderid", orderId).putValue("lat", lat).putValue("lng", lng).
                            putValue("type", "1").putValue("origin_region", city).createMap()));
        } else {

            CarReqUtils.orderInfo(this, this, null, new RespSDOrderDetail(), "SDOrderDetail", true,
                    StringUrlUtils.geturl(hashMapUtils.putValue("orderid", orderId).putValue("lat", lat).putValue("lng", lng).
                            putValue("type", "2").putValue("origin_region", city).createMap()));
        }
    }

    //首棒
    private void reqFirstBatter() {
        String time = etTime.getText().toString();
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Log.i("info--==--", s.format(date));

        showLoadingDialog();
        CarReqUtils.relOrder(this, this, null, new TakeOrder(), "relOrder", true,
                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                        putValue("orderid", orderId).putValue("handoveraddress", tvAddress.getText().toString().toString()).
                        putValue("handover", s.format(date)).
                        putValue("lat", lat_d).putValue("lng", lng_d).
                        putValue("destination_region", destination_region).putValue("cost", tvAllMoney).createMap()));

    }

    //非首棒
    private void reqNoFirstBatter() {

        String time = etTime.getText().toString();
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Log.i("info--==--", s.format(date));
        showLoadingDialog();
        if(lists.size()==3){
            lat_d =respSDOrderDetail.getData().getLat_d();
            lng_d =respSDOrderDetail.getData().getLng_d();
            destination_region =respSDOrderDetail.getData().getDestination_region();

            CarReqUtils.relayOrder(this, this, null, new TakeOrder(), "relayOrder", true,
                    StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                            putValue("orderid", orderId).putValue("handoveraddress",destination_region).
                            putValue("handover", s.format(date)).
                            putValue("lat", lat_d).putValue("lng", lng_d).
                            putValue("destination_region", destination_region).putValue("cost", tvAllMoney).
                            putValue("beginningplace", tvStart.getText().toString().trim()).createMap()));


        }else{
            CarReqUtils.relayOrder(this, this, null, new TakeOrder(), "relayOrder", true,
                    StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                            putValue("orderid", orderId).putValue("handoveraddress", tvAddress.getText().toString().toString()).
                            putValue("handover", s.format(date)).
                            putValue("lat", lat_d).putValue("lng", lng_d).
                            putValue("destination_region", destination_region).putValue("cost", tvAllMoney).
                            putValue("beginningplace", tvStart.getText().toString().trim()).createMap()));


        }

    }

    //请求接力预估费用
    private void expressCost() {

        showLoadingDialog();
        CarReqUtils.expressost(this, this, null, new ExpressCost(), "expressCost", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("lat_o", lat_o).putValue("lng_o", lng_o).
                        putValue("lat_d", lat_d).putValue("lng_d", lng_d).
                        putValue("origin_region", origin_region).putValue("destination_region", destination_region)
                        .putValue("length", respOrderDetail.getData().getLength()).putValue("width", respOrderDetail.getData().getWidth())
                        .putValue("height", respOrderDetail.getData().getHeight()).putValue("weight", respOrderDetail.getData().getWeight()).createMap()));

    }

    //接力预估费用
    private void relexpressCost() {
        showLoadingDialog();
        if (lists.size() == 3) {

            lat_d =respSDOrderDetail.getData().getLat_d();
            lng_d =respSDOrderDetail.getData().getLng_d();
            destination_region =respSDOrderDetail.getData().getDestination_region();

            CarReqUtils.relexpressost(this, this, null, new ExpressCost(), "relexpressCost", true,
                    StringUrlUtils.geturl(hashMapUtils.putValue("lat_o", lat_o).putValue("lng_o", lng_o).
                            putValue("lat_d", lat_d).putValue("lng_d", lng_d).
                            putValue("origin_region", origin_region).putValue("destination_region", destination_region)
                            .putValue("length", respSDOrderDetail.getData().getLength()).putValue("width", respSDOrderDetail.getData().getWidth())
                            .putValue("height", respSDOrderDetail.getData().getHeight()).putValue("weight", respSDOrderDetail.getData().getWeight()).
                                    putValue("orderid", orderId).createMap()));

        } else {
            CarReqUtils.relexpressost(this, this, null, new ExpressCost(), "relexpressCost", true,
                    StringUrlUtils.geturl(hashMapUtils.putValue("lat_o", lat_o).putValue("lng_o", lng_o).
                            putValue("lat_d", lat_d).putValue("lng_d", lng_d).
                            putValue("origin_region", origin_region).putValue("destination_region", destination_region)
                            .putValue("length", respSDOrderDetail.getData().getLength()).putValue("width", respSDOrderDetail.getData().getWidth())
                            .putValue("height", respSDOrderDetail.getData().getHeight()).putValue("weight", respSDOrderDetail.getData().getWeight()).
                                    putValue("orderid", orderId).createMap()));

        }

    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {
        if (output != null) {
            dismissLoadingDialog();
            if (output instanceof RespOrderDetail) {
                respOrderDetail = (RespOrderDetail) output;
                if (respOrderDetail.getRes().equals("0")) {
                    initView();

                    linearLayout.setVisibility(View.GONE);
                    tvTypeName.setText(name);
                    tvDistance.setText(respOrderDetail.getData().getDistance());
                    tvStart.setText(respOrderDetail.getData().getBeginningplace());
                    tvEnd.setText(respOrderDetail.getData().getDestination());
                    tvTakeOrderPerson.setText(respOrderDetail.getData().getName());
                    tvTakeOrderPhone.setText(respOrderDetail.getData().getContact());
                    tvGoodsName.setText(respOrderDetail.getData().getGoodsname());
                    tvGoodsPrice.setText(respOrderDetail.getData().getDeclared());
                    tvGoodsSize.setText(respOrderDetail.getData().getLength() + "x" + respOrderDetail.getData().getWidth() + "x" + respOrderDetail.getData().getHeight() + "cm");
                    tvGoodsWeight.setText(respOrderDetail.getData().getWeight() + "kg");
                    tvReceiveName.setText(respOrderDetail.getData().getAddressee());
                    tvReceivePhone.setText(respOrderDetail.getData().getRec_tel());

                    lat_o = respOrderDetail.getData().getLat_o();
                    lng_o = respOrderDetail.getData().getLng_o();
                    origin_region = respOrderDetail.getData().getOrigin_region();

                }
            }

            if (output instanceof RespSDOrderDetail) {
                respSDOrderDetail = (RespSDOrderDetail) output;
                if (respSDOrderDetail.getRes().equals("0")) {
                    initView();
                    listView = (MyListView) findViewById(R.id.listViewId);
                    lists = respSDOrderDetail.getRelayinfo();
                    adapter = new SDListViewAdapter(this, lists);
                    listView.setAdapter(adapter);

                    if (respSDOrderDetail.getRelayinfo().size() == 3) {
                        addressLinear.setVisibility(View.GONE);
                    }

                    tvTypeName.setText(name);
                    tvDistance.setText(respSDOrderDetail.getData().getDistance());
                    tvStart.setText(respSDOrderDetail.getData().getBeginningplace());
                    tvEnd.setText(respSDOrderDetail.getData().getDestination());
                    tvTakeOrderPerson.setText(respSDOrderDetail.getData().getName());
                    tvTakeOrderPhone.setText(respSDOrderDetail.getData().getContact());
                    tvGoodsName.setText(respSDOrderDetail.getData().getGoodsname());
                    tvGoodsPrice.setText(respSDOrderDetail.getData().getDeclared());
                    tvGoodsSize.setText(respSDOrderDetail.getData().getLength() + "x" + respSDOrderDetail.getData().getWidth() + "x" + respSDOrderDetail.getData().getHeight() + "cm");
                    tvGoodsWeight.setText(respSDOrderDetail.getData().getWeight() + "kg");
                    tvReceiveName.setText(respSDOrderDetail.getData().getAddressee());
                    tvReceivePhone.setText(respSDOrderDetail.getData().getRec_tel());

                    lat_o = respSDOrderDetail.getData().getLat_o();
                    lng_o = respSDOrderDetail.getData().getLng_o();
                    origin_region = respSDOrderDetail.getData().getOrigin_region();

//                    lat_d = respSDOrderDetail.getData().getLat_d();
//                    lng_d = respSDOrderDetail.getData().getLng_d();
//                    destination_region = respSDOrderDetail.getData().getDestination_region();
                }
            }

            if (output instanceof ExpressCost) {
                ExpressCost cost = (ExpressCost) output;
                if (cost.getRes().equals("0")) {

                    tvAllMoney = cost.getData().getCost();

                    dialog = new ExitDialog(SDRelayActivity.this);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }
            }

            if (output instanceof TakeOrder) {
                TakeOrder order = (TakeOrder) output;
                if (order.getRes().equals("0")) {
                    EUtil.showToast("接单成功");

                    finish();

//                    tvSure.setVisibility(View.GONE);
//                    bottomLinear.setVisibility(View.VISIBLE);
                } else {
                    EUtil.showToast(order.getErr());
                }
            }

        } else {
            dismissLoadingDialog();
            EUtil.showToast("网络错误,请稍后重试");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sureId:
                if (lists.size() == 3) {
                    if (!TextUtils.isEmpty(etTime.getText().toString().trim())) {

                        if (type == 1) {
                            expressCost();

                        } else {

                            relexpressCost();

                        }

                    } else {
                        EUtil.showToast("请先选择交接时间");
                    }
                } else {
                    if (!TextUtils.isEmpty(tvAddress.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(etTime.getText().toString().trim())) {

                            if (type == 1) {
                                expressCost();

                            } else {

                                relexpressCost();

                            }

                        } else {
                            EUtil.showToast("请先选择交接时间");
                        }
                    } else {
                        EUtil.showToast("请先填写交接地点");
                    }
                }

                break;
            case R.id.yisonghuoId:
                EUtil.showToast("已取货");
                break;

            case R.id.yisongdaId:
                EUtil.showToast("已送达");
                break;

            case R.id.setting_exit_ensure:
                dialog.dismiss();

                if (type == 1) {
                    reqFirstBatter();

                } else {

                    reqNoFirstBatter();
                }

                break;

            case R.id.setting_exit_cancel:
                dialog.dismiss();
                break;
        }
    }

    //预计费用 dialog
    class ExitDialog extends AlertDialog {

        int index;

        public ExitDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);

            TextView tvTitle = (TextView) findViewById(R.id.titleId);
            tvTitle.setText("预计费用:");

            TextView tvMoney = (TextView) findViewById(R.id.tvDialogActivity);
            tvMoney.setText(tvAllMoney + "元");
            tvMoney.setTextColor(getResources().getColor(R.color.txt_orange));

            findViewById(R.id.setting_exit_ensure).setOnClickListener(SDRelayActivity.this);

            findViewById(R.id.setting_exit_cancel).setOnClickListener(SDRelayActivity.this);

        }
    }
}
