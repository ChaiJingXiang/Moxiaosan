package com.moxiaosan.both.consumer.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.activity.PublishLabourActivity;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;

import java.util.List;

import consumer.api.ConsumerReqUtil;
import consumer.model.Dellabour;
import consumer.model.obj.RespLabour;
import consumer.model.obj.RespLabourComment;

/**
 * Created by qiangfeng on 16/3/1.
 */
public class FindLabourListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<RespLabour> respLabourList;
    private NotifyCommetListener notifyCommetListener;

    public FindLabourListAdapter(Context mContext, List<RespLabour> respLabours) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.respLabourList = respLabours;
    }

    public void refreshData(List<RespLabour> respLabours) {
        this.respLabourList = respLabours;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return respLabourList == null ? 0 : respLabourList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (respLabourList.get(groupPosition) != null) {
            if (respLabourList.get(groupPosition).getComments() != null) {
                if (respLabourList.get(groupPosition).isOpenComments()) { //展开评论
                    return respLabourList.get(groupPosition).getComments().size();
                } else {
                    return respLabourList.get(groupPosition).getComments().size() > 3 ? 3 : respLabourList.get(groupPosition).getComments().size();
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return respLabourList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return respLabourList.get(groupPosition).getComments().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.find_labour_list_group_item, null);
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.find_labour_list_group_title);
            groupViewHolder.tvTime = (TextView) convertView.findViewById(R.id.find_labour_list_group_time);
            groupViewHolder.tvPublishTime= (TextView) convertView.findViewById(R.id.find_labour_list_group_publish_time);
            groupViewHolder.tvContent = (TextView) convertView.findViewById(R.id.find_labour_list_group_content);
            groupViewHolder.tvPeopleNum = (TextView) convertView.findViewById(R.id.find_labour_list_group_people_num);
            groupViewHolder.tvSalary = (TextView) convertView.findViewById(R.id.find_labour_list_group_salary);
            groupViewHolder.tvAddress = (TextView) convertView.findViewById(R.id.find_labour_list_group_address);
            groupViewHolder.imgDelete = (ImageView) convertView.findViewById(R.id.find_labour_list_group_delete);
            groupViewHolder.imgEdit = (ImageView) convertView.findViewById(R.id.find_labour_list_group_edit);
            groupViewHolder.imgTalk = (ImageView) convertView.findViewById(R.id.find_labour_list_group_talk);
            groupViewHolder.groupDividerLayout = (LinearLayout) convertView.findViewById(R.id.find_labour_list_group_item_divider);

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final RespLabour respLabour = respLabourList.get(groupPosition);
        groupViewHolder.tvTitle.setText(respLabour.getTitle());
        groupViewHolder.tvTime.setText(respLabour.getDatetime());
        groupViewHolder.tvPublishTime.setText(respLabour.getFb_datetime());
        groupViewHolder.tvContent.setText(respLabour.getTechnique());
        groupViewHolder.tvPeopleNum.setText("人数：" + respLabour.getNums());
        groupViewHolder.tvSalary.setText("薪资：" + respLabour.getSalary());
        groupViewHolder.tvAddress.setText("地址：" + respLabour.getAddress());
        if (AppData.getInstance().getUserEntity().getUsername().equals(respLabour.getUsername())) {
            groupViewHolder.imgEdit.setVisibility(View.VISIBLE);
            groupViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PublishLabourActivity.class);
                    intent.putExtra("title", "修改劳力");
                    intent.putExtra("respLabour", respLabour);
                    mContext.startActivity(intent);
                }
            });
            groupViewHolder.imgDelete.setVisibility(View.VISIBLE);
            groupViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteDialog deleteDialog = new DeleteDialog(mContext, respLabour.getId());
                    deleteDialog.show();
                }
            });
        } else {
            groupViewHolder.imgEdit.setVisibility(View.GONE);
            groupViewHolder.imgDelete.setVisibility(View.GONE);
        }


        groupViewHolder.imgTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyCommetListener.opentCommetEdt(respLabour, groupPosition);
            }
        });
        if (getChildrenCount(groupPosition) == 0) {
            groupViewHolder.groupDividerLayout.setVisibility(View.VISIBLE);
        } else {
            groupViewHolder.groupDividerLayout.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.find_labour_list_child_item, null);
            childViewHolder.dividerLayout = (LinearLayout) convertView.findViewById(R.id.find_labour_list_child_item_divider);
            childViewHolder.openCommentLayout = (LinearLayout) convertView.findViewById(R.id.find_labour_list_child_open_comment);
            childViewHolder.tvName = (TextView) convertView.findViewById(R.id.find_labour_list_child_name);
            childViewHolder.tvComment = (TextView) convertView.findViewById(R.id.find_labour_list_child_content);
            childViewHolder.tvOpenOrClose = (TextView) convertView.findViewById(R.id.find_labour_list_child_open_comment_txt);
            childViewHolder.imgOpenOrClose = (ImageView) convertView.findViewById(R.id.find_labour_list_child_open_comment_img);

            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (isLastChild) {
            childViewHolder.dividerLayout.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.dividerLayout.setVisibility(View.GONE);
        }
        RespLabourComment respLabourComment = respLabourList.get(groupPosition).getComments().get(childPosition);
        childViewHolder.tvName.setText(respLabourComment.getUsername() + "：");
        childViewHolder.tvComment.setText(respLabourComment.getContent());

        if (respLabourList.get(groupPosition).isOpenComments()) { //展开
            if (isLastChild) {
                childViewHolder.openCommentLayout.setVisibility(View.VISIBLE);
                childViewHolder.tvOpenOrClose.setText("收起评论");
                childViewHolder.imgOpenOrClose.setImageResource(R.mipmap.close_comment);
                childViewHolder.openCommentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        respLabourList.get(groupPosition).setOpenComments(false);
                        notifyDataSetChanged();
                    }
                });
            } else {
                childViewHolder.openCommentLayout.setVisibility(View.GONE);
            }
        } else { //  不展开
            if (respLabourList.get(groupPosition).getComments().size() > 3) {
                if (isLastChild) {
                    childViewHolder.openCommentLayout.setVisibility(View.VISIBLE);
                    childViewHolder.tvOpenOrClose.setText("展开评论");
                    childViewHolder.imgOpenOrClose.setImageResource(R.mipmap.open_comment);
                    childViewHolder.openCommentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            respLabourList.get(groupPosition).setOpenComments(true);
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    childViewHolder.openCommentLayout.setVisibility(View.GONE);
                }
            }else {
                childViewHolder.openCommentLayout.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupViewHolder {
        private TextView tvTitle, tvTime, tvPublishTime, tvContent, tvPeopleNum, tvSalary, tvAddress;
        private ImageView imgDelete, imgEdit, imgTalk;
        private LinearLayout groupDividerLayout;
    }

    static class ChildViewHolder {
        private LinearLayout dividerLayout, openCommentLayout;
        private TextView tvName, tvComment, tvOpenOrClose;
        private ImageView imgOpenOrClose;
    }

    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (output == null) {
                return;
            }
            if (output instanceof Dellabour) {
                Dellabour dellabour = (Dellabour) output;
                EUtil.showToast(dellabour.getErr());
                if (dellabour.getRes().equals("0")) {
                    for (int i = 0; i < respLabourList.size(); i++) {
                        if (respLabourList.get(i).getId().equals(input)) {
                            respLabourList.remove(i);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        }
    };

    //删除发布劳力
    class DeleteDialog extends AlertDialog {
        private String respLabourId;

        public DeleteDialog(Context context, String id) {
            super(context);
            this.respLabourId = id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);
            TextView tv = (TextView) findViewById(R.id.tvDialogActivity);
            tv.setText("是否删除该劳力？");

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    ConsumerReqUtil.dellabour(mContext, iApiCallback, null, new Dellabour(), respLabourId, true,
                            "username=" + AppData.getInstance().getUserEntity().getUsername() + "&labourid=" + respLabourId
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

    public interface NotifyCommetListener {
        public abstract void opentCommetEdt(RespLabour respLabour, int pos);
    }

    public void setNotifyCommetListener(NotifyCommetListener notifyCommetListener) {
        this.notifyCommetListener = notifyCommetListener;
    }
}
