package com.tshang.peipei.activity.dialog;

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
 * @Title: LipsAnimationDialog.java 
 *
 * @Description: 烈焰红唇特效
 *
 * @author DYH  
 *
 * @date 2015-12-3 下午7:56:18 
 *
 * @version V1.0   
 */
public class LipsAnimationDialog extends Dialog implements OnDismissListener {

	private BroadcastQueue broadcast;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private int uid1;
	
	private ImageView iv_lips_head;
	private ImageView iv_lip1;
	private ImageView iv_lip2;
	private ImageView iv_lip3;
	private ImageView iv_lip4;
	private ImageView iv_lip5;
	
	public LipsAnimationDialog(Context context) {
		super(context);
	}
	
	public LipsAnimationDialog(Activity context, int theme, int uid1, BroadcastQueue broadcast) {
		super(context, theme);
		this.broadcast = broadcast;
		this.uid1 = uid1;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByW(context, 70);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_lips_animation);
		
		iv_lips_head = (ImageView) findViewById(R.id.iv_lips_head);
		iv_lip1 = (ImageView) findViewById(R.id.iv_lip1);
		iv_lip2 = (ImageView) findViewById(R.id.iv_lip2);
		iv_lip3 = (ImageView) findViewById(R.id.iv_lip3);
		iv_lip4 = (ImageView) findViewById(R.id.iv_lip4);
		iv_lip5 = (ImageView) findViewById(R.id.iv_lip5);
		
		String key1 = uid1 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + key1, iv_lips_head, options_head);
		startAnim();
		LipsAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		final ObjectAnimator alphaAnimator6 = ObjectAnimator.ofFloat(iv_lip5, "alpha", 1.0f, 1.0f);
		alphaAnimator6.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				LipsAnimationDialog.this.dismiss();
			}
		});
		alphaAnimator6.setDuration(1000);
		
		final AnimatorSet lipSet5 = new AnimatorSet();
		ObjectAnimator alphaAnimator5 = ObjectAnimator.ofFloat(iv_lip5, "alpha", 1.0f, 1.0f);
		ObjectAnimator scaleXAnimator5 = ObjectAnimator.ofFloat(iv_lip5, "scaleX", 2.0f, 1.0f);
		ObjectAnimator scaleYAnimator5 = ObjectAnimator.ofFloat(iv_lip5, "scaleY", 2.0f, 1.0f);
		lipSet5.play(alphaAnimator5).with(scaleXAnimator5).with(scaleYAnimator5);
		lipSet5.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_lip5.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				alphaAnimator6.start();
			}
		});
		lipSet5.setDuration(500);
		
		final AnimatorSet lipSet4 = new AnimatorSet();
		ObjectAnimator alphaAnimator4 = ObjectAnimator.ofFloat(iv_lip4, "alpha", 1.0f, 1.0f);
		ObjectAnimator scaleXAnimator4 = ObjectAnimator.ofFloat(iv_lip4, "scaleX", 2.0f, 1.0f);
		ObjectAnimator scaleYAnimator4 = ObjectAnimator.ofFloat(iv_lip4, "scaleY", 2.0f, 1.0f);
		lipSet4.play(alphaAnimator4).with(scaleXAnimator4).with(scaleYAnimator4);
		lipSet4.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_lip4.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				lipSet5.start();
			}
		});
		lipSet4.setDuration(500);
		
		final AnimatorSet lipSet3 = new AnimatorSet();
		ObjectAnimator alphaAnimator3 = ObjectAnimator.ofFloat(iv_lip3, "alpha", 1.0f, 1.0f);
		ObjectAnimator scaleXAnimator3 = ObjectAnimator.ofFloat(iv_lip3, "scaleX", 2.0f, 1.0f);
		ObjectAnimator scaleYAnimator3 = ObjectAnimator.ofFloat(iv_lip3, "scaleY", 2.0f, 1.0f);
		lipSet3.play(alphaAnimator3).with(scaleXAnimator3).with(scaleYAnimator3);
		lipSet3.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_lip3.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				lipSet4.start();
			}
		});
		lipSet3.setDuration(500);
		
		final AnimatorSet lipSet2 = new AnimatorSet();
		ObjectAnimator alphaAnimator2 = ObjectAnimator.ofFloat(iv_lip2, "alpha", 1.0f, 1.0f);
		ObjectAnimator scaleXAnimator2 = ObjectAnimator.ofFloat(iv_lip2, "scaleX", 2.0f, 1.0f);
		ObjectAnimator scaleYAnimator2 = ObjectAnimator.ofFloat(iv_lip2, "scaleY", 2.0f, 1.0f);
		lipSet2.play(alphaAnimator2).with(scaleXAnimator2).with(scaleYAnimator2);
		lipSet2.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_lip2.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				lipSet3.start();
			}
		});
		lipSet2.setDuration(500);
		
		AnimatorSet lipSet1 = new AnimatorSet();
		ObjectAnimator alphaAnimator1 = ObjectAnimator.ofFloat(iv_lip1, "alpha", 1.0f, 1.0f);
		ObjectAnimator scaleXAnimator1 = ObjectAnimator.ofFloat(iv_lip1, "scaleX", 5.0f, 1.0f);
		ObjectAnimator scaleYAnimator1 = ObjectAnimator.ofFloat(iv_lip1, "scaleY", 5.0f, 1.0f);
		lipSet1.play(alphaAnimator1).with(scaleXAnimator1).with(scaleYAnimator1);
		lipSet1.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_lip1.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				lipSet2.start();
			}
		});
		lipSet1.setDuration(500);
		lipSet1.start();
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
