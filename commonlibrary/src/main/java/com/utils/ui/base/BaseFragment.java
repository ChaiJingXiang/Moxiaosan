package com.utils.ui.base;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import consumer.HashMapUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected EProgressDialog mProgressDialog;
    protected boolean isDestroy = false;

    protected HashMapUtils hashMapUtils = null;


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hashMapUtils = new HashMapUtils();
        isDestroy = false;
    }

    // 友盟统计
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

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
            mProgressDialog = new EProgressDialog(getActivity());
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
    public void onLoadingDialogCancel(){
//        getActivity().finish();
    }

    /**
     * 当前loading对话框是否显示
     * @return
     */
    public boolean isLoadingDialogShowing() {
        if (mProgressDialog != null) {
            return mProgressDialog.isShowing();
        }
        else {
            return false;
        }
    }

    // 加载框是否可以取消
    protected void showLoadingDialog(String message,boolean cancelable,DialogInterface.OnCancelListener cancelListener) {
        if (null==mProgressDialog) {
            mProgressDialog=new EProgressDialog(getActivity());
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
        try{
            if (mProgressDialog!=null&&mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }catch(Exception e){

        }
    }

    public boolean hasDestroy(){
        return isDestroy;
    }


    @Override
    public void onDestroyView() {

        isDestroy = true;
        super.onDestroyView();
    }

    /**
     * 获得加载数据的地址
     *
     * @return
     */
    protected abstract String getUrl(String nameUrl);


}
