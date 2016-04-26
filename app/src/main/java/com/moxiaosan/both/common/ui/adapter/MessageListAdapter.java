package com.moxiaosan.both.common.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxiaosan.both.R;

import java.util.List;

import consumer.model.obj.ResMynews;

/**
 * Created by qiangfeng on 16/3/1.
 */
public class MessageListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ResMynews> resMynewsList;

    public MessageListAdapter(Context mContext, List<ResMynews> resMynewses) {
        this.mContext = mContext;
        this.resMynewsList = resMynewses;
    }

    public void reFreshData(List<ResMynews> resMynewses) {
        this.resMynewsList = resMynewses;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return resMynewsList == null ? 0 : resMynewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return resMynewsList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.messages_list_item, null);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.message_list_item_time);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.message_list_item_content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTime.setText(resMynewsList.get(position).getDatetime());
        viewHolder.tvContent.setText(resMynewsList.get(position).getContent());
        return convertView;
    }

    class ViewHolder {
        private TextView tvTime, tvContent;
    }
}
