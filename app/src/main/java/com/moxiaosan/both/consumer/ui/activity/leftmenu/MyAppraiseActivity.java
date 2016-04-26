package com.moxiaosan.both.consumer.ui.activity.leftmenu;

import android.os.Bundle;
import android.view.View;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.adapter.AppraiseListAdapter;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Mycomments;
import consumer.model.obj.RespMycomment;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

public class MyAppraiseActivity extends BaseActivity implements IXListViewRefreshListener, IXListViewLoadMore {
    private XListView myAppraiseListView;
    private AppraiseListAdapter appraiseListAdapter;
    private int pageNow = 1;
    private Mycomments mycomments;
    private List<RespMycomment> respMycommentList = new ArrayList<RespMycomment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appraise);
        showActionBar(true);
        setActionBarName("我的评价");
        initView();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void initView() {
        myAppraiseListView = (XListView) findViewById(R.id.my_appraise_listview);
        appraiseListAdapter = new AppraiseListAdapter(this, respMycommentList);
        myAppraiseListView.setAdapter(appraiseListAdapter);
        myAppraiseListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        myAppraiseListView.setPullRefreshEnable(this); //刷新
        myAppraiseListView.startRefresh();
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
            if (output instanceof Mycomments) {
                mycomments = (Mycomments) output;
                if ("-1".equals(mycomments.getRes())) {  //消息列表没有更多  不要加载更多
                    myAppraiseListView.disablePullLoad();
                    myAppraiseListView.stopRefresh(new Date());
                    EUtil.showToast(mycomments.getErr());
                } else { //  0
                    if (input.equals("onRefresh")) {
                        respMycommentList.clear();
                        myAppraiseListView.stopRefresh(new Date());
                    } else if (input.equals("onLoadMore")) {
                        myAppraiseListView.stopLoadMore();
                    }
                    List<RespMycomment> newList = mycomments.getData();
                    respMycommentList.addAll(newList);
                    appraiseListAdapter.reFreshData(respMycommentList);
                    myAppraiseListView.setPullLoadEnable(MyAppraiseActivity.this);
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        pageNow = 1;
        ConsumerReqUtil.mycomments(this, iApiCallback, null, new Mycomments(), "onRefresh", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", pageNow).createMap()));
    }

    @Override
    public void onLoadMore() {
        pageNow += 1;
        ConsumerReqUtil.mycomments(this, iApiCallback, null, new Mycomments(), "onLoadMore", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", pageNow).createMap()));
    }
}
