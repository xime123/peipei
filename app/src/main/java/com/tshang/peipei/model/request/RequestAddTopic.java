package com.tshang.peipei.model.request;

import java.math.BigInteger;
import java.util.List;

import android.text.TextUtils;

import com.tencent.mm.sdk.platformtools.Log;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddTopicV2;
import com.tshang.peipei.protocol.asn.gogirl.RspAddTopicV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.model.entity.PhotoEntity;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddTopic.java 
 *
 * @Description: 写贴 
 *
 * @author vactor
 *
 * @date 2014-4-10 下午5:52:51 
 *
 * @version V1.0   
 */
public class RequestAddTopic extends AsnBase implements ISocketMsgCallBack {

	private IAddTopic mAddTopicCallBack;

	//写贴 ,当图片数量>1的时候,分两个步骤.1>构造要发送的伪数据,等待服务器返回.2>再将要发送的数据填充到服务器返回的包体中.
	@SuppressWarnings("unchecked")
	public void addTopicIfHasManyPhotos(byte[] auth, int ver, int uid, String city, long time, int la, int lo, String province, String content,
			List<PhotoEntity> list, IAddTopic callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqAddTopicV2 req = new ReqAddTopicV2();
		req.city = city.getBytes();
		req.createtime = BigInteger.valueOf(time);
		req.la = BigInteger.valueOf(la);
		req.lo = BigInteger.valueOf(lo);
		req.province = province.getBytes();
		req.uid = BigInteger.valueOf(uid);

		if (!TextUtils.isEmpty(content)) {
			//文本数据
			GoGirlDataInfo info = new GoGirlDataInfo();
			info.data = ParseMsgUtil.convertUnicode2(content).getBytes();
			info.dataid = "".getBytes();
			info.datainfo = BigInteger.valueOf(0);
			info.revint0 = BigInteger.valueOf(0);
			info.revint1 = BigInteger.valueOf(0);
			info.revstr0 = "".getBytes();
			info.revstr1 = "".getBytes();
			info.type = BigInteger.valueOf(BAConstants.MessageType.TEXT.getValue());
			req.topiccontentlist.add(info);
		}
		//相册数据,(伪数据)
		for (int i = 0; i < list.size(); i++) {
			GoGirlDataInfo photoData = new GoGirlDataInfo();
			photoData.data = "".getBytes();
			photoData.dataid = "".getBytes();
			photoData.datainfo = BigInteger.valueOf(0);
			photoData.revint0 = BigInteger.valueOf(0);
			photoData.revint1 = BigInteger.valueOf(0);
			photoData.revstr0 = "".getBytes();
			photoData.revstr1 = "".getBytes();
			photoData.type = BigInteger.valueOf(BAConstants.MessageType.IMAGE_KEY.getValue());
			req.topiccontentlist.add(photoData);
		}

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDTOPICV2_CID;
		goGirlPkt.reqaddtopicv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		Log.i("vactor_log", "many>>>>>");

		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mAddTopicCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	//写贴 ,当图片数量>1的时候,分两个步骤.1>构造要发送的伪数据,等待服务器返回.2>再将要发送的数据填充到服务器返回的包体中.
	@SuppressWarnings("unchecked")
	public void addTopicIfHasManyPhotos2(byte[] auth, int ver, int uid, String city, long time, int la, int lo, String province, String content,
			List<String> imageKeys, IAddTopic callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqAddTopicV2 req = new ReqAddTopicV2();
		req.city = city.getBytes();
		req.createtime = BigInteger.valueOf(time);
		req.la = BigInteger.valueOf(la);
		req.lo = BigInteger.valueOf(lo);
		req.province = province.getBytes();
		req.uid = BigInteger.valueOf(uid);

		if (!TextUtils.isEmpty(content)) {
			//文本数据
			GoGirlDataInfo info = new GoGirlDataInfo();
			info.data = content.getBytes();
			info.dataid = "".getBytes();
			info.datainfo = BigInteger.valueOf(0);
			info.revint0 = BigInteger.valueOf(0);
			info.revint1 = BigInteger.valueOf(0);
			info.revstr0 = "".getBytes();
			info.revstr1 = "".getBytes();
			info.type = BigInteger.valueOf(BAConstants.MessageType.TEXT.getValue());
			req.topiccontentlist.add(info);
		}
		//相册数据,(伪数据)
		for (int i = 0; i < imageKeys.size(); i++) {
			GoGirlDataInfo photoData = new GoGirlDataInfo();
			photoData.data = "".getBytes();
			photoData.dataid = "".getBytes();
			photoData.datainfo = BigInteger.valueOf(0);
			photoData.revint0 = BigInteger.valueOf(0);
			photoData.revint1 = BigInteger.valueOf(0);
			photoData.revstr0 = "".getBytes();
			photoData.revstr1 = "".getBytes();
			photoData.type = BigInteger.valueOf(BAConstants.MessageType.IMAGE_KEY.getValue());
			req.topiccontentlist.add(photoData);
		}

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDTOPICV2_CID;
		goGirlPkt.reqaddtopicv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		Log.i("vactor_log", "many>>>>>");

		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mAddTopicCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	//写贴,图片数量==1时直接上传.这个时候,一步到位.
	@SuppressWarnings("unchecked")
	public void addTopicIfHasOnePhoto(byte[] auth, int ver, int uid, String city, long time, int la, int lo, String province, String content,
			byte[] bitmap, IAddTopic callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqAddTopicV2 req = new ReqAddTopicV2();
		req.city = city.getBytes();
		req.createtime = BigInteger.valueOf(time);
		req.la = BigInteger.valueOf(la);
		req.lo = BigInteger.valueOf(lo);
		req.province = province.getBytes();
		req.uid = BigInteger.valueOf(uid);

		if (!TextUtils.isEmpty(content)) {
			//文本数据
			GoGirlDataInfo info = new GoGirlDataInfo();
			info.data = ParseMsgUtil.convertUnicode2(content).getBytes();
			info.dataid = "".getBytes();
			info.datainfo = BigInteger.valueOf(0);
			info.revint0 = BigInteger.valueOf(0);
			info.revint1 = BigInteger.valueOf(0);
			info.revstr0 = "".getBytes();
			info.revstr1 = "".getBytes();
			info.type = BigInteger.valueOf(BAConstants.MessageType.TEXT.getValue());
			req.topiccontentlist.add(info);
		}
		if (null != bitmap) {
			//相册数据
			GoGirlDataInfo photoData = new GoGirlDataInfo();
			photoData.data = bitmap;
			photoData.dataid = "".getBytes();
			photoData.datainfo = BigInteger.valueOf(0);
			photoData.revint0 = BigInteger.valueOf(0);
			photoData.revint1 = BigInteger.valueOf(0);
			photoData.revstr0 = "".getBytes();
			photoData.revstr1 = "".getBytes();
			photoData.type = BigInteger.valueOf(BAConstants.MessageType.IMAGE.getValue());
			req.topiccontentlist.add(photoData);

		}
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDTOPICV2_CID;
		goGirlPkt.reqaddtopicv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mAddTopicCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mAddTopicCallBack) {
			RspAddTopicV2 rsp = pkt.rspaddtopicv2;
			int retCode = rsp.retcode.intValue();
			int userTopicId = rsp.usertopicid.intValue();
			GoGirlDataInfoList list = rsp.todocontentlist;

			if (checkRetCode(retCode))
				mAddTopicCallBack.addTopicCallBack(retCode, userTopicId, rsp.charmnum.intValue(), list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mAddTopicCallBack) {
			mAddTopicCallBack.addTopicCallBack(resultCode, -1, 0, null);
		}
	}

	public interface IAddTopic {
		public void addTopicCallBack(int retCode, int topicId, int charmnum, GoGirlDataInfoList list);
	}

}
