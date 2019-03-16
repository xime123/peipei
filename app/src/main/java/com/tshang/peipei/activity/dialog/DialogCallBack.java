package com.tshang.peipei.activity.dialog;

/**
 * 对话框回调接口
 * 
 * @author Aaron
 * @date 2015-01-12
 * @version 1.1
 */
public interface DialogCallBack {

	public void setTitleText(String title);

	public void setTitleText(int res);

	public void setConfirmText(String confirm);

	public void setConfirmText(int res);

	public void setCancelText(String cancel);

	public void setCancelText(int res);

	public void setTitleTextColor(int color);

	public void setConfirmBtnTextColor(int color);

	public void setCancelBtnTextColor(int color);

	public void setTitleTextSize(int size);

	public void setConfirmBtnTextSize(int size);

	public void setCancelBtnTextSize(int size);

	public void setConfirmBtnOnClickListener(
			android.view.View.OnClickListener listener);

	public void setCancelBtnOnClickListener(
			android.view.View.OnClickListener listener);

	public void setBottomLayoutVisibility(int visibility);
}
