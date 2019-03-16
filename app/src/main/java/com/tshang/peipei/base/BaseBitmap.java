package com.tshang.peipei.base;

import java.io.ByteArrayOutputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;

import com.tshang.peipei.vender.common.util.ImageUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class BaseBitmap {

	/** 水平方向模糊度 */
	private static float hRadius = 12;
	/** 竖直方向模糊度 */
	private static float vRadius = 12;
	/** 模糊迭代度 */
	private static int iterations = 15;

	/**
	 * Get the size in bytes of a bitmap.
	 * 
	 * @param bitmap
	 * @return size in bytes
	 */
	public static int getBitmapSize(Bitmap bitmap) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
				if (bitmap != null) {
					return bitmap.getByteCount();
				}
				return 0;
			}
			// Pre HC-MR1
			return bitmap.getRowBytes() * bitmap.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static Bitmap bytes2Bimap(byte[] b) {
		try {
			if (b.length != 0) {
				return BitmapFactory.decodeByteArray(b, 0, b.length);
			} else {
				return null;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		if (bm == null) {
			return null;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
			bm.recycle();
			return baos.toByteArray();
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	/**
	 * 设置圆角
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
		return toRoundCorner(bitmap, pixels, pixels);
	}

	/**
	 * 设置圆角
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCornerToWH(Bitmap bitmap, float pixels) {
		float wscale = bitmap.getHeight() / pixels;
		float wpixels = bitmap.getWidth() / wscale;

		return toRoundCorner(bitmap, wpixels, wpixels);
	}

	/**
	 * 设置圆角
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixelsX, float pixelsY) {
		if (bitmap == null) {
			return null;
		}
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);
		canvas.drawRoundRect(rectF, pixelsX, pixelsY, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 压缩图片大小
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		Bitmap newbmp = null;
		try {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = ((float) width / w);
			float scaleHeight = ((float) height / h);
			matrix.postScale(scaleWidth, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newbmp;
	}

	public static Bitmap setBitmapSizeByHeight(int width, int h, Bitmap bmp, Context context) {

		float hw = h * 100 / bmp.getHeight();
		int w = (int) (bmp.getWidth() * hw / 100);
		if (w < BaseUtils.dip2px(context, 100)) {
			w = BaseUtils.dip2px(context, 100);
		}

		bmp = BaseBitmap.zoomBitmap(bmp, w > (width - BaseUtils.dip2px(context, 140)) ? (width - BaseUtils.dip2px(context, 140)) : w, h);

		return bmp;
	}

	public static Bitmap setBitmapSizeByWight(int height, int w, Bitmap bmp, Context context) {

		float hw = w * 100 / bmp.getWidth();
		int h = (int) (bmp.getHeight() * hw / 100);

		if (h < BaseUtils.dip2px(context, 100)) {
			h = BaseUtils.dip2px(context, 100);
		}

		bmp = BaseBitmap.zoomBitmap(bmp, w, h > (height - BaseUtils.dip2px(context, 380)) ? (height - BaseUtils.dip2px(context, 380)) : h);

		return bmp;
	}

	/**
	 * 压缩图片方法，
	 * 
	 * @param image
	 * @param width
	 *            最大宽度
	 * @param size
	 *            压缩大小
	 * @return
	 */
	public static byte[] compBitmap2Byte(Bitmap image, int width, int size) {
		if (image.getWidth() > width) // 先压分辨率
		{
			int dstWidth = width;
			int dstHeight = image.getHeight() * (dstWidth * 100 / image.getWidth()) / 100;

			image = Bitmap.createScaledBitmap(image, dstWidth, dstHeight, false);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
		ImageUtils.recycleBitmap(image);
		return baos.toByteArray();
	}

	/**
	 * 压缩图片到宽高100的图片；
	 * 微信分享有图片大小要求，不能超过32k
	 * @param bitMap
	 * @return
	 */
	public static Bitmap createBitmapThumbnail(Bitmap bitMap) {
		if (bitMap == null) {
			return null;
		}
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		// 设置想要的大小
		int newWidth = 100;
		int newHeight = 100;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
		return newBitMap;
	}

	/**
	 * 获取图片的byte数组，并释放图片
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 70, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 高斯算法,图片模糊处理
	 *
	 * @param bmp
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap BoxBlurFilter(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < iterations; i++) {
			blur(inPixels, outPixels, width, height, hRadius);
			blur(outPixels, inPixels, height, width, vRadius);
		}
		blurFractional(inPixels, outPixels, width, height, hRadius);
		blurFractional(outPixels, inPixels, height, width, vRadius);
		bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static void blur(int[] in, int[] out, int width, int height, float radius) {
		int widthMinus1 = width - 1;
		int r = (int) radius;
		int tableSize = 2 * r + 1;
		int divide[] = new int[256 * tableSize];

		for (int i = 0; i < 256 * tableSize; i++)
			divide[i] = i / tableSize;

		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;
			int ta = 0, tr = 0, tg = 0, tb = 0;

			for (int i = -r; i <= r; i++) {
				int rgb = in[inIndex + clamp(i, 0, width - 1)];
				ta += (rgb >> 24) & 0xff;
				tr += (rgb >> 16) & 0xff;
				tg += (rgb >> 8) & 0xff;
				tb += rgb & 0xff;
			}

			for (int x = 0; x < width; x++) {
				out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

				int i1 = x + r + 1;
				if (i1 > widthMinus1)
					i1 = widthMinus1;
				int i2 = x - r;
				if (i2 < 0)
					i2 = 0;
				int rgb1 = in[inIndex + i1];
				int rgb2 = in[inIndex + i2];

				ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
				tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
				tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
				tb += (rgb1 & 0xff) - (rgb2 & 0xff);
				outIndex += height;
			}
			inIndex += width;
		}
	}

	public static void blurFractional(int[] in, int[] out, int width, int height, float radius) {
		radius -= (int) radius;
		float f = 1.0f / (1 + 2 * radius);
		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;

			out[outIndex] = in[0];
			outIndex += height;
			for (int x = 1; x < width - 1; x++) {
				int i = inIndex + x;
				int rgb1 = in[i - 1];
				int rgb2 = in[i];
				int rgb3 = in[i + 1];

				int a1 = (rgb1 >> 24) & 0xff;
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;
				int a2 = (rgb2 >> 24) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;
				int a3 = (rgb3 >> 24) & 0xff;
				int r3 = (rgb3 >> 16) & 0xff;
				int g3 = (rgb3 >> 8) & 0xff;
				int b3 = rgb3 & 0xff;
				a1 = a2 + (int) ((a1 + a3) * radius);
				r1 = r2 + (int) ((r1 + r3) * radius);
				g1 = g2 + (int) ((g1 + g3) * radius);
				b1 = b2 + (int) ((b1 + b3) * radius);
				a1 *= f;
				r1 *= f;
				g1 *= f;
				b1 *= f;
				out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
				outIndex += height;
			}
			try {
				out[outIndex] = in[width - 1];
				inIndex += width;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}

	public static Bitmap createBitmap(Bitmap bitMap, int w, int h) {
		if (bitMap == null) {
			return null;
		}
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		// 设置想要的大小
		int newWidth = w;
		int newHeight = h;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
		return newBitMap;
	}
}
