package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqBindPhone;
import com.tshang.peipei.protocol.asn.gogirl.RspBindPhone;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.BaseDes;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestBindPhone.java 
 *
 * @Description: 绑定手机 
 *
 * @author allen  
 *
 * @date 2014-11-18 下午7:31:54 
 *
 * @version V1.0   
 */
public class RequestBindPhone extends AsnBase implements ISocketMsgCallBack {

	private static String EASECHAT_USER_INFO_DES_KEY = "y3m&c*B#";

	private iBindPhone callback;
	private String phoneno;

	public void bindPhone(byte[] auth, int ver, int uid, String phone, String code, String passwd, iBindPhone callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqBindPhone req = new ReqBindPhone();
		req.phoneno = phone.getBytes();
		req.code = code.getBytes();
		req.uid = BigInteger.valueOf(uid);
		this.phoneno = phone;

		// 加密密码
		byte[] pwdTemp = BaseTools.getMergeArray(BaseTools.getMD5Str(passwd.getBytes()), "qazwsxed".getBytes());
		byte[] pwdByte = BaseDes.encrypt(pwdTemp, EASECHAT_USER_INFO_DES_KEY.getBytes());
		byte[] checksumByte = BaseTools.getMD5Str(BaseTools.getMergeArray(phone.getBytes(), pwdTemp));

		req.passwd = pwdByte;
		req.checksum = checksumByte;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQBINDPHONE_CID;
		goGirlPkt.reqbindphone = req;
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
			RspBindPhone rsp = pkt.rspbindphone;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultBindPhone(retCode, phoneno);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultBindPhone(resultCode, phoneno);
		}
	}

	public interface iBindPhone {
		public void resultBindPhone(int retCode, String phone);
	}

}
