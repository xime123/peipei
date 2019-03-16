package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Title: PeipeiGridView.java 
 *
 * @Description: 用户与listview嵌套使用
 *
 * @author allen  
 *
 * @date 2014-6-26 下午4:13:40 
 *
 * @version V1.0   
 */
public class PeipeiGridView extends GridView {
	public PeipeiGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PeipeiGridView(Context context) {
		super(context);
	}

	public PeipeiGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
