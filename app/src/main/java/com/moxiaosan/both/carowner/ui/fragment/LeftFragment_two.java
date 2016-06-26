package com.moxiaosan.both.carowner.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.BecomeCarOwnerActivity;
import com.moxiaosan.both.carowner.ui.activity.CarOwnerInfoActivity;
import com.moxiaosan.both.carowner.ui.activity.SettingActivity;
import com.moxiaosan.both.common.ui.activity.AboutUsActivity;
import com.moxiaosan.both.common.ui.activity.AddGPSPhoneActivity;
import com.moxiaosan.both.common.ui.activity.MessagesActivity;
import com.moxiaosan.both.common.ui.activity.MyWalletActivity;
import com.moxiaosan.both.common.ui.activity.ShareActivity;
import com.moxiaosan.both.common.ui.activity.UseHelpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.image.RoundImageView;
import com.utils.ui.base.BaseFragment_v4;

import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.Userinfo;

/**
 * Created by chris on 16/2/29.
 */
public class LeftFragment_two extends BaseFragment_v4 implements View.OnClickListener, IApiCallback {
    private RoundImageView imgHead;
    private TextView tvNickName, phoneNumber, tvYunying;
    private SlidingMenu slidingMen;
    private Userinfo userinfo;
    private ExitDialog dialog;

    public LeftFragment_two() {

    }

    @SuppressLint("ValidFragment")
    public LeftFragment_two(SlidingMenu slidingMenu) {
        this.slidingMen = slidingMenu;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        CarReqUtils.personalInfo(getActivity(),this,null,new Userinfo(),"PersonalInfo",true,
//                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_menu, null);
        imgHead = (RoundImageView) view.findViewById(R.id.left_frag_user_photo);
        tvNickName = (TextView) view.findViewById(R.id.left_frag_nickname);
        phoneNumber = (TextView) view.findViewById(R.id.left_frag_phone_num);

        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        CarReqUtils.personalInfo(getActivity(), this, null, new Userinfo(), "PersonalInfo", true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));
//        if(!TextUtils.isEmpty(AppData.getInstance().getUserEntity().getHeadportrait())){
//            ImageLoader.getInstance().displayImage(AppData.getInstance().getUserEntity().getHeadportrait(),imgHead);
//        }

    }

    private void initView(View view) {
        view.findViewById(R.id.left_frag_user_layout).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout1).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout2).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout3).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout4).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout5).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout6).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout7).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout8).setOnClickListener(this);
        tvYunying = (TextView) view.findViewById(R.id.left_menu_changeto_yunying);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_frag_user_layout:
                startActivity(new Intent(getActivity(), CarOwnerInfoActivity.class));
                slidingMen.toggle();
                break;
            case R.id.left_frag_menu_layout1:
                startActivity(new Intent(getActivity(), MyWalletActivity.class));
                slidingMen.toggle();
                break;
            case R.id.left_frag_menu_layout2:
                startActivity(new Intent(getActivity(), MessagesActivity.class));
                slidingMen.toggle();
                break;
            case R.id.left_frag_menu_layout3:
                startActivity(new Intent(getActivity(), AddGPSPhoneActivity.class));
                slidingMen.toggle();
                break;
            case R.id.left_frag_menu_layout4:
                SharedPreferences sp = getActivity().getSharedPreferences("request", Activity.MODE_PRIVATE);

                boolean flag = sp.getBoolean("carer", false);
                if (flag && AppData.getInstance().getUserEntity().getType() == 2) {  //在审核中   2普通车主
                    dialog = new ExitDialog(getActivity(), 2);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else { // getType()=2  或者  getType()=3
                    if (userinfo.getData().getAppstatus().equals("1") || userinfo.getData().getAppstatus().equals("2") || userinfo.getData().getAppstatus().equals("5")
                            || userinfo.getData().getAppstatus().equals("4")) {
                        //0申请中 1未通过 2 通过 4是未申请过 5 修改申请未通过
                        //已经是运营车主去修改资料或者申请成为运营车主
                        startActivity(new Intent(getActivity(), BecomeCarOwnerActivity.class).putExtra("appstatus", userinfo.getData().getAppstatus()));
                    } else if (userinfo.getData().getAppstatus().equals("0")){
                        dialog = new ExitDialog(getActivity(), 2);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }else if (userinfo.getData().getAppstatus().equals("3")){
                        EUtil.showToast("车主暂停使用");
                    }
                }

                break;
            case R.id.left_frag_menu_layout5:
                startActivity(new Intent(getActivity(), UseHelpActivity.class));
                slidingMen.toggle();
                break;
            case R.id.left_frag_menu_layout6:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                slidingMen.toggle();
                break;
            case R.id.left_frag_menu_layout7:
                if (AppData.getInstance().getUserEntity().getBind() == 1) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    slidingMen.toggle();
                } else {

                    EUtil.showToast("未绑定设备,请先绑定设备");
                }

                break;
            case R.id.left_frag_menu_layout8:
                Intent shareIntent = new Intent(getActivity(), ShareActivity.class);
                shareIntent.putExtra("title", "推荐应用摩小三给你");
                shareIntent.putExtra("content", "农村移动互联网众包服务第一平台，同城速递，农村顺风车，找劳力，卖农货，请用摩小三。详情访问:http://www.moxiaosan.com");
                shareIntent.putExtra("imgPath", "app_log");
                shareIntent.putExtra("targetUrl", "http://www.moxiaosan.com");
                this.startActivity(shareIntent);
                getActivity().overridePendingTransition(R.anim.share_pop_in, 0);
                break;
        }
    }

    @Override
    public void onData(Object output, Object input) {
        if (output != null) {
            if (output instanceof Userinfo) {
                userinfo = (Userinfo) output;
                dismissLoadingDialog();
                if (userinfo.getRes() == 0) {
//                    EUtil.showToast(userinfo.getErr());
                    if (!TextUtils.isEmpty(userinfo.getData().getHeadportrait())) {
                        ImageLoader.getInstance().displayImage(userinfo.getData().getHeadportrait(), imgHead);
                    }

                    tvNickName.setText(userinfo.getData().getNickname());
                    phoneNumber.setText(userinfo.getData().getContact());
                    if (userinfo.getData().getAppstatus().equals("2") || userinfo.getData().getAppstatus().equals("5")) {
                        //0申请中 1未通过 2 通过 4是未申请过 5 修改申请未通过
                        tvYunying.setText("修改运营资料");
                    } else {
                        tvYunying.setText("成为运营车主");
                    }
                }
            }
        } else {
            EUtil.showToast("网络错误，请稍后重试");
        }
    }

    // dialog
    class ExitDialog extends AlertDialog {
        int index;

        public ExitDialog(Context context, int index) {
            super(context);

            this.index = index;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);

            TextView textView = (TextView) findViewById(R.id.tvDialogActivity);

            if (index == 2) {
                textView.setText("你已提交申请，请等待审核");
            }

            ViewGroup.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = getActivity().getWindowManager().getDefaultDisplay().getWidth();

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (AppData.getInstance().getUserEntity().getUserType() == 3) {
//                        if (AppData.getInstance().getUserEntity().getType() == 3) {
//                            dismiss();
//                            slidingMen.toggle();
//                        } else {
//                            dismiss();
//                            startActivity(new Intent(getActivity(), BusinessMainActivity.class));
//                            ActivityHolder.getInstance().pop(new GPSSafeCenterActivity());
//                        }
//
//                    } else {
                    dismiss();
//                        slidingMen.toggle();
//                    }

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
