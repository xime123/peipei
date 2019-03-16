package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddSkillv2;
import com.tshang.peipei.protocol.asn.gogirl.RspAddSkillv2;
import com.tshang.peipei.protocol.asn.gogirl.SkillInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestSaveGoddessSkill.java 
 *
 * @Description: 保存女神技能 
 *
 * @author DYH  
 *
 * @date 2015-11-9 下午3:34:29 
 *
 * @version V1.0   
 */
public class RequestSaveGoddessSkill extends AsnBase implements ISocketMsgCallBack {

	private SaveGoddessSkillCallBack callBack;

	public void requestSaveGoddessSkill(byte[] auth, int ver, int uid, SkillInfoList skillInfoList, SaveGoddessSkillCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddSkillv2 req = new ReqAddSkillv2();
		req.uid = BigInteger.valueOf(uid);
		req.skillInfoList = skillInfoList;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDSKILLV2_CID;
		goGirlPkt.reqaddskillv2 = req;
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
			RspAddSkillv2 rsp = pkt.rspaddskillv2;
			int retCode = rsp.retcode.intValue();
			String meeage = new String(rsp.retmsg);
			callBack.saveOnSuccess(retCode, meeage);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.saveOnError(resultCode);
	}

	public interface SaveGoddessSkillCallBack {
		public void saveOnSuccess(int code, String msg);

		public void saveOnError(int code);
	}

}
