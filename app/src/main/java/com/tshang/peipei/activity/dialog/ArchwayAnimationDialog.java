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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseAnimUtil;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: ArchwayAnimationDialog.java 
 *
 * @Description: 牌坊动画
 *
 * @author Administrator  
 *
 * @date 2016-1-25 上午11:18:26 
 *
 * @version V1.0   
 */
public class ArchwayAnimationDialog extends Dialog implements OnDismissListener {

	private Activity context;
	private BroadcastQueue broadcast;
	private BroadcastInfo archwayInfo;
	private FrameLayout fl_door;
	private TextView tv_des;
	private ImageView iv_fire1;
	private ImageView iv_fire2;
	private ImageView iv_fire3;
	private ImageView iv_fire4;
	private ImageView iv_fire5;
	private ImageView iv_crackers;
	private ImageView iv_fireworks1;
	private ImageView iv_fireworks2;
	private ImageView iv_fireworks3;
	private ImageView iv_fireworks4;
	private ImageView iv_fireworks5;
	private ImageView iv_fireworks6;
	private ImageView iv_fireworks7;
	private ImageView iv_fireworks8;
	private ImageView iv_fireworks9;
	private ImageView iv_fireworks10;
	private ImageView iv_fireworks11;
	private ImageView iv_fireworks12;
	private ImageView iv_fireworks13;
	private ImageView iv_fireworks14;
	private ImageView iv_fireworks15;
	private ImageView iv_fireworks16;
	private ImageView iv_fireworks17;

	public ArchwayAnimationDialog(Context context) {
		super(context);
	}

	public ArchwayAnimationDialog(Activity context, int theme, BroadcastInfo archwayInfo, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
		this.archwayInfo = archwayInfo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_archway_animation);
		fl_door = (FrameLayout) findViewById(R.id.fl_door);
		tv_des = (TextView) findViewById(R.id.tv_des);
		iv_fire1 = (ImageView) findViewById(R.id.iv_fire1);
		iv_fire2 = (ImageView) findViewById(R.id.iv_fire2);
		iv_fire3 = (ImageView) findViewById(R.id.iv_fire3);
		iv_fire4 = (ImageView) findViewById(R.id.iv_fire4);
		iv_fire5 = (ImageView) findViewById(R.id.iv_fire5);
		iv_crackers = (ImageView) findViewById(R.id.iv_crackers);
		iv_fireworks1 = (ImageView) findViewById(R.id.iv_fireworks1);
		iv_fireworks2 = (ImageView) findViewById(R.id.iv_fireworks2);
		iv_fireworks3 = (ImageView) findViewById(R.id.iv_fireworks3);
		iv_fireworks4 = (ImageView) findViewById(R.id.iv_fireworks4);
		iv_fireworks5 = (ImageView) findViewById(R.id.iv_fireworks5);
		iv_fireworks6 = (ImageView) findViewById(R.id.iv_fireworks6);
		iv_fireworks7 = (ImageView) findViewById(R.id.iv_fireworks7);
		iv_fireworks8 = (ImageView) findViewById(R.id.iv_fireworks8);
		iv_fireworks9 = (ImageView) findViewById(R.id.iv_fireworks9);
		iv_fireworks10 = (ImageView) findViewById(R.id.iv_fireworks10);
		iv_fireworks11 = (ImageView) findViewById(R.id.iv_fireworks11);
		iv_fireworks12 = (ImageView) findViewById(R.id.iv_fireworks12);
		iv_fireworks13 = (ImageView) findViewById(R.id.iv_fireworks13);
		iv_fireworks14 = (ImageView) findViewById(R.id.iv_fireworks14);
		iv_fireworks15 = (ImageView) findViewById(R.id.iv_fireworks15);
		iv_fireworks16 = (ImageView) findViewById(R.id.iv_fireworks16);
		iv_fireworks17 = (ImageView) findViewById(R.id.iv_fireworks17);
		
		if(archwayInfo != null){
			GoGirlDataInfo dataInfo = BaseAnimUtil.decodeGoGirlDataInfo(archwayInfo);
			String str = "";
			if(dataInfo != null){
				if(dataInfo.revint1.intValue() == 1){
					if(archwayInfo.tousers != null && archwayInfo.tousers.size() > 0){
						GoGirlUserInfo info = (GoGirlUserInfo) archwayInfo.tousers.get(0);
						str = context.getString(R.string.str_archway_des, new String(dataInfo.revstr0), new String(info.nick), new String(dataInfo.revstr1));
					}
				}else{
					if(archwayInfo.tousers != null && archwayInfo.tousers.size() > 1){
						GoGirlUserInfo info = (GoGirlUserInfo) archwayInfo.tousers.get(1);
						str = context.getString(R.string.str_archway_des, new String(dataInfo.revstr0), new String(info.nick), new String(dataInfo.revstr1));
					}
				}
				
			}
			tv_des.setText(str);
		}
		
