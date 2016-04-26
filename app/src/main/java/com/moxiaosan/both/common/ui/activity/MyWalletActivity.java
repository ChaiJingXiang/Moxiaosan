package com.moxiaosan.both.common.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.adapter.WalletListAdapter;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Mybalance;
import consumer.model.obj.RespMybalance;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

public class MyWalletActivity extends BaseActivity implements View.OnClickListener, IXListViewRefreshListener, IXListViewLoadMore {
    private XListView walletListView;
    private WalletListAdapter walletListAdapter;
    private List<RespMybalance> respMybalanceList = new ArrayList<RespMybalance>();
    private int pageNow = 1;
    private TextView tvLeave;
    private int userType = 0;  //用户类型  调用不同的接口


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        userType = getIntent().getIntExtra("userType", 0);
        showActionBar(true);
        setActionBarName("我的钱包");
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        walletListView.startRefresh();
    }

    private void initView() {
        tvLeave = (TextView) findViewById(R.id.my_wallet_leave_money_txt);
        walletListView = (XListView) findViewById(R.id.my_wallet_listview);
        walletListAdapter = new WalletListAdapter(this, respMybalanceList);
        walletListView.setAdapter(walletListAdapter);
        findViewById(R.id.my_wallet_withdraw_money_txt).setOnClickListener(this);
        findViewById(R.id.my_wallet_recharge_txt).setOnClickListener(this);
        walletListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        walletListView.setPullRefreshEnable(this); //刷新
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_wallet_withdraw_money_txt:
                startActivity(new Intent(MyWalletActivity.this, WithdrawMoneyActivity.class));
                break;
            case R.id.my_wallet_recharge_txt:
                startActivity(new Intent(MyWalletActivity.this, RechargeActivity.class));
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
            if (output instanceof Mybalance) {
                Mybalance mybalance = (Mybalance) output;
                tvLeave.setText("¥" + mybalance.getBalance() + "元");
                if ("-1".equals(mybalance.getRes())) {  //消息列表没有更多  不要加载更多
                    walletListView.disablePullLoad();
                    walletListView.stopRefresh(new Date());
                    //EUtil.showToast(mybalance.getErr());
                } else { //  0
                    if (input.equals("onRefresh")) {
                        respMybalanceList.clear();
                        walletListView.stopRefresh(new Date());
                    } else if (input.equals("onLoadMore")) {
                        walletListView.stopLoadMore();
                    }
                    List<RespMybalance> newList = mybalance.getData();
                    respMybalanceList.addAll(newList);
                    walletListAdapter.reFreshData(respMybalanceList);
                    walletListView.setPullLoadEnable(MyWalletActivity.this);
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        pageNow = 1;
        if (userType == 1) {  //用户端
            ConsumerReqUtil.mybalance(this, iApiCallback, null, new Mybalance(), "onRefresh", true,
                    StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", pageNow).createMap()));
        } else {
            CarReqUtils.cuserbalance(this, iApiCallback, null, new Mybalance(), "onRefresh", true,
                    StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", pageNow).createMap()));
        }
    }

    @Override
    public void onLoadMore() {
        pageNow += 1;
        if (userType == 1) {  //用户端
            ConsumerReqUtil.mybalance(this, iApiCallback, null, new Mybalance(), "onLoadMore", true,
                    StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", pageNow).createMap()));
        } else {
            CarReqUtils.cuserbalance(this, iApiCallback, null, new Mybalance(), "onLoadMore", true,
                    StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", pageNow).createMap()));
        }
    }
}
