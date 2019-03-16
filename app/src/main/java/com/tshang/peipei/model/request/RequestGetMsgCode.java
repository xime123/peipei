package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetMsgCode;
import com.tshang.peipei.protocol.asn.gogirl.RspGetMsgCode;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetMsgCode.java 
 *
 * @Description: 获取手机验证码
 *
 * @author allen  
 *
 * @date 2014-11-18 下午3:31:46 
 *
 * @version V1.0   
 */
public class RequestGetMsgCode extends AsnBase implements ISocketMsgCallBack {

	private iGetMsgCode callback;

	public void getMsgCode(byte[] auth, int ver, int uid, String phone, String piccode, iGetMsgCode callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetMsgCode req = new ReqGetMsgCode();
		req.phoneno = phone.getBytes();
		req.piccode = piccode.getBytes();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETMSGCODE_CID;
		goGirlPkt.reqgetmsgcode = req;
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
			RspGetMsgCode rsp = pkt.rspgetmsgcode;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultMsgCode(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultMsgCode(resultCode);
		}
	}

	public interface iGetMsgCode {
		public void resultMsgCode(int code);
	}

}
