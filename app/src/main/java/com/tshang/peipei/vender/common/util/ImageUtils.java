package com.tshang.peipei.vender.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDownloadHeadPic;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlDownloadPic;
import com.tshang.peipei.protocol.asn.gogirl.RspDownloadHeadPic;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlDownloadPic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;

/**
 * ImageUtils
 * <ul>
 * convert between Bitmap, byte array, Drawable
 * <li>{@link #bitmapToByte(Bitmap)}</li>
 * <li>{@link #bitmapToDrawable(Bitmap)}</li>
 * <li>{@link #byteToBitmap(byte[])}</li>
 * <li>{@link #byteToDrawable(byte[])}</li>
 * <li>{@link #drawableToBitmap(Drawable)}</li>
 * <li>{@link #drawableToByte(Drawable)}</li>
 * </ul>
 * <ul>
 * get image
 * <li>{@link #getInputStreamFromUrl(String, int)}</li>
 * <li>{@link #getBitmapFromUrl(String, int)}</li>
 * <li>{@link #getDrawableFromUrl(String, int)}</li>
 * </ul>
 * <ul>
 * scale image
 * <li>{@link #scaleImageTo(Bitmap, int, int)}</li>
 * <li>{@link #scaleImage(Bitmap, float, float)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-6-27
 */
public class ImageUtils {

	/**
	 * convert Bitmap to byte array
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap b) {
		if (b == null) {
			return null;
		}

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 60, o);
		return o.toByteArray();
	}

	/**
	 * convert Bitmap to byte array
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap b, int cale) {
		if (b == null) {
			return null;
		}

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, cale, o);
		return o.toByteArray();
	}

	/**
	 * convert byte array to Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] b) {
		return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	/**
	 * convert Drawable to Bitmap
	 * 
	 * @param d
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable d) {
		return d == null ? null : ((BitmapDrawable) d).getBitmap();
	}

	/**
	 * convert Bitmap to Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap b) {
		return b == null ? null : new BitmapDrawable(b);
	}

	/**
	 * convert Drawable to byte array
	 * 
	 * @param d
	 * @return
	 */
	public static byte[] drawableToByte(Drawable d) {
		return bitmapToByte(drawableToBitmap(d));
	}

