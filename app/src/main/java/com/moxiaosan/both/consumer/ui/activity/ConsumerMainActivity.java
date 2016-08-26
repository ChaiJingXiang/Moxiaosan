package com.moxiaosan.both.consumer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.moxiaosan.both.APP;
import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.activity.CityPositionActivity;
import com.moxiaosan.both.common.ui.activity.MessagesActivity;
import com.moxiaosan.both.consumer.ui.customView.banner.AutoScrollViewPager;
import com.moxiaosan.both.consumer.ui.customView.banner.ViewPagerAdapter;
import com.moxiaosan.both.consumer.ui.fragment.LeftFragment;
import com.moxiaosan.both.mqtt.MqttService;
import com.umeng.update.UmengUpdateAgent;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.log.LLog;
import com.utils.ui.base.ActivityHolder;
import com.utils.ui.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Mynews;
import consumer.model.Pictrues;
import consumer.model.obj.ResPictrue;

public class ConsumerMainActivity extends BaseFragmentActivity implements View.OnClickListener {
    public final static int CITY_GET_CODE = 1;
    private SlidingMenu slidingMenu;
    private FragmentManager fm;
    private LeftFragment lf;
    private FragmentTransaction ft;
    public AutoScrollViewPager autoScrollViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private AutoViewPagerListener autoViewPagerListener;
    private RadioGroup radio_group;
    private List<ResPictrue> subLists = new ArrayList<ResPictrue>(); //viewpager专题对象
    private TextView tvCityName, tvMessageCount,tvDistrictName;
    private SharedPreferences sp;
    private SharedPreferences.Editor editorLocation = null;

    private Mynews mynews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showActionBar(false);

        //友盟自动更新
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setDeltaUpdate(true);
        initMqttServer();

        ConsumerReqUtil.carousel(this, iApiCallback, null, new Pictrues(), "ConsumerMainActivity", true);

