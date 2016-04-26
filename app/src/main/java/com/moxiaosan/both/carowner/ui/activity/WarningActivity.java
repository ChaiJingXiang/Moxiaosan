package com.moxiaosan.both.carowner.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.adapter.WarningListAdapter;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.List;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.RespAlarmList;
import consumer.model.obj.AlarmObj;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

/**
 * Created by chris on 16/4/21.
 */
public class WarningActivity extends BaseActivity implements IApiCallback,AdapterView.OnItemClickListener{
    private  XListView listView;
    private WarningListAdapter adapter;
    private List<AlarmObj> lists;
    private TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBar(true);
        setActionBarName("警情列表");

        setContentView(R.layout.b_warning_layout);

        listView =(XListView)findViewById(R.id.w_listViewId);
        tvNoData =(TextView)findViewById(R.id.noData);

//        listView.setPullRefreshEnable(this);
//        listView.setPullLoadEnable(this);
        listView.setOnItemClickListener(this);

        showLoadingDialog();
        CarReqUtils.alarmlist(this,this,null,new RespAlarmList(),"alarmlist",true, StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {

        if(output !=null){
            dismissLoadingDialog();
            if(output instanceof RespAlarmList){
                RespAlarmList alarmList =(RespAlarmList)output;
                if(alarmList.getRes().equals("0")){

                    lists =alarmList.getData();

                    if(lists.size()==0){

                        tvNoData.setVisibility(View.VISIBLE);
                    }else{

                        adapter =new WarningListAdapter(this,lists);

                        listView.setAdapter(adapter);
                    }

                }

            }

        }else{
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        position =position-1;

        Intent intent =new Intent(this,AlarmLocationActivity.class);
        intent.putExtra("name",lists.get(position).getName());
        intent.putExtra("speed",lists.get(position).getSpeed());
        intent.putExtra("time",lists.get(position).getDatetime());
        intent.putExtra("address",lists.get(position).getAddress());
        intent.putExtra("lat",lists.get(position).getLat());
        intent.putExtra("lng",lists.get(position).getLng());
        startActivity(intent);

    }
}
