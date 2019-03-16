package com.tshang.peipei.activity.mine;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.path.android.jobqueue.JobManager;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.BaseActivity;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.jobs.ListAPathPhotosJob;
import com.tshang.peipei.model.entity.AlbumEntity;

import de.greenrobot.event.EventBus;

/**
 * @Title: 上传相片
 *
 * @Description: 读取SDC 所有相片列表,很多界面都会共用到该类
 *
 * @author allen
 *
 * @version V1.0   
 */
public class MineDirctorySdcPhotosListActivity extends BaseActivity implements OnItemClickListener {

	//	private static final int ERROR = -1;
	//	private static final int CREATE_ALBUM = 1;
	//	private static final int GET_ALBUM_LIST = 2;

	private boolean isWrite;//是否为写贴

	private ListView mListView;
	private TextView mTitle;

	private JobManager mJobManager;
	private MineDirectorySdcPhotosListAdapter mAdapter = null;
	private int mAlbumId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mAlbumId = intent.getIntExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, mAlbumId);
		isWrite = intent.getBooleanExtra(BAConstants.IntentType.SPACEWRITEACTIVITY_PHOTOLIST, false);
		initUI();
		mAdapter = new MineDirectorySdcPhotosListAdapter(this);
		mListView.setAdapter(mAdapter);
		setListener();

		//		File path = Environment.getExternalStorageDirectory();// 获得SD卡路径   
		mJobManager = BAApplication.getInstance().getJobManager();
		//		final File[] files = path.listFiles();// 读取   
		mJobManager.addJobInBackground(new ListAPathPhotosJob(this));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initUI() {
		mBackText = (TextView) findViewById(R.id.title_tv_left);
		mBackText.setText(R.string.chose_album);
		mBackText.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.title_tv_mid);
		mTitle.setText(R.string.chose_album);

		mTitle.setText(R.string.photo_album);
		mListView = (ListView) findViewById(R.id.sdc_photos_listview);
	}

	private void setListener() {
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.photo_album_manage:
			clickManage();
			break;
		case R.id.photo_album_delete:
			clickDelete();
			break;
		case R.id.photo_album_cancel:
			clickCancel();
			break;
		default:
			break;
		}
	}

	//由于多个界面会共用一个相片选择类,所以会有比较多的传值,根据不同的传值判断是上传还是写贴,或者其它操作
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		List<AlbumEntity> lists = mAdapter.getList();
		if (lists != null && !lists.isEmpty()) {
			Intent intent = new Intent(this, MineAllSdcPhotosActivity.class);
			//*********************上传相片******************************//
			//传相册ID
			intent.putExtra(BAConstants.IntentType.ALBUMACTIVITY_ALBUMID, mAlbumId);
			//传相片列表
			intent.putExtra(BAConstants.IntentType.DIRECTORYSDCPHOTOLISTACTIVITY_PHOTOLIST, lists.get(position));
			//*********************上传相片******************************//

			//*********************写贴*********************************//
			intent.putExtra(BAConstants.IntentType.SPACEWRITEACTIVITY_PHOTOLIST, isWrite);
			int size = this.getIntent().getIntExtra(MineWriteActivity.SIZE, 0);
			intent.putExtra(MineWriteActivity.SIZE, size);
			//*********************写贴*********************************//
			startActivity(intent);
			overridePendingTransition(R.anim.popwin_bottom_in, R.anim.popwin_bottom_out);
		}
	}

	public void onEventMainThread(String loading) {
		BaseUtils.showDialog(this, R.string.loading);
	}

	public void onEventMainThread(List<AlbumEntity> list) {
		mAdapter.setList(list);
		BaseUtils.cancelDialog();
	}

	private void clickManage() {
		setBottomTab(true);

	}

	private void clickDelete() {

	}

	private void clickCancel() {
		setBottomTab(false);

	}

	private void setBottomTab(boolean b) {}

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
		return R.layout.activity_sdc_photos_listview;
	}

}
