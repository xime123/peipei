package com.tshang.peipei.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tshang.peipei.R;

public class TasksCompletedView extends View {// 234 67 0

	// 画实心圆的画笔
	//	private Paint mCirclePaint;
	// 画圆环的画笔
	private Paint mRingProgressPaint;
	// 画圆环的画笔
	private Paint mRingtwoPaint;
	// 画圆环的画笔
	private Paint mRingPaint;
	// 圆形颜色
	//	private int mCircleColor;
	// 圆环颜色
	private int mRingColor;
	// 半径
	private float mRadius;
	// 圆环半径
	private float mRingRadius;
	// 圆环宽度
	private float mStrokeWidth;
	// 圆心x坐标
	private int mXCenter;
	// 圆心y坐标
	private int mYCenter;
	// 字的长度
	//	private float mTxtWidth;
	// 字的高度
	//	private float mTxtHeight;
	// 总进度
	private int mTotalProgress = 60;
	// 当前进度
	private int mProgress;

	public void setRingColor(int color) {
		mRingPaint.setColor(color);
	}

	public TasksCompletedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TasksCompletedView, 0, 0);
		mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_circleradius, 50);
		mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_circlestrokeWidth, 1);
		//		mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, android.R.color.white);
		mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);

		mRingRadius = mRadius + mStrokeWidth / 2;
	}

	private void initVariable() {
		//		mCirclePaint = new Paint();
		//		mCirclePaint.setAntiAlias(true);// 设置画笔为抗锯齿
		//		mCirclePaint.setColor(mCircleColor);
		//		mCirclePaint.setStyle(Paint.Style.FILL);

		mRingProgressPaint = new Paint();
		mRingProgressPaint.setAntiAlias(true);
		mRingProgressPaint.setColor(getResources().getColor(R.color.ring_three_color));
		mRingProgressPaint.setStyle(Paint.Style.STROKE);
		mRingProgressPaint.setStrokeWidth(mStrokeWidth);

		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);

		mRingtwoPaint = new Paint();
		mRingtwoPaint.setAntiAlias(true);
		mRingtwoPaint.setColor(getResources().getColor(R.color.ring_two_color));
		mRingtwoPaint.setStyle(Paint.Style.STROKE);
		mRingtwoPaint.setStrokeWidth(mStrokeWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width = this.getWidth();
		int height = this.getHeight();

		if (width != height) {
			int min = Math.min(width, height);
			width = min;
			height = min;
		}
		mXCenter = width / 2;
		mYCenter = height / 2;

		//		canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
		//		canvas.drawCircle(mXCenter, mYCenter, mRingRadius, mRingProgressPaint);

		//		RectF oval1 = new RectF();
		//		oval1.left = (mXCenter - mRingRadius);
		//		oval1.top = (mYCenter - mRingRadius);
		//		oval1.right = mRingRadius * 2 + (mXCenter - mRingRadius);
		//		oval1.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
		//		canvas.drawArc(oval1, 0, 360, false, mRingtwoPaint); //
		// canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2,
		// mRingPaint);
		if (mProgress > 0) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
			canvas.drawArc(oval, 270, ((float) mProgress / mTotalProgress) * 360, false, mRingPaint); //
		}

	}

	public void setmTotalProgress(int mTotalProgress) {
		this.mTotalProgress = mTotalProgress;
	}

	public void setProgress(int progress) {
		mProgress = progress;
		// invalidate();
		postInvalidate();
	}

}
