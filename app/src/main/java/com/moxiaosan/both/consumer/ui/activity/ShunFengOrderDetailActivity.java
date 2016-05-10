package com.moxiaosan.both.consumer.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.activity.ShareActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

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
    private ImageView img1, img2, imgLine1;
    private TextView tvFromPlace, tvToPlace, tvJiedanRen, tvJieDanPhone;
    private ImageView imgMap;
    private LinearLayout layoutShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shun_feng_order_detail);
        showActionBar(true);
        respUserOrder = (RespUserOrder) getIntent().getSerializableExtra("respUserOrder");
        isPayed = getIntent().getBooleanExtra("isPayed", false);
        setActionBarName("订单编号：" + respUserOrder.getOrderid());
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ConsumerReqUtil.userorderinfo(this, iApiCallback, null, new Userorderinfo(), respUserOrder.getOrderid(), true, "orderid=" + respUserOrder.getOrderid());
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

        imgMap = (ImageView) findViewById(R.id.shun_feng_order_detail_map_img);
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

    private void setData(RespUserOrderInfo respUserOrderInfo){
        ImageLoader.getInstance().displayImage("https://www.baidu.com/img/bd_logo1.png", imgMap);
        tvFromPlace.setText(respUserOrderInfo.getBeginningplace());
        tvToPlace.setText(respUserOrderInfo.getDestination());
        tvJiedanRen.setText(respUserOrderInfo.getCommentsid());
        tvJieDanPhone.setText(respUserOrderInfo.getCom_tel());

        int status=Integer.valueOf(respUserOrderInfo.getServicestatus());
        if (status>0){ //1
            tvTime1.setText(respUserOrderInfo.getPickuptime());
            img1.setImageResource(R.mipmap.order_state_light);
            tvQuJian.setTextColor(getResources().getColor(R.color.main_color));
        }
        if (status>1){  //2
            tvTime2.setText(respUserOrderInfo.getDeliverytime());
            imgLine1.setBackgroundColor(getResources().getColor(R.color.main_color));
            img2.setImageResource(R.mipmap.order_state_light);
            tvQueRen.setTextColor(getResources().getColor(R.color.main_color));
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
                startActivity(new Intent(ShunFengOrderDetailActivity.this, SelectPayMethodActivity.class).putExtra("respUserOrder",respUserOrder));
                finish();
                break;
            case R.id.shun_feng_order_detail_cancel:
                if (isPayed) { //评价
                    Intent intent=new Intent(ShunFengOrderDetailActivity.this, OrderCommentActivity.class);
                    intent.putExtra("respUserOrder", respUserOrder);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                } else {  //取消订单
                    CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(ShunFengOrderDetailActivity.this, respUserOrder.getOrderid());
                    cancelOrderDialog.show();
                }
                break;

            case R.id.shun_feng_order_detail_share_layout:
                Intent shareIntent = new Intent(ShunFengOrderDetailActivity.this, ShareActivity.class);
                this.startActivity(shareIntent);
                overridePendingTransition(R.anim.share_pop_in, 0);
                break;
        }
    }


    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (isLoadingDialogShowing()){
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
                Userorderinfo userorderinfo= (Userorderinfo) output;
                setData(userorderinfo.getData());
            }
            if (output instanceof Payment){
                Payment payment= (Payment) output;
                EUtil.showToast(payment.getErr());
                if (payment.getRes().equals("0")){
                    isPayed=true;
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
}
