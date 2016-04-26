package com.moxiaosan.both.consumer.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Drivercomments;
import consumer.model.obj.RespUserOrder;

public class OrderCommentActivity extends BaseActivity {
    private RespUserOrder respUserOrder;
    private RatingBar ratingBarSpeed, ratingBarService;
    private EditText etContent;
    private TextView tvSure;
    private int speed, service;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comment);
        respUserOrder = (RespUserOrder) getIntent().getSerializableExtra("respUserOrder");
        type = getIntent().getIntExtra("type", 0);
        showActionBar(true);
        setActionBarName("评价");
        ratingBarSpeed = (RatingBar) findViewById(R.id.order_comment_ratingbar_speed);
        ratingBarService = (RatingBar) findViewById(R.id.order_comment_ratingbar_service);
        etContent = (EditText) findViewById(R.id.order_comment_content);
        tvSure = (TextView) findViewById(R.id.order_comment_ensure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsumerReqUtil.drivercomments(OrderCommentActivity.this, iApiCallback, null, new Drivercomments(), respUserOrder.getOrderid(), true,
                        StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("commentsid", respUserOrder.getCommentsid())
                                .putValue("type", type).putValue("commenttext", etContent.getText().toString()).putValue("speed", speed).putValue("services", service).putValue("orderid", respUserOrder.getOrderid()).createMap())
                );
                showLoadingDialog();
            }
        });
        ratingBarSpeed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                speed = (int) rating;
//                EUtil.showToast(speed+"");
            }
        });
        ratingBarService.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                service = (int) rating;
//                EUtil.showToast(service+"");
            }
        });
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
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

            if (output instanceof Drivercomments) {
                Drivercomments drivercomments = (Drivercomments) output;
                EUtil.showToast(drivercomments.getErr());
                if (drivercomments.getRes().equals("0")) {
                    finish();
                }
            }

        }
    };
}
