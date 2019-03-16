package com.tshang.peipei.activity.chat.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.vender.imageloader.core.ImageLoader;

/**
 * 
 ****************************************** 
 * @author Jeff
 * @文件名称 : HaremFaceConversionUtil.java
 * @创建时间 : 2014-12-5 
 * @文件描述 : 表情轉換工具
 ****************************************** 
 */
public class CommonFaceConversionUtil {

	/** 每一页表情的个数 */
	private static final int PAGESIZE = 20;

	private static CommonFaceConversionUtil mFaceConversionUtil;

	/** 保存于内存中的表情HashMap */
	private HashMap<String, String> commonMap = new HashMap<String, String>();

	/** 保存于内存中的表情集合 */
	private ArrayList<EmotionBean> commonFace = new ArrayList<EmotionBean>();

	/** 表情分页的结果集合 */
	private ArrayList<ArrayList<EmotionBean>> commonLists = new ArrayList<ArrayList<EmotionBean>>();

	public HashMap<String, String> getCommonMap() {
		return commonMap;
	}

	public ArrayList<EmotionBean> getCommonFace() {
		return commonFace;
	}

	public ArrayList<ArrayList<EmotionBean>> getCommonLists(Activity activity) {
		if (commonLists.isEmpty()) {
			commonFace.clear();
			commonMap.clear();
			getFileText(activity);
		}
		return commonLists;
	}

	private CommonFaceConversionUtil() {

	}

	public static CommonFaceConversionUtil getInstace() {
		if (mFaceConversionUtil == null) {
			mFaceConversionUtil = new CommonFaceConversionUtil();
		}
		return mFaceConversionUtil;
	}

	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public SpannableString getExpressionString1(Context context, SpannableString spannableString, int bitmapSize) {
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊<p+0.01>
		String zhengze = "<[p][+][0-9].[0-9][0-9]>";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0, bitmapSize);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		} catch (StackOverflowError error) {
			Log.e("dealExpression_error", error.getMessage());
		}
		return spannableString;
	}

	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(Context context, int imgId, String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		int size = BaseUtils.dip2px(context, HaremEmotionUtil.EMOJI_SAMLL_SIZE);
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + imgId);
		bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
		ImageSpan imageSpan = new ImageSpan(context, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start, int bitmapSize) throws Exception,
			StackOverflowError {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			String value = commonMap.get(key);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
			// 通过上面匹配得到的字符串来生成图片资源id
			if (resId != 0) {
				int size = BaseUtils.dip2px(context, bitmapSize);
				Bitmap bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + resId);
				bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
				// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
				ImageSpan imageSpan = new ImageSpan(context, bitmap);
				// 计算该图片名字的长度，也就是要替换的字符串的长度
				int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				if (end < spannableString.length()) {
					// 如果整个字符串还未验证完，则继续。。
					dealExpression(context, spannableString, patten, end, bitmapSize);
				}
				break;
			}
		}
	}

	public void getFileText(Context context) {
		ParseData(context);
	}

	/**
	 * 解析字符
	 * 
	 * @param data
	 */
	private void ParseData(Context context) {

		try {
			for (String str : EmotionUtil.commonEmotions) {
				String[] text = str.split(",");
				String fileName = text[0].substring(0, text[0].lastIndexOf("."));
				commonMap.put(text[1], fileName);
				int resID = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());

				if (resID != 0) {
					EmotionBean emojEentry = new EmotionBean();
					emojEentry.setId(resID);
					emojEentry.setCharacter(text[1]);
					emojEentry.setFaceName(fileName);
					commonFace.add(emojEentry);
				}
			}
			int pageCount = (int) Math.ceil(commonFace.size() / 20.0);
			for (int i = 0; i < pageCount; i++) {
				commonLists.add(getData(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private ArrayList<EmotionBean> getData(int page) {
		int startIndex = page * PAGESIZE;
		int endIndex = startIndex + PAGESIZE;

		if (endIndex > commonFace.size()) {
			endIndex = commonFace.size();
		}
		// 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
		ArrayList<EmotionBean> list = new ArrayList<EmotionBean>();
		list.addAll(commonFace.subList(startIndex, endIndex));
		if (list.size() < PAGESIZE) {
			for (int i = list.size(); i < PAGESIZE; i++) {
				EmotionBean object = new EmotionBean();
				list.add(object);
			}
		}
		if (list.size() == PAGESIZE) {
			EmotionBean object = new EmotionBean();
			object.setId(R.drawable.message_img_cancel_selector);
			list.add(object);
		}
		return list;
	}
}