package com.moxiaosan.both.common.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.adapter.MessageListAdapter;
import com.moxiaosan.both.consumer.ui.activity.FindLabourActivity;
import com.moxiaosan.both.consumer.ui.activity.SellThingActivity;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.log.LLog;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Mynews;
import consumer.model.UpdateNews;
import consumer.model.obj.ResMynews;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

public class MessagesActivity extends BaseActivity implements IXListViewRefreshListener, IXListViewLoadMore {
    private XListView messageListView;
    private MessageListAdapter messageListAdapter;
    private int pageNow = 1;
    private Mynews mynews;
    private List<ResMynews> resMynewsList = new ArrayList<ResMynews>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        showActionBar(true);
        setActionBarName("消息通知");
        ConsumerReqUtil.updatenews(this, iApiCallback, null, new UpdateNews(), "MessagesActivity", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            if (output instanceof UpdateNews) {
                UpdateNews updateNews = (UpdateNews) output;
                if ("0".equals(updateNews.getRes())) {
                    LLog.i("更新消息状态为已读success:" + updateNews.getErr());
                }
            }
            if (output instanceof Mynews) {
                mynews = (Mynews) output;
                if ("-1".equals(mynews.getRes())) {  //消息列表没有更多  不要加载更多
                    messageListView.disablePullLoad();
                    messageListView.stopRefresh(new Date());
                    EUtil.showToast(mynews.getErr());
                } else { //  0
                    if (input.equals("onRefresh")) {
                        resMynewsList.clear();
                        messageListView.stopRefresh(new Date());
                    } else if (input.equals("onLoadMore")) {
                        messageListView.stopLoadMore();
                    }
                    List<ResMynews> newList = mynews.getData();
                    resMynewsList.addAll(newList);
                    messageListAdapter.reFreshData(resMynewsList);
                    messageListView.setPullLoadEnable(MessagesActivity.this);
                }
            }

        }
    };

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void initView() {
        messageListView = (XListView) findViewById(R.id.messages_listview);
        messageListAdapter = new MessageListAdapter(this, resMynewsList);
        messageListView.setAdapter(messageListAdapter);
        messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        messageListView.setPullRefreshEnable(this); //刷新
        messageListView.startRefresh();
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position - 1;
                if (resMynewsList.get(pos).getType().equals("1")) {
                    startActivity(new Intent(MessagesActivity.this, SellThingActivity.class));
                } else if (resMynewsList.get(pos).getType().equals("11")) {
                    startActivity(new Intent(MessagesActivity.this, FindLabourActivity.class));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        pageNow = 1;
        ConsumerReqUtil.mynews(this, iApiCallback, null, new Mynews(), "onRefresh", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", pageNow).createMap()));
    }

    @Override
    public void onLoadMore() {
        pageNow += 1;
        ConsumerReqUtil.mynews(this, iApiCallback, null, new Mynews(), "onLoadMore", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", pageNow).createMap()));
    }
}
