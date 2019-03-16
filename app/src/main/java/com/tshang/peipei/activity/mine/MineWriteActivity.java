package com.tshang.peipei.activity.mine;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.activity.EmojiPagerSet;
import com.tshang.peipei.activity.dialog.DeleteTopicPhotoDialog;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.activity.dialog.HintToastDialog;
import com.tshang.peipei.activity.mine.bean.NetworkPicEntity;
import com.tshang.peipei.activity.space.SpaceWritePhotoAdapter;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.IFileUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.model.biz.user.UserWriteBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackAddTopic;
import com.tshang.peipei.model.entity.PhotoEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.xutils.DownloadManager;
import com.tshang.peipei.model.xutils.DownloadService;
import com.tshang.peipei.model.xutils.HttpFactory;
import com.tshang.peipei.model.xutils.HttpRequestCallBack;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.view.PageControlView;
import com.tshang.peipei.view.UnScrollGridview;

import de.greenrobot.event.EventBus;

/**
 * @Title: SpaceWriteActivity.java 
 *
 * @Description: 写心情界面
 *
 * @author vactor
 *
 * @date 2014-4-9 下午5:47:58 
 *
 * @version V1.0   
 */
public class MineWriteActivity extends BaseActivity implements OnTouchListener, BizCallBackAddTopic, OnItemClickListener, TextWatcher {

	public static final String FROM_FLAG = "flage";
	public static final String PUBLIC_DYNAMIC = "public";
	public static final String PRIVATE_DYNAMIC = "private";
	public static final String OFFICIAL_DYNAMIC = "official";
	public static final String SIZE = "size";
	// 拍照的照片存储位置
	private final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/100MEDIA");
	// 拍照文件名,后缀以 index+".jpg"方式,index自增长
	private final String CAMERA_FILE_NAME = "/w_photo_camera_temp";
	private final int GET_PHOTO_BY_CAMERA = 1;
	private final int WRITE_UPLOAD = 2;
	private final int WRITE_PHOTO = 3;
	public static final int RESULT_CODE = 4;

	private TextView mTexMid;
	private LinearLayout mLinRight;
	private ImageView mImageRight;
	private TextView mTextRight;
	private EditText mSentText;
	private UnScrollGridview mGridview;
	private RelativeLayout mTakePhoto;
	private RelativeLayout mChoosePicure;
	private RelativeLayout mChooseEmoji;
	private RelativeLayout mEmojiPanel;
	private FrameLayout mFrameLayout;
	private TextView mTxtEmoj;
	private CheckBox mCheckBox;
	private LinearLayout mBottomLayout;
	//	private LinearLayout mCheckBoxLayout;
	private RelativeLayout mPublicETLayout;
	private LinearLayout mPrivateETLayout;
	private EditText mPublicET;
	private TextView mNumberTV;
	private TextView mTopicTV;

	private List<PhotoEntity> photoList;
	private File mTempCameraFile;
	private SpaceWritePhotoAdapter photoAdapter;
	private int index = 0;

	private boolean isPublicDynamic = false;//记录是否发布新动态

	private int position = 0;

	private String topicContent;
	private int type;//请求类型
	private int topicid = -1;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String tag = getIntent().getExtras().getString(FROM_FLAG);
		if (!TextUtils.isEmpty(tag) && PUBLIC_DYNAMIC.equals(tag) || OFFICIAL_DYNAMIC.equals(tag)) {
			isPublicDynamic = true;
		}
		initUi();
		setlistener();
		if (isPublicDynamic) {
			showSoftInput(mPublicET);
		} else
			showSoftInput(mSentText);

		if (!TextUtils.isEmpty(tag) && OFFICIAL_DYNAMIC.equals(tag)) {
			topicid = getIntent().getExtras().getInt("topicid");
			mTopicTV.setText("#" + getIntent().getExtras().getString("topic") + "#");
			mTopicTV.setVisibility(View.VISIBLE);
			mPublicET.setHint("");
			mPublicET.setGravity(Gravity.TOP | Gravity.CENTER);
		}

		photoList = new ArrayList<PhotoEntity>();

