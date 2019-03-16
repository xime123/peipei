package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAdvUrlV3;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAdvUrlV3;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetAdvUrl.java 
 *
 * @Description: 获取活动url
 *
 * @author allen  
 *
 * @date 2014-7-31 下午5:50:52 
 *
 * @version V1.0   
 */
public class RequestGetAdvUrl extends AsnBase implements ISocketMsgCallBack {

	private IGetAdv getAdv;

	public void getAdvUrl(byte[] auth, int ver, int uid, IGetAdv callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetAdvUrlV3 req = new ReqGetAdvUrlV3();
		req.uid = BigInteger.valueOf(uid);
		req.fromos = BAConstants.RECHARGE_OS_ANDROID;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETADVURLV3_CID;
		goGirlPkt.reqgetadvurlv3 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getAdv = callback;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getAdv) {
			RspGetAdvUrlV3 rsp = pkt.rspgetadvurlv3;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				getAdv.getAdv(retCode, new String(rsp.url), new String(rsp.verifystr));
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getAdv) {
			getAdv.getAdv(resultCode, "", "");
		}
	}

	public interface IGetAdv {
		public void getAdv(int retCode, String url, String verifystr);
	}

}
