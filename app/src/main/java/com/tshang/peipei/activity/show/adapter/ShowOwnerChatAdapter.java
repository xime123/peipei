package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.entity.ShowChatEntity;

/**
 * @Title: ShowOwnerChatAdapter.java 
 *
 * @Description: 秀场主屏adpater
 *
 * @author allen  
 *
 * @date 2015-1-27 上午11:26:54 
 *
 * @version V1.0   
 */
public class ShowOwnerChatAdapter extends ArrayListAdapter<ShowChatEntity> {

	protected String fileName;

	private ViewVoiceShowChatAdapter viewVoiceShowChatAdapter;

	public ShowOwnerChatAdapter(Activity context, BAHandler mHandler) {
		super(context);
		viewVoiceShowChatAdapter = new ViewVoiceShowChatAdapter(context, mHandler);
	}

	@Override
	public int getItemViewType(int position) {
		if (mList == null) {
			return 0;
		}
		ShowChatEntity msg = mList.get(position);
		if (msg == null) {
			return 0;
		}
		int type = msg.type;
		return type;
	}

	/**
	 * 返回所有的layout的数量
	 * 
	 * */
	@Override
	public int getViewTypeCount() {
		return 49;//这个值一定要大于type的值
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShowChatEntity msg = mList.get(position);
		int type = msg.type;
		switch (type) {
		case 2:
		case 45:
			convertView = viewVoiceShowChatAdapter.getView(position, convertView, parent, msg, fileName);
			break;
		default:
			break;
		}

		return convertView;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		notifyDataSetChanged();
	}

	public String getFileName() {
		return fileName;
	}

}
