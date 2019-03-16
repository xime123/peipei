package com.tshang.peipei.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Title: RewardSkillViewPager.java 
 *
 * @Description: 自适应发布悬赏礼物列表 
 *
 * @author Aaron  
 *
 * @date 2015-9-25 下午5:21:11 
 *
 * @version V1.0   
 */
public class RewardGiftViewPager extends ViewPager {

	public RewardGiftViewPager(Context context) {
		super(context);
	}

	public RewardGiftViewPager(Context context, AttributeSet attr) {
		super(context, attr);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = 0;
		//下面遍历所有child的高度
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int h = child.getMeasuredHeight();
			if (h > height) //采用最大的view的高度。
				height = h;
		}
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
