package com.moxiaosan.both.carowner.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.ReceiveOrderOkActivity;
import com.moxiaosan.both.carowner.ui.adapter.OrderListAdapter;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseFragment_v4;

import java.util.Date;
import java.util.List;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.MyOrderList;
import consumer.model.obj.MyOrderObj;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

/**
 * Created by chris on 16/2/29.
 */
public class OrderFragment extends BaseFragment_v4 implements AdapterView.OnItemClickListener, IXListViewRefreshListener, IXListViewLoadMore, IApiCallback {

    private XListView listView;
    private OrderListAdapter adapter;
    private int page = 1;
    private List<MyOrderObj> lists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.b_order_fragment, null);

        listView = (XListView) view.findViewById(R.id.orderListId);

//        listView.startRefresh();
        listView.setPullRefreshEnable(this);
        listView.setPullLoadEnable(this);
        listView.setOnItemClickListener(this);
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        showLoadingDialog();
//        reqData(page,"onFirst");

    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        showLoadingDialog();
        reqData(page, "onFirst");
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (("进行中").equals(lists.get(position - 1).getStatus())) {
            Intent intent = new Intent(getActivity(), ReceiveOrderOkActivity.class);
            intent.putExtra("name", lists.get(position - 1).getTitle());
            intent.putExtra("orderId", lists.get(position - 1).getOrderid());
            startActivity(intent);
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

    @Override
    public void onData(Object output, Object input) {
        if (output != null) {
            if (output instanceof MyOrderList) {
                MyOrderList respOrderList = (MyOrderList) output;
//                EUtil.showToast(respOrderList.getErr());
                if (respOrderList.getRes().equals("0")) {
                    dismissLoadingDialog();
                    if (input.equals("onFirst")) {
                        lists = respOrderList.getData();

                        adapter = new OrderListAdapter(getActivity(), lists);
                        listView.setAdapter(adapter);
                    } else if (input.equals("onRefresh")) {
                        listView.stopRefresh(new Date());
                        lists.clear();
                        lists = respOrderList.getData();
                        adapter = new OrderListAdapter(getActivity(), lists);
                        listView.setAdapter(adapter);
                    } else {
                        listView.stopLoadMore();
                        List<MyOrderObj> newList = respOrderList.getData();
                        lists.addAll(newList);
                        adapter.notifyDataSetChanged();

                    }

                } else {
                    dismissLoadingDialog();
                    listView.disablePullLoad();
                    EUtil.showToast("没有更多了哦~");
                }

            }
        } else {
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }
    }

    private void reqData(int page, String input) {

        CarReqUtils.myOrderList(getActivity(), this, null, new MyOrderList(), input, true,
                StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", page).
                        putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

    }
}
