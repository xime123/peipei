package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDareBegPass;
import com.tshang.peipei.protocol.asn.gogirl.RspDareBegPass;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 大冒险求过
 *
 * @author allen  
 *
 * @date 2015-7-20 上午10:59:54 
 *
 * @version V1.0   
 */
public class RequestDareBegPass extends AsnBase implements ISocketMsgCallBack {

	private IDareBegPass addblack;

	public void dareBegPass(byte[] auth, int ver, int uid, int groupid, String dareid, IDareBegPass callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDareBegPass req = new ReqDareBegPass();
		req.groupid = BigInteger.valueOf(groupid);
		req.dareid = dareid.getBytes();
		req.userid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDAREBEGPASS_CID;
		goGirlPkt.reqdarebegpass = req;
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
			RspDareBegPass rsp = pkt.rspdarebegpass;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.resultDareBegPass(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.resultDareBegPass(resultCode);
		}

	}

	public interface IDareBegPass {
		public void resultDareBegPass(int retCode);
	}

}
