package picture.view;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moxiaosan.commonlibrary.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import picture.PictureGalleryFragment;

/**
 * Created by peizhihui on 15/10/1.
 */
public class PictureGalleryPopupWindow extends PopupWindow {
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    private PictureGalleryFragment galleryFragment;

    public PictureGalleryPopupWindow(PictureGalleryFragment galleryFragment) {
        super(galleryFragment.getActivity());
        this.galleryFragment = galleryFragment;
        Context context = galleryFragment.getActivity();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(com.quncao.lark.R.mipmap.more_pic)
//                .showImageForEmptyUri(com.quncao.lark.R.mipmap.more_pic)
                .cacheInMemory(false)
                .cacheOnDisc(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();

        View contentView = inflater.inflate(R.layout.view_picture_gallery_popupwindow, null);
        setContentView(contentView);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ListView listView = (ListView) contentView.findViewById(R.id.lv_popup);

        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String select = "0 == 0 ) group by ( " + MediaStore.Images.Media.BUCKET_ID;
        Cursor[] cursors = new Cursor[2];
        cursors[0] = context.getContentResolver().query(baseUri,
                new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.BUCKET_ID, // dir id 目录
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
                        MediaStore.Images.Media.DATA,
                        "count(*) as count"
                }, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC"
        );
        cursors[1] = context.getContentResolver().query(baseUri,
                new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.BUCKET_ID, // dir id 目录
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
                        MediaStore.Images.Media.DATA,
                        "count(*) as count"
                }, select, null, MediaStore.Images.Media.DATE_TAKEN + " DESC"
        );
        MergeCursor cursor = new MergeCursor(cursors);
        listView.setAdapter(new ListDownAdapter(context, cursor));
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        setOutsideTouchable(true);
    }

    private class ListDownAdapter extends CursorAdapter {
        private final LayoutInflater inflater;

        public ListDownAdapter(Context context, Cursor c) {
            super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return inflater.inflate(R.layout.picture_gallery_cover_item, parent, false);
        }

        @Override
        public void bindView(View view, final Context context, Cursor cursor) {
            TextView dir_name = (TextView) view.findViewById(R.id.dir_name);
            final int position = cursor.getPosition();
            final int dirId;
            final String dirName;
            if (position == 0) {
                dirId = 0;
                dirName = "所有照片";
            } else {
                dirId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                dirName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            }
            dir_name.setText(dirName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    galleryFragment.switchDir(dirId, dirName);
                    dismiss();
                }
            });
            TextView count = (TextView) view.findViewById(R.id.count);
            count.setText(cursor.getString(cursor.getColumnIndex("count")));
//            if (dirId == galleryFragment.getDirId()) {
//                count.setBackgroundResource(R.drawable.circle_red_40dp);
//                count.setTextColor(0xffffffff);
//            } else {
//                count.setBackgroundResource(R.drawable.circle_gray_line_40dp);
//                count.setTextColor(0xff777777);
//            }
            count.setTextColor(0xff777777);
            ImageView iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
            String url = String.format("file://%s", cursor.getString(
                    cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            imageLoader.displayImage(url, iv_cover, options, null);
        }
    }
}
