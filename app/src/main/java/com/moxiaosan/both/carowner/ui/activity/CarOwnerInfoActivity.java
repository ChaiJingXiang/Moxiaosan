package com.moxiaosan.both.carowner.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.activity.ModifyNickNameActivity;
import com.moxiaosan.both.common.ui.activity.ModifyPasswordActivity;
import com.moxiaosan.both.common.ui.activity.ModifyPhoneActivity;
import com.moxiaosan.both.consumer.ui.activity.ConsumerMainActivity;
import com.moxiaosan.both.consumer.ui.customView.BottomDialog;
import com.moxiaosan.both.consumer.ui.customView.ImageUtils;
import com.moxiaosan.both.utils.AvatarUploader;
import com.moxiaosan.both.utils.FileUploader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.image.RoundImageView;
import com.utils.ui.base.ActivityHolder;
import com.utils.ui.base.BaseActivity;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.api.UserReqUtil;
import consumer.model.UpdateHead;
import consumer.model.Userinfo;
import consumer.model.obj.RespUserInfo;

/**
 * Created by chris on 16/3/2.
 */
public class CarOwnerInfoActivity extends BaseActivity implements View.OnClickListener, FileUploader.OnFileUploadListener, IApiCallback {

    private BottomDialog bottomDialog;
    private RoundImageView userPhoto;
    private TextView tvName, tvPhone, tvAddress, tvCarType;
    private TextView tvKim;

    // 变量
    private String mFileUrl; // 网络文件地址
    private String mLocalFilePath; // 本地文件地址
    private AvatarUploader mUploader; // 文件上传对象

    private final static int UPLOAD_OK = 1;
    private final static int UPLOAD_FAIL = 2;

    private RadioGroup radioGroup;
    private RadioButton consumerButton, carButton;
    private String carImg;

    private boolean isUploadingPic = false; //默认不在上传图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.b_personinfo_layout);

        showActionBar(true);
        setActionBarName("个人资料");

        initView();

