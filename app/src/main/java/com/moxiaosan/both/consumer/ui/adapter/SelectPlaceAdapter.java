package com.moxiaosan.both.consumer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;

import java.util.List;

/**
 * Created by qiangfeng on 16/3/30.
 */
public class SelectPlaceAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> stringList;

    public SelectPlaceAdapter(Context context, List<String> strings) {
        this.mContext = context;
        this.stringList = strings;
    }

    public void reFreshData(List<String> strings){
        this.stringList = strings;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.select_place_list_item,null);
            viewHolder.tvName= (TextView) convertView.findViewById(R.id.select_place_item_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(stringList.get(position));
        return convertView;
    }

    class ViewHolder{
        private TextView tvName;
    }
}
