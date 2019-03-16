package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.show.PeipeiShowActivity;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.service.PeipeiFloatingService;

/**
 * @Title: IntoShowDialog.java 
 *
 * @Description: 5分钟没有语音提示进入秀场 
 *
 * @author allen
 *
 * @date 2015-2-8 下午3:02:26 
 *
 * @version V1.0   
 */
public class IntoShowDialog extends Dialog implements OnClickListener, OnDismissListener {

	private Activity context;
	private TextView txtVeiw;

	private BAHandler mHandler;
	private long time;
	private boolean isCheck;

	public IntoShowDialog(final Activity context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		mHandler = new BAHandler(context) {
			public void handleMessage(Message msg) {
				try {
					super.handleMessage(msg);
				} catch (NullPointerException e) {
					return;
				}

				switch (msg.what) {
				case HandlerValue.BIND_GET_PHONE:
					int index = (int) ((time - System.currentTimeMillis()) / 1000);
					if (index > 0) {
						String str = String.format(context.getString(R.string.str_show_long_time_left), index);
						txtVeiw.setText(str);
						mHandler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 1000);
					} else {
						dismiss();
					}
					break;

				default:
					break;
				}
			}
		};
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
		txtVeiw = (TextView) findViewById(R.id.msg);
		Button btn = (Button) findViewById(R.id.ok_sure);
		btn.setLayoutParams(params);
		btn.setText(R.string.str_show_into_room);
		findViewById(R.id.ok_cancel).setVisibility(View.GONE);
		btn.setOnClickListener(this);

		this.setOnDismissListener(this);
		// 点击对话框外不消失
		setCanceledOnTouchOutside(false);

		time = System.currentTimeMillis() + 10000;
		mHandler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 0);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, PeipeiShowActivity.class);
		context.startActivity(intent);
		isCheck = true;
		dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (!isCheck) {
			if (context instanceof PeipeiShowActivity) {
				context.finish();
			} else {
//				Intent intent = new Intent(context, PeipeiFloatingService.class);
//				context.stopService(intent);
			}
			BAApplication.getInstance().closeOrOutRoom(1);
			BAApplication.clearShow();
		}
	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
