package com.moxiaosan.both.consumer.ui.activity.leftmenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.BusinessMainActivity;
import com.moxiaosan.both.carowner.ui.activity.GPSSafeCenterActivity;
import com.moxiaosan.both.common.ui.activity.LoginActivity;
import com.moxiaosan.both.consumer.ui.activity.ConsumerMainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.file.FileConstants;
import com.utils.file.FileHelper;
import com.utils.ui.base.ActivityHolder;
import com.utils.ui.base.BaseActivity;

import java.io.File;
import java.math.BigDecimal;

import consumer.model.obj.RespUserInfo;

/**
 *
 */
public class UserSettingActivity extends BaseActivity implements View.OnClickListener {
    private String count;
    private TextView tvCLeanNum;
    private RadioGroup radioGroup;
    private RadioButton consumerButton;
    private RadioButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        showActionBar(true);
        setActionBarName("设置");

        findViewById(R.id.setting_clear_cache_layout).setOnClickListener(this);
        findViewById(R.id.setting_logout_account).setOnClickListener(this);
        tvCLeanNum= (TextView) findViewById(R.id.setting_cache_num);

        button =(RadioButton)findViewById(R.id.setting_radio_btn_user);

        radioGroup =(RadioGroup)findViewById(R.id.setting_radio_group);
        consumerButton =(RadioButton)findViewById(R.id.setting_radio_btn_user);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.setting_radio_btn_car){

                    if(AppData.getInstance().getUserEntity().getType() !=1){
                        ExitDialog dialog = new ExitDialog(UserSettingActivity.this,3);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }else{

                        button.setChecked(true);
                        EUtil.showToast("你不是车主,不可切换车主身份");
                    }


                }
            }
        });
        setCount();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_clear_cache_layout:
                ExitDialog dialog2 = new ExitDialog(UserSettingActivity.this,1);
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.show();
                break;
            case R.id.setting_logout_account:
                ExitDialog dialog = new ExitDialog(UserSettingActivity.this,0);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
    }

    //清除缓存，退出登录 dialog
    class ExitDialog extends AlertDialog {

        int index;
        public ExitDialog(Context context,int indext) {
            super(context);
            this.index=indext;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);
            TextView tv=(TextView)findViewById(R.id.tvDialogActivity);
            if (index == 1){  //清除缓存

                tv.setText("是否清除本地缓存");
            }else if(index ==3){
                tv.setText("切换为车主身份");
            }

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (index == 1){  //清除缓存
                        clearAllCache(getApplicationContext());
                        FileHelper.deleteDirectoryAllFile(FileConstants.getApiSaveFilePath());
                        FileHelper.deleteDirectoryAllFile(FileConstants.getImageSaveFilePath());
                        setCount();
                        EUtil.showToast("完成清理");
                    }else if(index ==0){  // index=0  退出登录
//                        reqData();
                        RespUserInfo respUserInfo=null;
                        AppData.getInstance().saveUserEntity(respUserInfo);
                        ActivityHolder.getInstance().finishAllActivity();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                        System.exit(-1);
                    }else{

                        if(AppData.getInstance().getUserEntity().getType()==2){
                            startActivity(new Intent(UserSettingActivity.this, GPSSafeCenterActivity.class));
                            RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                            userInfo.setUserType(2);
                            AppData.getInstance().saveUserEntity(userInfo);
                            finish();
                            ActivityHolder.getInstance().pop(new ConsumerMainActivity());

                        }else{

                            startActivity(new Intent(UserSettingActivity.this, BusinessMainActivity.class));
                            RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                            userInfo.setUserType(3);
                            AppData.getInstance().saveUserEntity(userInfo);
                            finish();
                            ActivityHolder.getInstance().pop(new ConsumerMainActivity());

                        }
                    }
                }
            });

            findViewById(R.id.setting_exit_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(index ==3){
                        consumerButton.setChecked(true);
                        dismiss();
                    }else{
                        dismiss();
                    }

                }
            });
        }
    }

    public static  void clearAllCache(Context context) {
        ImageLoader.getInstance().clearDiscCache();
        ImageLoader.getInstance().clearMemoryCache();
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private void setCount(){
        try{
            count=getTotalCacheSize(getApplicationContext());
            tvCLeanNum.setText(count);
        }catch (Exception e){
            tvCLeanNum.setText("0K");
//            e.printStackTrace();
        }
    }
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "T";
    }
}
