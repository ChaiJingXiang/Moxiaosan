package com.moxiaosan.both.common.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.moxiaosan.both.R;
import com.utils.ui.base.BaseActivity;

/**
 * 提现界面
 */
public class WithdrawMoneyActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout wechat,zhifubao,card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_money);
        showActionBar(true);
        setActionBarName("提现");

        findViewById(R.id.wechat_layout).setOnClickListener(this);
        findViewById(R.id.zhifubao_layout).setOnClickListener(this);
        findViewById(R.id.card_layout).setOnClickListener(this);
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wechat_layout:

                startActivity(new Intent(this,WeChatMoneyActivity.class).putExtra("type",1));

                break;
            case R.id.zhifubao_layout:

                startActivity(new Intent(this,WeChatMoneyActivity.class).putExtra("type",2));

                break;
            case R.id.card_layout:

                startActivity(new Intent(this,CardMoneyActivity.class));

                break;

        }

    }
}
