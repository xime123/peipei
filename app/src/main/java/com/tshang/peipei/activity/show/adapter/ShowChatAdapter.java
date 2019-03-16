package com.tshang.peipei.activity.show.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.model.entity.ShowChatEntity;

/**
 * @Title: ShowChatAdapter.java 
 *
 * @Description: 游客发言adapter 
 *
 * @author allen  
 *
 * @date 2015-1-21 上午11:02:10 
 *
 * @version V1.0   
 */
public class ShowChatAdapter extends ArrayListAdapter<ShowChatEntity> {

	private ViewBaseShowChatAdapter viewTextShowChatAdapter;
	private ViewBaseShowChatAdapter viewPetShowChatAdapter;
	private ViewBaseShowChatAdapter viewGiftShowChatAdapter;

	public ShowChatAdapter(Activity context) {
		super(context);
		viewTextShowChatAdapter = new ViewTextShowChatAdapter(context);
		viewGiftShowChatAdapter = new ViewGiftShowChatAdapter(context);
		viewPetShowChatAdapter = new ViewPetShowChatAdapter(context);
	}

	@Override
	public int getItemViewType(int position) {
		if (mList == null) {
			return 0;
		}

		ShowChatEntity chatEntity = (ShowChatEntity) mList.get(position);
		int type = 0;
		if (chatEntity != null) {
			type = chatEntity.type;
		}
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

		int type = 0;

		ShowChatEntity chatEntity = (ShowChatEntity) mList.get(position);

		if (chatEntity != null) {
			type = chatEntity.type;
		}

		switch (type) {
		case 0:
			convertView = viewTextShowChatAdapter.getView(position, convertView, parent, chatEntity, "");
			break;
		case 47:
			convertView = viewPetShowChatAdapter.getView(position, convertView, parent, chatEntity, "");
			break;
		case 48:
			convertView = viewGiftShowChatAdapter.getView(position, convertView, parent, chatEntity, "");
			break;

		}

		return convertView;
	}

}