	/**
	 * convert byte array to Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] b) {
		return bitmapToDrawable(byteToBitmap(b));
	}

	/**
	 * get input stream from network by imageurl, you need to close inputStream yourself
	 * 
	 * @param imageUrl
	 * @param readTimeOutMillis
	 * @return
	 * @see ImageUtils#getInputStreamFromUrl(String, int, boolean)
	 */
	public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeOutMillis) {
		return getInputStreamFromUrl(imageUrl, readTimeOutMillis, null);
	}

	/**
	 * get input stream from network by imageurl, you need to close inputStream yourself
	 * 
	 * @param imageUrl
	 * @param readTimeOutMillis read time out, if less than 0, not set, in mills
	 * @param requestProperties http request properties
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeOutMillis, Map<String, String> requestProperties) {
		InputStream returnInpustream = null;
		Socket socket = new Socket();
		String host = BAConstants.PEIPEI_SERVER_HOST_PRO;
		if (BAConstants.IS_TEST) {
			host = BAConstants.PEIPEI_SERVER_HOST_TEST;
		}
		InetSocketAddress socektAddress = new InetSocketAddress(host, BAConstants.PEIPEI_SERVER_PORT);
		try {
			socket.connect(socektAddress, BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			if (readTimeOutMillis > 0) {
				socket.setSoTimeout(readTimeOutMillis);
			} else {
				socket.setSoTimeout(BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
			}

		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		YdmxMsg ydmxMsg = AsnBase.createYdmx("".getBytes(), BAApplication.app_version_code);
		ReqGoGirlDownloadPic req = new ReqGoGirlDownloadPic();
		ReqDownloadHeadPic reqDownloadHeadPic = new ReqDownloadHeadPic();
		boolean flag = true;
		if (imageUrl.contains("@")) {
			String[] strs = imageUrl.split("@");
			if (strs.length == 4) {
				flag = true;
				req.key = strs[0].getBytes();
				req.width = BigInteger.valueOf(Integer.valueOf(strs[2]));
				req.height = BigInteger.valueOf(Integer.valueOf(strs[3]));
				req.sincemodtime = BigInteger.valueOf(0);
			} else if (strs.length == 5) {
				flag = false;
				reqDownloadHeadPic.uid = BigInteger.valueOf(Integer.valueOf(strs[0]));
				reqDownloadHeadPic.width = BigInteger.valueOf(Integer.valueOf(strs[2]));
				reqDownloadHeadPic.height = BigInteger.valueOf(Integer.valueOf(strs[3]));
				reqDownloadHeadPic.sincemodtime = BigInteger.valueOf(Integer.valueOf(0));
			}
		}
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		if (flag) {
			goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLDOWNLOADPIC_CID;
			goGirlPkt.reqgogirldownloadpic = req;
		} else {
			goGirlPkt.choiceId = GoGirlPkt.REQDOWNLOADHEADPIC_CID;//ReqDownloadHeadPic
			goGirlPkt.reqdownloadheadpic = reqDownloadHeadPic;
		}
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = AsnBase.encode(ydmxMsg);

		OutputStream os = null;
		InputStream is = null;
		try {
			if (socket.isOutputShutdown() || socket.isInputShutdown()) {
				return null;
			}
			os = socket.getOutputStream();
			is = socket.getInputStream();
			// 发送请求msg
			os.write(msg);
			os.flush();
			int len = 0;
			int ret = -1;
			byte[] b = new WeakReference<byte[]>(new byte[1024 * 8]).get();
			byte[] bb = new WeakReference<byte[]>(new byte[0]).get();
			;
			while ((len = is.read(b, 0, b.length)) != -1) {
				//拷贝读取到的字节数组就好了，不需要好重新开去读取流
				bb = (byte[]) arrayGrow(bb, len);
				System.arraycopy(b, 0, bb, (bb.length - len), len);
				ret = AsnProtocolTools.is_pkg_complete(bb);
				if (ret > 0) {//收到完整的包
					break;
				} else if (ret == 0) {//部分正确，不完整，继续收
					continue;
				} else {//出错了
					if (null != os) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						os = null;
					}
					closeInputStream(is);
					if (null != socket) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					throw new IOException();
				}
			}

			if (len < 0 && ret <= 0) {
				throw new IOException();
			}

			YdmxMsg resultPacket = (YdmxMsg) AsnBase.decode(bb);
			bb = null;
			GoGirlPkt pkt = resultPacket.body.gogirlpkt;
			byte[] returnData = null;
			if (flag) {
				RspGoGirlDownloadPic rsp = pkt.rspgogirldownloadpic;
				returnData = rsp.picdata;
			} else {
				RspDownloadHeadPic rsp = pkt.rspdownloadheadpic;
				returnData = rsp.pic;
			}
			returnInpustream = byte2InputStream(returnData);
		} catch (OutOfMemoryError error) {
			System.gc();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
			closeInputStream(is);
			if (null != socket) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnInpustream;
	}

	private static Object arrayGrow(Object obj, int addLength) {
		Class c = obj.getClass();
		if (!c.isArray()) {
			return null;
		}

		Class componentType = c.getComponentType();
		int length = Array.getLength(obj);
		int newLength = length + addLength;
		Object newArray = Array.newInstance(componentType, newLength);
		System.arraycopy(obj, 0, newArray, 0, length);

		return newArray;
	}

	/** 
	* @方法功能 byte 转为 InputStream 
	* @param 字节数组 
	* @return InputStream 
	* @throws Exception 
	*/
	public static InputStream byte2InputStream(byte[] b) throws Exception {
		if (b == null) {
			return null;
		}
		InputStream is = new ByteArrayInputStream(b);
		return is;
	}

	/**
	 * get drawable by imageUrl
	 * 
	 * @param imageUrl
	 * @param readTimeOutMillis
	 * @return
	 * @see ImageUtils#getDrawableFromUrl(String, int, boolean)
	 */
	public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis) {
		return getDrawableFromUrl(imageUrl, readTimeOutMillis, null);
	}

	/**
	 * get drawable by imageUrl
	 * 
	 * @param imageUrl
	 * @param readTimeOutMillis read time out, if less than 0, not set, in mills
	 * @param requestProperties http request properties
	 * @return
	 */
	public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis, Map<String, String> requestProperties) {
		InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis, requestProperties);
		Drawable d = Drawable.createFromStream(stream, "src");
		closeInputStream(stream);
		return d;
	}

	/**
	 * get Bitmap by imageUrl
	 * 
	 * @param imageUrl
	 * @param readTimeOut
	 * @return
	 * @see ImageUtils#getBitmapFromUrl(String, int, boolean)
	 */
	public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut) {
		return getBitmapFromUrl(imageUrl, readTimeOut, null);
	}

	/**
	 * get Bitmap by imageUrl
	 * 
	 * @param imageUrl
	 * @param requestProperties http request properties
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut, Map<String, String> requestProperties) {
		InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut, requestProperties);
		Bitmap b = BitmapFactory.decodeStream(stream);
		closeInputStream(stream);
		return b;
	}

	/**
	 * scale image
	 * 
	 * @param org
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
		return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
	}

	/**
	 * scale image
	 * 
	 * @param org
	 * @param scaleWidth sacle of width
	 * @param scaleHeight scale of height
	 * @return
	 */
	public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
		if (org == null) {
			return null;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
	}

	/**
	 * close inputStream
	 * 
	 * @param s
	 */
	private static void closeInputStream(InputStream s) {
		if (s == null) {
			return;
		}

		try {
			s.close();
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		}
	}

	/**
	 * set image src
	 * 
	 * @param imageView
	 * @param imagePath
	 */
	public static void setImageSrc(ImageView imageView, String imagePath) {
		imageView.setImageBitmap(getBitmapFromSdCardPath(imagePath));
	}

	public static Bitmap getBitmapFromSdCardPath(String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = getImageScale(imagePath);
		return BitmapFactory.decodeFile(imagePath, option);
	}

	private static int IMAGE_MAX_WIDTH = 480;
	private static int IMAGE_MAX_HEIGHT = 960;

	/**
	 * scale image to fixed height and weight
	 * 
	 * @param imagePath
	 * @return
	 */
	public static int getImageScale(String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		// set inJustDecodeBounds to true, allowing the caller to query the bitmap info without having to allocate the
		// memory for its pixels.
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, option);

		int scale = 1;
		while (option.outWidth / scale >= IMAGE_MAX_WIDTH || option.outHeight / scale >= IMAGE_MAX_HEIGHT) {
			scale *= 2;
		}
		return scale;
	}

	public static byte[] imagePathToByte(String path) {
		if (!TextUtils.isEmpty(path)) {
			Bitmap bitmap = getBitmapFromSdCardPath(path);
			byte[] bitmapBytes = bitmapToByte(bitmap);
			ImageUtils.recycleBitmap(bitmap);
			return bitmapBytes;
		}
		return null;
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param bitmap 释放Bitmap对象
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}

}
