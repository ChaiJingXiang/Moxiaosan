package com.moxiaosan.both.carowner.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.HitchhikingActivity;
import com.moxiaosan.both.carowner.ui.activity.ReceiveOrderActivity;
import com.moxiaosan.both.carowner.ui.activity.SDRelayActivity;
import com.moxiaosan.both.carowner.ui.constants.Constants;

import java.util.List;

import consumer.model.obj.OrderObj;

/**
 * Created by chris on 16/3/1.
 */
public class TakeOrderListAdapter extends BaseAdapter {

    private Context context;
    private List<OrderObj> list;

    public TakeOrderListAdapter(Context context,List<OrderObj> list) {

        this.context =context;
        this.list =list;

    }

    @Override
    public int getCount() {
        return list.size();
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

            convertView = LayoutInflater.from(context).inflate(R.layout.b_takeorder_list_item,null);
            viewHolder = new ViewHolder();

            viewHolder.tvName =(TextView)convertView.findViewById(R.id.nameId);
            viewHolder.tvTime =(TextView)convertView.findViewById(R.id.timeId);
            viewHolder.tvDanhao =(TextView)convertView.findViewById(R.id.danhaoId);
            viewHolder.tvPrice =(TextView)convertView.findViewById(R.id.priceId);

            viewHolder.tvChufadi =(TextView)convertView.findViewById(R.id.chufadiId);
            viewHolder.tvMudidi =(TextView)convertView.findViewById(R.id.mudidiId);
            viewHolder.tvZhiDaJiedan =(TextView)convertView.findViewById(R.id.zhidajiedanId);
            viewHolder.tvQingQiuJieli =(TextView)convertView.findViewById(R.id.qingqiujieliId);
            viewHolder.tvWoYaoJieDan =(TextView)convertView.findViewById(R.id.woyaojiedanId);
            viewHolder.tvJieLi =(TextView)convertView.findViewById(R.id.jieliId);

            convertView.setTag(viewHolder);

        }else{

            viewHolder =(ViewHolder)convertView.getTag();

        }

        final OrderObj orderObj =list.get(position);

        viewHolder.tvName.setText(orderObj.getTitle());
        viewHolder.tvDanhao.setText(orderObj.getOrderid());
        viewHolder.tvTime.setText(orderObj.getDatetime());
        viewHolder.tvChufadi.setText(orderObj.getBeginningplace());
        viewHolder.tvMudidi.setText(orderObj.getDestination());
        viewHolder.tvPrice.setText("¥" + orderObj.getReward() + "元");

        if(orderObj.isZhidai() && !orderObj.isQingQiuJieLi()){

            viewHolder.tvQingQiuJieli.setVisibility(View.GONE);
            viewHolder.tvZhiDaJiedan.setVisibility(View.GONE);
            viewHolder.tvWoYaoJieDan.setVisibility(View.GONE);
            viewHolder.tvJieLi.setVisibility(View.GONE);

            viewHolder.tvZhiDaJiedan.setVisibility(View.VISIBLE);
            viewHolder.tvZhiDaJiedan.setText("直达接单");

            viewHolder.tvZhiDaJiedan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "直达接单", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ReceiveOrderActivity.class);
                    intent.putExtra("name","直达接单");
                    intent.putExtra("orderId",orderObj.getOrderid());
                    context.startActivity(intent);

                }
            });
        }else if(!orderObj.isZhidai() && orderObj.isQingQiuJieLi()){
            viewHolder.tvQingQiuJieli.setVisibility(View.GONE);
            viewHolder.tvZhiDaJiedan.setVisibility(View.GONE);
            viewHolder.tvWoYaoJieDan.setVisibility(View.GONE);
            viewHolder.tvJieLi.setVisibility(View.GONE);

            viewHolder.tvQingQiuJieli.setVisibility(View.VISIBLE);
            viewHolder.tvQingQiuJieli.setText("请求接力");

            viewHolder.tvQingQiuJieli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "接力接单", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SDRelayActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra("name", "接力速递");
                    intent.putExtra("orderId",orderObj.getOrderid());
                    context.startActivity(intent);
                }
            });


        }else if(orderObj.isZhidai() && orderObj.isQingQiuJieLi()){



            viewHolder.tvQingQiuJieli.setVisibility(View.GONE);
            viewHolder.tvZhiDaJiedan.setVisibility(View.GONE);
            viewHolder.tvWoYaoJieDan.setVisibility(View.GONE);
            viewHolder.tvJieLi.setVisibility(View.GONE);

            viewHolder.tvQingQiuJieli.setVisibility(View.VISIBLE);
            viewHolder.tvQingQiuJieli.setText("请求接力");

            viewHolder.tvQingQiuJieli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "接力接单", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SDRelayActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("name", "接力速递");
                    intent.putExtra("orderId", orderObj.getOrderid());
                    context.startActivity(intent);
                }
            });

            viewHolder.tvZhiDaJiedan.setVisibility(View.VISIBLE);
            viewHolder.tvZhiDaJiedan.setText("直达接单");

            viewHolder.tvZhiDaJiedan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "直达接单", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ReceiveOrderActivity.class);
                    intent.putExtra("name","直达接单");
                    intent.putExtra("orderId",orderObj.getOrderid());
                    context.startActivity(intent);

                }
            });
        }


        if(orderObj.isJieLi()){

            viewHolder.tvQingQiuJieli.setVisibility(View.GONE);
            viewHolder.tvZhiDaJiedan.setVisibility(View.GONE);
            viewHolder.tvWoYaoJieDan.setVisibility(View.GONE);
            viewHolder.tvJieLi.setVisibility(View.GONE);

            viewHolder.tvJieLi.setVisibility(View.VISIBLE);
            viewHolder.tvJieLi.setText("接力");

            viewHolder.tvJieLi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SDRelayActivity.class);
                    intent.putExtra("type",2);
                    intent.putExtra("name","接力速递");
                    intent.putExtra("orderId",orderObj.getOrderid());
                    context.startActivity(intent);


                }
            });
        }

        if(orderObj.isShunfeng()){
            viewHolder.tvQingQiuJieli.setVisibility(View.GONE);
            viewHolder.tvZhiDaJiedan.setVisibility(View.GONE);
            viewHolder.tvWoYaoJieDan.setVisibility(View.GONE);
            viewHolder.tvJieLi.setVisibility(View.GONE);

            viewHolder.tvWoYaoJieDan.setVisibility(View.VISIBLE);
            viewHolder.tvWoYaoJieDan.setText("我要接单");

            viewHolder.tvWoYaoJieDan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HitchhikingActivity.class);
                    intent.putExtra("name","顺风车");
                    intent.putExtra("orderId",orderObj.getOrderid());
                    context.startActivity(intent);

                }
            });

        }

        return convertView;
    }


    class ViewHolder {

        public TextView tvName, tvTime,tvDanhao,tvPrice;
        public TextView tvChufadi,tvMudidi;
        public TextView tvZhiDaJiedan, tvQingQiuJieli,tvWoYaoJieDan,tvJieLi;
    }
}
