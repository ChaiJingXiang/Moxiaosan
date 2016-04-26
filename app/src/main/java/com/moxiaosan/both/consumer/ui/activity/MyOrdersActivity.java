package com.moxiaosan.both.consumer.ui.activity;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.fragment.HavePayedOrderFrament;
import com.moxiaosan.both.consumer.ui.fragment.NoPayOrderFragment;
import com.utils.ui.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends BaseFragmentActivity implements View.OnClickListener {
    private TextView tvNoPay, tvHavePayed;
    private ViewPager viewPager;
    private ImageView cursor;
    private HavePayedOrderFrament havePayedOrderFrament = new HavePayedOrderFrament();
    private NoPayOrderFragment noPayOrderFragment = new NoPayOrderFragment();
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    OrderViewPagerAdapter orderViewPagerAdapter;
    PageChangeListener pageChangeListener;
    FragmentManager fragmentManager;

    private static int lineWidth = 0;         //选项卡下划线长度
    private static int offset = 0;            //偏移量手机屏幕宽度/4-选项卡长度）/2
    private static final int TAB_COUNT = 2;   //选项卡总数
    private int current_index = 0;            //当前显示的选项卡位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        showActionBar(true);
        setActionBarName("我的订单");
        initCursorImageView();
        initView();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private void initView() {

        fragmentList.add(noPayOrderFragment);
        fragmentList.add(havePayedOrderFrament);

        tvNoPay = (TextView) findViewById(R.id.my_orders_no_pay_txt);
        tvNoPay.setOnClickListener(this);
        tvHavePayed = (TextView) findViewById(R.id.my_orders_have_pay_txt);
        tvHavePayed.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.my_orders_viewpager);
        fragmentManager = getSupportFragmentManager();
        orderViewPagerAdapter = new OrderViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(orderViewPagerAdapter);
        pageChangeListener = new PageChangeListener();
        viewPager.addOnPageChangeListener(pageChangeListener);


    }

    private void initCursorImageView() {
        cursor = (ImageView) findViewById(R.id.my_orders_divider_img);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //获取图片宽度
        lineWidth = dm.widthPixels / 2;
        //获取屏幕宽度
        int screenWidth = dm.widthPixels;
        Matrix matrix = new Matrix();
        offset = (int) ((screenWidth / (float) TAB_COUNT - lineWidth) / 2);
        matrix.postTranslate(offset, 0);
        //设置初始位置
        cursor.setImageMatrix(matrix);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_orders_no_pay_txt:
                viewPager.setCurrentItem(0);
                tvNoPay.setTextColor(getResources().getColor(R.color.txt_333333));
                tvHavePayed.setTextColor(getResources().getColor(R.color.txt_999999));
                break;
            case R.id.my_orders_have_pay_txt:
                viewPager.setCurrentItem(1);
                tvHavePayed.setTextColor(getResources().getColor(R.color.txt_333333));
                tvNoPay.setTextColor(getResources().getColor(R.color.txt_999999));
                break;
        }
    }

    class OrderViewPagerAdapter extends FragmentStatePagerAdapter {


        public OrderViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {
        int one = offset * 2 + lineWidth;    //页卡1 -> 页卡2 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = new TranslateAnimation(one * current_index, one * position, 0, 0);
            animation.setFillAfter(true);
            animation.setDuration(300);
            cursor.startAnimation(animation);
            current_index = position;

            if (position == 0) {
                tvNoPay.setTextColor(getResources().getColor(R.color.txt_333333));
                tvHavePayed.setTextColor(getResources().getColor(R.color.txt_999999));
            } else if (position == 1) {
                tvHavePayed.setTextColor(getResources().getColor(R.color.txt_333333));
                tvNoPay.setTextColor(getResources().getColor(R.color.txt_999999));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
