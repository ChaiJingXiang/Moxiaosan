package com.utils.ui.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.moxiaosan.commonlibrary.R;

import consumer.HashMapUtils;

/**
 * @Filename BaseActivity.java
 * @Author fengyongqiang
 * @Date 2016-2-15
 * @description
 */
public abstract class BaseActivity extends Activity {

    public static final String TAG = "BaseActivity";
    protected EProgressDialog mProgressDialog;
    protected boolean isDestroy = false;
    protected View viewBar;
    protected HashMapUtils hashMapUtils = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHolder.getInstance().push(this);
        isDestroy = false;

        initActionBar();
        showActionBar(false);

        hashMapUtils =new HashMapUtils();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        ActionBar actionbar = getActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);
        View mCustomView = getLayoutInflater().inflate(R.layout.view_action_bar, null);
        actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));

    }

    /**
     * 控制ActionBar的显示/隐藏
     * @param isShow
     */
    public void showActionBar(boolean isShow) {
        if (isShow) {
            getActionBar().show();
        } else {
            getActionBar().hide();
        }
    }

    /**
     * 设置ActionBar的标头
     * @param titleName
     */
    public void setActionBarName(String titleName) {
        viewBar = getActionBar().getCustomView();
        ((TextView) viewBar.findViewById(R.id.title_name_textview)).setText(titleName);
        ((ImageButton) viewBar.findViewById(R.id.title_back_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 返回按钮触发事件的特殊处理
     */
    public ImageButton getActionBarBack(){
        return (ImageButton) viewBar.findViewById(R.id.title_back_btn);
    }

    /**
     * 获取ActionBar右边的图片
     * @param imageBtnId
     * @return
     */
    public ImageButton getActionBarRightImage(int imageBtnId){
        ImageButton btnRight = (ImageButton)viewBar.findViewById(R.id.title_right_btn);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setBackgroundResource(imageBtnId);
        return btnRight;
    }

    /**
     * 获取ActionBar右边的文字
     * @param text
     * @return
     */
    public TextView getActionBarRightTXT(String text){
        TextView tvRight = (TextView)viewBar.findViewById(R.id.title_right_textview);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(text);
        return tvRight;
    }


    //*****************actionbar ----end

    //显示普通的无文字loading对话框
    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    //按字符串资源id显示对话框
    protected void showLoadingDialog(int resId) {
        showLoadingDialog(getString(resId));
    }

    //显示字符串loading对话框
    protected void showLoadingDialog(String message) {
        if (null == mProgressDialog) {
            mProgressDialog = new EProgressDialog(BaseActivity.this);
//            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialogInterface) {
//                    //KeelLog.d(TAG, "mProgressDialog.onCancel");
//                    //BaseActivity.this.finish();
//                    onLoadingDialogCancel();
//                }
//            });
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    /**
     * 取消loading对话框动作回调，如果想要处理，就继承该函数，默认关闭当前页面
     */
    public void onLoadingDialogCancel() {
//        finish();
    }

    /**
     * 当前loading对话框是否显示
     *
     * @return
     */
    public boolean isLoadingDialogShowing() {
        if (mProgressDialog != null) {
            return mProgressDialog.isShowing();
        } else {
            return false;
        }
    }

    // 加载框是否可以取消
    protected void showLoadingDialog(String message, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        if (null == mProgressDialog) {
            mProgressDialog = new EProgressDialog(BaseActivity.this);
            mProgressDialog.setCancelable(cancelable);
            if (cancelable) {
                mProgressDialog.setOnCancelListener(cancelListener);
            }
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }


    //取消显示loading对话框
    protected void dismissLoadingDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * @author fengyongqinag
     * @decs 分享时回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean hasDestroy() {
        return isDestroy;
    }

    @Override
    protected void onDestroy() {
        ActivityHolder.getInstance().pop(this);
        isDestroy = true;
        super.onDestroy();
    }

    /**
     * 获得加载数据的地址
     *
     * @return
     */
    protected abstract String getUrl(String nameUrl);


}
