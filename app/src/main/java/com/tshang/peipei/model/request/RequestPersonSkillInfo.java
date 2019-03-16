package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetPersonSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetPersonSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestPersonSkillInfo.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-11-10 上午11:44:48 
 *
 * @version V1.0   
 */
public class RequestPersonSkillInfo extends AsnBase implements ISocketMsgCallBack {
	private GetPersonSkillInfo callBack;

	public void requestPersonSkillInfo(byte[] auth, int ver, int uid, GetPersonSkillInfo callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetPersonSkillInfo req = new ReqGetPersonSkillInfo();
		req.uid = BigInteger.valueOf(uid);
		req.skillkind = BigInteger.valueOf(0);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETPERSONSKILLINFO_CID;
		goGirlPkt.reqgetpersonskillinfo = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspGetPersonSkillInfo rsp = pkt.rspgetpersonskillinfo;
			int retCode = rsp.retcode.intValue();
			SkillTextInfoList list = rsp.skillTextInfoList;
			callBack.getPersonSkillInfoOnSuccess(retCode, list);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.getPersonSkillInfoOnError(resultCode);
	}

	public interface GetPersonSkillInfo {
		public void getPersonSkillInfoOnSuccess(int resCode, Object obj);

		public void getPersonSkillInfoOnError(int resCode);
	}

}
