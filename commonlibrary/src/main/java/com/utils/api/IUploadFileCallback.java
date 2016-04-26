package com.utils.api;

/**
 * Created by fengyongqiang on 16/2/25.
 */
public interface IUploadFileCallback extends IApiCallback {
    public abstract void onProgress(int percent);//0-100之间
}
