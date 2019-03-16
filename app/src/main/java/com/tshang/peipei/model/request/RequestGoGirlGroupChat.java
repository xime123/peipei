package com.tshang.peipei.model.request;

import java.math.BigInteger;
import java.util.Random;

import android.util.Log;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlChatData;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlGroupChat;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlGroupChat;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: RequestGoGirlGroupChat.java 
 *
 * @Description: 发送群聊信息
 *
 * @author Jeff  
 *
 * @date 2014-9-22 上午9:59:25 
 *
 * @version V1.3.0   
 */
public class RequestGoGirlGroupChat extends AsnBase implements ISocketMsgCallBack {

	private IGroupChat addblack;
	private ChatDatabaseEntity chatDatabaseEntity;
	private int groupid;

	public void reqSendGroupChat(byte[] auth, int ver, int uid, int groupid, GoGirlChatData chatData, ChatDatabaseEntity chatDatabaseEntity,
			IGroupChat callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGoGirlGroupChat req = new ReqGoGirlGroupChat();
		req.chatdata = chatData;
		req.fromuid = BigInteger.valueOf(uid);
		req.togroupid = BigInteger.valueOf(groupid);

		

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLGROUPCHAT_CID;
		goGirlPkt.reqgogirlgroupchat = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.addblack = callback;
		this.chatDatabaseEntity = chatDatabaseEntity;
		this.groupid = groupid;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != addblack) {
			RspGoGirlGroupChat rsp = pkt.rspgogirlgroupchat;
			int retCode = rsp.retcode.intValue();
			long chatTime = rsp.chattime.longValue();
			if (checkRetCode(retCode))
				addblack.callBackGroupChat(retCode, chatTime, groupid, chatDatabaseEntity);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.callBackGroupChat(resultCode, 0, groupid, chatDatabaseEntity);
		}

	}

	public interface IGroupChat {
		public void callBackGroupChat(int retCode, long chatTime, int groupid, ChatDatabaseEntity chatDatabaseEntity);
	}

}
