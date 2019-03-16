package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqVerifyUid;
import com.tshang.peipei.protocol.asn.gogirl.RspVerifyUid;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestVerifyUid.java 
 *
 * @Description: 效验邀请id
 *
 * @author allen  
 *
 * @date 2014-10-20 下午5:49:19 
 *
 * @version V1.0   
 */
public class RequestVerifyUid extends AsnBase implements ISocketMsgCallBack {

	private iVerifyUid callback;
	private int verifyUid;

	public void getVerifyUid(byte[] auth, int ver, int uid, int sex, iVerifyUid callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqVerifyUid req = new ReqVerifyUid();
		req.shouldbesex = BigInteger.valueOf(sex);
		req.uid = BigInteger.valueOf(uid);
		verifyUid = uid;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQVERIFYUID_CID;
		goGirlPkt.reqverifyuid = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callback) {
			RspVerifyUid rsp = pkt.rspverifyuid;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultVerifyUid(retCode, verifyUid);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultVerifyUid(resultCode, verifyUid);
		}
	}

	public interface iVerifyUid {
		public void resultVerifyUid(int retCode, int verifyUid);
	}

}
