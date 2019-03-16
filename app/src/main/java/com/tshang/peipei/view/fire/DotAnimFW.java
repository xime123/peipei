package com.tshang.peipei.view.fire;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.tshang.peipei.R;
import com.tshang.peipei.network.socket.ThreadPoolService;

public class DotAnimFW extends Dot {

	public DotAnimFW(Context context, int color, int y, int endX, int endY) {
		super(context, color, y, endX, endY);
		ThreadPoolService.getInstance().execute(new loadAnim());
	}

	Animation mFWAnim = null;

	class loadAnim implements Runnable {

		public void run() {
			Random random = new Random();
			switch (random.nextInt(4)) {
			case 0:
				mFWAnim = new Animation(mContext, new int[] { R.drawable.fw1_01, R.drawable.fw1_02, R.drawable.fw1_03, R.drawable.fw1_04,
						R.drawable.fw1_05, R.drawable.fw1_06, R.drawable.fw1_07, R.drawable.fw1_08 }, false);
				break;

			case 1:
				mFWAnim = new Animation(mContext, new int[] { R.drawable.fw2_01, R.drawable.fw2_02, R.drawable.fw2_03, R.drawable.fw2_04,
						R.drawable.fw2_05, R.drawable.fw2_06, R.drawable.fw2_07, R.drawable.fw2_08, }, false);
				break;

			case 2:
				mFWAnim = new Animation(mContext, new int[] { R.drawable.fw3_01, R.drawable.fw3_02, R.drawable.fw3_03, R.drawable.fw3_04,
						R.drawable.fw3_05, R.drawable.fw3_06 }, false);
				break;

			case 3:
				mFWAnim = new Animation(mContext, new int[] { R.drawable.fw4_01, R.drawable.fw4_02, R.drawable.fw4_03, R.drawable.fw4_04,
						R.drawable.fw4_05 }, false);
				break;

			default:
				mFWAnim = new Animation(mContext, new int[] { R.drawable.fw1_01, R.drawable.fw1_02, R.drawable.fw1_03, R.drawable.fw1_04,
						R.drawable.fw1_05, R.drawable.fw1_06, R.drawable.fw1_07, R.drawable.fw1_08 }, false);
				break;

			}

		}
	}

	@Override
	public LittleDot[] initBlast() {
		return null;
	}

	@Override
	public LittleDot[] blast() {
		return null;
	}

	public void myPaint(Canvas canvas, Vector<Dot> lList) {
		Paint mPaint = new Paint();
		mPaint.setColor(color);
		if (state == 1) {
			if (mFireAnim != null) {
				mFireAnim.DrawAnimation(canvas, null, x, y);
			}

		}
		if (state == 2) {
			state = 3;

		} else if (state == 3) {
			if (mFWAnim != null) {
				if (mFWAnim.ismIsend() == false) {
					mFWAnim.DrawAnimation(canvas, null, x, y);
				} else {
					circle = 100;
				}
			}
		} else if (state == 4) {
			synchronized (lList) {
				lList.remove(this);
			}
		}

	}

}
