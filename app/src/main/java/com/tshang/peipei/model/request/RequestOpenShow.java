package com.tshang.peipei.model.request;

import android.app.Activity;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.BaseProtobuf;
import com.tshang.peipei.protocol.Gogirl;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.Gogirl.ShowRoomInfo;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestOpenShow.java 
 *
 * @Description:开秀
 *
 * @author allen  
 *
 * @date 2015-1-15 下午5:08:29 
 *
 * @version V1.0   
 */
public class RequestOpenShow extends AsnBase implements ISocketMsgCallBack {

	private IOpenShowCallBack callback;

	public void requestOpenShow(byte[] auth, int ver, int uid, IOpenShowCallBack callback) {
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqOpenShow_cid_VALUE;

		byte[] body = openshow(auth, ver, uid);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] openshow(byte[] auth, int ver, int uid) {
		Gogirl.ReqOpenShow.Builder builder = Gogirl.ReqOpenShow.newBuilder();
		builder.setUid(uid);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqOpenShow_cid_VALUE)));

		Gogirl.ReqOpenShow msg = builder.build();
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
					Gogirl.RspOpenShow rsp = Gogirl.RspOpenShow.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null) {
							callback.openShowCallback(retCode, rsp.getRoominfo());
						}
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public void error(int resultCode) {
		if (callback != null) {
			callback.openShowCallback(resultCode, null);
		}
	}

	public interface IOpenShowCallBack {
		public void openShowCallback(int retCode, ShowRoomInfo info);
	}
}
