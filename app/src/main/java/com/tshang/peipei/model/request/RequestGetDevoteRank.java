package com.tshang.peipei.model.request;

import java.util.List;

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
 * @Title: RequestGetDevoteRank.java 
 *
 * @Description: 成员贡献排行 
 *
 * @author allen  
 *
 * @date 2015-1-22 下午4:02:05 
 *
 * @version V1.0   
 */
public class RequestGetDevoteRank extends AsnBase implements ISocketMsgCallBack {

	private IGetDevoteRank callback;
	private int type;

	public void getDevoteRank(byte[] auth, int ver, int roomid, int Selfuid, int uid, int start, int num, int type, IGetDevoteRank callback) {
		this.callback = callback;
		this.type = type;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqGetDevoteRank_cid_VALUE;

		byte[] body = devoteRank(auth, ver, roomid, Selfuid, uid, start, num, type);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] devoteRank(byte[] auth, int ver, int roomid, int Selfuid, int uid, int start, int num, int type) {
		Gogirl.ReqGetDevoteRank.Builder builder = Gogirl.ReqGetDevoteRank.newBuilder();
		builder.setRoomid(roomid);
		builder.setSelfuid(Selfuid);
		builder.setNum(num);
		builder.setHostuid(uid);
		builder.setStart(start);
		builder.setType(type);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqGetDevoteRank_cid_VALUE)));
		Gogirl.ReqGetDevoteRank msg = builder.build();
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
					Gogirl.RspGetDevoteRank rsp = Gogirl.RspGetDevoteRank.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null)
							callback.getDevoteRankResult(retCode, rsp.getIsend(), type, rsp.getMemberlistList());
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
			callback.getDevoteRankResult(resultCode, 0, type, null);
	}

	public interface IGetDevoteRank {
		public void getDevoteRankResult(int retCode, int end, int type, List<Gogirl.ShowRoomMemberInfo> getMemberlistList);
	}
}
