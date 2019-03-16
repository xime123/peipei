	package com.tshang.peipei.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.dialog.CreatAlertDialog;
import com.tshang.peipei.activity.show.PeipeiShowActivity;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.SharedPreferencesTools;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PeipeiFloatingService extends Service {

	// 定义浮动窗口布局
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	// 创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;

	public static ImageView mFloatView;

	ImageView ivStatus;

	@Override
	public void onCreate() {
		super.onCreate();

		createFloatView();
		// Toast.makeText(FxService.this, "create FxService",
		// Toast.LENGTH_LONG);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void createFloatView() {
		wmParams = new WindowManager.LayoutParams();
		// 获取WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		// 设置window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags =
		// LayoutParams.FLAG_NOT_TOUCH_MODAL |
		LayoutParams.FLAG_NOT_FOCUSABLE
		// LayoutParams.FLAG_NOT_TOUCHABLE
		;

		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;

		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = SharedPreferencesTools.getInstance(this).getIntValueByKey(BAConstants.PEIPEI_FLOAT_X, 0);
		wmParams.y = SharedPreferencesTools.getInstance(this).getIntValueByKey(BAConstants.PEIPEI_FLOAT_Y, 0);

		/*
		 * // 设置悬浮窗口长宽数据 wmParams.width = 200; wmParams.height = 80;
		 */

		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
		// 添加mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);

		// 浮动窗口按钮
		mFloatView = (ImageView) mFloatLayout.findViewById(R.id.float_id);

		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		// 设置监听浮动窗口的触摸移动
		mFloatView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						break;
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						// getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
						wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;
						// 25为状态栏的高度
						wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;
						// 刷新
						mWindowManager.updateViewLayout(mFloatLayout, wmParams);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});

		mFloatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PeipeiFloatingService.this, PeipeiShowActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		mFloatView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
//				CreatAlertDialog.showIsFinishDialog();
//				Intent intent = new Intent(PeipeiFloatingService.this, PeipeiFloatingService.class);
//				stopService(intent);
				return true;
			}
		});

		if (BAApplication.showImage != null) {
			mFloatView.setImageBitmap(BAApplication.showImage);
		}

		if (BAApplication.showRoomInfo != null) {
			long time = BAApplication.showRoomInfo.getLefttime();
			ivStatus = (ImageView) mFloatLayout.findViewById(R.id.float_show_room_status);
			if (time > 0) {
				AnimationDrawable anim = (AnimationDrawable) ivStatus.getBackground();
				anim.start();
				ivStatus.setVisibility(View.VISIBLE);
				mFloatLayout.findViewById(R.id.float_pb).setVisibility(View.VISIBLE);
			} else {
				ivStatus.setVisibility(View.GONE);
				mFloatLayout.findViewById(R.id.float_pb).setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mFloatLayout != null) {
			SharedPreferencesTools.getInstance(this).saveIntKeyValue(wmParams.x, BAConstants.PEIPEI_FLOAT_X);
			SharedPreferencesTools.getInstance(this).saveIntKeyValue(wmParams.y, BAConstants.PEIPEI_FLOAT_Y);
			mWindowManager.removeView(mFloatLayout);
		}
	}

}
