package com.moxiaosan.both.carowner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.adapter.BusinessInfoAdapter;
import com.moxiaosan.both.common.ui.activity.MessagesActivity;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.RespBusinissInfo;
import consumer.model.Shopcomments;
import consumer.model.UpdateNews;
import consumer.model.obj.RespShop;
import consumer.model.obj.RespShopComment;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XExpandableListView;

/**
 * Created by chris on 16/3/17.
 */
public class BusinessInfoActivity extends BaseActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, IXListViewRefreshListener, IXListViewLoadMore, IApiCallback, View.OnClickListener {

    private XExpandableListView expandableListView;
    private BusinessInfoAdapter businessInfoAdapter;
    private List<RespShop> businessList = new ArrayList<RespShop>();
    private LinearLayout noLayout;
    private int page = 1;

    private EditText etComment;
    private ImageView imgExpression;
    private RelativeLayout commentLayout;
    private TextView tvSend;
    private String commentContent;
    private RespShop businessInfo;
    private TextView tvMessage;

    public static boolean isNeedFresh = false;  //在onResume()中是否需要刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_businessinfo_layout);
        showActionBar(true);
        setActionBarName("商业白板");
        showLoadingDialog();
        reqData(page, "onFirst");
    }

    private void initView() {

        tvMessage = (TextView) findViewById(R.id.messageCount);

        noLayout = (LinearLayout) findViewById(R.id.sell_thing_no_layout);

        etComment = (EditText) findViewById(R.id.sell_thing_comment_edit);
        imgExpression = (ImageView) findViewById(R.id.sell_thing_expression);
        imgExpression.setOnClickListener(this);
        tvSend = (TextView) findViewById(R.id.sell_thing_comment_send);
        tvSend.setOnClickListener(this);
        commentLayout = (RelativeLayout) findViewById(R.id.sell_thing_comment_layout);

        expandableListView = (XExpandableListView) findViewById(R.id.business_thing_expandablelistview);
        businessInfoAdapter = new BusinessInfoAdapter(this, businessList);
        expandableListView.setAdapter(businessInfoAdapter);
        expandableListView.setOnGroupClickListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        expandableListView.setPullRefreshEnable(this);

        businessInfoAdapter.setNotifyCommetListener(new BusinessInfoAdapter.NotifyBusinessInfoCommetListener() {
            @Override
            public void opentCommetEdt(RespShop respShop, int pos) {
                businessInfo = respShop;
                commentLayout.setVisibility(View.VISIBLE);
                etComment.requestFocus();
                Timer timer = new Timer(); //设置定时器
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() { //弹出软键盘的代码

                        InputMethodManager inputManager = (InputMethodManager) etComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(etComment, 0);
                    }
                }, 600); //设置400毫秒的时长
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isNeedFresh) {
            isNeedFresh = false;
            showLoadingDialog();
            reqData(page, "onFirst");
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    @Override
    public void onRefresh() {
        page = 1;
        expandableListView.setPullLoadEnable(this);
        reqData(page, "onRefresh");
    }

    @Override
    public void onData(Object output, Object input) {
        if (output != null) {
            dismissLoadingDialog();
            if (output instanceof RespBusinissInfo) {
                RespBusinissInfo businissInfo = (RespBusinissInfo) output;
                if (businissInfo.getRes().equals("0")) {
                    dismissLoadingDialog();
                    businessList = businissInfo.getData();

                    if (input.equals("onFirst")) {
                        if (businessList.size() == 0) {
                            businessList = businissInfo.getData();
                            initView();
                            noLayout.setVisibility(View.VISIBLE);
                        } else {
                            businessList = businissInfo.getData();
                            initView();
                            expandableListView.setPullLoadEnable(this);
                            noLayout.setVisibility(View.GONE);
                        }

                        if (businissInfo.getNewsnum().equals("0")) {
                            tvMessage.setVisibility(View.GONE);
                        } else {
                            tvMessage.setVisibility(View.VISIBLE);
                            tvMessage.setText(businissInfo.getNewsnum() + "条新消息");
                            tvMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ConsumerReqUtil.updatenews(BusinessInfoActivity.this, BusinessInfoActivity.this, null, new UpdateNews(), "MessagesActivity", true,
                                            StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

                                    startActivity(new Intent(BusinessInfoActivity.this, MessagesActivity.class));
                                }
                            });
                        }

                    } else if (input.equals("onRefresh")) {
                        expandableListView.stopRefresh(new Date());
                        businessList = businissInfo.getData();
                        businessInfoAdapter.refreshData(businessList);

//                        businessInfoAdapter=new BusinessInfoAdapter(this,businessList);
//                        expandableListView.setAdapter(businessInfoAdapter);
                    } else {
                        expandableListView.stopLoadMore();
                        List<RespShop> newList = businissInfo.getData();
                        businessList.addAll(newList);
                        businessInfoAdapter.refreshData(businessList);
                    }

                    //展开
                    for (int i = 0; i < businessInfoAdapter.getGroupCount(); i++) {
                        expandableListView.expandGroup(i);
                    }

                } else {
                    dismissLoadingDialog();
                    expandableListView.disablePullLoad();
                    EUtil.showToast("没有更多了哦~");
                }

            }

            if (output instanceof Shopcomments) {
                Shopcomments shopcomments = (Shopcomments) output;
                EUtil.showToast(shopcomments.getErr());
                for (int i = 0; i < businessList.size(); i++) {
                    if (businessList.get(i).getId().equals(input)) {
                        RespShopComment respShopComment = new RespShopComment();
                        respShopComment.setCommentstext(commentContent);
                        respShopComment.setUsername(AppData.getInstance().getUserEntity().getUsername());
                        businessList.get(i).getComments().add(0, respShopComment);
                        businessInfoAdapter.refreshData(businessList);
                        break;
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

    private void reqData(int page, String input) {

        CarReqUtils.businessList(this, this, null, new RespBusinissInfo(), input, true,
                StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", page).putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sell_thing_expression:

                break;
            case R.id.sell_thing_comment_send:
                if (!TextUtils.isEmpty(etComment.getText().toString().trim())) {
                    commentContent = etComment.getText().toString().trim();
                    etComment.setText("");
                    // 先隐藏键盘
                    ((InputMethodManager) etComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(BusinessInfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    commentLayout.setVisibility(View.GONE);
                    //网络
                    ConsumerReqUtil.shopcomments(this, this, null, new Shopcomments(), businessInfo.getId(), true,
                            StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("shopid", businessInfo.getId())
                                    .putValue("content", commentContent).createMap())
                    );
                    showLoadingDialog();
                } else {
                    etComment.requestFocus();
                    EUtil.showToast("评论内容不能为空");
                }
                break;
        }
    }
}
