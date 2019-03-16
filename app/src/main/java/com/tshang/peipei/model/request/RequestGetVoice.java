package com.tshang.peipei.model.request;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetGoGirlVoice;
import com.tshang.peipei.protocol.asn.gogirl.RspGetGoGirlVoice;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetVoice.java 
 *
 * @Description: 获取音频接口
 *
 * @author allen  
 *
 * @date 2014-4-17 下午9:12:52 
 *
 * @version V1.0   
 */
public class RequestGetVoice extends AsnBase implements ISocketMsgCallBack {

	private IGetVocie getVocie;
	private long mLocalID;
	private int friendUid;

	public void setData(long id, int uid) {
		mLocalID = id;
		friendUid = uid;
	}

	public void getVoice(byte[] auth, int ver, byte[] key, IGetVocie voice) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetGoGirlVoice req = new ReqGetGoGirlVoice();
		req.voicekey = key;

		//整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETGOGIRLVOICE_CID;
		goGirlPkt.reqgetgogirlvoice = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getVocie = voice;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface IGetVocie {
		public void getVocie(int retCode, byte[] data, long id, int friendUid, String fileName);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (getVocie != null) {
			RspGetGoGirlVoice rsp = pkt.rspgetgogirlvoice;
			getVocie.getVocie(rsp.retcode.intValue(), rsp.voicedata, mLocalID, friendUid, new String(rsp.voicekey));
		}

	}

	@Override
	public void error(int resultCode) {
		if (getVocie != null) {
			getVocie.getVocie(resultCode, null, mLocalID, friendUid, null);
		}

	}

}
