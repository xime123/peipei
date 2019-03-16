package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tshang.peipei.R;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.protocol.asn.gogirl.EnterBroadcastInfo;

/**
 * @Title: CarAnimationDialog.java 
 *
 * @Description: 汽车座驾的特效 
 *
 * @author DYH  
 *
 * @date 2015-12-8 下午2:31:02 
 *
 * @version V1.0   
 */
public class CarAnimationDialog extends Dialog implements OnDismissListener {

	private Activity context;
	private BroadcastQueue broadcast;
	private EnterBroadcastInfo enterInfo;
	private ImageView iv_car;
	private TextView tv_nick;
	private TextView tv_gift_name;
	
	public CarAnimationDialog(Activity context) {
		super(context);
	}
	
	public CarAnimationDialog(Activity context, int theme, EnterBroadcastInfo enterInfo, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
		this.enterInfo = enterInfo;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_car_animation);
		iv_car = (ImageView) findViewById(R.id.iv_car);
		tv_nick = (TextView) findViewById(R.id.tv_nick);
		tv_gift_name = (TextView) findViewById(R.id.tv_gift_name);
		if(enterInfo != null){
			tv_nick.setText(new String(enterInfo.nick));
			tv_gift_name.setText(new String(enterInfo.ridingname));
		}
		startAnim();
		CarAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		final AnimatorSet transSet2 = new AnimatorSet();
		ObjectAnimator xTranslationAnimator2 = ObjectAnimator.ofFloat(iv_car, "translationX", 0.0f, -1000f);
		ObjectAnimator yTranslationAnimator2 = ObjectAnimator.ofFloat(iv_car, "translationY", 0.0f, 100f);
		transSet2.play(xTranslationAnimator2).with(yTranslationAnimator2);
		transSet2.setDuration(500);
		transSet2.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				CarAnimationDialog.this.dismiss();
			}
		});
		
		final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(iv_car, "alpha", 1.0f, 1.0f);
		alphaAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				transSet2.start();
			}
		});
		alphaAnimator.setDuration(3000);
		
		AnimatorSet transSet = new AnimatorSet();
		ObjectAnimator xTranslationAnimator = ObjectAnimator.ofFloat(iv_car, "translationX", 500f, 0.0f);
		ObjectAnimator yTranslationAnimator = ObjectAnimator.ofFloat(iv_car, "translationY", -250f, 0.0f);
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(iv_car, "scaleX", 0.0f, 1.0f);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(iv_car, "scaleY", 0.0f, 1.0f);
		transSet.play(xTranslationAnimator).with(yTranslationAnimator).with(scaleXAnimator).with(scaleYAnimator);
		transSet.setDuration(800);
		transSet.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				alphaAnimator.start();
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
