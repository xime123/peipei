package com.tshang.peipei.model.request;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqReSetPasswd;
import com.tshang.peipei.protocol.asn.gogirl.RspReSetPasswd;
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
public class RequestReSetPasswd extends AsnBase implements ISocketMsgCallBack {

	ISetPasswd iAddComment;

	public void addReply(byte[] auth, int ver, String email, ISetPasswd callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqReSetPasswd req = new ReqReSetPasswd();
		req.email = email.getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQRESETPASSWD_CID;
		goGirlPkt.reqresetpasswd = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iAddComment = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iAddComment) {
			RspReSetPasswd rsp = pkt.rspresetpasswd;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iAddComment.addCommentCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iAddComment) {
			iAddComment.addCommentCallBack(resultCode);
		}

	}

	public interface ISetPasswd {
		public void addCommentCallBack(int retCode);
	}

}
