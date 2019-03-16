package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @Title: PeiPeiListView.java 
 *
 * @Description: 可与scrollview嵌套使用的listview
 *
 * @author DYH  
 *
 * @date 2015-12-16 上午12:57:32 
 *
 * @version V1.0   
 */
public class PeiPeiListView extends ListView {
	public PeiPeiListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PeiPeiListView(Context context) {
		super(context);
	}

	public PeiPeiListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 该自定义控件只是重写了ListView的onMeasure方法，使其不会出现滚动条，ScrollView嵌套GridView也是同样的道理，不再赘述。
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
