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
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;

/**
 * @Title: CustomPlayViewMine.java 
 *
 * @Description: 个人主页主人态播放控件
 *
 * @author allen  
 *
 * @date 2014-8-29 下午5:26:28 
 *
 * @version V1.0   
 */
public class CustomPlayViewMine extends RelativeLayout {

	private TextView tv_play;
	private ImageView pb_view;
	private ImageView iv_replay;
	private AnimationDrawable rocketAnimation;

	private Context context;
	private ProgressBar pb;
	private boolean isClickSure;

	public boolean isClickSure() {
		return isClickSure;
	}

	public CustomPlayViewMine(Context context) {
		super(context);
		this.context = context;
		initUI(context);
	}

	public CustomPlayViewMine(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initUI(context);
	}

	private void initUI(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_video_play_mine, null);
		tv_play = (TextView) view.findViewById(R.id.view_mine_video_play_tv);
		pb_view = (ImageView) view.findViewById(R.id.view_mine_video_play_pb);
		iv_replay = (ImageView) view.findViewById(R.id.view_mine_video_replay);
		pb = (ProgressBar) view.findViewById(R.id.view_mine_voice_pb);
		addView(view);
	}

	public void clickImage(String videoKey, int status) {
		switch (status) {
		case 0:
			setVoiceTime(0, 60);
			setPlayImage(R.drawable.homepage_introduction_record_selector);
			break;
		case 1:
			//录制
			setPlayImage(R.drawable.homepage_introduction_recording_selector);
			pb_view.setVisibility(View.VISIBLE);
			pb_view.setBackgroundResource(R.anim.view_play_red);
			rocketAnimation = (AnimationDrawable) pb_view.getBackground();
			rocketAnimation.start();
			break;
		case 2:
			setPlayImage(R.drawable.homepage_introduction_playing2_selector);
			pb_view.setVisibility(View.VISIBLE);
			pb_view.setBackgroundResource(R.anim.view_play_green);
			rocketAnimation = (AnimationDrawable) pb_view.getBackground();
			rocketAnimation.start();
			break;
		case 3:
		case 4:
			setPlayImage(R.drawable.homepage_introduction_pasue2_selector);
			pb_view.setVisibility(View.GONE);
			if (rocketAnimation != null)
				rocketAnimation.stop();
			break;
		default:
			break;
		}
	}

	public void clickReplay() {
		setPlayImage(R.drawable.homepage_introduction_recording_selector);
		pb_view.setVisibility(View.VISIBLE);
		pb_view.setBackgroundResource(R.anim.view_play_red);
		rocketAnimation = (AnimationDrawable) pb_view.getBackground();
		rocketAnimation.start();
		setVoiceTime(0, 60);
		iv_replay.setImageResource(R.drawable.homepage_introduction_recorded_dis);
		iv_replay.setClickable(false);
	}

	public void setDefault(int curr, int time, String path) {
		if (TextUtils.isEmpty(path)) {
			setVoiceTime(curr, time);
			setPlayImage(R.drawable.homepage_introduction_record_selector);
			iv_replay.setImageResource(R.drawable.homepage_introduction_recorded_dis);
			iv_replay.setClickable(false);
		} else {
			setVoiceTime(curr, time);
			setPlayImage(R.drawable.homepage_introduction_pasue2_selector);
			iv_replay.setImageResource(R.drawable.homepage_introduction_recorded_selector);
			iv_replay.setClickable(true);
		}
		pb_view.setVisibility(View.GONE);
	}

	public void setPlayImage(int rid) {
		tv_play.setBackgroundResource(rid);
		tv_play.setPadding(BaseUtils.dip2px(context, 65), 0, BaseUtils.dip2px(context, 10), 0);
	}

	private void setVoiceTime(int curr, int time) {
		tv_play.setText(curr + "/" + time + "“");
	}

	public void setVoice(int curr, int time, int rid) {
		setVoiceTime(curr, time);
		switch (rid) {
		case 0:
			setPlayImage(R.drawable.homepage_introduction_record_selector);
			break;
		case 1:
			setPlayImage(R.drawable.homepage_introduction_recording_selector);
			break;
		case 2:
			setPlayImage(R.drawable.homepage_introduction_playing2_selector);
			break;
		case 3:
		case 4:
			setPlayImage(R.drawable.homepage_introduction_pasue2_selector);
			break;
		default:
			break;
		}
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
