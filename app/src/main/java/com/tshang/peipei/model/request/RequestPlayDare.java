package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tencent.mm.sdk.platformtools.Log;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqPlayDare;
import com.tshang.peipei.protocol.asn.gogirl.RspPlayDare;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 发起大冒险
 *
 * @author allen  
 *
 * @date 2015-6-15 上午10:59:54 
 *
 * @version V1.0   
 */
public class RequestPlayDare extends AsnBase implements ISocketMsgCallBack {

	private IPlayDare callback;
	private String dareid;

	public void playdare(byte[] auth, int ver, int uid, int groupid, String dareid, IPlayDare callback) {
		this.dareid = dareid;
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqPlayDare req = new ReqPlayDare();
		req.selfuid = BigInteger.valueOf(uid);
		req.groupid = BigInteger.valueOf(groupid);
		req.dareid = dareid.getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPLAYDARE_CID;
		goGirlPkt.reqplaydare = req;
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
			RspPlayDare rsp = pkt.rspplaydare;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultPlayDare(retCode, dareid);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultPlayDare(resultCode, dareid);
		}
	}

	public interface IPlayDare {
		public void resultPlayDare(int retCode, String dareid);
	}

}
