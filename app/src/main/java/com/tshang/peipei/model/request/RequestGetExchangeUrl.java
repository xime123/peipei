package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetExchangeUrl;
import com.tshang.peipei.protocol.asn.gogirl.RspGetExchangeUrl;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetExchangeUrl.java 
 *
 * @Description: 获取积分商城url
 *
 * @author allen  
 *
 * @date 2014-5-20 下午5:11:09 
 *
 * @version V1.0   
 */
public class RequestGetExchangeUrl extends AsnBase implements ISocketMsgCallBack {

	private IGetExchangeUrl getExchageUrl;

	public void getExchangeUrl(byte[] auth, int ver, int uid, IGetExchangeUrl callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetExchangeUrl req = new ReqGetExchangeUrl();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETEXCHANGEURL_CID;
		goGirlPkt.reqgetexchangeurl = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getExchageUrl = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getExchageUrl) {
			RspGetExchangeUrl rsp = pkt.rspgetexchangeurl;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				getExchageUrl.getUrl(retCode, new String(rsp.url));
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getExchageUrl) {
			getExchageUrl.getUrl(resultCode, null);
		}

	}

	public interface IGetExchangeUrl {
		public void getUrl(int retCode, String url);
	}

}
