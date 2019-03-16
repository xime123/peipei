package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetShareData;
import com.tshang.peipei.protocol.asn.gogirl.RspGetShareData;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetShareData.java 
 *
 * @Description: 获取分享链接
 *
 * @author allen  
 *
 * @date 2014-6-21 下午1:56:59 
 *
 * @version V1.0   
 */
public class RequestGetShareData extends AsnBase implements ISocketMsgCallBack {

	private GetShareData getshareData;
	private int mType;

	public void getShareData(byte[] auth, int ver, int uid, int fuid, int type, GetShareData callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		mType = type;
		ReqGetShareData req = new ReqGetShareData();
		req.selfuid = BigInteger.valueOf(uid);
		req.uid = BigInteger.valueOf(fuid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSHAREDATA_CID;
		goGirlPkt.reqgetsharedata = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getshareData = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getshareData) {
			RspGetShareData rsp = pkt.rspgetsharedata;
			int retCode = rsp.retcode.intValue();
			getshareData.getShare(retCode, mType, new String(rsp.url));
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != getshareData) {
			getshareData.getShare(resultCode, mType, "");
		}

	}

	public interface GetShareData {
		public void getShare(int retCode, int type, String url);

	}
}
