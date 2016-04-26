package com.moxiaosan.both.carowner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
public class HitchhikingActivity extends BaseActivity implements IApiCallback{

    private String lat,lng;
    private String city;
    private String orderId;
    private String name;
    private TextView tvSure;

    private TextView tvDistance,tvStart,tvEnd,tvTakeOrderPerson,tvTakeOrderPhone,tvAllMoney;
    private TextView tvTypeName;
    private RespOrderDetail detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);

        city =sp.getString("city","");
        lng =sp.getString("longitude","");
        lat =sp.getString("latitude","");
        Log.i("info", city + "," + lat + "," + lng);

        Intent intent =getIntent();

        name =intent.getStringExtra("name");
        orderId =intent.getStringExtra("orderId");

        showActionBar(true);
        setActionBarName("订单号：" + orderId);

        setContentView(R.layout.b_hitchhiking_layout);

        initView();

        reqData();

    }


    private void reqData(){

        showLoadingDialog();

        CarReqUtils.orderInfo(this, this, null, new RespOrderDetail(), "OrderDetail", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("orderid", orderId).putValue("lat", lat).putValue("lng", lng).
                        putValue("type", "1").putValue("origin_region", city).createMap()));
    }


    private void initView() {

        tvTypeName =(TextView)findViewById(R.id.typeNameId);
        tvDistance =(TextView)findViewById(R.id.distance);
        tvStart =(TextView)findViewById(R.id.shifadiId);
        tvEnd =(TextView)findViewById(R.id.mudidiId);
        tvTakeOrderPerson =(TextView)findViewById(R.id.xiadanrenId);
        tvTakeOrderPhone =(TextView)findViewById(R.id.xiadanPhoneId);
        tvAllMoney =(TextView)findViewById(R.id.allMoney);
        tvSure =(TextView)findViewById(R.id.sureId);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoadingDialog();
                CarReqUtils.order(HitchhikingActivity.this, HitchhikingActivity.this, null, new TakeOrder(), "Order", true,
                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                putValue("orderid", orderId).createMap()));
            }
        });
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {
        if(output!=null) {
            dismissLoadingDialog();
            if (output instanceof RespOrderDetail) {
                detail = (RespOrderDetail) output;
                if (detail.getRes().equals("0")) {
                    tvTypeName.setText(name);
                    tvDistance.setText(detail.getData().getDistance());
                    tvStart.setText(detail.getData().getBeginningplace());
                    tvEnd.setText(detail.getData().getDestination());
                    tvTakeOrderPerson.setText(detail.getData().getName());
                    tvTakeOrderPhone.setText(detail.getData().getContact());
                    tvAllMoney.setText(detail.getData().getReward());
                }
            }

            if (output instanceof TakeOrder) {

                TakeOrder order = (TakeOrder) output;

                if (order.getRes().equals("0")) {
                    EUtil.showToast("成功接单");
                    finish();
                }else{
                    EUtil.showToast(order.getErr());
                }
            }
        }
    }
}
