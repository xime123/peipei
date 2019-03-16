package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSingleSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSingleSkillInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 *
 * @Description: 获取单个技能信息
 *
 * @author Jeff
 *
 */
public class RequestGetSingleSkillInfo extends AsnBase implements ISocketMsgCallBack {

	BizCallBackGetSingleSkillInfo bizCallBackGetSingleSkillInfo;

	public void getSingleSkillInfo(byte[] auth, int ver, int skillid, int uid, int otherUid, BizCallBackGetSingleSkillInfo callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqGetSingleSkillInfo req = new ReqGetSingleSkillInfo();
		req.selfuid = BigInteger.valueOf(uid);
		req.skillid = BigInteger.valueOf(skillid);
		req.uid = BigInteger.valueOf(otherUid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSINGLESKILLINFO_CID;
		goGirlPkt.reqgetsingleskillinfo = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.bizCallBackGetSingleSkillInfo = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != bizCallBackGetSingleSkillInfo) {
			RspGetSingleSkillInfo rsp = pkt.rspgetsingleskillinfo;
			int retCode = rsp.retcode.intValue();
			RetGGSkillInfo skillInfo = rsp.skill;
			if (checkRetCode(retCode))
				bizCallBackGetSingleSkillInfo.getSingleSkillInfo(retCode, skillInfo);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != bizCallBackGetSingleSkillInfo) {
			bizCallBackGetSingleSkillInfo.getSingleSkillInfo(resultCode, null);
		}
	}

	public interface BizCallBackGetSingleSkillInfo {
		public void getSingleSkillInfo(int retCode, RetGGSkillInfo skillInfo);
	}

}
