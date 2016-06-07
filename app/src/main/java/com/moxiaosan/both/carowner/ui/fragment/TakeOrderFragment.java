package com.moxiaosan.both.carowner.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.adapter.TakeOrderListAdapter;
import com.utils.api.IApiCallback;
import com.utils.common.EUtil;
import com.utils.log.LLog;
import com.utils.ui.base.BaseFragment_v4;

import java.util.Date;
import java.util.List;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.RespOrderList;
import consumer.model.obj.OrderObj;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

/**
 * Created by chris on 16/2/29.
 */
public class TakeOrderFragment extends BaseFragment_v4 implements IXListViewRefreshListener, IXListViewLoadMore, IApiCallback {
    public final static String HAVE_NEW_ORDER = "have_new_order";
    private XListView listView;
    private TakeOrderListAdapter adapter;
    private int page = 1;
    protected HashMapUtils hashMapUtils = null;
    private List<OrderObj> lists;

    private BusinessMainBroadReceiver businessMainBroadReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.b_takeorder_fragment, null);

        listView = (XListView) view.findViewById(R.id.takeorderListId);
        listView.setPullRefreshEnable(this);
        listView.setPullLoadEnable(this);
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hashMapUtils = new HashMapUtils();


//        reqData(page, "onFirst");
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(HAVE_NEW_ORDER);  //有新订单
        businessMainBroadReceiver = new BusinessMainBroadReceiver();
        getActivity().registerReceiver(businessMainBroadReceiver, iFilter);
    }

    public class BusinessMainBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(HAVE_NEW_ORDER)) {
                LLog.i("新订单");
                onRefresh();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        showLoadingDialog();
        page = 1;
        reqData(page, "onFirst");
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
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
            if (output instanceof RespOrderList) {
                RespOrderList respOrderList = (RespOrderList) output;
//                EUtil.showToast(respOrderList.getErr());
                if (respOrderList.getRes() == 0) {
                    dismissLoadingDialog();
                    if (input.equals("onFirst")) {
                        lists = respOrderList.getData();

                        adapter = new TakeOrderListAdapter(getActivity(), lists);
                        listView.setAdapter(adapter);
                    } else if (input.equals("onRefresh")) {
                        listView.stopRefresh(new Date());
                        lists.clear();
                        lists = respOrderList.getData();
                        adapter = new TakeOrderListAdapter(getActivity(), lists);
                        listView.setAdapter(adapter);
                    } else {
                        listView.stopLoadMore();
                        List<OrderObj> newList = respOrderList.getData();
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
        SharedPreferences sp = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(sp.getString("city", ""))) {
            CarReqUtils.orderList(getActivity(), this, null, new RespOrderList(), input, true,
                    StringUrlUtils.geturl(hashMapUtils.putValue("pageNow", page).putValue("city", sp.getString("city", "")).createMap()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(businessMainBroadReceiver);
    }
}
