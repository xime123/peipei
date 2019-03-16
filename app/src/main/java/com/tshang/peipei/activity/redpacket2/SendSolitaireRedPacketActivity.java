package com.tshang.peipei.activity.redpacket2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.chat.ChatActivity;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.participatePromptDialog;
import com.tshang.peipei.activity.redpacket2.adapter.TipsAdapter;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.groupchat.SendGroupChatRedPacketMessage;
import com.tshang.peipei.model.redpacket2.SolitaireRedPacketBiz;
import com.tshang.peipei.model.request.RequestSendSolitaireRedpacket.GetSendSolitaireRedpacketCallBack;
import com.tshang.peipei.model.request.RequestSolitaireRedPacketMoney.GetSolitaireRedPacketCallBack;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfoList;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.view.PeiPeiListView;
import com.tshang.peipei.view.RedPacketCheckButton;

/**
 * @Title: SendSolitaireRedPacketActivity.java 
 *
 * @Description: 发送接龙红包界面
 *
 * @author DYH  
 *
 * @date 2015-12-8 下午6:26:03 
 *
 * @version V1.0   
 */
public class SendSolitaireRedPacketActivity extends BaseActivity implements GetSolitaireRedPacketCallBack, GetSendSolitaireRedpacketCallBack {
	public static final String STR_GROUP_ID = "str_group_id";
	public static final String STR_GROUP_NAME = "str_group_name";
	public static final String STR_RETURN_MESSAGE_ID = "str_return_message_id";
	
	private RedPacketCheckButton redpacket_money1;
	private RedPacketCheckButton redpacket_money2;
	private RedPacketCheckButton redpacket_money3;
	private TextView tv_send;
	private Dialog loadDialog;
	private List<RedPacketCheckButton> views = new ArrayList<RedPacketCheckButton>();
	private PeiPeiListView tips_list;
	private ScrollView sv_scrollview;
	private TipsAdapter adapter;
	private SolitaireRedPacketBiz redPacketBiz;
	
