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

import com.tshang.peipei.R;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.view.fall.RedpacketFlakeView;
import com.tshang.peipei.view.fall.RedpacketFlakeView.RedpacketAnimationEndListener;

/**
 * @Title: RedpacketAnimationDialog.java 
 *
 * @Description: 红包雨特效
 *
 * @author Administrator  
 *
 * @date 2015-12-22 下午6:20:28 
 *
 * @version V1.0   
 */
public class RedpacketAnimationDialog extends Dialog implements OnDismissListener, RedpacketAnimationEndListener {

	private Activity context;
	private BroadcastQueue broadcast;
	
	private FrameLayout fl_christmas_snow;
	private RedpacketFlakeView redpacketFlakeView;
	
	public RedpacketAnimationDialog(Context context) {
		super(context);
	}
	
	public RedpacketAnimationDialog(Activity context, int theme, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_redpacket_animation);
		fl_christmas_snow = (FrameLayout) findViewById(R.id.fl_christmas_snow);
		
		startAnim();
		RedpacketAnimationDialog.this.setOnDismissListener(this);
	}
	
	private void startAnim(){
		redpacketFlakeView = new RedpacketFlakeView(context, this);
		fl_christmas_snow.addView(redpacketFlakeView);
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
