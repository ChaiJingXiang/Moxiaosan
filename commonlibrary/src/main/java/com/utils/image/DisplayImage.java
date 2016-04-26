package com.utils.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import com.moxiaosan.commonlibrary.R;
/**
 * Created by fengyongqiang on 15/11/6.
 * 显示图片：缩略图
 */
public class DisplayImage {

    private static String URI_POSTFIX_BIG = "!column1";
    private static String URI_POSTFIX_MID = "!column2";
    private static String URI_POSTFIX_SMALL = "!column3";

    //枚举缩略图类型
    public enum ThumbnailType{ORIGIN, COLUMN1, COLUMN2, COLUMN3};

    /**
     * 异步加载图片
     *
     * @param imageView 显示图片的view
     * @param uri       图片地址
     * @param options   显示图片时需要的配置信息
     * @param listener  图片加载过程监听
     */

    public static void displayImage(ImageView imageView, String uri, DisplayImageOptions options, ImageLoadingListener listener) {

        if(uri != null && !"".equals(uri)) {
            ImageLoader.getInstance().displayImage(uri.trim(), imageView, options, listener);
        }else {
            imageView.setImageResource(R.mipmap.default_pic);
        }
    }

    public static void displayImage(ImageView imageView, String uri, int stubImageRes, int emptyImageRes, int errorImageRes, ImageLoadingListener listener) {
        displayImage(imageView, uri, getSampleDisplayImageOptions(stubImageRes, emptyImageRes, errorImageRes), listener);
    }

    public static void displayImage(ImageView imageView, String uri, int stubImageRes, int errorImageRes, ImageLoadingListener listener) {
        displayImage(imageView, uri, getSampleDisplayImageOptions(stubImageRes, errorImageRes, errorImageRes), listener);
    }

    public static void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), null);
    }

    public static String  displayImage(ImageView imageView, String uri, ThumbnailType type) {
        if (!uri.contains("!bac")) {
            String uriPostfix = "";
            if (type == ThumbnailType.COLUMN1) {
                uriPostfix = URI_POSTFIX_BIG;
            } else if (type == ThumbnailType.COLUMN2) {
                uriPostfix = URI_POSTFIX_MID;
            } else if (type == ThumbnailType.COLUMN3) {
                uriPostfix = URI_POSTFIX_SMALL;
            }
            displayImage(imageView, uri + uriPostfix, getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), null);
            return  uri + uriPostfix;
        }else{
            displayImage(imageView, uri, getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), null);
            return  uri;
        }

    }

    //显示动态默认图片
    public static String displayDynamicImage(ImageView imageView, String uri, ThumbnailType type) {
       int imageID = 0;
        if (type == ThumbnailType.COLUMN1) {
            imageID = R.mipmap.default_pic;
        } else if (type == ThumbnailType.COLUMN2) {
            imageID = R.mipmap.default_pic_two;
        } else if (type == ThumbnailType.COLUMN3) {
            imageID = R.mipmap.default_pic_three;
        }

        if (!uri.contains("!bac")) {
            String uriPostfix = "";
            if (type == ThumbnailType.COLUMN1) {
                uriPostfix = URI_POSTFIX_BIG;
            } else if (type == ThumbnailType.COLUMN2) {
                uriPostfix = URI_POSTFIX_MID;
            } else if (type == ThumbnailType.COLUMN3) {
                uriPostfix = URI_POSTFIX_SMALL;
            }
            displayImage(imageView, uri + uriPostfix, getSampleDisplayImageOptions(imageID, imageID, imageID), null);
            return  uri + uriPostfix;
        }else{
            displayImage(imageView, uri, getSampleDisplayImageOptions(imageID, imageID, imageID), null);
            return  uri;
        }


    }

    //显示默认头像
    public static void displayHeadImage(ImageView imageView, String uri, ThumbnailType type) {
        if (!uri.contains("!bac")) {
            String uriPostfix = "";
            if (type == ThumbnailType.COLUMN1) {
                uriPostfix = URI_POSTFIX_BIG;
            } else if (type == ThumbnailType.COLUMN2) {
                uriPostfix = URI_POSTFIX_MID;
            } else if (type == ThumbnailType.COLUMN3) {
                uriPostfix = URI_POSTFIX_SMALL;
            }
            displayImage(imageView, uri + uriPostfix, getSampleDisplayImageOptions(R.mipmap.icon_default_head, R.mipmap.icon_default_head, R.mipmap.icon_default_head), null);
        }else{
            displayImage(imageView, uri, getSampleDisplayImageOptions(R.mipmap.icon_default_head, R.mipmap.icon_default_head, R.mipmap.icon_default_head), null);
        }
    }

    public static void displayImage(ImageView imageView, String uri, ThumbnailType type,int stubImageRes,int errorImageRes) {
        if (!uri.contains("!bac")) {
            String uriPostfix = "";
            if (type == ThumbnailType.COLUMN1) {
                uriPostfix = URI_POSTFIX_BIG;
            } else if (type == ThumbnailType.COLUMN2) {
                uriPostfix = URI_POSTFIX_MID;
            } else if (type == ThumbnailType.COLUMN3) {
                uriPostfix = URI_POSTFIX_SMALL;
            }
            displayImage(imageView, uri + uriPostfix, getSampleDisplayImageOptions(stubImageRes, errorImageRes, errorImageRes), null);
        }else{
            displayImage(imageView, uri, getSampleDisplayImageOptions(stubImageRes, errorImageRes, errorImageRes), null);
        }
    }

    //加载淡出效果
    public static void displayImage(ImageView imageView, String uri, ThumbnailType type,ImageLoadingListener listener) {
        if (!uri.contains("!bac")) {
            String uriPostfix = "";
            if (type == ThumbnailType.COLUMN1) {
                uriPostfix = URI_POSTFIX_BIG;
            } else if (type == ThumbnailType.COLUMN2) {
                uriPostfix = URI_POSTFIX_MID;
            } else if (type == ThumbnailType.COLUMN3) {
                uriPostfix = URI_POSTFIX_SMALL;
            }
            displayImage(imageView, uri + uriPostfix, getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), listener);
        }else{
            displayImage(imageView, uri, getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), listener);
        }

    }

    /**
     * 异步加载图片方法
     *
     * @param imageView     显示图片的view
     * @param uri           图片地址
     * @param stubImageRes  默认背景图片
     * @param errorImageRes 加载错误图片
     */
    public static void displayImage(ImageView imageView, String uri, int stubImageRes, int errorImageRes) {
        displayImage(imageView, uri, getSampleDisplayImageOptions(stubImageRes, errorImageRes, errorImageRes), null);
    }

    /**
     * 返回ImageLoader加载图片时需要的参数
     *
     * @param stubImageRes  默认背景图片
     * @param emptyImageRes 加载地址为空的图片
     * @param errorImageRes 加载错误时显示的图片
     * @return
     */
    public static DisplayImageOptions getSampleDisplayImageOptions(int stubImageRes, int emptyImageRes, int errorImageRes) {
        return new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showStubImage(stubImageRes)
                .showImageForEmptyUri(emptyImageRes)
                .showImageOnFail(errorImageRes)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

}
