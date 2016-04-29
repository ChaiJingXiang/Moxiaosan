package com.moxiaosan.both.common.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.string.StringUtils;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.RespWithdraw;

/**
 * Created by chris on 16/4/29.
 */
public class WeChatMoneyActivity extends BaseActivity implements IApiCallback{

    private TextView tvName;
    private EditText etUserName,etPhone,etMoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wechat_money_layout);
        showActionBar(true);

        Intent intent =getIntent();
        final int type =intent.getIntExtra("type",1);

        tvName =(TextView)findViewById(R.id.typeNameId);
        etUserName =(EditText)findViewById(R.id.usernameId);
        etPhone =(EditText)findViewById(R.id.phoneId);
        etMoney =(EditText)findViewById(R.id.moneyId);

        if(type ==1){
            setActionBarName("提现到微信");
            tvName.setText("微信号");

        }else{
            setActionBarName("提现到支付宝");
            tvName.setText("支付宝账号");
        }

        findViewById(R.id.sureId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(etUserName.getText().toString())){
                    if(!TextUtils.isEmpty(etPhone.getText().toString())){
                        if(!TextUtils.isEmpty(etMoney.getText().toString())){
                            showLoadingDialog();
                            if(type ==1){

                                UserReqUtil.cashapp(WeChatMoneyActivity.this,WeChatMoneyActivity.this,null,new RespWithdraw(),"cashapp",true,
                                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                                putValue("money",etMoney.getText().toString()).putValue("type","微信钱包").
                                                putValue("account",etUserName.getText().toString()).putValue("tel",etPhone.getText().toString()).createMap()));
                            }else{

                                UserReqUtil.cashapp(WeChatMoneyActivity.this,WeChatMoneyActivity.this,null,new RespWithdraw(),"cashapp",true,
                                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                                putValue("money",etMoney.getText().toString()).putValue("type","支付宝").
                                                putValue("account",etUserName.getText().toString()).putValue("tel",etPhone.getText().toString()).createMap()));

                            }



                        }else{
                            EUtil.showToast("提现金额为空，请填写");
                        }

                    }else{
                        EUtil.showToast("绑定手机号不能为空，请填写");
                    }
                }else{
                    EUtil.showToast("账号不能为空，请填写");
                }

            }
        });

    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {
        if(output !=null){

            dismissLoadingDialog();
            if(output instanceof RespWithdraw){
                RespWithdraw withdraw =(RespWithdraw)output;
                if(withdraw.getRes().endsWith("0")){
                    EUtil.showToast(withdraw.getErr());
                    finish();

                }else{
                    EUtil.showToast(withdraw.getErr());
                }

            }
        }else{
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }

    }
}
