package com.tshang.peipei.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 
 * @ClassName: BounceScrollView
 * @Description: 实现布局反弹效果
 * @author Aaron
 * @date 2015年5月22日 上午11:44:08
 * 
 */
public class BounceScrollView extends ScrollView {

	@SuppressWarnings("unused")
	private final String TAG = BounceScrollView.class.getSimpleName();

	private View inner;// 孩子View

	private float y;// 点击时y坐标

	private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

	private boolean isCount = false;// 是否开始计算

	public BounceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
	 * 方法，也应该调用父类的方法，使该方法得以执行.
	 */
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptHoverEvent(ev);
	}

	/***
	 * 监听touch
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}
}
