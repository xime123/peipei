package com.tshang.peipei.vender.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;
import com.tshang.peipei.vender.imageloader.core.assist.ImageScaleType;
import com.tshang.peipei.vender.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * @Title: ImageOptionsUtils.java 
 *
 * @Description: 设置图片的一些基本属性加载
 *
 * @author Jeff
 *
 * @date 2014年8月1日 上午10:58:47 
 *
 * @version V1.0   
 */
public class ImageOptionsUtils {

	/**
	 * 获取图片80圆角的头像,缓存在本地
	 * @author jeff
	 *
	 * @param activity 上下文对象
	 * @return
	 */
	public static DisplayImageOptions GetHeadKeySmallRounded(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaulthead_un).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 27))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 后宫头像
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetGroupHeadKeySmallRounded(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.message_list_palace_ic).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 40))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 悬浮框加载配置
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetFloatIconKeySmallRounded(Activity activity) {
		return new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 40)))
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public static DisplayImageOptions GetGroupHeadKeySmall(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaultpic_small).cacheOnDisk(false).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 加载的是大图的头像圆角
	 * @author Jeff
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetHeadKeyBigRounded(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.homepage_headimg_defaut).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 70))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	public static DisplayImageOptions GetGroupHeadKeyBigRounded(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaultpic_small).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 30))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 获取通过uid获取到的头像，不缓存在本地磁盘 80的小图
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetHeadUidSmallRounded(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaulthead_un).cacheOnDisk(false).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 27))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 获取通过uid获取到的头像，不缓存在本地磁盘 80的小图
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetHeadUidSmallRounded(Context activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaulthead_un).cacheOnDisk(false).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 27))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 获取通过uid获取到的头像，不缓存在本地磁盘 80的小图
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetHeadUidSmallRoundedByW(Activity activity, int w) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaulthead_un).cacheOnDisk(false).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, w))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	/**
	 * 获取通过uid获取到的头像，不缓存在本地磁盘 80的小图
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetHeadUidSmallRoundedByGift(Activity activity, int w) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.couplet_monekey_face).cacheOnDisk(false).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, w))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 图文适配
	 * @author Aaron
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetImgTextRounded(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.dynamicinfo_defaultimage).cacheOnDisk(false).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 6))).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 图文适配没有圆角
	 * @author Aaron
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions GetImgTextDefult(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.dynamicinfo_defaultimage).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE).build();
	}

	/**
	 * 一般的没有圆角 的图片,缓存在本地和硬盘
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions getImageKeyOptions(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.main_img_defaultpic_small).cacheOnDisk(true).cacheInMemory(true)
				.considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE).build();

	}

	/**
	 * 个人主页的背景图片
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions getImageKeyOptionsPersonBg(Activity activity) {
		return new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).showImageOnFail(R.drawable.person_img_defaultpic_small)
				.showImageForEmptyUri(R.drawable.person_img_defaultpic_small).cacheOnDisk(true).cacheInMemory(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	public static DisplayImageOptions getGradeInfoImageKeyOptions(Activity activity) {
		return new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheOnDisk(true).showImageOnLoading(R.drawable.head_default_level)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	/**
	 * 活动帽子
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions getCampaignHatOptions(Activity activity) {
		return new DisplayImageOptions.Builder().cacheInMemory(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();

	}

	/**
	 * 头像挂件
	 * @author Aaron
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions getDeliverGiftOptions(Activity activity) {
		return new DisplayImageOptions.Builder().cacheInMemory(false).considerExifParams(true).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE).build();
	}

	public static DisplayImageOptions getShareOptions(Activity activity) {
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();

	}

	/**
	 * 视频缩略图
	 */
	public static DisplayImageOptions getVideoThumbOptions(Activity activity) {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.message_icon_video_file_play_default)
				.showImageForEmptyUri(R.drawable.message_icon_video_file_play_default)
				.showImageOnFail(R.drawable.message_icon_video_file_play_default).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).displayer(new RoundedBitmapDisplayer(BaseUtils.dip2px(activity, 8)))
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	/**
	 * 
	 * @author 悬赏礼物
	 *
	 * @param activity
	 * @return
	 */
	public static DisplayImageOptions getRewardGiftOptions(Activity activity) {
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnLoading(R.drawable.homepage_img_default).showImageForEmptyUri(R.drawable.homepage_img_default)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}
}
