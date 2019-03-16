package com.tshang.peipei.model.request;

import java.io.File;
import java.io.IOException;

import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.entity.ShowChatEntity;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.BaseProtobuf;
import com.tshang.peipei.protocol.Gogirl;
import com.tshang.peipei.protocol.Gogirl.GoGirlBaseMsg;
import com.tshang.peipei.protocol.Gogirl.GoGirlChatDataP;
import com.tshang.peipei.protocol.Gogirl.GoGirlDataInfoP;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.protobuf.ByteString;
import com.tshang.peipei.protocol.protobuf.InvalidProtocolBufferException;

/**
 * @Title: RequestShowChat.java 
 *
 * @Description: 秀场聊天
 *
 * @author allen  
 *
 * @date 2015-1-19 下午3:21:04 
 *
 * @version V1.0   
 */
public class RequestShowChat extends AsnBase implements ISocketMsgCallBack {

	private IShowChat callback;
	private int screenType;
	private GoGirlChatDataP.Builder chatdataPBuilder;
	private GoGirlDataInfoP.Builder info;
	private ShowChatEntity chatEntity;
	private int length;

	public void showChat(byte[] auth, int ver, int roomuid, int fromuid, int roomid, String fromnick, int fromsex, int fromtype, byte[] data,
			String dataid, int length, int type, int screenType, ShowChatEntity chatEntity, IShowChat callback) {
		this.callback = callback;
		this.screenType = screenType;
		this.chatEntity = chatEntity;
		this.length = length;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/appbiz/api?cid=" + GoGirlBaseMsg.PacketId.ReqShowChat_cid_VALUE;

		byte[] body = sentChat(auth, ver, roomuid, fromuid, roomid, fromnick, fromsex, fromtype, data, dataid, length, type, screenType);
		if (body != null) {
			byte[] requests = http_encode_post(url, host, body);
			PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
			AppQueueManager.getInstance().addRequest(request);
		}
	}

	private byte[] sentChat(byte[] auth, int ver, int roomuid, int fromuid, int roomid, String fromnick, int fromsex, int fromtype, byte[] data,
			String dataid, int length, int type, int screenType) {
		Gogirl.ReqShowChat.Builder builder = Gogirl.ReqShowChat.newBuilder();
		builder.setOwneruid(roomuid);
		builder.setFrom(fromuid);
		builder.setType(screenType);
		builder.setRoomid(roomid);
		chatdataPBuilder = GoGirlChatDataP.newBuilder();
		long time = System.currentTimeMillis() / 1000;
		long times = System.currentTimeMillis() % 1000 * 1000;
		chatdataPBuilder.setCreatetimes((int) time);
		chatdataPBuilder.setCreatetimeus((int) times);
		chatdataPBuilder.setFromnick(ByteString.copyFromUtf8(fromnick));
		chatdataPBuilder.setFromsex(fromsex);
		chatdataPBuilder.setFromtype(fromtype);
		chatdataPBuilder.setFrom(fromuid);
		chatdataPBuilder.setTo(0);
		chatdataPBuilder.setTonick(ByteString.copyFromUtf8(""));
		chatdataPBuilder.setTotype(fromtype);
		chatdataPBuilder.setTosex(0);

		info = GoGirlDataInfoP.newBuilder();
		info.setData(ByteString.copyFrom(data));
		info.setDataid(ByteString.copyFromUtf8(dataid));
		info.setDatainfo(length);
		info.setType(type);

		chatdataPBuilder.addChatdatalist(info);

		builder.setChatdata(chatdataPBuilder);
		builder.setBasemsg(BaseProtobuf.setBaseMsg(auth, ver, GoGirlBaseMsg.PacketId.valueOf(GoGirlBaseMsg.PacketId.ReqShowChat_cid_VALUE)));

		return builder.build().toByteArray();
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
					Gogirl.RspShowChat rsp = Gogirl.RspShowChat.parseFrom(tempByte);

					if (info.getType() == 2 || info.getType() == 45) {//语音是修改
						String[] str = new String(rsp.getRetmsg().toByteArray()).split("/");
						File file = new File(SdCardUtils.getInstance().getDirectory(0), str[str.length - 1]);
						file.createNewFile();

						chatEntity.voiceFile = new String(rsp.getRetmsg().toByteArray());
						chatEntity.voiceLength = length;

						BaseFile.saveByteToFile(info.getData().toByteArray(), file.getAbsolutePath());
						info.setData(rsp.getRetmsg());
						chatdataPBuilder.setChatdatalist(0, info);
					}
					int retCode = rsp.getRetcode();
					if (checkRetCode(retCode)) {
						if (callback != null) {
							callback.showChatcallback(retCode, screenType, chatdataPBuilder.build());
						}
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (callback != null) {
			callback.showChatcallback(resultCode, screenType, chatdataPBuilder.build());
		}
	}

	public interface IShowChat {
		public void showChatcallback(int retcode, int screenType, GoGirlChatDataP chatDataP);
	}
}
