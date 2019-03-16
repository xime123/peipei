package com.tshang.peipei.view.fire;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Animation {

	/** ��һ֡����ʱ�� **/
	private long mLastPlayTime = 0;
	/** ���ŵ�ǰ֡��ID **/
	private int mPlayID = 0;
	/** ����frame���� **/
	private int mFrameCount = 0;
	/** ���ڴ��涯����ԴͼƬ **/
	private Bitmap[] mframeBitmap = null;
	/** �Ƿ�ѭ������ **/
	private boolean mIsLoop = false;
	/** ���Ž��� **/
	private boolean mIsend = false;
	/** �������ż�϶ʱ�� **/
	private static final int ANIM_TIME = 150;

	/**
	 * ���캯��
	 * 
	 * @param context
	 * @param frameBitmapID
	 * @param isloop
	 */
	public Animation(Context context, int[] frameBitmapID, boolean isloop) {
		mFrameCount = frameBitmapID.length;
		mframeBitmap = new Bitmap[mFrameCount];
		for (int i = 0; i < mFrameCount; i++) {
			mframeBitmap[i] = ReadBitMap(context, frameBitmapID[i]);
		}
		mIsLoop = isloop;
	}

	/**
	 * ���캯��
	 * 
	 * @param context
	 * @param frameBitmap
	 * @param isloop
	 */
	public Animation(Context context, Bitmap[] frameBitmap, boolean isloop) {
		mFrameCount = frameBitmap.length;
		mframeBitmap = frameBitmap;
		mIsLoop = isloop;
	}

	/**
	 * ���ƶ����е�����һ֡
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 * @param frameID
	 */
	public void DrawFrame(Canvas Canvas, Paint paint, int x, int y, int frameID) {
		Canvas.drawBitmap(mframeBitmap[frameID], x, y, paint);
	}

	/**
	 * ���ƶ���
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 */
	public void DrawAnimation(Canvas Canvas, Paint paint, int x, int y) {
		// ���û�в��Ž���������
		if (!mIsend) {
			Canvas.drawBitmap(mframeBitmap[mPlayID], x - mframeBitmap[mPlayID].getWidth() / 2, y - mframeBitmap[mPlayID].getHeight() / 2, paint);
			long time = System.currentTimeMillis();
			if (time - mLastPlayTime > ANIM_TIME) {
				mPlayID++;
				mLastPlayTime = time;
				if (mPlayID >= mFrameCount) {
					// ��־�������Ž���
					mIsend = true;
					if (mIsLoop) {
						// ����ѭ������
						mIsend = false;
						mPlayID = 0;
					}
				}
			}
		}
	}

	public boolean ismIsend() {
		return mIsend;
	}

	public void setmIsend(boolean mIsend) {
		this.mIsend = mIsend;
	}

	/**
	 * ��ȡͼƬ��Դ
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap ReadBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
		//		return ImageLoader.getInstance().loadImageSync("drawable://"+resId);
	}
}
