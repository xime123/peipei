package com.tshang.peipei.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Title: RewardSkillViewPager.java 
 *
 * @Description: 发布悬赏自适应技能 列表 
 *
 * @author Aaron  
 *
 * @date 2015-9-25 下午5:21:11 
 *
 * @version V1.0   
 */
public class RewardSkillViewPager extends ViewPager {

	public RewardSkillViewPager(Context context) {
		super(context);
	}

	public RewardSkillViewPager(Context context, AttributeSet attr) {
		super(context, attr);
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
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height * 3 + 60, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
