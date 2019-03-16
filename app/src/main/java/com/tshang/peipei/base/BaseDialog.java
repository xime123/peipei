package com.tshang.peipei.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;

public class BaseDialog {

	/**
	 * 创建带2个输入框的对话框,有一个只能选择，不能输入
	 *
	 * @return
	 */
	public static Dialog createDialogByTwoInput2(final Context context, final View contentView, int theme, final BAHandler handler) {

		final Dialog dialog = new Dialog(context, theme == 0 ? android.R.style.Theme_Translucent_NoTitleBar : theme);

		try {
			dialog.setContentView(R.layout.dialog_view_two_input2);
			dialog.setCanceledOnTouchOutside(true);

			final EditText editName = (EditText) dialog.findViewById(R.id.dialog_edit_frist);
			final TextView editLoyalty = (TextView) dialog.findViewById(R.id.dialog_edit_second);

			dialog.findViewById(R.id.dialog_ok).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (TextUtils.isEmpty(editName.getText().toString())) {
						Message msg = handler.obtainMessage();
						msg.what = BAConstants.HandlerType.INPUT_NULL;
						handler.sendMessage(msg);
						return;
					} else if (TextUtils.isEmpty(editLoyalty.getText().toString())) {
						Message msg = handler.obtainMessage();
						msg.what = BAConstants.HandlerType.INPUT_NULL2;
						handler.sendMessage(msg);
						return;
					} else {
						Message msg = handler.obtainMessage();
						msg.what = BAConstants.HandlerType.CREATE_ALBUM_SURE;
						msg.obj = editName.getText().toString();
						msg.arg1 = Integer.parseInt(editLoyalty.getText().toString());
						handler.sendMessage(msg);
					}

					dialog.dismiss();
				}
			});

			dialog.findViewById(R.id.dialog_cancel).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			editLoyalty.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					handler.sendEmptyMessage(BAConstants.HandlerType.INPUT_MESSAGE);
				}
			});

			final Window w = dialog.getWindow();
			final WindowManager.LayoutParams wlps = w.getAttributes();
			wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
			wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
			wlps.dimAmount = 0.6f;
			wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
			wlps.gravity = Gravity.CENTER;
			w.setAttributes(wlps);
			return dialog;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
