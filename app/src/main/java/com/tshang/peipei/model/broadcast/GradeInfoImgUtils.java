package com.tshang.peipei.model.broadcast;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;
import com.tshang.peipei.vender.imageloader.core.assist.FailReason;
import com.tshang.peipei.vender.imageloader.core.listener.ImageLoadingListener;

/**
 * @Title: GradeInfoImgUtils.java 
 *
 * @Description: 加载等级头像的参数
 *
 * @author Administrator  
 *
 * @date 2014年8月14日 下午4:39:53 
 *
 * @version V1.0   
 */
public class GradeInfoImgUtils {
	/**
	 * 
	 * @author Jeff
	 *
	 * @param imageLoader
	 * @param gradeinfo 头像等级字符串
	 * @param imageview 显示等级的view
	 * @param options 图片保存选项
	 */
	public static void loadGradeInfoImg(final Activity activity, ImageLoader imageLoader, String gradeinfo, final ImageView imageview,
			DisplayImageOptions options) {
		if (TextUtils.isEmpty(gradeinfo)) {
			return;
		}
		String[] gradeinfos = gradeinfo.split(",");
		if (gradeinfos == null || gradeinfos.length < 2) {//第一位为id,第二位为等级头像
			return;
		}
		String key = "http://" + gradeinfos[1] + BAConstants.LOAD_0APPENDSTR;
		imageLoader.loadImage(key, options, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				int imageWidth = loadedImage.getWidth();
				int loadedImageHeight = loadedImage.getHeight();
				int finalw = BaseUtils.dip2px(activity, (float) (imageWidth / 2));
				int finalh = BaseUtils.dip2px(activity, (float) (loadedImageHeight / 2));
				LinearLayout.LayoutParams parames = new LinearLayout.LayoutParams(finalw, finalh);
				parames.leftMargin = BaseUtils.dip2px(activity, 5);
				imageview.setLayoutParams(parames);
				imageview.setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}
		});
	}
	
	/**
	 * 
	 * @author Administrator
	 * 父控件为RelativeLayout的等级图标显示器
	 * @param imageLoader
	 * @param gradeinfo 头像等级字符串
	 * @param imageview 显示等级的view
	 * @param options 图片保存选项
	 */
	public static void loadGradeInfoImgInRL(final Activity activity, ImageLoader imageLoader, String gradeinfo, final ImageView imageview,
			DisplayImageOptions options) {
		if (TextUtils.isEmpty(gradeinfo)) {
			return;
		}
		String[] gradeinfos = gradeinfo.split(",");
		if (gradeinfos == null || gradeinfos.length < 2) {//第一位为id,第二位为等级头像
			return;
		}
		String key = "http://" + gradeinfos[1] + BAConstants.LOAD_0APPENDSTR;
		imageLoader.loadImage(key, options, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				int imageWidth = loadedImage.getWidth();
				int loadedImageHeight = loadedImage.getHeight();
				int finalw = BaseUtils.dip2px(activity, (float) (imageWidth / 2));
				int finalh = BaseUtils.dip2px(activity, (float) (loadedImageHeight / 2));
				RelativeLayout.LayoutParams parames = new RelativeLayout.LayoutParams(finalw, finalh);
				parames.leftMargin = BaseUtils.dip2px(activity, 5);
				imageview.setLayoutParams(parames);
				imageview.setImageBitmap(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});
	}

	public static void loadGradeInfoImg(Activity activity, ImageLoader imageLoader, DisplayImageOptions options, String gradeinfo, ImageView imageview) {
		loadGradeInfoImg(activity, imageLoader, gradeinfo, imageview, options);
	}
	
	public static void loadGradeInfoImgInRL(Activity activity, ImageLoader imageLoader, DisplayImageOptions options, String gradeinfo, ImageView imageview) {
		loadGradeInfoImgInRL(activity, imageLoader, gradeinfo, imageview, options);
	}

	public static void loadCampaignHatImg(final Activity activity, ImageLoader imageLoader, DisplayImageOptions campaignHatOptions,
			int campaignHatValue, final ImageView imageview) {

		imageLoader.loadImage("http://@" + campaignHatValue, campaignHatOptions, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				int imageWidth = loadedImage.getWidth();
				int loadedImageHeight = loadedImage.getHeight();
				int finalw = BaseUtils.dip2px(activity, (float) (imageWidth / 2));
				int finalh = BaseUtils.dip2px(activity, (float) (loadedImageHeight / 2));
				LinearLayout.LayoutParams parames = new LinearLayout.LayoutParams(finalw, finalh);
				parames.leftMargin = BaseUtils.dip2px(activity, 5);
				imageview.setLayoutParams(parames);
				imageview.setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}
		});
	}
	
	public static void loadDeliverImg(final Activity activity, ImageLoader imageLoader, DisplayImageOptions campaignHatOptions,
			int campaignHatValue, final ImageView imageview) {
		
//		imageLoader.displayImage("http://@" + campaignHatValue, imageview, campaignHatOptions);
		Log.d("Aaron", "loadDeliverImg url==" + "http://@" + campaignHatValue);
		imageLoader.loadImage("http://@" + campaignHatValue, campaignHatOptions, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				imageview.setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}
		});
	}

}
