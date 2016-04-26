package com.moxiaosan.both.common.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.moxiaosan.both.APP;
import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.customView.sortlistview.CharacterParser;
import com.moxiaosan.both.common.ui.customView.sortlistview.PinyinComparator;
import com.moxiaosan.both.common.ui.customView.sortlistview.SideBar;
import com.moxiaosan.both.common.ui.customView.sortlistview.SortAdapter;
import com.moxiaosan.both.common.ui.customView.sortlistview.SortModel;
import com.utils.api.IApiCallback;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import consumer.api.UserReqUtil;
import consumer.model.Getcityname;
import consumer.model.obj.RespCityName;

public class CityPositionActivity extends BaseActivity {
    private ListView listView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private TextView tvCityName, tvCityState;
    private BroadcastReceiver broadcastReceiver;
    public final static String LOCATION_BCR = "location_bcr";
    private String latitude = "";  //定位定到的纬度
    private String longitude = "";  //定位定到的经度

    private List<RespCityName> respCityNameList;
    private Getcityname getcityname=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_position);
        showActionBar(true);
        setActionBarName("城市定位");

        initView();
        initSideBarView();
        getcityname= (Getcityname) UserReqUtil.getcityname(this, iApiCallback, null, new Getcityname(), "CityPositionActivity", false);
        if (getcityname!=null){
            if (getcityname.getData().size()>0){//有缓存
                respCityNameList = getcityname.getData();
                List<String> nameStrList = new ArrayList<String>();
                for (RespCityName respCityName : respCityNameList) {
                    nameStrList.add(respCityName.getCityname());
                }
                SourceDateList = filledData(nameStrList.toArray(new String[nameStrList.size()]));
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortAdapter(CityPositionActivity.this, SourceDateList);
                listView.setAdapter(adapter);
            }
        }else {
            UserReqUtil.getcityname(this, iApiCallback, null, new Getcityname(), "CityPositionActivity", true);
            showLoadingDialog();
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
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
            if (output instanceof Getcityname) {
                getcityname = (Getcityname) output;
                respCityNameList = getcityname.getData();
                List<String> nameStrList = new ArrayList<String>();
                for (RespCityName respCityName : respCityNameList) {
                    nameStrList.add(respCityName.getCityname());
                }
//            SourceDateList = filledData(getResources().getStringArray(R.array.name));
                SourceDateList = filledData(nameStrList.toArray(new String[nameStrList.size()]));

                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortAdapter(CityPositionActivity.this, SourceDateList);
                listView.setAdapter(adapter);
            }
        }
    };

    private void initView() {
        tvCityName = (TextView) findViewById(R.id.city_position_city_name);
        tvCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("city", tvCityName.getText().toString());
                intent.putExtra("lng", longitude);
                intent.putExtra("lat", latitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tvCityState = (TextView) findViewById(R.id.city_position_city_name_state);

        tvCityState.setText("定位中。。。");
        registerBroadCastReceiver();
        APP.getInstance().requestLocationInfo();
    }

    private void initSideBarView() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.friend_frag_sidebar);
        dialog = (TextView) findViewById(R.id.friend_frag_text_dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (adapter != null) {
                    //该字母首次出现的位置
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        listView.setSelection(position);
                    }
                }
            }
        });

        listView = (ListView) findViewById(R.id.friend_frag_contact_book_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                tvCityName.setText(((SortModel) adapter.getItem(position)).getName());
                Intent intent = getIntent();
                intent.putExtra("city", ((SortModel) adapter.getItem(position)).getName());
                intent.putExtra("lng", 100.0);
                intent.putExtra("lat", 80.0);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 注册一个广播，监听定位结果
     */
    private void registerBroadCastReceiver() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                latitude = intent.getStringExtra("latitude");
                longitude = intent.getStringExtra("longitude");
                String city = intent.getStringExtra("address");
                tvCityState.setText("GPS定位");
                tvCityName.setText(city);
//                Log.i("info===========+++++", city + "," + latitude + "," + longitude);
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(LOCATION_BCR);
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
//    private void filterData(String filterStr){
//        List<SortModel> filterDateList = new ArrayList<SortModel>();
//
//        if(TextUtils.isEmpty(filterStr)){
//            filterDateList = SourceDateList;
//        }else{
//            filterDateList.clear();
//            for(SortModel sortModel : SourceDateList){
//                String name = sortModel.getName();
//                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
//                    filterDateList.add(sortModel);
//                }
//            }
//        }
//
//        // 根据a-z进行排序
//        Collections.sort(filterDateList, pinyinComparator);
//        adapter.updateListView(filterDateList);
//    }
}
