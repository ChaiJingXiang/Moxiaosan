package com.moxiaosan.both.consumer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;

import java.util.List;

import consumer.model.obj.RespMybalance;

/**
 * Created by qiangfeng on 16/3/2.
 */
public class WalletListAdapter extends BaseAdapter {
    private Context mContext;
    private List<RespMybalance> respMybalanceList;

    public WalletListAdapter(Context mContext,List<RespMybalance> respMybalances) {
        this.mContext = mContext;
        this.respMybalanceList=respMybalances;
    }

    public void reFreshData(List<RespMybalance> respMybalances) {
        this.respMybalanceList=respMybalances;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return respMybalanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return respMybalanceList.get(position);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.wallet_list_item,null);
            viewHolder.tvType= (TextView) convertView.findViewById(R.id.wallet_list_item_type);
            viewHolder.tvTime= (TextView) convertView.findViewById(R.id.wallet_list_item_time);
            viewHolder.tvMoney= (TextView) convertView.findViewById(R.id.wallet_list_item_money);

            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        RespMybalance respMybalance=respMybalanceList.get(position);
        viewHolder.tvType.setText(respMybalance.getTitle());
        viewHolder.tvTime.setText(respMybalance.getDatetime());
        viewHolder.tvMoney.setText("¥"+respMybalance.getMoney());
        return convertView;
    }

    class ViewHolder{
        private TextView tvType,tvMoney,tvTime;
    }
}
