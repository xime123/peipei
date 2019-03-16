package com.tshang.peipei.activity.mine;

import java.lang.reflect.Field;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.vender.common.util.ImageUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.assist.ImageSize;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;
import com.tshang.peipei.view.ClipView;

public class ClipViewActivity extends BaseActivity implements OnTouchListener {
	public static final String GET_IMAGE_PATH = "get_image_path";
	private ImageView screenshot_imageView;
	private ClipView clipview;
	private int width;// 屏幕宽度
	private int height;// 屏幕高度
	private Rect rectIV;
	private int statusBarHeight = 0;
	private int titleBarHeight = 0;
	private int n = 0;// angleInt % 4 的值，用于计算旋转后图片区域

	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	DisplayMetrics dm;

	float minScaleR = 1.0f;// 最小缩放比例
	static final float MAX_SCALE = 5f;// 最大缩放比例

	// We can be in one of these 3 states
	static final int NONE = 0;// 初始状态
	static final int DRAG = 1;// 拖动
	static final int ZOOM = 2;// 缩放
	private static final String TAG = "11";
	int mode = NONE;

	// Remember some things for zooming
	PointF prev = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private int bWidth = 480;
	private int Bheight = 480;

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String path = bundle.getString(GET_IMAGE_PATH);
			if (!TextUtils.isEmpty(path)) {
				BaseUtils.showDialog(this, "正在加载图片");

				DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
						.considerExifParams(true).cacheInMemory(false).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565).build();

				ImageSize imageSize = new ImageSize(BasePhone.getScreenWidth(this), BasePhone.getScreenHeight(this));
				
				imageLoader.displayImage("file://"+ path, screenshot_imageView,options,new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						BaseUtils.cancelDialog();
						if (loadedImage != null) {
							bWidth = loadedImage.getWidth();
							Bheight = loadedImage.getHeight();

							if (bWidth < 425 || Bheight < 425) {//小图不让上传
								BaseUtils.showTost(ClipViewActivity.this, "图片太小，请重新上传");
								ClipViewActivity.this.finish();
							}
//							screenshot_imageView.setImageBitmap(loadedImage);
							rectIV = screenshot_imageView.getDrawable().getBounds();

							getStatusBarHeight();
							minZoom(bWidth, Bheight);
							center();
							screenshot_imageView.setImageMatrix(matrix);
						}
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						
					}
				});

