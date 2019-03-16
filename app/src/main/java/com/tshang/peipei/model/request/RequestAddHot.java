package com.tshang.peipei.model.request;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.BaseProtobuf;
import com.tshang.peipei.protocol.Gogirl;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestAddHot.java 
 *
 * @Description: 点赞
 *
 * @author allen  
 *
 * @date 2015-2-2 上午10:51:49 
 *
 * @version V1.0   
 */
public class RequestAddHot extends AsnBase implements ISocketMsgCallBack {

	private IAddHot callback;

	public void clickAddHot(byte[] auth, int ver, int roomid, int Selfuid, int uid, IAddHot callback) {
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqAddHot_cid_VALUE;

		byte[] body = addHot(auth, ver, roomid, Selfuid, uid);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] addHot(byte[] auth, int ver, int roomid, int Selfuid, int uid) {
		Gogirl.ReqAddHot.Builder builder = Gogirl.ReqAddHot.newBuilder();
		builder.setRoomid(roomid);
		builder.setSelfuid(Selfuid);
		builder.setOwneruid(uid);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqAddHot_cid_VALUE)));
		Gogirl.ReqAddHot msg = builder.build();
		return msg.toByteArray();
	}

	@Override
	public void succuess(byte[] msg) {
		if (msg != null && msg.length > 0) {
			int len = AsnProtocolTools.http_net_body_length(msg);
			int totalLen = msg.length;
			if (len > 0) {
				byte[] tempByte = new byte[len];
				System.arraycopy(msg, totalLen - len, tempByte, 0, len);

				try {
					Gogirl.RspAddHot rsp = Gogirl.RspAddHot.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null)
							callback.resultAddHot(retCode);
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public void error(int resultCode) {
		if (callback != null)
			callback.resultAddHot(resultCode);
	}

	public interface IAddHot {
		public void resultAddHot(int retCode);
	}
}
