package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddVoiceDesc;
import com.tshang.peipei.protocol.asn.gogirl.RspAddVoiceDesc;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddVoiceDesc.java 
 *
 * @Description: 个人主页语音介绍
 *
 * @author allen  
 *
 * @date 2014-8-30 下午2:42:57 
 *
 * @version V1.0   
 */
public class RequestAddVoiceDesc extends AsnBase implements ISocketMsgCallBack {

	private IAddVoice addVoice;

	public void addVoiceDesc(byte[] auth, int ver, int uid, byte[] data, int len, IAddVoice callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddVoiceDesc req = new ReqAddVoiceDesc();
		req.uid = BigInteger.valueOf(uid);
		req.voice = data;
		req.voicelen = BigInteger.valueOf(len);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDVOICEDESC_CID;
		goGirlPkt.reqaddvoicedesc = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.addVoice = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != addVoice) {
			RspAddVoiceDesc rsp = pkt.rspaddvoicedesc;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addVoice.addVoice(retCode, new String(rsp.voicekey));
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != addVoice) {
			addVoice.addVoice(resultCode, "");
		}
	}

	public interface IAddVoice {
		public void addVoice(int retCode, String name);
	}

}
