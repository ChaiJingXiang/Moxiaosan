package com.utils.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.moxiaosan.commonlibrary.R;

public class EProgressDialog extends Dialog {


	public EProgressDialog(Context context) {
		super(context, R.style.LarkLoadingDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_loading);
		ImageView rocketImage = (ImageView) findViewById(R.id.loading);  
        AnimationDrawable rocketAnimation = (AnimationDrawable)rocketImage.getBackground(); 
        rocketAnimation.start();
	}
	
	public void setMessage(String message) {

	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}
	
	@Override
	public void show() {
		super.show();
	}
}
