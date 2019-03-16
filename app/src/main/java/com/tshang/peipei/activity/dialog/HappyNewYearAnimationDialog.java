package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tshang.peipei.R;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.view.fall.GoldFlakeView;
import com.tshang.peipei.view.fall.GoldFlakeView.GoldAnimationEndListener;

/**
 * @Title: HappyNewYearAnimationDialog.java 
 *
 * @Description: 拜年特效
 *
 * @author DYH  
 *
 * @date 2016-1-21 下午9:23:10 
 *
 * @version V1.0   
 */
public class HappyNewYearAnimationDialog extends Dialog implements OnDismissListener, GoldAnimationEndListener {

	private Activity context;
	private BroadcastQueue broadcast;
	
	private FrameLayout fl_content;
	private ImageView iv_cloud;
	private ImageView iv_monkey;
	private GoldFlakeView goldFlakeView;
	
	public HappyNewYearAnimationDialog(Context context) {
		super(context);
	}
	
	public HappyNewYearAnimationDialog(Activity context, int theme, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_happy_new_year_animation);
		fl_content = (FrameLayout) findViewById(R.id.fl_content);
		iv_cloud = (ImageView) findViewById(R.id.iv_cloud);
		iv_monkey = (ImageView) findViewById(R.id.iv_monkey);
		
		startAnim();
		HappyNewYearAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		AnimatorSet transSet = new AnimatorSet();
		ObjectAnimator transCloudlationAnimator = ObjectAnimator.ofFloat(iv_cloud, "translationX", -1000f, 0f);
		ObjectAnimator transMonkeylationAnimator = ObjectAnimator.ofFloat(iv_monkey, "translationX", -1000f, 0f);
		final ObjectAnimator transYCloudlationAnimator = ObjectAnimator.ofFloat(iv_cloud, "translationY", 0f, -10f);
		transYCloudlationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
		transYCloudlationAnimator.setRepeatMode(ObjectAnimator.REVERSE);
		transSet.play(transCloudlationAnimator).with(transMonkeylationAnimator);
		transSet.setDuration(1000);
		transSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				transYCloudlationAnimator.setDuration(200);
				transYCloudlationAnimator.start();
				goldFlakeView = new GoldFlakeView(context, HappyNewYearAnimationDialog.this);
				fl_content.addView(goldFlakeView);
			}
		});
		transSet.start();
		
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

	@Override
	public void onEndAnimation() {
		this.dismiss();
	}

}
