package com.tshang.peipei.activity.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.tshang.peipei.activity.show.PeipeiShowActivity;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.service.PeipeiFloatingService;

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
public class CreatIntoAlertDialog {

	private TextView txtVeiw;
	public static AlertDialog ad;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerValue.BIND_GET_PHONE:
				int index = (int) ((time - System.currentTimeMillis()) / 1000);
				if (index > 0) {
					String str = String.format(BAApplication.getInstance().getString(R.string.str_show_long_time_left), index);
					txtVeiw.setText(str);
					handler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 1000);
				} else {
					if (ad != null) {
						ad.dismiss();
					}
				}
				break;

			default:
				break;
			}
		}
	};
	private long time;
	private boolean isCheck;

	public AlertDialog intoShowDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(BAApplication.getInstance());

		LayoutInflater layoutInflater = LayoutInflater.from(BAApplication.getInstance());
		View view = layoutInflater.inflate(R.layout.dialog_view, null);

		builder.setView(view);

		ad = builder.create();
		txtVeiw = (TextView) view.findViewById(R.id.msg);
		Button btn_Ok = (Button) view.findViewById(R.id.ok_sure);
		btn_Ok.setText(R.string.str_show_into_room);
		btn_Ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BAApplication.getInstance(), PeipeiShowActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				BAApplication.getInstance().startActivity(intent);
				isCheck = true;
				ad.dismiss();
			}
		});
		view.findViewById(R.id.ok_cancel).setVisibility(View.GONE);

//		ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		ad.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (!isCheck) {
					BAApplication.getInstance().exitShowActivity();
//					Intent intent = new Intent(BAApplication.getInstance(), PeipeiFloatingService.class);
//					BAApplication.getInstance().stopService(intent);

					BAApplication.getInstance().closeOrOutRoom(1);
					BAApplication.clearShow();
				}
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

		ad.setCanceledOnTouchOutside(false); //点击外面区域不会让dialog消失
		time = System.currentTimeMillis() + 10000;
		handler.sendEmptyMessageDelayed(HandlerValue.BIND_GET_PHONE, 0);
		return ad;
	}

	public static void showIntoDialog() {
		try {
			ad.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
