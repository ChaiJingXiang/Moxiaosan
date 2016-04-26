package com.moxiaosan.both.consumer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.adapter.NoPayOrderListAdapter;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Userorderlist;
import consumer.model.obj.RespUserOrder;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

/**
 * Created by qiangfeng on 16/3/1.
 */
public class NoPayOrderFragment extends Fragment implements IXListViewRefreshListener, IXListViewLoadMore {
    private XListView noPayListview;
    private NoPayOrderListAdapter noPayOrderListAdapter;
    private List<RespUserOrder> respUserOrderList=new ArrayList<RespUserOrder>();
    private int pageNow = 1;
    private Userorderlist userorderlist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noPayOrderListAdapter=new NoPayOrderListAdapter(getActivity(),respUserOrderList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_no_pay_order,container,false);
        noPayListview= (XListView) view.findViewById(R.id.no_pay_order_listview);
        noPayListview.setAdapter(noPayOrderListAdapter);
        noPayListview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        noPayListview.setPullRefreshEnable(this); //刷新
        noPayListview.startRefresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNow = 1;
        ConsumerReqUtil.userorderlist(getActivity(), iApiCallback, null, new Userorderlist(), "onRefresh", true,
                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("type", 1).putValue("pageNow", pageNow).createMap()));
    }

    @Override
    public void onRefresh() {
        pageNow = 1;
        ConsumerReqUtil.userorderlist(getActivity(), iApiCallback, null, new Userorderlist(), "onRefresh", true,
                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("type", 1).putValue("pageNow", pageNow).createMap()));
    }

    @Override
    public void onLoadMore() {
        pageNow += 1;
        ConsumerReqUtil.userorderlist(getActivity(),iApiCallback,null,new Userorderlist(),"onLoadMore",true,
                StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("type",1).putValue("pageNow",pageNow).createMap()));
    }

    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (output == null) {
                return;
            }
            if (output instanceof Userorderlist) {
                userorderlist = (Userorderlist) output;
                if ("-1".equals(userorderlist.getRes())) {  //消息列表没有更多  不要加载更多
                    noPayListview.disablePullLoad();
                    noPayListview.stopRefresh(new Date());
                    EUtil.showToast(userorderlist.getErr());
                } else { //  0
                    if (input.equals("onRefresh")) {
                        respUserOrderList.clear();
                        noPayListview.stopRefresh(new Date());
                    } else if (input.equals("onLoadMore")) {
                        noPayListview.stopLoadMore();
                    }
                    List<RespUserOrder> newList = userorderlist.getData();
                    respUserOrderList.addAll(newList);
                    noPayOrderListAdapter.reFreshData(respUserOrderList);
                    noPayListview.setPullLoadEnable(NoPayOrderFragment.this);
                }
            }
        }
    };


}
