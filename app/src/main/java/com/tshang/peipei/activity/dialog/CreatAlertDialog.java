package com.tshang.peipei.activity.dialog;

import java.util.HashMap;

import android.app.Activity;
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
public class CreatAlertDialog {

	public static AlertDialog isFinishShowDialog(Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		View view = layoutInflater.inflate(R.layout.dialog_view, null);

		builder.setView(view);

		final AlertDialog ad = builder.create();
		TextView tvMsg = (TextView) view.findViewById(R.id.msg);
		tvMsg.setText(R.string.str_show_room_is_finish);
		Button btn_Ok = (Button) view.findViewById(R.id.ok_sure);
		Button btn_Can = (Button) view.findViewById(R.id.ok_cancel);
		btn_Ok.setText(R.string.ok);
		btn_Can.setText(R.string.cancel);
		btn_Ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BAApplication.mLocalUserInfo != null) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("uid", BAApplication.mLocalUserInfo.uid.intValue() + "");
//					MobclickAgent.onEvent(BAApplication.getInstance(), "dianjichanganguanbi", map);
				}

				BAApplication.getInstance().closeOrOutRoom(0);
				BAApplication.clearShow();
				ad.dismiss();
			}
		});

		btn_Can.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});

//		ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		ad.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (BAApplication.showRoomInfo != null) {
//					Intent intent = new Intent(BAApplication.getInstance(), PeipeiFloatingService.class);
//					BAApplication.getInstance().startService(intent);
				}
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
		return ad;
	}

	public static void showIsFinishDialog(Activity activity) {
		try {
			isFinishShowDialog(activity).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
