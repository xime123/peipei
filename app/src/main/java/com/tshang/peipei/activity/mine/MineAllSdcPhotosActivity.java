package com.tshang.peipei.activity.mine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.path.android.jobqueue.JobManager;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.jobs.UploadPhotosJob;
import com.tshang.peipei.model.entity.AlbumEntity;
import com.tshang.peipei.model.entity.PhotoEntity;
import com.tshang.peipei.model.event.NoticeEvent;

import de.greenrobot.event.EventBus;

/**
 * @Title: 上传相片
 *
 * @Description: 读取SDC 所有相片列表
 *
 * @author vactor
 *
 * @version V1.0   
 */
public class MineAllSdcPhotosActivity extends BaseActivity implements OnItemClickListener {

	private final int max = 9;
	private GridView gridView;
	private LinearLayout mLinUpload;
	private LinearLayout mCancelUpload;
	private MineAllSdcPhotosAdapter mAdpater;
	private AlbumEntity mAlbum;//本地相册
	private int mAlbumId;//服务器里的相册ID
	private boolean isWrite;//是否是写贴
	private int size;//从写贴界面传过来的图片数量

	private JobManager jobManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jobManager = BAApplication.getInstance().getJobManager();
		Intent intent = this.getIntent();
		isWrite = intent.getBooleanExtra(BAConstants.IntentType.SPACEWRITEACTIVITY_PHOTOLIST, false);
		size = intent.getIntExtra(MineWriteActivity.SIZE, 0);
		mAlbum = (AlbumEntity) intent.getSerializableExtra(BAConstants.IntentType.DIRECTORYSDCPHOTOLISTACTIVITY_PHOTOLIST);
		mAlbumId = intent.getIntExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, -1);
		initUI();
		setListener();
		mAdpater = new MineAllSdcPhotosAdapter(this);
		gridView.setAdapter(mAdpater);
		mAdpater.setList(mAlbum.getList());
	}

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.chose_photo);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(mAlbum.getAlbumname() + "(" + mAlbum.getList().size() + ")");

		gridView = (GridView) this.findViewById(R.id.listphotos_gridview);
		mLinUpload = (LinearLayout) this.findViewById(R.id.photolist_photo_upload);
		mCancelUpload = (LinearLayout) this.findViewById(R.id.photolist_photo_manage);
	}

	private void setListener() {
		mBackText.setOnClickListener(this);
		mLinUpload.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
		mCancelUpload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.photolist_photo_upload:

			if (null != mAdpater && mAdpater.getCheckedMap() != null && mAdpater.getCheckedMap().size() > 0) {
				List<PhotoEntity> list = new ArrayList<PhotoEntity>();
				Iterator<Integer> it = mAdpater.getCheckedMap().keySet().iterator();
				while (it.hasNext()) {
					int position = it.next();
					list.add(mAdpater.getCheckedMap().get(position));
				}
				//写贴,心情
				if (isWrite) {
					NoticeEvent notice = new NoticeEvent();
					notice.setFlag(NoticeEvent.NOTICE3);
					notice.setObj(list);
					EventBus.getDefault().postSticky(notice);
					Intent intent = new Intent(MineAllSdcPhotosActivity.this, MineWriteActivity.class);
					startActivity(intent);

					overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
					this.finish();

					//上传
				} else {
					UploadPhotosJob job = new UploadPhotosJob(MineAllSdcPhotosActivity.this, mAlbumId, list);
					jobManager.addJobInBackground(job);

					Intent intent = new Intent(this, MineNetPhotosListActivity.class);
					intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, mAlbumId);
					startActivity(intent);

					overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
				}

			}
			break;
		case R.id.photolist_photo_manage:
			if (null != mAdpater && null != mAdpater.getCheckedMap()) {
				mAdpater.getCheckedMap().clear();
				mAdpater.notifyDataSetChanged();
				this.finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		PhotoEntity info = mAdpater.getList().get(arg2);
		info.setTime(System.currentTimeMillis() + "");
		byte[] bitmap = BaseFile.getBytesByFilePath(info.getPath());
		if (null == bitmap) {
			BaseUtils.showTost(this, "该相片数据已损坏,请选择其它相片");
			return;
		}
		if (mAdpater.getCheckedMap().containsKey(arg2)) {
			mAdpater.getCheckedMap().remove(arg2);
		} else {
			//写贴，最多9张
			if (isWrite) {
				if (mAdpater.getCheckedMap().size() < max - size) {
					mAdpater.getCheckedMap().put(arg2, info);
				} else {
					BaseUtils.showTost(this, "上传最大张数不能超过9张");
				}

			} else {
				mAdpater.getCheckedMap().put(arg2, info);
			}

		}
		bitmap = null;
		mAdpater.notifyDataSetChanged();
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initRecourse() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int initView() {
		return R.layout.activity_listphotos;
	}

}
