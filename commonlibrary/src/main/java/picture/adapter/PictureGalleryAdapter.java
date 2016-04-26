package picture.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.moxiaosan.commonlibrary.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import picture.PictureGalleryFragment;

/**
 * Created by peizhihui on 15/10/1.
 */
public class PictureGalleryAdapter extends CursorAdapter {
    private final LayoutInflater mInflater;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private List<String> imageList;
    private PictureGalleryFragment galleryFragment;

    public PictureGalleryAdapter(PictureGalleryFragment galleryFragment, Cursor c, List<String> imageList) {
        super(galleryFragment.getActivity(), c, FLAG_REGISTER_CONTENT_OBSERVER);
        mInflater = (LayoutInflater) galleryFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.galleryFragment = galleryFragment;
        this.imageList = imageList;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(com.quncao.lark.R.mipmap.more_pic)
//                .showImageForEmptyUri(com.quncao.lark.R.mipmap.more_pic)
                .cacheInMemory(false)
                .cacheOnDisc(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count == 0 ? 1 : count + 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String getItem(int position) {
        if (position > 0) {
            Cursor cursor = (Cursor) super.getItem(position - 1);
            if (cursor != null) {
                return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            if (convertView == null) {
                convertView = newView(mContext, null, parent);
                ImageView image = (ImageView) convertView.findViewById(R.id.image);
                image.setImageResource(R.drawable.button_camera);
                convertView.findViewById(R.id.checkBox).setVisibility(View.GONE);
                convertView.findViewById(R.id.zoomIn).setVisibility(View.GONE);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        galleryFragment.takePhoto();
                    }
                });
            }
            return convertView;
        } else {
            return super.getView(position - 1, convertView, parent);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_picture_gallery, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        ImageView zoom = (ImageView)view.findViewById(R.id.zoomIn);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        final int position = cursor.getPosition();
        Object tag = image.getTag();
        if (tag == null || !tag.equals(path)) {
            imageLoader.displayImage("file://" + path, image, options, null);
        }
        image.setTag(path);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!checkBox.isChecked() && imageList.contains(path)){
                    galleryFragment.removeImage(path);
                }else if (checkBox.isChecked() && !imageList.contains(path)){
                    if(!galleryFragment.addImage(path)){
                        checkBox.setChecked(false);
                    }
                }
            }
        });
//        zoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                galleryFragment.getFragmentManager().beginTransaction()
//                        .replace(android.R.id.content, BigPictureFragment.getInstance(getCursor(), position,
//                                imageList, galleryFragment))
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
        boolean isChecked = imageList.contains(path);
        if (isChecked) {
            //    checkBox.setText(String.format("%d", imageList.indexOf(path) + 1));
            checkBox.setBackgroundColor(0x66000000);
            zoom.setVisibility(View.VISIBLE);

        } else {

            checkBox.setBackgroundResource(0);
            zoom.setVisibility(View.GONE);
        }
        checkBox.setChecked(isChecked);
    }
}