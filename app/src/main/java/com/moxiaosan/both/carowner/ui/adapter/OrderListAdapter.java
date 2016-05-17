package com.moxiaosan.both.carowner.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;

import java.util.List;

import consumer.model.obj.MyOrderObj;

/**
 * Created by chris on 16/3/1.
 */
public class OrderListAdapter extends BaseAdapter {
    private Context context;
    private List<MyOrderObj> lists;

    public OrderListAdapter(Context context,List<MyOrderObj> lists) {

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

            convertView = LayoutInflater.from(context).inflate(R.layout.b_order_list_item,null);
            viewHolder = new ViewHolder();

            viewHolder.tvName =(TextView)convertView.findViewById(R.id.nameId);
            viewHolder.tvTime =(TextView)convertView.findViewById(R.id.timeId);
            viewHolder.tvDanhao =(TextView)convertView.findViewById(R.id.danhaoId);
            viewHolder.tvPrice =(TextView)convertView.findViewById(R.id.priceId);

            viewHolder.tvChufadi =(TextView)convertView.findViewById(R.id.chufadiId);
            viewHolder.tvMudidi =(TextView)convertView.findViewById(R.id.mudidiId);
            viewHolder.tvZhuangtai =(TextView)convertView.findViewById(R.id.zhuangtaiId);

            convertView.setTag(viewHolder);

        }else{

            viewHolder =(ViewHolder)convertView.getTag();

            viewHolder.tvZhuangtai.setText("");

        }

        final MyOrderObj orderObj =lists.get(position);
        viewHolder.tvName.setText(orderObj.getTitle());
        viewHolder.tvTime.setText(orderObj.getDatetime());
        viewHolder.tvDanhao.setText(orderObj.getOrderid());
        viewHolder.tvPrice.setText(orderObj.getCost());
        viewHolder.tvChufadi.setText(orderObj.getBeginningplace());
        viewHolder.tvMudidi.setText(orderObj.getDestination());

        if(("进行中").equals(orderObj.getStatus())){
            viewHolder.tvZhuangtai.setText(orderObj.getStatus());

        }else{

            viewHolder.tvZhuangtai.setText(orderObj.getStatus());
            viewHolder.tvZhuangtai.setTextColor(Color.GRAY);
        }

        return convertView;
    }

    class ViewHolder {

        public TextView tvName, tvTime,tvDanhao,tvPrice;
        public TextView tvChufadi,tvMudidi;
        public TextView tvZhuangtai;

    }
}
