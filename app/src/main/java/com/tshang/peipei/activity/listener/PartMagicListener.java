package com.tshang.peipei.activity.listener;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

import com.tshang.peipei.R;
import com.tshang.peipei.base.BaseUtils;

/**
 * 仙术文本变色
 * @author Administrator
 *
 */
public class PartMagicListener extends CharacterStyle {
	private Activity activity;

	public PartMagicListener(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void updateDrawState(TextPaint ds) {//选中的文字变色
		ds.setColor(activity.getResources().getColor(R.color.gray));
		ds.setUnderlineText(false);
		ds.setTextSize(BaseUtils.sp2px(activity, 12));

	}

	public static SpannableStringBuilder setStyle(Activity activity, String str) {
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new PartMagicListener(activity), 0, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}

}
