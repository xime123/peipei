package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.view.fall.RoseRainView;
import com.tshang.peipei.view.fall.RoseRainView.RoseAnimationEndListener;
import com.tshang.peipei.view.fire.MyFireView.FireCancelAnimationListener;

/**
 * 玫瑰花雨动画
 * @author allen
 *
 */
public class RoseRainAnimationDialog extends Dialog implements DialogInterface.OnDismissListener, FireCancelAnimationListener,
		RoseAnimationEndListener {

	private Activity context;
	private BroadcastQueue broadcast;
	private RoseRainView fireView;

	public RoseRainAnimationDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public RoseRainAnimationDialog(Activity context, int theme, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_rose_rain_animation);

		ImageView imageview = (ImageView) findViewById(R.id.iv_rose_rain);
		AnimationDrawable rocketAnimation = (AnimationDrawable) imageview.getBackground();
		rocketAnimation.start();

		RelativeLayout rl_rose = (RelativeLayout) findViewById(R.id.ll_rose_rain);
		fireView = new RoseRainView(context, this);
		rl_rose.addView(fireView);
		RoseRainAnimationDialog.this.setOnDismissListener(this);
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
		wlps.dimAmount = 0f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		broadcast.setSynchWork(false);
	}

	@Override
	public void onEndAnimation() {
		this.dismiss();
	}

}
