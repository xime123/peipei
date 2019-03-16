package com.tshang.peipei.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.broadcast.WriteBroadCastBiz;
import com.tshang.peipei.model.redpacket.RedPacketCreate;

/**
 * 发送红包dialog
 * @author Jeff
 *
 */
public class SendRedPacketDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener, TextWatcher {

	private Activity context;
	private EditText edt_count;
	private EditText edt_coin;
	private EditText edt_desc;
	private TextView tv_have_total_coin;
	private TextView tv_send_total_coin;
	private int totalCoin = 0;

	private BAHandler mHandler;
	private int groupid = -1;

	public SendRedPacketDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public SendRedPacketDialog(Activity context, int theme, int groupid, final ISendRedPacketSuccessCallBack callback) {
		super(context, theme);
		this.context = context;
		this.groupid = groupid;
		mHandler = new BAHandler(context) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HandlerValue.RED_PACKET_CREATE_SUCCESS_VALUE:
					SendRedPacketDialog.this.dismiss();
					if (callback != null) {
						callback.sendRedPacketSuccess((RedPacketInfo) msg.obj);
					}
					break;
				case HandlerValue.RED_PACKET_CREATE_FAILED_VALUE:
					BaseUtils.cancelDialog();
					BaseUtils.showTost(SendRedPacketDialog.this.context, "创建失败，请检查网络是否连接正常");
					break;
				case HandlerValue.RED_PACKET_CREATE_OUT_OF_NUM_VALUE:
					BaseUtils.cancelDialog();
					BaseUtils.showTost(SendRedPacketDialog.this.context, "红包数量已超过后宫人数");
					break;
				case HandlerValue.RED_PACKET_GET_TOTAL_COIN_VALUE:
					BaseUtils.cancelDialog();
					totalCoin = (Integer) msg.obj;
					tv_have_total_coin.setText(String.valueOf(totalCoin));
					break;
				case HandlerValue.RED_PACKET_ABOVE_NORM:
					BaseUtils.cancelDialog();
					BaseUtils.showTost(SendRedPacketDialog.this.context, "发红包超过限额");
					break;

				default:
					break;
				}

			}
		};
		if (BAApplication.mLocalUserInfo != null)
			new WriteBroadCastBiz(context, mHandler).getWealth();//获取财富值

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_create_red_packet);

		findViewById(R.id.btn_charity).setOnClickListener(this);
		findViewById(R.id.btn_send_envelope_cancel).setOnClickListener(this);
		edt_count = (EditText) findViewById(R.id.edt_send_red_envelope_count);
		edt_coin = (EditText) findViewById(R.id.edt_send_red_envelope_money);
		edt_desc = (EditText) findViewById(R.id.edt_send_red_envelope_describe);
		tv_have_total_coin = (TextView) findViewById(R.id.tv_account_coin);
		tv_send_total_coin = (TextView) findViewById(R.id.tv_send_total_money);
		findViewById(R.id.ll_send_redpacket_dialog).setOnClickListener(this);
		edt_coin.addTextChangedListener(this);
	}

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(false);
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

	@Override
	public void onDismiss(DialogInterface dialog) {}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_send_redpacket_dialog:
			BaseUtils.hideKeyboard(context, edt_coin);
			break;
		case R.id.btn_send_envelope_cancel:
			this.dismiss();
			break;
		case R.id.btn_charity://恩赐
			String redPacketCount = edt_count.getText().toString();
			if (TextUtils.isEmpty(redPacketCount)) {
				BaseUtils.showTost(context, "请输入发送红包的数量");
				return;
			}
			String redPacketTotalCoin = edt_coin.getText().toString();
			if (TextUtils.isEmpty(redPacketTotalCoin)) {
				BaseUtils.showTost(context, "请输入发送红包的总金额");
				return;
			}
			String desc = edt_desc.getText().toString();
			if (TextUtils.isEmpty(desc)) {
				desc = edt_desc.getHint().toString();
			}
			if (TextUtils.isEmpty(desc)) {
				desc = "恭喜发财~";
			}
			try {
				int totalgoldcoin = Integer.parseInt(redPacketTotalCoin);
				if (totalgoldcoin < 1) {
					BaseUtils.showTost(context, "请输入发送红包的总金额");
					return;
				}
				int portionnum = Integer.parseInt(redPacketCount);
				if (portionnum < 1) {
					BaseUtils.showTost(context, "请输入发送红包的数量");
					return;
				}
				if (totalCoin < totalgoldcoin) {
					BaseUtils.showTost(context, "余额不足");
					return;
				}
				RedPacketCreate.getInstance().reqCreateRedPacket(context, groupid, totalgoldcoin, portionnum, desc, mHandler);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		tv_send_total_coin.setText(s);
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	public interface ISendRedPacketSuccessCallBack {
		public void sendRedPacketSuccess(RedPacketInfo info);
	}
}
