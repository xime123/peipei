package com.tshang.peipei.activity.space;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.ImageDetailActivity;
import com.tshang.peipei.activity.dialog.GoLoginDialog;
import com.tshang.peipei.activity.mine.MineShowAllGiftListActivity;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseTimes;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAParseRspData;
import com.tshang.peipei.base.babase.BAParseRspData.ContentData;
import com.tshang.peipei.base.emoji.ParseMsgUtil;
import com.tshang.peipei.model.biz.main.MainMySpaceBiz;
import com.tshang.peipei.model.biz.user.UserResendBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackAddTopic;
import com.tshang.peipei.model.bizcallback.BizCallBackAppreciateTopic;
import com.tshang.peipei.model.space.SpaceUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.operate.AppreciateOperate;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/*
 *类        名 : SpaceCustomAdapter.java
 *功能描述 : 客人态 个人中心 ADAPTER
 *作　    者 : vactor
 *设计日期 : 2014-3-27 下午1:38:52
 *修改日期 : 
 *修  改   人: Jeff
 *修 改内容: 
 */
public class SpaceCustomAdapter extends ArrayListAdapter<TopicInfo> {

	private int oneWidth;

	private LinearLayout.LayoutParams params;
	private LinearLayout.LayoutParams params2;

	private Map<String, TopicCounterInfo> countMap = new HashMap<String, TopicCounterInfo>();
	private BAParseRspData parser;

	private AppreciateOperate appreciateOperate;
	private int mFriendUid = -1;

	protected DisplayImageOptions options;
	protected DisplayImageOptions options_head;
	private long time;

	public SpaceCustomAdapter(Activity context, int sex) {
		super(context);
		if (BAApplication.mLocalUserInfo != null) {
			appreciateOperate = AppreciateOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
		}
		int screenWidth = BasePhone.getScreenWidth(mContext);

		oneWidth = (screenWidth - BaseUtils.dip2px(mContext, 90));
		parser = new BAParseRspData();
		params = new LinearLayout.LayoutParams(oneWidth, oneWidth);
		params2 = new LinearLayout.LayoutParams(oneWidth / 2, oneWidth / 2);
	}

	/**
	 * 
	 * @param context
	 * @param fuid 用于判断是否是自己
	 * @param sex 用于判断礼物显示文字
	 */
	public SpaceCustomAdapter(Activity context, int fuid, int sex) {
		super(context);
		if (BAApplication.mLocalUserInfo != null) {
			appreciateOperate = AppreciateOperate.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "");
		}
		int screenWidth = BasePhone.getScreenWidth(mContext);