		photoAdapter = new SpaceWritePhotoAdapter(this, photoList);
		mGridview.setAdapter(photoAdapter);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void onEvent(NoticeEvent event) {
		super.onEvent(event);
		if (event.getFlag() == NoticeEvent.NOTICE3) {
			Message msg = Message.obtain();
			msg.what = WRITE_PHOTO;
			msg.obj = event.getObj();
			mHandler.sendMessage(msg);
		} else if (event.getFlag() == NoticeEvent.NOTICE86) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					MineWriteActivity.this.finish();
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == Activity.RESULT_OK) {
			switch (arg0) {
			case GET_PHOTO_BY_CAMERA:
				PhotoEntity photo = new PhotoEntity();
				photo.setPath(mTempCameraFile.getAbsolutePath());
				photoList.add(photo);
				photoAdapter.notifyDataSetChanged();
				break;
			}
		} else {
			switch (arg1) {
			case RESULT_CODE://动态发布完成
				this.finish();
				break;
			default:
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
		switch (msg.what) {
		case WRITE_UPLOAD:
			int retCode = msg.arg1;
			if (retCode == 0) {
				if (msg.arg2 > 0) {
					BaseUtils.showTost(this, "发布成功!魅力值增加" + msg.arg2);
				} else {
					BaseUtils.showTost(this, "发布成功!");
				}
			} else if (retCode == rspContMsgType.E_GG_FORBIT) {
				new HintToastDialog(this, R.string.limit_talk, R.string.ok).showDialog();
			} else {
				BaseUtils.showTost(this, "写贴失败 ");
			}
			NoticeEvent event = new NoticeEvent();
			event.setFlag(NoticeEvent.NOTICE26);
			EventBus.getDefault().postSticky(event);

			break;
		case WRITE_PHOTO:
			List<PhotoEntity> list = (List<PhotoEntity>) msg.obj;
			if (null != list) {
				photoList.addAll(list);
				photoAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	private void initUi() {
		mTexMid = (TextView) this.findViewById(R.id.title_tv_mid);
		mTexMid.setText(R.string.publish_feelings);
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		if (isPublicDynamic) {
			mBackText.setText(R.string.back);
		} else
			mBackText.setText(R.string.private_page);
		mBackText.setOnClickListener(this);
		mLinRight = (LinearLayout) this.findViewById(R.id.title_lin_right);
		mLinRight.setVisibility(View.VISIBLE);
		mLinRight.setOnClickListener(this);
		mImageRight = (ImageView) this.findViewById(R.id.title_iv_right);
		mImageRight.setVisibility(View.GONE);
		mTextRight = (TextView) this.findViewById(R.id.title_tv_right);
		mTextRight.setVisibility(View.VISIBLE);
		if (isPublicDynamic) {
			mTextRight.setText(R.string.write_next);
		} else {
			mTextRight.setText(R.string.publish);
		}
		mTakePhoto = (RelativeLayout) findViewById(R.id.write_text_photo_take_photos);
		mChoosePicure = (RelativeLayout) findViewById(R.id.write_text_photo_choose_picture);
		mChooseEmoji = (RelativeLayout) findViewById(R.id.write_text_photo_choose_emoji);
		mSentText = (EditText) findViewById(R.id.write_et_sent_texts);
		mEmojiPanel = (RelativeLayout) findViewById(R.id.write_ll_emoji_select);
		ViewPager mEmojiPager = (ViewPager) findViewById(R.id.emoji_viewpager);
		PageControlView pageControlView = (PageControlView) findViewById(R.id.pageControlView);
		new EmojiPagerSet(this, mSentText, pageControlView, mEmojiPager);
		mFrameLayout = (FrameLayout) findViewById(R.id.write_frame);
		mGridview = (UnScrollGridview) findViewById(R.id.write_gv_photo);
		mTxtEmoj = (TextView) findViewById(R.id.write_text_photo_tv_emoji);
		mCheckBox = (CheckBox) findViewById(R.id.write_dynamic_checkbox);
		mBottomLayout = (LinearLayout) findViewById(R.id.write_bottom_layout);
		mPublicETLayout = (RelativeLayout) findViewById(R.id.write_public_et_layout);
		mPrivateETLayout = (LinearLayout) findViewById(R.id.write_private_et_layout);
		mPublicET = (EditText) findViewById(R.id.write_public_et);
		mNumberTV = (TextView) findViewById(R.id.write_public_number);
		mTopicTV = (TextView) findViewById(R.id.topic_tv);
		mPublicET.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
					Log.d("Aaron", "onTouth===true;");
					return true;
				}
				return false;
			}
		});
		if (isPublicDynamic) {
			mCheckBox.setVisibility(View.VISIBLE);
			mBottomLayout.setVisibility(View.GONE);
			mPublicETLayout.setVisibility(View.VISIBLE);
			mPrivateETLayout.setVisibility(View.GONE);
		} else {
			mCheckBox.setVisibility(View.GONE);
			mBottomLayout.setVisibility(View.VISIBLE);
			mPublicETLayout.setVisibility(View.GONE);
			mPrivateETLayout.setVisibility(View.VISIBLE);
		}
	}

