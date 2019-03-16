package com.tshang.peipei.model.request;

import java.math.BigInteger;

import android.text.TextUtils;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddDynamicsReply;
import com.tshang.peipei.protocol.asn.gogirl.RspAddDynamicsReply;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestDynamicReply.java 
 *
 * @Description: 对动态信息回复请求
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午6:30:24 
 *
 * @version V1.0   
 */
public class RequestDynamicReply2 extends AsnBase implements ISocketMsgCallBack {

	private addDynamicReplyCallBack2 callBack;

	@SuppressWarnings("unchecked")
	public void addDynamicReply2(byte[] auth, int ver, int uid, int topicuid, int topicid, int commentid, int type, int systemtopicid,
			String province, String city, String content, addDynamicReplyCallBack2 callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddDynamicsReply req = new ReqAddDynamicsReply();
		req.uid = BigInteger.valueOf(uid);
		req.province = province.getBytes();
		req.city = city.getBytes();
		req.topicid = BigInteger.valueOf(topicid);
		req.topicuid = BigInteger.valueOf(topicuid);
		req.commentid = BigInteger.valueOf(commentid);
		req.type = BigInteger.valueOf(type);
		req.systemtopicid = BigInteger.valueOf(systemtopicid);

		if (!TextUtils.isEmpty(content)) {
			GoGirlDataInfo info = new GoGirlDataInfo();
			info.data = ParseMsgUtil.convertUnicode2(content).getBytes();
			info.type = BigInteger.valueOf(BAConstants.MessageType.TEXT.getValue());
			info.dataid = "".getBytes();
			info.datainfo = BigInteger.valueOf(0);
			info.revint0 = BigInteger.valueOf(0);
			info.revint1 = BigInteger.valueOf(0);
			info.revstr0 = "".getBytes();
			info.revstr1 = "".getBytes();
			req.replycontentlist.add(info);
		}

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDDYNAMICSREPLY_CID;
		goGirlPkt.reqadddynamicsreply = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspAddDynamicsReply rsp = pkt.rspadddynamicsreply;
			int retCode = rsp.retcode.intValue();
			callBack.onSuccess(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onFailure(resultCode);
	}

	public interface addDynamicReplyCallBack2 {
		public void onSuccess(int code);

		public void onFailure(int code);
	}

}
