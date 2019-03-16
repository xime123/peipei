package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqUnpackRedPacket;
import com.tshang.peipei.protocol.asn.gogirl.RspUnpackRedPacket;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 发红包
 *
 * @author Jeff  
 *
 * @date 2014-10-16 上午10:59:54 
 *
 * @version V1.4.0   
 */
public class RequestUnpackRedPacket extends AsnBase implements ISocketMsgCallBack {

	private IUnpackredpacket addblack;

	public void unpackRedPacket(byte[] auth, int ver, int uid, int redpacketid, int redpacketuid, IUnpackredpacket callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqUnpackRedPacket req = new ReqUnpackRedPacket();
		req.redpacketid = BigInteger.valueOf(redpacketid);
		req.redpacketuid = BigInteger.valueOf(redpacketuid);
		req.selfuid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUNPACKREDPACKET_CID;
		goGirlPkt.requnpackredpacket = req;
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
			RspUnpackRedPacket rsp = pkt.rspunpackredpacket;
			int retCode = rsp.retcode.intValue();
			RedPacketInfo info = rsp.redpacket;
			if (checkRetCode(retCode))
				addblack.unpacketRedpacket(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.unpacketRedpacket(resultCode, null);
		}

	}

	public interface IUnpackredpacket {
		public void unpacketRedpacket(int retCode, RedPacketInfo info);
	}

}
