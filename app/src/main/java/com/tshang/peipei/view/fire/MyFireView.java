package com.tshang.peipei.view.fire;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.network.socket.ThreadPoolService;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

public class MyFireView extends View {

	final String LOG_TAG = MyFireView.class.getSimpleName();
	private FireCancelAnimationListener listener;

	public static final int ID_SOUND_UP = 0;
	public static final int ID_SOUND_BLOW = 1;
	public static final int ID_SOUND_MULTIPLE = 2;
	final static int TIME = 5; // 圈数

	private Vector<Dot> lList = new Vector<Dot>();

	LittleDot[] ld = new LittleDot[200];
	private DotFactory df = null;

	boolean running = true;
	//
	//	Bitmap backGroundBitmap;

	Context mContext;

	// NineOld

	public MyFireView(Activity context, FireCancelAnimationListener listener) {
		super(context);
		this.listener = listener;

		df = new DotFactory();
		ThreadPoolService.getInstance().execute(new MyRunnable());
		mContext = context;
		int height = BasePhone.getScreenHeight(context);
		int width = BasePhone.getScreenWidth(context);
		int bottomHeight = 3 * height / 5;
		Dot dot = null;
		int len = (int) (Math.random() * 5) + 8;
		for (int i = 0; i < len; i++) {
			int rankX = (int) (Math.random() * width);
			int rankY = (int) (Math.random() * (bottomHeight - 200)) + 200;
			dot = df.makeDot(mContext, bottomHeight, rankX, rankY);
			synchronized (lList) {
				lList.add(dot);
			}
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		synchronized (lList) {
			for (int i = 0; i < lList.size(); i++) {
				lList.get(i).myPaint(canvas, lList);
			}
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		try {
			if (listener != null) {
				lList.clear();
				listener.onEndAnimation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Bitmap ReadBitMap(Context context, int resId) {
		return ImageLoader.getInstance().loadImageSync("drawable://" + resId);
		//		BitmapFactory.Options opt = new BitmapFactory.Options();
		//		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		//		opt.inPurgeable = true;
		//		opt.inInputShareable = true;
		//		// 获取资源图片
		//		InputStream is = context.getResources().openRawResource(resId);
		//		return BitmapFactory.decodeStream(is, null, opt);
	}

	public Bitmap resizeImage(Bitmap mBitmap, int w, int h) {
		Bitmap BitmapOrg = mBitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap tmp = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
		return tmp;
	}

	class MyRunnable implements Runnable {
		// 新建一个进程类来处理重画
		// 用于控制烟火在空中滞留的时间
		int times = 0;

		public void run() {
			Dot dot = null;
			while (running) {

				try {
					Thread.sleep(100);
				} catch (Exception e) {
					System.out.println(e);
				}

				synchronized (lList) {
					// 防止画面的烟花个数多于50个
					while (lList.size() > 50) {
						//						System.out.println("当前数目超过50");
						for (int i = 0; i < 10; i++) {
							lList.remove(i);
						}
					}

				}

				for (int i = 0; i < lList.size(); i++) {
					dot = (Dot) lList.get(i);
					if (dot.state == 1 && !dot.whetherBlast()) {
						dot.rise();
					}
					// 如果是whetherBlast()返回的是true，那么就把该dot的state设置为2
					else if (dot.state == 1 && dot.state != 2) {
						dot.state = 2;
						// soundPlay.play(ID_SOUND_BLOW, 0);
					} else if (dot.state == 3) {

					}
					// 规定，每个爆炸点最多是TIME圈，超过就会消失
					if (dot.circle >= TIME) {
						// 在空中滞留一秒才消失
						if (times >= 10) {
							dot.state = 4;
							times = 0;
						} else {
							times++;
						}
						// dot.state = 4;
					}
				}
				if (listener != null && lList.size() == 0) {
					listener.onEndAnimation();
				}
			}
		}
	}

	public interface FireCancelAnimationListener {
		public void onEndAnimation();

	}
}
