package com.tshang.peipei.activity.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;

/**
 * 
 * @ClassName: IMessageDialog
 * @Description: 消息提示布局
 * @author Aaron
 * @date 2015-5-27 下午4:12:45
 * 
 */
@SuppressLint("InflateParams")
public class IMessageDialog extends IBaseDialog {

	private View view;

	private TextView mMsgText;

	private boolean backCancel;

	public IMessageDialog(Context context) {
		super(context);
		view = LayoutInflater.from(context).inflate(
				R.layout.message_dialog_layout, null);
		mMsgText = (TextView) view.findViewById(R.id.dialog_msg_text);
	}

	@Override
	public View createContentView() {
		return view;
	}

	/**
	 * 设置提示内容
	 * 
	 * @param msg
	 */
	public void setMsgText(String msg) {
		if ("".equals(msg) || null == msg) {
			return;
		}
		mMsgText.setText(msg);
	}

	/**
	 * 设置提示内容
	 * 
	 * @param res
	 */
	public void setMsgText(int res) {
		if (res <= 0) {
			return;
		}
		mMsgText.setText(res);
	}

	/**
	 * 设置内容字体颜色
	 * 
	 * @param color
	 */
	public void setMsgTextColor(int color) {
		if (color <= 0) {
			return;
		}
		mMsgText.setTextColor(color);
	}

	/**
	 * 设置内容字体大小
	 * 
	 * @param size
	 */
	public void setMsgTextSize(int size) {
		if (size <= 0) {
			return;
		}
		mMsgText.setTextSize(size);
	}

	/**
	 * 设置内容显示方式
	 * 
	 * @param gravity
	 */
	public void setMsgTextGravity(int gravity) {
		if (gravity <= 0) {
			return;
		}
		mMsgText.setGravity(gravity);
	}

	/**
	 * 设置按返回键对对话框的操作
	 * 
	 * @param backCancel
	 */
	public void setBackPressCancel(boolean backCancel) {
		this.backCancel = backCancel;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (backCancel) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
