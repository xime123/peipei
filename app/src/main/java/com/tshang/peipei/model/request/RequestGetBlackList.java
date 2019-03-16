package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetBlackList;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetBlackList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetBlackList.java 
 *
 * @Description: 拉取黑名单
 *
 * @author allen  
 *
 * @date 2014-10-16 下午1:37:05 
 *
 * @version V1.0   
 */
public class RequestGetBlackList extends AsnBase implements ISocketMsgCallBack {

	private iGetBlackList callback;

	public void getBlackList(byte[] auth, int ver, int uid, int start, int num, iGetBlackList callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetBlackList req = new ReqGetBlackList();
		req.selfuid = BigInteger.valueOf(uid);
		req.hostuid = BigInteger.valueOf(uid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETBLACKLIST_CID;
		goGirlPkt.reqgetblacklist = req;
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
			RspGetBlackList rsp = pkt.rspgetblacklist;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultBlackList(retCode, rsp.isend.intValue(), rsp.blacklist);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultBlackList(resultCode, 0, null);
		}
	}

	public interface iGetBlackList {
		public void resultBlackList(int code, int end, RetRelevantPeopleInfoList list);
	}
}
