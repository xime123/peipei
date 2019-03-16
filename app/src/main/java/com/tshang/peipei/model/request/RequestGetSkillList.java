package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSkillList;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSkillList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * 
 * @author Jeff
 *
 */
public class RequestGetSkillList extends AsnBase implements ISocketMsgCallBack {

	IGetSkillList iGetSkillList;

	public void getSkillList(byte[] auth, int ver, int num, int start, int selfuid, int uid, IGetSkillList callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSkillList req = new ReqGetSkillList();
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.selfuid = BigInteger.valueOf(selfuid);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSKILLLIST_CID;
		goGirlPkt.reqgetskilllist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iGetSkillList = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iGetSkillList) {
			RspGetSkillList rsp = pkt.rspgetskilllist;
			RetGGSkillInfoList lists = rsp.skilllist;
			int tatal = rsp.total.intValue();
			int retCode = rsp.retcode.intValue();
			int isSend = rsp.isend.intValue();
			if (checkRetCode(retCode))
				iGetSkillList.getSkillListCallBack(retCode, tatal, isSend, lists);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iGetSkillList) {
			iGetSkillList.getSkillListCallBack(resultCode, 0, 0, null);
		}

	}

	public interface IGetSkillList {
		public void getSkillListCallBack(int retCode, int tatal, int isSend, RetGGSkillInfoList lists);
	}

}
