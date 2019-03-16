package com.tshang.peipei.model.request;

import java.math.BigInteger;

import android.util.Log;

import com.tshang.peipei.model.ISentMessageCallBack;
import com.tshang.peipei.model.entity.ChatMessageReceiptEntity;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlChatData;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlChat;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlChat;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: 发送聊天信息接口
 *
 * @Description: 发送聊天信息接口组包、解包
 *
 * @author allen  
 *
 * @date 2014-4-10 下午1:48:43 
 *
 * @version V1.0   
 */
public class RequestSentMessage extends AsnBase implements ISocketMsgCallBack {

	private ISentMessageCallBack mSentMessageCallBack;

	private ChatMessageReceiptEntity receiptEntity;
	private ChatDatabaseEntity chatDatabaseEntity;
	private byte[] auth;

	@SuppressWarnings("unchecked")
	public void sentMessage(byte[] auth, int ver, int uid, byte[] data, int type, int lenght, int friendUid, String burnID, String nick,
			String fnick, int sex, int fsex, long msgLocalId, ChatDatabaseEntity chatDatabaseEntity, ISentMessageCallBack mainHotCallBack) {

		receiptEntity = new ChatMessageReceiptEntity();
		receiptEntity.setType(type);
		receiptEntity.setBurnId(burnID);
		receiptEntity.setLocalId(msgLocalId);
		receiptEntity.setfUid(friendUid);

		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGoGirlChat req = new ReqGoGirlChat(); 
		req.fromuid = BigInteger.valueOf(uid);
		req.fromtype = BigInteger.valueOf(0);
		req.totype = BigInteger.valueOf(0);
		req.touid = BigInteger.valueOf(friendUid);

		GoGirlChatData chatdata = new GoGirlChatData();
		chatdata.from = BigInteger.valueOf(uid);
		chatdata.fromtype = BigInteger.valueOf(0);//0代表私聊
		chatdata.fromnick = nick.getBytes();
		chatdata.fromsex = BigInteger.valueOf(sex);
		chatdata.totype = BigInteger.valueOf(0);
		chatdata.to = BigInteger.valueOf(friendUid);
		chatdata.tonick = fnick.getBytes();
		chatdata.tosex = BigInteger.valueOf(fsex);
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

		GoGirlDataInfoList datalist = new GoGirlDataInfoList();
		GoGirlDataInfo datainfo = new GoGirlDataInfo();
		datainfo.data = data;
		datainfo.type = BigInteger.valueOf(type);//消息类型
		datainfo.datainfo = BigInteger.valueOf(lenght);
		datainfo.dataid = burnID.getBytes();
		datainfo.revint0 = BigInteger.ZERO;
		datainfo.revint1 = BigInteger.ZERO;
		datainfo.revstr0 = "".getBytes();
		datainfo.revstr1 = "".getBytes();

		datalist.add(datainfo);
		chatdata.chatdatalist = datalist;

		req.chatdata = chatdata;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLCHAT_CID;
		goGirlPkt.reqgogirlchat = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		this.auth = auth;
		this.chatDatabaseEntity = chatDatabaseEntity;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mSentMessageCallBack = mainHotCallBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		//		System.out.println("最直接的=====111====");
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mSentMessageCallBack) {
			RspGoGirlChat rspGoGirlChat = pkt.rspgogirlchat;
			if (checkRetCode(rspGoGirlChat.retcode.intValue())) {
				Log.d("Aaron", "succuess code===" + rspGoGirlChat.retcode.intValue() + ",msg" + new String(rspGoGirlChat.retmsg));
				receiptEntity.setTime(rspGoGirlChat.chattime.longValue() * 1000);
				mSentMessageCallBack.sentMessageCallBack(auth, rspGoGirlChat.retcode.intValue(), receiptEntity, chatDatabaseEntity);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		Log.e("Aaron", "error code=="+resultCode);
		//		System.out.println("最直接的========="+resultCode+"=="+mSentMessageCallBack);
		if (null != mSentMessageCallBack) {
			receiptEntity.setTime(System.currentTimeMillis());
			mSentMessageCallBack.sentMessageCallBack(auth, resultCode, receiptEntity, chatDatabaseEntity);
		}
	}
}
