package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDareSendFlowers;
import com.tshang.peipei.protocol.asn.gogirl.RspDareSendFlowers;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestDareSendFlowers.java 
 *
 * @Description: 大冒险送花
 *
 * @author allen  
 *
 * @date 2015-7-22 下午17:48:54 
 *
 * @version V1.0   
 */
public class RequestDareSendFlowers extends AsnBase implements ISocketMsgCallBack {

	private IDareSendFlowers addblack;

	public void dareSendFlowers(byte[] auth, int ver, int uid, int groupid, int gifttype, String dareid, IDareSendFlowers callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDareSendFlowers req = new ReqDareSendFlowers();
		req.dareid = dareid.getBytes();
		req.gifttype = BigInteger.valueOf(gifttype);
		req.groupid = BigInteger.valueOf(groupid);
		req.userid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDARESENDFLOWERS_CID;
		goGirlPkt.reqdaresendflowers = req;
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
			RspDareSendFlowers rsp = pkt.rspdaresendflowers;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.resDareSendFlowers(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.resDareSendFlowers(resultCode);
		}

	}

	public interface IDareSendFlowers {
		public void resDareSendFlowers(int retCode);
	}

}
