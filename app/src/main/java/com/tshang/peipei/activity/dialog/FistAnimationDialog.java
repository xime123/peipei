package com.tshang.peipei.activity.dialog;

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
import android.widget.ImageView;

/**
 * @Title: FistAnimationDialog.java 
 *
 * @Description: 天马流星拳仙术
 *
 * @author DYH  
 *
 * @date 2015-12-4 下午5:20:26 
 *
 * @version V1.0   
 */
public class FistAnimationDialog extends Dialog implements OnDismissListener {

	private BroadcastQueue broadcast;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private int uid1;

	private ImageView iv_left_bubble;
	private ImageView iv_right_bubble;
	private ImageView iv_left_fist;
	private ImageView iv_right_fist;
	private ImageView iv_left_blast;
	private ImageView iv_right_blast;
	private ImageView iv_head;

	public FistAnimationDialog(Context context) {
		super(context);
	}

	public FistAnimationDialog(Activity context, int theme, int uid1, BroadcastQueue broadcast) {
		super(context, theme);
		this.broadcast = broadcast;
		this.uid1 = uid1;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByW(context, 70);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_fist_animation);

		iv_head = (ImageView) findViewById(R.id.iv_head);
		iv_left_bubble = (ImageView) findViewById(R.id.iv_left_bubble);
		iv_right_bubble = (ImageView) findViewById(R.id.iv_right_bubble);
		iv_left_fist = (ImageView) findViewById(R.id.iv_left_fist);
		iv_right_fist = (ImageView) findViewById(R.id.iv_right_fist);
		iv_left_blast = (ImageView) findViewById(R.id.iv_left_blast);
		iv_right_blast = (ImageView) findViewById(R.id.iv_right_blast);

		String key1 = uid1 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + key1, iv_head, options_head);

		startAnim();
		FistAnimationDialog.this.setOnDismissListener(this);
	}

	private void startAnim() {
		/*******************************头像动画**********************************/
		final AnimatorSet scaleHeadSet = new AnimatorSet();
		ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(iv_head, "scaleX", 1.0f, 0.3f);
		ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(iv_head, "scaleY", 1.0f, 0.3f);
		scaleHeadSet.play(scaleXAnim).with(scaleYAnim);
		scaleHeadSet.setDuration(500);
		
		final AnimatorSet animHeadSet = new AnimatorSet();
		ObjectAnimator headTranXFistAnim = ObjectAnimator.ofFloat(iv_head, "translationX", 0.0f, -500f);
		ObjectAnimator headTranYFistAnim = ObjectAnimator.ofFloat(iv_head, "translationY", 0.0f, -400f);
		ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(iv_head, "rotation", 0, -360);
		animHeadSet.play(headTranXFistAnim).with(headTranYFistAnim).with(rotateAnim);
		animHeadSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				FistAnimationDialog.this.dismiss();
			}
			
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				scaleHeadSet.start();
			}
		});
		animHeadSet.setDuration(800);
		
		
		/*******************************打到人时的显示*****************************/
		ObjectAnimator scaleAnim2 = ObjectAnimator.ofFloat(iv_left_fist, "scale", 0f, 0f);
		scaleAnim2.setStartDelay(410);
		scaleAnim2.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_left_fist.setVisibility(View.VISIBLE);
			}
		});
		scaleAnim2.start();

		ObjectAnimator scaleAnim3 = ObjectAnimator.ofFloat(iv_left_bubble, "scale", 0f, 0f);
		scaleAnim3.setStartDelay(890);
		scaleAnim3.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_left_blast.setVisibility(View.VISIBLE);
			}
		});
		scaleAnim3.start();

		/*************************************左手的动画*************************************/
		//收拳
		final AnimatorSet leftAnimSet2 = new AnimatorSet();
		ObjectAnimator leftTranXFistAnim2 = ObjectAnimator.ofFloat(iv_left_fist, "translationX", 0.0f, -500f);
		ObjectAnimator leftTranYFistAnim2 = ObjectAnimator.ofFloat(iv_left_fist, "translationY", 0.0f, 250f);
		leftAnimSet2.play(leftTranXFistAnim2).with(leftTranYFistAnim2);
		leftAnimSet2.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_left_blast.setVisibility(View.GONE);
			}
		});
		leftAnimSet2.setDuration(500);

		//出拳
		final AnimatorSet leftAnimSet1 = new AnimatorSet();
		ObjectAnimator leftTranXFistAnim1 = ObjectAnimator.ofFloat(iv_left_fist, "translationX", -500f, 0.0f);
		ObjectAnimator leftTranYFistAnim1 = ObjectAnimator.ofFloat(iv_left_fist, "translationY", 250f, 0.0f);
		leftAnimSet1.play(leftTranXFistAnim1).with(leftTranYFistAnim1);
		leftAnimSet1.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				leftAnimSet2.start();
			}

		});
		leftAnimSet1.setStartDelay(400);
		leftAnimSet1.setDuration(500);
		leftAnimSet1.start();

		/*************************************右手的动画*************************************/
		//第二次出拳
		final AnimatorSet rightAnimSet3 = new AnimatorSet();
		ObjectAnimator rightTranXFistAnim3 = ObjectAnimator.ofFloat(iv_right_fist, "translationX", 500f, 0.0f);
		ObjectAnimator rightTranYFistAnim3 = ObjectAnimator.ofFloat(iv_right_fist, "translationY", 250f, 0.0f);
		rightAnimSet3.play(rightTranXFistAnim3).with(rightTranYFistAnim3);
		rightAnimSet3.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				iv_right_blast.setVisibility(View.VISIBLE);
				animHeadSet.start();
			}
		});
		rightAnimSet3.setDuration(500);

		ObjectAnimator scaleAnim1 = ObjectAnimator.ofFloat(iv_right_bubble, "scale", 0f, 0f);
		scaleAnim1.setStartDelay(470);
		scaleAnim1.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_left_bubble.setVisibility(View.VISIBLE);
				iv_right_bubble.setVisibility(View.VISIBLE);
				iv_right_blast.setVisibility(View.VISIBLE);
			}
		});
		scaleAnim1.start();

		//收拳
		final AnimatorSet rightAnimSet2 = new AnimatorSet();
		ObjectAnimator rightTranXFistAnim2 = ObjectAnimator.ofFloat(iv_right_fist, "translationX", 0.0f, 500f);
		ObjectAnimator rightTranYFistAnim2 = ObjectAnimator.ofFloat(iv_right_fist, "translationY", 0.0f, 250f);
		rightAnimSet2.play(rightTranXFistAnim2).with(rightTranYFistAnim2);
		rightAnimSet2.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_right_blast.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				rightAnimSet3.start();
			}
		});
		rightAnimSet2.setDuration(500);

		//出拳
		AnimatorSet rightAnimSet1 = new AnimatorSet();
		ObjectAnimator rightTranXFistAnim1 = ObjectAnimator.ofFloat(iv_right_fist, "translationX", 500f, 0.0f);
		ObjectAnimator rightTranYFistAnim1 = ObjectAnimator.ofFloat(iv_right_fist, "translationY", 250f, 0.0f);
		rightAnimSet1.play(rightTranXFistAnim1).with(rightTranYFistAnim1);
		rightAnimSet1.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				rightAnimSet2.start();
			}
		});
		rightAnimSet1.setDuration(500);
		rightAnimSet1.start();

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
