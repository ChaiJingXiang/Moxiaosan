package picture;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moxiaosan.commonlibrary.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import picture.view.MyHackyViewPager;
import picture.view.photoview.PhotoView;
import picture.view.photoview.PhotoViewAttacher;

/**
 * Created by peizhihui on 15/10/1.
 */
public class BigPictureFragment extends Fragment implements ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener {
    private MyHackyViewPager viewPager;
    private ImageLoader imageLoader;
    private Cursor cursor;
    private int index;
    private CheckBox checkBox;
    private View titleBar;
    private TextView title;
    private View bottom;
    private TextView next;

    private List<String> imageList;
    private PictureGalleryFragment galleryFragment;
    private List<String> previewList = new ArrayList<String>();
    private boolean isPreview = false;
    private String path;

    public static BigPictureFragment getInstance(Cursor cursor, int index, List<String> imageList, PictureGalleryFragment galleryFragment) {
        BigPictureFragment fragment = new BigPictureFragment();
        fragment.cursor = cursor;
        fragment.index = index;
        fragment.imageList = imageList;
        fragment.galleryFragment = galleryFragment;
        fragment.isPreview = false;
        fragment.imageLoader = ImageLoader.getInstance();
        return fragment;
    }

    public static BigPictureFragment getPreviewInstance(List<String> imageList, PictureGalleryFragment galleryFragment) {
        BigPictureFragment fragment = new BigPictureFragment();
        fragment.cursor = null;
        fragment.index = 0;
        fragment.imageList = imageList;
        fragment.galleryFragment = galleryFragment;
        fragment.previewList.clear();
        fragment.previewList.addAll(imageList);
        fragment.isPreview = true;
        fragment.imageLoader = ImageLoader.getInstance();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.big_picture_gallery, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewPager = (MyHackyViewPager) view.findViewById(R.id.vp);
        titleBar = view.findViewById(R.id.title);
        title = (TextView) view.findViewById(R.id.title_text);
        bottom = view.findViewById(R.id.bottom);
        next = (TextView) view.findViewById(R.id.next);
        next.setText(galleryFragment.getNextString());
//        next.setBackgroundResource(R.color.txt_red);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.size() == 0) {
                    imageList.add(path);
                }
                ArrayList<String> list = new ArrayList<String>();
                list.addAll(imageList);
                getActivity().setResult(Activity.RESULT_OK, new Intent().putStringArrayListExtra("imageList", list));
                getActivity().finish();
            }
        });

        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        if (isPreview) {
            path = previewList.get(0);
            title.setText(String.format("1/%d", previewList.size()));
        } else {
            cursor.moveToPosition(0);
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            title.setText(String.format("1/%d", cursor.getCount()));
        }
        checkBox.setChecked(imageList.contains(path));
        checkBox.setOnCheckedChangeListener(this);
        viewPager.setAdapter(new PhotoPagerAdapter());
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (isPreview) {
            path = previewList.get(position);
            title.setText(String.format("%d/%d", position + 1, previewList.size()));
        } else {
            cursor.moveToPosition(position);
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            title.setText(String.format("%d/%d", position + 1, cursor.getCount()));
        }
        checkBox.setChecked(imageList.contains(path));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked && imageList.contains(path)) {
            galleryFragment.removeImage(path);
        } else if (isChecked && !imageList.contains(path)) {
            if (!galleryFragment.addImage(path)) {
                buttonView.setChecked(false);
            }
        }
        next.setText(galleryFragment.getNextString());
    }

    /**
     * 填充ViewPager页面的适配器
     */
    private class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (isPreview) {
                return previewList.size();
            }
            return cursor.getCount();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(getActivity(), R.layout.item_album_photo_show, null);
            final PhotoView photoView = (PhotoView) view.findViewById(R.id.image);
            final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
            String path = null;
            if (isPreview) {
                path = String.format("file://%s", previewList.get(position));
            } else {
                cursor.moveToPosition(position);
                path = String.format("file://%s", cursor.getString(1));
            }
            imageLoader.displayImage(path, photoView,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            pb.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            pb.setVisibility(View.GONE);
                        }
                    }
            );

            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (titleBar.getVisibility() == View.GONE) {
                        titleBar.setVisibility(View.VISIBLE);
                        bottom.setVisibility(View.VISIBLE);
                    } else {
                        titleBar.setVisibility(View.GONE);
                        bottom.setVisibility(View.GONE);
                    }
                }
            });

            // Now just add PhotoView to ViewPager and return it
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

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
}