//				imageLoader.loadImage("file://" + path, imageSize, options, new ImageLoadingListener() {
//
//					@Override
//					public void onLoadingStarted(String imageUri, View view) {
//
//					}
//
//					@Override
//					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//					}
//
//					@Override
//					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//						BaseUtils.cancelDialog();
//						if (loadedImage != null) {
//							bWidth = loadedImage.getWidth();
//							Bheight = loadedImage.getHeight();
//
//							if (bWidth < 425 || Bheight < 425) {//小图不让上传
//								BaseUtils.showTost(ClipViewActivity.this, "图片太小，请重新上传");
//								ClipViewActivity.this.finish();
//							}
//							screenshot_imageView.setImageBitmap(loadedImage);
//							rectIV = screenshot_imageView.getDrawable().getBounds();
//
//							getStatusBarHeight();
//							minZoom(bWidth, Bheight);
//							center();
//							screenshot_imageView.setImageMatrix(matrix);
//						}
//					}
//
//					@Override
//					public void onLoadingCancelled(String imageUri, View view) {
//
//					}
//				});

			}

		}
	}

	/**
	 * 获取状态栏高度 下面方法在oncreate中调用时获得的状态栏高度为0，不能用 Rect frame = new Rect();
	 * getWindow().getDecorView().getWindowVisibleDisplayFrame(frame); int
	 * statusBarHeight = frame.top;
	 */
	private void getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler handle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x10:
				BaseUtils.cancelDialog();
				byte[] bitmapByte = (byte[]) msg.obj;
				Intent intent = new Intent(ClipViewActivity.this, MineSettingUserInfoActivity.class);
				intent.putExtra(MineSettingUserInfoActivity.RESULT_BITMAP_DATA, bitmapByte);
				setResult(RESULT_OK, intent);
				ClipViewActivity.this.finish();
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.clip_cancel_btn:
			finish();
			break;
		case R.id.clip_ok_btn:
			BaseUtils.showDialog(this, "正在截取图片");
			new Thread(new Runnable() {

				@Override
				public void run() {
					Bitmap finalBitmap = getBitmap();
					int bitmapWidth = finalBitmap.getWidth();
					float scale = ((float) 425.0 / bitmapWidth);
					finalBitmap = ImageUtils.scaleImage(finalBitmap, scale, scale);
					byte[] bitmapByte = ImageUtils.bitmapToByte(finalBitmap);
					handle.sendMessage(handle.obtainMessage(0x10, bitmapByte));
					if (finalBitmap != null && !finalBitmap.isRecycled()) {
						finalBitmap.recycle();
					}
				}
			}).start();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		BaseUtils.cancelDialog();
		super.onDestroy();
	}

	/**
	 * 下面的触屏方法摘自网上经典的触屏方法 只在判断是否在图片区域内做了少量修改
	 * 
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		try {

			ImageView view = (ImageView) v;
			// Handle touch events here...
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 主点按下
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				// 设置初始点位置
				prev.set(event.getX(), event.getY());
				if (isOnCP(event.getX(), event.getY())) {
					// 触点在图片区域内
					BaseLog.d(TAG, "mode=DRAG");
					mode = DRAG;
				} else {
					mode = NONE;
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				// 判断触点是否在图片区域内
				boolean isonpic = isOnCP(event.getX(), event.getY());
				BaseLog.d(TAG, "oldDist=" + oldDist);
				// 如果连续两点距离大于10，则判定为多点模式
				if (oldDist > 10f && isonpic) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
					BaseLog.d(TAG, "mode=ZOOM");
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				BaseLog.d(TAG, "mode=NONE");
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					// ...
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - prev.x, event.getY() - prev.y);
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					BaseLog.d(TAG, "newDist=" + newDist);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}
			view.setImageMatrix(matrix);
			CheckView();
			return true; // indicate event was handled
		} catch (Exception e) {
			e.printStackTrace();
//			MobclickAgent.reportError(BAApplication.getInstance().getApplicationContext(), "截图报错");
			return false;
		}
	}

	/**
	 * 两点的距离 Determine the space between the first two fingers
	 */
	private float spacing(MotionEvent event) {
		float x = 0;
		float y = 0;
		try {
			x = event.getX(0) - event.getX(1);
			y = event.getY(0) - event.getY(1);
		} catch (IllegalArgumentException e) {
//			MobclickAgent.reportError(BAApplication.getInstance().getApplicationContext(), "触控点越界");
			e.printStackTrace();
		}
		return (float) Math.sqrt(x * x + y * y);

	}

	/**
	 * 两点的中点 Calculate the mid point of the first two fingers
	 * */
	private void midPoint(PointF point, MotionEvent event) {
		try {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		} catch (IllegalArgumentException e) {
//			MobclickAgent.reportError(BAApplication.getInstance().getApplicationContext(), "触控点越界");
			e.printStackTrace();
		}
	}

	/**
	 * 判断点所在的控制点
	 * 
	 * @param evX
	 * @param evY
	 * @return
	 */
	private boolean isOnCP(float evx, float evy) {
		float p[] = new float[9];
		matrix.getValues(p);
		float scale = Math.max(Math.abs(p[0]), Math.abs(p[1]));
		// 由于本人很久不用数学，矩阵的计算已经忘得差不多了，所以图片区域的计算只能按最笨的办法，
		RectF rectf = null;
		switch (n) {
		case 0:
			rectf = new RectF(p[2], p[5], (p[2] + rectIV.width() * scale), (p[5] + rectIV.height() * scale));
			break;
		case 1:
			rectf = new RectF(p[2], p[5] - rectIV.width() * scale, p[2] + rectIV.height() * scale, p[5]);
			break;
		case 2:
			rectf = new RectF(p[2] - rectIV.width() * scale, p[5] - rectIV.height() * scale, p[2], p[5]);
			break;
		case 3:
			rectf = new RectF(p[2] - rectIV.height() * scale, p[5], p[2], p[5] + rectIV.width() * scale);
			break;
		}
		if (rectf != null && rectf.contains(evx, evy)) {
			return true;
		}
		return false;
	}

	/**
	 * 最小缩放比例，最大为100%
	 */
	private void minZoom(int bitmapWidth, int bitmpHeight) {
		minScaleR = Math.min((float) dm.widthPixels / (float) bitmapWidth, (float) dm.widthPixels / (float) bitmpHeight) * dm.density;
		if (minScaleR < 1.0) {
			float scale = Math.max((float) dm.widthPixels / (float) bitmapWidth, (float) dm.widthPixels / (float) bitmpHeight);
			matrix.postScale(scale, scale);
			minScaleR = minScaleR + 0.1f;//0.1为修正
		} else {
			minScaleR = 1.0f;
		}
	}

	/**
	 * 限制最大最小缩放比例
	 */
	private void CheckView() {
		float p[] = new float[9];
		matrix.getValues(p);
		float scale = Math.max(Math.abs(p[0]), Math.abs(p[1]));
		if (mode == ZOOM) {
			if (scale < minScaleR) {
				matrix.setScale(minScaleR, minScaleR);
				center();
			}
			if (scale > MAX_SCALE) {
				matrix.set(savedMatrix);
			}
		}
	}

	private void center() {
		center(true, true);
	}

	/**
	 * 横向、纵向居中
	 */
	protected void center(boolean horizontal, boolean vertical) {

		Matrix m = new Matrix();
		m.set(matrix);

		RectF rect = new RectF(0, 0, bWidth, Bheight);
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
			int screenHeight = dm.heightPixels;
			if (height < screenHeight) {
				deltaY = (screenHeight - height - statusBarHeight) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = screenshot_imageView.getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int screenWidth = dm.widthPixels;
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right > screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
		if (n % 4 != 0) {
			matrix.postRotate(-90 * (n % 4), screenshot_imageView.getWidth() / 2, screenshot_imageView.getHeight() / 2);
		}
	}

	/* 获取矩形区域内的截图 */
	private Bitmap getBitmap() {
		getBarHeight();
		Bitmap screenShoot = takeScreenShot();

		//TODO x,y >0 判断
		Bitmap finalBitmap = Bitmap.createBitmap(screenShoot, ClipView.SX + 1, (height - width + statusBarHeight + titleBarHeight) / 2 + 1, width
				- ClipView.SX - ClipView.EX - 1, width - ClipView.SX - ClipView.EX - 1);
		return finalBitmap;
	}

	private void getBarHeight() {
		// 获取状态栏高度
		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

		statusBarHeight = frame.top;

		int contenttop = this.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		// statusBarHeight是上面所求的状态栏的高度
		titleBarHeight = contenttop - statusBarHeight;

		BaseLog.v(TAG, "statusBarHeight = " + statusBarHeight + ", titleBarHeight = " + titleBarHeight);
	}

	// 获取Activity的截屏
	private Bitmap takeScreenShot() {
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		return view.getDrawingCache();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRecourse() {
		screenshot_imageView = (ImageView) findViewById(R.id.clip_imageView);
		findViewById(R.id.clip_cancel_btn).setOnClickListener(this);
		findViewById(R.id.clip_ok_btn).setOnClickListener(this);
		clipview = (ClipView) findViewById(R.id.clipview);

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		screenshot_imageView.setImageMatrix(matrix);
		screenshot_imageView.setOnTouchListener(this);
	}

	@Override
	protected int initView() {
		return R.layout.activity_clip_image;
	}

}
