package com.moxiaosan.both.carowner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import consumer.model.CashPay;
import consumer.model.RespOrderedInfo;
import consumer.model.obj.PickUp;

/**
 * Created by chris on 16/3/1.
 */
public class ReceiveOrderOkActivity extends BaseActivity implements IApiCallback {

    private TextView tvDistance, tvStart, tvEnd, tvTakeOrderPerson, tvTakeOrderPhone, tvGoodsName, tvGoodsPrice, tvGoodsSize, tvGoodsWeight,
            tvReceiveName, tvReceivePhone;

    private TextView tvTypeName, tvAllMoney;

    private TextView tvQuHuo, tvSongDa;

    private String orderId;
    private String name;
    private TextView tvMoneyPay;
    private LinearLayout bottomLinear;
    private LinearLayout hideLinear;

    private String lat, lng;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBar(true);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");

        orderId = intent.getStringExtra("orderId");

        setActionBarName("订单号:" + orderId);

        setContentView(R.layout.b_receiveok_layout);

        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        city = sp.getString("city", "");
        lng = sp.getString("longitude", "");
        lat = sp.getString("latitude", "");

        hideLinear = (LinearLayout) findViewById(R.id.hideId);
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

        tvMoneyPay = (TextView) findViewById(R.id.moneyPayId);
        bottomLinear = (LinearLayout) findViewById(R.id.bottomLinearId);
        tvQuHuo = (TextView) findViewById(R.id.yisonghuoId);
        tvSongDa = (TextView) findViewById(R.id.yisongdaId);
        if (name.equals("顺风车")) {
            hideLinear.setVisibility(View.GONE);
            tvQuHuo.setText("已接乘客");
        }
        tvQuHuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoadingDialog();
                CarReqUtils.pickup(ReceiveOrderOkActivity.this, ReceiveOrderOkActivity.this, null, new PickUp(), "PickUp", true,
                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                putValue("orderid", orderId).createMap()));

            }
        });

        tvSongDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();

                CarReqUtils.delivery(ReceiveOrderOkActivity.this, ReceiveOrderOkActivity.this, null, new PickUp(), "delivery", true,
                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                putValue("orderid", orderId).createMap()));

            }
        });

        showLoadingDialog();

        CarReqUtils.orderedInfo(this, this, null, new RespOrderedInfo(), "orderedInfo", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("orderid", orderId)
                        .putValue("username", AppData.getInstance().getUserEntity().getUsername())
                        .putValue("lat", lat).putValue("lng", lng).putValue("origin_region", city).createMap()));
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {

        dismissLoadingDialog();
        if (output instanceof RespOrderedInfo) {

            RespOrderedInfo detail = (RespOrderedInfo) output;
            if (detail.getRes().equals("0")) {
                tvTypeName.setText(name);
                tvDistance.setText(detail.getData().getDistance()+ "");
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

                if (detail.getData().getServicestatus().equals("1")) {

                    tvQuHuo.setTextColor(getResources().getColor(R.color.txt_666666));
                    tvSongDa.setTextColor(getResources().getColor(R.color.txt_white));

                    tvQuHuo.setBackgroundResource(R.mipmap.yisongda_pic);
                    tvSongDa.setBackgroundResource(R.mipmap.yiquhuo_pic);

                } else if (detail.getData().getServicestatus().equals("2")) {

                    tvMoneyPay.setVisibility(View.VISIBLE);
                    bottomLinear.setVisibility(View.GONE);

                    tvMoneyPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showLoadingDialog();
                            CarReqUtils.cashpayment(ReceiveOrderOkActivity.this, ReceiveOrderOkActivity.this, null, new CashPay(), "cashpayment", true,
                                    StringUrlUtils.geturl(hashMapUtils.putValue("orderid", orderId).createMap()));

                        }
                    });

                }

            }

        }

        if (output instanceof PickUp) {

            PickUp pickUp = (PickUp) output;

            if (pickUp.getRes().equals("0")) {
                if (input.equals("PickUp")) {
                    EUtil.showToast("取货成功");
                    finish();

                } else {

                    EUtil.showToast("送达成功");

                    tvMoneyPay.setVisibility(View.VISIBLE);
                    bottomLinear.setVisibility(View.GONE);

                    tvMoneyPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showLoadingDialog();
                            CarReqUtils.cashpayment(ReceiveOrderOkActivity.this, ReceiveOrderOkActivity.this, null, new CashPay(), "cashpayment", true,
                                    StringUrlUtils.geturl(hashMapUtils.putValue("orderid", orderId).createMap()));

                        }
                    });

                }

            } else {

                EUtil.showToast(pickUp.getErr());
            }


        }

        if (output instanceof CashPay) {
            CashPay cashPay = (CashPay) output;
            if (cashPay.getRes().equals("0")) {
                EUtil.showToast(cashPay.getErr());
                finish();
            }
        }
    }
}
