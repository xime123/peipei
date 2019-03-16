package com.tshang.peipei.activity.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.chat.ChatEmojiPagerAdapter;
import com.tshang.peipei.activity.chat.bean.CommonFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.EmojiFaceConversionUtil;
import com.tshang.peipei.activity.chat.bean.EmotionBean;
import com.tshang.peipei.activity.chat.bean.HaremFaceConversionUtil;
import com.tshang.peipei.view.PageControlView;

/**
 * @Title: EmojiPagerSet.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 表情面板
 *
 * @author Jeff 
 *
 * @date 2014年10月15日 下午9:11:04 
 *
 * @version V1.4.0   
 */
public class EmotionViewAdd implements OnPageChangeListener, OnItemClickListener {
	private PageControlView pageControlView;
	private ImageView iv_common_face;
	private ImageView iv_emoji_face;
	private ImageView iv_harem_face;
	private Activity activity;
	private HaremEmotionClickListener callback;

	public EmotionViewAdd(Activity activity, EditText edt_input, PageControlView pageControlView, ViewPager viewPage, ImageView iv_common_face,
			ImageView iv_emoji_face, ImageView iv_harem_face, HaremEmotionClickListener callback) {
		this.activity = activity;
		this.pageControlView = pageControlView;
		this.iv_common_face = iv_common_face;
		this.iv_emoji_face = iv_emoji_face;
		this.iv_harem_face = iv_harem_face;
		this.callback = callback;
		List<View> mListViews = new ArrayList<View>();
		ArrayList<ArrayList<EmotionBean>> commonFacelists = CommonFaceConversionUtil.getInstace().getCommonLists(activity);
		for (ArrayList<EmotionBean> arrayList : commonFacelists) {
			View view = LayoutInflater.from(activity).inflate(R.layout.view_common_face_gridview, null);
			GridView gridview = (GridView) view.findViewById(R.id.view_common_face_gridview);
			CommonEmotionAdapter adapter = new CommonEmotionAdapter(activity, edt_input);
			adapter.setList(arrayList);
			gridview.setAdapter(adapter);
			mListViews.add(gridview);
		}
		pageControlView.count = 2;

		ArrayList<ArrayList<EmotionBean>> emojilists = EmojiFaceConversionUtil.getInstace().getCommonLists(activity);
		for (ArrayList<EmotionBean> arrayList : emojilists) {
			View view = LayoutInflater.from(activity).inflate(R.layout.view_common_face_gridview, null);
			GridView gridview = (GridView) view.findViewById(R.id.view_common_face_gridview);
			CommonEmotionAdapter adapter = new CommonEmotionAdapter(activity, edt_input);
			adapter.setList(arrayList);
			gridview.setAdapter(adapter);
			mListViews.add(gridview);
		}

		ArrayList<ArrayList<EmotionBean>> lists = HaremFaceConversionUtil.getInstace().getEmojiLists(activity);
		for (ArrayList<EmotionBean> arrayList : lists) {
			View view = LayoutInflater.from(activity).inflate(R.layout.view_gridview, null);
			GridView gridview = (GridView) view.findViewById(R.id.view_gridview);
			gridview.setOnItemClickListener(this);
			HaremEmotionAdapter adapter = new HaremEmotionAdapter(activity);
			adapter.setList(arrayList);
			gridview.setAdapter(adapter);
			mListViews.add(gridview);
		}

		ChatEmojiPagerAdapter mEmojiAdapter = new ChatEmojiPagerAdapter(mListViews);
		viewPage.setAdapter(mEmojiAdapter);
		viewPage.setCurrentItem(0);
		pageControlView.generatePageControl(0);
		viewPage.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 < 2) {//ps_0_1-2
			pageControlView.count = 2;
			iv_common_face.setBackgroundColor(activity.getResources().getColor(R.color.upload));
			iv_harem_face.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
			iv_emoji_face.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
			pageControlView.generatePageControl(arg0);
		} else if (arg0 < 7) {
			pageControlView.count = 5;
			iv_common_face.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
			iv_emoji_face.setBackgroundColor(activity.getResources().getColor(R.color.upload));
			iv_harem_face.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
			pageControlView.generatePageControl(arg0 - 2);
		} else if (arg0 < 11) {
			pageControlView.count = 4;
			iv_common_face.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
			iv_emoji_face.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
			iv_harem_face.setBackgroundColor(activity.getResources().getColor(R.color.upload));
			pageControlView.generatePageControl(arg0 - 7);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		EmotionBean haremEmotionBean = (EmotionBean) parent.getAdapter().getItem(position);
		if (callback != null) {
			callback.OnHaremEmotionClick(haremEmotionBean);
		}

	}

	public interface HaremEmotionClickListener {
		public void OnHaremEmotionClick(EmotionBean haremEmotionBean);

	}
}
