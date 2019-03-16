package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetDistance;
import com.tshang.peipei.protocol.asn.gogirl.RspGetDistance;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: GetDistance.java 
 *
 * @Description: 获取两个人之间的距离
 *
 * @author jeff 
 *
 * @date 2015-03-06 上午10:59:54 
 *
 * @version V2.1.0   
 */
public class RequestGetDistance extends AsnBase implements ISocketMsgCallBack {

	private IGetDistance addblack;

	public void getDistance(byte[] auth, int ver, int uid, int peeruid, IGetDistance callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetDistance req = new ReqGetDistance();
		req.selfuid = BigInteger.valueOf(uid);
		req.peeruid = BigInteger.valueOf(peeruid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETDISTANCE_CID;
		goGirlPkt.reqgetdistance = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.addblack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != addblack) {
			RspGetDistance rsp = pkt.rspgetdistance;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.getDistance(retCode, rsp.dis.intValue());
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.getDistance(resultCode, -1);
		}

	}

	public interface IGetDistance {
		public void getDistance(int retCode, int dis);
	}

}
