package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetReward;
import com.tshang.peipei.protocol.asn.gogirl.RspGetReward;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetReward.java 
 *
 * @Description: 领取奖励 
 *
 * @author allen  
 *
 * @date 2014-5-6 上午10:51:09 
 *
 * @version V1.0   
 */
public class RequestGetReward extends AsnBase implements ISocketMsgCallBack {

	private IGetReward getReward;
	private int mType = -1;

	public void geteward(byte[] auth, int ver, int uid, int type, IGetReward callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetReward req = new ReqGetReward();
		req.type = BigInteger.valueOf(type);
		req.uid = BigInteger.valueOf(uid);
		mType = type;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETREWARD_CID;
		goGirlPkt.reqgetreward = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getReward = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface IGetReward {
		public void getRewardCallBack(int retCode, int type);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getReward) {
			RspGetReward rsp = pkt.rspgetreward;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				getReward.getRewardCallBack(retCode, mType);
		}
	}

	@Override
	public void error(int resultCode) {
		if (getReward != null) {
			getReward.getRewardCallBack(resultCode, mType);
		}
	}

}
