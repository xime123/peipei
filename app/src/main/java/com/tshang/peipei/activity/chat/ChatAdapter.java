package com.tshang.peipei.activity.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.FailedReSendListener;
import com.tshang.peipei.activity.chat.adapter.ViewBaseChatItemAdapter.NickOnClickListener;
import com.tshang.peipei.activity.chat.adapter.ViewBurnImageChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewBurnImageChatItemAdapter.ChatBurnImageInterface;
import com.tshang.peipei.activity.chat.adapter.ViewBurnVoiceChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewCreatRedPacketChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewDareChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewDareResultItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewFingerGuessingChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewGetRedPacketChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewGoddessSkillChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewGoddessSkillChatItemAdapter.SkillInviteResCallBack;
import com.tshang.peipei.activity.chat.adapter.ViewGrapSolitaireRedPacketChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewHaremFaceChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewHaremFaceChatItemAdapter.OnClickListenrHaremVoice;
import com.tshang.peipei.activity.chat.adapter.ViewHelpItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewImageChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewImageTextChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewJoinHaremChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewReceiveGiftsChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewRewardChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewSkillOrderChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewSolitaireRedPacketChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewSystemNotifyInfoChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewTextChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewTruthChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewVersionLowerItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewVideoChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewVoiceChatItemAdapter;
import com.tshang.peipei.activity.chat.adapter.ViewVoiceChatItemAdapter.ChatClickVoiceInterface;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: 聊天内容适配器
 *
 * @Description: 对聊天内容进行匹配处理 
 *
 * @author allen  
 *
 * @date 2014-3-26 下午4:47:19 
 *
 * @version V1.0   
 */
public class ChatAdapter extends ArrayListAdapter<ChatDatabaseEntity> {
	private BAHandler mHandler;
	private ViewBaseChatItemAdapter viewTextChatItemAdapter;
	private ViewBaseChatItemAdapter viewImageChatItemAdapater;
	private ViewBaseChatItemAdapter viewVersionLowerItemAdapter;
	private ViewBaseChatItemAdapter viewVoiceChatItemAdapter;
	private ViewBaseChatItemAdapter viewVideoChatItemAdapter;
	private ViewBaseChatItemAdapter viewHelpItemAdapter;
	private ViewBaseChatItemAdapter viewReceiveGiftsChatItemAdapter;
	private ViewBaseChatItemAdapter viewFingerGuessingChatItemAdapter;
	private ViewBaseChatItemAdapter viewBurnImageChatItemAdapter;
	private ViewBaseChatItemAdapter viewBurnVoiceChatItemAdapter;
	private ViewBaseChatItemAdapter viewJoinHaremChatItemAdapter;
	private ViewBaseChatItemAdapter viewCreatRedPacketChatItemAdapter;
	private ViewBaseChatItemAdapter viewGetRedPacketChatItemAdapter;
	private ViewBaseChatItemAdapter viewSkillOrderChatItemAdapter;
	private ViewHaremFaceChatItemAdapter viewHaremFaceChatItemAdapter;
	private ViewSystemNotifyInfoChatItemAdapter viewSystemNotifyInfoChatItemAdapter;
	private ViewDareChatItemAdapter viewDareChatItemAdapter;
	private ViewImageTextChatItemAdapter viewDareNumChatItemAdapter;
	private ViewDareResultItemAdapter viewDareResultItemAdapter;
	private ViewTruthChatItemAdapter viewTruthChatItemAdapter;//真心话
	private ViewRewardChatItemAdapter viewRewardChatItemAdapter;
	private ViewGoddessSkillChatItemAdapter viewGoddessSkillChatItemAdapter; //女神技
	private ViewSolitaireRedPacketChatItemAdapter viewSolitaireRedPacketChatItemAdapter; //红包接龙
	private ViewGrapSolitaireRedPacketChatItemAdapter viewGrapSolitaireRedPacketChatItemAdapter; //抢接龙红包

	private boolean isGroupChat;

