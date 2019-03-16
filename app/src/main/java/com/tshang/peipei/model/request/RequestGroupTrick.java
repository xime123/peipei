package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGroupTrick;
import com.tshang.peipei.protocol.asn.gogirl.RspGroupTrick;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGroupTrick.java 
 *
 * @Description: 翻牌子接口
 *
 * @author allen  
 *
 * @date 2014-9-26 下午4:07:08 
 *
 * @version V1.0   
 */
public class RequestGroupTrick extends AsnBase implements ISocketMsgCallBack {

	public iGroupTrick callback;

	public void groupTrick(byte[] auth, int ver, int uid, int trickuid, int groupid, iGroupTrick callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGroupTrick req = new ReqGroupTrick();
		req.selfuid = BigInteger.valueOf(uid);
		req.trickuid = BigInteger.valueOf(trickuid);
		req.groupid = BigInteger.valueOf(groupid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGROUPTRICK_CID;
		goGirlPkt.reqgrouptrick = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callback) {
			RspGroupTrick rsp = pkt.rspgrouptrick;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.groupTrickBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.groupTrickBack(resultCode);
		}
	}

	public interface iGroupTrick {
		public void groupTrickBack(int retcode);
	}

}
