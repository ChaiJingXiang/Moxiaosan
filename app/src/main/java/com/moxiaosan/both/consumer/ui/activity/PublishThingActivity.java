package com.moxiaosan.both.consumer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
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
import com.moxiaosan.both.consumer.ui.adapter.PublishThingImageAdapter;
import com.moxiaosan.both.consumer.ui.adapter.SelectPlaceAdapter;
import com.moxiaosan.both.consumer.utils.CitycodeUtil;
import com.moxiaosan.both.consumer.utils.Cityinfo;
import com.moxiaosan.both.consumer.utils.FileUtil;
import com.moxiaosan.both.utils.AvatarUploader;
import com.moxiaosan.both.utils.FileUploader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Modiyshop;
import consumer.model.Shopping;
import consumer.model.obj.RespShop;
import picture.PictureGalleryActivity;

public class PublishThingActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_GRIDE_VIEW = 3;

    private EditText etName, etNum, etPrice, etDescribe, etAddress;
    private GridView gridView;
    private ArrayList<String> imageList; //要上传的本地图片路径
    private ArrayList<String> successUrlList = new ArrayList<String>();  //上传成功的网络图片路径
    private static final int MAX_NUM = 3;  //最多上传3张图片
    private PublishThingImageAdapter adapter;
    private String pics = "";  //图片地址，中间用逗号隔开

    private String title = "";
    private RespShop respShop;

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

    // 上传文件相关   变量
