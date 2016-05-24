package com.moxiaosan.both.consumer.ui.activity.personalcenter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.activity.ModifyNickNameActivity;
import com.moxiaosan.both.common.ui.activity.ModifyPasswordActivity;
import com.moxiaosan.both.common.ui.activity.ModifyPhoneActivity;
import com.moxiaosan.both.consumer.ui.customView.BottomDialog;
import com.moxiaosan.both.consumer.ui.customView.ImageUtils;
import com.moxiaosan.both.utils.AvatarUploader;
import com.moxiaosan.both.utils.FileUploader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.image.RoundImageView;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.UserReqUtil;
import consumer.model.UpdateHead;
import consumer.model.Userinfo;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {
    private BottomDialog bottomDialog;
    private RoundImageView userPhoto;
    private TextView tvName, tvPhone;

    // 变量
    private String mFileUrl; // 头像网络文件地址
    private String mLocalFilePath; // 本地文件地址
    private AvatarUploader mUploader; // 文件上传对象

    private final static int UPLOAD_OK = 1;
    private final static int UPLOAD_FAIL = 2;

    public static boolean isUploadingPic = false; //是否在上传图片  默认不在上传

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        showActionBar(true);
        setActionBarName("个人资料");
        initView();

        initVars();
    }

    @Override
    public void onResume() {
        super.onResume();
//        LLog.i("onResume");
        if (!isUploadingPic) {
            UserReqUtil.userinfo(this, iApiCallback, null, new Userinfo(), "LeftFragment", true, "username=" + AppData.getInstance().getUserEntity().getUsername());
            showLoadingDialog();
        }

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
            if (output instanceof Userinfo) {
                Userinfo userinfo = (Userinfo) output;
                if (!TextUtils.isEmpty(userinfo.getData().getHeadportrait())) {
                    ImageLoader.getInstance().displayImage(userinfo.getData().getHeadportrait(), userPhoto);
                }
                tvName.setText(userinfo.getData().getNickname());
                tvPhone.setText(userinfo.getData().getUsername());
            }
        }
    };

    private void initView() {
        findViewById(R.id.person_info_photo_modify_layout).setOnClickListener(this);
        findViewById(R.id.person_info_nick_modify_layout).setOnClickListener(this);
        findViewById(R.id.person_info_phone_mdify_layout).setOnClickListener(this);
        findViewById(R.id.person_info_password_modify_layout).setOnClickListener(this);
        userPhoto = (RoundImageView) findViewById(R.id.person_info_user_photo);
        tvName = (TextView) findViewById(R.id.person_info_nick_modify_name_txt);
        tvPhone = (TextView) findViewById(R.id.person_info_nick_modify_phone_txt);
    }

    // 初始化变量
    private void initVars() {

        // 本地文件地址
        mLocalFilePath = "";
        // 网络文件地址
        mFileUrl = "";

        // 文件上传对象
        mUploader = new AvatarUploader(new FileUploader.OnFileUploadListener() {
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
                EUtil.showLongToast("上传失败，稍后重试");
            }
        });
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_OK:
                    if (isLoadingDialogShowing()){
                        dismissLoadingDialog();
                    }
                    EUtil.showToast("上传成功");
                    isUploadingPic = false;  //不在上传
                    UserReqUtil.updatehead(PersonInfoActivity.this, iApiCallback, null, new UpdateHead(), "PersonInfoActivity", true,
                            StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername())
                                    .putValue("headportrait", mFileUrl).createMap())
                    );
                    break;

                case UPLOAD_FAIL:
                    EUtil.showToast("上传失败，稍后重试");
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_info_photo_modify_layout:
                showBottomDialog();
                break;
            case R.id.person_info_nick_modify_layout:
                startActivity(new Intent(PersonInfoActivity.this, ModifyNickNameActivity.class).putExtra("nickname", tvName.getText().toString().trim()));
                break;
            case R.id.person_info_phone_mdify_layout:
                startActivity(new Intent(PersonInfoActivity.this, ModifyPhoneActivity.class).putExtra("phone", tvPhone.getText().toString().trim()));
                break;
            case R.id.person_info_password_modify_layout:
                startActivity(new Intent(PersonInfoActivity.this, ModifyPasswordActivity.class));
                break;
        }
    }

    //底部选择照片dialog
    protected void showBottomDialog() {
        if (null == bottomDialog) {
            bottomDialog = new BottomDialog(PersonInfoActivity.this);
        }

        Window window = bottomDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        bottomDialog.show();

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = bottomDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        bottomDialog.getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            // 拍照获取图片
            case ImageUtils.GET_IMAGE_BY_CAMERA:
                // uri传入与否影响图片获取方式,以下二选一
                // 方式一,自定义Uri(ImageUtils.imageUriFromCamera),用于保存拍照后图片地址
                if (ImageUtils.imageUriFromCamera != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
//				iv.setImageURI(ImageUtils.imageUriFromCamera);

                    // 对图片进行裁剪
                    ImageUtils.cropImage(this, ImageUtils.imageUriFromCamera);
                    break;
                }

                break;
            // 手机相册获取图片
            case ImageUtils.GET_IMAGE_FROM_PHONE:
                if (data != null && data.getData() != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
                    userPhoto.setImageURI(data.getData());
                    mLocalFilePath = getPath(data.getData());
//                    Log.i("info+++",mLocalFilePath);
                    mUploader.start(mLocalFilePath);
                    isUploadingPic = true; //开始上传
                    showLoadingDialog();
                    // 对图片进行裁剪
//                    ImageUtils.cropImage(this, data.getData());
                }
                break;
            // 裁剪图片后结果
            case ImageUtils.CROP_IMAGE:
//                LLog.i("onActivityResult");
                if (ImageUtils.cropImageUri != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩等)
                    userPhoto.setImageURI(ImageUtils.cropImageUri);

                    mLocalFilePath = getPath(ImageUtils.cropImageUri);

//                    Log.i("info+++",mLocalFilePath);
                    mUploader.start(mLocalFilePath);
                    isUploadingPic = true; //开始上传
                    showLoadingDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
