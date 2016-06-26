package com.moxiaosan.both.carowner.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

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
public class GuiJiActivity extends BaseActivity implements IApiCallback {

    private CheckBox checkBox;
    private Spinner spinner;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private String[] m;
    private ArrayAdapter<String> arrayAdapter;

    private LineChartView chart;

    float[] speedArray = null;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private ValueShape shape = ValueShape.CIRCLE;
    //    private ProgressBar progressBar;
    private SeekBar seekBar;
    private TextView tvAllKm, tvMAxSpeed, tvAverageSpeed;
    private List<GuiJiObj> list = new ArrayList<GuiJiObj>();

    private List<LatLng> points = null;
    private BitmapDescriptor icon = null;
    private Marker mMarker;//标注
    private boolean flag = false;
    private int index = 0;// 第几个点
    private int numberOfPoints;
    private LatLng llMiddle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_guiji_layout);
        showActionBar(true);
        setActionBarName("骑行轨迹");

        checkBox = (CheckBox) findViewById(R.id.guiji_checkbox);
        spinner = (Spinner) findViewById(R.id.spinnerId);

//        progressBar = (ProgressBar) findViewById(R.id.progressBarId);
        seekBar = (SeekBar) findViewById(R.id.seekBarId);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                index = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mapViewId);

        tvAllKm = (TextView) findViewById(R.id.allKimId);
        tvMAxSpeed = (TextView) findViewById(R.id.maxSpeedId);
        tvAverageSpeed = (TextView) findViewById(R.id.averageSpeedId);

        initMapView();

        showLoadingDialog();
        CarReqUtils.travelingtrack(this, this, null, new RespGuiJI(), "travelingtrack", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                        putValue("hour", 1).createMap()));

        //时间spinner
        m = (String[]) getResources().getStringArray(R.array.guiji_time);

        arrayAdapter = new SpinnerAdapter(this, m);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        spinner.setSelection(0, true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                showLoadingDialog();

                CarReqUtils.travelingtrack(GuiJiActivity.this, GuiJiActivity.this, null, new RespGuiJI(), "spinner", true,
                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                putValue("hour", m[position]).createMap()));

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
                resetOverlay();
            }
        });
    }

    public void resetOverlay() {
        if (!flag) {
            if (points != null) {
                if (points.size() != 0) {
                    if (index == points.size()) {
                        mBaiduMap.clear();
                        index = 0;
                        initOverlay(llMiddle);// 初始化
                    }
                    checkBox.setChecked(true);
                    if (points.size() >= 3) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        EUtil.showToast("轨迹点数量必须大于2");
                    }
                }
            } else {
                //无数据
            }

        } else {
            checkBox.setChecked(false);
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
    }

    // 初始化
    public void initOverlay(LatLng middleLatLng) {
//        progressBar.setMax(points.size());
        seekBar.setMax(points.size());
        index = 0;
        seekBar.setProgress(index);
//        progressBar.setProgress(index);
        OverlayOptions ooPolyline = new PolylineOptions().width(5).color(0xAAFF0000).points(points);
        mBaiduMap.addOverlay(ooPolyline);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(middleLatLng);
        mBaiduMap.setMapStatus(u);
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.location_indicator);
        OverlayOptions ooA = new MarkerOptions().position(points.get(index)).zIndex(7)
                .icon(icon).draggable(false);
        mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                flag = true;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        start();
                    }
                }).start();
            }
            if (msg.what == 2) {
                flag = false;
            }
            if (msg.what == 3) {   //自动播放完全了
                checkBox.setChecked(false);
                flag = false;
            }
            return false;
        }
    });

    // 画轨迹
    public void start() {
        if (flag) {
            if (mMarker != null) {
                mMarker.remove();
            }
            /**
             * 计算角度
             */
            Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.location_indicator);
            Matrix matrix = new Matrix();
            double x1 = points.get(index).latitude, x2 = points
                    .get(index + 1 == points.size() ? index - 2 : index + 1).latitude; // 点1坐标;
            double y1 = points.get(index).longitude, y2 = points
                    .get(index + 1 == points.size() ? index - 2 : index + 1).longitude;// 点2坐标
            double x = Math.abs(x1 - x2);
            double y = Math.abs(y1 - y2);
            double z = Math.sqrt(x * x + y * y);
            int jiaodu = Math.round((float) (Math.asin(y / z) / Math.PI * 180));// 最终角度
            x = y1 - y2;
            y = x1 - x2;

            if (x > 0 && y < 0) {// 在第二象限
                jiaodu = 0 - jiaodu;
            }
            if (x > 0 && y > 0) {// 在第三象限
                jiaodu = jiaodu + 90;
            }
            if (x < 0 && y > 0) {// 在第四象限
                jiaodu = 180 + (90 - jiaodu);
            }
            /**
             * 计算角度
             */


            /**
             * 旋转图标
             */
            matrix.postRotate(jiaodu);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                    bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
            icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
            /**
             * 旋转图标
             */

            OverlayOptions ooA = new MarkerOptions()
                    .position(points.get(index)).icon(icon).draggable(true);
            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(points.get(index));
            float f = mBaiduMap.getMaxZoomLevel();//最大比例尺
            //float m = mBaiduMap.getMinZoomLevel();//3.0
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(points.get(index), f - 2);
            mBaiduMap.setMapStatus(u);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            index++;
//            progressBar.setProgress(index); //设置进度条
            seekBar.setProgress(index); //设置进度条
            if (index != points.size()) {
                start();
            } else {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        }
    }


    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {

        if (output != null) {
            dismissLoadingDialog();
            if (output instanceof RespGuiJI) {
                RespGuiJI guiji = (RespGuiJI) output;
                if (guiji.getRes().equals("0")) {

                    if (input.equals("spinner")) {

                        if (list.size() != 0) {
                            list.clear();
                        }

                        list = guiji.getData();
                        numberOfPoints = list.size();
                        tvAllKm.setText(guiji.getJourney() + "km");
                        tvMAxSpeed.setText(guiji.getMaxspeed() + "km/h");
                        tvAverageSpeed.setText(guiji.getAveragespeed() + "km/h");

                        reset();

                        resetViewport();

                        generateValues();

                        generateData();
                        if (points == null) {
                            points = new ArrayList<LatLng>();
                        }
                        if (points.size() != 0) {
                            mBaiduMap.clear();

                            points.clear();
                        }

                        for (int i = 0; i < list.size(); i++) {
                            double[] db = BaiduLocation.wgs2bd(list.get(i).getLat(), list.get(i).getLng());
                            points.add(new LatLng(db[0], db[1]));
//                            points.add(new LatLng(list.get(i).getLat(),list.get(i).getLng()));
                        }
                        if (list.size() == 0) {
                            return;
                        }

                        if (points.size() >= 2) {
                            LatLng llStart = new LatLng(points.get(0).latitude, points.get(0).longitude);
                            OverlayOptions ooStart = new MarkerOptions().position(llStart).icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.ic_photograph_coordinate)).zIndex(4).draggable(false);

                            if (mBaiduMap != null) {
                                mBaiduMap.addOverlay(ooStart);
                            }

                            LatLng llA = new LatLng(points.get(points.size() - 1).latitude, points.get(points.size() - 1).longitude);

                            OverlayOptions ooEnd = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.chufa_small)).zIndex(4).draggable(false);

                            if (mBaiduMap != null) {
                                mBaiduMap.addOverlay(ooEnd);
                            }

                            llMiddle = new LatLng((points.get(0).latitude + points.get(points.size() - 1).latitude) / 2, (points.get(0).longitude + points.get(points.size() - 1).longitude) / 2);
                            initOverlay(llMiddle);
                        }

                    } else {

                        points = new ArrayList<LatLng>();

                        list = guiji.getData();
                        numberOfPoints = list.size();

                        for (int i = 0; i < list.size(); i++) {
                            double[] db = BaiduLocation.wgs2bd(list.get(i).getLat(), list.get(i).getLng());
                            points.add(new LatLng(db[0], db[1]));
//                            points.add(new LatLng(list.get(i).getLat(),list.get(i).getLng()));

                        }
                        tvAllKm.setText(guiji.getJourney() + "km");
                        tvMAxSpeed.setText(guiji.getMaxspeed() + "km/h");
                        tvAverageSpeed.setText(guiji.getAveragespeed() + "km/h");

                        if (points.size() >= 2) {
                            LatLng llStart = new LatLng(points.get(0).latitude, points.get(0).longitude);
                            OverlayOptions ooStart = new MarkerOptions().position(llStart).icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.ic_photograph_coordinate)).zIndex(4).draggable(false);

                            if (mBaiduMap != null) {
                                mBaiduMap.addOverlay(ooStart);
                            }

                            LatLng llEnd = new LatLng(points.get(points.size() - 1).latitude, points.get(points.size() - 1).longitude);
                            OverlayOptions ooEnd = new MarkerOptions().position(llEnd).icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.chufa_small)).zIndex(4).draggable(false);

                            if (mBaiduMap != null) {
                                mBaiduMap.addOverlay(ooEnd);
                            }

                            llMiddle = new LatLng((points.get(0).latitude + points.get(points.size() - 1).latitude) / 2, (points.get(0).longitude + points.get(points.size() - 1).longitude) / 2);
                            initOverlay(llMiddle);
                        }

                        resetViewport();

                        generateValues();

                        generateData();
                    }

                } else {
                    dismissLoadingDialog();
                    EUtil.showToast(guiji.getErr());

                    //原来有数据  后来没有
//                    progressBar.setMax(0);
//                    progressBar.setProgress(0);
                    seekBar.setMax(0);
                    seekBar.setProgress(0);
                    index = 0;
                    if (points != null) {
                        points.clear();
                    }
                    mBaiduMap.clear();
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
//                    finish();
                }
            }
        } else {
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
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(new LatLng(22.553719, 113.925328), 15.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
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
        flag = false;
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        super.onPause();
    }


    private void generateValues() {
        speedArray = new float[numberOfPoints];

        for (int i = 0; i < numberOfPoints; i++) {

            speedArray[i] = Integer.parseInt(list.get(i).getSpeed());

        }

    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
//            Toast.makeText(GuiJiActivity.this, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    private void generateData() {

        List<PointValue> values = new ArrayList<PointValue>();

        for (int i = 0; i < list.size(); i++) {
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
        v.right = numberOfPoints - 1;
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
//    public int doWork() {
//        //为数组元素赋值
//        array[hasData += 10] = (int) (Math.random() * 100);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return hasData;
//
//    }
}
