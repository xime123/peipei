package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqPlayFingerGuessingWithAnteV2;
import com.tshang.peipei.protocol.asn.gogirl.RspPlayFingerGuessingWithAnteV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: RequestPlayFingerGuessingWithAnte.java 
 *
 * @Description: 猜拳接口
 *
 * @author Jeff 
 *
 * @date 2014-11-06 下午2:20:58 
 *
 * @version V1.4.0   
 */
public class RequestPlayFingerGuessingWithAnte extends AsnBase implements ISocketMsgCallBack {

	private iPlayFingerGuessing callback;
	private ChatDatabaseEntity chatentity;//收到的猜拳数据或者第一次发送的猜拳数据
	private boolean isGroupGuess = false;
	private int peeruid = 0;//对方的uid
	private String peernick;//对方的昵称
	private int peersex;//对方的性别
	private int antetype;//猜拳类别,金币银币

	public void playFingerGuessingWithAnte(byte[] auth, int ver, int selfuid, int peeruid, int ante, int finger, String guessingstrid,
			iPlayFingerGuessing callback, ChatDatabaseEntity chatentity, boolean isGroupGuess, String nick, int sex, int antetype) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqPlayFingerGuessingWithAnteV2 req = new ReqPlayFingerGuessingWithAnteV2();
		req.selfuid = BigInteger.valueOf(selfuid);
		req.peeruid = BigInteger.valueOf(peeruid);
		req.guessingstrid = guessingstrid.getBytes();
		req.ante = BigInteger.valueOf(ante);
		req.finger = BigInteger.valueOf(finger);
		req.antetype = BigInteger.valueOf(antetype);

		//整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPLAYFINGERGUESSINGWITHANTEV2_CID;
		goGirlPkt.reqplayfingerguessingwithantev2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;

		byte[] msg = encode(ydmxMsg);// 编码
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);

		this.callback = callback;
		this.chatentity = chatentity;
		this.isGroupGuess = isGroupGuess;
		this.peeruid = peeruid;
		this.peernick = nick;
		this.peersex = sex;
		this.antetype = antetype;

		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (callback != null) {
			RspPlayFingerGuessingWithAnteV2 rsp = pkt.rspplayfingerguessingwithantev2;
			callback.playFingerBack(rsp.retcode.intValue(), peeruid, isGroupGuess, chatentity, rsp.fingerguessing, peernick, peersex, antetype);
		}
	}

	@Override
	public void error(int resultCode) {
		if (callback != null) {
			callback.playFingerBack(resultCode, peeruid, isGroupGuess, chatentity, null, peernick, peersex, antetype);
		}
	}

	public interface iPlayFingerGuessing {
		public void playFingerBack(int retcode, int uid, boolean isGroupGuess, ChatDatabaseEntity chatentity, FingerGuessingInfo info, String nick,
				int sex, int antetype);
	}

}
