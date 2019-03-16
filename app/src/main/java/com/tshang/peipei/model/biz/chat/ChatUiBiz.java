package com.tshang.peipei.model.biz.chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.tshang.peipei.R;

/**
 * @Title: ChatUiBiz.java 
 *
 * @Description: (用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014-7-16 下午7:59:35 
 *
 * @version V1.0   
 */
public class ChatUiBiz {

	/**
	 * 弹出选择图片方式的提示框
	 *
	 */
	public static PopupWindow initPopuptWindow2Pic(Activity context, PopupWindow popMenu, OnClickListener listener) {
		// 获取自定义布局文件xml的视图
		View popupWindow_view = context.getLayoutInflater().inflate(R.layout.popupwindow_select_picture, null, false);
		popMenu = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popMenu.setTouchable(true);
		popMenu.setOutsideTouchable(true);
		popMenu.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
		popMenu.getContentView().setFocusableInTouchMode(true);
		popMenu.getContentView().setFocusable(true);
		// 设置动画效果
		popMenu.setAnimationStyle(R.style.anim_popwindow_bottombar);
		popupWindow_view.findViewById(R.id.pop_open_photograph).setOnClickListener(listener);
		popupWindow_view.findViewById(R.id.pop_open_picture).setOnClickListener(listener);
		popupWindow_view.findViewById(R.id.pop_close).setOnClickListener(listener);
		return popMenu;
	}
}
