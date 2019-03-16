package com.tshang.peipei.activity.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;

/**
 * @Title: CreatAlertDialog.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author allen  
 *
 * @date 2015-2-9 下午4:46:26 
 *
 * @version V1.0   
 */
public class CreatHintAlertDialog {

	public static AlertDialog ad;

	private int title, sure;

	public CreatHintAlertDialog(int title, int sure) {
		this.title = title;
		this.sure = sure;
	}

	public AlertDialog hintDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(BAApplication.getInstance());

		LayoutInflater layoutInflater = LayoutInflater.from(BAApplication.getInstance());
		View view = layoutInflater.inflate(R.layout.dialog_view, null);

		builder.setView(view);

		ad = builder.create();
		TextView txtVeiw = (TextView) view.findViewById(R.id.msg);
		txtVeiw.setText(title);
		Button btn_Ok = (Button) view.findViewById(R.id.ok_sure);
		btn_Ok.setText(sure);
		btn_Ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});
		view.findViewById(R.id.ok_cancel).setVisibility(View.GONE);

//		ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		ad.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				ad = null;
			}
		});
		final Window w = ad.getWindow();
		final WindowManager.LayoutParams wlps = w.getAttributes();
		wlps.width = BAApplication.getInstance().getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		w.setAttributes(wlps);

		return ad;
	}

	public static void showDialog() {
		try {
			ad.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
