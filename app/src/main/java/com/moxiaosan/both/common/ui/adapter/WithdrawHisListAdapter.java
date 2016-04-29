package com.moxiaosan.both.common.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;

import java.util.List;

import consumer.model.obj.RespMybalance;
import consumer.model.obj.WithdrawObj;

/**
 * Created by qiangfeng on 16/3/2.
 */
public class WithdrawHisListAdapter extends BaseAdapter {
    private Context mContext;
    private List<WithdrawObj> lists;

    public WithdrawHisListAdapter(Context mContext,List<WithdrawObj> lists) {
        this.mContext = mContext;
        this.lists =lists;
    }


    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.withdraw_list_item,null);
            viewHolder.tvType= (TextView) convertView.findViewById(R.id.withdraw_list_item_type);
            viewHolder.tvTime= (TextView) convertView.findViewById(R.id.withdraw_list_item_time);
            viewHolder.tvMoney= (TextView) convertView.findViewById(R.id.withdraw_list_item_money);
            viewHolder.tvNum= (TextView) convertView.findViewById(R.id.withdraw_list_item_num);
            viewHolder.tvStatus= (TextView) convertView.findViewById(R.id.withdraw_list_item_status);

            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();

        }


        final WithdrawObj withdrawObj =lists.get(position);

        viewHolder.tvTime.setText(withdrawObj.getDatetime());
        viewHolder.tvType.setText(withdrawObj.getType());
        viewHolder.tvNum.setText(withdrawObj.getAccount());
        viewHolder.tvMoney.setText(withdrawObj.getMoney());
        viewHolder.tvStatus.setText(withdrawObj.getStatus());


        return convertView;
    }

    class ViewHolder{
        private TextView tvType,tvMoney,tvTime,tvNum,tvStatus;
    }
}
