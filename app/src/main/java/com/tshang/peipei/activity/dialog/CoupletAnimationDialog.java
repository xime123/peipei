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
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseAnimUtil;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * @Title: CoupletAnimationDialog.java 
 *
 * @Description: 对联动画
 *
 * @author DYH  
 *
 * @date 2016-1-21 下午9:23:10 
 *
 * @version V1.0   
 */
public class CoupletAnimationDialog extends Dialog implements OnDismissListener {

	private Activity context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private BroadcastQueue broadcast;
	private BroadcastInfo coupletInfo;
	private ImageView iv_left_couplet;
	private ImageView iv_right_couplet;
	private ImageView iv_left_avatar;
	private ImageView iv_right_avatar;
	private ImageView iv_floral;
	
	public CoupletAnimationDialog(Context context) {
		super(context);
	}
	
	public CoupletAnimationDialog(Activity context, int theme, BroadcastInfo coupletInfo, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
		this.coupletInfo = coupletInfo;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByGift(context, 70);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_couplet_animation);
		iv_left_couplet = (ImageView) findViewById(R.id.iv_left_couplet);
		iv_right_couplet = (ImageView) findViewById(R.id.iv_right_couplet);
		iv_left_avatar = (ImageView) findViewById(R.id.iv_left_avatar);
		iv_right_avatar = (ImageView) findViewById(R.id.iv_right_avatar);
		iv_floral = (ImageView) findViewById(R.id.iv_floral);
		
		if(coupletInfo != null){
			GoGirlDataInfo dataInfo = BaseAnimUtil.decodeGoGirlDataInfo(coupletInfo);
			if(dataInfo != null){
				if(dataInfo.revint1.intValue() == 1){
					iv_left_avatar.setImageResource(R.drawable.couplet_monekey_face);
					if(coupletInfo.tousers != null && coupletInfo.tousers.size() > 0){
						GoGirlUserInfo touser = (GoGirlUserInfo) coupletInfo.tousers.get(0);
						if(touser != null){
							setHeadImage(iv_right_avatar, touser.uid.intValue());
						}
					}
				}else{
					setHeadImage(iv_left_avatar, dataInfo.revint0.intValue());
					if(coupletInfo.tousers != null && coupletInfo.tousers.size() > 1){
						GoGirlUserInfo touser = (GoGirlUserInfo) coupletInfo.tousers.get(1);
						if(touser != null){
							setHeadImage(iv_right_avatar, touser.uid.intValue());
						}
					}
				}
			}
			
		}
		
		startAnim();
		CoupletAnimationDialog.this.setOnDismissListener(this);
	}
	
	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_head);
	}
	
	private void startAnim(){
		AnimationDrawable leftDrawable = (AnimationDrawable) iv_left_couplet.getBackground();
		AnimationDrawable rightDrawable = (AnimationDrawable) iv_right_couplet.getBackground();
		startLeftCouplet(leftDrawable);
		startRightCouplet(rightDrawable);
		ObjectAnimator animator = ObjectAnimator.ofFloat(iv_floral, "alpha", 1.0f);
		animator.setDuration(100);
		animator.setStartDelay(600);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				iv_floral.setVisibility(View.VISIBLE);
			}
		});
		animator.start();
		
		ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv_floral, "alpha", 1.0f);
		animator2.setDuration(3000);
		animator2.setStartDelay(600);
		animator2.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				CoupletAnimationDialog.this.dismiss();
			}
		});
		animator2.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.dismiss();
		return true;
	}
	
	private void startLeftCouplet(AnimationDrawable leftDrawable){
		if(leftDrawable != null){
			leftDrawable.start();
		}
	}
	
	private void startRightCouplet(AnimationDrawable rightDrawable){
		if(rightDrawable != null){
			rightDrawable.start();
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
