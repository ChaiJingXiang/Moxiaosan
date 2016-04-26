package com.moxiaosan.both.consumer.ui.activity.leftmenu;

import android.os.Bundle;

import com.moxiaosan.both.R;
import com.utils.ui.base.BaseActivity;

public class ChangeToHostActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_to_host);
        showActionBar(true);
        setActionBarName("成为车主");
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }
}
