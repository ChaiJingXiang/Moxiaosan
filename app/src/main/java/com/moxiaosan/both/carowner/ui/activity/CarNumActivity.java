package com.moxiaosan.both.carowner.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.moxiaosan.both.R;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.UpdatePlatenum;

public class CarNumActivity extends BaseActivity implements IApiCallback {
    private EditText etCarNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_num);
        showActionBar(true);
        setActionBarName("车牌号");
        etCarNum = (EditText) findViewById(R.id.carNumId);
        etCarNum.setText(getIntent().getStringExtra("carNum"));
        etCarNum.setSelection(etCarNum.getText().toString().length());
        findViewById(R.id.car_Num_Sure_Id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etCarNum.getText().toString().trim())) {

                    showLoadingDialog();

                    CarReqUtils.updatePlatenum(CarNumActivity.this, CarNumActivity.this, null, new UpdatePlatenum(), "CarNumActivity", true,
                            StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                    putValue("platenum", etCarNum.getText().toString().trim()).createMap()));
                } else {

                    EUtil.showToast("请输入车的品牌");
                }

            }
        });
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onData(Object output, Object input) {
        if (isLoadingDialogShowing()) {
            dismissLoadingDialog();
        }
        if (output == null) {
            return;
        }
        if (output instanceof UpdatePlatenum) {
            UpdatePlatenum updatePlatenum = (UpdatePlatenum) output;
            EUtil.showLongToast(updatePlatenum.getErr());
            if (updatePlatenum.getRes() == 0) {
                finish();
            }
        }
    }
}
