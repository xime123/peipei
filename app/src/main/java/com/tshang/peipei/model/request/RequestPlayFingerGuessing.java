package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqPlayFingerGuessing;
import com.tshang.peipei.protocol.asn.gogirl.RspPlayFingerGuessing;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestPlayFingerGuessing.java 
 *
 * @Description: 猜拳 
 *
 * @author allen  
 *
 * @date 2014-7-10 下午6:13:19 
 *
 * @version V1.0   
 */
public class RequestPlayFingerGuessing extends AsnBase implements ISocketMsgCallBack {

	private iFinger callback;
	private int msgLocalId;

	public void palyFinger(byte[] auth, int ver, int uid, int fuid, int index, int msgLocalId, String guessid, iFinger callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqPlayFingerGuessing req = new ReqPlayFingerGuessing();
		req.selfuid = BigInteger.valueOf(uid);
		req.peeruid = BigInteger.valueOf(fuid);
		req.finger = BigInteger.valueOf(index);
		req.guessingstrid = guessid.getBytes();
		this.msgLocalId = msgLocalId;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPLAYFINGERGUESSING_CID;
		goGirlPkt.reqplayfingerguessing = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		this.callback = callback;
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		RspPlayFingerGuessing rsp = pkt.rspplayfingerguessing;

		if (null != callback) {
			if (checkRetCode(rsp.retcode.intValue())) {
				callback.finger(rsp.retcode.intValue(), rsp.fingerguessing, msgLocalId);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.finger(resultCode, null, msgLocalId);
		}
	}

	public interface iFinger {
		public void finger(int resultCode, FingerGuessingInfo finger, int msgLocalId);
	}

}
