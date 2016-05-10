package com.moxiaosan.both.carowner.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.adapter.SpinnerAdapter;
import com.moxiaosan.both.utils.BaiduLocation;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.RespGuiJI;
import consumer.model.obj.GuiJiObj;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by chris on 16/3/18.
 */
public class GuiJiActivity extends BaseActivity implements IApiCallback{

    private CheckBox checkBox;
    private Spinner spinner;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private String[] m;
    private ArrayAdapter<String> arrayAdapter;

    private LineChartView chart;
    private int numberOfPoints;
    float[] speedArray = null;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private ProgressBar progressBar;
    private boolean flag =true;
    private List<LatLng> points =null;
    private TextView tvAllKm,tvMAxSpeed,tvAverageSpeed;
    private  List<GuiJiObj> list =new ArrayList<>();

    //该程序模拟填充长度为100的数组
    private int[] array = new int[101];
    int hasData = 0;
    //记录ProgressBar的完成进度
    int status = 0;

    BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_dingwei);
    private LatLng ll1 =null;
    private LatLng ll2 =null;
    private LatLng ll3 =null;
    private LatLng ll4 =null;
    private LatLng ll5 =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_guiji_layout);
        showActionBar(true);
        setActionBarName("骑行轨迹");

        checkBox =(CheckBox)findViewById(R.id.guiji_checkbox);
        spinner =(Spinner)findViewById(R.id.spinnerId);

        progressBar =(ProgressBar)findViewById(R.id.progressBarId);

        // 地图初始化
        mMapView =(MapView)findViewById(R.id.mapViewId);

        tvAllKm =(TextView)findViewById(R.id.allKimId);
        tvMAxSpeed =(TextView)findViewById(R.id.maxSpeedId);
        tvAverageSpeed =(TextView)findViewById(R.id.averageSpeedId);

        initMapView();

        showLoadingDialog();
        CarReqUtils.travelingtrack(this, this, null,new RespGuiJI(),"travelingtrack",true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                        putValue("hour",1).createMap()));

        //时间spinner
        m = (String[])getResources().getStringArray(R.array.guiji_time);

        arrayAdapter = new SpinnerAdapter(this,m);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        spinner.setSelection(0,true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                showLoadingDialog();

                CarReqUtils.travelingtrack(GuiJiActivity.this, GuiJiActivity.this, null,new RespGuiJI(),"spinner",true,
                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                putValue("hour",m[position]).createMap()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chart = (LineChartView) findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        // Disable viewpirt recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    hasData =0;
                    status =0;
                    flag =true;

                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            while(flag){

                                if(status<100){
                                    // 获取耗时操作的完成百分比
                                    status = doWork();
                                    // 发送消息到Handler
                                    handler.sendEmptyMessage(0);

                                }else{
                                    handler.sendEmptyMessage(1);
                                }

                            }

                        }
                    }.start();

                }else{

                    handler.sendEmptyMessage(1);
                }

            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what ==0){
                progressBar.setProgress(status);
                if(status>0 && status<=20){
                    OverlayOptions oo = new MarkerOptions().position(ll1).icon(bd).zIndex(9).draggable(true);
                    Marker markers = (Marker) (mBaiduMap.addOverlay(oo));
                }else if(status>20 && status<=40){
                    OverlayOptions oo = new MarkerOptions().position(ll2).icon(bd).zIndex(9).draggable(true);
                    Marker markers = (Marker) (mBaiduMap.addOverlay(oo));
                }else if(status>40 && status<=60){
                    OverlayOptions oo = new MarkerOptions().position(ll3).icon(bd).zIndex(9).draggable(true);
                    Marker markers = (Marker) (mBaiduMap.addOverlay(oo));
                }else if(status>60 && status<=80){
                    OverlayOptions oo = new MarkerOptions().position(ll4).icon(bd).zIndex(9).draggable(true);
                    Marker markers = (Marker) (mBaiduMap.addOverlay(oo));
                }else{
                    OverlayOptions oo = new MarkerOptions().position(ll5).icon(bd).zIndex(9).draggable(true);
                    Marker markers = (Marker) (mBaiduMap.addOverlay(oo));
                }
            }else{
                flag =false;
                status =100;
                progressBar.setProgress(status);
                checkBox.setChecked(false);
            }

        }
    };

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {

        if(output !=null){
            dismissLoadingDialog();
            if(output instanceof RespGuiJI){
                RespGuiJI guiji =(RespGuiJI)output;
                if(guiji.getRes().equals("0")){

                    if(input.equals("spinner")){

                        if(list.size()!=0){
                            list.clear();
                        }

                        list =guiji.getData();
                        numberOfPoints =list.size();

                        Log.i("info---==--",list.size()+"");

                        tvAllKm.setText(guiji.getJourney()+"km");
                        tvMAxSpeed.setText(guiji.getMaxspeed()+"km/h");
                        tvAverageSpeed.setText(guiji.getAveragespeed()+"km/h");

                        reset();

                        resetViewport();

                        generateValues();

                        generateData();

                        if(points.size()!=0){
                            mBaiduMap.clear();

                            points.clear();

                            Log.i("info===---",points.size()+"");

                        }

                        for(int i=0;i<list.size();i++){
                            points.add(new LatLng(list.get(i).getLat(),list.get(i).getLng()));

                        }

                        Log.i("info===---",points.size()+"");

                        if(list.size()>30){
                            ll1 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll2 =new LatLng(points.get(list.size()/4).latitude,points.get(list.size()/4).longitude);
                            ll3 =new LatLng(points.get(list.size()/3).latitude,points.get(list.size()/3).longitude);
                            ll4 =new LatLng(points.get(list.size()/2).latitude,points.get(list.size()/2).longitude);
                            ll5 =new LatLng(points.get(list.size()-1).latitude,points.get(list.size()-1).longitude);

                        }else{

                            ll1 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll2 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll3 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll4 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll5 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                        }

                        OverlayOptions ooStart = new MarkerOptions().position(new LatLng(points.get(0).latitude,points.get(0).longitude)).icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.ic_photograph_coordinate)).zIndex(4).draggable(false);

                        if (mBaiduMap != null) {
                            mBaiduMap.addOverlay(ooStart);
                        }

                        LatLng llA = new LatLng(points.get(points.size()-1).latitude,points.get(points.size()-1).longitude);

                        OverlayOptions ooEnd = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.chufa_small)).zIndex(4).draggable(false);

                        if (mBaiduMap != null) {
                            mBaiduMap.addOverlay(ooEnd);
                        }

                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llA, 14.0f);
                        mBaiduMap.animateMapStatus(u);

                        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                                .color(0xAAFF0000).points(points);
                        mBaiduMap.addOverlay(ooPolyline);

                    }else{

                        points =new ArrayList<>();

                        list =guiji.getData();

                        Log.i("info---==--",list.size()+"");

                        numberOfPoints =list.size();

                        for(int i=0;i<list.size();i++){
//                            double [] db = BaiduLocation.wgs2bd(list.get(i).getLat(),list.get(i).getLng());
//                            points.add(new LatLng(db[0] , db[1]));
                            points.add(new LatLng(list.get(i).getLat(),list.get(i).getLng()));

                        }

                        if(list.size()>30){
                            ll1 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll2 =new LatLng(points.get(list.size()/4).latitude,points.get(list.size()/4).longitude);
                            ll3 =new LatLng(points.get(list.size()/3).latitude,points.get(list.size()/3).longitude);
                            ll4 =new LatLng(points.get(list.size()/2).latitude,points.get(list.size()/2).longitude);
                            ll5 =new LatLng(points.get(list.size()-1).latitude,points.get(list.size()-1).longitude);

                        }else{

                            ll1 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll2 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll3 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll4 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                            ll5 =new LatLng(points.get(1).latitude,points.get(1).longitude);
                        }



                        Log.i("info-----",list.toString());

                        tvAllKm.setText(guiji.getJourney()+"km");
                        tvMAxSpeed.setText(guiji.getMaxspeed()+"km/h");
                        tvAverageSpeed.setText(guiji.getAveragespeed()+"km/h");


                        OverlayOptions ooStart = new MarkerOptions().position(new LatLng(points.get(0).latitude,points.get(0).longitude)).icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.ic_photograph_coordinate)).zIndex(4).draggable(false);

                        if (mBaiduMap != null) {
                            mBaiduMap.addOverlay(ooStart);
                        }

                        LatLng llA = new LatLng(points.get(points.size()-1).latitude,points.get(points.size()-1).longitude);

                        OverlayOptions ooEnd = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.chufa_small)).zIndex(4).draggable(false);

                        if (mBaiduMap != null) {
                            mBaiduMap.addOverlay(ooEnd);
                        }

                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llA, 14.0f);
                        mBaiduMap.animateMapStatus(u);
                        //起点与终点
