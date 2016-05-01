package com.moxiaosan.both.common.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.adapter.WithdrawHisListAdapter;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.Date;
import java.util.List;

import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.RespWithdrawList;
import consumer.model.obj.WithdrawObj;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

/**
 * Created by chris on 16/4/29.
 */
public class WithdrawHistoryActivity extends BaseActivity implements IApiCallback, IXListViewRefreshListener, IXListViewLoadMore {
    private XListView listView;
    private WithdrawHisListAdapter adapter;
    private List<WithdrawObj> lists;
    private TextView tvNoData;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBar(true);
        setActionBarName("提现记录");
        setContentView(R.layout.withdraw_his_layout);

        listView = (XListView) findViewById(R.id.withdrawHisListId);
        tvNoData = (TextView) findViewById(R.id.noData);

        showLoadingDialog();
        reqData(page, "onFirst");
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {

        if (output != null) {
            dismissLoadingDialog();
            if (output instanceof RespWithdrawList) {
                RespWithdrawList withdrawList = (RespWithdrawList) output;
                if (withdrawList.getRes().equals("0")) {

                    if (input.equals("onFirst")) {
                        lists = withdrawList.getData();

                        if (lists.size() == 0) {

                            tvNoData.setVisibility(View.VISIBLE);

                        } else {
                            adapter = new WithdrawHisListAdapter(this, lists);
                            listView.setAdapter(adapter);
                            listView.setPullRefreshEnable(this);
                        }

                    } else if (input.equals("onRefresh")) {
                        listView.stopRefresh(new Date());
                        lists.clear();
                        lists = withdrawList.getData();
                        adapter = new WithdrawHisListAdapter(this, lists);
                        listView.setAdapter(adapter);
                        listView.setPullRefreshEnable(this);
                    } else {

                        listView.stopLoadMore();
                        List<WithdrawObj> newList = withdrawList.getData();
                        lists.addAll(newList);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    dismissLoadingDialog();
                    if (input.equals("onFirst")) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        listView.disablePullLoad();
                        EUtil.showToast("没有更多了哦~");
                    }
                }
            }
        } else {
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }
    }

    @Override
    public void onLoadMore() {
        page += 1;
        reqData(page, "loadMore");
    }

    @Override
    public void onRefresh() {
        page = 1;
        listView.setPullLoadEnable(this);
        reqData(page, "onRefresh");
    }

    private void reqData(int page, String input) {
        UserReqUtil.cashapprec(this, this, null, new RespWithdrawList(), input, true, StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", page).createMap()));
    }
}
