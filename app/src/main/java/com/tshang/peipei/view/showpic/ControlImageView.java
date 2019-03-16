package com.tshang.peipei.view.showpic;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.tshang.peipei.base.BaseLog;

@SuppressLint("HandlerLeak")
public class ControlImageView implements IImageView, View.OnTouchListener, MyGestureDetetor.OnGestureListener, GestureDetector.OnDoubleTapListener,
		ViewTreeObserver.OnGlobalLayoutListener {

	static final String LOG_TAG = "ControlImageView";

	// let debug flag be dynamic, but still Proguard can be used to remove from
	// release builds
	static final boolean DEBUG = false;

	static final int EDGE_NONE = -1;
	static final int EDGE_LEFT = 0;
	static final int EDGE_RIGHT = 1;
	static final int EDGE_BOTH = 2;

	private float mMinScale = 1.0f;
	private float mMidScale = 1.5f;
	private float mMaxScale = 2f;

	private boolean mAllowParentInterceptOnEdge = true;

	private WeakReference<ImageView> mImageView;// 加载图片的控件的缓存
	private ViewTreeObserver mViewTreeObserver;// 控件位置变化侦听

	// Gesture Detectors
	private GestureDetector mGestureDetector;// 控件手势变化侦听
	private MyGestureDetetor mScaleDragDetector;// 控件手势变化控制拖动的操作

	// These are set so we don't keep allocating them on the heap
	private final Matrix mBaseMatrix = new Matrix();
	private final Matrix mDrawMatrix = new Matrix();
	private final Matrix mSuppMatrix = new Matrix();
	private final RectF mDisplayRect = new RectF();
	private final float[] mMatrixValues = new float[9];

	// Listeners
	private OnMatrixChangedListener mMatrixChangeListener;
	private OnPhotoTapListener mPhotoTapListener;
	private OnViewTapListener mViewTapListener;
	private OnLongClickListener mLongClickListener;

	private int mIvTop, mIvRight, mIvBottom, mIvLeft;// 图片距离控件的边距
	private FlingRunnable mCurrentFlingRunnable;
	private int mScrollEdge = EDGE_BOTH;

	private boolean mZoomEnabled;// 是否可以缩放
	private boolean isStarting = true;// 是否第一次加载进入
	private ScaleType mScaleType = ScaleType.FIT_CENTER;

	private Handler mHand = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				setStart();
				break;
			default:
				break;
			}
		}

	};

	public ControlImageView(ImageView imageView, Context context) {
		mImageView = new WeakReference<ImageView>(imageView);

		imageView.setOnTouchListener(this);

		mViewTreeObserver = imageView.getViewTreeObserver();
		mViewTreeObserver.addOnGlobalLayoutListener(this);

		if (!imageView.isInEditMode()) {
			// Create Gesture Detectors...
			mScaleDragDetector = MyGestureDetetor.newInstance(imageView.getContext(), this);

			mGestureDetector = new GestureDetector(imageView.getContext(), new GestureDetector.SimpleOnGestureListener() {

				// forward long click listener
				@Override
				public void onLongPress(MotionEvent e) {
					if (null != mLongClickListener) {
						mLongClickListener.onLongClick(mImageView.get());
					}
				}
			});

			mGestureDetector.setOnDoubleTapListener(this);

			// Make sure we using MATRIX Scale Type
			setImageViewScaleTypeMatrix(imageView);
			// Finally, update the UI so that we're zoomable
			setZoomable(true);
			mZoomEnabled = true;
		}
	}

	// 控制imageview加载到图片为止
	private void setMaxtixFromBackground() {
		mHand.sendEmptyMessageDelayed(0, 200);
	}

	/**
	 * 控制图片 第一次显示的时候imageview加载到drawable调用，用于控制图片居中
	 */
	public void setStart() {
		ImageView imageView = getImageView();

		final float viewWidth = imageView.getWidth();
		final float viewHeight = imageView.getHeight();

		Drawable d = imageView.getDrawable();
		float dWidth = d.getIntrinsicWidth();
		float dHeight = d.getIntrinsicHeight();

		if (dWidth == 0 || dHeight == 0) {
			dWidth = 480;
			dHeight = 640;
			setMaxtixFromBackground();
			return;
		}

		mBaseMatrix.reset();
		mSuppMatrix.reset();

		RectF mTempSrc = new RectF(110, 110, 110, 110);
		RectF mTempDst = new RectF(110, 110, 110, 110);

		mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.CENTER);

		mSuppMatrix.postTranslate((viewWidth - dWidth) / 2, (viewHeight - dHeight) / 2);
		if (viewWidth != 0) {
			if (dWidth > viewWidth) {
				float scale = viewWidth / dWidth;
				mMinScale = scale;
				mMidScale = scale + 0.25f;
				mMaxScale = scale + 0.5f;
				mSuppMatrix.postScale(scale, scale, viewWidth / 2, viewHeight / 2);
				setImageViewMatrix(getDisplayMatrix());
			} else {
				// mMinScale = dWidth / viewWidth;
				if (dWidth <= (viewWidth / 2)) {// 屏幕分辨率大 显得图片过小 处理
					mMinScale = 1.5f;
					mMidScale = 2.0f;
					mMaxScale = 2.5f;
					mSuppMatrix.postScale(mMinScale, mMinScale, viewWidth / 2, viewHeight / 2);
					setImageViewMatrix(getDisplayMatrix());
				} else {
					mSuppMatrix.reset();
					mSuppMatrix.postTranslate((viewWidth - dWidth) / 2, (viewHeight - dHeight) / 2);
					setImageViewMatrix(getDisplayMatrix());
				}
			}
		} else {
			mSuppMatrix.postScale(mMinScale, mMinScale, viewWidth / 2, viewHeight / 2);
			mSuppMatrix.postTranslate((viewWidth - dWidth), (viewHeight - dHeight));
			mSuppMatrix.postScale(mMinScale, mMinScale, viewWidth / 2, viewHeight / 2);
			imageView.setImageMatrix(mSuppMatrix);
			mSuppMatrix.reset();
			mSuppMatrix.postTranslate(300, 300);
			imageView.setImageMatrix(mSuppMatrix);

			if (hasDrawable(imageView)) {
				mCurrentFlingRunnable = new FlingRunnable(imageView.getContext());
				mCurrentFlingRunnable.fling(imageView.getWidth(), imageView.getHeight(), 100, 100);
				imageView.post(mCurrentFlingRunnable);
			}
		}
	}

	/**
	 * @return true if the ImageView exists, and it's Drawable existss
	 */
	private static boolean hasDrawable(ImageView imageView) {
		return null != imageView && null != imageView.getDrawable();
	}

	/**
	 * Set's the ImageView's ScaleType to Matrix.
	 */
	private static void setImageViewScaleTypeMatrix(ImageView imageView) {
		if (null != imageView) {
			if (imageView instanceof IImageView) {
				/**
				 * PhotoView sets it's own ScaleType to Matrix, then diverts all
				 * calls setScaleType to this.setScaleType. Basically we don't
				 * need to do anything here
				 */
			} else {
				imageView.setScaleType(ScaleType.MATRIX);
			}
		}
	}

	// 是否可以缩放
	@Override
	public final boolean canZoom() {
		return mZoomEnabled;
	}

	// 当前显示矩阵
	@Override
	public final RectF getDisplayRect() {
		checkMatrixBounds();
		return getDisplayRect(getDisplayMatrix());
	}

	public final ImageView getImageView() {
		ImageView imageView = null;

		if (null != mImageView) {
			imageView = mImageView.get();
		}

		// If we don't have an ImageView, call cleanup()
		if (null == imageView) {
			cleanup();
			// throw new IllegalStateException(
			// "ImageView no longer exists. You should not use this PhotoViewAttacher any more.");
		}

		return imageView;
	}

	@Override
	public float getMinScale() {
		return mMinScale;
	}

	@Override
	public float getMidScale() {
		return mMidScale;
	}

	@Override
	public float getMaxScale() {
		return mMaxScale;
	}

	@Override
	public final float getScale() {
		return getValue(mSuppMatrix, Matrix.MSCALE_X);
	}

	@Override
	public final ScaleType getScaleType() {
		return mScaleType;
	}

	// 手势双击的时候处理
	public final boolean onDoubleTap(MotionEvent ev) {
		try {
			float scale = getScale();
			float x = ev.getX();
			float y = ev.getY();

			if (scale < mMidScale) {
				zoomTo(mMidScale, x, y);
			} else if (scale >= mMidScale && scale < mMaxScale) {
				zoomTo(mMaxScale, x, y);
			} else {
				zoomTo(mMinScale, x, y);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// Can sometimes happen when getX() and getY() is called
		}

		return true;
	}

	public final boolean onDoubleTapEvent(MotionEvent e) {
		// Wait for the confirmed onDoubleTap() instead
		return false;
	}

	// 移动控件
	public final void onDrag(float dx, float dy) {
		if (DEBUG) {
			BaseLog.d(LOG_TAG, String.format("onDrag: dx: %.2f. dy: %.2f", dx, dy));
		}

		ImageView imageView = getImageView();

		if (null != imageView && hasDrawable(imageView)) {
			mSuppMatrix.postTranslate(dx, dy);
			checkAndDisplayMatrix();

			/**
			 * Here we decide whether to let the ImageView's parent to start
			 * taking over the touch event.
			 * 
			 * First we check whether this function is enabled. We never want
			 * the parent to take over if we're scaling. We then check the edge
			 * we're on, and the direction of the scroll (i.e. if we're pulling
			 * against the edge, aka 'overscrolling', let the parent take over).
			 */
			if (mAllowParentInterceptOnEdge && !mScaleDragDetector.isScaling()) {
				if (mScrollEdge == EDGE_BOTH || (mScrollEdge == EDGE_LEFT && dx >= 1f) || (mScrollEdge == EDGE_RIGHT && dx <= -1f)) {
					imageView.getParent().requestDisallowInterceptTouchEvent(false);
				}
			}
		}
	}

	// 手势滑动控件
	@Override
	public final void onFling(float startX, float startY, float velocityX, float velocityY) {
		ImageView imageView = getImageView();
		if (hasDrawable(imageView)) {
			mCurrentFlingRunnable = new FlingRunnable(imageView.getContext());
			mCurrentFlingRunnable.fling(imageView.getWidth(), imageView.getHeight(), (int) velocityX, (int) velocityY);
			imageView.post(mCurrentFlingRunnable);
		}
	}

	// 控件位置变化的时候侦听
	@Override
	public final void onGlobalLayout() {
		ImageView imageView = getImageView();
		if (null != imageView && mZoomEnabled) {
			final int top = imageView.getTop();
			final int right = imageView.getRight();
			final int bottom = imageView.getBottom();
			final int left = imageView.getLeft();

			/**
			 * We need to check whether the ImageView's bounds have changed.
			 * This would be easier if we targeted API 11+ as we could just use
			 * View.OnLayoutChangeListener. Instead we have to replicate the
			 * work, keeping track of the ImageView's bounds and then checking
			 * if the values change.
			 */
			if (top != mIvTop || bottom != mIvBottom || left != mIvLeft || right != mIvRight) {
				// Update our base matrix, as the bounds have changed
				updateBaseMatrix(imageView.getDrawable());

				// Update values as something has changed
				mIvTop = top;
				mIvRight = right;
				mIvBottom = bottom;
				mIvLeft = left;
			}
		}
	}

	// 控件imageview缩放
	public final void onScale(float scaleFactor, float focusX, float focusY) {
		if (DEBUG) {
			BaseLog.d(LOG_TAG, String.format("onScale: scale: %.2f. fX: %.2f. fY: %.2f", scaleFactor, focusX, focusY));
		}
		if (hasDrawable(getImageView()) && (getScale() < mMaxScale || scaleFactor < 1f)) {
			mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
			checkAndDisplayMatrix();
		}
	}

	// 手势单击
	public final boolean onSingleTapConfirmed(MotionEvent e) {
		ImageView imageView = getImageView();
		if (null != imageView) {
			if (null != mPhotoTapListener) {
				final RectF displayRect = getDisplayRect();

				if (null != displayRect) {
					final float x = e.getX(), y = e.getY();

					// Check to see if the user tapped on the photo
					if (displayRect.contains(x, y)) {

						float xResult = (x - displayRect.left) / displayRect.width();
						float yResult = (y - displayRect.top) / displayRect.height();

						mPhotoTapListener.onPhotoTap(imageView, xResult, yResult);
						return true;
					}
				}
			}
			if (null != mViewTapListener) {
				mViewTapListener.onViewTap(imageView, e.getX(), e.getY());
			}
		}

		return false;
	}

	@Override
	public final boolean onTouch(View v, MotionEvent ev) {
		boolean handled = false;
		try {
			isStarting = true;
			if (mZoomEnabled) {
				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// First, disable the Parent from intercepting the touch
					// event
					v.getParent().requestDisallowInterceptTouchEvent(true);

					// If we're flinging, and the user presses down, cancel
					// fling
					cancelFling();
					break;

				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					// If the user has zoomed less than min scale, zoom back
					// to min scale
					if (getScale() < mMinScale) {
						RectF rect = getDisplayRect();
						if (null != rect) {
							v.post(new AnimatedZoomRunnable(getScale(), mMinScale, rect.centerX(), rect.centerY()));
							handled = true;
						}
					}
					break;
				}

				// Check to see if the user double tapped
				if (null != mGestureDetector && mGestureDetector.onTouchEvent(ev)) {
					handled = true;
				}

				// Finally, try the Scale/Drag detector
				if (null != mScaleDragDetector && mScaleDragDetector.onTouchEvent(ev)) {
					handled = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return handled;
	}

	@Override
	public void setAllowParentInterceptOnEdge(boolean allow) {
		mAllowParentInterceptOnEdge = allow;
	}

	@Override
	public void setMinScale(float minScale) {
		mMinScale = minScale;
	}

	@Override
	public void setMidScale(float midScale) {
		mMidScale = midScale;
	}

	@Override
	public void setMaxScale(float maxScale) {
		mMaxScale = maxScale;
	}

	@Override
	public final void setOnLongClickListener(OnLongClickListener listener) {
		mLongClickListener = listener;
	}

	@Override
	public final void setScaleType(ScaleType scaleType) {
		if (scaleType != mScaleType) {
			mScaleType = scaleType;
			// Finally update
			update();
		}
	}

	@Override
	public final void setZoomable(boolean zoomable) {
		mZoomEnabled = zoomable;
		update();
	}

	public final void update() {
		ImageView imageView = getImageView();
		if (null != imageView) {
			if (mZoomEnabled) {
				// Make sure we using MATRIX Scale Type
				setImageViewScaleTypeMatrix(imageView);

				// Update the base matrix using the current drawable
				updateBaseMatrix(imageView.getDrawable());
			} else {
				// Reset the Matrix...
				resetMatrix();
			}
		}
	}

	@Override
	public final void zoomTo(float scale, float focalX, float focalY) {
		ImageView imageView = getImageView();

		if (null != imageView) {
			imageView.post(new AnimatedZoomRunnable(getScale(), scale, focalX, focalY));
		}
	}

	protected Matrix getDisplayMatrix() {
		mDrawMatrix.set(mBaseMatrix);
		mDrawMatrix.postConcat(mSuppMatrix);
		return mDrawMatrix;
	}

	private void cancelFling() {
		if (null != mCurrentFlingRunnable) {
			mCurrentFlingRunnable.cancelFling();
			mCurrentFlingRunnable = null;
		}
	}

	/**
	 * Helper method that simply checks the Matrix, and then displays the result
	 */
	private void checkAndDisplayMatrix() {
		checkMatrixBounds();
		setImageViewMatrix(getDisplayMatrix());
	}

	private void checkImageViewScaleType() {
		ImageView imageView = getImageView();

		/**
		 * PhotoView's getScaleType() will just divert to this.getScaleType() so
		 * only call if we're not attached to a PhotoView.
		 */
		if (null != imageView && !(imageView instanceof IImageView)) {
			imageView.setScaleType(ScaleType.MATRIX);
		}
	}

	// 调节要显示控件的的矩阵的边界
	private void checkMatrixBounds() {
		final ImageView imageView = getImageView();
		if (null == imageView) {
			return;
		}

		final RectF rect = getDisplayRect(getDisplayMatrix());
		if (null == rect) {
			return;
		}

		final float height = rect.height(), width = rect.width();
		float deltaX = 0, deltaY = 0;

		final int viewHeight = imageView.getHeight();
		if (height <= viewHeight) {
			switch (mScaleType) {
			case FIT_START:
				deltaY = -rect.top;
				break;
			case FIT_END:
				deltaY = viewHeight - height - rect.top;
				break;
			default:
				deltaY = (viewHeight - height) / 2 - rect.top;
				break;
			}
		} else if (rect.top > 0) {
			deltaY = -rect.top;
		} else if (rect.bottom < viewHeight) {
			deltaY = viewHeight - rect.bottom;
		}

		final int viewWidth = imageView.getWidth();
		if (width <= viewWidth) {
			switch (mScaleType) {
			case FIT_START:
				deltaX = -rect.left;
				break;
			case FIT_END:
				deltaX = viewWidth - width - rect.left;
				break;
			default:
				deltaX = (viewWidth - width) / 2 - rect.left;
				break;
			}
			mScrollEdge = EDGE_BOTH;
		} else if (rect.left > 0) {
			mScrollEdge = EDGE_LEFT;
			deltaX = -rect.left;
		} else if (rect.right < viewWidth) {
			deltaX = viewWidth - rect.right;
			mScrollEdge = EDGE_RIGHT;
		} else {
			mScrollEdge = EDGE_NONE;
		}

		// Finally actually translate the matrix
		mSuppMatrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * Helper method that maps the supplied Matrix to the current Drawable
	 * 
	 * @param matrix
	 *            - Matrix to map Drawable against
	 * @return RectF - Displayed Rectangle
	 */
	private RectF getDisplayRect(Matrix matrix) {
		ImageView imageView = getImageView();
		if (null != imageView) {
			Drawable d = imageView.getDrawable();
			if (null != d) {
				mDisplayRect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				matrix.mapRect(mDisplayRect);
				return mDisplayRect;
			}
		}
		return null;
	}

	/**
	 * Helper method that 'unpacks' a Matrix and returns the required value
	 * 
	 * @param matrix
	 *            - Matrix to unpack
	 * @param whichValue
	 *            - Which value from Matrix.M* to return
	 * @return float - returned value
	 */
	private float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		return mMatrixValues[whichValue];
	}

	/**
	 * Resets the Matrix back to FIT_CENTER, and then displays it.s
	 */
	private void resetMatrix() {
		mSuppMatrix.reset();
		setImageViewMatrix(getDisplayMatrix());
		checkMatrixBounds();
	}

	// 设置控件的 矩阵
	private void setImageViewMatrix(Matrix matrix) {
		ImageView imageView = getImageView();
		if (null != imageView) {
			checkImageViewScaleType();
			imageView.setImageMatrix(matrix);

			// Call MatrixChangedListener if needed
			if (null != mMatrixChangeListener) {
				RectF displayRect = getDisplayRect(matrix);
				if (null != displayRect) {
					mMatrixChangeListener.onMatrixChanged(displayRect);
				}
			}
		}
	}

	/**
	 * Calculate Matrix for FIT_CENTER
	 * 
	 * @param d
	 *            - Drawable being displayed
	 */
	private void updateBaseMatrix(Drawable d) {
		ImageView imageView = getImageView();
		if (null == imageView || null == d) {
			return;
		}

		final float viewWidth = imageView.getWidth();
		final float viewHeight = imageView.getHeight();
		final int drawableWidth = d.getIntrinsicWidth();
		final int drawableHeight = d.getIntrinsicHeight();

		mBaseMatrix.reset();

		final float widthScale = viewWidth / drawableWidth;
		final float heightScale = viewHeight / drawableHeight;

		if (mScaleType == ScaleType.CENTER) {
			mBaseMatrix.postTranslate((viewWidth - drawableWidth) / 2F, (viewHeight - drawableHeight) / 2F);

		} else if (mScaleType == ScaleType.CENTER_CROP) {
			float scale = Math.max(widthScale, heightScale);
			mBaseMatrix.postScale(scale, scale);
			mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2F, (viewHeight - drawableHeight * scale) / 2F);

		} else if (mScaleType == ScaleType.CENTER_INSIDE) {
			float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
			mBaseMatrix.postScale(scale, scale);
			mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2F, (viewHeight - drawableHeight * scale) / 2F);

		} else {
			RectF mTempSrc = null;
			RectF mTempDst = null;
			if (isStarting) {
				mTempSrc = new RectF(viewWidth / 2, viewHeight / 2, viewWidth / 2, viewHeight / 2);
				mTempDst = new RectF(viewWidth / 2, viewHeight / 2, viewWidth, viewHeight);
			} else {
				mTempSrc = new RectF(0, 0, drawableWidth, drawableHeight);
				mTempDst = new RectF(0, 0, viewWidth, viewHeight);
			}

			switch (mScaleType) {
			case FIT_CENTER:
				mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.CENTER);
				break;

			case FIT_START:
				mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.START);
				break;

			case FIT_END:
				mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.END);
				break;

			case FIT_XY:
				mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.FILL);
				break;

			default:
				break;
			}
		}

		resetMatrix();
	}

	/**
	 * Interface definition for a callback to be invoked when the internal
	 * Matrix has changed for this View.
	 * 
	 * @author Chris Banes
	 */
	public static interface OnMatrixChangedListener {
		/**
		 * Callback for when the Matrix displaying the Drawable has changed.
		 * This could be because the View's bounds have changed, or the user has
		 * zoomed.
		 * 
		 * @param rect
		 *            - Rectangle displaying the Drawable's new bounds.
		 */
		void onMatrixChanged(RectF rect);
	}

	/**
	 * Interface definition for a callback to be invoked when the Photo is
	 * tapped with a single tap.
	 * 
	 * @author Chris Banes
	 */
	public static interface OnPhotoTapListener {

		/**
		 * A callback to receive where the user taps on a photo. You will only
		 * receive a callback if the user taps on the actual photo, tapping on
		 * 'whitespace' will be ignored.
		 * 
		 * @param view
		 *            - View the user tapped.
		 * @param x
		 *            - where the user tapped from the of the Drawable, as
		 *            percentage of the Drawable width.
		 * @param y
		 *            - where the user tapped from the top of the Drawable, as
		 *            percentage of the Drawable height.
		 */
		void onPhotoTap(View view, float x, float y);
	}

	/**
	 * Interface definition for a callback to be invoked when the ImageView is
	 * tapped with a single tap.
	 * 
	 * @author Chris Banes
	 */
	public static interface OnViewTapListener {

		/**
		 * A callback to receive where the user taps on a ImageView. You will
		 * receive a callback if the user taps anywhere on the view, tapping on
		 * 'whitespace' will not be ignored.
		 * 
		 * @param view
		 *            - View the user tapped.
		 * @param x
		 *            - where the user tapped from the left of the View.
		 * @param y
		 *            - where the user tapped from the top of the View.
		 */
		void onViewTap(View view, float x, float y);
	}

	// 缩放动画
	private class AnimatedZoomRunnable implements Runnable {

		// These are 'postScale' values, means they're compounded each iteration
		static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07f;
		static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93f;

		private final float mFocalX, mFocalY;
		private final float mTargetZoom;
		private final float mDeltaScale;

		public AnimatedZoomRunnable(final float currentZoom, final float targetZoom, final float focalX, final float focalY) {
			mTargetZoom = targetZoom;
			mFocalX = focalX;
			mFocalY = focalY;

			if (currentZoom < targetZoom) {
				mDeltaScale = ANIMATION_SCALE_PER_ITERATION_IN;
			} else {
				mDeltaScale = ANIMATION_SCALE_PER_ITERATION_OUT;
			}
		}

		public void run() {
			ImageView imageView = getImageView();
			if (null != imageView) {
				mSuppMatrix.postScale(mDeltaScale, mDeltaScale, mFocalX, mFocalY);
				checkAndDisplayMatrix();

				final float currentScale = getScale();

				if ((mDeltaScale > 1f && currentScale < mTargetZoom) || (mDeltaScale < 1f && mTargetZoom < currentScale)) {
					// We haven't hit our target scale yet, so post ourselves
					// again
					AdapterSDK.postOnAnimation(imageView, this);

				} else {
					// We've scaled past our target zoom, so calculate the
					// necessary scale so we're back at target zoom
					final float delta = mTargetZoom / currentScale;
					mSuppMatrix.postScale(delta, delta, mFocalX, mFocalY);
					checkAndDisplayMatrix();
				}
			}
		}
	}

	// 图片点击的 侦听回调
	@Override
	public final void setOnPhotoTapListener(OnPhotoTapListener listener) {
		mPhotoTapListener = listener;
	}

	// 手势滑动操作
	private class FlingRunnable implements Runnable {

		private final ViewScroller mScroller;
		private int mCurrentX, mCurrentY;

		public FlingRunnable(Context context) {
			mScroller = ViewScroller.getScroller(context);
		}

		public void cancelFling() {
			if (DEBUG) {
				BaseLog.d(LOG_TAG, "Cancel Fling");
			}
			mScroller.forceFinished(true);
		}

		public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
			final RectF rect = getDisplayRect();
			if (null == rect) {
				return;
			}

			final int startX = Math.round(-rect.left);
			final int minX, maxX, minY, maxY;

			if (viewWidth < rect.width()) {
				minX = 0;
				maxX = Math.round(rect.width() - viewWidth);
			} else {
				minX = maxX = startX;
			}

			final int startY = Math.round(-rect.top);
			if (viewHeight < rect.height()) {
				minY = 0;
				maxY = Math.round(rect.height() - viewHeight);
			} else {
				minY = maxY = startY;
			}

			mCurrentX = startX;
			mCurrentY = startY;

			if (DEBUG) {
				BaseLog.d(LOG_TAG, "fling. StartX:" + startX + " StartY:" + startY + " MaxX:" + maxX + " MaxY:" + maxY);
			}

			// If we actually can move, fling the scroller
			if (startX != maxX || startY != maxY) {
				mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
			}
		}

		@Override
		public void run() {
			ImageView imageView = getImageView();
			if (null != imageView && mScroller.computeScrollOffset()) {

				final int newX = mScroller.getCurrX();
				final int newY = mScroller.getCurrY();

				if (DEBUG) {
					BaseLog.d(LOG_TAG, "fling run(). CurrentX:" + mCurrentX + " CurrentY:" + mCurrentY + " NewX:" + newX + " NewY:" + newY);
				}

				mSuppMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
				setImageViewMatrix(getDisplayMatrix());

				mCurrentX = newX;
				mCurrentY = newY;

				// Post On animation
				AdapterSDK.postOnAnimation(imageView, this);
			}
		}
	}

	/**
	 * Clean-up the resources attached to this object. This needs to be called
	 * when the ImageView is no longer used. A good example is from
	 * {@link android.view.View#onDetachedFromWindow()} or from
	 * {@link android.app.Activity#onDestroy()}. This is automatically called if
	 * you are using {@link uk.co.senab.photoview.PhotoView}.
	 */
	@SuppressWarnings("deprecation")
	public final void cleanup() {
		mHand.removeCallbacksAndMessages(null);
		if (null != mImageView) {
			mImageView.get().getViewTreeObserver().removeGlobalOnLayoutListener(this);
		}
		mViewTreeObserver = null;

		// Clear listeners too
		mMatrixChangeListener = null;
		mPhotoTapListener = null;
		mViewTapListener = null;

		// Finally, clear ImageView
		mImageView = null;
	}
}