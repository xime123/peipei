package com.tshang.peipei.model.biz.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.model.entity.AlbumEntity;
import com.tshang.peipei.model.entity.PhotoEntity;

import de.greenrobot.event.EventBus;

/*
 *类        名 : ListAPathPhotosJob.java
 *功能描述 : 读取手机SDC上所有照片的路径
 *作　    者 : vactor
 *设计日期 :2014 2014-3-24 下午3:41:35
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class ListAPathPhotosJob extends Job {
	private static final long serialVersionUID = 1L;
	private static final int PRIORITY = 1;
	private Context context;

	public ListAPathPhotosJob(Context context) {
		super(new Params(PRIORITY).setRequiresNetwork(false));
		this.context = context;
	}

	@Override
	public void onAdded() {
		EventBus.getDefault().post("loading");
	}

	@Override
	protected void onCancel() {

	}

	@Override
	public void onRun() throws Throwable {
		List<AlbumEntity> photoList = getImgPathList();
		EventBus.getDefault().post(photoList);

	}

	/** 
	 *  
	 * 获取图片地址列表 
	 *  
	 * @return list 
	 */
	private List<AlbumEntity> getImgPathList() {

		File path = Environment.getExternalStorageDirectory();
		List<File> listFile = BaseTools.listFileDirctory(path);

		List<AlbumEntity> photoList = new ArrayList<AlbumEntity>();

		//获取文件夹下的图片
		for (int i = 0; i < listFile.size(); i++) {
			String name = listFile.get(i).getName();
			AlbumEntity album = getPhotoes(name, true);
			if (album.getList().size() > 0) {
				photoList.add(album);
			}
		}

		//获取根目录下的图片
		List<String> list = BaseTools.getImageFromFile(path.listFiles());
		AlbumEntity album = new AlbumEntity();
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i);
			AlbumEntity alb = getPhotoes(name, false);
			if (alb.getList().size() > 0) {
				album.setAlbumname(alb.getAlbumname());
				album.getList().addAll(alb.getList());
			}
		}
		if (album.getList().size() > 0) {
			photoList.add(album);
		}

		return photoList;
	}

	private AlbumEntity getPhotoes(String name, boolean flag) {
		AlbumEntity album = new AlbumEntity();
		// 条件
		String selection = MediaStore.Images.Media.DATA + " like ?";
		String[] selectionArgs = { "%" + name + "%" };
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA }, selection,
				selectionArgs, null);
		while (cursor.moveToNext()) {
			PhotoEntity p = new PhotoEntity();
			p.setId(cursor.getLong(0) + "");
			p.setPath(cursor.getString(2));
			p.setTitle("");
			p.setDesc("");
			album.getList().add(p);
		}
		cursor.close();
		name = flag == true ? name : "其它";
		album.setAlbumname(name);
		return album;
	}

	@Override
	protected boolean shouldReRunOnThrowable(Throwable arg0) {
		return false;
	}

}
