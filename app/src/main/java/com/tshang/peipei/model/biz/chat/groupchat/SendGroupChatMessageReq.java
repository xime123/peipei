package com.tshang.peipei.model.biz.chat.groupchat;

import java.math.BigInteger;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.request.RequestGoGirlGroupChat.IGroupChat;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlChatData;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: GroupChatUtils.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 群聊发送消息
 *
 * @author Jeff  
 *
 * @date 2014年9月22日 下午2:09:20 
 *
 * @version V1.3.0   
 */
public class SendGroupChatMessageReq implements IGroupChat {

	private static SendGroupChatMessageReq instance = null;
	private BAHandler handler;
	private Activity activiy;

	public static SendGroupChatMessageReq getInstance() {
		if (instance == null) {
			synchronized (SendGroupChatMessageReq.class) {
				if (instance == null) {
					instance = new SendGroupChatMessageReq();
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param info 发送消息用户
	 * @param fromtype
	 * @param groupid 群id
	 * @param groupName 群名字
	 * @return
	 */
	private GoGirlChatData getGoGirlChatData(GoGirlUserInfo info, int fromtype, int groupid, String groupName) {
		if (TextUtils.isEmpty(groupName)) {
			groupName = "";
		}
		GoGirlChatData chatdata = new GoGirlChatData();//拼接接口数据
		chatdata.from = info.uid;
		chatdata.fromtype = BigInteger.valueOf(fromtype);
		chatdata.fromnick = info.nick;
		chatdata.fromsex = info.sex;
		chatdata.totype = BigInteger.valueOf(1);
		chatdata.to = BigInteger.valueOf(groupid);
		chatdata.tonick = groupName.getBytes();
		chatdata.tosex = BigInteger.valueOf(0);
		chatdata.createtimes = BigInteger.ZERO;
		chatdata.createtimeus = BigInteger.ZERO;
		chatdata.revint0 = BigInteger.ZERO;
		chatdata.revint1 = BigInteger.ZERO;
		chatdata.revint2 = BigInteger.ZERO;
		chatdata.revint3 = BigInteger.ZERO;
		chatdata.revstr0 = "".getBytes();
		chatdata.revstr1 = "".getBytes();
		chatdata.revstr2 = "".getBytes();
		chatdata.revstr3 = "".getBytes();
		return chatdata;
	}

	private GoGirlDataInfoList getGoGirlDataInfoList(byte[] data, int fromtype, int timeLen) {

		GoGirlDataInfoList datalist = new GoGirlDataInfoList();
		if (data != null) {
			GoGirlDataInfo datainfo = new GoGirlDataInfo();
			datainfo.data = data;
			datainfo.type = BigInteger.valueOf(fromtype);
			datainfo.datainfo = BigInteger.valueOf(timeLen);
			datainfo.dataid = "0".getBytes();
			datainfo.revint0 = BigInteger.ZERO;
			datainfo.revint1 = BigInteger.ZERO;
			datainfo.revstr0 = "".getBytes();
			datainfo.revstr1 = "".getBytes();

			datalist.add(datainfo);
		}
		return datalist;
	}

	@Override
	public void callBackGroupChat(int retCode, long chatTime, int groupid, ChatDatabaseEntity chatDatabaseEntity) {
		if (chatDatabaseEntity == null) {
			return;
		}
		if (retCode == 0) {
			chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());
			chatDatabaseEntity.setCreateTime(chatTime * 1000);
		} else {
			chatDatabaseEntity.setStatus(ChatStatus.FAILED.getValue());
		}
		if (activiy != null && BAApplication.mLocalUserInfo != null) {
			ChatManageBiz.getInManage(activiy).changeMessageStatusByLocalID(activiy, groupid, chatDatabaseEntity.getStatus(),
					chatDatabaseEntity.getMesLocalID(), chatDatabaseEntity.getCreateTime(), true);
		}
		Message msg = handler.obtainMessage();
		msg.what = HandlerValue.CHAT_REFLESH_UI_VALUE;
		msg.arg1 = retCode;
		handler.sendMessage(msg);

	}
}
