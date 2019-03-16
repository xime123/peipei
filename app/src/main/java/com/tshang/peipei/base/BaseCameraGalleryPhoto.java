package com.tshang.peipei.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import com.tshang.peipei.R;

/**
 * @Title: BaseCameraGalleryPhoto.java 
 *
 * @Description: 拍照,从图片中选择图片
 *
 * @author vactor
 *
 * @date 2014-4-23 下午8:31:50 
 *
 * @version V1.0   
 */
public class BaseCameraGalleryPhoto {

	// 拍照的照片存储位置
	//protected static File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/" + BAConstants.PEIPEI_FILE + "/HEAD");
	protected static File mTempCameraFile;

	/**
	 * 从图库中选取照片
	 * @author vactor
	 *
	 * @param context
	 * @param fileName
	 * @param type
	 */
	public static void clickIvChatPhoto(File photoDir, FragmentActivity context, String fileName, int type) {

		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		mTempCameraFile = getPicUriByGalley(photoDir, fileName);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempCameraFile));
		context.startActivityForResult(intent, type);
	}

	/**
	 * 新建一个新的图片路径 ,旧的文件会删除
	 *
	 * @param photoFile
	 * @param tempGallayFile
	 * @param galleryFileName
	 * @return
	 */
	public static File getPicUriByGalley(File photoFile, String galleryFileName) {
		if (!photoFile.exists()) {
			photoFile.mkdirs();
		}
		File tempGallayFile = new File(photoFile, galleryFileName);
		try {
			if (tempGallayFile.exists()) {
				tempGallayFile.delete();
				tempGallayFile.createNewFile();
			} else {
				tempGallayFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempGallayFile;
	}

	/**
	 * 拍照获取图片
	 * @author vactor
	 *
	 * @param context
	 * @param fileName 图片另存为的文件名
	 * @param type 
	 */
	public static void clickIvChatCamera(File photoDir, FragmentActivity context, String fileName, int type) {

		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			final Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			mTempCameraFile = BaseCameraGalleryPhoto.getPicUriByCamera(photoDir, fileName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempCameraFile));
			context.startActivityForResult(intent, type);
		} else {
			BaseUtils.showTost(context, R.string.nosdcard);
		}
	}

	/**
	 * 从SDC选择一张图片,并压缩到指定宽度
	 * @author vactor
	 *
	 * @param photoFile  图片路径
	 * @param cameraFileName 图片名字
	 * @param width 指定宽度
	 * @param context
	 * @return
	 */
	public static byte[] getPhotoByCamera(File photoFile, String cameraFileName, Context context) {
		byte[] mBitmap;
		File tempCameraFile = getPicUriByCamera(photoFile, cameraFileName);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inSampleSize = 3;

		Bitmap map = BitmapFactory.decodeFile(tempCameraFile.getAbsolutePath(), newOpts);
		if (map == null) {
			BaseUtils.showTost(context, R.string.msg_rechoice_gallery);
			return null;
		} else {
			if (map.getHeight() <= 0 || map.getWidth() <= 0) {
				BaseUtils.showTost(context, R.string.msg_rechoice_gallery);
				return null;
			}
		}

		//不压缩处理
		mBitmap = BaseBitmap.bitmap2Bytes(map);//compBitmap2Byte(map, width, BAConstants.IMAGE_COMP_SIZE);
		return mBitmap;
	}

	/**
	 * 解析 构造 一个图片 byte[]
	 * @author vactor
	 *
	 * @param uri
	 * @param data
	 * @param tempGallayFile
	 * @param width
	 * @param context
	 * @return
	 */
	public static byte[] decodeUriAsBitmap(Uri uri, Intent data, File tempGallayFile, Context context) {
		Bitmap bitmap = null;
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inSampleSize = 3;
		if (uri == null) {
			if (tempGallayFile == null) {
				tempGallayFile = mTempCameraFile;
			}

			try {
				bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.fromFile(tempGallayFile)));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				try {
					bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.fromFile(tempGallayFile)), null, newOpts);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					return null;
				}
			}
		} else {
			try {
				bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				try {
					bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, newOpts);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					return null;
				}
			}
		}

		if (bitmap == null && data != null) {
			uri = data.getData();
			ContentResolver cr = context.getContentResolver();
			if (uri != null) {
				try {
					bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					try {
						bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, newOpts);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
						return null;
					} catch (OutOfMemoryError e2) {
						return null;
					}
				}
			}
		}
		if (null != bitmap) {
			//不压缩处理
			return BaseBitmap.bitmap2Bytes(bitmap);//compBitmap2Byte(bitmap, width, BAConstants.IMAGE_COMP_SIZE);
		} else {
			return null;
		}
	}

	/**
	 * 以文件路径 ,文件名,拿 到一个文件,如果文件已经存在,则使用旧的
	 *
	 * @param photoFile
	 * @param tempCameraFile
	 * @param cameraFileName
	 * @return
	 */
	public static File getPicUriByCamera(File photoFile, String cameraFileName) {
		if (!photoFile.exists()) {
			photoFile.mkdirs();
		}
		File tempCameraFile = new File(photoFile, cameraFileName);

		if (!tempCameraFile.exists()) {
			try {
				tempCameraFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return tempCameraFile;
	}

	//照相
	public static void clickBtnGetPhotoByTakePhoto(File photoDir, FragmentActivity context, int type) {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			final Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, getPicUriByCamera(photoDir, context));
			context.startActivityForResult(intent, type);
		} else {
			BaseUtils.showTost(context, R.string.nosdcard);
		}
	}

	//相册
	public static void clickBtnGetPicFromGallery(File photoDir, FragmentActivity context, int type) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 425);
		intent.putExtra("outputY", 425);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getPicUriByGalley(photoDir, context));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		context.startActivityForResult(intent, type);
	}

	/**
	 *  拍照
	 * @author vactor
	 *
	 * @return
	 */

	public static Uri getPicUriByCamera(File photoDir, FragmentActivity context) {
		if (!photoDir.exists()) {
			photoDir.mkdirs();
		}
		mTempCameraFile = new File(photoDir, System.currentTimeMillis() + "temp.jpg");

		if (!mTempCameraFile.exists()) {
			try {
				mTempCameraFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return Uri.fromFile(mTempCameraFile);
	}

	public static Uri getPicUriByGalley(File photoDir, FragmentActivity context) {
		if (!photoDir.exists()) {
			photoDir.mkdirs();
		}
		mTempCameraFile = new File(photoDir, System.currentTimeMillis() + "temp.jpg");
		try {
			if (mTempCameraFile.exists()) {
				mTempCameraFile.delete();
				mTempCameraFile.createNewFile();
			} else {
				mTempCameraFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uri.fromFile(mTempCameraFile);
	}

	public static Bitmap decodeUriAsBitmap(FragmentActivity context, Uri uri) {
		Bitmap bitmap = null;
		try {
			if (uri == null) {
				if (mTempCameraFile == null) {
					return null;
				}

				bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.fromFile(mTempCameraFile)));
			} else {
				bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
		return new WeakReference<Bitmap>(bitmap).get();
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param context 上下文
	 * @param isPhoto true浏览相册，false 拍照
	 * @param requectCode 请求码
	 * @param popupwind 有悬浮框的就传，没有就不传，用来隐藏悬浮框
	 */
	public static void intentSelectImage(Activity context, boolean isPhoto, int requectCode) {
		if (!BaseUtils.hasSdcard()) {
			BaseUtils.showTost(context, R.string.str_sdcard_not_exist);
			return;
		}
		Intent intent = null;
		if (!isPhoto) {
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(BaseFile.getTempFile()));

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		context.startActivityForResult(intent, requectCode);
	}

}
