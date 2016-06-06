package com.moxiaosan.both.common.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

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

import java.text.SimpleDateFormat;
import java.util.Date;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.PaymxsAlipay;
import consumer.model.PaymxsWX;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    private EditText etMoney;
    private RadioGroup radioGroup;
    private final static int RECHARGE_WX = 1; //微信
    private final static int RECHARGE_ALIPAY = 2;  //支付宝
    private int chooseType = RECHARGE_WX; //默认微信

    private IWXAPI api;
    //支付宝
    private static final int SDK_PAY_FLAG = 1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        showActionBar(true);
        setActionBarName("充值");
        initView();

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
    }

    private void initView() {
        etMoney = (EditText) findViewById(R.id.recharge_num);
        radioGroup = (RadioGroup) findViewById(R.id.recharge_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.recharge_radiobtn_weixin) {  //微信
                    chooseType = RECHARGE_WX;
                } else if (checkedId == R.id.recharge_radiobtn_zhifubao) {  //支付宝
                    chooseType = RECHARGE_ALIPAY;
                }
            }
        });
        findViewById(R.id.recharge_ensure).setOnClickListener(this);
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_ensure:
                if (!TextUtils.isEmpty(etMoney.getText().toString())) {
                    Date date=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
                    String sn=AppData.getInstance().getUserEntity().getUsername()+sdf.format(date);
                    if (chooseType == RECHARGE_WX) { //微信
                        UserReqUtil.payMxs(RechargeActivity.this, iApiCallback, null, new PaymxsWX(), chooseType,
                                true, StringUrlUtils.geturl(new HashMapUtils().putValue("pay_type", "wx").putValue("amount", (int) (Double.valueOf(etMoney.getText().toString())*100))
                                        .putValue("sn", sn).putValue("username", AppData.getInstance().getUserEntity().getUsername())
                                        .putValue("type", "up").createMap()));
                        showLoadingDialog();
                    } else { //支付宝
                        UserReqUtil.payMxs(RechargeActivity.this, iApiCallback, null, new PaymxsAlipay(), chooseType,
                                true, StringUrlUtils.geturl(new HashMapUtils().putValue("pay_type", "alipay").putValue("amount", (int)(Double.valueOf(etMoney.getText().toString())*100))
                                        .putValue("sn", sn).putValue("username", AppData.getInstance().getUserEntity().getUsername())
                                        .putValue("type", "up").createMap()));
                        showLoadingDialog();
                    }
                } else {
                    EUtil.showToast("充值金额不能为空");
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
            if (Integer.parseInt(String.valueOf(input)) == RECHARGE_WX) {//微信
                PaymxsWX paymxsWX = (PaymxsWX) output;
                if (paymxsWX.getPrepayid() != null) {
                    PayReq req = new PayReq();
                    req.appId = paymxsWX.getAppid();
                    req.partnerId = paymxsWX.getPartnerid();
                    req.prepayId = paymxsWX.getPrepayid();
                    req.nonceStr = paymxsWX.getNoncestr();
                    req.timeStamp = paymxsWX.getTimestamp()+"";
                    req.packageValue = "Sign=WXPay";
                    req.sign = paymxsWX.getSign();
                    //req.extData = "app data"; // optional
                    //在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    EUtil.showToast("参数有误");
                }
            } else if (Integer.parseInt(String.valueOf(input)) == RECHARGE_ALIPAY) { //支付宝
                PaymxsAlipay paymxsAlipay = (PaymxsAlipay) output;
                if (paymxsAlipay.getErr() == 0) {
                    alipay(paymxsAlipay.getQuery());
                } else {
                    EUtil.showToast("数据有误");
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
                PayTask alipay = new PayTask(RechargeActivity.this);
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
}
