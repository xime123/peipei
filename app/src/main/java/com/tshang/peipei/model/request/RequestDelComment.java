package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDelComment;
import com.tshang.peipei.protocol.asn.gogirl.RspDelComment;
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
public class RequestDelComment extends AsnBase implements ISocketMsgCallBack {

	IDelCommentCallBack iDelReplyCallBack;

	@SuppressWarnings("unchecked")
	public void delReply(byte[] auth, int ver, int commentid, int topicid, int uid, int topicuid, IDelCommentCallBack callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDelComment req = new ReqDelComment();
		req.commentid = BigInteger.valueOf(commentid);
		req.uid = BigInteger.valueOf(uid);
		req.topicid = BigInteger.valueOf(topicid);
		req.topicuid = BigInteger.valueOf(topicuid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELCOMMENT_CID;
		goGirlPkt.reqdelcomment = req;
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
			RspDelComment rsp = pkt.rspdelcomment;
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

	public interface IDelCommentCallBack {
		public void delSkillCallBack(int retCode, String msg);
	}

}
