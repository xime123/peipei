package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSkillGiftList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSkillGiftList;
import com.tshang.peipei.protocol.asn.gogirl.SkillGiftInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetSkillGiftList.java 
 *
 * @Description: 请求女神技能礼物列表 
 *
 * @author DYH  
 *
 * @date 2015-11-11 上午10:20:13 
 *
 * @version V1.0   
 */
public class RequestGetSkillGiftList extends AsnBase implements ISocketMsgCallBack {
	private GetSkillGiftListCallBack callBack;

	public void requestSkillGiftList(byte[] auth, int ver, int uid, int mFriendUid, int type, GetSkillGiftListCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSkillGiftList req = new ReqGetSkillGiftList();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.inviteduid = BigInteger.valueOf(mFriendUid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSKILLGIFTLIST_CID;
		goGirlPkt.reqgetskillgiftlist = req;
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
			RspGetSkillGiftList rsp = pkt.rspgetskillgiftlist;
			int retCode = rsp.retcode.intValue();
			SkillGiftInfoList list = rsp.skillGiftInfoList;
			callBack.getSkillGiftListOnSuccess(retCode, list);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.getSkillGiftListOnError(resultCode);
	}

	public interface GetSkillGiftListCallBack {
		public void getSkillGiftListOnSuccess(int resCode, Object object);

		public void getSkillGiftListOnError(int resCode);
	}

}
