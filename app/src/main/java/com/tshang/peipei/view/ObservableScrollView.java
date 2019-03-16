package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @Title: ObservableScrollView.java 
 *
 * @Description: TODO可以监听滚动状态的scrollview
 *
 * @author DYH  
 *
 * @date 2015-10-13 上午11:13:41 
 *
 * @version V1.0   
 */
public class ObservableScrollView extends ScrollView {

	private ScrollListener scrollListener;

	public void setScrollListener(ScrollListener scrollListener) {
		this.scrollListener = scrollListener;
	}

	public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObservableScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (scrollListener != null) {
			scrollListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	public interface ScrollListener {
		void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
	}
}
