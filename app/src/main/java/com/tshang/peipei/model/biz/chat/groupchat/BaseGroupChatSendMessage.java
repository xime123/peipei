package com.tshang.peipei.model.biz.chat.groupchat;

import java.math.BigInteger;
import java.util.Random;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.chat.BaseChatSendMessage;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.request.RequestGoGirlGroupChat.IGroupChat;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlChatData;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: BaseSendMessage.java 
 *
 * @Description: 发送消息基类
 *
 * @author Jeff  
 *
 * @date 2014年9月24日 下午3:28:35 
 *
 * @version V1.3.0   
 */
public class BaseGroupChatSendMessage extends BaseChatSendMessage implements IGroupChat {

	/**
	 * 
	 * @author Jeff
	 *
	 * @param data 发送消息数据
	 * @param fromtype 发送消息type
	 * @return
	 */
	protected GoGirlDataInfoList getGoGirlDataInfoList(byte[] data, int fromtype) {
		return getGoGirlDataInfoList(data, fromtype, 0);
	}

	protected GoGirlDataInfoList getGoGirlDataInfoList(byte[] data, int fromtype, int timeLen) {

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

	/**
	 * 
	 * @author Jeff
	 *
	 * @param info 发送消息用户
	 * @param fromtype
	 * @param touid 发送给谁uid
	 * @param toName 发送给谁名字
	 * @param toType 0说明是私聊，1是群聊
	 * @return
	 */
	protected GoGirlChatData getGoGirlChatData(GoGirlUserInfo info, int toType, int tosex, int fromtype, int touid, String toName) {
		if (TextUtils.isEmpty(toName)) {
			toName = "";
		}
		GoGirlChatData chatdata = new GoGirlChatData();//拼接接口数据
		chatdata.from = info.uid;
		chatdata.fromtype = BigInteger.valueOf(fromtype);
		chatdata.fromnick = info.nick;
		chatdata.fromsex = info.sex;
		chatdata.totype = BigInteger.valueOf(toType);
		chatdata.to = BigInteger.valueOf(touid);
		chatdata.tonick = toName.getBytes();
		chatdata.tosex = BigInteger.valueOf(tosex);
		chatdata.createtimes = BigInteger.ZERO;
		chatdata.createtimeus = BigInteger.ZERO;
		chatdata.revint0 = BigInteger.ZERO;
		chatdata.revint1 = BigInteger.ZERO;
		chatdata.revint2 = BigInteger.ZERO;
		chatdata.revint3 = BigInteger.valueOf(new Random().nextInt(11));
		chatdata.revstr0 = "".getBytes();
		chatdata.revstr1 = "".getBytes();
		chatdata.revstr2 = "".getBytes();
		chatdata.revstr3 = "".getBytes();
		return chatdata;
	}

	/**
	 * 组装群消息数据
	 * @author Jeff
	 *
	 * @param info
	 * @param fromtype
	 * @param touid
	 * @param toName
	 * @return
	 */
	protected GoGirlChatData getGoGirlChatData(GoGirlUserInfo info, int fromtype, int touid, String toName) {
		return getGoGirlChatData(info, 1, 0, fromtype, touid, toName);
	}

	@Override
	public void callBackGroupChat(int retCode, long chatTime, int touid, ChatDatabaseEntity chatDatabaseEntity) {//群消息发送数据网络返回
		if (chatDatabaseEntity == null) {
			return;
		}
		if (handler == null) {
			return;
		}
		if (retCode == 0) {
			chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());
			chatDatabaseEntity.setCreateTime(chatTime * 1000);
		} else {
			chatDatabaseEntity.setStatus(ChatStatus.FAILED.getValue());
		}
		if (activiy != null && BAApplication.mLocalUserInfo != null) {
			ChatManageBiz.getInManage(activiy).changeMessageStatusByLocalID(activiy, touid, chatDatabaseEntity.getStatus(),
					chatDatabaseEntity.getMesLocalID(), chatDatabaseEntity.getCreateTime(), true);
		}
		Message msg = handler.obtainMessage();
		msg.what = HandlerValue.CHAT_REFLESH_UI_VALUE;
		msg.arg1 = retCode;
		handler.sendMessage(msg);
	}

}
