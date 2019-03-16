package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDelFollowByUid;
import com.tshang.peipei.protocol.asn.gogirl.RspDelFollowByUid;
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
 * @author allen
 *
 * @date 2014-12-1 下午4:15:29 
 *
 * @version V1.0   
 */
public class RequestDelFollowByUid extends AsnBase implements ISocketMsgCallBack {

	IDeleteFollowByUid iDeleteFollow;

	public void delFollow(byte[] auth, int ver, int followUid, int uid, IDeleteFollowByUid callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqDelFollowByUid req = new ReqDelFollowByUid();
		req.followuid = BigInteger.valueOf(followUid);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELFOLLOWBYUID_CID;
		goGirlPkt.reqdelfollowbyuid = req;
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
			RspDelFollowByUid rsp = pkt.rspdelfollowbyuid;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iDeleteFollow.deleteFollowCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iDeleteFollow) {
			iDeleteFollow.deleteFollowCallBack(resultCode);
		}
	}

	public interface IDeleteFollowByUid {
		public void deleteFollowCallBack(int retCode);
	}

}
