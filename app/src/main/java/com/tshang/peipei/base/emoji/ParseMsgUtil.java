package com.tshang.peipei.base.emoji;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.tshang.peipei.base.BaseTools;

public class ParseMsgUtil {

	public static String convertToMsg(CharSequence cs, Context mContext) {
		SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
		ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
		for (int i = 0; i < spans.length; i++) {
			ImageSpan span = spans[i];
			String c = span.getSource();
			int a = ssb.getSpanStart(span);
			int b = ssb.getSpanEnd(span);
			if (c.contains("emoji")) {
				ssb.replace(a, b, convertUnicode(c));
			}
		}
		ssb.clearSpans();
		return ssb.toString();
	}

	private static String convertUnicode(String emo) {
		emo = emo.substring(emo.indexOf("_") + 1);
		if (emo.length() < 6) {
			return new String(Character.toChars(Integer.parseInt(emo, 16)));
		}
		String[] emos = emo.split("_");
		char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
		char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
		char[] emoji = new char[char0.length + char1.length];
		for (int i = 0; i < char0.length; i++) {
			emoji[i] = char0[i];
		}
		for (int i = char0.length; i < emoji.length; i++) {
			emoji[i] = char1[i - char0.length];
		}
		return new String(emoji);
	}

	public static String convertUnicode2(String emo) {
		String regex = "\\[e\\](.*?)\\[/e\\]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emo);
		String tmpemo = "";
		while (matcher.find()) {
			tmpemo = matcher.group();
			try {
				String ss = tmpemo.substring(tmpemo.indexOf("]") + 1, tmpemo.lastIndexOf("["));
				String ss1 = null;
				if (ss.length() < 6) {
					ss1 = new String(Character.toChars(Integer.parseInt(ss, 16)));
				}
				emo = emo.replace(tmpemo, ss1);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return emo;
	}

	public static SpannableStringBuilder convetToHtml(String content, Context context, int index) {
		String regex = "\\[e\\](.*?)\\[/e\\]";
		Pattern pattern = Pattern.compile(regex);
		String emo = "";
		Resources resources = context.getResources();
		String unicode = EmojiParser.getInstance(context).parseEmoji(content);
		Matcher matcher = pattern.matcher(unicode);
		SpannableStringBuilder sBuilder = new SpannableStringBuilder(unicode);
		Drawable drawable = null;
		ImageSpan span = null;
		while (matcher.find()) {
			emo = matcher.group();
			try {

				int id = resources.getIdentifier("emoji_" + emo.substring(emo.indexOf("]") + 1, emo.lastIndexOf("[")), "drawable",
						BaseTools.getAppPackageName(context));
				if (id != 0) {
					drawable = resources.getDrawable(id);
					drawable.setBounds(0, 0, index, index);
					span = new ImageSpan(drawable, resources.toString(), ImageSpan.ALIGN_BOTTOM);
					sBuilder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} catch (Exception e) {
				break;
			}
		}
		return sBuilder;
	}

	public static int convetToHtml2(String content, Context mContext) {
		String regex = "\\[e\\](.*?)\\[/e\\]";
		Pattern pattern = Pattern.compile(regex);
		String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
		Matcher matcher = pattern.matcher(unicode);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}

}
