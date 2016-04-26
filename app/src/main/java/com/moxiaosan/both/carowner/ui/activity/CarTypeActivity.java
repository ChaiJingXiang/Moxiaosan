package com.moxiaosan.both.carowner.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.customView.sortlistview.CharacterParser;
import com.moxiaosan.both.common.ui.customView.sortlistview.SideBar;
import com.moxiaosan.both.common.ui.customView.sortlistview.SortAdapter;
import com.moxiaosan.both.common.ui.customView.sortlistview.SortModel;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.pinyin.PinyinComparator;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.BecomeApply;
import consumer.model.CarBrand;

/**
 * Created by chris on 16/3/3.
 */
public class CarTypeActivity extends BaseActivity implements IApiCallback{

    private EditText etCarType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_cartype_layout);

        showActionBar(true);
        setActionBarName("车的品牌");

        etCarType =(EditText)findViewById(R.id.carTypeId);

        findViewById(R.id.sureId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etCarType.getText().toString().trim())){

                    showLoadingDialog();

                    CarReqUtils.modifCarBrand(CarTypeActivity.this,CarTypeActivity.this,null,new CarBrand(),"CarBrand",true,
                            StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                    putValue("brand",etCarType.getText().toString().trim()).createMap()));
                }else{

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
        if(output!=null){
            dismissLoadingDialog();
            if (output instanceof CarBrand){
                CarBrand brand= (CarBrand) output;
                EUtil.showToast(brand.getErr());
                if (brand.getRes() == 0){

                    finish();

                }
            }
        }else{
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }
    }
}
