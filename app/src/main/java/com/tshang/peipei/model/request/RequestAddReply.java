package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddReply;
import com.tshang.peipei.protocol.asn.gogirl.RspAddReply;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddReply.java 
 *
 * @Description: 二级回复
 *
 * @author vactor
 *
 * @date 2014-4-30 下午2:21:11 
 *
 * @version V1.0   
 */
public class RequestAddReply extends AsnBase implements ISocketMsgCallBack {

	IAddReply iAddReply;

	@SuppressWarnings("unchecked")
	public void addReply(byte[] auth, int ver, String city, String province, int topicid, int topicuid, int uid, int commentId, String coment,
			IAddReply callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddReply req = new ReqAddReply();
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

		req.replycontentlist.add(info);
		req.province = province.getBytes();
		req.topicid = BigInteger.valueOf(topicid);
		req.topicuid = BigInteger.valueOf(topicuid);
		req.uid = BigInteger.valueOf(uid);
		req.city = "".getBytes();
		req.commentid = BigInteger.valueOf(commentId);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDREPLY_CID;
		goGirlPkt.reqaddreply = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iAddReply = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iAddReply) {
			RspAddReply rsp = pkt.rspaddreply;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iAddReply.addReplyCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iAddReply) {
			iAddReply.addReplyCallBack(resultCode);
		}

	}

	public interface IAddReply {
		public void addReplyCallBack(int retCode);
	}

}
