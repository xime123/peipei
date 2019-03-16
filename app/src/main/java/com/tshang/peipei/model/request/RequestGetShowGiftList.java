package com.tshang.peipei.model.request;

import java.util.List;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.BaseProtobuf;
import com.tshang.peipei.protocol.Gogirl;
import com.tshang.peipei.protocol.Gogirl.GiftDealInfoP;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestGetShowGiftList.java 
 *
 * @Description: 秀场动态
 *
 * @author allen  
 *
 * @date 2015-3-9 下午4:08:35 
 *
 * @version V1.0   
 */
public class RequestGetShowGiftList extends AsnBase implements ISocketMsgCallBack {

	private IGetShowGiftList callback;

	public void getShowGiftHistory(byte[] auth, int ver, int hostuid, int uid, int start, int num, IGetShowGiftList callback) {
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqGetShowGiftList_cid_VALUE;

		byte[] body = showHistoryData(auth, ver, hostuid, uid, start, num);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] showHistoryData(byte[] auth, int ver, int hostuid, int uid, int start, int num) {
		Gogirl.ReqGetShowGiftList.Builder builder = Gogirl.ReqGetShowGiftList.newBuilder();
		builder.setSelfuid(uid);
		builder.setHostuid(hostuid);
		builder.setNum(num);
		builder.setStart(start);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqGetShowGiftList_cid_VALUE)));
		Gogirl.ReqGetShowGiftList msg = builder.build();
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
					Gogirl.RspGetShowGiftList rsp = Gogirl.RspGetShowGiftList.parseFrom(tempByte);
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null)
							callback.resultShowGiftList(retCode, rsp.getIsend(), rsp.getGiftdeallistList());
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
			callback.resultShowGiftList(resultCode, 0, null);
	}

	public interface IGetShowGiftList {
		public void resultShowGiftList(int retCode, int end, List<GiftDealInfoP> list);
	}
}
