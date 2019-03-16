package com.tshang.peipei.activity.chat;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @Title: ChatEmojiPagerAdapter.java 
 *
 * @Description: emoji表情适配
 *
 * @author allen  
 *
 * @date 2014-3-27 上午10:33:47 
 *
 * @version V1.0   
 */
public class ChatEmojiPagerAdapter extends PagerAdapter {
	public List<View> mListViews;

	public ChatEmojiPagerAdapter(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(mListViews.get(arg1));
	}

	public void finishUpdate(View arg0) {}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mListViews.get(arg1), 0);
		return mListViews.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {}

}
