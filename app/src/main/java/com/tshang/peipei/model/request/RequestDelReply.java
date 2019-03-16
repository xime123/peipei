package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDelReply;
import com.tshang.peipei.protocol.asn.gogirl.RspDelReply;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddReply.java 
 *
 * @Description: 回复
 *
 * @author vactor
 *
 * @date 2014-4-30 下午2:21:11 
 *
 * @version V1.0   
 */
public class RequestDelReply extends AsnBase implements ISocketMsgCallBack {

	IDelReplyCallBack iDelReplyCallBack;

	@SuppressWarnings("unchecked")
	public void delReply(byte[] auth, int ver, int commentid, int replyid, int topicid, int uid, int topicuid, IDelReplyCallBack callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDelReply req = new ReqDelReply();
		req.commentid = BigInteger.valueOf(commentid);
		req.uid = BigInteger.valueOf(uid);
		req.replyid = BigInteger.valueOf(replyid);
		req.topicid = BigInteger.valueOf(topicid);
		req.topicuid = BigInteger.valueOf(topicuid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELREPLY_CID;
		goGirlPkt.reqdelreply = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iDelReplyCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iDelReplyCallBack) {
			RspDelReply rsp = pkt.rspdelreply;
			String retMst = new String(rsp.retmsg);
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode)) {
				iDelReplyCallBack.delSkillCallBack(retCode, retMst);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iDelReplyCallBack) {
			iDelReplyCallBack.delSkillCallBack(resultCode, "删除失败");
		}

	}

	public interface IDelReplyCallBack {
		public void delSkillCallBack(int retCode, String msg);
	}

}
