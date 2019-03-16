package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqRemoveBlacklist;
import com.tshang.peipei.protocol.asn.gogirl.RspRemoveBlacklist;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestRemoveBlacklist.java 
 *
 * @Description: 删除黑名单
 *
 * @author allen  
 *
 * @date 2014-10-17 下午6:40:50 
 *
 * @version V1.0   
 */
public class RequestRemoveBlacklist extends AsnBase implements ISocketMsgCallBack {

	private iRemoveBlacklist callback;
	private int blackid;

	public void removeBlacklist(byte[] auth, int ver, int uid, int blackuid, iRemoveBlacklist callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqRemoveBlacklist req = new ReqRemoveBlacklist();
		req.selfuid = BigInteger.valueOf(uid);
		req.blackuid = BigInteger.valueOf(blackuid);
		blackid = blackuid;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQREMOVEBLACKLIST_CID;
		goGirlPkt.reqremoveblacklist = req;
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
			RspRemoveBlacklist rsp = pkt.rspremoveblacklist;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultBlacklist(retCode, blackid);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultBlacklist(resultCode, blackid);
		}
	}

	public interface iRemoveBlacklist {
		public void resultBlacklist(int retCode, int blackid);
	}

}