		oneWidth = (screenWidth - BaseUtils.dip2px(mContext, 90));
		parser = new BAParseRspData();
		params = new LinearLayout.LayoutParams(oneWidth, oneWidth);
		params2 = new LinearLayout.LayoutParams(oneWidth / 2, oneWidth / 2);
		mFriendUid = fuid;
		options_head = ImageOptionsUtils.GetHeadUidSmallRounded(context);
		options = ImageOptionsUtils.getImageKeyOptions(context);
	}

	public Map<String, TopicCounterInfo> getCountMap() {
		return countMap;
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 1;
		}
		return mList.size() + 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_custom_space_list, parent, false);
			mViewholer.ivHead = (ImageView) convertView.findViewById(R.id.item_custom_space_head);
			mViewholer.tvNick = (TextView) convertView.findViewById(R.id.item_custom_space_nick);
			mViewholer.tvCreateTime = (TextView) convertView.findViewById(R.id.item_custom_space_create_time);
			mViewholer.tvContent = (TextView) convertView.findViewById(R.id.item_custom_space_tv_content);
			mViewholer.gridview = (GridView) convertView.findViewById(R.id.item_custom_space_gridview);
			mViewholer.ivOneImage = (ImageView) convertView.findViewById(R.id.item_custom_space_list_rc_imageview);
			mViewholer.txtUp = (TextView) convertView.findViewById(R.id.item_fcustom_space_tv_appreciate);
			mViewholer.txtReply = (TextView) convertView.findViewById(R.id.item_custom_space_tv_reply);
			mViewholer.relReply = (LinearLayout) convertView.findViewById(R.id.item_custom_space_ll_reply);
			mViewholer.tvTopicStatus = (TextView) convertView.findViewById(R.id.item_space_tv_write_status);
			mViewholer.llShowData = (LinearLayout) convertView.findViewById(R.id.ll_show_data);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		try {
			if (position < mList.size()) {
				mViewholer.llShowData.setVisibility(View.VISIBLE);
				final TopicInfo topicInfo = mList.get(position);
				if (null != topicInfo) {
					if (topicInfo.id.intValue() == -100) {
						mViewholer.tvTopicStatus.setVisibility(View.VISIBLE);
					} else {
						mViewholer.tvTopicStatus.setVisibility(View.GONE);
					}
					mViewholer.tvTopicStatus.setOnClickListener(new ReSendClickListener(topicInfo, position, mViewholer.tvTopicStatus));
					GoGirlDataInfoList dataInfoList = topicInfo.topiccontentlist;
					final ContentData data = parser.parseTopicInfo(mContext, dataInfoList, topicInfo.sex.intValue());
					mViewholer.txtReply.setOnClickListener(new ClickListener(position, topicInfo));
					//gridview 数据 adapter
					if (null == mViewholer.gridview.getAdapter()) {
						SpaceCustomGridViewAdapter adpater = new SpaceCustomGridViewAdapter(mContext, data, topicInfo.uid.intValue(),
								topicInfo.sex.intValue());
						mViewholer.gridview.setAdapter(adpater);
					} else {
						SpaceCustomGridViewAdapter adapter = (SpaceCustomGridViewAdapter) mViewholer.gridview.getAdapter();
						adapter.freshData(data);
					}
					//设置gridview的显示和隐藏,以及只有一张图片时的UI,注意这里顺序不能调整
					if (data.getImageList().size() == 1) {
						if (data.getType() == BAConstants.MessageType.UPLOAD_PHOTO.getValue()) {//礼物，上传照片，还有一种就是发布心情和加照片
							String key = data.getImageList().get(0) + BAConstants.LOAD_210_APPENDSTR;
							mViewholer.ivOneImage.setTag(key);
							//					mViewholer.ivOneImage.setImageResource(R.drawable.main_img_defaultpic_big);
							imageLoader.displayImage("http://" + key, mViewholer.ivOneImage, options);
							//					imageLoaderMy.addTask(key, mViewholer.ivOneImage);
							//发布心情显示，其他都不显示  三种动态，上传照片，礼物不显示
							mViewholer.relReply.setVisibility(View.GONE);
							mViewholer.ivOneImage.setLayoutParams(params2);
							mViewholer.ivOneImage.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									intentImageDetail(data.getImageList());
								}
							});
						} else if (data.getType() == BAConstants.MessageType.GIFT.getValue()) {
							String key = data.getImageList().get(0) + BAConstants.LOAD_180_APPENDSTR;
							mViewholer.ivOneImage.setTag(key);
							imageLoader.displayImage("http://" + key, mViewholer.ivOneImage, options);
							mViewholer.ivOneImage.setLayoutParams(params2);
							mViewholer.relReply.setVisibility(View.GONE);
							mViewholer.ivOneImage.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {

									Bundle bundle = new Bundle();
									bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, topicInfo.uid.intValue());
									bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, topicInfo.sex.intValue());
									BaseUtils.openActivity(mContext, MineShowAllGiftListActivity.class, bundle);
								}
							});
						} else {//相册动态
							mViewholer.relReply.setVisibility(View.VISIBLE);
							if (data.getImageList().get(0).contains("sdcard")) {
								mViewholer.ivOneImage.setImageBitmap(BaseFile.getImageFromFile(data.getImageList().get(0)));
							} else {
								String key = data.getImageList().get(0) + "@false@500@500";
								mViewholer.ivOneImage.setTag(key);
								//						mViewholer.ivOneImage.setImageResource(R.drawable.main_img_defaultpic_big);
								//						imageLoaderMy.addTask(key, mViewholer.ivOneImage);
								imageLoader.displayImage("http://" + key, mViewholer.ivOneImage, options);
							}
							mViewholer.ivOneImage.setLayoutParams(params);
							mViewholer.ivOneImage.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									intentImageDetail(data.getImageList());
								}
							});
						}

						mViewholer.gridview.setVisibility(View.GONE);
						//显示一张图片
						mViewholer.ivOneImage.setVisibility(View.VISIBLE);

					} else {
						//显示gridview
						int size = data.getImageList().size();
						if (size == 2 || size == 4) {
							mViewholer.gridview.setNumColumns(2);
						} else {
							mViewholer.gridview.setNumColumns(3);
						}
						mViewholer.gridview.setVisibility(View.VISIBLE);
						mViewholer.ivOneImage.setVisibility(View.GONE);

						if (data.getType() == BAConstants.MessageType.UPLOAD_PHOTO.getValue()
								|| data.getType() == BAConstants.MessageType.GIFT.getValue()) {
							mViewholer.relReply.setVisibility(View.GONE);
						} else {
							mViewholer.relReply.setVisibility(View.VISIBLE);
						}

					}
					//判断是否已经点过赞了
					boolean flag = false;
					if (appreciateOperate != null)
						flag = appreciateOperate.isExist(topicInfo.topicid.intValue(), topicInfo.uid.intValue());
					if (flag) {
						mViewholer.txtUp.setOnClickListener(null);
						mViewholer.txtUp.setTextColor(mContext.getResources().getColor(R.color.peach));
						setTextViewLeftDrawable(R.drawable.person_icon_praise, mViewholer.txtUp);
					} else {
						setTextViewLeftDrawable(R.drawable.person_icon_praise_un, mViewholer.txtUp);
						mViewholer.txtUp.setTextColor(mContext.getResources().getColor(R.color.black));
						mViewholer.txtUp.setOnClickListener(new ClickListener(position, topicInfo));
					}

					// 显示计数
					String key = topicInfo.topicid.intValue() + "" + topicInfo.uid.intValue();
					TopicCounterInfo countInfo = countMap.get(key);

					if (null != countInfo) {
						mViewholer.txtUp.setText(countInfo.appreciatenum.intValue() + "");
						mViewholer.txtReply.setText(countInfo.commentnum.intValue() + "");
					}

					String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
							topicInfo.uid.intValue());

					mViewholer.tvNick.setText(TextUtils.isEmpty(alias) ? new String(topicInfo.nick) : alias);
					String tt = BaseTimes.getDiffTime2(Long.valueOf(topicInfo.createtime.longValue() * 1000));
					mViewholer.tvCreateTime.setText(tt);
					mViewholer.tvContent.setText(ParseMsgUtil.convetToHtml(data.getContent(), mContext, BaseUtils.dip2px(mContext, 24)));

					String uidKey = topicInfo.uid.intValue() + BAConstants.LOAD_HEAD_UID_APPENDSTR;
					mViewholer.ivHead.setTag(uidKey);
					imageLoader.displayImage("http://" + uidKey, mViewholer.ivHead, options_head);
					if (data.getType() != BAConstants.MessageType.GIFT.getValue()) {
						mViewholer.gridview.setOnItemClickListener(new GridViewOnItemClickListner(data.getImageList(), false, topicInfo.uid
								.intValue(), topicInfo.sex.intValue()));
					} else {
						mViewholer.gridview.setOnItemClickListener(new GridViewOnItemClickListner(data.getImageList(), true,
								topicInfo.uid.intValue(), topicInfo.sex.intValue()));
					}

					mViewholer.ivHead.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (topicInfo.uid.intValue() == mFriendUid) {
								return;
							}

							SpaceUtils.toSpaceCustom(mContext, topicInfo.uid.intValue(), topicInfo.sex.intValue());
						}
					});
				}
			} else {//这样处理就是防止头部不显示出来
				mViewholer.llShowData.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	private void intentImageDetail(ArrayList<String> imglist) {
		Bundle bundle = new Bundle();
		bundle.putInt(ImageDetailActivity.POSITION, 0);
		bundle.putStringArrayList(ImageDetailActivity.EXTRA_IMAGE, imglist);
		bundle.putBoolean(ImageDetailActivity.ISREPORT, true);
		bundle.putInt(ImageDetailActivity.PIC_UID, mFriendUid);
		BaseUtils.openActivity(mContext, ImageDetailActivity.class, bundle);
	}

	final class ViewHoler {
		LinearLayout llShowData;
		TextView txtReply;
		TextView txtUp;
		ImageView ivHead;
		ImageView ivOneImage;
		TextView tvNick;
		TextView tvCreateTime;
		TextView tvContent;
		GridView gridview;
		LinearLayout relReply;
		TextView tvTopicStatus;

	}

	//gridview 点击
	class GridViewOnItemClickListner implements OnItemClickListener {

		public ArrayList<String> list;
		private boolean isGift;
		private int uid;
		private int sex;

		public GridViewOnItemClickListner(ArrayList<String> list, boolean isGift, int uid, int sex) {
			this.list = list;
			this.isGift = isGift;
			this.uid = uid;
			this.sex = sex;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (!isGift) {
				Bundle bundle = new Bundle();
				bundle.putInt(ImageDetailActivity.POSITION, arg2);
				bundle.putStringArrayList(ImageDetailActivity.EXTRA_IMAGE, list);
				BaseUtils.openActivity(mContext, ImageDetailActivity.class, bundle);
			} else {
				Bundle bundle = new Bundle();
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERID, uid);
				bundle.putInt(BAConstants.IntentType.MAINHALLFRAGMENT_USERSEX, sex);
				BaseUtils.openActivity(mContext, MineShowAllGiftListActivity.class, bundle);
			}
		}

	}

	//刷新计数
	public void freshCount(Map<String, TopicCounterInfo> map) {
		this.countMap = map;
		this.notifyDataSetChanged();
	}

	class ClickListener implements View.OnClickListener {

		TopicInfo topicInfo;
		int position;

		//		private Dialog mDialog;

		public ClickListener(int position, TopicInfo topicInfo) {
			this.topicInfo = topicInfo;
			this.position = position;
			if (topicInfo.id.intValue() < 0) {
				return;
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.item_custom_space_tv_reply:
				if (BAApplication.mLocalUserInfo == null) {
					new GoLoginDialog(mContext, android.R.style.Theme_Translucent_NoTitleBar).showDialog();
				} else {
					if (topicInfo.id.intValue() < 0) {
						return;
					}
					Bundle bundle = new Bundle();
					String key = topicInfo.id + "" + topicInfo.uid;//发帖人的UID + 帖子的id
					bundle.putInt(SpaceCustomDetailActivity.TOPICID, topicInfo.id.intValue());
					bundle.putInt(SpaceCustomDetailActivity.TOPICUID, topicInfo.uid.intValue());
					bundle.putBoolean(SpaceCustomDetailActivity.ISADDCOMMENT, true);
					TopicCounterInfo countInfo = getCountMap().get(key);
					int appreciatenum = 0;
					int commentnum = 0;
					if (countInfo != null) {
						appreciatenum = countInfo.appreciatenum.intValue();
						commentnum = countInfo.commentnum.intValue();
					}
					bundle.putInt(SpaceCustomDetailActivity.APPRECIATENUM, appreciatenum);
					bundle.putInt(SpaceCustomDetailActivity.REPLYNUM, commentnum);
					BaseUtils.openActivity(mContext, SpaceCustomDetailActivity.class, bundle);
				}
				break;
			case R.id.item_fcustom_space_tv_appreciate:
				//一秒内连续点击不做处理
				if (System.currentTimeMillis() - time > 1000) {
					if (BAApplication.mLocalUserInfo != null) {
						time = System.currentTimeMillis();
						int topicId = topicInfo.topicid.intValue();
						int topicUid = topicInfo.uid.intValue();
						final String appreciateKey = topicId + "" + topicUid;

						int act = 1;
						//点赞过来
						MainMySpaceBiz space = new MainMySpaceBiz();
						space.appreciate(mContext, topicId, topicUid, act, new BizCallBackAppreciateTopic() {

							@Override
							public void appreciateCallBack(int retCode) {
								if (retCode == 0) {
									TopicCounterInfo countInfo = countMap.get(appreciateKey);
									if (null != countInfo) {
										countInfo.appreciatenum = BigInteger.valueOf((countInfo.appreciatenum.intValue() + 1));
									}
									handle.sendEmptyMessage(0x11);
								}
							}
						});
					} else {
						new GoLoginDialog(mContext, android.R.style.Theme_Translucent_NoTitleBar).showDialog();
					}
				}
				break;
			}
		}
	}

	private Handler handle = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 0x11:
				notifyDataSetChanged();
				break;
			case 0x12:
				TextView tv = (TextView) msg.obj;
				tv.setText("发送失败");
				setTextViewLeftDrawable(R.drawable.person_img_sendfailed, tv);
				break;

			default:
				break;
			}
		}

	};

	private class ReSendClickListener implements OnClickListener, BizCallBackAddTopic {
		private TopicInfo topicInfo;
		private int Position;
		private TextView tv;

		public ReSendClickListener(TopicInfo topicInfo, int position, TextView tv) {
			this.topicInfo = topicInfo;
			Position = position;
			this.tv = tv;
		}

		@Override
		public void onClick(View v) {
			if (tv.getText().equals("正在发送")) {
				return;
			}
			tv.setText("正在发送");
			setTextViewLeftDrawable(R.drawable.person_img_sendout, tv);
			GoGirlDataInfoList dataInfoList = topicInfo.topiccontentlist;
			if (topicInfo.sex == null) {
				return;
			}
			ContentData data = parser.parseTopicInfo(mContext, dataInfoList, topicInfo.sex.intValue());

			//			String imageKeys = "";//publishEntity.getImageKeys()
			ArrayList<String> images = data.getImageList();
			//			if (!TextUtils.isEmpty(imageKeys)) {
			//				images = imageKeys.split(";");
			//			}

			byte[] auth = BAApplication.mLocalUserInfo.auth;
			int uid = BAApplication.mLocalUserInfo.uid.intValue();
			String city = "深圳";

			String time = String.valueOf(topicInfo.createtime.longValue());

			int la = 0;
			int lo = 0;
			String province = "广东省";
			String content = "";
			if (dataInfoList != null) {
				GoGirlDataInfo gireDataInfo = (GoGirlDataInfo) dataInfoList.get(0);
				content = new String(gireDataInfo.data);
			}

			UserResendBiz userResendBiz = new UserResendBiz(mContext);
			//多张图片时
			if (null != images && images.size() > 1) {
				//				List<String> photoList = images;//BaseTools.getList(images)
				int topicId = topicInfo.topicid.intValue();
				userResendBiz.resendTopicIfHasManyPhotos(auth, BAApplication.app_version_code, uid, city, topicId, Long.valueOf(time), la, lo,
						province, content, images, this);
			} else {
				//1张图片
				String bitmapPath = null;
				if (null != images && images.size() == 1) {
					bitmapPath = images.get(0);
					userResendBiz.addTopicIfHasOnePhoto(auth, BAApplication.app_version_code, uid, city, Long.valueOf(time), la, lo, province,
							content, bitmapPath, this);
					//没有图片
				} else {
					userResendBiz.addTopicIfHasOnePhoto(auth, BAApplication.app_version_code, uid, city, Long.valueOf(time), la, lo, province,
							content, bitmapPath, this);
				}

			}
		}

		@Override
		public void addTopicCallBack(int retCode, int userTopicId, int charmnum, GoGirlDataInfoList list) {
			if (retCode == 0) {
//				MobclickAgent.onEvent(mContext, "DiANJiDianZanAnNiuCiShu");
				topicInfo.id = BigInteger.valueOf(userTopicId);

				//PublishOperate.getInstance(mContext, BATools.getDB(mContext)).deleteTopicId(String.valueOf(topicInfo.createtime));
				//PublishOperate.getInstance(mContext, BATools.getDB(mContext)).deleteTopicId(String.valueOf(topicInfo.topicid));
				handle.sendMessage(handle.obtainMessage(0x11, 0, charmnum));
			} else {
				handle.sendMessage(handle.obtainMessage(0x12, Position, Position, tv));
			}

		}
	}

	private void setTextViewLeftDrawable(int source, TextView tv) {
		if (tv != null) {
			Drawable drawable = mContext.getResources().getDrawable(source);
			if (drawable != null) {
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
				tv.setCompoundDrawables(drawable, null, null, null);
			}
		}
	}

}
