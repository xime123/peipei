package com.tshang.peipei.model.biz.user;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfoList;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackCreateAlbum;
import com.tshang.peipei.model.bizcallback.BizCallBackDelAlbum;
import com.tshang.peipei.model.bizcallback.BizCallBackDeleteAlbumPic;
import com.tshang.peipei.model.bizcallback.BizCallBackGetAlbumList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetAlbumPhotoList;
import com.tshang.peipei.model.bizcallback.BizCallBackUploadPhotos;
import com.tshang.peipei.model.request.RequestCreateAlbums;
import com.tshang.peipei.model.request.RequestCreateAlbums.IAlbumCreate;
import com.tshang.peipei.model.request.RequestDeleteAlbum;
import com.tshang.peipei.model.request.RequestDeleteAlbum.IDeleteAlbum;
import com.tshang.peipei.model.request.RequestDeleteAlbumPic;
import com.tshang.peipei.model.request.RequestDeleteAlbumPic.IDeleteAlbumPic;
import com.tshang.peipei.model.request.RequestGetAlbumLists;
import com.tshang.peipei.model.request.RequestGetAlbumLists.IGetAlbumList;
import com.tshang.peipei.model.request.RequestGetAlbumPhotoList;
import com.tshang.peipei.model.request.RequestGetAlbumPhotoList.IGetAlbumPhotoList;
import com.tshang.peipei.model.request.RequestUploadPhoto;
import com.tshang.peipei.model.request.RequestUploadPhoto.IUploadPhotos;

/*
 *类        名 : AlbumBiz.java
 *功能描述 : 相册逻辑相关具体实现,如创建相册,删除相册等
 *作　    者 : vactor
 *设计日期 : 2014-3-25 上午11:21:42
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class UserAlbumBiz implements IAlbumCreate, IGetAlbumList, IUploadPhotos, IGetAlbumPhotoList, IDeleteAlbum, IDeleteAlbumPic {

	private BizCallBackCreateAlbum mCreateAlbumCallBack;
	private BizCallBackGetAlbumList mGetAlbumListCallBack;
	private BizCallBackUploadPhotos mUploadPhotosCallBack;
	private BizCallBackGetAlbumPhotoList mGetAlbumPhotoListCallBack;
	private BizCallBackDelAlbum mDeleteAlbumCallBack;
	private BizCallBackDeleteAlbumPic mDeleteAlbumPicCallBack;

	//创建相册
	public void createAlbum(Activity activity, String albumname, int accessloyalty, String desc, BizCallBackCreateAlbum callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestCreateAlbums createAlbum = new RequestCreateAlbums();
		this.mCreateAlbumCallBack = callBack;
		createAlbum.createAlbum(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), albumname, accessloyalty, desc, this);
	}

	@Override
	public void albumCreateCallBack(int retCode, AlbumInfo album) {
		if (null != mCreateAlbumCallBack) {
			mCreateAlbumCallBack.createAlbumCallBack(retCode, album);
		}
	}

	//相册列表
	public void getAlbumList(Activity activity, int start, int num, int min, int max, BizCallBackGetAlbumList callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestGetAlbumLists albumListImpl = new RequestGetAlbumLists();
		this.mGetAlbumListCallBack = callBack;
		albumListImpl.getAlbumList(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), start, num, min, max, this);

	}

	@Override
	public void getAlbumListCallBack(int retCode, AlbumInfoList list) {
		if (null != mGetAlbumListCallBack) {
			mGetAlbumListCallBack.getAlbumListCallBack(retCode, list);
		}
	}

	//上传相片
	public void uploadPhotos(byte[] auth, int ver, int uid, int albumid, byte[] pic, String picTitle, String picDesc, int isSend,
			BizCallBackUploadPhotos callBack) {
		RequestUploadPhoto requestUploadPhoto = new RequestUploadPhoto();
		this.mUploadPhotosCallBack = callBack;
		requestUploadPhoto.uploadPhotos(auth, ver, uid, albumid, pic, picTitle, picDesc, isSend, this);
	}

	@Override
	public void uploadPhotosCallBack(int retCode, int charmnum) {
		if (null != mUploadPhotosCallBack) {
			mUploadPhotosCallBack.uploadPhotosCallBack(retCode, charmnum);
		}
	}

	//获取相片列表
	public void getAlbumPhotoList(Activity activity, int uid, int albumId, int start, int num, BizCallBackGetAlbumPhotoList callBack) {
		byte[] auth = "".getBytes();
		int selfUid = uid;
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity != null) {
			auth = userEntity.auth;
			selfUid = userEntity.uid.intValue();
		}
		RequestGetAlbumPhotoList requestGetAlbumlists = new RequestGetAlbumPhotoList();
		this.mGetAlbumPhotoListCallBack = callBack;
		requestGetAlbumlists.getAlbumPhotoList(auth, BAApplication.app_version_code, uid, selfUid, albumId, start, num, this);
	}

	@Override
	public void getAlbumPhotoListCallBack(int retCode, int total, PhotoInfoList list) {
		if (null != mGetAlbumPhotoListCallBack) {
			mGetAlbumPhotoListCallBack.getAlbumPhotoList(retCode, total, list);
		}
	}

	//删除相册
	public void deleteAlbum(byte[] auth, int ver, int uid, int albumId, BizCallBackDelAlbum callBack) {
		RequestDeleteAlbum request = new RequestDeleteAlbum();
		this.mDeleteAlbumCallBack = callBack;
		request.deleteAlbum(auth, ver, uid, albumId, this);
	}

	@Override
	public void deleteAlbumCallBack(int resultCode) {
		if (null != mDeleteAlbumCallBack) {
			mDeleteAlbumCallBack.deleteAlbumCallBack(resultCode);
		}
	}

	//删除相册相片
	public void deleteAlbumPic(byte[] auth, int ver, int uid, int albumId, int photoId, BizCallBackDeleteAlbumPic callBack) {
		RequestDeleteAlbumPic req = new RequestDeleteAlbumPic();
		this.mDeleteAlbumPicCallBack = callBack;
		req.deleteAlbumPic(auth, ver, uid, albumId, photoId, this);
	}

	@Override
	public void deletePhotoCallBack(int resultCode, int picId) {
		if (null != mDeleteAlbumPicCallBack) {
			mDeleteAlbumPicCallBack.deletePhotoCallBack(resultCode, picId);
		}
	}

}
