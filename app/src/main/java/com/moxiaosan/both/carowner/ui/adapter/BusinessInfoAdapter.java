package com.moxiaosan.both.carowner.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.consumer.ui.activity.PublishThingActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;

import java.util.List;

import consumer.api.ConsumerReqUtil;
import consumer.model.Dellabour;
import consumer.model.Delshop;
import consumer.model.obj.RespShop;
import consumer.model.obj.RespShopComment;

/**
 * Created by chris on 16/3/17.
 */
public class BusinessInfoAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private List<RespShop> lists;

    private NotifyBusinessInfoCommetListener notifyBusinessInfoCommetListene;

    public BusinessInfoAdapter(Context mContext, List<RespShop> respShopList) {
        this.mContext = mContext;
        this.lists = respShopList;
        inflater = LayoutInflater.from(mContext);
    }

    public void refreshData(List<RespShop> respShops) {
        this.lists = respShops;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (lists.get(groupPosition) != null) {
            return lists.get(groupPosition).getComments() == null ? 0 :
                    lists.get(groupPosition).getComments().size() > 3 ? 3 : lists.get(groupPosition).getComments().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lists.get(groupPosition).getComments().get(childPosition);
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
            convertView = inflater.inflate(R.layout.sell_thing_list_group_item, null);
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.sell_thing_list_group_title);
            groupViewHolder.tvTime = (TextView) convertView.findViewById(R.id.sell_thing_list_group_time);
            groupViewHolder.tvContent = (TextView) convertView.findViewById(R.id.sell_thing_list_group_content);
            groupViewHolder.tvPeopleNum = (TextView) convertView.findViewById(R.id.sell_thing_list_group_people_num);
            groupViewHolder.tvSalary = (TextView) convertView.findViewById(R.id.sell_thing_list_group_salary);
            groupViewHolder.tvAddress = (TextView) convertView.findViewById(R.id.sell_thing_list_group_address);
            groupViewHolder.imgDelete = (ImageView) convertView.findViewById(R.id.sell_thing_list_group_delete);
            groupViewHolder.imgEdit = (ImageView) convertView.findViewById(R.id.sell_thing_list_group_edit);
            groupViewHolder.imgTalk = (ImageView) convertView.findViewById(R.id.sell_thing_list_group_talk);
            groupViewHolder.groupDividerLayout = (LinearLayout) convertView.findViewById(R.id.sell_thing_list_group_item_divider);
            groupViewHolder.picsLayout = (LinearLayout) convertView.findViewById(R.id.sell_thing_list_group_pics_layout);
            groupViewHolder.imgPic1 = (ImageView) convertView.findViewById(R.id.sell_thing_list_group_pic1);
            groupViewHolder.imgPic2 = (ImageView) convertView.findViewById(R.id.sell_thing_list_group_pic2);
            groupViewHolder.imgPic3 = (ImageView) convertView.findViewById(R.id.sell_thing_list_group_pic3);

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final RespShop respShop = lists.get(groupPosition);
        groupViewHolder.tvTitle.setText(respShop.getTitle());
        groupViewHolder.tvTime.setText(respShop.getFb_datetime());
        groupViewHolder.tvContent.setText(respShop.getDescribes());
        groupViewHolder.tvPeopleNum.setText("数量：" + respShop.getNums() + "个");
        groupViewHolder.tvSalary.setText("价格：" + respShop.getPrice());
        groupViewHolder.tvAddress.setText("地址：" + respShop.getAddress());
        if (AppData.getInstance().getUserEntity().getUsername().equals(respShop.getUsername())) {
            groupViewHolder.imgEdit.setVisibility(View.VISIBLE);
            groupViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PublishThingActivity.class);
                    intent.putExtra("title", "修改商品");
                    intent.putExtra("respShop", respShop);
                    mContext.startActivity(intent);
                }
            });
            groupViewHolder.imgDelete.setVisibility(View.VISIBLE);
            groupViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteDialog deleteDialog = new DeleteDialog(mContext, respShop.getId());
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
                notifyBusinessInfoCommetListene.opentCommetEdt(respShop,groupPosition);
            }
        });
        if (getChildrenCount(groupPosition) == 0) {
            groupViewHolder.groupDividerLayout.setVisibility(View.VISIBLE);
        } else {
            groupViewHolder.groupDividerLayout.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(respShop.getPictures())) {
            groupViewHolder.picsLayout.setVisibility(View.GONE);
        } else {
            groupViewHolder.picsLayout.setVisibility(View.VISIBLE);
            String[] pics = respShop.getPictures().split(",");
            if (pics.length == 1) {
                ImageLoader.getInstance().displayImage(pics[0], groupViewHolder.imgPic1);
            } else if (pics.length == 2) {
                ImageLoader.getInstance().displayImage(pics[0], groupViewHolder.imgPic1);
                ImageLoader.getInstance().displayImage(pics[1], groupViewHolder.imgPic2);
            } else if (pics.length == 3) {
                ImageLoader.getInstance().displayImage(pics[0], groupViewHolder.imgPic1);
                ImageLoader.getInstance().displayImage(pics[1], groupViewHolder.imgPic2);
                ImageLoader.getInstance().displayImage(pics[2], groupViewHolder.imgPic3);
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.sell_thing_list_child_item, null);
            childViewHolder.dividerLayout = (LinearLayout) convertView.findViewById(R.id.sell_thing_list_child_item_divider);
            childViewHolder.openCommentLayout = (LinearLayout) convertView.findViewById(R.id.sell_thing_list_child_open_comment);
            childViewHolder.tvName = (TextView) convertView.findViewById(R.id.sell_thing_list_child_name);
            childViewHolder.tvComment = (TextView) convertView.findViewById(R.id.sell_thing_list_child_content);

            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (isLastChild) {
            childViewHolder.dividerLayout.setVisibility(View.VISIBLE);
        }else {
            childViewHolder.dividerLayout.setVisibility(View.GONE);
        }
        RespShopComment respShopComment = lists.get(groupPosition).getComments().get(childPosition);
        childViewHolder.tvName.setText(respShopComment.getUsername() + "：");
        childViewHolder.tvComment.setText(respShopComment.getCommentstext());
        if (lists.get(groupPosition).getComments().size() > 3) {
            if(isLastChild){
                childViewHolder.openCommentLayout.setVisibility(View.VISIBLE);
                childViewHolder.openCommentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EUtil.showToast("展开更多");
                    }
                });
            }

        } else {
            childViewHolder.openCommentLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        private TextView tvTitle, tvTime, tvContent, tvPeopleNum, tvSalary, tvAddress;
        private ImageView imgDelete, imgEdit, imgTalk, imgPic1, imgPic2, imgPic3;
        private LinearLayout groupDividerLayout, picsLayout;
    }

    class ChildViewHolder {
        private LinearLayout dividerLayout, openCommentLayout;
        private TextView tvName, tvComment;
    }

    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (output == null) {
                return;
            }
            if (output instanceof Delshop) {
                Delshop delshop = (Delshop) output;
                EUtil.showToast(delshop.getErr());
                if (delshop.getRes().equals("0")) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getId().equals(input)) {
                            lists.remove(i);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        }
    };

    //删除卖东西列表
    class DeleteDialog extends AlertDialog {
        private String respShopId;

        public DeleteDialog(Context context, String id) {
            super(context);
            this.respShopId = id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);
            TextView tv = (TextView) findViewById(R.id.tvDialogActivity);
            tv.setText("是否删除该商品？");

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    ConsumerReqUtil.delshop(mContext, iApiCallback, null, new Dellabour(), respShopId, true,
                            "username=" + AppData.getInstance().getUserEntity().getUsername() + "&shopid=" + respShopId
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

    public interface NotifyBusinessInfoCommetListener {
        public abstract void opentCommetEdt(RespShop respShop, int pos);
    }

    public void setNotifyCommetListener(NotifyBusinessInfoCommetListener notifyBusinessInfoCommetListene) {
        this.notifyBusinessInfoCommetListene = notifyBusinessInfoCommetListene;
    }
}
