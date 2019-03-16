package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddComment;
import com.tshang.peipei.protocol.asn.gogirl.RspAddComment;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
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
public class RequestAddComment extends AsnBase implements ISocketMsgCallBack {

	IAddComment iAddComment;

	@SuppressWarnings("unchecked")
	public void addReply(byte[] auth, int ver, String city, String province, int topicid, int topicuid, int uid, String coment, IAddComment callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddComment req = new ReqAddComment();
		req.city = city.getBytes();

		//文本数据
		GoGirlDataInfo info = new GoGirlDataInfo();
		info.data = coment.getBytes();
		info.dataid = "".getBytes();
		info.datainfo = BigInteger.valueOf(0);
		info.revint0 = BigInteger.valueOf(0);
		info.revint1 = BigInteger.valueOf(0);
		info.revstr0 = "".getBytes();
		info.revstr1 = "".getBytes();
		info.type = BigInteger.valueOf(BAConstants.MessageType.TEXT.getValue());
		req.commentcontentlist.add(info);
		req.province = province.getBytes();
		req.city = "".getBytes();
		req.topicid = BigInteger.valueOf(topicid);
		req.topicuid = BigInteger.valueOf(topicuid);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDCOMMENT_CID;
		goGirlPkt.reqaddcomment = req;
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
			RspAddComment rsp = pkt.rspaddcomment;
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

	public interface IAddComment {
		public void addCommentCallBack(int retCode);
	}

}
