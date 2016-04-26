package com.moxiaosan.both.consumer.ui.activity;

import android.os.Bundle;

import com.moxiaosan.both.R;
import com.utils.ui.base.BaseActivity;

public class ToAppraiseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_appraise);
        showActionBar(true);
        setActionBarName("评价");
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }
}
