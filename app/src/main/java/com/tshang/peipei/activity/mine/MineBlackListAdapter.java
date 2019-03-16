package com.tshang.peipei.activity.mine;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.ArrayListAdapter;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.biz.space.BlackListBiz;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.imageloader.ImageOptionsUtils;
import com.tshang.peipei.vender.imageloader.core.DisplayImageOptions;

/**
 * @Title: MineBlackListAdapter.java 
 *
 * @Description: 黑名单列表适配器 
 *
 * @author allen  
 *
 * @date 2014-10-15 上午11:23:12 
 *
 * @version V1.0   
 */
public class MineBlackListAdapter extends ArrayListAdapter<RetRelevantPeopleInfo> {

	private DisplayImageOptions options;

	private BAHandler mHandler;

	public MineBlackListAdapter(Activity context, BAHandler mHandler) {
		super(context);
		options = ImageOptionsUtils.GetHeadKeySmallRounded(context);
		this.mHandler = mHandler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHoler mViewholer;
		if (convertView == null) {
			mViewholer = new ViewHoler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_blacklist, parent, false);
			mViewholer.iv_head = (ImageView) convertView.findViewById(R.id.item_blacklist_iv_head);
			mViewholer.tv_nick = (TextView) convertView.findViewById(R.id.item_blacklist_tv_nick);
			mViewholer.tv_id = (TextView) convertView.findViewById(R.id.item_blacklist_tv_id);
			mViewholer.btn_delete = (Button) convertView.findViewById(R.id.item_blacklist_btn_del);
			convertView.setTag(mViewholer);
		} else {
			mViewholer = (ViewHoler) convertView.getTag();
		}
		final RetRelevantPeopleInfo info = mList.get(position);
		if (info != null) {
			GoGirlUserInfo userinfo = info.userinfo;
			imageLoader.displayImage("http://" + new String(userinfo.headpickey) + BAConstants.LOAD_HEAD_KEY_APPENDSTR, mViewholer.iv_head, options);

			String alias = SharedPreferencesTools.getInstance(mContext, BAApplication.mLocalUserInfo.uid.intValue() + "_remark").getAlias(
					userinfo.uid.intValue());
			mViewholer.tv_nick.setText(TextUtils.isEmpty(alias) ? new String(userinfo.nick) : alias);
			mViewholer.tv_id.setText("ID: " + userinfo.uid.intValue());

			mViewholer.btn_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BlackListBiz biz = new BlackListBiz();
					biz.removeBlackList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
							BAApplication.mLocalUserInfo.uid.intValue(), info.userinfo.uid.intValue(), mHandler);

				}
			});
		}
		return convertView;
	}

	final class ViewHoler {
		private ImageView iv_head;
		private TextView tv_nick, tv_id;
		private Button btn_delete;
	}

}
