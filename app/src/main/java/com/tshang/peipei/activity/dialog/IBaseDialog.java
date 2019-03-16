package com.tshang.peipei.activity.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tshang.peipei.R;

/**
 * 
 * @ClassName: IMessageBaseDialog
 * @Description: 基类Dialog
 * @author Aaron
 * @date 2015年5月22日 上午11:49:28
 * 
 */
@SuppressLint("InlinedApi")
public abstract class IBaseDialog extends Dialog {

	/**
	 * 内容控件
	 */
	private FrameLayout mContent;

	/**
	 * 标题
	 */
	private TextView mTitleText;

	/**
	 * 确定Btn
	 */
	private TextView mConfirmBtnText;

	/**
	 * 取消Btn
	 */
	private TextView mCancelBtnText;

	/**
	 * 底部按钮Layout
	 */
	private LinearLayout mBottomLayout;

	private View line;

	/**
	 * 监听器
	 */
	private android.view.View.OnClickListener mConfirBtnListener;
	private android.view.View.OnClickListener mCancelBtnListener;

	public IBaseDialog(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(createContentView());
	}

	@SuppressWarnings("deprecation")
	private void init(Context context) {
		this.getContext().setTheme(android.R.style.Theme_InputMethod);
		super.setContentView(R.layout.base_dialog_layout);

		mContent = (FrameLayout) findViewById(R.id.dialog_content_layout);
		mTitleText = (TextView) findViewById(R.id.dialog_title);
		mConfirmBtnText = (TextView) findViewById(R.id.dialog_confirm_text);
		mCancelBtnText = (TextView) findViewById(R.id.dialog_calcel_text);
		mBottomLayout = (LinearLayout) findViewById(R.id.dialog_bottom_layout);
		line = (View) findViewById(R.id.dialog_vertical_line);

		mCancelBtnText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCancelBtnListener != null) {
					mCancelBtnListener.onClick(v);
				} else {
					dismiss();
				}
			}
		});

		mConfirmBtnText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mConfirBtnListener != null) {
					mConfirBtnListener.onClick(v);
				} else {
					dismiss();
				}
			}
		});

		Window window = getWindow();
		WindowManager.LayoutParams attributesParams = window.getAttributes();
		attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		attributesParams.dimAmount = 0.4f;
		int width = (int) (window.getWindowManager().getDefaultDisplay().getWidth() * 0.9f);
		window.setLayout(width, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 构建dialog的内容视图
	 * 
	 * @return
	 */
	public abstract View createContentView();

	/**
	 * 内容布局创建
	 */
	@SuppressLint("InlinedApi")
	public void setContentView(View view) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		mContent.addView(view);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitleText(String title) {
		if (title == null || "".equals(title)) {
			mTitleText.setVisibility(View.GONE);
			return;
		}
		mTitleText.setText(title);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitleText(int res) {
		if (res <= 0) {
			return;
		}
		mTitleText.setText(res);
	}

	/**
	 * 设置确定按钮
	 * 
	 * @param confirm
	 */
	public void setConfirmBtnText(String confirm) {
		if (confirm == null || "".equals(confirm)) {
			return;
		}
		mConfirmBtnText.setText(confirm);
	}

	/**
	 * 设置确定按钮
	 * 
	 * @param confirm
	 */
	public void setConfirmBtnText(int res) {
		if (res <= 0) {
			return;
		}
		mConfirmBtnText.setText(res);
	}

	/**
	 * 设置取消按钮
	 * 
	 * @param cancel
	 */
	public void setCancelBtnText(String cancel) {
		if (cancel == null || "".equals(cancel)) {
			return;
		}
		mCancelBtnText.setText(cancel);
	}

	/**
	 * 设置取消按钮
	 * 
	 * @param cancel
	 */
	public void setCancelBtnText(int res) {
		if (res <= 0) {
			return;
		}
		mCancelBtnText.setText(res);
	}

	/**
	 * 设置标题字体颜色
	 * 
	 * @param color
	 */
	public void setTitleTextColor(int color) {
		if (color <= 0) {
			return;
		}
		mTitleText.setTextColor(color);
	}

	/**
	 * 设置确定按钮字体颜色
	 * 
	 * @param color
	 */
	public void setConfirmBtnTextColor(int color) {
		if (color <= 0) {
			return;
		}
		mConfirmBtnText.setTextColor(color);
	}

	/**
	 * 设置取消按钮字体颜色
	 * 
	 * @param color
	 */
	public void setCancelBtnTextColor(int color) {
		if (color <= 0) {
			return;
		}
		mCancelBtnText.setTextColor(color);
	}

	/**
	 * 设置标题字体大小
	 * 
	 * @param size
	 */
	public void setTitleTextSize(int size) {
		if (size <= 0) {
			return;
		}
		mTitleText.setTextSize(size);
	}

	/**
	 * 设置确定按钮字体大小
	 * 
	 * @param size
	 */
	public void setConfirmBtnTextSize(int size) {
		if (size <= 0) {
			return;
		}
		mConfirmBtnText.setTextSize(size);
	}

	/**
	 * 设置取消按钮字体大小
	 * 
	 * @param size
	 */
	public void setCancelBtnTextSize(int size) {
		if (size <= 0) {
			return;
		}
		mCancelBtnText.setTextSize(size);
	}

	/**
	 * 设置确定按钮监听回调
	 * 
	 * @param listener
	 */
	public void setConfirmBtnOnClickListener(android.view.View.OnClickListener listener) {
		this.mConfirBtnListener = listener;
	}

	/**
	 * 设置取消按钮监听回调
	 * 
	 * @param listener
	 */
	public void setCancelBtnOnClickListener(android.view.View.OnClickListener listener) {
		this.mCancelBtnListener = listener;
	}

	/**
	 * 设置标题显示方式
	 * 
	 * @param gravity
	 */
	public void setTitleTextGravity(int gravity) {
		if (gravity <= 0) {
			return;
		}
		mTitleText.setGravity(gravity);
	}

	/**
	 * 设置标题显示状态
	 * 
	 * @param visibility
	 */
	public void setTitleTextVisibility(int visibility) {
		if (visibility <= 0) {
			return;
		}
		mTitleText.setVisibility(visibility);
	}

	/**
	 * 设置取消按钮显示状态
	 * 
	 * @param visibility
	 */
	public void setCancelBtnTextVisibility(int visibility) {
		if (visibility == View.GONE) {
			line.setVisibility(View.GONE);
		}
		mCancelBtnText.setVisibility(visibility);
	}

	public void setConfirBtnTextVisibility(int visibility) {
		mConfirmBtnText.setVisibility(visibility);
	}

	/**
	 * 设置底部Layout显示状态
	 * 
	 * @param visibility
	 */
	public void setBottomLayoutVisibility(int visibility) {
		if (visibility <= 0) {
			return;
		}
		mBottomLayout.setVisibility(visibility);
	}
}
