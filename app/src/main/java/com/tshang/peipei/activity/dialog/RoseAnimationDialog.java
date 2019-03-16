package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.view.fall.RoseFlakeView;
import com.tshang.peipei.view.fall.RoseFlakeView.RoseAnimationEndListener;

/**
 * 烟花动画接口
 * @author Jeff
 *
 */
public class RoseAnimationDialog extends Dialog implements DialogInterface.OnDismissListener, RoseAnimationEndListener {

	private Activity context;
	private BroadcastQueue broadcast;
	private RoseFlakeView fireView;

	public RoseAnimationDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public RoseAnimationDialog(Activity context, int theme, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_fire_animation);
		LinearLayout imageview = (LinearLayout) findViewById(R.id.ll_fire);
		fireView = new RoseFlakeView(context, this);
		imageview.addView(fireView);
		//		context.fireView.setLayerType( View.LAYER_TYPE_NONE, null);
		RoseAnimationDialog.this.setOnDismissListener(this);
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
