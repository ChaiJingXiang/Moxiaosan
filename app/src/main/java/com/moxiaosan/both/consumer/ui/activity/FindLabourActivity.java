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
import com.moxiaosan.both.consumer.ui.adapter.FindLabourListAdapter;
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
import consumer.model.LabourList;
import consumer.model.Labourcomments;
import consumer.model.obj.RespLabour;
import consumer.model.obj.RespLabourComment;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XExpandableListView;

public class FindLabourActivity extends BaseActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, IXListViewRefreshListener, IXListViewLoadMore, View.OnClickListener {
    private XExpandableListView expandableListView;
    private FindLabourListAdapter findLabourListAdapter;
    private List<RespLabour> respLabourList = new ArrayList<RespLabour>();
    private LinearLayout noLayout;
    private int pageNum = 1;
    private LabourList labourList;
    private EditText etComment;
    private ImageView imgExpression;
    private RelativeLayout commentLayout;
    private TextView tvSend;
    private RespLabour respLabour;
    private String commentContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_labour);
        showActionBar(true);
        setActionBarName(getString(R.string.find_labour));
        getActionBarRightImage(R.mipmap.add_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindLabourActivity.this, PublishLabourActivity.class));
            }
        });
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNum = 1;
        ConsumerReqUtil.mylabourlist(this, iApiCallback, null, new LabourList(), "onRefresh", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", pageNum).putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));
        showLoadingDialog();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void initView() {
        noLayout = (LinearLayout) findViewById(R.id.find_labour_no_layout);
        etComment = (EditText) findViewById(R.id.find_labour_comment_edit);
        imgExpression = (ImageView) findViewById(R.id.find_labour_expression);
        imgExpression.setOnClickListener(this);
        tvSend = (TextView) findViewById(R.id.find_labour_comment_send);
        tvSend.setOnClickListener(this);
        commentLayout = (RelativeLayout) findViewById(R.id.find_labour_comment_layout);
        expandableListView = (XExpandableListView) findViewById(R.id.find_labour_expandablelistview);
        findLabourListAdapter = new FindLabourListAdapter(this, respLabourList);
        expandableListView.setAdapter(findLabourListAdapter);
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
        findLabourListAdapter.setNotifyCommetListener(new FindLabourListAdapter.NotifyCommetListener() {
            @Override
            public void opentCommetEdt(RespLabour respLabou, int pos) {
                respLabour = respLabou;
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

    private void showSoftInput(View v){
        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    private void hideSoftInput(View v){
        // 先隐藏键盘
        ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(FindLabourActivity.this.getCurrentFocus().getWindowToken(), 0);
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
            if (output instanceof LabourList) {
                labourList = (LabourList) output;

                if ("-1".equals(labourList.getRes())) {  //消息列表没有更多  不要加载更多
                    expandableListView.disablePullLoad();
                    EUtil.showToast("没有更多了哦~");
                } else {
                    if (input.equals("onRefresh")) {
                        respLabourList.clear();
                        expandableListView.stopRefresh(new Date());
                    } else if (input.equals("onLoadMore")) {
                        expandableListView.stopLoadMore();
                    }
                    List<RespLabour> newList = labourList.getData();
                    if (newList.size() == 0) {
                        noLayout.setVisibility(View.VISIBLE);
                    } else {
                        noLayout.setVisibility(View.INVISIBLE);
                    }
                    respLabourList.addAll(newList);
                    findLabourListAdapter.refreshData(respLabourList);
                    //展开
                    for (int i = 0; i < findLabourListAdapter.getGroupCount(); i++) {
                        expandableListView.expandGroup(i);
                    }
                    expandableListView.setPullLoadEnable(FindLabourActivity.this);
                }
            }
            if (output instanceof Labourcomments) {
                Labourcomments labourcomments = (Labourcomments) output;
                EUtil.showToast(labourcomments.getErr());
                for (int i = 0; i < respLabourList.size(); i++) {
                    if (respLabourList.get(i).getId().equals(input)) {
                        RespLabourComment respLabourComment = new RespLabourComment();
                        respLabourComment.setContent(commentContent);
                        respLabourComment.setUsername(AppData.getInstance().getUserEntity().getUsername());
                        respLabourList.get(i).getComments().add(0,respLabourComment);
                        findLabourListAdapter.refreshData(respLabourList);
                        break;
                    }
                }
            }
        }
    };

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return true;
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        ConsumerReqUtil.mylabourlist(this, iApiCallback, null, new LabourList(), "onRefresh", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", pageNum).createMap()));
    }

    @Override
    public void onLoadMore() {
        pageNum += 1;
        ConsumerReqUtil.mylabourlist(this, iApiCallback, null, new LabourList(), "onLoadMore", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", pageNum).createMap()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (commentLayout.isShown()){
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
            case R.id.find_labour_expression:

                break;
            case R.id.find_labour_comment_send:
                if (!TextUtils.isEmpty(etComment.getText().toString().trim())) {
                    commentContent = etComment.getText().toString().trim();
                    etComment.setText("");
                    hideSoftInput(etComment);

                    //网络
                    ConsumerReqUtil.labourcomments(this, iApiCallback, null, new Labourcomments(), respLabour.getId(), true,
                            StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("labourid", respLabour.getId())
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
