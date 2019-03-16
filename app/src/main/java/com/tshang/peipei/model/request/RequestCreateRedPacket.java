package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqCreateRedPacket;
import com.tshang.peipei.protocol.asn.gogirl.RspCreateRedPacket;
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
public class RequestCreateRedPacket extends AsnBase implements ISocketMsgCallBack {

	private ICreateredpacket addblack;

	public void createRedPacket(byte[] auth, int ver, int uid, int groupid, int totalgoldcoin, String desc, int portionnum, ICreateredpacket callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqCreateRedPacket req = new ReqCreateRedPacket();
		req.uid = BigInteger.valueOf(uid);
		req.desc = desc.getBytes();
		req.portionnum = BigInteger.valueOf(portionnum);
		req.togroupid = BigInteger.valueOf(groupid);
		req.totalgoldcoin = BigInteger.valueOf(totalgoldcoin);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQCREATEREDPACKET_CID;
		goGirlPkt.reqcreateredpacket = req;
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
			RspCreateRedPacket rsp = pkt.rspcreateredpacket;
			int retCode = rsp.retcode.intValue();
			RedPacketInfo info = rsp.redpacket;
			if (checkRetCode(retCode))
				addblack.createredpacket(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.createredpacket(resultCode, null);
		}

	}

	public interface ICreateredpacket {
		public void createredpacket(int retCode, RedPacketInfo info);
	}

}