	public ChatAdapter(Activity context, int fuid, String right, int fSex, String nick, String friendNick, Boolean isGroupChat, BAHandler mHandler,
			ChatClickVoiceInterface callBack, ChatBurnImageInterface burnImageCallBack, FailedReSendListener reListener,
			OnClickListenrHaremVoice listener, SkillInviteResCallBack skillInviteCallBack, NickOnClickListener nickOnClickListener) {
		super(context);
		this.isGroupChat = isGroupChat;
		this.mHandler = mHandler;
		viewTextChatItemAdapter = new ViewTextChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, nickOnClickListener);
		viewImageChatItemAdapater = new ViewImageChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, nickOnClickListener);
		viewVersionLowerItemAdapter = new ViewVersionLowerItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, nickOnClickListener);
		viewVoiceChatItemAdapter = new ViewVoiceChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, callBack,
				nickOnClickListener);
		viewVideoChatItemAdapter = new ViewVideoChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, mHandler,
				nickOnClickListener);
		viewHelpItemAdapter = new ViewHelpItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, nickOnClickListener);
		viewReceiveGiftsChatItemAdapter = new ViewReceiveGiftsChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener,
				nickOnClickListener);
		viewFingerGuessingChatItemAdapter = new ViewFingerGuessingChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, mHandler,
				nickOnClickListener);
		viewBurnImageChatItemAdapter = new ViewBurnImageChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, mHandler,
				burnImageCallBack, nickOnClickListener);
		viewBurnVoiceChatItemAdapter = new ViewBurnVoiceChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, burnImageCallBack,
				nickOnClickListener);
		viewJoinHaremChatItemAdapter = new ViewJoinHaremChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, mHandler,
				nickOnClickListener);
		viewCreatRedPacketChatItemAdapter = new ViewCreatRedPacketChatItemAdapter(context, fuid, fSex, friendNick, true, null, mHandler,
				nickOnClickListener);
		viewGetRedPacketChatItemAdapter = new ViewGetRedPacketChatItemAdapter(context, fuid, fSex, friendNick, true, null, nickOnClickListener);
		viewSkillOrderChatItemAdapter = new ViewSkillOrderChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, null, mHandler,
				nickOnClickListener);
		viewHaremFaceChatItemAdapter = new ViewHaremFaceChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, reListener, listener,
				nickOnClickListener);
		viewSystemNotifyInfoChatItemAdapter = new ViewSystemNotifyInfoChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, null, mHandler,
				nickOnClickListener);
		viewDareChatItemAdapter = new ViewDareChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, null, mHandler, nickOnClickListener);
		viewDareNumChatItemAdapter = new ViewImageTextChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, null, mHandler,
				nickOnClickListener);
		viewDareResultItemAdapter = new ViewDareResultItemAdapter(context, fuid, fSex, friendNick, isGroupChat, null, nickOnClickListener);
		viewTruthChatItemAdapter = new ViewTruthChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, mHandler, null, nickOnClickListener);
		viewRewardChatItemAdapter = new ViewRewardChatItemAdapter(context, fuid, fSex, nick, false, null, nickOnClickListener);
		viewGoddessSkillChatItemAdapter = new ViewGoddessSkillChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, null,
				skillInviteCallBack, nickOnClickListener);
		viewSolitaireRedPacketChatItemAdapter = new ViewSolitaireRedPacketChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, null,
				mHandler, nickOnClickListener);
		viewGrapSolitaireRedPacketChatItemAdapter = new ViewGrapSolitaireRedPacketChatItemAdapter(context, fuid, fSex, friendNick, isGroupChat, null,
				mHandler, nickOnClickListener);
	}

	@Override
	public int getItemViewType(int position) {
		if (mList == null) {
			return 0;
		}
		ChatDatabaseEntity msg = mList.get(position);
		if (msg == null) {
			return 0;
		}
		int type = msg.getType();
		return type;
	}

	/**
	 * 返回所有的layout的数量
	 * 
	 * */
	@Override
	public int getViewTypeCount() {
		return 103;//这个值一定要大于type的值
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ChatDatabaseEntity chatEntity = mList.get(position);
		int type = chatEntity.getType();
		switch (type) {
		case 0://纯文本信息k
		case 32://感兴趣技能
		case 62://真心话答案
		case 70://匿名 文本
			convertView = viewTextChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 1://在线图片
		case 3://离线图片
		case 71:
		case 73:
			convertView = viewImageChatItemAdapater.getView(position, convertView, parent, chatEntity);
			break;
		case 2://在线语音
		case 6://离线语音
		case 72:
		case 76:
			convertView = viewVoiceChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 10:
		case 11://阅后即焚图片
		case 80:
		case 81:
			convertView = viewBurnImageChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 12:
		case 13:
		case 82:
		case 83:
			convertView = viewBurnVoiceChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;

		case 19://收到礼物
		case 89:
			convertView = viewReceiveGiftsChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 21://猜拳
		case 28://带赌注猜拳
		case 41://银币猜拳
			convertView = viewFingerGuessingChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 22://进入帮助
		case 23://进入女王攻略
			convertView = viewHelpItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 25://视频文件
		case 85://匿名视频文件
			convertView = viewVideoChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 26://申请加入后宫
			convertView = viewJoinHaremChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 29:
			convertView = viewCreatRedPacketChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 30:
			convertView = viewGetRedPacketChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 31:
			convertView = viewSkillOrderChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 36:
		case 86:
			convertView = viewHaremFaceChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 49:
			convertView = viewSystemNotifyInfoChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 54:
			convertView = viewDareChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 53:
			convertView = viewDareNumChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 55:
			convertView = viewDareResultItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 61://真心话
			convertView = viewTruthChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 64://悬赏
			convertView = viewRewardChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 68://女神技
			convertView = viewGoddessSkillChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 100://发送接龙红包
			convertView = viewSolitaireRedPacketChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 101://抢接龙红包
			convertView = viewGrapSolitaireRedPacketChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		case 102: //红包过期
			convertView = viewGrapSolitaireRedPacketChatItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		default://版本太低不支持
			convertView = viewVersionLowerItemAdapter.getView(position, convertView, parent, chatEntity);
			break;
		}
		return convertView;
	}

	//改变发送成功或者失败的状态
	public void setStatusByLocalID(int status, long localID) {
		if (mList != null && mList.size() != 0) {
			for (ChatDatabaseEntity t : mList) {
				if (t.getMesLocalID() == localID) {
					if (status == 0) {
						t.setStatus(ChatStatus.SUCCESS.getValue());
					} else if (status == rspContMsgType.E_GG_BLACKLIST) {
						t.setStatus(ChatStatus.BLACK.getValue());
					} else if (status == 1) {
						t.setStatus(ChatStatus.SENDING.getValue());
					} else {
						t.setStatus(ChatStatus.FAILED.getValue());
					}
				}
			}
		}
	}

	public void setStatusByBurnId(int status, String burnId) {
		if (!TextUtils.isEmpty(burnId)) {
			for (ChatDatabaseEntity t : mList) {
				if (t.getMesSvrID().equals(burnId)) {
					t.setStatus(status);
				}
			}
		}
	}

	public void setDareByBurnId(int progress, String burnId) {
		if (!TextUtils.isEmpty(burnId)) {
			for (ChatDatabaseEntity t : mList) {
				if (t.getMesSvrID().equals(burnId)) {
					t.setProgress(progress);
					notifyDataSetChanged();
				}
			}
		}
	}

	/**
	 * 更新真心话
	 * @author Aaron
	 *
	 * @param progress
	 * @param burnId
	 */
	public void setTruthByBurnid(int progress, String burnId) {
		if (!TextUtils.isEmpty(burnId)) {
			for (ChatDatabaseEntity t : mList) {
				if (t.getMesSvrID().equals(burnId)) {
					t.setProgress(progress);
					notifyDataSetChanged();
				}
			}
		}
	}

	public void setSkillByBurnid(int progress, String burnId) {
		if (!TextUtils.isEmpty(burnId)) {
			for (ChatDatabaseEntity t : mList) {
				if (burnId.equals(t.getMesSvrID())) {
					t.setProgress(progress);
					this.notifyDataSetChanged();
				}
			}
		}
	}

	public void setSkillInviteTimeByBurnid(String burnId) {
		if (!TextUtils.isEmpty(burnId)) {
			for (ChatDatabaseEntity t : mList) {
				if (burnId.equals(t.getMesSvrID())) {
					notifyDataSetChanged();
				}
			}
		}
	}
}
