package com.tshang.peipei.activity.dialog;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
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
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * @Title: ChangeAnimationDialog.java 
 *
 * @Description: 变变变仙术
 *
 * @author DYH  
 *
 * @date 2015-12-4 上午10:46:20 
 *
 * @version V1.0   
 */
public class ChangeAnimationDialog extends Dialog implements OnDismissListener {

	private BroadcastQueue broadcast;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private int uid1;
	
	private ImageView iv_pig;
	private ImageView iv_head;
	private ImageView iv_hat;
	private ImageView iv_nose;
	private ImageView iv_tie;
	private ImageView iv_cloud;
	private ImageView iv_wand;
	
	public ChangeAnimationDialog(Context context) {
		super(context);
	}
	
	public ChangeAnimationDialog(Activity context, int theme, int uid1, BroadcastQueue broadcast) {
		super(context, theme);
		this.broadcast = broadcast;
		this.uid1 = uid1;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByW(context, 70);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_change_animation);
		iv_pig = (ImageView) findViewById(R.id.iv_pig);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		iv_hat = (ImageView) findViewById(R.id.iv_hat);
		iv_nose = (ImageView) findViewById(R.id.iv_nose);
		iv_tie = (ImageView) findViewById(R.id.iv_tie);
		iv_cloud = (ImageView) findViewById(R.id.iv_cloud);
		iv_wand = (ImageView) findViewById(R.id.iv_wand);
		
		String key1 = uid1 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + key1, iv_head, options_head);
		
		startAnim();
		ChangeAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		
	 	final ObjectAnimator objAnimator = ObjectAnimator.ofFloat(iv_pig, "alpha", 1.0f, 1.0f);
	 	objAnimator.setStartDelay(750);
	 	objAnimator.setDuration(1000);
	 	objAnimator.addListener(new AnimatorListenerAdapter() {
	 		@Override
	 		public void onAnimationStart(Animator animation) {
	 			super.onAnimationStart(animation);
	 			iv_cloud.setVisibility(View.GONE);
	 			iv_pig.setVisibility(View.VISIBLE);
	 			iv_hat.setVisibility(View.VISIBLE);
	 			iv_nose.setVisibility(View.VISIBLE);
	 			iv_tie.setVisibility(View.VISIBLE);
	 		}
	 		
	 		@Override
	 		public void onAnimationEnd(Animator animation) {
	 			super.onAnimationEnd(animation);
	 			ChangeAnimationDialog.this.dismiss();
	 		}
		});
		
		iv_wand.setPivotX(300.0f);
		iv_wand.setPivotY(300.0f);
		iv_wand.invalidate();
		ObjectAnimator transAnimator1 = ObjectAnimator.ofFloat(iv_wand, "rotation", 20f, -20f);
		transAnimator1.setDuration(500);
		transAnimator1.setRepeatCount(2);
		transAnimator1.setRepeatMode(ObjectAnimator.REVERSE);
		transAnimator1.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				iv_cloud.setVisibility(View.VISIBLE);
			 	AnimationDrawable cloudDrawable = (AnimationDrawable) iv_cloud.getBackground();
			 	cloudDrawable.start();
			 	objAnimator.start();
			}
		});
		transAnimator1.start();
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
