package com.tshang.peipei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * @Title: PeipeiWebView.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014-9-2 上午11:57:15 
 *
 * @version V1.0   
 */
public class PeipeiWebView extends WebView {

	public PeipeiWebView(Context context) {
		super(context);
	}

	public PeipeiWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int temp_ScrollY = getScrollY();
				scrollTo(getScrollX(), getScrollY() + 1);
				scrollTo(getScrollX(), temp_ScrollY);
			}
			return super.onTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

}
