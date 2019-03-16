package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.tshang.peipei.R;
import com.tshang.peipei.model.broadcast.BroadcastQueue;

/**
 * @Title: FireCrackersAnimationDialog.java 
 *
 * @Description: 鞭炮动画
 *
 * @author DYH  
 *
 * @date 2016-1-21 下午9:23:10 
 *
 * @version V1.0   
 */
public class FireCrackersAnimationDialog extends Dialog implements OnDismissListener {

	private Activity context;
	private BroadcastQueue broadcast;
	
	private ImageView iv_firecrackers;
	
	public FireCrackersAnimationDialog(Context context) {
		super(context);
	}
	
	public FireCrackersAnimationDialog(Activity context, int theme, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_firecrackers_animation);
		iv_firecrackers = (ImageView) findViewById(R.id.iv_firecrackers);
		startAnim();
		FireCrackersAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		AnimationDrawable animDrawable = (AnimationDrawable) iv_firecrackers.getBackground();
		startFireCrackers(animDrawable);
		ValueAnimator animator = ValueAnimator.ofInt(0);
		animator.setDuration(1000);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				FireCrackersAnimationDialog.this.dismiss();
			}
		});
		animator.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.dismiss();
		return true;
	}
	
	private void startFireCrackers(AnimationDrawable animDrawable){
		if(animDrawable != null){
			animDrawable.start();
		}
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
