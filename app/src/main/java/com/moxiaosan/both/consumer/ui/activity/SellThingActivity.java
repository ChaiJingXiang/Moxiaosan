package com.moxiaosan.both.consumer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.adapter.SellThingListAdapter;
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
import consumer.api.ConsumerReqUtil;
import consumer.model.ShopList;
import consumer.model.Shopcomments;
import consumer.model.obj.RespShop;
import consumer.model.obj.RespShopComment;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XExpandableListView;

public class SellThingActivity extends BaseActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, IXListViewRefreshListener, IXListViewLoadMore, View.OnClickListener {
    private XExpandableListView expandableListView;
    private SellThingListAdapter sellThingListAdapter;
    private List<RespShop> respShops = new ArrayList<RespShop>();
    private ShopList shopList;
    private LinearLayout noLayout;
    private int pageNum = 1;

    private EditText etComment;
    private ImageView imgExpression;
    private RelativeLayout commentLayout;
    private TextView tvSend;
    private RespShop respShop;
    private String commentContent;
    public static boolean isNeedRefresh = false; //控制是否刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_thing);
        showActionBar(true);
        setActionBarName("买卖东西");
        getActionBarRightImage(R.mipmap.add_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellThingActivity.this, PublishThingActivity.class));
            }
        });
        initView();
        requestData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNeedRefresh) {
            isNeedRefresh = false;
            requestData();
        }
    }

    /**
     * 请求数据
     */
    private void requestData() {
        pageNum = 1;
        ConsumerReqUtil.shoplist(this, iApiCallback, null, new ShopList(), "onRefresh", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", pageNum).createMap()));
        showLoadingDialog();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void initView() {
        noLayout = (LinearLayout) findViewById(R.id.sell_thing_no_layout);

        etComment = (EditText) findViewById(R.id.sell_thing_comment_edit);
        imgExpression = (ImageView) findViewById(R.id.sell_thing_expression);
        imgExpression.setOnClickListener(this);
        tvSend = (TextView) findViewById(R.id.sell_thing_comment_send);
        tvSend.setOnClickListener(this);
        commentLayout = (RelativeLayout) findViewById(R.id.sell_thing_comment_layout);

        expandableListView = (XExpandableListView) findViewById(R.id.sell_thing_expandablelistview);
        sellThingListAdapter = new SellThingListAdapter(this, respShops);
        expandableListView.setAdapter(sellThingListAdapter);
        expandableListView.setOnGroupClickListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        expandableListView.setPullRefreshEnable(this);
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    hideSoftInput(getWindow().peekDecorView());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        sellThingListAdapter.setNotifyCommetListener(new SellThingListAdapter.NotifySellThingCommetListener() {
            @Override
            public void opentCommetEdt(RespShop respSho, int pos) {
                respShop = respSho;
                commentLayout.setVisibility(View.VISIBLE);
                etComment.requestFocus();
                Timer timer = new Timer(); //设置定时器
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() { //弹出软键盘的代码
                        showSoftInput(etComment);
                    }
                }, 600); //设置400毫秒的时长
            }
        });
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return true;
    }

    private void showSoftInput(View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    private void hideSoftInput(View v) {
        // 先隐藏键盘
        ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(SellThingActivity.this.getCurrentFocus().getWindowToken(), 0);
        commentLayout.setVisibility(View.GONE);
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
            if (output instanceof ShopList) {
                shopList = (ShopList) output;

                if ("-1".equals(shopList.getRes())) {  //消息列表没有更多  不要加载更多
                    expandableListView.disablePullLoad();
                    EUtil.showToast("没有更多了哦~");
                } else {
                    if (input.equals("onRefresh")) {
                        respShops.clear();
                        expandableListView.stopRefresh(new Date());
                    } else if (input.equals("onLoadMore")) {
                        expandableListView.stopLoadMore();
                    }
                    List<RespShop> newList = shopList.getData();
                    if (newList.size() == 0) {
                        noLayout.setVisibility(View.VISIBLE);
                    } else {
                        noLayout.setVisibility(View.INVISIBLE);
                    }
                    respShops.addAll(newList);
                    sellThingListAdapter.refreshData(respShops);
                    //展开
                    for (int i = 0; i < sellThingListAdapter.getGroupCount(); i++) {
                        expandableListView.expandGroup(i);
                    }
                    expandableListView.setPullLoadEnable(SellThingActivity.this);
                }
            }

            if (output instanceof Shopcomments) {
                Shopcomments shopcomments = (Shopcomments) output;
                EUtil.showToast(shopcomments.getErr());
                for (int i = 0; i < respShops.size(); i++) {
                    if (respShops.get(i).getId().equals(input)) {
                        RespShopComment respShopComment = new RespShopComment();
                        respShopComment.setCommentstext(commentContent);
                        respShopComment.setUsername(AppData.getInstance().getUserEntity().getUsername());
                        respShops.get(i).getComments().add(0, respShopComment);
                        sellThingListAdapter.refreshData(respShops);
                        break;
                    }
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        pageNum = 1;
        ConsumerReqUtil.shoplist(this, iApiCallback, null, new ShopList(), "onRefresh", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", pageNum).createMap()));
    }

    @Override
    public void onLoadMore() {
        pageNum += 1;
        ConsumerReqUtil.shoplist(this, iApiCallback, null, new ShopList(), "onLoadMore", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", pageNum).createMap()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (commentLayout.isShown()) {
                commentLayout.setVisibility(View.GONE);
                hideSoftInput(etComment);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
                    hideSoftInput(etComment);
                    //网络
                    ConsumerReqUtil.shopcomments(this, iApiCallback, null, new Shopcomments(), respShop.getId(), true,
                            StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("shopid", respShop.getId())
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
