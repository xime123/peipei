package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqChangePhonePasswd;
import com.tshang.peipei.protocol.asn.gogirl.RspChangePhonePasswd;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.BaseDes;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.babase.BATools;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestChangePhonePasswd.java 
 *
 * @Description: 修改手机账号密码 
 *
 * @author allen  
 *
 * @date 2014-11-21 上午10:49:02 
 *
 * @version V1.0   
 */
public class RequestChangePhonePasswd extends AsnBase implements ISocketMsgCallBack {

	IChangePwdByPhone iChangePwd;

	public void changePassword(byte[] auth, int ver, int uid, String oldPwd, String newPwd, IChangePwdByPhone callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqChangePhonePasswd req = new ReqChangePhonePasswd();

		byte[] tmpOld = BATools.getMergeArray(BaseTools.getMD5Str(BATools.intToByteArray1(uid)), "qazwsxed".getBytes());
		byte[] key = BaseTools.getMD5Str_8(oldPwd.getBytes());
		byte[] pwdByteold = BaseDes.encrypt(tmpOld, key);

		byte[] tmpNew = BaseTools.getMergeArray(BaseTools.getMD5Str(newPwd.getBytes()), "qazwsxed".getBytes());
		byte[] pwdBytenew = BaseDes.encrypt(tmpNew, key);

		req.newpwdverifystr = pwdBytenew;
		req.oldpwdverifystr = pwdByteold;
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQCHANGEPHONEPASSWD_CID;
		goGirlPkt.reqchangephonepasswd = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iChangePwd = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iChangePwd) {
			RspChangePhonePasswd rsp = pkt.rspchangephonepasswd;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iChangePwd.changePwdByPhoneCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iChangePwd) {
			iChangePwd.changePwdByPhoneCallBack(resultCode);
		}

	}

	public interface IChangePwdByPhone {
		public void changePwdByPhoneCallBack(int retCode);
	}

}
