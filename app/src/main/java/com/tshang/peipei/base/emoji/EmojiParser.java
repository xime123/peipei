package com.tshang.peipei.base.emoji;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;

public class EmojiParser {

	private final String mEmojiXml = "emoji.xml";

	private EmojiParser(Context context) {
		readMap(context);
	}

	private HashMap<List<Integer>, String> convertMap = new HashMap<List<Integer>, String>();
	private HashMap<String, ArrayList<String>> emoMap = new HashMap<String, ArrayList<String>>();
	private static EmojiParser mParser;

	public static EmojiParser getInstance(Context mContext) {
		if (mParser == null) {
			mParser = new EmojiParser(mContext);
		}
		return mParser;
	}

	public HashMap<String, ArrayList<String>> getEmoMap() {
		return emoMap;
	}

	public void readMap(Context mContext) {
		if (convertMap == null || convertMap.size() == 0) {
			convertMap = new HashMap<List<Integer>, String>();
			XmlPullParser xmlpull = null;
			String fromAttr = null;
			String key = null;
			ArrayList<String> emos = null;
			try {
				XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
				xmlpull = xppf.newPullParser();
				InputStream stream = mContext.getAssets().open(mEmojiXml);
				xmlpull.setInput(stream, "UTF-8");
				int eventCode = xmlpull.getEventType();
				while (eventCode != XmlPullParser.END_DOCUMENT) {
					switch (eventCode) {
					case XmlPullParser.START_DOCUMENT: {
						break;
					}
					case XmlPullParser.START_TAG: {
						if (xmlpull.getName().equals("key")) {
							emos = new ArrayList<String>();
							key = xmlpull.nextText();
						}
						if (xmlpull.getName().equals("e")) {
							fromAttr = xmlpull.nextText();
							emos.add(fromAttr);
							List<Integer> fromCodePoints = new ArrayList<Integer>();
							if (fromAttr.length() > 6) {
								String[] froms = fromAttr.split("\\_");
								for (String part : froms) {
									fromCodePoints.add(Integer.parseInt(part, 16));
								}
							} else {
								fromCodePoints.add(Integer.parseInt(fromAttr, 16));
							}
							convertMap.put(fromCodePoints, fromAttr);
						}
						break;
					}
					case XmlPullParser.END_TAG: {
						if (xmlpull.getName().equals("dict")) {
							emoMap.put(key, emos);
						}
						break;
					}
					case XmlPullParser.END_DOCUMENT: {
						break;
					}
					}
					eventCode = xmlpull.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String parseEmoji(String input) {
		if (input == null || input.length() <= 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		int[] codePoints = toCodePointArray(input);
		int codePointsLen = codePoints.length;
		List<Integer> key = null;
		for (int i = 0; i < codePointsLen; i++) {
			key = new ArrayList<Integer>();
			if (i + 1 < codePointsLen) {
				key.add(codePoints[i]);
				key.add(codePoints[i + 1]);
				if (convertMap.containsKey(key)) {
					String value = convertMap.get(key);
					if (value != null) {
						result.append("[e]" + value + "[/e]");
					}
					i++;
					continue;
				}
			}
			key.clear();
			key.add(codePoints[i]);
			if (convertMap.containsKey(key)) {
				String value = convertMap.get(key);
				if (value != null) {
					result.append("[e]" + value + "[/e]");
				}
				continue;
			}
			result.append(Character.toChars(codePoints[i]));
		}
		return result.toString();
	}

	private int[] toCodePointArray(String str) {
		char[] ach = str.toCharArray();
		int len = ach.length;
		int[] acp = new int[Character.codePointCount(ach, 0, len)];
		int j = 0;
		for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
			cp = Character.codePointAt(ach, i);
			acp[j++] = cp;
		}
		return acp;
	}

	public String convertEmoji(String input) {
		if (input == null || input.length() <= 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		int[] codePoints = toCodePointArray(input);
		List<Integer> key = null;
		for (int i = 0; i < codePoints.length; i++) {
			key = new ArrayList<Integer>();
			if (i + 1 < codePoints.length) {
				key.add(codePoints[i]);
				key.add(codePoints[i + 1]);
				if (convertMap.containsKey(key)) {
					String value = convertMap.get(key);
					if (value != null) {
						result.append("[表情]");
					}
					i++;
					continue;
				}
			}
			key.clear();
			key.add(codePoints[i]);
			if (convertMap.containsKey(key)) {
				String value = convertMap.get(key);
				if (value != null) {
					result.append("[表情]");
				}
				continue;
			}
			result.append(Character.toChars(codePoints[i]));
		}
		return result.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public List<View> setEmoji(final Context mContext, final EditText et) {
		List<View> listViews = new ArrayList<View>();
		Iterator iter = emoMap.entrySet().iterator();
		int index = 0;
		int padding = BaseUtils.dip2px(mContext, 5);
		int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		int spacing = BaseUtils.dip2px(mContext, 20);
		int size = (BaseUtils.dip2px(mContext, 33));
		int horPadding = (screenWidth - spacing - 7 * size) / 14;
		int verPadding = (BaseUtils.dip2px(mContext, 120) - size * 3) / 6;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object val = entry.getValue();
			ArrayList<String> list = (ArrayList<String>) val;

			LinearLayout lin = new LinearLayout(mContext);
			lin.setOrientation(LinearLayout.VERTICAL);
			lin.setPadding(0, padding, 0, padding);
			int k = 0;
			for (int i = 0; i < 3; i++) {
				LinearLayout layout = new LinearLayout(mContext);
				LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				layout.setOrientation(LinearLayout.HORIZONTAL);
				layout.setLayoutParams(itemParam);

				layout.setPadding(10, 10, 10, 10);
				for (int j = 0; j < 7; j++) {
					if (k >= list.size()) {
						if (k == 20) {
							LinearLayout layoutBtn = new LinearLayout(mContext);
							LinearLayout.LayoutParams itemParam1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							layoutBtn.setOrientation(LinearLayout.HORIZONTAL);
							layoutBtn.setLayoutParams(itemParam1);
							if (index == emoMap.size() - 1) {
								layoutBtn.setGravity(Gravity.RIGHT);
							}

							layoutBtn.setPadding(horPadding, verPadding, horPadding, verPadding);

							ImageView btn = new ImageView(mContext);
							Drawable drawable = mContext.getResources().getDrawable(R.drawable.message_img_cancel_selector);

							btn.setBackgroundDrawable(drawable);
							btn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									((FragmentActivity) mContext).dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
								}
							});
							layoutBtn.addView(btn);
							layout.addView(layoutBtn);
						}
						k++;
						continue;
					}
					TextView tv = new TextView(mContext);

					final SpannableStringBuilder s = ParseMsgUtil
							.convetToHtml("[e]" + list.get(k) + "[/e]", mContext, BaseUtils.dip2px(mContext, 24));
					k++;
					tv.setText(s);
					tv.setPadding(horPadding, verPadding, horPadding, verPadding);
					tv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String st = et.getText().toString();
							et.requestFocus();
							SpannableStringBuilder sss = ParseMsgUtil.convetToHtml(st + s, mContext, BaseUtils.dip2px(mContext, 24));
							et.setText(sss);
							et.setSelection(sss.length());
						}
					});
					layout.addView(tv);
				}

				lin.addView(layout);
			}

			listViews.add(lin);
			index++;
		}
		return listViews;
	}

}
