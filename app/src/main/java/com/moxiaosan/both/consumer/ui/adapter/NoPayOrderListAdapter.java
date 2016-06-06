package com.moxiaosan.both.consumer.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.activity.GateOrderDetailActivity;
import com.moxiaosan.both.consumer.ui.activity.SelectPayMethodActivity;
import com.moxiaosan.both.consumer.ui.activity.ShunFengOrderDetailActivity;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;

import java.util.List;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.ConsumerReqUtil;
import consumer.model.Cancelexpress;
import consumer.model.obj.RespUserOrder;

/**
 * Created by qiangfeng on 16/3/1.
 */
public class NoPayOrderListAdapter extends BaseAdapter {
    private Context mContext;
    private List<RespUserOrder> respUserOrderList;

    public NoPayOrderListAdapter(Context mContext, List<RespUserOrder> respUserOrders) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.nopayorder_list_item, null);
            viewHolder.tvTile = (TextView) convertView.findViewById(R.id.no_pay_list_item_title);
            viewHolder.tvOrderNum = (TextView) convertView.findViewById(R.id.no_pay_list_item_order_num);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.no_pay_list_item_price);
            viewHolder.tvDownTime = (TextView) convertView.findViewById(R.id.no_pay_list_item_down_time);
            viewHolder.tvStartPlace = (TextView) convertView.findViewById(R.id.no_pay_list_item_start_place);
            viewHolder.tvEndPlace = (TextView) convertView.findViewById(R.id.no_pay_list_item_end_place);
            viewHolder.tvTakeTime = (TextView) convertView.findViewById(R.id.no_pay_list_item_take_time);
            viewHolder.tvDetail = (TextView) convertView.findViewById(R.id.no_pay_list_item_detail);
            viewHolder.tvEnsure = (TextView) convertView.findViewById(R.id.no_pay_listview_item_ensure);
            viewHolder.tvCancle = (TextView) convertView.findViewById(R.id.no_pay_listview_item_cancel);
            viewHolder.tvTakeTimeTxt = (TextView) convertView.findViewById(R.id.no_pay_list_item_take_time_txt);
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
        if (!respUserOrder.getTitle().equals(mContext.getString(R.string.shun_feng_che))) { //速递
            viewHolder.tvTakeTimeTxt.setText("取件时间：");
        } else {
            viewHolder.tvTakeTimeTxt.setText("接到乘客时间："); //顺风车
        }
        viewHolder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!respUserOrder.getTitle().equals(mContext.getString(R.string.shun_feng_che))) {
                    Intent intent = new Intent(mContext, GateOrderDetailActivity.class);
                    intent.putExtra("respUserOrder", respUserOrder);
                    intent.putExtra("isPayed", false);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, ShunFengOrderDetailActivity.class);
                    intent.putExtra("respUserOrder", respUserOrder);
                    intent.putExtra("isPayed", false);
                    mContext.startActivity(intent);
                }

            }
        });
        viewHolder.tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SelectPayMethodActivity.class).putExtra("respUserOrder", respUserOrder));

            }
        });
        if (!TextUtils.isEmpty(respUserOrder.getServicestatus())){
            viewHolder.tvCancle.setVisibility(View.GONE);
        }else {
            viewHolder.tvCancle.setVisibility(View.VISIBLE);
            viewHolder.tvCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(mContext, respUserOrder.getOrderid());
                    cancelOrderDialog.show();
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        private TextView tvTile, tvOrderNum, tvPrice, tvDownTime, tvStartPlace, tvEndPlace, tvTakeTime, tvTakeTimeTxt, tvDetail, tvEnsure, tvCancle;
    }

    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (output == null) {
                return;
            }
            if (output instanceof Cancelexpress) {
                Cancelexpress cancelexpress = (Cancelexpress) output;
                EUtil.showToast(cancelexpress.getErr());
                if (cancelexpress.getRes().equals("0")) {
                    for (int i = 0; i < respUserOrderList.size(); i++) {
                        if (respUserOrderList.get(i).getOrderid().equals(input)) {
                            respUserOrderList.remove(i);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        }
    };

    //取消订单
    class CancelOrderDialog extends AlertDialog {

        String orderId;

        public CancelOrderDialog(Context context, String id) {
            super(context);
            this.orderId = id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);
            TextView tv = (TextView) findViewById(R.id.tvDialogActivity);
            tv.setText("确认取消该订单");
            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    //网络
                    ConsumerReqUtil.cancelexpress(mContext, iApiCallback, null, new Cancelexpress(), orderId, true,
                            StringUrlUtils.geturl(new HashMapUtils().putValue("username", AppData.getInstance().getUserEntity().getUsername()).putValue("expid", orderId).createMap())
                    );

                }
            });

            findViewById(R.id.setting_exit_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
}
