package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.tshang.peipei.R;
import com.tshang.peipei.vender.wheelview.OnWheelChangedListener;
import com.tshang.peipei.vender.wheelview.WheelView;
import com.tshang.peipei.vender.wheelview.adapters.NumericWheelAdapter;

public class SelectGiftNumDialog extends Dialog implements DialogInterface.OnDismissListener {

	private Activity context;
	private IGiftInfoNumCallBack giftInfoNumCallBack;
	private int value = 1;
	private WheelView mWheelView;

	public SelectGiftNumDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SelectGiftNumDialog(Activity context, int theme, int value, IGiftInfoNumCallBack giftInfoItemOnClickCallBack) {
		super(context, theme);
		this.context = context;
		this.value = value;
		this.giftInfoNumCallBack = giftInfoItemOnClickCallBack;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_select_gift_num);
		mWheelView = (WheelView) this.findViewById(R.id.wheelview);
		mWheelView.setViewAdapter(new NumericWheelAdapter(context, 1, 60));
		mWheelView.setCurrentItem(value - 1);
		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				value = newValue + 1;
			}
		};
		mWheelView.addChangingListener(wheelListener);
		setOnDismissListener(this);

	}

	public void showDialog(int x, int y) {
		try {
			windowDeploy(x, y);

			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy(int x, int y) {
		final Window w = getWindow();
		w.setWindowAnimations(R.style.anim_popwindow_bottombar); // 设置窗口弹出动画
		final WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = WindowManager.LayoutParams.MATCH_PARENT;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.gravity = Gravity.BOTTOM;
		w.setAttributes(wlps);
	}

	public interface IGiftInfoNumCallBack {
		public void getGiftInfoNumCallBack(int value);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (giftInfoNumCallBack != null) {
			giftInfoNumCallBack.getGiftInfoNumCallBack(value);
		}
	}
}
