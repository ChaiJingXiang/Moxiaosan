package com.moxiaosan.both.common.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.moxiaosan.both.R;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

/**
 * 提现界面
 */
public class WithdrawMoneyActivity extends BaseActivity implements View.OnClickListener {
    private EditText etMoney;
    private RadioGroup radioGroup;
    private int chooseType = 1; //默认微信

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_money);
        showActionBar(true);
        setActionBarName("提现");
        initView();
    }

    private void initView() {
        etMoney = (EditText) findViewById(R.id.withdraw_money_num);
        radioGroup = (RadioGroup) findViewById(R.id.withdraw_money_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.withdraw_money_radiobtn_weixin) {  //微信
                    chooseType = 1;
                } else if (checkedId == R.id.withdraw_money_radiobtn_zhifubao){  //支付宝
                    chooseType = 2;
                }
            }
        });
        findViewById(R.id.with_draw_money_ensure).setOnClickListener(this);
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.with_draw_money_ensure:
                if (!TextUtils.isEmpty(etMoney.getText().toString())){


                }else {
                    EUtil.showToast("提现金额不能为空");
                }

                break;
        }
    }

}