        initVars();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isUploadingPic) {
            showLoadingDialog();
            CarReqUtils.personalInfo(this, this, null, new Userinfo(), "PersonalInfo", true,
                    StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));
        }


    }

    private void initView() {
        findViewById(R.id.person_info_photo_modify_layout).setOnClickListener(this);
        findViewById(R.id.person_info_nick_modify_layout).setOnClickListener(this);
        findViewById(R.id.person_info_phone_mdify_layout).setOnClickListener(this);
        findViewById(R.id.person_info_password_modify_layout).setOnClickListener(this);
        findViewById(R.id.person_info_address_layout).setOnClickListener(this);
        findViewById(R.id.person_info_car_type_layout).setOnClickListener(this);
        findViewById(R.id.person_info_car_photo_layout).setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);

        consumerButton = (RadioButton) findViewById(R.id.radioConsumer);
        carButton = (RadioButton) findViewById(R.id.radioCar);

        userPhoto = (RoundImageView) findViewById(R.id.person_info_user_photo);
        tvName = (TextView) findViewById(R.id.person_info_nick_modify_name_txt);
        tvPhone = (TextView) findViewById(R.id.person_info_nick_modify_phone_txt);

        tvAddress = (TextView) findViewById(R.id.person_info_address_txt);
        tvCarType = (TextView) findViewById(R.id.person_info_car_type_name_txt);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioConsumer) {

                    ExitDialog dialog = new ExitDialog(CarOwnerInfoActivity.this, 2);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }
            }
        });
    }

    // 初始化变量
    private void initVars() {

        // 本地文件地址
        mLocalFilePath = "";
        // 网络文件地址
        mFileUrl = "";

        // 文件上传对象
        mUploader = new AvatarUploader(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_info_photo_modify_layout:
                showBottomDialog();
                break;
            case R.id.person_info_nick_modify_layout:
                startActivity(new Intent(CarOwnerInfoActivity.this, ModifyNickNameActivity.class).putExtra("nickname", tvName.getText().toString().trim()));
                break;
            case R.id.person_info_phone_mdify_layout:
                startActivity(new Intent(CarOwnerInfoActivity.this, ModifyPhoneActivity.class).putExtra("phone", tvPhone.getText().toString().trim()));
                break;
            case R.id.person_info_password_modify_layout:
                startActivity(new Intent(CarOwnerInfoActivity.this, ModifyPasswordActivity.class));
                break;
            case R.id.person_info_address_layout:
                startActivity(new Intent(CarOwnerInfoActivity.this, AddressActivity.class));
                break;

            case R.id.person_info_car_type_layout:
                startActivity(new Intent(CarOwnerInfoActivity.this, CarTypeActivity.class));
                break;


            case R.id.person_info_car_photo_layout:
                startActivity(new Intent(CarOwnerInfoActivity.this, CarPhotoActivity.class).putExtra("carImg", carImg));
                break;

            case R.id.close:

                ExitDialog dialog2 = new ExitDialog(CarOwnerInfoActivity.this, 1);
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.show();

                break;
        }
    }

    //底部选择照片dialog
    protected void showBottomDialog() {
        if (null == bottomDialog) {
            bottomDialog = new BottomDialog(CarOwnerInfoActivity.this);
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
                    // iv.setImageURI(data.getData());

                    // 对图片进行裁剪
                    ImageUtils.cropImage(this, data.getData());
                }
                break;
            // 裁剪图片后结果
            case ImageUtils.CROP_IMAGE:
                if (ImageUtils.cropImageUri != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩等)
                    userPhoto.setImageURI(ImageUtils.cropImageUri);

                    mLocalFilePath = getPath(ImageUtils.cropImageUri);

//                    Log.i("info+++",mLocalFilePath);
                    mUploader.start(mLocalFilePath);
                    isUploadingPic = true;
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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case UPLOAD_OK:
                    if (isLoadingDialogShowing()) {
                        dismissLoadingDialog();
                    }

                    EUtil.showToast("上传成功");
                    isUploadingPic = false;
                    UserReqUtil.updatehead(CarOwnerInfoActivity.this, CarOwnerInfoActivity.this, null, new UpdateHead(), "UpdateHead", true,
                            StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).
                                    putValue("headportrait", mFileUrl).createMap()));

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setHeadportrait(mFileUrl);
                    AppData.getInstance().saveUserEntity(userInfo);

                    break;

                case UPLOAD_FAIL:

                    Toast.makeText(CarOwnerInfoActivity.this, "上传失败，稍后重试", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };


    //上传图片方法
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
//        Log.i("info----",code +","+message);
        handler.sendEmptyMessage(UPLOAD_FAIL);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onData(Object output, Object input) {

        if (output != null) {

            if (output instanceof UpdateHead) {
                dismissLoadingDialog();
                UpdateHead head = (UpdateHead) output;
                EUtil.showToast(head.getErr());
                if (head.getRes() == 0) {
//                    Toast.makeText(CarOwnerInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            if (output instanceof Userinfo) {
                if (output instanceof Userinfo) {
                    Userinfo userinfo = (Userinfo) output;
                    dismissLoadingDialog();
                    if (userinfo.getRes() == 0) {
                        if (!TextUtils.isEmpty(userinfo.getData().getHeadportrait())) {
                            ImageLoader.getInstance().displayImage(userinfo.getData().getHeadportrait(), userPhoto);
                        }
                        tvName.setText(userinfo.getData().getNickname());
                        tvPhone.setText(userinfo.getData().getContact());
                        tvAddress.setText(userinfo.getData().getAddress());
                        tvCarType.setText(userinfo.getData().getCarbrand());

                        carImg = userinfo.getData().getCarimg();
                    }
                }
            }

        } else {
            dismissLoadingDialog();
            EUtil.showToast("网络错误，请稍后重试");
        }

    }

    //退出登录 dialog
    class ExitDialog extends AlertDialog {

        int index;

        public ExitDialog(Context context, int index) {
            super(context, index);
            this.index = index;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);

            TextView textView = (TextView) findViewById(R.id.tvDialogActivity);
            if (index == 2) {

                textView.setText("是否切换为用户身份");

            } else {

                textView.setText("您确定要退出吗");

            }

            ViewGroup.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (index == 1) {
                        ActivityHolder.getInstance().finishAllActivity();
                        RespUserInfo respUserInfo = null;
                        AppData.getInstance().saveUserEntity(respUserInfo);
//                        System.exit(-1);
                    } else {
                        startActivity(new Intent(CarOwnerInfoActivity.this, ConsumerMainActivity.class));
                        RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                        userInfo.setUserType(1);
                        AppData.getInstance().saveUserEntity(userInfo);
                        finish();
                        ActivityHolder.getInstance().pop(new BusinessMainActivity());
                    }

                }
            });

            findViewById(R.id.setting_exit_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (index == 2) {
                        carButton.setChecked(true);
                    }
                }
            });
        }
    }

}
