package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GGConfInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAppConfV2;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAppConfV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetAppConf.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014-9-3 下午8:25:20 
 *
 * @version V1.0   
 */
public class RequestGetAppConfV2 extends AsnBase implements ISocketMsgCallBack {

	private IGetAppConfV2 getAppConf;

	public void getAppConf(byte[] auth, int ver, int uid, String channel, IGetAppConfV2 callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetAppConfV2 req = new ReqGetAppConfV2();
		req.fromos = BAConstants.RECHARGE_OS_ANDROID;
		req.uid = BigInteger.valueOf(uid);
		req.appver = BigInteger.valueOf(ver);
		req.fromdownload = channel.getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETAPPCONFV2_CID;
		goGirlPkt.reqgetappconfv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getAppConf = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != getAppConf) {
			RspGetAppConfV2 rsp = pkt.rspgetappconfv2;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				getAppConf.getAppConfigV2(retCode, rsp.conflist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getAppConf) {
			getAppConf.getAppConfigV2(resultCode, null);
		}
	}

	public interface IGetAppConfV2 {
		public void getAppConfigV2(int retCode, GGConfInfoList list);
	}

}
