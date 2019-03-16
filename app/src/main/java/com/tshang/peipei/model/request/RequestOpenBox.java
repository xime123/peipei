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
 * @Title: RequestOpenBox.java 
 *
 * @Description: 开宝箱
 *
 * @author allen  
 *
 * @date 2015-3-11 下午5:28:46 
 *
 * @version V1.0   
 */
public class RequestOpenBox extends AsnBase implements ISocketMsgCallBack {
	private IOpenBox callback;

	public void openBox(byte[] auth, int ver, int uid, IOpenBox callback) {
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqOpenBox_cid_VALUE;

		byte[] body = getRoom(auth, ver, uid);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] getRoom(byte[] auth, int ver, int uid) {
		Gogirl.ReqOpenBox.Builder builder = Gogirl.ReqOpenBox.newBuilder();
		builder.setUid(uid);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqOpenBox_cid_VALUE)));

		Gogirl.ReqOpenBox msg = builder.build();
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
					Gogirl.RspOpenBox rsp = Gogirl.RspOpenBox.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null) {
							callback.resultOpenBox(rsp.getRetcode(), rsp.getLeftboxnum(), new String(rsp.getPicurl().toByteArray()), new String(rsp
									.getResname().toByteArray()));
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
			callback.resultOpenBox(resultCode, 0, "", "");
		}
	}

	public interface IOpenBox {
		public void resultOpenBox(int retCode, int num, String key, String name);
	}
}
