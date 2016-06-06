package com.moxiaosan.both.consumer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.activity.GateOrderDetailActivity;
import com.moxiaosan.both.consumer.ui.activity.OrderCommentActivity;
import com.moxiaosan.both.consumer.ui.activity.ShunFengOrderDetailActivity;
import com.utils.api.IApiCallback;

import java.util.List;

import consumer.model.obj.RespUserOrder;

/**
 * Created by qiangfeng on 16/3/1.
 */
public class HavePayedOrderListAdapter extends BaseAdapter {
    private Context mContext;
    private List<RespUserOrder> respUserOrderList;

    public HavePayedOrderListAdapter(Context mContext, List<RespUserOrder> respUserOrders) {
        this.mContext = mContext;
        this.respUserOrderList = respUserOrders;
    }

    public void reFreshData(List<RespUserOrder> respUserOrders) {
        this.respUserOrderList = respUserOrders;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return respUserOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return respUserOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.havepayorder_list_item, null);
            viewHolder.tvTile = (TextView) convertView.findViewById(R.id.have_payed_listview_item_title);
            viewHolder.tvOrderNum = (TextView) convertView.findViewById(R.id.have_payed_listview_item_order_num);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.have_payed_listview_item_price);
            viewHolder.tvDownTime = (TextView) convertView.findViewById(R.id.have_payed_listview_item_down_time);
            viewHolder.tvStartPlace = (TextView) convertView.findViewById(R.id.have_payed_listview_item_start_place);
            viewHolder.tvEndPlace = (TextView) convertView.findViewById(R.id.have_payed_listview_item_end_place);
            viewHolder.tvTakeTime = (TextView) convertView.findViewById(R.id.have_payed_listview_item_take_time);
            viewHolder.tvTakeTimeTxt = (TextView) convertView.findViewById(R.id.have_payed_listview_item_take_time_txt);
            viewHolder.tvDetail = (TextView) convertView.findViewById(R.id.have_payed_listview_item_detail);
            viewHolder.tvComment = (TextView) convertView.findViewById(R.id.have_payed_listview_item_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final RespUserOrder respUserOrder = respUserOrderList.get(position);
        viewHolder.tvTile.setText(respUserOrder.getTitle());
        viewHolder.tvOrderNum.setText(respUserOrder.getOrderid());
        viewHolder.tvPrice.setText("¥" + respUserOrder.getCost());
        viewHolder.tvDownTime.setText(respUserOrder.getDatetime());
        viewHolder.tvStartPlace.setText(respUserOrder.getBeginningplace());
        viewHolder.tvEndPlace.setText(respUserOrder.getDestination());
        viewHolder.tvTakeTime.setText(respUserOrder.getPickuptime());
        if (!respUserOrder.getTitle().equals(mContext.getString(R.string.shun_feng_che))) {  //速递
            viewHolder.tvTakeTimeTxt.setText("取件时间：");
        } else {
            viewHolder.tvTakeTimeTxt.setText("接到乘客时间：");
        }
        viewHolder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!respUserOrder.getTitle().equals(mContext.getString(R.string.shun_feng_che))) { //速递
                    Intent intent = new Intent(mContext, GateOrderDetailActivity.class);
                    intent.putExtra("respUserOrder", respUserOrder);
                    intent.putExtra("isPayed", true);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, ShunFengOrderDetailActivity.class);
                    intent.putExtra("respUserOrder", respUserOrder);
                    intent.putExtra("isPayed", true);
                    mContext.startActivity(intent);
                }
            }
        });
        viewHolder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderCommentActivity.class);
                intent.putExtra("respUserOrder", respUserOrder);
                if (!respUserOrder.getTitle().equals(mContext.getString(R.string.shun_feng_che))) {  //速递
                    intent.putExtra("type", 1);
                } else {
                    intent.putExtra("type", 2);
                }
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView tvTile, tvOrderNum, tvPrice, tvDownTime, tvStartPlace, tvEndPlace, tvTakeTime, tvTakeTimeTxt, tvDetail, tvComment;
    }

    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (output == null) {
                return;
            }
//            if (){
//
//            }
        }
    };
}
