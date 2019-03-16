package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddBlacklist;
import com.tshang.peipei.protocol.asn.gogirl.RspAddBlacklist;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 拉黑 
 *
 * @author allen  
 *
 * @date 2014-8-8 上午10:59:54 
 *
 * @version V1.0   
 */
public class RequestAddBlacklist extends AsnBase implements ISocketMsgCallBack {

	private IAddBlacklist addblack;

	public void addBlacklist(byte[] auth, int ver, int uid, int blackuid, IAddBlacklist callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddBlacklist req = new ReqAddBlacklist();
		req.selfuid = BigInteger.valueOf(uid);
		req.blackuid = BigInteger.valueOf(blackuid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDBLACKLIST_CID;
		goGirlPkt.reqaddblacklist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.addblack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != addblack) {
			RspAddBlacklist rsp = pkt.rspaddblacklist;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.addBlackList(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.addBlackList(resultCode);
		}

	}

	public interface IAddBlacklist {
		public void addBlackList(int retCode);
	}

}
