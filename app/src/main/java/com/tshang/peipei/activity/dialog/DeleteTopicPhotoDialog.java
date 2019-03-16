package com.tshang.peipei.activity.dialog;

import java.util.List;

import android.app.Activity;

import com.tshang.peipei.activity.space.SpaceWritePhotoAdapter;
import com.tshang.peipei.model.entity.PhotoEntity;

/**
 * @Title: DeleteTopicPhotoDialog.java 
 *
 * @Description: 发帖时删除选中照片 
 *
 * @author allen  
 *
 * @date 2014-7-17 上午11:10:04 
 *
 * @version V1.0   
 */
public class DeleteTopicPhotoDialog extends BaseNormalDialog {

	private int position;
	private List<PhotoEntity> photoList;
	private SpaceWritePhotoAdapter photoAdapter;

	public DeleteTopicPhotoDialog(Activity context, int theme, int title, int sure, int cancel, int position, List<PhotoEntity> photoList,
			SpaceWritePhotoAdapter photoAdapter) {
		super(context, theme, title, sure, cancel);
		this.position = position;
		this.photoList = photoList;
		this.photoAdapter = photoAdapter;
	}

	@Override
	public void OnclickSure() {
		photoList.remove(position);
		photoAdapter.notifyDataSetChanged();
	}

}
