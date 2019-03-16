package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddFollow;
import com.tshang.peipei.protocol.asn.gogirl.RspAddFollow;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetRelasionship.java 
 *
 * @Description: 关注
 *
 * @author vactor
 *
 * @date 2014-5-4 上午10:10:44 
 *
 * @version V1.0   
 */
public class RequestAddFollow extends AsnBase implements ISocketMsgCallBack {

	IAddFollow mIAddFollow;

	public void addFollow(byte[] auth, int ver, int uid, int uid2, IAddFollow callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqAddFollow req = new ReqAddFollow();
		req.peeruid = BigInteger.valueOf(uid2);
		req.selfuid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDFOLLOW_CID;
		goGirlPkt.reqaddfollow = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mIAddFollow = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mIAddFollow) {
			RspAddFollow rsp = pkt.rspaddfollow;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				mIAddFollow.addFollowCallBack(retCode, rsp.followid.intValue());
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mIAddFollow) {
			mIAddFollow.addFollowCallBack(resultCode, -1);
		}
	}

	public interface IAddFollow {
		public void addFollowCallBack(int retCode, int followid);
	}

}
