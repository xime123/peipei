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
import com.tshang.peipei.protocol.protobuf.ByteString;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestDelateShow.java 
 *
 * @Description: 秀场举报
 *
 * @author allen  
 *
 * @date 2015-2-4 下午4:33:50 
 *
 * @version V1.0   
 */
public class RequestDelateShow extends AsnBase implements ISocketMsgCallBack {

	private IDelateShow callback;

	public void getDelateShow(byte[] auth, int ver, int roomid, int Selfuid, int uid, String other, IDelateShow callback) {
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqDelateShow_cid_VALUE;

		byte[] body = delateShow(auth, ver, roomid, Selfuid, uid, other);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] delateShow(byte[] auth, int ver, int roomid, int Selfuid, int uid, String other) {
		Gogirl.ReqDelateShow.Builder builder = Gogirl.ReqDelateShow.newBuilder();
		builder.setRoomid(roomid);
		builder.setSelfuid(Selfuid);
		builder.setHostuid(uid);
		builder.setOtherreason(ByteString.copyFromUtf8(other));
		builder.setReasonid(0);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqDelateShow_cid_VALUE)));
		Gogirl.ReqDelateShow msg = builder.build();
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
					Gogirl.RspDelateShow rsp = Gogirl.RspDelateShow.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null)
							callback.getDelateShowResult(retCode);
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
			callback.getDelateShowResult(resultCode);
	}

	public interface IDelateShow {
		public void getDelateShowResult(int retCode);
	}
}