        initView();
        showSlidingMenu();
    }

    /**
     * 启动 mqtt 服务
     */
    private void initMqttServer() {
        String mDeviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);  //设备ID保存
        SharedPreferences.Editor editor = getSharedPreferences(MqttService.TAG, MODE_PRIVATE).edit();
        editor.putString(MqttService.PREF_DEVICE_ID, mDeviceID);
        editor.commit();
        this.startService(new Intent(this, MqttService.class));
    }

    private void initView() {
        sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        editorLocation = sp.edit();
        tvCityName = (TextView) findViewById(R.id.main_city_name_txt);
        tvDistrictName = (TextView) findViewById(R.id.main_district_name_txt);
        if (!TextUtils.isEmpty(sp.getString("city", ""))) {
            tvCityName.setText(sp.getString("city", ""));
        }
        if (!TextUtils.isEmpty(sp.getString("district", ""))) {
            tvDistrictName.setText(sp.getString("district", ""));
        }
        tvMessageCount = (TextView) findViewById(R.id.main_my_message_count);
        findViewById(R.id.main_user_photo).setOnClickListener(this);
        findViewById(R.id.main_city_location).setOnClickListener(this);
        findViewById(R.id.main_gatetogate_layout).setOnClickListener(this);
        findViewById(R.id.main_shunfengche_layout).setOnClickListener(this);
        findViewById(R.id.main_sellthing_layout).setOnClickListener(this);
        findViewById(R.id.main_find_labour_layout).setOnClickListener(this);
        findViewById(R.id.main_my_orders).setOnClickListener(this);
        findViewById(R.id.main_my_messages).setOnClickListener(this);
        radio_group = (RadioGroup) findViewById(R.id.main_radio_group);
        autoScrollViewPager = (AutoScrollViewPager) findViewById(R.id.main_viewpager);

        //无线轮播设置
        autoScrollViewPager.setInterval(3000);
        autoScrollViewPager.setBorderAnimation(true);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setCycle(true);
        autoScrollViewPager.setAutoScrollDurationFactor(8);

    }

    //初始化viewpager
    private void initViewPageUI() {
        viewPagerAdapter = new ViewPagerAdapter(this, subLists);
        autoScrollViewPager.setAdapter(viewPagerAdapter);
        autoScrollViewPager.startAutoScroll();

        int dp5 = (int) getResources().getDimension(R.dimen.value_8dp);
        radio_group.removeAllViews();

        for (int i = 0; i < subLists.size(); i++) {
            RadioButton dot = new RadioButton(this);
            dot.setButtonDrawable(new BitmapDrawable(null, ((Bitmap) null)));
            dot.setButtonDrawable(0);
            dot.setBackgroundResource(R.drawable.dot_selector);
            dot.setId(i);
            radio_group.addView(dot);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) dot.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.value_10dp);
            layoutParams.width = (int) getResources().getDimension(R.dimen.value_10dp);
            if (i != 0) {
                layoutParams.leftMargin = dp5;
            }
            dot.setLayoutParams(layoutParams);
        }

        autoViewPagerListener = new AutoViewPagerListener();
        autoScrollViewPager.addOnPageChangeListener(autoViewPagerListener);

        autoScrollViewPager.setCurrentItem(100 * subLists.size() + 1);
        autoScrollViewPager.setCurrentItem(100 * subLists.size());
    }

    @Override
    public void onResume() {
        super.onResume();
        autoScrollViewPager.startAutoScroll();
        //请求我的信息接口
        ConsumerReqUtil.mynews(this, iApiCallback, null, new Mynews(), "ConsumerMainActivity", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("pageNow", 1).createMap()));
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        autoScrollViewPager.stopAutoScroll();
    }

    private void showSlidingMenu() {

        slidingMenu = new SlidingMenu(this);

        slidingMenu.setMode(SlidingMenu.LEFT);

        slidingMenu.setMenu(R.layout.slidingmenu);

        // 第四步:设置菜单显示的宽度
        int sw = getResources().getDisplayMetrics().widthPixels;
        slidingMenu.setBehindWidth((int) (sw * 4 / 5));// 单位:px

        // 设置启用菜单渐变动画
        slidingMenu.setFadeEnabled(true);
        // 设置菜单动画的渐变度
        slidingMenu.setFadeDegree(0.9f);

        // 设置菜单层与内容滚动比例
        slidingMenu.setBehindScrollScale(0.25f);

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        fm = getSupportFragmentManager();

        ft = fm.beginTransaction();

        lf = new LeftFragment();

        ft.replace(R.id.menuId, lf);

        ft.commit();

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
            if (output instanceof Pictrues) {
                Pictrues pictrues = (Pictrues) output;
                subLists = pictrues.getData();
                initViewPageUI();
            }
            if (output instanceof Mynews) {
                mynews = (Mynews) output;
                if ("0".equals(mynews.getNums()) || TextUtils.isEmpty(mynews.getNums())) {
                    tvMessageCount.setVisibility(View.GONE);
                } else {
                    tvMessageCount.setVisibility(View.VISIBLE);
                    tvMessageCount.setText(mynews.getNums() + "");
                }
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_user_photo:
                if (!slidingMenu.isShown()) {
                    slidingMenu.showMenu();
                } else {
                    slidingMenu.toggle();//关闭菜单
                }
                break;
            case R.id.main_city_location:
                Intent intent = new Intent(ConsumerMainActivity.this, CityPositionActivity.class);
                startActivityForResult(intent, CITY_GET_CODE); //requestCode
                break;
            case R.id.main_gatetogate_layout:
                startActivity(new Intent(ConsumerMainActivity.this, GateToGateActivity.class));
                break;
            case R.id.main_shunfengche_layout:
                startActivity(new Intent(ConsumerMainActivity.this, ShunFengCheActivity.class));
                break;
            case R.id.main_sellthing_layout:
                startActivity(new Intent(ConsumerMainActivity.this, SellThingActivity.class));
                break;
            case R.id.main_find_labour_layout:
                startActivity(new Intent(ConsumerMainActivity.this, FindLabourActivity.class));
                break;
            case R.id.main_my_orders:
                startActivity(new Intent(ConsumerMainActivity.this, MyOrdersActivity.class));
                break;
            case R.id.main_my_messages:
                startActivity(new Intent(ConsumerMainActivity.this, MessagesActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CITY_GET_CODE) {
                tvCityName.setText(data.getStringExtra("city"));
                data.getDoubleExtra("lng", 0.0);
                data.getDoubleExtra("lat", 0.0);
                editorLocation.putString("city", data.getStringExtra("city"));
                editorLocation.commit();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class AutoViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            radio_group.check(position % subLists.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        APP.getInstance().stopLocationClient(); //关闭定位
        //关闭 mqtt 服务
        if (MqttService.wasStarted()){
            LLog.i("===ConsumerMainActivity===onDestroy()==stopService()");
            stopService(new Intent(this,MqttService.class));
        }
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            EUtil.showToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ActivityHolder.getInstance().finishAllActivity();
        }
    }

}
