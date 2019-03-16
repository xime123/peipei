package com.tshang.peipei.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;

/**
 * @Title: HintToastDialog.java 
 *
 * @Description: 一个按键+提示对话框
 *
 * @author allen  
 *
 * @date 2014-7-17 上午11:54:12 
 *
 * @version V1.0   
 */
public class HintToastDialog extends Dialog implements OnClickListener {

	private int title, sure;
	private Context context;
	private HintSureOnClickListener listener;

	public HintToastDialog(Context context, int title, int sure) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.title = title;
		this.sure = sure;
		this.context = context;
	}

	public HintToastDialog(Context context, int title, int sure, HintSureOnClickListener listener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.title = title;
		this.sure = sure;
		this.context = context;
		this.listener = listener;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window w = getWindow();
		WindowManager.LayoutParams wlps = w.getAttributes();
		setContentView(R.layout.dialog_view);
		setCanceledOnTouchOutside(true);
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		w.setAttributes(wlps);
//		w.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = 20;
		params.rightMargin = 20;
		TextView txtVeiw = (TextView) findViewById(R.id.msg);
		txtVeiw.setText(title);
		Button btn = (Button) findViewById(R.id.ok_sure);
		btn.setLayoutParams(params);
		btn.setText(sure);
		findViewById(R.id.ok_cancel).setVisibility(View.GONE);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.sureOnClick();
		}
		dismiss();
	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public interface HintSureOnClickListener {
		public void sureOnClick();
	}
}