//                    final PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", "宝安中心");
//                    final PlanNode enNode = PlanNode.withCityNameAndPlaceName("深圳", "高新园地铁站");
//
//                    mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
                        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                                .color(0xAAFF0000).points(points);
                        mBaiduMap.addOverlay(ooPolyline);

                        resetViewport();

                        generateValues();

                        generateData();
                    }

                }else{
                    dismissLoadingDialog();
                    EUtil.showToast(guiji.getErr());
//                    finish();
                }
            }
        }else{
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
            finish();
        }
    }

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

    private void initMapView() {
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mMapView.removeViewAt(1); //隐藏百度logo
        mMapView.setLongClickable(true);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(new LatLng(22.553719,113.925328),17.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        super.onPause();
    }


    private void generateValues() {
        speedArray =new float[numberOfPoints];

        for (int i = 0; i <numberOfPoints; i++) {

            speedArray[i] =Integer.parseInt(list.get(i).getSpeed());

        }

    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(GuiJiActivity.this, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    private void generateData() {

        List<PointValue> values = new ArrayList<PointValue>();

        for(int i=0;i<list.size();i++){
            values.add(new PointValue(i, speedArray[i]));
        }
//        values.add(new PointValue(0, 2));
//        values.add(new PointValue(1, 40));
//        values.add(new PointValue(2, 103));
//        values.add(new PointValue(3, 60));
//        values.add(new PointValue(4, 2));
//        values.add(new PointValue(5, 40));
//        values.add(new PointValue(6, 103));
//        values.add(new PointValue(7, 60));
//        values.add(new PointValue(8, 2));
//        values.add(new PointValue(9, 40));
//        values.add(new PointValue(10, 103));
//        values.add(new PointValue(11, 60));

        Line line = new Line(values).
                setColor(getResources().getColor(R.color.main_color)).
                setPointColor(getResources().getColor(R.color.main_color)).
                setShape(shape).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("");
                axisY.setName("速度km/h");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 160;
        v.left = 0;
        v.right = numberOfPoints-1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void reset() {
        hasAxes = true;
        hasAxesNames = true;
        shape = ValueShape.CIRCLE;

        resetViewport();
    }



    //模拟一个耗时的操作。
    public int doWork()
    {
        //为数组元素赋值
        array[hasData+=10] = (int)(Math.random() * 100);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return hasData;

    }
}
