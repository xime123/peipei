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
import com.tshang.peipei.protocol.asn.gogirl.ReqPublicationDynamics;
import com.tshang.peipei.protocol.asn.gogirl.RspPublicationDynamics;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestWriteDynamic.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-8-17 下午6:30:24 
 *
 * @version V1.0   
 */
public class RequestWriteDynamic extends AsnBase implements ISocketMsgCallBack {

	private final String TAG = this.getClass().getSimpleName();

	private WriteDynamicCallBack callBack;

	@SuppressWarnings("unchecked")
	public void writeDynamic(byte[] auth, int ver, int uid, String province, String city, int isanonymous, int fonttype,int srcpic, String content,
			byte[] bitmap, int relativetopic,WriteDynamicCallBack callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqPublicationDynamics dynamics = new ReqPublicationDynamics();
		dynamics.uid = BigInteger.valueOf(uid);
		dynamics.relativetopic = BigInteger.valueOf(relativetopic);
		dynamics.province = province.getBytes();
		dynamics.city = city.getBytes();
		dynamics.isanonymous = BigInteger.valueOf(isanonymous);
		dynamics.fonttype = BigInteger.valueOf(fonttype);
		dynamics.srcpic=BigInteger.valueOf(srcpic);

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
			dynamics.topiccontentlist.add(info);
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
			dynamics.topiccontentlist.add(photoData);
		}

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPUBLICATIONDYNAMICS_CID;
		goGirlPkt.reqpublicationdynamics = dynamics;
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
			RspPublicationDynamics rsp = pkt.rsppublicationdynamics;
			int retCode = rsp.retcode.intValue();
			callBack.onSuccess(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onError(resultCode);
	}

	public interface WriteDynamicCallBack {
		public void onSuccess(int code);

		public void onError(int code);
	}

}
