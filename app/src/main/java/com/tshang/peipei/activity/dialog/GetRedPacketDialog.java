package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserSimpleInfoList;
import com.tshang.peipei.base.BaseUtils;

/**
 * 获取红包dialog
 * @author Jeff
 *
 */
public class GetRedPacketDialog extends Dialog {

	private Activity context;
	private RedPacketInfo redPaInfo;
	private boolean isGet = false;//是否领过

	public GetRedPacketDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public GetRedPacketDialog(Activity context, int theme, RedPacketInfo redPaInfo, boolean isGet) {
		super(context, theme);
		this.context = context;
		this.redPaInfo = redPaInfo;
		this.isGet = isGet;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_getredpacket);
		TextView tvMsg = (TextView) findViewById(R.id.tv_name);
		TextView tvMoney = (TextView) findViewById(R.id.tv_get_redpacket_money);
		if (redPaInfo != null) {
			//			String message = context.getString(R.string.str_congratulations_to_you);
			if (isGet) {
				tvMsg.setPadding(0, BaseUtils.dip2px(context, 10), 0, 0);
				tvMsg.setText(R.string.str_have_get_redpacket);
			} else {
				tvMsg.setText(R.string.str_congratulations_to_you);
				//				tvMsg.setText(message.replace("$", "" + new String(redPaInfo.createnick)));
			}

			UserSimpleInfoList list = redPaInfo.records;
			if (list != null && !list.isEmpty()) {
				UserSimpleInfo info = (UserSimpleInfo) list.get(0);
				if (info != null) {
					tvMoney.setText("" + info.intdata.intValue());
				}
			}
		}
	}

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

}
