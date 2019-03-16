package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqUploadTopicContent;
import com.tshang.peipei.protocol.asn.gogirl.RspUploadTopicContent;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestUploadTopicContent.java 
 *
 * @Description: 写贴最后一步,适用于图片count>1的情况
 *
 * @author vactor
 *
 * @date 2014-4-10 下午7:22:24 
 *
 * @version V1.0   
 */
public class RequestUploadTopicContent extends AsnBase implements ISocketMsgCallBack {

	IUploadTopicContent mUploadTopicContentCallBack;

	public void requestUploadTopicContent(byte[] auth, int ver, int uid, int topicId, byte[] bitmapData, GoGirlDataInfo dataInfo,
			IUploadTopicContent callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqUploadTopicContent req = new ReqUploadTopicContent();
		req.topicid = BigInteger.valueOf(topicId);
		req.uid = BigInteger.valueOf(uid);
		req.metadata = dataInfo;
		req.data = bitmapData;
		req.datainfo = BigInteger.valueOf(BAConstants.MessageType.IMAGE.getValue());
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUPLOADTOPICCONTENT_CID;
		goGirlPkt.requploadtopiccontent = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mUploadTopicContentCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mUploadTopicContentCallBack) {
			RspUploadTopicContent rspUploadTopicContent = pkt.rspuploadtopiccontent;
			int retCode = rspUploadTopicContent.retcode.intValue();
			GoGirlDataInfoList list = rspUploadTopicContent.todocontentlist;
			mUploadTopicContentCallBack.uploadTopicContentCallBack(retCode, list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mUploadTopicContentCallBack) {
			mUploadTopicContentCallBack.uploadTopicContentCallBack(resultCode, null);
		}
	}

	public interface IUploadTopicContent {
		public void uploadTopicContentCallBack(int retCode, GoGirlDataInfoList list);
	}

}
