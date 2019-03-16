package com.tshang.peipei.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * @ClassName: ClipImageLayout
 * @Description:
 * @author xiechengfa2000@163.com
 * @date 2015-5-10 下午10:22:24
 */
public class ClipImageLayout extends RelativeLayout {
	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	private int mHorizontalPadding = 0;// 框左右的边距，这里左右边距为0，为�?��屏幕宽度的正方形�?

	public ClipImageLayout(Context context) {
		super(context);
		init(context);
	}

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}

	public void setImageDrawable(Drawable drawable) {
		mZoomImageView.setImageDrawable(drawable);
	}

	public void setImageBitmap(Bitmap bitmap) {
		mZoomImageView.setImageBitmap(bitmap);
	}

	public void setImagePath(String path) {
		mZoomImageView.setImagePath(path);
	}

	public void setContentText(String msg) {
		mZoomImageView.setText(msg);
	}

	public void setTitleTextView(String title) {
		mZoomImageView.setTitleTextView(title);
	}

	/**
	 * 对外公布设置边距的方�?单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

	public void setTextColor(int color) {
		mZoomImageView.setTextColor(color);
		invalidate();
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}
}
