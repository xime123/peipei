package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Title: RewardSkillGridView.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Aaron  
 *
 * @date 2015-9-25 下午6:21:37 
 *
 * @version V1.0   
 */
public class RewardSkillGridView extends GridView {

	public RewardSkillGridView(Context context) {
		super(context);
	}

	public RewardSkillGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RewardSkillGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
