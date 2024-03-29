package com.tshang.peipei.activity.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.redpacket2.adapter.DialogTipsAdapter;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfo;

/**
 * @Title: SolitaireRedPacketInfoDialog.java 
 *
 * @Description: 红包接龙介绍弹框
 *
 * @author DYH  
 *
 * @date 2015-12-9 下午2:36:07 
 *
 * @version V1.0   
 */
public class SolitaireRedPacketQuestionDialog extends Dialog implements DialogInterface.OnDismissListener, android.view.View.OnClickListener {
	private Activity context;
	private ImageView iv_close;
	private RedPacketBetCreateInfo checkRedpacketInfo;
	private DialogTipsAdapter adapter;
	private ListView listview;
	private List<String> strList = new ArrayList<String>();
	private boolean isHall;

	public SolitaireRedPacketQuestionDialog(Activity context) {
		super(context);
		this.context = context;
	}
	
	public SolitaireRedPacketQuestionDialog(Activity context, int theme, boolean isHall, RedPacketBetCreateInfo checkRedpacketInfo) {
		super(context, theme);
		this.isHall = isHall;
		this.context = context;
		this.checkRedpacketInfo = checkRedpacketInfo;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_solitaire_redpacket_requestion);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		listview = (ListView) findViewById(R.id.tips_list);
		adapter = new DialogTipsAdapter(context);
		listview.setAdapter(adapter);
		if(checkRedpacketInfo != null){
			if(isHall){
				if(BAApplication.getInstance().hallRedList != null){
					if(checkRedpacketInfo.orggoldcoin.intValue() > 0){
						setGoldTips();
					}else{
						setSilverTips();
					}
				}
			}else{
				if(BAApplication.getInstance().redList != null){
					if(checkRedpacketInfo.orggoldcoin.intValue() > 0){
						setGoldTips();
					}else{
						setSilverTips();
					}
				}
			}
		}
		
		
		iv_close.setOnClickListener(this);
	}
	
	private void setGoldTips(){
		if(isHall){
			for(RedPacketBetInfo item : BAApplication.getInstance().hallRedList){
				if(item.gold.intValue() == checkRedpacketInfo.orggoldcoin.intValue()){
					String str = new String(item.desc);
					divisionStr(str);
					setDes();
					ILog.d("DYH", "the des is : " + new String(item.desc));
					break;
				}
			}
		}else{
			for(RedPacketBetInfo item : BAApplication.getInstance().redList){
				if(item.gold.intValue() == checkRedpacketInfo.orggoldcoin.intValue()){
					String str = new String(item.desc);
					divisionStr(str);
					setDes();
					break;
				}
			}
		}
	}
	
	private void divisionStr(String str){
		if(!TextUtils.isEmpty(str) && str.contains("·")){
			String[] strArr = str.split("·");
			if(strArr != null && strArr.length > 0){
				List<String> list = Arrays.asList(strArr);
				strList.addAll(list);
			}
		}
	}
	
	private void setDes(){
		adapter.appendToList(strList);
		adapter.notifyDataSetChanged();
	}
	
	private void setSilverTips(){
		if(isHall){
			for(RedPacketBetInfo item : BAApplication.getInstance().hallRedList){
				if(item.gold.intValue() == checkRedpacketInfo.orgsilvercoin.intValue()){
					String str = new String(item.desc);
					divisionStr(str);
					setDes();
					ILog.d("DYH", "the des is : " + new String(item.desc));
					break;
				}
			}
		}else{
			for(RedPacketBetInfo item : BAApplication.getInstance().redList){
				if(item.gold.intValue() == checkRedpacketInfo.orgsilvercoin.intValue()){
					String str = new String(item.desc);
					divisionStr(str);
					setDes();
					ILog.d("DYH", "the des is : " + new String(item.desc));
					break;
				}
			}
		}
	}

	public void showDialog() {
		try {
			windowDeploy();
			// 设置触摸对话框意外的地方取消对话框
			setCanceledOnTouchOutside(true);
			show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error : "  + e.toString());
		}
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window window = getWindow(); // 得到对话框
		final WindowManager.LayoutParams wlps = window.getAttributes();
		wlps.width = context.getResources().getDisplayMetrics().widthPixels * 9 / 10;
		wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wlps.dimAmount = 0.6f;
		wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		wlps.gravity = Gravity.CENTER;
		window.setAttributes(wlps);
	}

	@Override
	public void onDismiss(DialogInterface arg0) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_close:
			this.dismiss();
			break;
		}
	}
	
}
