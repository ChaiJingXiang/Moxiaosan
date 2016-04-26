package com.moxiaosan.both.carowner.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.moxiaosan.both.R;
import com.moxiaosan.both.utils.AvatarUploader;
import com.moxiaosan.both.utils.FileUploader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.image.BitmapUtils;
import com.utils.ui.base.BaseActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.BecomeApply;
import consumer.model.PhoneCode;
import picture.PictureGalleryActivity;

public class BecomeCarOwnerActivity extends BaseActivity implements View.OnClickListener,FileUploader.OnFileUploadListener,IApiCallback{

    private EditText etName,etCardNumber,etCarNumber,etDriverNumber,etCarType;
    private ImageView imgCard,imgDriver,imgCar,imgOther;

    private final static int REQ_CARD_NUMBER=1;
    private final static int REQ_DRIVER_NUMBER=2;
    private final static int REQ_CAR_NUMBER=3;
    private final static int REQ_OTHER_NUMBER=4;
    private int type =1;

    private ArrayList<String> imageList;

    // 变量
    private String cardUrl,driveUrl,carUrl,otherUrl ; // 网络文件地址
    private String mLocalFilePath; // 本地文件地址
    private AvatarUploader mUploader; // 文件上传对象

    private final static int UPLOAD_OK = 1;
    private final static int UPLOAD_FAIL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_becomecarowner_layout);
        showActionBar(true);
        setActionBarName("申请资料");

        etName =(EditText)findViewById(R.id.nameId);
        etCardNumber =(EditText)findViewById(R.id.cardId);
        etCarNumber =(EditText)findViewById(R.id.carNumerId);
        etDriverNumber =(EditText)findViewById(R.id.driveNumerId);
        etCarType =(EditText)findViewById(R.id.carTypeId);

        imgCard =(ImageView)findViewById(R.id.imgCardId);
        imgCard.setOnClickListener(this);
        imgDriver =(ImageView)findViewById(R.id.imgDriverId);
        imgDriver.setOnClickListener(this);
        imgCar =(ImageView)findViewById(R.id.imgCarId);
        imgCar.setOnClickListener(this);
        imgOther =(ImageView)findViewById(R.id.imgOtherId);
        imgOther.setOnClickListener(this);

        findViewById(R.id.sureId).setOnClickListener(this);

        // 本地文件地址
        mLocalFilePath = "";
        // 网络文件地址
        cardUrl = "";
        driveUrl ="";
        carUrl ="";
        otherUrl ="";

        // 文件上传对象
        mUploader = new AvatarUploader(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imgCardId:
                startActivityForResult(new Intent(BecomeCarOwnerActivity.this, PictureGalleryActivity.class).putExtra("maxNum", 1), REQ_CARD_NUMBER);

                break;
            case R.id.imgDriverId:
                startActivityForResult(new Intent(BecomeCarOwnerActivity.this, PictureGalleryActivity.class).putExtra("maxNum", 1), REQ_DRIVER_NUMBER);

                break;
            case R.id.imgCarId:
                startActivityForResult(new Intent(BecomeCarOwnerActivity.this, PictureGalleryActivity.class).putExtra("maxNum", 1), REQ_CAR_NUMBER);

                break;
            case R.id.imgOtherId:
                startActivityForResult(new Intent(BecomeCarOwnerActivity.this, PictureGalleryActivity.class).putExtra("maxNum", 1), REQ_OTHER_NUMBER);

                break;

            case R.id.sureId:
                if(!TextUtils.isEmpty(etName.getText().toString().trim())){
                    if(!TextUtils.isEmpty(etCardNumber.getText().toString().trim())){
                        if(!TextUtils.isEmpty(etCarNumber.getText().toString().trim())){
                            if(!TextUtils.isEmpty(etDriverNumber.getText().toString().trim())){
                                if(!TextUtils.isEmpty(etCarType.getText().toString().trim())){
                                    if(!TextUtils.isEmpty(cardUrl)){
                                        if(!TextUtils.isEmpty(driveUrl)){
                                            if(!TextUtils.isEmpty(carUrl)){

                                                showLoadingDialog();
                                                CarReqUtils.apply(this,this,null,new BecomeApply(),"Apply",true,
                                                        StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                                                putValue("surname",etName.getText().toString().toString()).
                                                                putValue("platenum",etCarNumber.getText().toString().trim()).
                                                                putValue("drivingnum",etDriverNumber.getText().toString().trim()).
                                                                putValue("cartype",etCarType.getText().toString().trim()).
                                                                putValue("cardimg",cardUrl).putValue("drivingimg",driveUrl).
                                                                putValue("carimg",carUrl).putValue("otherimg",otherUrl).createMap()));

                                            }else{
                                                EUtil.showToast("请先上传车辆照片");
                                            }
                                        }else{
                                            EUtil.showToast("请先上传行驶证照片");
                                        }
                                    }else{
                                        EUtil.showToast("请先上传身份证照片");
                                    }

                                }else{
                                    EUtil.showToast("车辆类型不能为空");
                                }
                            }else{
                                EUtil.showToast("行驶证编号不能为空");
                            }

                        }else{
                            EUtil.showToast("车牌号不能为空");
                        }
                    }else{
                        EUtil.showToast("身份证号不能为空");
                    }
                }else{
                    EUtil.showToast("车主姓名不能为空");
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(data !=null){
                imageList = new ArrayList<String>();
                imageList.addAll(data.getStringArrayListExtra("imageList"));

                if(requestCode ==REQ_CARD_NUMBER){
                    type =REQ_CARD_NUMBER;
                    Log.i("info", imageList.get(0) + "");
                    Bitmap bitmap =BitmapFactory.decodeFile(imageList.get(0));
                    imgCard.setImageBitmap(bitmap);
                    mUploader.start(imageList.get(0));
                }else if(requestCode ==REQ_DRIVER_NUMBER){
                    type =REQ_DRIVER_NUMBER;
                    Log.i("info",imageList.get(0)+"");
                    Bitmap bitmap =BitmapFactory.decodeFile(imageList.get(0));
                    imgDriver.setImageBitmap(bitmap);
                    mUploader.start(imageList.get(0));
                }else if(requestCode ==REQ_CAR_NUMBER){
                    type =REQ_CAR_NUMBER;
                    Log.i("info",imageList.get(0)+"");
                    Bitmap bitmap =BitmapFactory.decodeFile(imageList.get(0));
                    imgCar.setImageBitmap(bitmap);
                    mUploader.start(imageList.get(0));
                }else{
                    type =REQ_OTHER_NUMBER;
                    Log.i("info",imageList.get(0)+"");
                    Bitmap bitmap =BitmapFactory.decodeFile(imageList.get(0));
                    imgOther.setImageBitmap(bitmap);
                    mUploader.start(imageList.get(0));
                }
            }

        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onStarted() {
        showLoadingDialog();
    }

    @Override
    public void onUpdate(int value) {

    }

    @Override
    public void onCanceled() {

    }

    @Override
    public void onSuccess(String fileUrl) {
        if(type ==REQ_CARD_NUMBER){
            cardUrl =fileUrl;
        }else if(type ==REQ_DRIVER_NUMBER){
            driveUrl =fileUrl;
        }else if(type ==REQ_CAR_NUMBER){
            carUrl =fileUrl;
        }else{
            otherUrl =fileUrl;
        }
        handler.sendEmptyMessage(UPLOAD_OK);
    }

    @Override
    public void onError(int code, String message) {
        handler.sendEmptyMessage(UPLOAD_FAIL);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case UPLOAD_OK:
                    dismissLoadingDialog();
//        EUtil.showToast("上传成功");

                    Toast.makeText(BecomeCarOwnerActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

                    break;

                case UPLOAD_FAIL:

                    Toast.makeText(BecomeCarOwnerActivity.this, "上传失败，稍后重试", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };


    @Override
    public void onData(Object output, Object input) {

        if(output!=null){
            dismissLoadingDialog();
            if (output instanceof BecomeApply){
                BecomeApply apply= (BecomeApply) output;
                EUtil.showToast(apply.getErr());
                if (apply.getRes() == 0){
                    SharedPreferences sp =getSharedPreferences("request",Activity.MODE_PRIVATE);

                    SharedPreferences.Editor editor =sp.edit();
                    editor.putBoolean("carer",true);
                    editor.commit();

                    finish();

                }
            }
        }else{
            EUtil.showToast("网络错误，请稍后重试");
        }
    }
}
