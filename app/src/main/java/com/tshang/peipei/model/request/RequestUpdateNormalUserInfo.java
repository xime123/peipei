package com.tshang.peipei.model.request;

import java.math.BigInteger;

import android.text.TextUtils;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqUpdateUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspUpdateUserInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestUpdateNormalUserInfo.java 
 *
 * @Description: 修改用户 信息,修改密码不包括在内
 *
 * @author vactor
 *
 * @date 2014-4-21 下午5:46:14 
 *
 * @version V1.0   
 */
public class RequestUpdateNormalUserInfo extends AsnBase implements ISocketMsgCallBack {

	private IUpdateUserInfo iUpdateUserInfo;

	public void updateUserInfo(byte[] auth, int ver, int uid, String nick, int sex, long birthday, String email, String phone,
			IUpdateUserInfo callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqUpdateUserInfo req = new ReqUpdateUserInfo();
		req.birthday = BigInteger.valueOf(birthday);
		if (TextUtils.isEmpty(email)) {
			req.email = "".getBytes();
		} else {
			req.email = email.getBytes();
		}
		req.nick = nick.getBytes();
		req.phone = phone.getBytes();
		req.revint = BigInteger.valueOf(0);
		req.revstr = "".getBytes();
		req.sex = BigInteger.valueOf(sex);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUPDATEUSERINFO_CID;
		goGirlPkt.requpdateuserinfo = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		this.iUpdateUserInfo = callBack;
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		int retCode = -1;
		if (null != iUpdateUserInfo) {
			RspUpdateUserInfo rsp = pkt.rspupdateuserinfo;
			retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iUpdateUserInfo.updateUserInfoCallBack(retCode);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != iUpdateUserInfo) {
			iUpdateUserInfo.updateUserInfoCallBack(resultCode);
		}
	}

	public interface IUpdateUserInfo {
		public void updateUserInfoCallBack(int retCode);
	}

}
