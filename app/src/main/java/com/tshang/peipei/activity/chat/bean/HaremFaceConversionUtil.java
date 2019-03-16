package com.tshang.peipei.activity.chat.bean;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

/**
 * 
 ****************************************** 
 * @author Jeff
 * @文件名称 : HaremFaceConversionUtil.java
 * @创建时间 : 2014-12-5 
 * @文件描述 : 表情轉換工具
 ****************************************** 
 */
public class HaremFaceConversionUtil {

	/** 每一页表情的个数 */
	private static final int PAGESIZE = 8;

	private static HaremFaceConversionUtil mFaceConversionUtil;

	/** 保存于内存中的表情HashMap */
	private HashMap<String, String> emojiMap = new HashMap<String, String>();

	/** 保存于内存中的表情集合 */
	private ArrayList<EmotionBean> emojis = new ArrayList<EmotionBean>();

	/** 表情分页的结果集合 */
	private ArrayList<ArrayList<EmotionBean>> emojiLists = new ArrayList<ArrayList<EmotionBean>>();

	private HaremFaceConversionUtil() {

	}

	public static HaremFaceConversionUtil getInstace() {
		if (mFaceConversionUtil == null) {
			mFaceConversionUtil = new HaremFaceConversionUtil();
		}
		return mFaceConversionUtil;
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

		EmotionBean emojEentry;
		try {
			for (String str : EmotionUtil.haremEmotions) {
				String[] text = str.split(",");
				String fileName = text[0].substring(0, text[0].lastIndexOf("."));
				emojiMap.put(text[1], fileName);
				int resID = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());

				if (resID != 0) {
					emojEentry = new EmotionBean();
					emojEentry.setId(resID);
					emojEentry.setCharacter(text[1]);
					emojEentry.setFaceName(fileName);
					emojis.add(emojEentry);
				}
			}
			int pageCount = (int) Math.ceil(emojis.size() / 8.0);

			for (int i = 0; i < pageCount; i++) {
				emojiLists.add(getData(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int dealExpression(Context context, String key) {

		String value = emojiMap.get(key);
		if (TextUtils.isEmpty(value)) {
			return 0;
		}
		int resId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
		return resId;
	}

	public HashMap<String, String> getEmojiMap() {
		return emojiMap;
	}

	public ArrayList<EmotionBean> getEmojis() {
		return emojis;
	}

	public ArrayList<ArrayList<EmotionBean>> getEmojiLists(Activity activity) {
		if (emojiLists.isEmpty()) {
			emojis.clear();
			emojiMap.clear();
			getFileText(activity);
		}
		return emojiLists;
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

		if (endIndex > emojis.size()) {
			endIndex = emojis.size();
		}
		// 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
		ArrayList<EmotionBean> list = new ArrayList<EmotionBean>();
		list.addAll(emojis.subList(startIndex, endIndex));
		if (list.size() < PAGESIZE) {
			for (int i = list.size(); i < PAGESIZE; i++) {
				EmotionBean object = new EmotionBean();
				list.add(object);
			}
		}
		return list;
	}
}