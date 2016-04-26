package com.moxiaosan.both.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.moxiaosan.both.pay.wxpay.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

/**
 * Created by fengyongqiang
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = ".WXPayEntryActivity";
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if(resp.errCode ==0){
//                MobclickAgent.onEvent(this, "weixin_pay");
//                App.getInstance().pay_ok=true;
                finish();

            }else{
                EUtil.showToast("取消支付");
                finish();
            }
        }
    }

}