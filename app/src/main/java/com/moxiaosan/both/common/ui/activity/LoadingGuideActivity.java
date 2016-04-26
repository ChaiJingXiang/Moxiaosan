package com.moxiaosan.both.common.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moxiaosan.both.R;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LoadingGuideActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private List<ImageView> imageViewList; // ViewPager要显示的内容列表
    private ImageView startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_guide);
        boolean isFrist = readShared("isFrist");
        Intent intent = new Intent();
        if (!isFrist) {
            intent.setClass(this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_loading_guide);
            viewPager = (ViewPager) findViewById(R.id.loading_guide_viewPager);
            startBtn = (ImageView) findViewById(R.id.loading_guide_start);
            startBtn.setOnClickListener(this);
            initViewPagers();
            viewPager.setAdapter(new ImagePagerAdapter());

            // 设置页面滑动事件
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) { // 确定选择页面时调用的方法

                    if (position == imageViewList.size() - 1)
                        startBtn.setVisibility(View.VISIBLE);
                    else
                        startBtn.setVisibility(View.GONE);
                }

                @Override
                public void onPageScrolled(int position, float offset,
                                           int offsetPixes) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }


    // 被始化ViewPager显示的内容
    private void initViewPagers() {
        imageViewList = new ArrayList<ImageView>();
        int[] images = {R.mipmap.guide_one, R.mipmap.guide_two,
                R.mipmap.guide_three, R.mipmap.guide_four};

        ImageView imageView;

        for (int i = 0; i < images.length; i++) {
            imageView = new ImageView(this);
            imageView.setImageResource(images[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY); // 设置图片拉伸到控件大小
            imageViewList.add(imageView); // 将ImageView实例增加到List列表中
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loading_guide_start:
                Intent intent = new Intent(LoadingGuideActivity.this, LoginActivity.class);
                writeShared("isFrist", false);
                startActivity(intent);
                finish();
                break;
        }
    }

    class ImagePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 将当位置的View增加到容器中(ViewPager)
            container.addView(imageViewList.get(position));

            return imageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object item) {
            return view == item;
        }
    }

    // 读取SharedPreference共享参数
    private boolean readShared(String key) {

        SharedPreferences preferences = getSharedPreferences("key",
                MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, true);

        return value;

    }

    // 写入Sharepreference
    private void writeShared(String key, Boolean value) {

        // 获取SharedPreference的对象,并指定共享参数的文件名和读取模式
        SharedPreferences preferences = getSharedPreferences("key",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();// 获取写的对象
        editor.putBoolean(key, value);

        editor.commit();// 提交操作
    }
}
