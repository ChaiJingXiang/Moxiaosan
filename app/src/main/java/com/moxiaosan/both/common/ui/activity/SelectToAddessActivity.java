package com.moxiaosan.both.common.ui.activity;

import android.content.Context;
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

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.model.MyPoiInfo;
import com.moxiaosan.both.common.ui.adapter.SelectToListAdapter;
import com.moxiaosan.both.common.utils.BaiduMapUtil;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

//去哪里
public class SelectToAddessActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private EditText etToAddress;
    private SelectToListAdapter selectToListAdapter;
    private List<MyPoiInfo> myPoiInfoList;
    private SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_to_addess);
        sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.select_to_address_listview);
        findViewById(R.id.select_to_address_back_btn).setOnClickListener(this);
        etToAddress = (EditText) findViewById(R.id.select_to_address_edit);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateAroundPositionList(myPoiInfoList, position);
                doCallBack(myPoiInfoList.get(position));
                finish();
            }
        });
        etToAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                LLog.i("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LLog.i("onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(sp.getString("city", ""))) {
                    BaiduMapUtil.getSuggestion(sp.getString("city", ""), s.toString(), new BaiduMapUtil.SuggestionsGetListener() {
                        @Override
                        public void onGetSucceed(List<MyPoiInfo> searchPoiList) {
                            if (myPoiInfoList == null) {
                                myPoiInfoList = new ArrayList<MyPoiInfo>();
                            }
                            myPoiInfoList.clear();
                            if (searchPoiList != null) {
                                myPoiInfoList = searchPoiList;
                            } else {
                                EUtil.showToast("抱歉，获取位置信息失败");
                            }
                            updateAroundPositionList(myPoiInfoList, -1);
                        }

                        @Override
                        public void onGetFailed() {
                            EUtil.showToast("抱歉，未能找到结果");
                        }
                    });
                }
            }
        });

    }

    // 更新周围地点列表的adapter
    private void updateAroundPositionList(List<MyPoiInfo> list, int index) {
        if (selectToListAdapter == null) {
            selectToListAdapter = new SelectToListAdapter(this, list);
            listView.setAdapter(selectToListAdapter);
        } else {
            selectToListAdapter.refreshListData(this, list, index);
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_to_address_back_btn:
                finish();
                break;
        }
    }

    private void doCallBack(MyPoiInfo myPoiInfo) {
        Intent intent = this.getIntent();
        intent.putExtra("toPosition", myPoiInfo);
        setResult(RESULT_OK, intent);
        finish();
    }
}
