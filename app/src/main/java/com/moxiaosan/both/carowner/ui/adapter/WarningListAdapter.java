package com.moxiaosan.both.carowner.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;

import java.util.List;

import consumer.model.obj.AlarmObj;
import consumer.model.obj.MyOrderObj;

/**
 * Created by chris on 16/3/1.
 */
public class WarningListAdapter extends BaseAdapter {
    private Context context;
    private List<AlarmObj> lists;

    public WarningListAdapter(Context context,List<AlarmObj> lists) {

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

            convertView = LayoutInflater.from(context).inflate(R.layout.b_warning_list_item,null);
            viewHolder = new ViewHolder();

            viewHolder.tvName =(TextView)convertView.findViewById(R.id.nameId);
            viewHolder.tvTime =(TextView)convertView.findViewById(R.id.timeId);
            viewHolder.tvAddress=(TextView)convertView.findViewById(R.id.addressId);

            convertView.setTag(viewHolder);

        }else{

            viewHolder =(ViewHolder)convertView.getTag();

        }

        final AlarmObj alarmObj =lists.get(position);
        viewHolder.tvName.setText(alarmObj.getAlarminfo());
        viewHolder.tvTime.setText(alarmObj.getDatetime());
        viewHolder.tvAddress.setText(alarmObj.getAddress());

        return convertView;
    }

    class ViewHolder {

        public TextView tvName, tvTime,tvAddress;

    }
}
