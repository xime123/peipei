package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSkillDealList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSkillDealList;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetSkillDealList.java 
 *
 * @Description: 获取订单详情
 *
 * @author allen  
 *
 * @date 2014-10-22 下午7:29:00 
 *
 * @version V1.0   
 */
public class RequestGetSkillDealList extends AsnBase implements ISocketMsgCallBack {

	private iGetSkillDealList callback;

	public void getSkillDealList(byte[] auth, int ver, int uid, int start, int num, iGetSkillDealList callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSkillDealList req = new ReqGetSkillDealList();
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSKILLDEALLIST_CID;
		goGirlPkt.reqgetskilldeallist = req;
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
			RspGetSkillDealList rsp = pkt.rspgetskilldeallist;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultSkillDealList(retCode, rsp.isend.intValue(), rsp.skilldeallist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultSkillDealList(resultCode, 0, null);
		}
	}

	public interface iGetSkillDealList {
		public void resultSkillDealList(int retCode, int end, SkillDealInfoList list);
	}
}
