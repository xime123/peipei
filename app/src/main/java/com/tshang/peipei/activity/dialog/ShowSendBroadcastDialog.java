package com.tshang.peipei.activity.dialog;

import java.math.BigInteger;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.DEREncoder;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.mine.MineWriteBroadCastActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.broadcast.WriteBroadCastBiz;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ShowShareBroadcastInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.view.MySlipSwitchTwo;
import com.tshang.peipei.view.MySlipSwitchTwo.OnSwitchTwoListener;

/**
 * @Title: ShowSendBroadcastDialog.java 
 *
 * @Description: 秀场内发广播对话框
 *
 * @author allen  
 *
 * @date 2015-3-9 下午1:55:10 
 *
 * @version V1.0   
 */
public class ShowSendBroadcastDialog extends Dialog implements OnClickListener, OnSwitchTwoListener, OnDismissListener {

	private Activity context;
	private int title;
	private int sure;
	private int cancel;
	private TextView tvmoney;
	private TextView tvNum;
	private EditText edtContent;
	private boolean canSend = true;
	private WriteBroadCastBiz writeBiz;
	InputMethodManager mInput;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerValue.BROADCAST_SEND_SUCCESS_VALUE:
				if (msg.arg1 == 0) {
					BaseUtils.showTost(context, "发送成功");
					SharedPreferencesTools.getInstance(context).saveLongKeyValue(System.currentTimeMillis(), BAConstants.SHOW_BROADCAST);
				} else if (msg.arg1 == rspContMsgType.E_GG_NOT_ENGOUH_WELTH) {//财富不够
					new participatePromptDialog(context, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
				} else if (msg.arg1 == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够
					new participatePromptDialog(context, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
				}
				ShowSendBroadcastDialog.this.dismiss();
				break;

			default:
				break;
			}

		}

	};

	public ShowSendBroadcastDialog(Activity context, int title, int sure, int cancel) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.title = title;
		this.sure = sure;
		this.cancel = cancel;
		writeBiz = new WriteBroadCastBiz(context, mHandler);
		writeBiz.getWealth();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_show_broadcast_cancel:
			dismiss();
			break;
		case R.id.tv_show_broadcast_public:
			long time = SharedPreferencesTools.getInstance(context).getLongKeyValue(BAConstants.SHOW_BROADCAST);
			if (System.currentTimeMillis() - time > 30000) {
				if (canSend) {
					String text = edtContent.getText().toString();
					if (TextUtils.isEmpty(text)) {
						text = edtContent.getHint().toString();
					}
					if (TextUtils.isEmpty(text)) {
						BaseUtils.showTost(context, "请输入分享内容");
						return;
					}
					if (BAApplication.showRoomInfo != null) {
						ShowShareBroadcastInfo info = new ShowShareBroadcastInfo();
						info.ownernick = BAApplication.showRoomInfo.getOwneruserinfo().getNick().toByteArray();
						info.owneruid = BigInteger.valueOf(BAApplication.showRoomInfo.getOwneruserinfo().getUid());
						info.txt = text.getBytes();
						info.roomid = BigInteger.valueOf(BAApplication.showRoomInfo.getRoomid());
						info.revint0 = BigInteger.valueOf(0);
						info.revint1 = BigInteger.valueOf(0);
						info.revint2 = BigInteger.valueOf(0);
						info.revint3 = BigInteger.valueOf(0);
						info.revint4 = BigInteger.valueOf(0);
						info.revstr0 = "".getBytes();
						info.revstr1 = "".getBytes();
						info.revstr2 = "".getBytes();
						info.revstr3 = "".getBytes();
						info.revstr4 = "".getBytes();
						DEREncoder edc = new DEREncoder();
						try {
							info.encode(edc);
							byte[] data = edc.toByteArray();

							writeBiz.setDecree(false);
							writeBiz.setType(WriteBroadCastBiz.BROADCAST_TEXT_TYPE);
							writeBiz.sendTextBroadCast(new ArrayList<GoGirlUserInfo>(), data, MineWriteBroadCastActivity.BROADCAST_TEXT_COLOR_BLACK,
									true);
						} catch (ASN1Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					BaseUtils.showTost(context, R.string.str_long_lenth);
				}
			}else{
				BaseUtils.showTost(context, "亲，不要刷屏哦");
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.dialog_show_broadcast);
			TextView btn_Ok = (TextView) findViewById(R.id.tv_show_broadcast_public);
			TextView btn_Can = (TextView) findViewById(R.id.tv_show_broadcast_cancel);
			if (sure != 0) {
				btn_Ok.setText(sure);
			}
			if (cancel != 0) {
				btn_Can.setText(cancel);
			}

			mInput = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

			btn_Ok.setOnClickListener(this);
			btn_Can.setOnClickListener(this);
			setCanceledOnTouchOutside(true);

			MySlipSwitchTwo mssTwo = (MySlipSwitchTwo) findViewById(R.id.myslipswitch_show_broadcast);
			mssTwo.setImageResource(R.drawable.broadcast_voice1_switch_bg, R.drawable.broadcast_voice_switch_bg);
			mssTwo.setSwitchState(false);
			mssTwo.setOnSwitchListener(this);
			this.setOnDismissListener(this);
			tvmoney = (TextView) findViewById(R.id.tv_show_broadcast_money);
			tvNum = (TextView) findViewById(R.id.tv_show_broadcast_num);
			edtContent = (EditText) findViewById(R.id.tv_show_broadcast_content);
			edtContent.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String str = s.toString();
					if (str.length() <= 40) {
						canSend = true;
					} else {
						canSend = false;
					}
					tvNum.setText(str.length() + "/40");
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

			final Window w = getWindow();
			final WindowManager.LayoutParams wlps = w.getAttributes();
			wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
			wlps.height = WindowManager.LayoutParams.MATCH_PARENT;
			wlps.dimAmount = 0.6f;
			wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
			wlps.gravity = Gravity.CENTER;
			w.setAttributes(wlps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSwitched(boolean isSwitchOn) {
		if (!isSwitchOn) {
			writeBiz.setBroadcastType(WriteBroadCastBiz.COMMON_BROADCAST);
			tvmoney.setText("-50银币");
			tvmoney.setTextColor(context.getResources().getColor(R.color.show_broadcast_content_num_color));
		} else {
			writeBiz.setBroadcastType(WriteBroadCastBiz.TOP_BRAODCAST);

			tvmoney.setText("-" + SharedPreferencesTools.getInstance(context).getIntValueByKey(BAConstants.PEIPEI_APP_CONFIG_TOP_TEXT_BROADCAST_COIN)
					+ "金币");
			tvmoney.setTextColor(context.getResources().getColor(R.color.orange));
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(context.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
