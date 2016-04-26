package com.moxiaosan.both.common.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.moxiaosan.both.R;
import com.moxiaosan.both.common.model.LocationBean;
import com.moxiaosan.both.common.model.MyPoiInfo;
import com.moxiaosan.both.common.ui.adapter.SelectFromListAdapter;
import com.moxiaosan.both.common.utils.BaiduMapUtil;
import com.moxiaosan.both.common.utils.CommonHelper;
import com.utils.common.EUtil;
import com.utils.log.LLog;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

//从哪里出发
public class SelectFromAddressActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private SelectFromListAdapter selectFromListAdapter;
    private List<MyPoiInfo> poiInfoList;   // 定位poi地名信息数据源
    private LocationClient locClient;  // 定位相关
    private EditText etFromAddress;
    private MyPoiInfo selectPoiInfo;
    private LocationBean selectLocationBean;
    private SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_address);
        sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        initView();
    }

    @Override
    public void onResume() {
        doLocation();
        if (locClient != null) {
            locClient.start();
        }
        super.onResume();
    }

    private void initView() {
        findViewById(R.id.select_from_address_back_btn).setOnClickListener(this);
//        findViewById(R.id.select_from_address_ensure).setOnClickListener(this);
        etFromAddress = (EditText) findViewById(R.id.select_from_address_edit);
        etFromAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(sp.getString("city", ""))) {
                    LLog.i("==city===" + sp.getString("city", ""));
                    BaiduMapUtil.getSuggestion(sp.getString("city", ""), s.toString(), new BaiduMapUtil.SuggestionsGetListener() {

                        @Override
                        public void onGetSucceed(List<MyPoiInfo> searchPoiList) {
                            if (poiInfoList == null) {
                                poiInfoList = new ArrayList<MyPoiInfo>();
                            }
                            poiInfoList.clear();
                            if (searchPoiList != null) {
                                poiInfoList = searchPoiList;
                            } else {
                                EUtil.showToast("抱歉，获取位置信息失败");
                            }
                            updateAroundPositionList(searchPoiList, -1);
                        }

                        @Override
                        public void onGetFailed() {
                            EUtil.showToast("抱歉，未能找到结果");
                        }
                    });
                }
            }
        });
        listView = (ListView) findViewById(R.id.select_from_address_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateAroundPositionList(poiInfoList, position);
                selectPoiInfo = poiInfoList.get(position);
                LocationBean locationBean = new LocationBean();
                locationBean.setCity(poiInfoList.get(position).getCity());
                selectLocationBean = locationBean;
                doCallBack(selectPoiInfo);
            }
        });
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_from_address_back_btn:
                finish();
                break;
        }
    }

    // 根据经纬度获取周边热点名
    private void getPositionList(LatLng ll) {
        showLoadingDialog();

        BaiduMapUtil.getPoisByGeoCode(ll.latitude, ll.longitude,
                new BaiduMapUtil.GeoCodePoiListener() {

                    @Override
                    public void onGetSucceed(LocationBean locationBean, List<MyPoiInfo> poiList) {
                        dismissLoadingDialog();

                        if (poiInfoList == null) {
                            poiInfoList = new ArrayList<MyPoiInfo>();
                        }
                        poiInfoList.clear();
                        if (poiList != null) {
                            poiInfoList = poiList;
                        } else {
                            EUtil.showToast("抱歉，获取位置信息失败");
                        }
                        selectLocationBean = locationBean;
                        updateAroundPositionList(poiInfoList, -1);
                    }

                    @Override
                    public void onGetFailed() {
                        dismissLoadingDialog();
                        EUtil.showToast("抱歉，未能找到结果");
                    }
                });

    }

    // 更新周围地点列表的adapter
    private void updateAroundPositionList(List<MyPoiInfo> list, int index) {
        if (selectFromListAdapter == null) {
            selectFromListAdapter = new SelectFromListAdapter(this, list);
            listView.setAdapter(selectFromListAdapter);
        } else {
            selectFromListAdapter.refreshListData(this, list, index);
        }
    }

    /**
     * 监听函数，有新位置的时候输出到屏幕中
     */
    class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            dismissLoadingDialog();
            if (location == null) {
                return;
            }
            getPositionList(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void doLocation() {
        if (!CommonHelper.isNetworkAvailable(SelectFromAddressActivity.this)) {
            EUtil.showToast("网络不可用,无法获取地址！");
        } else {
            if (CommonHelper.isOPen(this)) {
                showLoadingDialog();
                if (locClient == null) {
                    locClient = new LocationClient(this);
                }
                locClient.registerLocationListener(new MyLocationListenner());
                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true);// 打开gps
                // option.setCoorType("bd09ll"); //设置坐标类型
                option.setCoorType("gcj02");
                // option.setScanSpan(30000);
                option.setAddrType("all");
                locClient.setLocOption(option);
            } else {
                if (poiInfoList == null) {
                    poiInfoList = new ArrayList<MyPoiInfo>();
                }
                //aroundPoiList.add(0, "不显示位置");
                updateAroundPositionList(poiInfoList, -1);

                new AlertDialog.Builder(this)
                        .setMessage("无法确认位置,需要开启定位吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        CommonHelper.openGPS(SelectFromAddressActivity.this);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .create().show();
            }
        }
    }

    private void doCallBack(MyPoiInfo myPoiInfo) {
        Intent intent = this.getIntent();
        intent.putExtra("fromCity", selectLocationBean.getCity());
        intent.putExtra("fromPosition", myPoiInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPause() {
        if (locClient != null) {
            locClient.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (locClient != null) {
            locClient.stop();
        }
        super.onDestroy();
    }
}
