package com.moxiaosan.both.consumer.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxiaosan.both.R;

import java.util.List;

import consumer.model.obj.RespMycomment;

/**
 * Created by qiangfeng on 16/3/2.
 */
public class AppraiseListAdapter extends BaseAdapter {
    private Context mContext;
    private List<RespMycomment> respMycommentList;

    public AppraiseListAdapter(Context mContext, List<RespMycomment> mycomments) {
        this.mContext = mContext;
        this.respMycommentList = mycomments;
    }

    public void reFreshData(List<RespMycomment> mycomments) {
        this.respMycommentList = mycomments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return respMycommentList == null ? 0 : respMycommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return respMycommentList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.appraise_list_item, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.appraise_list_item_name);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.appraise_list_item_type);
            viewHolder.tvComments = (TextView) convertView.findViewById(R.id.appraise_list_item_comments);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.appraise_list_item_time);
            viewHolder.ratingBarSpeed = (LinearLayout) convertView.findViewById(R.id.appraise_list_item_speed);
            viewHolder.ratingBarService = (LinearLayout) convertView.findViewById(R.id.appraise_list_item_service);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RespMycomment respMycomment = respMycommentList.get(position);
        viewHolder.tvName.setText(respMycomment.getSurname());
        if (respMycomment.getType().equals("1")) {
            viewHolder.tvType.setText("门到门速递");
        } else if (respMycomment.getType().equals("2")) {
            viewHolder.tvType.setText("出行");
        }
        viewHolder.tvComments.setText(respMycomment.getCommenttext());
        viewHolder.tvTime.setText(respMycomment.getDatetime());
        if (viewHolder.ratingBarSpeed.getChildCount() > 0) {
            viewHolder.ratingBarSpeed.removeAllViews();
        }
        viewHolder.ratingBarSpeed.addView(setStart(Integer.valueOf(respMycomment.getSpeed())));

        if (viewHolder.ratingBarService.getChildCount() > 0) {
            viewHolder.ratingBarService.removeAllViews();
        }
        viewHolder.ratingBarService.addView(setStart(Integer.valueOf(respMycomment.getServices())));
        return convertView;
    }

    class ViewHolder {
        private TextView tvName, tvType, tvComments, tvTime;
        private LinearLayout ratingBarSpeed, ratingBarService;
    }

    //设置评分  返回View
    private View setStart(int score) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(0, 0, 0, 0);

        for (int i = 0; i < score; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.mipmap.light_star);
            linearLayout.addView(imageView);
        }
        for (int j = 0; j < 5 - score; j++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.mipmap.grey_star);
            linearLayout.addView(imageView);
        }

        return linearLayout;

    }
}
