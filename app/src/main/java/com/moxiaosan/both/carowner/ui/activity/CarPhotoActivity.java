package com.moxiaosan.both.carowner.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moxiaosan.both.R;
import com.moxiaosan.both.utils.AvatarUploader;
import com.moxiaosan.both.utils.FileUploader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.ui.base.BaseActivity;

import java.util.ArrayList;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.UpdateHead;
import consumer.model.obj.RespUserInfo;
import picture.PictureGalleryActivity;

/**
 * Created by chris on 16/3/3.
 */
public class CarPhotoActivity extends BaseActivity implements FileUploader.OnFileUploadListener,IApiCallback {

    private ImageView imageView;
    private ArrayList<String> imageList;
    private String mFileUrl; // 网络文件地址
    private String mLocalFilePath; // 本地文件地址
    private AvatarUploader mUploader; // 文件上传对象

    private final static int UPLOAD_OK = 1;
    private final static int UPLOAD_FAIL = 2;
    private ImageView imgCar;
    private LinearLayout layoutShowCar;
    private String carImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_carphoto_layout);

        showActionBar(true);
        setActionBarName("机车照片");

        Intent intent = getIntent();
        carImg = intent.getStringExtra("carImg");

        Log.i("info",carImg);

        imageView = (ImageView) findViewById(R.id.photoId);

        imgCar = (ImageView) findViewById(R.id.showCarPhotoId);
        layoutShowCar = (LinearLayout) findViewById(R.id.show_CarPhoto_layout);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CarPhotoActivity.this, PictureGalleryActivity.class).putExtra("maxNum", 1), 111);

            }
        });

        // 本地文件地址
        mLocalFilePath = "";
        // 网络文件地址
        mFileUrl = "";

        // 文件上传对象
        mUploader = new AvatarUploader(this);

        if (!TextUtils.isEmpty(carImg)) {
            ImageLoader.getInstance().displayImage(carImg, imgCar);
        } else {
            layoutShowCar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageList = new ArrayList<String>();
                imageList.addAll(data.getStringArrayListExtra("imageList"));

                if (requestCode == 111) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imageList.get(0));
                    imageView.setImageBitmap(bitmap);

                    Log.i("info+++", imageList.get(0));

                    mUploader.start(imageList.get(0));
                }
            }
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case UPLOAD_OK:
                    showLoadingDialog();
                    CarReqUtils.modifCarPic(CarPhotoActivity.this,CarPhotoActivity.this,null,new UpdateHead(), "UpCarpic", true, StringUrlUtils.geturl(hashMapUtils.putValue("username",AppData.getInstance().getUserEntity().getUsername()).
                            putValue("picurl",mFileUrl).createMap()));

//                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
//                    userInfo.setCarimg(mFileUrl);
//                    AppData.getInstance().saveUserEntity(userInfo);
                    break;
                case UPLOAD_FAIL:
                    EUtil.showToast("上传失败，稍后重试");
                    break;
            }
        }
    };

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
        mFileUrl = fileUrl;
        handler.sendEmptyMessage(UPLOAD_OK);

    }

    @Override
    public void onError(int code, String message) {
        handler.sendEmptyMessage(UPLOAD_FAIL);

    }

    @Override
    public void onData(Object output, Object input) {

        if(output !=null){
            if (output instanceof UpdateHead) {
                dismissLoadingDialog();
                UpdateHead head = (UpdateHead) output;
                EUtil.showToast(head.getErr());
                if (head.getRes() == 0) {
                    finish();
                }
            }
        }
    }
}
