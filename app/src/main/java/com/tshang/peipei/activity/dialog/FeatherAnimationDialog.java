package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.broadcast.BroadcastQueue;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.view.fall.FeatherView;
import com.tshang.peipei.view.fall.FeatherView.FeatherAnimationEndListener;
import com.tshang.peipei.view.fire.MyFireView.FireCancelAnimationListener;

/**
 * 流星雨动画
 * @author allen
 *
 */
public class FeatherAnimationDialog extends Dialog implements DialogInterface.OnDismissListener, FireCancelAnimationListener,
		FeatherAnimationEndListener {

	private Activity context;
	private BroadcastQueue broadcast;
	private FeatherView fireView;
	private ImageView iv_left, iv_right;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options_head;
	private int uid1, uid2;

	public FeatherAnimationDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public FeatherAnimationDialog(Activity context, int theme, int uid1, int uid2, BroadcastQueue broadcast) {
		super(context, theme);
		this.context = context;
		this.broadcast = broadcast;
		this.uid1 = uid1;
		this.uid2 = uid2;
		imageLoader = ImageLoader.getInstance();
		options_head = ImageOptionsUtils.GetHeadUidSmallRoundedByW(context, 30);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_feather_animation);

		iv_left = (ImageView) findViewById(R.id.iv_feather_left);
		iv_right = (ImageView) findViewById(R.id.iv_feather_right);

		String key1 = uid1 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		String key2 = uid2 + BAConstants.LOAD_HEAD_UID_APPENDSTR;
		imageLoader.displayImage("http://" + key1, iv_left, options_head);
		imageLoader.displayImage("http://" + key2, iv_right, options_head);

		RelativeLayout imageview = (RelativeLayout) findViewById(R.id.ll_feather);
		fireView = new FeatherView(context, this);
		imageview.addView(fireView);
		FeatherAnimationDialog.this.setOnDismissListener(this);
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