		startAnim();
		ArchwayAnimationDialog.this.setOnDismissListener(this);
	}

	private void startAnim() {
		startDoorAnim();
	}
	
	private void startDoorAnim(){
		ObjectAnimator doorAnimator = ObjectAnimator.ofFloat(fl_door, "translationY", 1000f, 0f);
		doorAnimator.setDuration(1000);
		doorAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				startCrackerAnim();
			}
		});
		doorAnimator.start();
	}
	
	private void startCrackerAnim(){
		ObjectAnimator crackersAnimator = ObjectAnimator.ofFloat(iv_crackers, "translationY", -1000f, 0f);
		crackersAnimator.setDuration(1000);
		crackersAnimator.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_crackers.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				fire1Anim();
			}
		});
		crackersAnimator.start();
	}
	
	private void fire1Anim(){
		final ObjectAnimator fire1Animator = ObjectAnimator.ofFloat(iv_fire1, "translationY", 400f, -400f);
		fire1Animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_fire1.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				iv_fire1.setVisibility(View.GONE);
				iv_fireworks1.setVisibility(View.VISIBLE);
				startWorkfire1Anim(iv_fireworks1);
				fire2Anim();
			}
		});
		fire1Animator.setDuration(1000);
		fire1Animator.start();
	}
	
	private void fire2Anim(){
		AnimatorSet fireSet = new AnimatorSet();
		ObjectAnimator fireYAnimator = ObjectAnimator.ofFloat(iv_fire2, "translationY", 400f, -500f);
		ObjectAnimator fireXAnimator = ObjectAnimator.ofFloat(iv_fire2, "translationX", 0f, -150f);
		fireSet.play(fireXAnimator).with(fireYAnimator);
		fireSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_fire2.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				iv_fire2.setVisibility(View.GONE);
				iv_fireworks2.setVisibility(View.VISIBLE);
				startWorkfire1Anim(iv_fireworks2);
				fire3Anim();
				fireNomalAnim(iv_fire4);
				fireNomalAnim(iv_fire5);
			}
		});
		fireSet.setDuration(1000);
		fireSet.start();
	}
	
	private void fire3Anim(){
		AnimatorSet fireSet = new AnimatorSet();
		ObjectAnimator fireXAnimator = ObjectAnimator.ofFloat(iv_fire3, "translationX", 0f, 150f);
		ObjectAnimator fireYAnimator = ObjectAnimator.ofFloat(iv_fire3, "translationY", 400f, -250f);
		fireSet.play(fireXAnimator).with(fireYAnimator);
		fireSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_fire3.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				iv_fire3.setVisibility(View.GONE);
				iv_fireworks1.setVisibility(View.VISIBLE);
				iv_fireworks2.setVisibility(View.VISIBLE);
				iv_fireworks3.setVisibility(View.VISIBLE);
				iv_fireworks4.setVisibility(View.VISIBLE);
				iv_fireworks5.setVisibility(View.VISIBLE);
				iv_fireworks6.setVisibility(View.VISIBLE);
				iv_fireworks7.setVisibility(View.VISIBLE);
				iv_fireworks8.setVisibility(View.VISIBLE);
				iv_fireworks9.setVisibility(View.VISIBLE);
				iv_fireworks10.setVisibility(View.VISIBLE);
				iv_fireworks11.setVisibility(View.VISIBLE);
				iv_fireworks12.setVisibility(View.VISIBLE);
				iv_fireworks13.setVisibility(View.VISIBLE);
				iv_fireworks14.setVisibility(View.VISIBLE);
				iv_fireworks15.setVisibility(View.VISIBLE);
				iv_fireworks16.setVisibility(View.VISIBLE);
				iv_fireworks17.setVisibility(View.VISIBLE);
				startWorkfireAnimNotGone(iv_fireworks9);
				delayDismissAnim();
			}
		});
		fireSet.setDuration(1000);
		fireSet.start();
	}
	
	private void delayDismissAnim(){
		ObjectAnimator animator = ObjectAnimator.ofFloat(iv_fireworks9, "alpha", 1.0f);
		animator.setDuration(1000);
		animator.setStartDelay(1000);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				ArchwayAnimationDialog.this.dismiss();
			}
		});
		animator.start();
	}
	
	private void fireNomalAnim(final View view){
		final ObjectAnimator fire1Animator = ObjectAnimator.ofFloat(view, "translationY", 400f, -300f);
		fire1Animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				view.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				view.setVisibility(View.GONE);
			}
		});
		fire1Animator.setDuration(1000);
		fire1Animator.start();
	}
	
	
	private void startWorkfire1Anim(final View view){
		final AnimatorSet fireWorks1Set = new AnimatorSet();
		ObjectAnimator fireWorks1XAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.0f);
		ObjectAnimator fireWorks1YAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.0f);
		fireWorks1Set.setDuration(1000);
		fireWorks1Set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				view.setVisibility(View.GONE);
			}
		});
		fireWorks1Set.play(fireWorks1YAnimator).with(fireWorks1XAnimator);
		fireWorks1Set.start();
	}
	
	private void startWorkfireAnimNotGone(final View view){
		final AnimatorSet fireWorks1Set = new AnimatorSet();
		ObjectAnimator fireWorks1XAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.0f);
		ObjectAnimator fireWorks1YAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.0f);
		fireWorks1Set.setDuration(1000);
		fireWorks1Set.play(fireWorks1YAnimator).with(fireWorks1XAnimator);
		fireWorks1Set.start();
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
	public void onDismiss(DialogInterface dialog) {
		broadcast.setSynchWork(false);
	}

}
