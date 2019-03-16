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
import com.tshang.peipei.view.fall.PetalRainView;
import com.tshang.peipei.view.fall.RoseRainView;
import com.tshang.peipei.view.fall.PetalRainView.PetalAnimationEndListener;
import com.tshang.peipei.view.fall.RoseRainView.RoseAnimationEndListener;

/**
 * @Title: LoveOathAnimationDialog.java 
 *
 * @Description: 真爱誓言仙术
 *
 * @author DYH  
 *
 * @date 2015-12-4 上午10:40:00 
 *
 * @version V1.0   
 */
public class LoveOathAnimationDialog extends Dialog implements OnDismissListener, PetalAnimationEndListener {

	private Activity context;
	private BroadcastQueue broadcast;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private int uid1, uid2;
	
	private ImageView iv_petal1;
	private ImageView iv_petal2;
	private ImageView iv_heart;
	private ImageView iv_left_head;
	private ImageView iv_right_head;
	private FrameLayout fl_flower;
	private PetalRainView fireView;
	
	public LoveOathAnimationDialog(Context context) {
		super(context);
	}
	
	public LoveOathAnimationDialog(Activity context, int theme, int uid1, int uid2, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
		this.uid1 = uid1;
		this.uid2 = uid2;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByW(context, 70);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_love_oath_animation);
		
		iv_petal1 = (ImageView) findViewById(R.id.iv_petal1);
		iv_petal2 = (ImageView) findViewById(R.id.iv_petal2);
		iv_heart = (ImageView) findViewById(R.id.iv_heart);
		iv_left_head = (ImageView) findViewById(R.id.iv_left_head);
		iv_right_head = (ImageView) findViewById(R.id.iv_right_head);
		
		fl_flower = (FrameLayout) findViewById(R.id.fl_love_oath);
		
		String key1 = uid1 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		String key2 = uid2 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + key1, iv_left_head, options_head);
		imageLoader.displayImage("http://" + key2, iv_right_head, options_head);
		
		startAnim();
		LoveOathAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		AnimationDrawable heartDrawable = (AnimationDrawable) iv_heart.getBackground();
		heartDrawable.start();
		AnimatorSet animSet = new AnimatorSet();
		ObjectAnimator objAnimator1 = ObjectAnimator.ofFloat(iv_petal1, "alpha", 1.0f);
		objAnimator1.setStartDelay(350L);
		objAnimator1.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_petal1.setVisibility(View.VISIBLE);
			}
		});
		ObjectAnimator objAnimator2 = ObjectAnimator.ofFloat(iv_petal2, "alpha", 1.0f);
		objAnimator2.setStartDelay(400L);
		objAnimator2.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_petal2.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				fireView = new PetalRainView(context, LoveOathAnimationDialog.this);
				fl_flower.addView(fireView);
			}
		});
		animSet.play(objAnimator1).with(objAnimator2);
		animSet.start();
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

	@Override
	public void onEndAnimation() {
		this.dismiss();
	}

}
