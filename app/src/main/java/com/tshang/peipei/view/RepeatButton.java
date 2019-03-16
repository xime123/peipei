package com.tshang.peipei.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.tshang.peipei.base.BaseLog;

/**
 * @author hafly
 */
public class RepeatButton extends ImageButton {
	public static final String TAG = "RepeatButton";
	private long mStart;// 长按开始时间
	private int mRepeatCount;// 长按重复次数
	private RepeatListener mListener;//
	private long mInterval = 500;// 长按一次持续时间
	//	private boolean isClick = false;
	private long time;//时间
	private boolean isBurn = false;

	public boolean isBurn() {
		return isBurn;
	}

	public void setBurn(boolean isBurn) {
		this.isBurn = isBurn;
	}

	//	public boolean isClick() {
	//		return isClick;
	//	}
	//
	//	public void setClick(boolean isClick) {
	//		this.isClick = isClick;
	//	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public RepeatButton(Context context) {
		super(context);
		BaseLog.d(TAG, "RepeatButton1");
	}

	public RepeatButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		BaseLog.d(TAG, "RepeatButton");
		setFocusable(true); // 设置焦点
		setLongClickable(true); // 启用长按事件，长按后会执行performLongClick()
	}

	public RepeatButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		BaseLog.d(TAG, "RepeatButton3");
	}

	private Runnable mThread = new Runnable() {
		public void run() {
			// Log.d(TAG, "mRepeaterThread run()");
			doRepeat(false);
			// if (isPressed()) {
			// Log.d(TAG, "mRepeaterThread run() press");
			postDelayed(this, mInterval);// 延迟mInterval后执行当前线程
			// }
		}
	};

	/**
	 * @param end
	 *            表示最后一次长按，即结束长按事件
	 */
	private void doRepeat(boolean end) {//
										// Log.d(TAG,
										// "mRepeaterThread run() end=" + end);
		long now = SystemClock.elapsedRealtime();// 获取当前时间
		if (mListener != null) {
			mListener.onRepeat(this, now - mStart, end ? -1 : mRepeatCount++);
		}
	}

	/**
	 * 长按结束
	 */
	private void endRepeat() {
		doRepeat(true);
		mStart = 0;
	}

	/**
	 * 设置长按监听事件，初始化mInterval
	 */
	public void setRepeatListener(RepeatListener listener, long interval) {
		// Log.d(TAG, "setRepeatListener interval=" + interval);
		mListener = listener;
		mInterval = interval;
	}

	@Override
	public boolean performLongClick() {
		mStart = System.currentTimeMillis();// 获取系统当前时间
		mRepeatCount = 0;
		post(mThread);// 调用post()方法，执行mThread
		return true;
	}

	public void onBurnFile() {
		removeCallbacks(mThread);// 删除队列当中未执行的线程对象
		if (mStart != 0) {
			endRepeat();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				removeCallbacks(mThread);// 删除队列当中未执行的线程对象
				if (mStart != 0) {
					endRepeat();
				}
			}
			return super.onTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			super.onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			removeCallbacks(mThread);
			if (mStart != 0) {
				endRepeat();
			}
			super.onKeyUp(keyCode, event);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

}
