package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.tshang.peipei.view.fall.PetalFlakeView;

/**
 * @Title: ValentineDayDialog.java 
 *
 * @Description: 情人节礼物特效
 *
 * @author Aaron  
 *
 * @date 2016-1-22 上午4:07:20 
 *
 * @version V1.0   
 */
public class ValentineDayDialog extends Dialog implements OnDismissListener {

	private ImageView star1, star2, star3, star4;

	private ImageView headIv1, headIv2;

	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private BroadcastInfo flowerInfo;
	private BroadcastQueue broadcast;
	private LinearLayout contentLayout;

	public ValentineDayDialog(Context context) {
		super(context);
	}

	public ValentineDayDialog(Activity context, int theme, BroadcastInfo flowerInfo, BroadcastQueue broadcast) {
		super(context, theme);

		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByW(context, 70);
		this.flowerInfo = flowerInfo;
		this.broadcast = broadcast;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.valentine_day_dialog_layout);

		star1 = (ImageView) findViewById(R.id.valentine_star1);
		star2 = (ImageView) findViewById(R.id.valentine_star2);
		star3 = (ImageView) findViewById(R.id.valentine_star3);
		star4 = (ImageView) findViewById(R.id.valentine_star4);

		headIv1 = (ImageView) findViewById(R.id.valentine_head1);
		headIv2 = (ImageView) findViewById(R.id.valentine_head2);

		AlphaAnimation animation = initStarAnim(100);
		star1.setAnimation(animation);
		star2.setAnimation(initStarAnim(180));
		star3.setAnimation(initStarAnim(140));
		star4.setAnimation(initStarAnim(200));

		if(flowerInfo != null){
			GoGirlDataInfo dataInfo = BaseAnimUtil.decodeGoGirlDataInfo(flowerInfo);
			String str = "";
			if(dataInfo != null){
				if(dataInfo.revint1.intValue() == 1){
					if(flowerInfo.tousers != null && flowerInfo.tousers.size() > 0){
						headIv1.setImageResource(R.drawable.couplet_monekey_face);
						GoGirlUserInfo info = (GoGirlUserInfo) flowerInfo.tousers.get(0);
						setHeadImage(headIv2, info.uid.intValue());
					}
				}else{
					if(flowerInfo.tousers != null && flowerInfo.tousers.size() > 1){
						setHeadImage(headIv1, dataInfo.revint0.intValue());
						GoGirlUserInfo info = (GoGirlUserInfo) flowerInfo.tousers.get(1);
						setHeadImage(headIv2, info.uid.intValue());
					}
				}
				
			}
		}
		
		contentLayout = (LinearLayout) findViewById(R.id.valentine_content);
		contentLayout.addView(new PetalFlakeView(getContext()));
		ValentineDayDialog.this.setOnDismissListener(this);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				dismiss();
			}
		}, 3000);
	}
	
	protected void setHeadImage(ImageView iv, int uid) {
		imageLoader.displayImage("http://" + uid + BAConstants.LOAD_HEAD_UID_APPENDSTR, iv, options_head);
	}

	private Handler handler = new Handler();

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框

			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dismiss();
		return true;
	}

	private AlphaAnimation initStarAnim(int startOffset) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(500);
		alphaAnimation.setRepeatCount(Animation.INFINITE);
		alphaAnimation.setRepeatMode(Animation.RESTART);
		alphaAnimation.setStartOffset(startOffset);
		alphaAnimation.setFillAfter(true);
		alphaAnimation.start();
		return alphaAnimation;
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