	private List<RedPacketBetInfo> list;
	private int redId;
	private int groupid = -1;
	private String groupName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			groupid = bundle.getInt(STR_GROUP_ID, -1);
			groupName = bundle.getString(STR_GROUP_NAME);
		}
		
		redPacketBiz = new SolitaireRedPacketBiz();
		getRedPacketMoneyInfo();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		DialogFactory.dimissDialog(loadDialog);
		switch (msg.what) {
		case HandlerValue.HAREM_SOLITAIRE_REDPACKET_INFO_SUCCESS:
			if(0 == msg.arg1){
				RedPacketBetInfoList redList = (RedPacketBetInfoList) msg.obj;
				list = getSolitaireRedpacketListData(redList);
				setFirstTip(list);
				setMoney(list);
				redpacket_money1.setCheck(true);
				
			}else{
				BaseUtils.showTost(this, "获取失败");
			}
			break;
		case HandlerValue.HAREM_SOLITAIRE_REDPACKET_INFO_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		case HandlerValue.HAREM_SEND_SOLITAIRE_REDPACKET_SUCCESS:
			int retCode = msg.arg1;
			if(retCode == 0){
				RedPacketBetCreateInfo info = (RedPacketBetCreateInfo) msg.obj;
				if(info != null){
					if(info.redpacketstatus.intValue() == 0){
						saveSendSolitaireRedpacket(info);
					}else{
						BaseUtils.showTost(this, R.string.str_send_solitaire_redpacket_fail);
					}
				}
			}else if (retCode == rspContMsgType.E_GG_PROPERTY_LACK) {//财富不足
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, true, 0, 0).showDialog();
			} else if (retCode == rspContMsgType.E_GG_LACK_OF_SILVER) {//财富不够银币
				new participatePromptDialog(this, android.R.style.Theme_Translucent_NoTitleBar, false, 0, 0).showDialog();
			}
			break;
		case HandlerValue.HAREM_SEND_SOLITAIRE_REDPACKET_ERROR:
			BaseUtils.showTost(this, R.string.toast_login_failure);
			break;
		}
	}
	
	private void setFirstTip(List<RedPacketBetInfo> list){
		if(list != null && !list.isEmpty()){
			RedPacketBetInfo info = (RedPacketBetInfo) list.get(0);
			if(info != null){
				List<String> strList = divisionStr(new String(info.desc));
				setTips(strList);
			}
			
			redId = info.id.intValue();
		}
	}
	
	private void setSecondTip(List<RedPacketBetInfo> list){
		if(list != null && list.size() > 1){
			RedPacketBetInfo info = (RedPacketBetInfo) list.get(1);
			if(info != null){
				List<String> strList = divisionStr(new String(info.desc));
				setTips(strList);
			}
			redId = info.id.intValue();
		}
	}
	
	private void setThirdTip(List<RedPacketBetInfo> tipList){
		if(tipList != null && tipList.size() > 2){
			RedPacketBetInfo info = (RedPacketBetInfo) list.get(2);
			if(info != null){
				List<String> strList = divisionStr(new String(info.desc));
				setTips(strList);
			}
			redId = info.id.intValue();
		}
	}
	
	private void setTips(List<String> list){
		if(list != null){
			adapter.clearList();
			adapter.appendToList(list);
			adapter.notifyDataSetChanged();
			tips_list .setSelection(tips_list.getCount()-1);
			sv_scrollview.smoothScrollTo(0, 0);
			
		}
	}
	
	private List<String> divisionStr(String str){
		List<String> strList = new ArrayList<String>();
		if(!TextUtils.isEmpty(str) && str.contains("·")){
			String[] strArr = str.split("·");
			if(strArr != null && strArr.length > 0){
				List<String> list = Arrays.asList(strArr);
				strList.addAll(list);
			}
		}
		return strList;
	}
	
	private void setMoney(List<RedPacketBetInfo> list){
		for(int i=0; i<list.size(); i++){
			if(i<views.size()){
				views.get(i).setText(list.get(i).gold.intValue() + "");
			}
		}
	}
	
	private void getRedPacketMoneyInfo(){
		loadDialog = DialogFactory.createLoadingDialog(this);
		DialogFactory.showDialog(loadDialog);
		redPacketBiz.reqeustSolitaireRedPacketMoney(0, this);
	}
	
	private void sendRedpacket(){
		if (groupid < 0) {
			BaseUtils.showTost(this, R.string.str_group_error);
			return;
		}
		redPacketBiz.requestSendSolitaireRedPacket(redId, groupid, 0, this);
	}
	
	private ArrayList<RedPacketBetInfo> getSolitaireRedpacketListData(List<RedPacketBetInfo> list) {//去除重复的数据
		ArrayList<RedPacketBetInfo> newLists = new ArrayList<RedPacketBetInfo>();
		if (list != null && !list.isEmpty()) {
			for (RedPacketBetInfo betInfo : list) {
				newLists.add(betInfo);
			}
		}
		return newLists;
	}

	@Override
	protected void initRecourse() {
		findViewById(R.id.title_tv_left).setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.str_solitaire_redpacket);
		tips_list = (PeiPeiListView) findViewById(R.id.tips_list);
		sv_scrollview = (ScrollView) findViewById(R.id.sv_scrollview);
		redpacket_money1 = (RedPacketCheckButton) findViewById(R.id.redpacket_money1);
		redpacket_money2 = (RedPacketCheckButton) findViewById(R.id.redpacket_money2);
		redpacket_money3 = (RedPacketCheckButton) findViewById(R.id.redpacket_money3);
		tv_send = (TextView) findViewById(R.id.tv_send);
		redpacket_money1.setBackgroundRes(R.drawable.solitaire_redpacket_unselect, R.drawable.solitaire_redpacket_selected, true);
		redpacket_money2.setBackgroundRes(R.drawable.solitaire_redpacket_unselect, R.drawable.solitaire_redpacket_selected, false);
		redpacket_money3.setBackgroundRes(R.drawable.solitaire_redpacket_unselect, R.drawable.solitaire_redpacket_selected, false);
		adapter = new TipsAdapter(this);
		tips_list.setAdapter(adapter);
		views.add(redpacket_money1);
		views.add(redpacket_money2);
		views.add(redpacket_money3);
		
		setListener();
	}
	
	private void setListener(){
		tv_send.setOnClickListener(this);
		redpacket_money1.setOnClickListener(this);
		redpacket_money2.setOnClickListener(this);
		redpacket_money3.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.redpacket_money1:
			redpacket_money1.setCheck(true);
			redpacket_money2.setCheck(false);
			redpacket_money3.setCheck(false);
			setFirstTip(list);
			break;
		case R.id.redpacket_money2:
			redpacket_money1.setCheck(false);
			redpacket_money2.setCheck(true);
			redpacket_money3.setCheck(false);
			setSecondTip(list);
			break;
		case R.id.redpacket_money3:
			redpacket_money1.setCheck(false);
			redpacket_money2.setCheck(false);
			redpacket_money3.setCheck(true);
			setThirdTip(list);
			break;
		case R.id.tv_send:
			sendRedpacket();
			break;
		}
	}
	
	private void saveSendSolitaireRedpacket(RedPacketBetCreateInfo info){
		ChatDatabaseEntity entity = SendGroupChatRedPacketMessage.sendSolitaireRedPacketMsg(this, info, groupid, groupName);
		if (entity != null) {
			long messageId = entity.getMesLocalID();
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(STR_RETURN_MESSAGE_ID, messageId);
			setResult(RESULT_OK, intent);
		}
		this.finish();
	}

	@Override
	protected int initView() {
		return R.layout.activity_solitaire_redpacket_creat;
	}

	@Override
	public void onSolitaireRedPacketMoneySuccess(int code, int isOpen, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_SOLITAIRE_REDPACKET_INFO_SUCCESS, code, obj);
	}

	@Override
	public void onSolitaireRedPacketMoneyError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_SOLITAIRE_REDPACKET_INFO_ERROR, code);
	}

	@Override
	public void onSendSolitaireRedpacketSuccess(int code, Object obj) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_SEND_SOLITAIRE_REDPACKET_SUCCESS, code, obj);
	}

	@Override
	public void onSendSolitaireRedpacketError(int code) {
		sendHandlerMessage(mHandler, HandlerValue.HAREM_SEND_SOLITAIRE_REDPACKET_ERROR, code);		
	}

	@Override
	public void onSendHallSolitaireRedpacketSuccess(int code, Object obj, String errMsg) {
		
	}

}
