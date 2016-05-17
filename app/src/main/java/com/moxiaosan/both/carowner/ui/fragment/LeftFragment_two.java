package com.moxiaosan.both.carowner.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.moxiaosan.both.APP;
import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.BecomeCarOwnerActivity;
import com.moxiaosan.both.carowner.ui.activity.BusinessMainActivity;
import com.moxiaosan.both.carowner.ui.activity.CarOwnerInfoActivity;
import com.moxiaosan.both.carowner.ui.activity.GPSSafeCenterActivity;
import com.moxiaosan.both.common.ui.activity.AddGPSPhoneActivity;
import com.moxiaosan.both.common.ui.activity.AboutUsActivity;
import com.moxiaosan.both.common.ui.activity.MessagesActivity;
import com.moxiaosan.both.common.ui.activity.MyWalletActivity;
import com.moxiaosan.both.common.ui.activity.ShareActivity;
import com.moxiaosan.both.common.ui.activity.UseHelpActivity;
import com.moxiaosan.both.carowner.ui.activity.SettingActivity;
import com.moxiaosan.both.consumer.ui.activity.ConsumerMainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.api.IApiCallback;
import com.utils.common.AppData;
import com.utils.common.EUtil;
import com.utils.image.RoundImageView;
import com.utils.ui.base.ActivityHolder;
import com.utils.ui.base.BaseFragment_v4;

import consumer.HashMapUtils;
import consumer.StringUrlUtils;
import consumer.api.CarReqUtils;
import consumer.model.Mqtt;
import consumer.model.Userinfo;

/**
 * Created by chris on 16/2/29.
 */
public class LeftFragment_two extends BaseFragment_v4 implements View.OnClickListener,IApiCallback{
    private RoundImageView imgHead;
    private TextView tvNickName,phoneNumber;
    SlidingMenu slidingMen;

    public LeftFragment_two() {

    }
    @SuppressLint("ValidFragment")
    public LeftFragment_two(SlidingMenu slidingMenu) {
            this.slidingMen=slidingMenu;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CarReqUtils.personalInfo(getActivity(),this,null,new Userinfo(),"PersonalInfo",true,
                StringUrlUtils.geturl(hashMapUtils.putValue("username", AppData.getInstance().getUserEntity().getUsername()).createMap()));

    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.left_menu,null);
        imgHead =(RoundImageView)view.findViewById(R.id.left_frag_user_photo);
        tvNickName =(TextView)view.findViewById(R.id.left_frag_nickname);
        phoneNumber =(TextView)view.findViewById(R.id.left_frag_phone_num);

        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!TextUtils.isEmpty(AppData.getInstance().getUserEntity().getHeadportrait())){
            ImageLoader.getInstance().displayImage(AppData.getInstance().getUserEntity().getHeadportrait(),imgHead);
        }

    }

    private void initView(View view){
        view.findViewById(R.id.left_frag_user_layout).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout1).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout2).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout3).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout4).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout5).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout6).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout7).setOnClickListener(this);
        view.findViewById(R.id.left_frag_menu_layout8).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                SharedPreferences sp =getActivity().getSharedPreferences("request", Activity.MODE_PRIVATE);

                boolean flag =sp.getBoolean("carer",false);

                if(AppData.getInstance().getUserEntity().getUserType()==3){
                    ExitDialog dialog =new ExitDialog(getActivity(),1);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }else if(flag && AppData.getInstance().getUserEntity().getType()==2){

                    ExitDialog dialog =new ExitDialog(getActivity(),2);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }else{

                    startActivity(new Intent(getActivity(), BecomeCarOwnerActivity.class));
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
                if(AppData.getInstance().getUserEntity().getBind()==1){
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    slidingMen.toggle();
                }else{

                    EUtil.showToast("未绑定设备,请先绑定设备");
                }

                break;
            case R.id.left_frag_menu_layout8:
                Intent shareIntent = new Intent(getActivity(), ShareActivity.class);
                this.startActivity(shareIntent);
                getActivity().overridePendingTransition(R.anim.share_pop_in, 0);
                break;
        }
    }

    @Override
    public void onData(Object output, Object input) {
        if(output!=null){
            if(output instanceof Userinfo){
                Userinfo userinfo =(Userinfo)output;
                dismissLoadingDialog();
                if(userinfo.getRes()==0){
//                    EUtil.showToast(userinfo.getErr());
                    if(!TextUtils.isEmpty(userinfo.getData().getHeadportrait())){
                        ImageLoader.getInstance().displayImage(userinfo.getData().getHeadportrait(),imgHead);
                    }

                    tvNickName.setText(userinfo.getData().getNickname());
                    phoneNumber.setText(userinfo.getData().getContact());
                }
            }
        }else{
            EUtil.showToast("网络错误，请稍后重试");
        }
    }

    // dialog
    class ExitDialog extends AlertDialog {
        int index;


        public ExitDialog(Context context,int index) {
            super(context);

            this.index =index;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_setting_exit);

            TextView textView =(TextView)findViewById(R.id.tvDialogActivity);

            if(index ==2){
                textView.setText("你已提交申请，请等待审核");
            }else{
                textView.setText("你已经是运营车主，去接单");
            }

            ViewGroup.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = getActivity().getWindowManager().getDefaultDisplay().getWidth();

            findViewById(R.id.setting_exit_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppData.getInstance().getUserEntity().getUserType() ==3){
                        if(AppData.getInstance().getUserEntity().getType()==3){
                            dismiss();
                            slidingMen.toggle();
                        }else{
                            dismiss();
                            startActivity(new Intent(getActivity(), BusinessMainActivity.class));
                            ActivityHolder.getInstance().pop(new GPSSafeCenterActivity());
                        }

                    }else{
                        dismiss();
                        slidingMen.toggle();
                    }

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
