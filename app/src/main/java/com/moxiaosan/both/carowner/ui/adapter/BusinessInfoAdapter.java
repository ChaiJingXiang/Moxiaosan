package com.moxiaosan.both.carowner.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

import consumer.api.ConsumerReqUtil;
import consumer.model.Dellabour;
import consumer.model.Delshop;
import consumer.model.obj.RespShop;
import consumer.model.obj.RespShopComment;
import picture.ImageCheckActivity;

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
            if (lists.get(groupPosition).getComments() != null) {
                if (lists.get(groupPosition).isOpenComments()) {
                    return lists.get(groupPosition).getComments().size();
                } else {
                    return lists.get(groupPosition).getComments().size() > 3 ? 3 : lists.get(groupPosition).getComments().size();
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
            convertView = inflater.inflate(R.layout.businessinfo_list_group_item, null);
            groupViewHolder.tvType = (TextView) convertView.findViewById(R.id.businessinfo_list_item_type_txt);
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.sell_thing_list_group_title);
            groupViewHolder.tvTime = (TextView) convertView.findViewById(R.id.sell_thing_list_group_time);
            groupViewHolder.tvServerTime = (TextView) convertView.findViewById(R.id.sell_thing_list_group_server_time);
            groupViewHolder.serverLayout= (LinearLayout) convertView.findViewById(R.id.sell_thing_list_group_server_layout);
            groupViewHolder.tvContactPhone = (TextView) convertView.findViewById(R.id.businessinfo_list_item_contact_phone);
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
        final ArrayList<String> imgsURL = new ArrayList<String>();
        if (respShop.getType().equals("1")){
            groupViewHolder.tvType.setText("买卖信息：");
            groupViewHolder.serverLayout.setVisibility(View.GONE);
            groupViewHolder.tvPeopleNum.setText("数量：" + respShop.getNums());

            groupViewHolder.tvSalary.setText("价格：" + respShop.getPrice());
        }else if (respShop.getType().equals("2")){
            groupViewHolder.tvType.setText("劳力信息：");
            groupViewHolder.serverLayout.setVisibility(View.VISIBLE);
            groupViewHolder.tvServerTime.setText(respShop.getDatetime());
            groupViewHolder.tvPeopleNum.setText("人数：" + respShop.getNums());

            groupViewHolder.tvSalary.setText("薪资：" + respShop.getPrice());
        }

        groupViewHolder.tvTitle.setText(respShop.getTitle());
        groupViewHolder.tvTime.setText(respShop.getFb_datetime());
        groupViewHolder.tvContactPhone.setText(respShop.getContact());
        groupViewHolder.tvContactPhone.setTag(respShop.getContact());
        groupViewHolder.tvContactPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+v.getTag()));
                mContext.startActivity(intent);
            }
        });
        groupViewHolder.tvContent.setText(respShop.getDescribes());


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
            ImageLoader imageLoader = ImageLoader.getInstance();

            if (pics.length > 0) {
                groupViewHolder.imgPic1.setVisibility(View.VISIBLE);
                groupViewHolder.imgPic2.setVisibility(View.GONE);
                groupViewHolder.imgPic3.setVisibility(View.GONE);

                if (groupViewHolder.imgPic1.getTag() != null && groupViewHolder.imgPic1.getTag().equals(pics[0])) {
                    groupViewHolder.imgPic1.setImageResource(0);
                } else {
                    groupViewHolder.imgPic1.setTag(pics[0]);
                    imageLoader.displayImage(pics[0], groupViewHolder.imgPic1);
                }
                for (int i = 0; i < pics.length; i++) {
                    imgsURL.add(pics[i]); //增加图片地址！！！！
                }
                groupViewHolder.imgPic1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageCheckActivity.invokeStartActivity(mContext, imgsURL, 0);
                    }
                });
            }

            if (pics.length > 1) {
                groupViewHolder.imgPic1.setVisibility(View.VISIBLE);
                groupViewHolder.imgPic2.setVisibility(View.VISIBLE);
                groupViewHolder.imgPic3.setVisibility(View.GONE);

                if (groupViewHolder.imgPic2.getTag() != null && groupViewHolder.imgPic2.getTag().equals(pics[1])) {
                    groupViewHolder.imgPic2.setImageResource(0);
                } else {
                    groupViewHolder.imgPic2.setTag(pics[1]);
                    imageLoader.displayImage(pics[1], groupViewHolder.imgPic2);
                }
                groupViewHolder.imgPic2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageCheckActivity.invokeStartActivity(mContext, imgsURL, 1);
                    }
                });
            }
            if (pics.length > 2) {
                groupViewHolder.imgPic1.setVisibility(View.VISIBLE);
                groupViewHolder.imgPic2.setVisibility(View.VISIBLE);
                groupViewHolder.imgPic3.setVisibility(View.VISIBLE);

                if (groupViewHolder.imgPic3.getTag() != null && groupViewHolder.imgPic3.getTag().equals(pics[2])) {
                    groupViewHolder.imgPic3.setImageResource(0);
                } else {
                    groupViewHolder.imgPic3.setTag(pics[2]);
                    imageLoader.displayImage(pics[2], groupViewHolder.imgPic3);
                }
                groupViewHolder.imgPic3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageCheckActivity.invokeStartActivity(mContext, imgsURL, 2);
                    }
                });
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.sell_thing_list_child_item, null);
            childViewHolder.dividerLayout = (LinearLayout) convertView.findViewById(R.id.sell_thing_list_child_item_divider);
            childViewHolder.openCommentLayout = (LinearLayout) convertView.findViewById(R.id.sell_thing_list_child_open_comment);
            childViewHolder.tvName = (TextView) convertView.findViewById(R.id.sell_thing_list_child_name);
            childViewHolder.tvComment = (TextView) convertView.findViewById(R.id.sell_thing_list_child_content);
            childViewHolder.tvOpenOrClose = (TextView) convertView.findViewById(R.id.sell_thing_list_child_open_comment_txt);
            childViewHolder.imgOpenOrClose = (ImageView) convertView.findViewById(R.id.sell_thing_list_child_open_comment_img);

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

        if (lists.get(groupPosition).isOpenComments()) { //展开
            if (isLastChild) {
                childViewHolder.openCommentLayout.setVisibility(View.VISIBLE);
                childViewHolder.tvOpenOrClose.setText("收起评论");
                childViewHolder.imgOpenOrClose.setImageResource(R.mipmap.close_comment);
                childViewHolder.openCommentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lists.get(groupPosition).setOpenComments(false);
                        notifyDataSetChanged();
                    }
                });
            } else {
                childViewHolder.openCommentLayout.setVisibility(View.GONE);
            }
        } else { //  不展开
            if (lists.get(groupPosition).getComments().size() > 3) {
                if (isLastChild) {
                    childViewHolder.openCommentLayout.setVisibility(View.VISIBLE);
                    childViewHolder.tvOpenOrClose.setText("展开评论");
                    childViewHolder.imgOpenOrClose.setImageResource(R.mipmap.open_comment);
                    childViewHolder.openCommentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lists.get(groupPosition).setOpenComments(true);
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    childViewHolder.openCommentLayout.setVisibility(View.GONE);
                }
            }else{
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
        private TextView tvType, tvTitle, tvServerTime, tvTime, tvContactPhone, tvContent, tvPeopleNum, tvSalary, tvAddress;
        private ImageView imgDelete, imgEdit, imgTalk, imgPic1, imgPic2, imgPic3;
        private LinearLayout groupDividerLayout, picsLayout, serverLayout;
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
