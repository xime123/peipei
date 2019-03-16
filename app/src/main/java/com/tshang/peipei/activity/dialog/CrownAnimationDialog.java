package com.tshang.peipei.activity.dialog;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.autoupdatesdk.obf.al;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: CrownAnimationDialog.java 
 *
 * @Description: 皇冠礼物特效 
 *
 * @author DYH  
 *
 * @date 2015-12-8 下午5:23:10 
 *
 * @version V1.0   
 */
public class CrownAnimationDialog extends Dialog implements OnDismissListener {

	private BroadcastQueue broadcast;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private BroadcastInfo info;
	
	private ImageView iv_head;
	private ImageView iv_crown;
	private ImageView iv_star1;
	private ImageView iv_star2;
	private ImageView iv_star3;
	
	public CrownAnimationDialog(Activity context) {
		super(context);
	}
	
	public CrownAnimationDialog(Activity context, int theme, BroadcastInfo info, BroadcastQueue broadcast) {
		super(context, theme);
		this.broadcast = broadcast;
		this.info = info;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByW(context, 70);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_crown_animation);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		iv_crown = (ImageView) findViewById(R.id.iv_crown);
		iv_star1 = (ImageView) findViewById(R.id.iv_star1);
		iv_star2 = (ImageView) findViewById(R.id.iv_star2);
		iv_star3 = (ImageView) findViewById(R.id.iv_star3);
		
		if(info != null){
			if(info.tousers != null && info.tousers.size() > 0){
				GoGirlUserInfo user = (GoGirlUserInfo) info.tousers.get(info.tousers.size() -1);
				if(user != null){
					int fuid = user.uid.intValue();
					String key1 = fuid + BAConstants.LOAD_HEAD_UID_APPENDSTR;
					imageLoader.displayImage("http://" + key1, iv_head, options_head);
				}
			}
		}
		
		startAnim();
		CrownAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		Random random = new Random();
		final AnimatorSet alphaSet = new AnimatorSet();
		ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(iv_star1, "alpha", 0.0f, 1.0f);
		alphaAnim1.setDuration(random.nextInt(100) + 200);
		alphaAnim1.setStartDelay(random.nextInt(100) + 50);
		alphaAnim1.setRepeatCount(5);
		alphaAnim1.setRepeatMode(ObjectAnimator.REVERSE);
		ObjectAnimator alphaAnim2 = ObjectAnimator.ofFloat(iv_star2, "alpha", 0.0f, 1.0f);
		alphaAnim2.setDuration(random.nextInt(100) + 200);
		alphaAnim2.setStartDelay(random.nextInt(100) + 50);
		alphaAnim2.setRepeatCount(5);
		alphaAnim2.setRepeatMode(ObjectAnimator.REVERSE);
		ObjectAnimator alphaAnim3 = ObjectAnimator.ofFloat(iv_star3, "alpha", 0.0f, 1.0f);
		alphaAnim3.setDuration(random.nextInt(100) + 200);
		alphaAnim3.setStartDelay(random.nextInt(100) + 50);
		alphaAnim3.setRepeatCount(5);
		alphaAnim3.setRepeatMode(ObjectAnimator.REVERSE);
		alphaSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				iv_star1.setVisibility(View.VISIBLE);
				iv_star2.setVisibility(View.VISIBLE);
				iv_star3.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				CrownAnimationDialog.this.dismiss();
			}
		});
		alphaSet.play(alphaAnim1).with(alphaAnim2).with(alphaAnim3);
		
		AnimatorSet animatorSet = new AnimatorSet();
		ObjectAnimator transAnimator = ObjectAnimator.ofFloat(iv_crown, "translationY", 500f, 0f);
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(iv_crown, "scaleX", 0.0f, 1.0f);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(iv_crown, "scaleY", 0.0f, 1.0f);
		animatorSet.play(transAnimator).with(scaleXAnimator).with(scaleYAnimator);
		animatorSet.setDuration(1000);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				alphaSet.start();
			}
		});
		animatorSet.start();
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
