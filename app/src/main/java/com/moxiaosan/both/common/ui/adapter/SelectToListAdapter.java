package com.moxiaosan.both.common.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.model.MyPoiInfo;

import java.util.List;

/**
 * Created by qiangfeng on 16/3/15.
 */
public class SelectToListAdapter extends BaseAdapter {
    private Context context;
    private int indexSelected = -1;
    private List<MyPoiInfo> myPoiInfoList;

    public SelectToListAdapter(Context context, List<MyPoiInfo> myPoiInfos) {
        this.context = context;
        this.myPoiInfoList = myPoiInfos;
    }

    public void refreshListData(Context context, List<MyPoiInfo> myPoiInfos, int index){
        this.context = context;
        this.myPoiInfoList = myPoiInfos;
        indexSelected = index;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myPoiInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return myPoiInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_showposition_list, null);
            holder.imgSeleted = (ImageView)convertView.findViewById(R.id.selected_img);
            holder.tvPosition = (TextView)convertView.findViewById(R.id.position_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(indexSelected == position){
            holder.imgSeleted.setVisibility(View.VISIBLE);
            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.txt_orange));
        }else{
            holder.imgSeleted.setVisibility(View.INVISIBLE);
            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.txt_gray_d));
        }

        holder.tvPosition.setText(myPoiInfoList.get(position).getAddress());

        return convertView;
    }

    class ViewHolder{
        TextView tvPosition;
        ImageView imgSeleted;
    }
}
