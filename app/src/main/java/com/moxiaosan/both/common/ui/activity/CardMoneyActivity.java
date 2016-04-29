package com.moxiaosan.both.common.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.RespWithdraw;

/**
 * Created by chris on 16/4/29.
 */
public class CardMoneyActivity extends BaseActivity implements IApiCallback{

    private EditText etCardType,etCardNum,etNmae,etMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.card_money_layout);

        showActionBar(true);
        setActionBarName("提现到银行卡");


        etCardType =(EditText)findViewById(R.id.cardTypeId);
        etCardNum =(EditText)findViewById(R.id.cardNumId);
        etNmae =(EditText)findViewById(R.id.card_nameId);
        etMoney =(EditText)findViewById(R.id.card_moneyId);

        findViewById(R.id.sureId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(etCardType.getText().toString())){
                    if(!TextUtils.isEmpty(etCardNum.getText().toString())){
                        if(!TextUtils.isEmpty(etNmae.getText().toString())){
                            if(!TextUtils.isEmpty(etMoney.getText().toString())){

                                showLoadingDialog();

                                UserReqUtil.cashappbank(CardMoneyActivity.this,CardMoneyActivity.this,null,new RespWithdraw(),"cashappbank",true,
                                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                                putValue("money",etMoney.getText().toString()).putValue("type",etCardNum.getText().toString()).
                                                putValue("account",etCardNum.getText().toString()).putValue("bank",etCardType.getText().toString()).createMap()));


                            }else{
                                EUtil.showToast("提现金额为空，请填写");
                            }

                        }else{
                            EUtil.showToast("姓名不能为空，请填写");
                        }

                    }else{
                        EUtil.showToast("银行卡号号不能为空，请填写");
                    }
                }else{
                    EUtil.showToast("开户行名称不能为空，请填写");
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
