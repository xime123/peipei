package com.tshang.peipei.activity.mine;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;

/**
 * @Title: MineSettingBindPhonedActivity.java 
 *
 * @Description: 已绑定手机 
 *
 * @author allen  
 *
 * @date 2014-11-18 下午8:28:43 
 *
 * @version V1.0   
 */
public class MineSettingBindPhonedActivity extends BaseActivity {

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mBackText.setText(R.string.back);
		mBackText.setOnClickListener(this);
		mTitle.setText(R.string.str_bind_phone);

		String phone = new String(BAApplication.mLocalUserInfo.phone);

		TextView tv = (TextView) findViewById(R.id.bind_phone_ok_tv);

		SpannableStringBuilder style = new SpannableStringBuilder(getString(R.string.str_bind_phone_ok));
		SpannableStringBuilder style1 = new SpannableStringBuilder(phone);
		style1.setSpan(new PhoneRedStyle(), 0, 11, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		style.append(style1);
		tv.setText(style);
	}

	@Override
	protected int initView() {
		return R.layout.activity_mobilephone1;
	}

	public class PhoneRedStyle extends ClickableSpan {

		@Override
		public void updateDrawState(TextPaint ds) {//选中的文字变色
			super.updateDrawState(ds);//369a00
			ds.setColor(getResources().getColor(R.color.peach));
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View widget) {}
	}

}
