package com.tshang.peipei.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.tshang.peipei.R;
import com.tshang.peipei.model.request.RequestSetUserBit.ISwitchPush;

/**
 * 广播三级滑动按钮
 * @author Jeff
 *
 */
public class MySlipSwitch extends View implements OnTouchListener {

	//开关开启时的背景，关闭时的背景，滑动按钮
	private Bitmap switch_one_Bkg, switch_two_Bkg, switch_three_Bkg, slip_Btn, slip_btn1, slip_btn2, slip_btn3;
	private Rect one_Rect, two_Rect, three_Rect;

	//是否正在滑动
	private boolean isSlipping = false;

	public static final int SELECT_FIRST = 1;
	public static final int SELECT_SECOND = 2;
	public static final int SELECT_THIRD = 3;

	//当前开关状态
	private int isSwitchOn = SELECT_FIRST;

	//手指按下时的水平坐标X，当前的水平坐标X
	private float previousX, currentX;

	//开关监听器
	private OnSwitchListener onSwitchListener;
	//是否设置了开关监听器
	private boolean isSwitchListenerOn = false;

	public MySlipSwitch(Context context) {
		super(context);
		init();
	}

	public float getCurrentX() {
		return currentX;
	}

	public void setCurrentX(float currentX) {
		this.currentX = currentX;
	}

	public MySlipSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOnTouchListener(this);
	}

	public void setImageResource(int switchOnBkg, int switchOffBkg, int thirdBkg, int slipBtn) {
		switch_one_Bkg = BitmapFactory.decodeResource(getResources(), switchOnBkg);
		switch_two_Bkg = BitmapFactory.decodeResource(getResources(), switchOffBkg);
		switch_three_Bkg = BitmapFactory.decodeResource(getResources(), thirdBkg);
		slip_btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.broadcast_switch);
		slip_btn2 = BitmapFactory.decodeResource(getResources(), R.drawable.broadcast_switch1);
		slip_btn3 = BitmapFactory.decodeResource(getResources(), R.drawable.broadcast_switch2);
		slip_Btn = slip_btn1;

		//右半边Rect，即滑动按钮在右半边时表示开关开启
		one_Rect = new Rect(0, 5, switch_one_Bkg.getWidth() / 3, slip_Btn.getHeight());
		//中间半边
		two_Rect = new Rect(switch_one_Bkg.getWidth() / 3, 5, switch_one_Bkg.getWidth() * 2 / 3, slip_Btn.getHeight());
		//左半边Rect，即滑动按钮在左半边时表示开关关闭
		three_Rect = new Rect(switch_one_Bkg.getWidth() * 2 / 3, 5, switch_one_Bkg.getWidth(), slip_Btn.getHeight());
	}

	public int getIsSwitchOn() {
		return isSwitchOn;
	}

	public void setIsSwitchOn(int isSwitchOn) {
		this.isSwitchOn = isSwitchOn;
	}

	public void updateSwitchState(int switchState) {
		isSwitchOn = switchState;
		if (isSwitchOn == SELECT_FIRST) {
			currentX = (switch_one_Bkg.getWidth() / 3) - 1;
		} else if (isSwitchOn == SELECT_SECOND) {
			currentX = (switch_one_Bkg.getWidth() * 2 / 3) - 1;
		} else {
			currentX = (switch_one_Bkg.getWidth() * 2 / 3) + 1;
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		//滑动按钮的左边坐标
		float left_SlipBtn;

		//手指滑动到左半边的时候表示开关为关闭状态，滑动到右半边的时候表示开关为开启状态
		if (currentX < (switch_one_Bkg.getWidth() / 3)) {
			canvas.drawBitmap(switch_one_Bkg, matrix, paint);
		} else if (currentX < (switch_one_Bkg.getWidth() * 2 / 3)) {
			canvas.drawBitmap(switch_two_Bkg, matrix, paint);
		} else {
			canvas.drawBitmap(switch_three_Bkg, matrix, paint);
		}

		//判断当前是否正在滑动
		if (isSlipping) {
			if (currentX > switch_one_Bkg.getWidth()) {
				left_SlipBtn = switch_one_Bkg.getWidth() - slip_Btn.getWidth();
			} else {
				left_SlipBtn = currentX - slip_Btn.getWidth() / 2;
			}
		} else {
			//根据当前的开关状态设置滑动按钮的位置
			if (isSwitchOn == SELECT_FIRST) {
				left_SlipBtn = one_Rect.left;
			} else if (isSwitchOn == SELECT_SECOND) {
				left_SlipBtn = two_Rect.left;
			} else {
				left_SlipBtn = three_Rect.left;
			}
		}

		//对滑动按钮的位置进行异常判断
		if (left_SlipBtn < 0) {
			left_SlipBtn = 0;
		} else if (left_SlipBtn > switch_one_Bkg.getWidth() - slip_Btn.getWidth()) {
			left_SlipBtn = switch_one_Bkg.getWidth() - slip_Btn.getWidth();
		}
		if (isSwitchOn == SELECT_FIRST) {
			slip_Btn = slip_btn1;
		} else if (isSwitchOn == SELECT_SECOND) {
			slip_Btn = slip_btn2;
		} else {
			slip_Btn = slip_btn3;
		}

		canvas.drawBitmap(slip_Btn, left_SlipBtn, 0, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(switch_one_Bkg.getWidth(), switch_one_Bkg.getHeight());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		//滑动
		case MotionEvent.ACTION_MOVE:
			currentX = event.getX();
			break;

		//按下
		case MotionEvent.ACTION_DOWN:
			if (event.getX() > switch_one_Bkg.getWidth() || event.getY() > switch_one_Bkg.getHeight()) {
				return false;
			}

			isSlipping = true;
			previousX = event.getX();
			currentX = previousX;
			break;

		//松开
		case MotionEvent.ACTION_UP:
			isSlipping = false;
			//松开前开关的状态
			int previousSwitchState = isSwitchOn;

			if (event.getX() >= (switch_one_Bkg.getWidth() * 2 / 3)) {
				isSwitchOn = SELECT_THIRD;
			} else if (event.getX() >= (switch_one_Bkg.getWidth() / 3)) {
				isSwitchOn = SELECT_SECOND;
			} else {
				isSwitchOn = SELECT_FIRST;
			}

			//如果设置了监听器，则调用此方法
			if (isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
				onSwitchListener.onSwitched(isSwitchOn);
			}
			break;

		default:
			break;
		}

		//重新绘制控件
		invalidate();
		return true;
	}

	public void setOnSwitchListener(OnSwitchListener listener) {
		onSwitchListener = listener;
		isSwitchListenerOn = true;
	}

	public interface OnSwitchListener {
		abstract void onSwitched(int isSwitchOn);
	}
}
