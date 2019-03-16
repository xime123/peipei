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
 * @Title: RequestGetShowApi.java 
 *
 * @Description: 获取秀场列表
 *
 * @author allen  
 *
 * @date 2015-1-15 下午2:52:57 
 *
 * @version V1.0   
 */
public class RequestSetMemberRole extends AsnBase implements ISocketMsgCallBack {

	private ISetMemberRole callBack;
	private int act;

	public void setMemberRole(byte[] auth, int ver, int act, int uid, int roomuid, int roomid, ISetMemberRole callBack) {
		this.act = act;
		String host = PEIPEI_PRODUCT_HOST;
		if (BAConstants.IS_TEST) {
			host = PEIPEI_TEST_HOST;
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqSetMemberRole_cid_VALUE;
		this.callBack = callBack;

		byte[] body = memberRole(auth, ver, act, uid, roomuid, roomid);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] memberRole(byte[] auth, int ver, int act, int uid, int roomuid, int roomid) {
		Gogirl.ReqSetMemberRole.Builder builder = Gogirl.ReqSetMemberRole.newBuilder();
		builder.setAct(act);
		builder.setRoleuid(uid);
		builder.setSelfuid(roomuid);
		builder.setRoomid(roomid);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqSetMemberRole_cid_VALUE)));

		Gogirl.ReqSetMemberRole msg = builder.build();
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

				Gogirl.RspSetMemberRole rsp;
				try {
					rsp = Gogirl.RspSetMemberRole.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callBack != null) {
							callBack.resultMemberRole(retCode, act);
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
		if (callBack != null) {
			callBack.resultMemberRole(resultCode, act);
		}
	}

	public interface ISetMemberRole {
		public void resultMemberRole(int retCode, int act);
	}
}
