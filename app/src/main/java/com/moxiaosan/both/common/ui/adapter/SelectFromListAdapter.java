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
public class SelectFromListAdapter extends BaseAdapter{
    private Context context;
    //private List<String> list;
    private int indexSelected = -1;
    private List<MyPoiInfo> poiInfoList;

    public SelectFromListAdapter(Context context, List<MyPoiInfo> poiInfos) {
        this.context = context;
        this.poiInfoList = poiInfos;
    }

    public void refreshListData(Context context, List<MyPoiInfo> poiInfos, int index){
        this.context = context;
        this.poiInfoList = poiInfos;
        indexSelected = index;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return poiInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return poiInfoList.get(position);
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

        holder.tvPosition.setText(poiInfoList.get(position).getAddress());

        return convertView;
    }

    class ViewHolder{
        TextView tvPosition;
        ImageView imgSeleted;
    }
}
