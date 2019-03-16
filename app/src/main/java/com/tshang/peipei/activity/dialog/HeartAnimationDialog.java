package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
 * @Title: HeartAnimationDialog.java 
 *
 * @Description: 一箭钟情特效
 *
 * @author DYH  
 *
 * @date 2015-12-3 下午2:53:31 
 *
 * @version V1.0   
 */
public class HeartAnimationDialog extends Dialog implements DialogInterface.OnDismissListener{
	private ImageView iv_heart_large;
	private ImageView iv_arrow_all;
	private ImageView iv_arrow_left_head;
	private ImageView iv_arrow_right_head;
	private ImageView iv_arrow_left;
	private ImageView iv_arrow_right;
	
	private BroadcastQueue broadcast;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private int uid1, uid2;
	
	public HeartAnimationDialog(Activity context) {
		super(context);
	}

	public HeartAnimationDialog(Activity context, int theme, int uid1, int uid2, BroadcastQueue broadcast) {
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
		this.setContentView(R.layout.dialog_heart_animation);
		iv_heart_large = (ImageView) findViewById(R.id.iv_heart_large);
		iv_arrow_all = (ImageView) findViewById(R.id.iv_arrow_all);
		iv_arrow_left_head = (ImageView) findViewById(R.id.iv_arrow_left_head);
		iv_arrow_right_head = (ImageView) findViewById(R.id.iv_arrow_right_head);
		iv_arrow_left = (ImageView) findViewById(R.id.iv_arrow_left);
		iv_arrow_right = (ImageView) findViewById(R.id.iv_arrow_right);
		
		String key1 = uid1 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		String key2 = uid2 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + key1, iv_arrow_left_head, options_head);
		imageLoader.displayImage("http://" + key2, iv_arrow_right_head, options_head);
		startAnim();
		HeartAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		final AnimatorSet scaleSet = new AnimatorSet();
		ObjectAnimator xScaleAnimator = ObjectAnimator.ofFloat(iv_heart_large, "scaleX", 1.1f, 1.0f);
		ObjectAnimator yScaleAnimator = ObjectAnimator.ofFloat(iv_heart_large, "scaleY", 1.1f, 1.0f);
		xScaleAnimator.setRepeatCount(6);
		xScaleAnimator.setRepeatMode(ObjectAnimator.RESTART);
		yScaleAnimator.setRepeatCount(6);
		yScaleAnimator.setRepeatMode(ObjectAnimator.RESTART);
		scaleSet.play(xScaleAnimator).with(yScaleAnimator);
		scaleSet.setDuration(300);
		scaleSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				HeartAnimationDialog.this.dismiss();
			}
		});
		
		AnimatorSet transSet = new AnimatorSet();
		ObjectAnimator xTranslationAnimator = ObjectAnimator.ofFloat(iv_arrow_all, "translationX", 500f, 0.0f);
		ObjectAnimator yTranslationAnimator = ObjectAnimator.ofFloat(iv_arrow_all, "translationY", -250f, 0.0f);
		transSet.play(xTranslationAnimator).with(yTranslationAnimator);
		transSet.setDuration(500);
		transSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_arrow_all.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				iv_arrow_left.setVisibility(View.VISIBLE);
				iv_arrow_right.setVisibility(View.VISIBLE);
				iv_arrow_all.setVisibility(View.GONE);
				scaleSet.start();
			}
		});
		transSet.start();
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
