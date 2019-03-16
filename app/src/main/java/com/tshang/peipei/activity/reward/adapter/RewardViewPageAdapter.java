package com.tshang.peipei.activity.reward.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @Title: RewardViewPageAdapter.java 
 *
 * @Description: 
 *
 * @author Aaron  
 *
 * @date 2015-9-24 下午4:43:06 
 *
 * @version V1.0   
 */
public class RewardViewPageAdapter extends PagerAdapter {

	private List<View> viewLists;

	public RewardViewPageAdapter(List<View> list) {
		if (list == null) {
			this.viewLists = new ArrayList<View>();
		} else {
			this.viewLists = list;
		}
	}

	@Override
	public int getCount() { //获得size
		return viewLists.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View view, int position, Object object) //销毁Item
	{
		((ViewPager) view).removeView(viewLists.get(position));
	}

	@Override
	public Object instantiateItem(View view, int position) //实例化Item
	{
		((ViewPager) view).addView(viewLists.get(position), 0);
		return viewLists.get(position);
	}

}
