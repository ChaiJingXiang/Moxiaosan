package picture.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxiaosan.commonlibrary.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.utils.image.DisplayImage;

import java.util.List;

import picture.ImageCheckActivity;
import picture.view.photoview.PhotoView;

/**
 * @author dingjun.he
 * @data 15-9-23
 * @des 图片查看器adapter
 */
public class ImageCheckAdatper extends PagerAdapter {

    private ImageCheckActivity activity;
    private List<String> imageUrl;
    private List<String> thmbnailImageUrl;
    private boolean isLocal;

    public ImageCheckAdatper(ImageCheckActivity activity, List<String> imageUrl, List<String> thmbnailImageUrl, boolean isLocal) {
        this.activity = activity;
        this.imageUrl = imageUrl;
        this.isLocal = isLocal;
        this.thmbnailImageUrl = thmbnailImageUrl;
    }

    @Override
    public int getCount() {
        return imageUrl == null ? 0 : imageUrl.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(activity).inflate(R.layout.image_check_item, null);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.photo_view);
//        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
//        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
//            @Override
//            public void onViewTap(View view, float x, float y) {
//                activity.finish();
//            }
//        });
        if (!isLocal) {
            if (thmbnailImageUrl != null && thmbnailImageUrl.size() != 0) {
                final Bitmap thmbnailBitmap = ImageLoader.getInstance().loadImageSync(thmbnailImageUrl.get(position));
                ImageLoader.getInstance().loadImage(imageUrl.get(position), DisplayImage.getSampleDisplayImageOptions(R.mipmap.default_pic, R.mipmap.default_pic, R.mipmap.default_pic), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        if (thmbnailBitmap != null) {
                            imageView.setImageBitmap(thmbnailBitmap);
                        } else {
                            imageView.setImageResource(R.mipmap.default_pic);
                        }
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
//                        imageView.setImageResource(R.mipmap.default_pic);
                        if (thmbnailBitmap != null) {
                            imageView.setImageBitmap(thmbnailBitmap);
                        } else {
                            imageView.setImageResource(R.mipmap.default_pic);
                        }
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        if (thmbnailBitmap != null) {
                            imageView.setImageBitmap(thmbnailBitmap);
                        } else {
                            imageView.setImageResource(R.mipmap.default_pic);
                        }
                    }
                });


            } else {
                DisplayImage.displayImage(imageView, imageUrl.get(position), R.mipmap.default_pic, R.mipmap.default_pic);
            }
        } else {

            DisplayImage.displayImage(imageView, "file://" + imageUrl.get(position), R.mipmap.default_pic, R.mipmap.default_pic);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
