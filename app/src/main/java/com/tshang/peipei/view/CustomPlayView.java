package com.tshang.peipei.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.dialog.HintToastDialog;

/**
 * @Title: CustomPlayView.java 
 *
 * @Description: 个人主页播放按键  
 *
 * @author allen
 *
 * @date 2014-8-29 下午3:10:15 
 *
 * @version V1.0   
 */
public class CustomPlayView extends RelativeLayout {

	private ImageView ivPlay;
	private ImageView pbPlay;

	private Context context;
	private ProgressBar pb;
	private boolean isClickSure;

	public boolean isClickSure() {
		return isClickSure;
	}

	private AnimationDrawable rocketAnimation;

	public CustomPlayView(Context context) {
		super(context);
		this.context = context;
		initUI(context);
	}

	public CustomPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initUI(context);
	}

	private void initUI(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_video_play, null);

		ivPlay = (ImageView) view.findViewById(R.id.view_custom_video_play_iv);
		pbPlay = (ImageView) view.findViewById(R.id.view_custom_video_play_pb);
		pb = (ProgressBar) view.findViewById(R.id.view_custom_voice_pb);

		rocketAnimation = (AnimationDrawable) pbPlay.getBackground();
		addView(view);
	}

	public void clickImage(String videoKey, int status) {
		switch (status) {
		case 0:
			setPlayImage(R.drawable.homepage_introduction_unrecord_selector);
			pbPlay.setVisibility(View.GONE);
			new HintToastDialog(context, R.string.str_custom_no_video, R.string.ok).showDialog();
			break;
		case 2:
			setPlayImage(R.drawable.homepage_introduction_playing_selector);
			pbPlay.setVisibility(View.VISIBLE);
			rocketAnimation.start();
			break;
		case 3:
		case 4:
			setPlayImage(R.drawable.homepage_introduction_pasue_selector);
			pbPlay.setVisibility(View.GONE);
			rocketAnimation.stop();
			break;
		default:
			break;
		}
	}

	public void setDefault(String path) {
		if (TextUtils.isEmpty(path)) {
			setPlayImage(R.drawable.homepage_introduction_unrecord_selector);
		} else {
			setPlayImage(R.drawable.homepage_introduction_pasue_selector);
		}
		pbPlay.setVisibility(View.GONE);
	}

	public void setPlayImage(int rid) {
		ivPlay.setImageResource(rid);
	}

	public void setProcessbar(boolean b) {
		isClickSure = b;
		if (b) {
			pb.setVisibility(View.VISIBLE);
		} else {
			pb.setVisibility(View.GONE);
		}
	}

}
