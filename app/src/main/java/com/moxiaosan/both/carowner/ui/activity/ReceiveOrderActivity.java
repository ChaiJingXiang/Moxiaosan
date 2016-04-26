package com.moxiaosan.both.carowner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.RespOrderDetail;
import consumer.model.TakeOrder;

/**
 * Created by chris on 16/3/1.
 */

public class ReceiveOrderActivity extends BaseActivity implements IApiCallback, View.OnClickListener {

    private TextView tvDistance, tvStart, tvEnd, tvTakeOrderPerson, tvTakeOrderPhone, tvGoodsName, tvGoodsPrice, tvGoodsSize, tvGoodsWeight,
            tvReceiveName, tvReceivePhone;
    private TextView tvTypeName, tvAllMoney;

    private String lat, lng;
    private String city;
    private String orderId;
    private String name;

    private TextView tvSure;

    private LinearLayout bottomLinear;
    private TextView tvQuHuo, tvSongDa;

    private RespOrderDetail detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);

        city = sp.getString("city", "");
        lng = sp.getString("longitude", "");
        lat = sp.getString("latitude", "");
        Log.i("info", city + "," + lat + "," + lng);


        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        orderId = intent.getStringExtra("orderId");

        setContentView(R.layout.b_zhidajiedan_layout);

        initView();

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
        tvAllMoney = (TextView) findViewById(R.id.allMoney);

        tvSure = (TextView) findViewById(R.id.sureId);
        tvSure.setOnClickListener(this);
//        bottomLinear = (LinearLayout) findViewById(R.id.bottomLinearId);
//        tvQuHuo = (TextView) findViewById(R.id.yisonghuoId);
//        tvSongDa = (TextView) findViewById(R.id.yisongdaId);

    }


    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void reqData() {

        showLoadingDialog();

        CarReqUtils.orderInfo(this, this, null, new RespOrderDetail(), "OrderDetail", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("orderid", orderId).putValue("lat", lat).putValue("lng", lng).
                        putValue("type", "1").putValue("origin_region", city).createMap()));
    }

    @Override
    public void onData(Object output, Object input) {

        if (output != null) {
            dismissLoadingDialog();
            if (output instanceof RespOrderDetail) {
                detail = (RespOrderDetail) output;
                if (detail.getRes().equals("0")) {
                    tvTypeName.setText(name);
                    tvDistance.setText(detail.getData().getDistance() + "");
                    tvStart.setText(detail.getData().getBeginningplace() + "");
                    tvEnd.setText(detail.getData().getDestination() + "");
                    tvTakeOrderPerson.setText(detail.getData().getName() + "");
                    tvTakeOrderPhone.setText(detail.getData().getContact() + "");
                    tvGoodsName.setText(detail.getData().getGoodsname() + "");
                    tvGoodsPrice.setText(detail.getData().getDeclared() + "");
                    tvGoodsSize.setText(detail.getData().getLength() + "x" + detail.getData().getWidth() + "x" + detail.getData().getHeight() + "cm" + "");
                    tvGoodsWeight.setText(detail.getData().getWeight() + "kg");
                    tvReceiveName.setText(detail.getData().getAddressee() + "");
                    tvReceivePhone.setText(detail.getData().getRec_tel() + "");
                    tvAllMoney.setText(detail.getData().getReward());
                }
            }

            if (output instanceof TakeOrder) {
                TakeOrder order = (TakeOrder) output;
                if (order.getRes().equals("0")) {
                    EUtil.showToast("成功接单");

                    finish();
//                    tvSure.setVisibility(View.GONE);
//                    bottomLinear.setVisibility(View.VISIBLE);

                }else{

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

                showLoadingDialog();
                CarReqUtils.order(this, this, null, new TakeOrder(), "Order", true,
                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                putValue("orderid", orderId).createMap()));
                break;

//            case R.id.yisonghuoId:
//                EUtil.showToast("已取货");
//                break;
//
//            case R.id.yisongdaId:
//                EUtil.showToast("已送达");
//                break;
        }
    }
}