//    private String mFileUrl; // 头像网络文件地址
    private String mLocalFilePath; // 本地文件地址
    private AvatarUploader mUploader; // 文件上传对象
    private final static int UPLOAD_OK = 1;
    private final static int UPLOAD_FAIL = 2;
    private int reUploadTimes = 0; //重新上传次数

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
        setContentView(R.layout.activity_publish_thing);
        showActionBar(true);
        title = getIntent().getStringExtra("title");
        respShop = (RespShop) getIntent().getSerializableExtra("respShop");
        if (TextUtils.isEmpty(title)) {
            setActionBarName("发布商品");
        } else {
            setActionBarName(title);
        }
        citycodeUtil = CitycodeUtil.getSingleton();
        getaddressinfo();
        initView();
        initVars();

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
            String address = "";
            if (!TextUtils.isEmpty(bdLocation.getAddress().street)) {
                address = bdLocation.getAddress().street;
                if (!TextUtils.isEmpty(bdLocation.getAddress().streetNumber)) {
                    address = address + bdLocation.getAddress().streetNumber + "号";
                }
            } else {
                EUtil.showToast("系统无法定位您的位置，请手动输入");
            }
            etAddress.setText(address);
            if (locClient != null) {
                locClient.stop();
            }
        }
    }

    private void initView() {
        findViewById(R.id.publish_thing_ensure).setOnClickListener(this);
        etName = (EditText) findViewById(R.id.publish_thing_name);
        etPrice = (EditText) findViewById(R.id.publish_thing_price);
        etNum = (EditText) findViewById(R.id.publish_thing_num);
        etDescribe = (EditText) findViewById(R.id.publish_thing_describe);
        etAddress = (EditText) findViewById(R.id.publish_thing_address);
        gridView = (GridView) findViewById(R.id.publish_thing_gridview);
//        setSpinner();
        imageList = new ArrayList<String>();
        if (respShop != null) {  //网络  修改
            etName.setText(respShop.getTitle());
            etPrice.setText(respShop.getPrice());
            etNum.setText(respShop.getNums());
            etAddress.setText(respShop.getAddress());
            etDescribe.setText(respShop.getDescribes());
            String[] urlList = respShop.getPictures().split(",");
            for (int i = 0; i < urlList.length; i++) {
                imageList.add(urlList[i]);
            }
            adapter = new PublishThingImageAdapter(this, imageList, MAX_NUM, true);
        } else {
            adapter = new PublishThingImageAdapter(this, imageList, MAX_NUM, false);
        }

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < imageList.size()) {

                } else if (imageList.size() < MAX_NUM) {
                    startActivityForResult(new Intent(PublishThingActivity.this, PictureGalleryActivity.class)
                            .putExtra("maxNum", MAX_NUM - imageList.size()), REQUEST_CODE_GRIDE_VIEW);
                } else {
                    EUtil.showToast(String.format("已满%d张图片", MAX_NUM));
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
        provinceAdapter = new SelectPlaceAdapter(PublishThingActivity.this, citycodeUtil.getProvince(province_list));
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

        cityAdapter = new SelectPlaceAdapter(PublishThingActivity.this, citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(provinIndex)));
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

        countyAdapter = new SelectPlaceAdapter(PublishThingActivity.this, citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(cityIndex)));
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
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = FileUtil.readAssets(this, "area.json");
        province_list = parser.getJSONParserResult(area_str, "area0");
        city_map = parser.getJSONParserResultArray(area_str, "area1");
        couny_map = parser.getJSONParserResultArray(area_str, "area2");
    }

    // 初始化变量
    private void initVars() {

        // 本地文件地址
        mLocalFilePath = "";
        // 网络文件地址
//        mFileUrl = "";

        // 文件上传对象
        mUploader = new AvatarUploader(new FileUploader.OnFileUploadListener() {
            @Override
            public void onPrepared() {

            }

            @Override
            public void onStarted() {
                showLoadingDialog();
            }

            @Override
            public void onUpdate(int value) {

            }

            @Override
            public void onCanceled() {

            }

            @Override
            public void onSuccess(String fileUrl) {
//                Log.i("PersonInfoActivity", "图片上传地址==" + fileUrl);
                successUrlList.add(fileUrl);
                pics = pics + fileUrl + ",";
                if (imageList.size() != successUrlList.size()) {
                    mUploader.start(imageList.get(successUrlList.size()));
                } else {
                    pics = pics.substring(0, pics.length() - 1);
                    handler.sendEmptyMessage(UPLOAD_OK);
                }

            }

            @Override
            public void onError(int code, String message) {
                republishPic(imageList.get(successUrlList.size()));
            }
        });
    }

    private void republishPic(String picStr) {
        if (reUploadTimes < 3) {
            mUploader.start(picStr);
            reUploadTimes++;
        } else {
            handler.sendEmptyMessage(UPLOAD_FAIL);
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case UPLOAD_OK:
                    dismissLoadingDialog();
//                    EUtil.showToast("上传成功");
                    publishThing(pics);
                    break;
                case UPLOAD_FAIL:
                    dismissLoadingDialog();
                    EUtil.showToast("上传失败，稍后重试");
                    break;
            }
        }
    };

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GRIDE_VIEW) {
                if (data != null) {
                    imageList.addAll(data.getStringArrayListExtra("imageList"));
                    adapter.notifyDataSetChanged();
                } else {
                    EUtil.showToast("无数据");
                }
            }
        }
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
            if (output instanceof Shopping) {
                Shopping shopping = (Shopping) output;
                EUtil.showToast(shopping.getErr());
                if ("0".equals(shopping.getRes())) {
                    finish();
                }
            }
            if (output instanceof Modiyshop) {
                Modiyshop modiyshop = (Modiyshop) output;
                EUtil.showToast(modiyshop.getErr());
                if ("0".equals(modiyshop.getRes())) {
                    finish();
                }
            }
        }
    };

    //访问服务器接口
    private void publishThing(String piclist) {
        ConsumerReqUtil.shopping(this, iApiCallback, null, new Shopping(), "PublishThingActivity", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("title", etName.getText().toString())
                        .putValue("describe", etDescribe.getText().toString()).putValue("nums", etNum.getText().toString()).putValue("price", etPrice.getText().toString())
                        .putValue("address", provinceSpinner.getSelectedItem().toString() + citySpinner.getSelectedItem().toString() + countySpinner.getSelectedItem().toString()
                                + etAddress.getText().toString()).putValue("pictures", piclist).createMap()));
        showLoadingDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish_thing_ensure:
                if (!TextUtils.isEmpty(etName.getText().toString())) {
                    if (!TextUtils.isEmpty(etPrice.getText().toString())) {
                        if (!TextUtils.isEmpty(etNum.getText().toString())) {
                            if (!TextUtils.isEmpty(etDescribe.getText().toString())) {
                                if (respShop != null) {  //修改
                                    ConsumerReqUtil.modiyshop(this, iApiCallback, null, new Shopping(), "PublishThingActivity", true,
                                            StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("title", etName.getText().toString())
                                                    .putValue("describe", etDescribe.getText().toString()).putValue("nums", etNum.getText().toString()).putValue("price", etPrice.getText().toString())
                                                    .putValue("address", provinceSpinner.getSelectedItem().toString() + citySpinner.getSelectedItem().toString() + countySpinner.getSelectedItem().toString()
                                                            + etAddress.getText().toString()).putValue("pictures", respShop.getPictures()).putValue("shopid", respShop.getId()).createMap())
                                    );
                                } else {   //发布新的商品
                                    if (successUrlList.size() > 0) {
                                        successUrlList.clear();
                                    }

                                    if (imageList.size() > 0) {  //有照片要上传
                                        mUploader.start(imageList.get(successUrlList.size()));  //开始上传
                                    } else {  //没有选择照片
                                        publishThing(pics);
                                    }
                                }
                            } else {
                                EUtil.showToast("描述不能为空");
                            }
                        } else {
                            EUtil.showToast("数量不能为空");
                        }
                    } else {
                        EUtil.showToast("价格不能为空");
                    }
                } else {
                    EUtil.showToast("名称不能为空");
                }
                break;
        }
    }

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
            System.out.println(province_list_code.size());
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
