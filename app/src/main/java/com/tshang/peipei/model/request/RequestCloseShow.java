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
 * @Title: RequestCloseShow.java 
 *
 * @Description: 关闭秀场
 *
 * @author allen  
 *
 * @date 2015-2-4 下午1:46:52 
 *
 * @version V1.0   
 */
public class RequestCloseShow extends AsnBase implements ISocketMsgCallBack {

	private ICloseShow callback;

	public void closeShow(byte[] auth, int ver, int roomid, int uid, int act, ICloseShow callback) {
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqCloseShow_cid_VALUE;

		byte[] body = getbuild(auth, ver, roomid, uid, act);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] getbuild(byte[] auth, int ver, int roomid, int uid, int act) {
		Gogirl.ReqCloseShow.Builder builder = Gogirl.ReqCloseShow.newBuilder();
		builder.setRoomid(roomid);
		builder.setUid(uid);
		builder.setAct(act);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqCloseShow_cid_VALUE)));
		Gogirl.ReqCloseShow msg = builder.build();
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
					Gogirl.RspCloseShow rsp = Gogirl.RspCloseShow.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null)
							callback.resultCloseShow(retCode);
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
			callback.resultCloseShow(resultCode);
	}

	public interface ICloseShow {
		public void resultCloseShow(int retCode);
	}

}
