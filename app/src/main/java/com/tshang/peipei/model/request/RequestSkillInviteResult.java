package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.activity.skillnew.bean.SkillResultBean;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSkillTipInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqSkillInviteResult;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSkillTipInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspSkillInviteResult;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestSkillInviteResult.java 
 *
 * @Description: 技能邀请回应
 *
 * @author DYH  
 *
 * @date 2015-11-13 上午10:52:19 
 *
 * @version V1.0   
 */
public class RequestSkillInviteResult extends AsnBase implements ISocketMsgCallBack {

	private SkillInviteResultCallBack callBack;

	public void requestSkillInviteResult(byte[] auth, int ver, int uid, int touid, int skillid, int giftid, int invitedstatus, int skilllistid, SkillInviteResultCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqSkillInviteResult req = new ReqSkillInviteResult();
		req.inviteduid = BigInteger.valueOf(uid);
		req.touid = BigInteger.valueOf(touid);
		req.type = BigInteger.valueOf(0);
		req.skillid = BigInteger.valueOf(skillid);
		req.giftid = BigInteger.valueOf(giftid);
		req.skilllistid = BigInteger.valueOf(skilllistid);
		req.invitedstatus = BigInteger.valueOf(invitedstatus);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSKILLINVITERESULT_CID;
		goGirlPkt.reqskillinviteresult = req;
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
			RspSkillInviteResult rsp = pkt.rspskillinviteresult;
			int retCode = rsp.retcode.intValue();
			SkillResultBean bean = new SkillResultBean();
			bean.setSkilllistid(rsp.skilllistid.intValue());
			bean.setInvitedstatus(rsp.invitedstatus.intValue());
			bean.setCreatetime(rsp.createtime.longValue());
			callBack.skillInviteResultOnSuccess(retCode, bean);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.skillInviteResultOnError(resultCode);
	}

	public interface SkillInviteResultCallBack {
		public void skillInviteResultOnSuccess(int resCode, Object object);

		public void skillInviteResultOnError(int resCode);
	}

}