	private void setlistener() {
		mTakePhoto.setOnClickListener(this);
		mChoosePicure.setOnClickListener(this);
		mChooseEmoji.setOnClickListener(this);
		mFrameLayout.setOnTouchListener(this);
		mSentText.setOnClickListener(this);
		mGridview.setOnItemClickListener(this);
		mPublicET.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.write_text_photo_take_photos:
			if (photoList.size() < 9) {
				takePhoto();
			} else {
				BaseUtils.showTost(this, "sorry,the up limit is 9");
			}
			break;

		case R.id.write_text_photo_choose_picture:
			if (photoList.size() <= 9) {
				intent = new Intent(this, MineDirctorySdcPhotosListActivity.class);
				//标记为写贴
				intent.putExtra(BAConstants.IntentType.SPACEWRITEACTIVITY_PHOTOLIST, true);
				intent.putExtra(SIZE, photoList.size());
			} else {
				BaseUtils.showTost(this, "sorry,the up limit is 9");
			}

			break;
		case R.id.write_text_photo_choose_emoji:
			if (mEmojiPanel.getVisibility() == View.GONE) {
				BaseUtils.hideKeyboard(this, mSentText);
				showEmojiPanel();
			} else {
				hideEmojiPanel();
				showSoftInput(mSentText);
			}
			break;
		case R.id.write_et_sent_texts:
			hideEmojiPanel();
			break;
		case R.id.title_lin_right://发布
			if (isPublicDynamic) {
				if (TextUtils.isEmpty(mPublicET.getText().toString().trim())) {
					BaseUtils.showTost(this, "输入不能为空!!!");
					return;
				}
			} else {
				if (TextUtils.isEmpty(mSentText.getText())) {
					BaseUtils.showTost(this, "输入不能为空!!!");
					return;
				}
			}
			//默认的地址
			String city = "深圳";
			long time = 0;
			int la = 0;
			int lo = 0;
			String province = "广东省";

			dialog = DialogFactory.createLoadingDialog(this, getResources().getString(R.string.adapter_loading));
			if (isPublicDynamic) {//新动态
				String str = mPublicET.getText().toString().replaceAll(" ", "").replaceAll("\n|\t|\r", "").toString();
				//获取网络图片
				HttpFactory.httpGet(this, "http://ppapp.tshang.com/appapi/baidu/getPics?keyword=" + str + "&page=1&num=8", false, getResources()
						.getString(R.string.adapter_loading), new HttpRequestCallBack() {

					@Override
					public void onStart() {
						DialogFactory.showDialog(dialog);
					}

					@Override
					public void onSuccess(String result) {
						if (TextUtils.isEmpty(result)) {
							DialogFactory.dimissDialog(dialog);
							BaseUtils.showTost(MineWriteActivity.this, R.string.adapter_failure);
							return;
						}

						final ArrayList<NetworkPicEntity> mEntities = parseResult(result);
						if (mEntities == null || mEntities.size() <= 0 && type == 1) {
							DialogFactory.dimissDialog(dialog);
							BaseUtils.showTost(MineWriteActivity.this, R.string.adapter_failure);
							return;
						}
						//请求下载
						if (type == 1) {
							DownloadImgRequest(mEntities);
						}
					}

					@Override
					public void onError(String error) {
						DialogFactory.dimissDialog(dialog);
						BaseUtils.showTost(MineWriteActivity.this, R.string.adapter_failure);
					}
				});
			} else {
				String content = mSentText.getText().toString().trim();
				//上传
				UserWriteBiz mUserWriteBiz = new UserWriteBiz(this);
				if (null != photoList && photoList.size() > 1) {
					mUserWriteBiz.addTopicIfHasManyPhotos(city, time, la, lo, province, content, photoList, this);
				} else {
					String bitmapPath = null;
					if (null != photoList && photoList.size() > 0 && photoList.size() <= 1) {
						PhotoEntity photo = photoList.get(0);
						bitmapPath = null == photo ? "" : photo.getPath();
					}
					mUserWriteBiz.addTopicIfHasOnePhoto(city, time, la, lo, province, content, bitmapPath, this);
				}
				finish();
			}
			break;
		default:
			break;

		}
		if (null != intent) {
			startActivity(intent);
			overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
		}
	}

	/**
	 * 解析处理
	 * @author Aaron
	 *
	 * @param str
	 * @return
	 */
	private ArrayList<NetworkPicEntity> parseResult(String str) {

		ArrayList<NetworkPicEntity> picList = new ArrayList<NetworkPicEntity>();
		try {
			JSONObject result = new JSONObject(str);
			type = result.getInt("type");
			if (type == 1) {//百度获取
				if (result.has("data")) {
					JSONArray jsonArray = new JSONArray(result.getString("data"));
					JSONObject jo;
					NetworkPicEntity npi;
					for (int i = 0; i < jsonArray.length(); i++) {
						npi = new NetworkPicEntity();
						jo = jsonArray.getJSONObject(i);
						if (jo.has("thumbURL")) {
							npi.setTumbUrl(jo.getString("thumbURL"));
						}
						if (jo.has("middleURL")) {
							npi.setMiddleURL(jo.getString("middleURL"));
						}
						if (jo.has("largeTnImageUrl")) {
							npi.setLargeTnImageUrl(jo.getString("largeTnImageUrl"));
						}
						if (jo.has("hoverURL")) {
							npi.setHoverURL(jo.getString("hoverURL"));
						}
						if (jo.has("objURL")) {
							npi.setObjURL(jo.getString("objURL"));
						}
						picList.add(npi);
					}
				}
			} else if (type == 2) {//抱抱获取 
				requestBaoBaoImg(mPublicET.getText().toString().trim().replaceAll(" ", "").replaceAll("\n", ""));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return picList;
	}

	/**
	 * 请求抱抱网络图片
	 * @author Aaron
	 *
	 * @param msg
	 */
	private void requestBaoBaoImg(String msg) {
		HttpFactory.httpGet(this, "http://media.myhug.cn//se/pic?content=" + msg + "&from=android&netType=WLAN", false, "",
				new HttpRequestCallBack() {

					@Override
					public void onSuccess(String result) {
						ArrayList<NetworkPicEntity> lists = parseBaoBaoResult(result);
						if (lists == null || lists.size() <= 0) {
							DialogFactory.dimissDialog(dialog);
							BaseUtils.showTost(MineWriteActivity.this, R.string.adapter_failure);
							return;
						}
						//请求图片下载
						DownloadImgRequest(lists);
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onError(String error) {
						DialogFactory.dimissDialog(dialog);
						BaseUtils.showTost(MineWriteActivity.this, R.string.adapter_failure);
					}
				});
	}

	/**
	 * 解析抱抱返回结果
	 * @author Aaron
	 *
	 * @param result
	 * @return
	 */
	private ArrayList<NetworkPicEntity> parseBaoBaoResult(String result) {
		ArrayList<NetworkPicEntity> lists = new ArrayList<NetworkPicEntity>();
		NetworkPicEntity entity = null;
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray array = obj.getJSONObject("picList").getJSONArray("picUrl");
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					entity = new NetworkPicEntity();
					entity.setHoverURL(array.getString(i).toString());
					lists.add(entity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lists;
	}

	/**
	 * 图片下载
	 * @author Aaron
	 *
	 * @param mEntities
	 */
	private void DownloadImgRequest(final ArrayList<NetworkPicEntity> mEntities) {
		String imagePath = null;
		BaseFile.delete(new File(IFileUtils.getSDCardRootDirectory() + "/PeiPei/cach/image"));
		//图片URL请求成功,先把图片下载本地
		for (int i = 0; i < mEntities.size(); i++) {
			position = i;
			if (!TextUtils.isEmpty(mEntities.get(i).getHoverURL())) {
				imagePath = mEntities.get(i).getHoverURL();
			} else if (!TextUtils.isEmpty(mEntities.get(i).getLargeTnImageUrl())) {
				imagePath = mEntities.get(i).getLargeTnImageUrl();
			} else if (!TextUtils.isEmpty(mEntities.get(i).getMiddleURL())) {
				imagePath = mEntities.get(i).getMiddleURL();
			} else if (!TextUtils.isEmpty(mEntities.get(i).getObjURL())) {
				imagePath = mEntities.get(i).getObjURL();
			} else if (!TextUtils.isEmpty(mEntities.get(i).getTumbUrl())) {
				imagePath = mEntities.get(i).getTumbUrl();
			}
			DownloadManager manager = DownloadService.getDownloadManager(MineWriteActivity.this.getApplicationContext());
			try {
				manager.addNewDownload(imagePath, "dImg", IFileUtils.getSDCardRootDirectory() + "/PeiPei/cach/image/" + System.currentTimeMillis()
						+ position + ".png", true, false, new RequestCallBack<File>() {

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						String path = responseInfo.result.getAbsolutePath();
						int p = Integer.parseInt(path.substring(path.length() - 5, path.lastIndexOf(".")));
						if (p == mEntities.size() - 1) {
							BaseUtils.cancelDialog();
							String content = mPublicET.getText().toString();
							Bundle bundle = new Bundle();
							bundle.putString(MineImageTextAdaterActivity.CONTENT_MSG, content);
							bundle.putBoolean(MineImageTextAdaterActivity.ANONYMITY_FLAG, mCheckBox.isChecked());
							bundle.putInt(MineImageTextAdaterActivity.TOPICID, topicid);
							bundle.putString(MineImageTextAdaterActivity.TOPIC_TITLE, mTopicTV.getText().toString().trim());
							bundle.putInt(MineImageTextAdaterActivity.SRCPIC, type == 1 ? (1 << 1) : (1 << 0));
							BaseUtils.openActivity(MineWriteActivity.this, MineImageTextAdaterActivity.class, bundle);
							DialogFactory.dimissDialog(dialog);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						BaseUtils.showTost(MineWriteActivity.this, R.string.adapter_failure);
						DialogFactory.dimissDialog(dialog);
					}
				});
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 显示表情面板 
	 * @author vactor
	 *
	 */
	private void showEmojiPanel() {
		if (mEmojiPanel.getVisibility() == View.GONE && mTxtEmoj != null) {
			Drawable left = getResources().getDrawable(R.drawable.message_icon_keyboard_selector);
			if (left != null) {
				left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
				mTxtEmoj.setCompoundDrawables(left, null, null, null);
				mTxtEmoj.setText("键盘");
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Animation animation = AnimationUtils.loadAnimation(MineWriteActivity.this, R.anim.emoji_panel_translate_begin);
						mEmojiPanel.setVisibility(View.VISIBLE);
						mEmojiPanel.startAnimation(animation);
					}
				}, 500);
			}
		}
	}

	/**
	 * 隐藏表情面板 
	 * @author vator
	 *
	 */
	private void hideEmojiPanel() {
		if (mTxtEmoj != null) {
			if (mEmojiPanel.getVisibility() == View.VISIBLE) {
				Drawable left = getResources().getDrawable(R.drawable.write_icon_emotion_selector);
				if (left != null) {
					left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
					mTxtEmoj.setCompoundDrawables(left, null, null, null);
					mTxtEmoj.setText("表情");
					Animation animation = AnimationUtils.loadAnimation(MineWriteActivity.this, R.anim.emoji_panel_translate_back);
					mEmojiPanel.setVisibility(View.GONE);
					mEmojiPanel.startAnimation(animation);
				}
			}
		}
	}

	/**
	 * 拍照
	 * @author vactor
	 *
	 */
	private void takePhoto() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			final Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, getPicUriByCameras());
			startActivityForResult(intent, GET_PHOTO_BY_CAMERA);
		} else {
			BaseUtils.showTost(this, R.string.nosdcard);
		}
	}

	/**
	 *  获取拍照 uri
	 * @author vactor
	 * @return uri
	 */
	private Uri getPicUriByCameras() {
		if (!PHOTO_DIR.exists()) {
			PHOTO_DIR.mkdirs();
		}
		mTempCameraFile = new File(PHOTO_DIR, CAMERA_FILE_NAME + index + ".jpg");
		try {
			mTempCameraFile.createNewFile();
			index++;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Uri.fromFile(mTempCameraFile);
	}

	public boolean onTouch(View v, MotionEvent event) {
		hideEmojiPanel();
		return false;
	}

	@Override
	public void addTopicCallBack(int retCode, int userTopicId, int charmnum, GoGirlDataInfoList list) {
		sendHandlerMessage(mHandler, WRITE_UPLOAD, retCode, charmnum, list);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		new DeleteTopicPhotoDialog(this, android.R.style.Theme_Translucent_NoTitleBar, R.string.dialog_msg1, R.string.ok, R.string.cancel, arg2,
				photoList, photoAdapter).show();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		BaseUtils.hideKeyboard(this, mSentText);
		//		hideEmojiPanel();
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void initData() {}

	@Override
	protected void initRecourse() {}

	@Override
	protected int initView() {
		return R.layout.activity_write_text_photo;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String content = mPublicET.getText().toString();
		mNumberTV.setText(content.length() + "/88");

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
