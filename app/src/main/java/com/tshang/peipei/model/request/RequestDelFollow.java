package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDelFollow;
import com.tshang.peipei.protocol.asn.gogirl.RspDelFollow;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestDelFollow.java 
 *
 * @Description: 取消关注
 *
 * @author vactor
 *
 * @date 2014-5-12 下午4:15:29 
 *
 * @version V1.0   
 */
public class RequestDelFollow extends AsnBase implements ISocketMsgCallBack {

	IDeleteFollow iDeleteFollow;

	public void delFollow(byte[] auth, int ver, int followId, int uid, IDeleteFollow callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqDelFollow req = new ReqDelFollow();
		req.followid = BigInteger.valueOf(followId);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELFOLLOW_CID;
		goGirlPkt.reqdelfollow = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iDeleteFollow = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iDeleteFollow) {
			RspDelFollow rsp = pkt.rspdelfollow;
			int retCode = rsp.retcode.intValue();
			int followId = rsp.followid.intValue();
			if (checkRetCode(retCode))
				iDeleteFollow.deleteFollowCallBack(retCode, followId);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iDeleteFollow) {
			iDeleteFollow.deleteFollowCallBack(resultCode, -1);
		}
	}

	public interface IDeleteFollow {
		public void deleteFollowCallBack(int retCode, int followId);
	}

}
