package com.tshang.peipei.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ClipView extends View {
	public static final int SX = 5;//显示器X轴起始余量
	public static final int EX = 5;//显示器X轴结束余量

	public ClipView(Context context) {
		super(context);
	}

	public ClipView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*这里就是绘制矩形区域*/
		int width = this.getWidth();
		int height = this.getHeight();
		Paint paint = new Paint();
		paint.setColor(0xaa000000);

		//top
		canvas.drawRect(0, 0, width, (height - width) / 2, paint);
		//left
		canvas.drawRect(0, (height - width) / 2, SX, (height + width) / 2, paint);
		//right
		canvas.drawRect(width - EX, (height - width) / 2, width, (height + width) / 2, paint);
		//bottom
		canvas.drawRect(0, (height + width) / 2, width, height, paint);

		Paint paintLine = new Paint();
		paintLine.setColor(0xFFFFFFFF);

		canvas.drawLine(SX, (height - width) / 2, width - EX, (height - width) / 2, paintLine);
		canvas.drawLine(SX, (height - width) / 2, SX, (height + width) / 2, paintLine);
		canvas.drawLine(SX, (height + width) / 2, width - EX, (height + width) / 2, paintLine);
		canvas.drawLine(width - EX, (height - width) / 2, width - EX, (height + width) / 2, paintLine);
	}
}
