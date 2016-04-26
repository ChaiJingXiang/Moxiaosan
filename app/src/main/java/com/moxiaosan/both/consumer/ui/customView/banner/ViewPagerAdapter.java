package com.moxiaosan.both.consumer.ui.customView.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moxiaosan.both.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import consumer.model.obj.ResPictrue;

/**
 * Created by fengyongqaing on 15/11/11.
 */
public class ViewPagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<ResPictrue> resPictrueList;
//    Map<Integer, View> viewMap = new HashMap<>();

    public ViewPagerAdapter(Context context, List<ResPictrue> lists) {
        this.context = context;
        this.resPictrueList = lists;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.auto_viewpager_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.auto_viewpager_item_img);
            convertView.setTag(holder);
//            viewMap.put(position, convertView);
        } else {
//            convertView = viewMap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }

        final ResPictrue resPictrue = resPictrueList.get(position % resPictrueList.size());
        ImageLoader.getInstance().displayImage(resPictrue.getPicurl(),holder.imageView);
        holder.imageView.setTag(position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EUtil.showToast("==" + v.getTag());
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
