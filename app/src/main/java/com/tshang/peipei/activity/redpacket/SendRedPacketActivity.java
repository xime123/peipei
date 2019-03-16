package com.tshang.peipei.activity.redpacket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.SendRedPacketDialog;
import com.tshang.peipei.activity.dialog.SendRedPacketDialog.ISendRedPacketSuccessCallBack;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.biz.chat.groupchat.SendGroupChatRedPacketMessage;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: 发送红包页面
 *
 * @author Jeff
 *
 * @version V1.4.0   
 */
public class SendRedPacketActivity extends BaseActivity implements ISendRedPacketSuccessCallBack {
	public static final String STR_GROUP_ID = "str_group_id";
	public static final String STR_GROUP_NAME = "str_group_name";
	public static final String STR_RETURN_MESSAGE_ID = "str_return_message_id";
	private int groupid = -1;
	private String groupName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_send_packet:
			if (groupid < 0) {
				BaseUtils.showTost(this, "群错误");
				return;
			}
			new SendRedPacketDialog(this, android.R.style.Theme_Translucent_NoTitleBar, groupid, this).showDialog();
			break;
		case R.id.tv_receiver_red_packet:
			Bundle bundle1 = new Bundle();
			bundle1.putBoolean(RedPacketReceiverAndDeliverActivity.DELIVER_OR_RECEIVER, false);
			BaseUtils.openActivity(this, RedPacketReceiverAndDeliverActivity.class, bundle1);
			break;
		case R.id.tv_send_red_packet:
			Bundle bundle = new Bundle();
			bundle.putBoolean(RedPacketReceiverAndDeliverActivity.DELIVER_OR_RECEIVER, true);
			BaseUtils.openActivity(this, RedPacketReceiverAndDeliverActivity.class, bundle);
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			groupid = bundle.getInt(STR_GROUP_ID, -1);
			groupName = bundle.getString(STR_GROUP_NAME);
		}
	}

	@Override
	protected void initRecourse() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_send_red_packet);

		findViewById(R.id.btn_send_packet).setOnClickListener(this);
		findViewById(R.id.tv_receiver_red_packet).setOnClickListener(this);
		findViewById(R.id.tv_send_red_packet).setOnClickListener(this);

	}

	@Override
	protected int initView() {
		return R.layout.activity_redpacket_creat;
	}

	@Override
	public void sendRedPacketSuccess(RedPacketInfo info) {
		ChatDatabaseEntity entity = SendGroupChatRedPacketMessage.sendRedPacketMsg(this, info, groupid, groupName);
		if (entity != null) {
			long messageId = entity.getMesLocalID();
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(STR_RETURN_MESSAGE_ID, messageId);
			setResult(RESULT_OK, intent);
		}
		this.finish();
	}

}
