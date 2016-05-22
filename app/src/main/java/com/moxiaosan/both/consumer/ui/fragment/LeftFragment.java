package com.moxiaosan.both.consumer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxiaosan.both.R;
import com.moxiaosan.both.common.ui.activity.AboutUsActivity;
import com.moxiaosan.both.common.ui.activity.AddGPSPhoneActivity;
import com.moxiaosan.both.common.ui.activity.MessagesActivity;
import com.moxiaosan.both.common.ui.activity.MyWalletActivity;
import com.moxiaosan.both.common.ui.activity.ShareActivity;
import com.moxiaosan.both.common.ui.activity.UseHelpActivity;
import com.moxiaosan.both.consumer.ui.activity.leftmenu.MyAppraiseActivity;
import com.moxiaosan.both.consumer.ui.activity.leftmenu.UserSettingActivity;
import com.moxiaosan.both.consumer.ui.activity.personalcenter.PersonInfoActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;

import consumer.api.UserReqUtil;
import consumer.model.Userinfo;

/**
 * Created by qiangfeng on 16/2/29.
 */
public class LeftFragment extends Fragment implements View.OnClickListener {
    private ImageView imgPhoto;
    private TextView tvNickName, tvPhone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_menu_consumer, null);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("LeftFragment", "onResume");
        UserReqUtil.userinfo(getActivity(), iApiCallback, null, new Userinfo(), "LeftFragment", true, "username=" + AppData.getInstance().getUserEntity().getUsername());
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.i("LeftFragment", "onStart");
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
        imgPhoto = (ImageView) view.findViewById(R.id.left_frag_user_photo);
        tvNickName = (TextView) view.findViewById(R.id.left_frag_nickname);
        tvPhone = (TextView) view.findViewById(R.id.left_frag_phone_num);
    }

    IApiCallback iApiCallback = new IApiCallback() {
        @Override
        public void onData(Object output, Object input) {
            if (output == null) {
                return;
            }
            if (output instanceof Userinfo) {
                Userinfo userinfo = (Userinfo) output;
                if (!TextUtils.isEmpty(userinfo.getData().getHeadportrait())) {
                    ImageLoader.getInstance().displayImage(userinfo.getData().getHeadportrait(), imgPhoto);
                }
                tvNickName.setText(userinfo.getData().getNickname());
                tvPhone.setText(userinfo.getData().getUsername());
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_frag_user_layout:
                startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
            case R.id.left_frag_menu_layout1:
                startActivity(new Intent(getActivity(), MyAppraiseActivity.class));
                break;
            case R.id.left_frag_menu_layout2:
                startActivity(new Intent(getActivity(), MyWalletActivity.class).putExtra("userType", AppData.getInstance().getUserEntity().getUserType()));
                break;
            case R.id.left_frag_menu_layout3:
                startActivity(new Intent(getActivity(), UseHelpActivity.class));
                break;
            case R.id.left_frag_menu_layout4:
                startActivity(new Intent(getActivity(), MessagesActivity.class));
                break;
            case R.id.left_frag_menu_layout5:
                startActivity(new Intent(getActivity(), AddGPSPhoneActivity.class));
                break;
            case R.id.left_frag_menu_layout6:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.left_frag_menu_layout7:
                startActivity(new Intent(getActivity(), UserSettingActivity.class));
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
}
