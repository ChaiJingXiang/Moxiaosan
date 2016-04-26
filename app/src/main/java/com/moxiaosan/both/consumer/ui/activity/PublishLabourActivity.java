package com.moxiaosan.both.consumer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moxiaosan.both.R;
import com.moxiaosan.both.common.utils.DateTimePickDialogUtil;
import com.moxiaosan.both.consumer.ui.adapter.SelectPlaceAdapter;
import com.moxiaosan.both.consumer.utils.CitycodeUtil;
import com.moxiaosan.both.consumer.utils.Cityinfo;
import com.moxiaosan.both.consumer.utils.FileUtil;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Labour;
import consumer.model.Modiylabour;
import consumer.model.obj.RespLabour;

public class PublishLabourActivity extends BaseActivity {
    private EditText etTitle, etNum, etTime, etSalary, etTechnique, etAddress;
    private String title="";
    private RespLabour respLabour=null;

    private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    private SelectPlaceAdapter provinceAdapter = null;  //省级适配器
    private SelectPlaceAdapter cityAdapter = null;    //地级适配器
    private SelectPlaceAdapter countyAdapter = null;    //县级适配器

    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();
    private CitycodeUtil citycodeUtil;

    private LocationClient locClient;  // 定位相关
    private BaiduSDKReceiver baiduReceiver;

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class BaiduSDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                EUtil.showToast("网络异常！");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_labour);
        showActionBar(true);
        title=getIntent().getStringExtra("title");
        respLabour= (RespLabour) getIntent().getSerializableExtra("respLabour");
        if (TextUtils.isEmpty(title)){
            setActionBarName("发布劳力");
        }else {
            setActionBarName(title);
        }
        citycodeUtil = CitycodeUtil.getSingleton();
        getaddressinfo();

        initView();

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        baiduReceiver = new BaiduSDKReceiver();
        registerReceiver(baiduReceiver, iFilter);

        showMapWithLocationClient();
        if (locClient != null) {
            locClient.start();  //开始定位自己
            showLoadingDialog();
        }
    }

    private void showMapWithLocationClient() {
        locClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("gcj02"); //设置坐标类型//bd09ll  //gcj02
        option.setScanSpan(30000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedAltitude(true);
        option.setNeedDeviceDirect(true);
        option.setAddrType("all");
        locClient.setLocOption(option);
        locClient.registerLocationListener(new MyMyLocationListenner());
    }

    private class MyMyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (isLoadingDialogShowing()) {
                dismissLoadingDialog();
            }
            if (bdLocation == null) {
                return;
            }
            setSpinner(bdLocation);
            if (locClient != null) {
                locClient.stop();
            }
        }
    }

    private void initView() {
        etTitle = (EditText) findViewById(R.id.publish_labour_title);
        etNum = (EditText) findViewById(R.id.publish_labour_num);
        etTime = (EditText) findViewById(R.id.publish_labour_time);
        etTime.requestFocus();
        etTime.setInputType(InputType.TYPE_NULL);

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date=new Date();
                DateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                String time=format.format(date);

                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(PublishLabourActivity.this, time);
                dateTimePicKDialog.dateTimePicKDialog(etTime);
            }
        });
        etSalary = (EditText) findViewById(R.id.publish_labour_salary);
        etTechnique = (EditText) findViewById(R.id.publish_labour_technique);
        etAddress = (EditText) findViewById(R.id.publish_labour_address);
        if (respLabour != null){
            etTitle.setText(respLabour.getTitle());
            etNum.setText(respLabour.getNums());
            etTime.setText(respLabour.getFb_datetime());
            etSalary.setText(respLabour.getSalary());
            etAddress.setText(respLabour.getAddress());
            etTechnique.setText(respLabour.getTechnique());
        }
        findViewById(R.id.publish_labour_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etTitle.getText().toString())) {
                    if (!TextUtils.isEmpty(etNum.getText().toString())) {
                        if (!TextUtils.isEmpty(etTime.getText().toString())) {
                            if (!TextUtils.isEmpty(etSalary.getText().toString())) {
                                if (!TextUtils.isEmpty(etTechnique.getText().toString())) {
                                    //把时间格式转化一下
                                    String time = "";
                                    DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                                    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    try {
                                        Date date2 = format.parse(etTime.getText().toString());
                                        time = format2.format(date2);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (respLabour != null) {  //修改 劳力
                                        ConsumerReqUtil.modiylabour(PublishLabourActivity.this, iApiCallback, null, new Labour(), "PublishLabourActivity", true,
                                                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("labourid", respLabour.getId()).putValue("title", etTitle.getText().toString())
                                                        .putValue("salary", etSalary.getText().toString()).putValue("nums", etNum.getText().toString()).putValue("technique", etTechnique.getText().toString())
                                                        .putValue("address", provinceSpinner.getSelectedItem().toString() + citySpinner.getSelectedItem().toString() + countySpinner.getSelectedItem().toString()
                                                                + etAddress.getText().toString()).putValue("datetime", time).createMap()));
                                    } else {  //发布劳力
                                        ConsumerReqUtil.labour(PublishLabourActivity.this, iApiCallback, null, new Labour(), "PublishLabourActivity", true,
                                                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("title", etTitle.getText().toString())
                                                        .putValue("salary", etSalary.getText().toString()).putValue("nums", etNum.getText().toString()).putValue("technique", etTechnique.getText().toString())
                                                        .putValue("address", provinceSpinner.getSelectedItem().toString()+citySpinner.getSelectedItem().toString()+countySpinner.getSelectedItem().toString()
                                                                +etAddress.getText().toString()).putValue("datetime", time).createMap()));
                                    }
                                    showLoadingDialog();
                                } else {
                                    EUtil.showToast("技能要求不能为空");
                                }
                            } else {
                                EUtil.showToast("薪资不能为空");
                            }
                        } else {
                            EUtil.showToast("时间不能为空");
                        }
                    } else {
                        EUtil.showToast("人数不能为空");
                    }
                } else {
                    EUtil.showToast("名称不能为空");
                }
            }
        });
    }

    /*
    * 设置下拉框
    */
    private void setSpinner(BDLocation bdLocation) {
        int provinIndex = 5;
        int cityIndex = 0;
        int countyIndex = 0;

        provinceSpinner = (Spinner) findViewById(R.id.spin_province);
        citySpinner = (Spinner) findViewById(R.id.spin_city);
        countySpinner = (Spinner) findViewById(R.id.spin_county);

        //绑定适配器和值
        provinceAdapter = new SelectPlaceAdapter(PublishLabourActivity.this, citycodeUtil.getProvince(province_list));
        provinceSpinner.setAdapter(provinceAdapter);
        for (int i = 0; i < province_list.size(); i++) {
            if (province_list.get(i).getCity_name().equals(bdLocation.getAddress().province)) {
                provinIndex = i;
                break;
            } else {
                provinIndex = 0;
            }
        }
        provinceSpinner.setSelection(provinIndex, true);  //设置默认选中项，此处为默认选中第5个值

        cityAdapter = new SelectPlaceAdapter(PublishLabourActivity.this, citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(provinIndex)));
        citySpinner.setAdapter(cityAdapter);
        for (int i = 0; i < citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(provinIndex)).size(); i++) {
            if (citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(provinIndex)).get(i).equals(bdLocation.getAddress().city)) {
                cityIndex = i;
                break;
            } else {
                cityIndex = 0;//默认选中第1个
            }
        }
        citySpinner.setSelection(cityIndex, true);

        countyAdapter = new SelectPlaceAdapter(PublishLabourActivity.this, citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(cityIndex)));
        countySpinner.setAdapter(countyAdapter);
        for (int i = 0; i < citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(cityIndex)).size(); i++) {
            if (citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(cityIndex)).get(i).equals(bdLocation.getAddress().district)) {
                countyIndex = i;
                break;
            } else {
                countyIndex = 0;
            }
        }
        countySpinner.setSelection(countyIndex, true);

        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                cityAdapter.reFreshData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                countyAdapter.reFreshData(citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


    // 获取城市信息
    private void getaddressinfo() {
        JSONParser jsonParser=new JSONParser();
        // 读取城市信息string
        String area_str = FileUtil.readAssets(this, "area.json");
        province_list = jsonParser.getJSONParserResult(area_str, "area0");
        city_map = jsonParser.getJSONParserResultArray(area_str, "area1");
        couny_map = jsonParser.getJSONParserResultArray(area_str, "area2");
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
            if (output instanceof Labour) {
                Labour labour = (Labour) output;
                EUtil.showToast(labour.getErr());
                if ("0".equals(labour.getRes())) {
                    finish();
                }
            }
            if (output instanceof Modiylabour){
                Modiylabour modiylabour= (Modiylabour) output;
                EUtil.showToast(modiylabour.getErr());
                if ("0".equals(modiylabour.getRes())) {
                    finish();
                }
            }

        }
    };

    public static class JSONParser {
        public ArrayList<String> province_list_code = new ArrayList<String>();
        public ArrayList<String> city_list_code = new ArrayList<String>();

        public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
            List<Cityinfo> list = new ArrayList<Cityinfo>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator<?> iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                @SuppressWarnings("unchecked")
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator
                        .next();
                Cityinfo cityinfo = new Cityinfo();

                cityinfo.setCity_name(entry.getValue().getAsString());
                cityinfo.setId(entry.getKey());
                province_list_code.add(entry.getKey());
                list.add(cityinfo);
            }
//            System.out.println(province_list_code.size());
            return list;
        }

        public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
                String JSONString, String key) {
            HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator<?> iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                @SuppressWarnings("unchecked")
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator
                        .next();
                List<Cityinfo> list = new ArrayList<Cityinfo>();
                JsonArray array = entry.getValue().getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    Cityinfo cityinfo = new Cityinfo();
                    cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
                            .getAsString());
                    cityinfo.setId(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    city_list_code.add(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    list.add(cityinfo);
                }
                hashMap.put(entry.getKey(), list);
            }
            return hashMap;
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locClient != null) {
            locClient.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locClient != null) {
            locClient.stop();
        }
        unregisterReceiver(baiduReceiver);
    }
}
