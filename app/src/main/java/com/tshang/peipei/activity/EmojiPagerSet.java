package com.tshang.peipei.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.EditText;

import com.tshang.peipei.activity.chat.ChatEmojiPagerAdapter;
import com.tshang.peipei.base.emoji.EmojiParser;
import com.tshang.peipei.view.PageControlView;

/**
 * @Title: EmojiPagerSet.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 表情面板
 *
 * @author Jeff 
 *
 * @date 2014年10月15日 下午9:11:04 
 *
 * @version V1.4.0   
 */
public class EmojiPagerSet implements OnPageChangeListener {
	private PageControlView pageControlView;

	public EmojiPagerSet(Activity activity, EditText edt_input, PageControlView pageControlView, ViewPager viewPage) {
		this.pageControlView = pageControlView;
		List<View> mListViews = new ArrayList<View>();
		mListViews.addAll(EmojiParser.getInstance(activity).setEmoji(activity, edt_input));
		ChatEmojiPagerAdapter mEmojiAdapter = new ChatEmojiPagerAdapter(mListViews);
		pageControlView.count = mListViews.size();
		viewPage.setAdapter(mEmojiAdapter);
		viewPage.setCurrentItem(0);
		pageControlView.generatePageControl(0);
		viewPage.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		pageControlView.generatePageControl(arg0);
	}

}
