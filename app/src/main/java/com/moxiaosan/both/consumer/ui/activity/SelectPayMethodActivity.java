package com.moxiaosan.both.consumer.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.moxiaosan.both.R;
import com.moxiaosan.both.pay.alipay.PayResult;
import com.moxiaosan.both.pay.wxpay.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.api.UserReqUtil;
import consumer.model.AliPay;
import consumer.model.Payment;
import consumer.model.WXPay;
import consumer.model.obj.RespUserOrder;

public class SelectPayMethodActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup radioGroup;
    private int chooseType = 1; //默认微信1   支付宝2   余额3
    private final static int WXPAY = 1;
    private final static int ALIPAY = 2;
    private final static int ACCOUNTPAY = 3;

    private RespUserOrder respUserOrder;
    private TextView tvCost;

    private IWXAPI api;

    //支付宝
    private static final int SDK_PAY_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pay_method);
        respUserOrder = (RespUserOrder) getIntent().getSerializableExtra("respUserOrder");
        showActionBar(true);
        setActionBarName("选择支付方式");
        initView();

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.pay_method_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pay_method_radiobtn_weixin) {  //微信
                    chooseType = 1;
                } else if (checkedId == R.id.pay_method_radiobtn_zhifubao) {  //支付宝
                    chooseType = 2;
                } else if (checkedId == R.id.pay_method_radiobtn_leave_money) {  //余额
                    chooseType = 3;
                }
            }
        });
        findViewById(R.id.pay_method_ensure).setOnClickListener(this);
        tvCost = (TextView) findViewById(R.id.pay_method_cost);
        tvCost.setText(respUserOrder.getCost() + "元");
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_method_ensure:
                if (chooseType == WXPAY) {
                    UserReqUtil.wxpay(SelectPayMethodActivity.this, iApiCallback, null, new WXPay(), "SelectPayMethodActivity", true,
                            StringUrlUtils.geturl(new HashMapUtils().putValue("pay_type", "wx").putValue("body", respUserOrder.getTitle()).putValue("amount", (int) (Double.valueOf(respUserOrder.getCost()) * 100))
                                    .putValue("sn", respUserOrder.getOrderid()).createMap()));
                    showLoadingDialog();
                } else if (chooseType == ALIPAY) {
                    UserReqUtil.wxpay(SelectPayMethodActivity.this, iApiCallback, null, new AliPay(), "SelectPayMethodActivity", true,
                            StringUrlUtils.geturl(new HashMapUtils().putValue("pay_type", "alipay").putValue("body", respUserOrder.getTitle()).putValue("amount", (int) (Double.valueOf(respUserOrder.getCost()) * 100))
                                    .putValue("sn", respUserOrder.getOrderid()).createMap()));
                    showLoadingDialog();
                } else if (chooseType == ACCOUNTPAY) {  //余额支付
                    ConsumerReqUtil.payment(SelectPayMethodActivity.this, iApiCallback, null, new Payment(), respUserOrder.getOrderid(), true,
                            StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("orderid", respUserOrder.getOrderid()).createMap()));
                    showLoadingDialog();
                }

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
            if (output instanceof Payment) {
                Payment payment = (Payment) output;
                EUtil.showToast(payment.getErr());
                if (payment.getRes().equals("0")) {
                    finish();
                }
            }
            if (output instanceof WXPay) {
                WXPay wxpay = (WXPay) output;
                if (wxpay != null) {
                    if (wxpay.getPrepayid() != null) {
                        PayReq req = new PayReq();
                        req.appId = wxpay.getAppid();
                        req.partnerId = wxpay.getPartnerid();
                        req.prepayId = wxpay.getPrepayid();
                        req.nonceStr = wxpay.getNoncestr();
                        req.timeStamp = wxpay.getTimestamp();
                        req.packageValue = "Sign=WXPay";
                        req.sign = wxpay.getSign();
                        //req.extData = "app data"; // optional
                        //在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        api.sendReq(req);
                    } else {
                        EUtil.showToast("参数有误");
                    }

                }
            }
            if (output instanceof AliPay) {
                AliPay aliPay = (AliPay) output;
                if (aliPay != null) {
                    alipay(aliPay.getQuery());
                }
            }
        }
    };

    private void alipay(final String payInfo) {
//        final String payInfo = "";
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(SelectPayMethodActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EUtil.showToast("支付成功");
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            EUtil.showToast("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            EUtil.showToast("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
}
