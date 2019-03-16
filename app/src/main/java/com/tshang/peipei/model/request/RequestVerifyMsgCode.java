package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqVerifyMsgCode;
import com.tshang.peipei.protocol.asn.gogirl.RspVerifyMsgCode;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestVerifyMsgCode.java 
 *
 * @Description: 验证验证码正确性
 *
 * @author allen
 *
 * @date 2014-11-21 下午2:36:24 
 *
 * @version V1.0   
 */
public class RequestVerifyMsgCode extends AsnBase implements ISocketMsgCallBack {

	private iVerifyMsgCode callback;
	private String phone, code;

	public void getVerifyMsgCode(byte[] auth, int ver, int uid, String code, String phone, iVerifyMsgCode callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqVerifyMsgCode req = new ReqVerifyMsgCode();
		req.uid = BigInteger.valueOf(uid);
		req.code = code.getBytes();
		req.phoneno = phone.getBytes();

		this.phone = phone;
		this.code = code;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQVERIFYMSGCODE_CID;
		goGirlPkt.reqverifymsgcode = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
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
			RspVerifyMsgCode rsp = pkt.rspverifymsgcode;
			if (checkRetCode(rsp.retcode.intValue()))
				callback.resultVerifyMsgCode(rsp.retcode.intValue(), phone, code);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultVerifyMsgCode(resultCode, phone, code);
		}
	}

	public interface iVerifyMsgCode {
		public void resultVerifyMsgCode(int retCode, String phone, String code);
	}
}
