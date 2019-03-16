package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseCameraGalleryPhoto;

public class PhotoSetDialog extends Dialog implements android.view.View.OnClickListener {

	private Activity context;
	public static final int GET_PHOTO_BY_GALLERY = 1020;
	public static final int GET_PHOTO_BY_CAMERA = 1010;

	public PhotoSetDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public PhotoSetDialog(Activity context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.popupwindow_select_picture);
		findViewById(R.id.pop_open_photograph).setOnClickListener(this);
		findViewById(R.id.pop_open_picture).setOnClickListener(this);
		findViewById(R.id.pop_close).setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		this.dismiss();
		switch (v.getId()) {
		//拍照
		case R.id.pop_open_photograph:
			BaseCameraGalleryPhoto.intentSelectImage(context, false, GET_PHOTO_BY_CAMERA);
			break;
		//从相册获取
		case R.id.pop_open_picture:
			BaseCameraGalleryPhoto.intentSelectImage(context, true, GET_PHOTO_BY_GALLERY);
			break;

		case R.id.pop_close:
			break;

		default:
			break;
		}
	}

}
