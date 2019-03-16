package com.tshang.peipei.activity.dialog;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: BalloonAnimationDialog.java 
 *
 * @Description: 热气球特效
 *
 * @author DYH  
 *
 * @date 2015-12-3 下午6:01:20 
 *
 * @version V1.0   
 */
public class BalloonAnimationDialog extends Dialog implements OnDismissListener {
	private BroadcastQueue broadcast;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private int uid1, uid2;
	
	private FrameLayout fl_balloon;
	private ImageView iv_balloon_left_head;
	private ImageView iv_balloon_right_head;
	private ImageView iv_balloon1;
	private ImageView iv_balloon2;
	private ImageView iv_balloon3;
	
	public BalloonAnimationDialog(Context context) {
		super(context);
	}
	
	public BalloonAnimationDialog(Activity context, int theme, int uid1, int uid2, BroadcastQueue broadcast) {
		super(context, theme);
		this.broadcast = broadcast;
		this.uid1 = uid1;
		this.uid2 = uid2;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByW(context, 30);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_balloon_animation);
		
		fl_balloon = (FrameLayout) findViewById(R.id.fl_balloon);
		iv_balloon_left_head = (ImageView) findViewById(R.id.iv_balloon_left_head);
		iv_balloon_right_head = (ImageView) findViewById(R.id.iv_balloon_right_head);
		iv_balloon1 = (ImageView) findViewById(R.id.iv_balloon1);
		iv_balloon2 = (ImageView) findViewById(R.id.iv_balloon2);
		iv_balloon3 = (ImageView) findViewById(R.id.iv_balloon3);
		
		String key1 = uid1 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		String key2 = uid2 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + key1, iv_balloon_left_head, options_head);
		imageLoader.displayImage("http://" + key2, iv_balloon_right_head, options_head);
		
		startAnim();
		BalloonAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		final AnimatorSet animSet = new AnimatorSet();
		Random random = new Random();
		int duration = random.nextInt(5000) + 1000;
		ObjectAnimator balloonAnimator1 = ObjectAnimator.ofFloat(iv_balloon1, "translationY", 0.0f, -500f);
		duration = random.nextInt(1000) + 1000;
		balloonAnimator1.setDuration(duration);
		balloonAnimator1.setStartDelay(random.nextInt(300));
		ObjectAnimator balloonAnimator2 = ObjectAnimator.ofFloat(iv_balloon2, "translationY", 0.0f, -700f);
		duration = random.nextInt(1000) + 1500;
		balloonAnimator2.setDuration(duration);
		balloonAnimator2.setStartDelay(random.nextInt(300));
		ObjectAnimator balloonAnimator3 = ObjectAnimator.ofFloat(iv_balloon3, "translationY", 0.0f, -200f);
		duration = random.nextInt(1000) + 500;
		balloonAnimator3.setDuration(duration);
		balloonAnimator3.setStartDelay(random.nextInt(300));
		animSet.play(balloonAnimator1).with(balloonAnimator2).with(balloonAnimator3);
		animSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				BalloonAnimationDialog.this.dismiss();
			}
		});
		
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fl_balloon, "translationY", 1000f, 0f);
		objectAnimator.setDuration(1000);
		objectAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				animSet.start();
			}
		});
		objectAnimator.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.dismiss();
		return true;
	}
	
	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.width = WindowManager.LayoutParams.MATCH_PARENT;
		wlps.height = WindowManager.LayoutParams.MATCH_PARENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onDismiss(DialogInterface arg0) {
		broadcast.setSynchWork(false);
	}

}
