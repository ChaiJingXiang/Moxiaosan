package com.moxiaosan.both.carowner.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.utils.image.RoundImageView;

import java.util.List;

import consumer.model.obj.RelayInfo;

/**
 * Created by chris on 16/3/11.
 */
public class SDListViewAdapter extends BaseAdapter {

    private Context context;
    List<RelayInfo> lists;

    public SDListViewAdapter(Context context,List<RelayInfo> lists) {

        this.context =context;
        this.lists =lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        if(convertView ==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.b_jieli_listview_item,null);

            viewHolder.tvTime =(TextView)convertView.findViewById(R.id.timeId);
            viewHolder.tvAddress =(TextView)convertView.findViewById(R.id.addressId);
            viewHolder.tvName =(TextView)convertView.findViewById(R.id.nameId);
            viewHolder.tvPhone =(TextView)convertView.findViewById(R.id.phoneId);

            viewHolder.img =(RoundImageView)convertView.findViewById(R.id.centerId);
            viewHolder.tvTop =(TextView)convertView.findViewById(R.id.topId);
            viewHolder.tvBottom =(TextView)convertView.findViewById(R.id.bottomId);

            convertView.setTag(viewHolder);

        }else{

            viewHolder =(ViewHolder)convertView.getTag();

        }


        final RelayInfo info =lists.get(position);

        if(position ==0){
            viewHolder.tvTop.setTextColor(Color.WHITE);
        }

        viewHolder.tvTime.setText(info.getHandover());
        viewHolder.tvAddress.setText(info.getHandoveraddress());
        viewHolder.tvPhone.setText(info.getRe_contact());

        viewHolder.img.setType(RoundImageView.TYPE_CIRCLE);

        return convertView;
    }

    class ViewHolder {

        public TextView tvTime,tvAddress,tvName,tvPhone;
        public TextView tvTop,tvBottom;
        public RoundImageView img;

    }
}
