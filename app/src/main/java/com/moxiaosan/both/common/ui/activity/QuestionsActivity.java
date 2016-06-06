package com.moxiaosan.both.common.ui.activity;

import android.os.Bundle;

import com.moxiaosan.both.R;
import com.utils.ui.base.BaseActivity;

public class QuestionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        showActionBar(true);
        setActionBarName("常见问题");
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }
}
