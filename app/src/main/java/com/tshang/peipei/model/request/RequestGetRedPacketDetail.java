package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRedPacketDetail;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRedPacketDetail;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 获取红包详情
 *
 * @author Jeff  
 *
 * @date 2014-10-16 上午10:59:54 
 *
 * @version V1.4.0   
 */
public class RequestGetRedPacketDetail extends AsnBase implements ISocketMsgCallBack {

	private IGetRedPacketDetail addblack;

	public void getRedPacketDetail(byte[] auth, int ver, int uid, int redpacketid, int redpacketuid, IGetRedPacketDetail callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRedPacketDetail req = new ReqGetRedPacketDetail();
		req.redpacketid = BigInteger.valueOf(redpacketid);
		req.redpacketuid = BigInteger.valueOf(redpacketuid);
		req.selfuid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETREDPACKETDETAIL_CID;
		goGirlPkt.reqgetredpacketdetail = req;
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
			RspGetRedPacketDetail rsp = pkt.rspgetredpacketdetail;
			int retCode = rsp.retcode.intValue();
			RedPacketInfo info = rsp.redpacket;
			if (checkRetCode(retCode))
				addblack.getRedpacketDetail(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.getRedpacketDetail(resultCode, null);
		}

	}

	public interface IGetRedPacketDetail {
		public void getRedpacketDetail(int retCode, RedPacketInfo info);
	}

}
