package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSkillTipInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSkillTipInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetSkillTipInfo.java 
 *
 * @Description: 获取技能提示信息 
 *
 * @author DYH  
 *
 * @date 2015-11-11 上午11:01:41 
 *
 * @version V1.0   
 */
public class RequestGetSkillTipInfo extends AsnBase implements ISocketMsgCallBack {
	private GetSkillInfoCallBack callBack;

	public void requestGetSkillTipInfo(byte[] auth, int ver, int uid, int mFriendUid, int type, GetSkillInfoCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSkillTipInfo req = new ReqGetSkillTipInfo();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.inviteduid = BigInteger.valueOf(mFriendUid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSKILLTIPINFO_CID;
		goGirlPkt.reqgetskilltipinfo = req;
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
			RspGetSkillTipInfo rsp = pkt.rspgetskilltipinfo;
			int retCode = rsp.retcode.intValue();
			GoGirlDataInfoList list = rsp.skillTipInfo;
			callBack.getSkillInfoOnSuccess(retCode, list);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.getSkillInfoOnError(resultCode);
	}

	public interface GetSkillInfoCallBack {
		public void getSkillInfoOnSuccess(int resCode, Object object);

		public void getSkillInfoOnError(int resCode);
	}
}
