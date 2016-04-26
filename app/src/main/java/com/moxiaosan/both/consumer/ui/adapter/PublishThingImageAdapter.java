package com.moxiaosan.both.consumer.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.customView.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import picture.ImageCheckActivity;

/**
 * Created by qiangfeng on 16/3/14.
 */
public class PublishThingImageAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Context mContext;
    private int maxNum;
    private ArrayList<String> list;
    private Handler handler = new Handler();
    private boolean isWeb = false;

    public PublishThingImageAdapter(Context context, ArrayList<String> list, int maxNum, boolean isWebUrl) {
        this.mContext = context;
        this.list = list;
        this.maxNum = maxNum;
        inflater = LayoutInflater.from(mContext);
        this.isWeb = isWebUrl;
    }

    private void refreshList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (isWeb) {  //网络的
            return list.size();
        } else {
            if (list.size() == maxNum) {
                return maxNum;
            } else {
                return list.size() + 1;
            }
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < list.size()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_publish_thing_image, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.imgDelete = (ImageView) convertView.findViewById(R.id.delete_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isWeb) { //是网络的图  只做显示
            holder.imgDelete.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(list.get(position), holder.image);
        } else {  //本地的图片库
            switch (getItemViewType(position)) {
                case 0:
                    final String imageUrl = list.get(position);
//                Log.i("PublishThingImageAdapter","imageUrl ="+imageUrl);

                    if (!imageUrl.equals(holder.imageUrl)) {
                        holder.imageUrl = imageUrl;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.image.setImageBitmap(ImageUtils.getBitmapThumbByPath(imageUrl));
//
                            }
                        });

                        holder.imgDelete.setVisibility(View.VISIBLE);
                        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                list.remove(imageUrl);
                                refreshList(list);
                            }
                        });

                        holder.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageCheckActivity.invokeLocalImagelStartActivity(mContext, list, position);
                            }
                        });
                    }

                    break;
                case 1:
                    holder.imgDelete.setVisibility(View.GONE);
                    holder.image.setImageResource(R.drawable.picture_add_btn);
                    break;
            }
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView image;
        private ImageView imgDelete;
        private String imageUrl;
    }
}
