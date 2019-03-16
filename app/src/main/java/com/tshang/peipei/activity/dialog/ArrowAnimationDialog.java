package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.view.fall.ArrowFlakeView;
import com.tshang.peipei.view.fall.ArrowFlakeView.ArrowAnimationEndListener;
import com.tshang.peipei.view.fire.MyFireView.FireCancelAnimationListener;

/**
 * 万箭阵动画
 * @author allen
 *
 */
public class ArrowAnimationDialog extends Dialog implements DialogInterface.OnDismissListener, FireCancelAnimationListener, ArrowAnimationEndListener {

	private Activity context;
	private BroadcastQueue broadcast;
	private ArrowFlakeView fireView;
	private ImageView rl_heart;

	public ArrowAnimationDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public ArrowAnimationDialog(Activity context, int theme, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_arrow_animation);

		rl_heart = (ImageView) findViewById(R.id.iv_arrow_heart);
		rl_heart.setVisibility(View.GONE);
		handler.sendEmptyMessageDelayed(111, 500);
		
		RelativeLayout imageview = (RelativeLayout) findViewById(R.id.ll_arrow);
		fireView = new ArrowFlakeView(context, this);
		imageview.addView(fireView);
		ArrowAnimationDialog.this.setOnDismissListener(this);
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

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case 111:
				rl_heart.setVisibility(View.VISIBLE);
				AnimationDrawable rocketAnimation = (AnimationDrawable) rl_heart.getBackground();
				rocketAnimation.start();
				break;
			default:
				break;
			}
		};
	};

}
