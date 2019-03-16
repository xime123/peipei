package com.tshang.peipei.model.event;

/**
 * @Title: NoticeEvent.java 
 *
 * @Description: activity 之间使用eventbus 通信
 *
 * @author vactor
 *
 * @date 2014-4-9 下午2:05:02 
 *
 * @version V1.0   
 */
public class NoticeEvent {

	//写心情,从SDC中选相片
	public static final int NOTICE3 = 3;

	//相册上传照片时,中止上传操作,通知JOB
	public static final int NOTICE4 = 4;

	//相册上传照片时,更新进度
	public static final int NOTICE5 = 5;

	//相册上传照片时,已经正在上传了 
	public static final int NOTICE6 = 6;

	//相册上传照片时,通知准备开始上传
	public static final int NOTICE7 = 7;

	//相册上传照片时,中止上传操作成功返回,通知界面
	public static final int NOTICE8 = 8;

	//消息界面显示删除栏
	public static final int NOTICE10 = 10;

	//消息界面点击删除
	public static final int NOTICE11 = 11;

	//femalefragment 中查询失败,或正在发送中的贴子
	public static final int NOTICE16 = 16;

	//写贴时，通知上传图片工作结束
	public static final int NOTICE17 = 17;

	//用户更改资料后,刷新界面资料
	public static final int NOTICE18 = 18;

	// 客人动态详情点赞后，返回动态列表界面，点赞数+1
	public static final int NOTICE21 = 21;

	//客人动态详情回复后,返回动态列表界面,更新回复数
	public static final int NOTICE23 = 23;

	//上传照片成功后,通知相册界面,更改封面
	public static final int NOTICE26 = 26;

	//其他地方登陆
	public static final int NOTICE27 = 27;

	//礼物计数
	public static final int NOTICE28 = 28;

	//未领取奖励
	public static final int NOTICE50 = 50;

	//发布广播成功了
	public static final int NOTICE51 = 51;
	//收到@我的广播计数
	public static final int NOTICE52 = 52;

	public static final int INOTICE_01 = 0x60; //登录成功

	public static final int NOTICE57 = 57;//刷新个人主页的技能
	public static final int NOTICE58 = 58;//切换用户更新我的页面
	public static final int NOTICE59 = 59;//让底部菜单智灰不可点击
	public static final int NOTICE60 = 60;//去掉底部菜单智灰不可点击
	public static final int NOTICE61 = 61;//大厅去掉置灰不可点击
	public static final int NOTICE62 = 62;//头像上传成功
	public static final int NOTICE63 = 63;//注销了用户
	public static final int NOTICE64 = 64;//删除了回复,通知消息数减1
	public static final int NOTICE65 = 65;//群踢人成功了
	public static final int NOTICE66 = 66;//关闭群聊界面
	public static final int NOTICE67 = 67;//删除会话列表刷新未读数据
	public static final int NOTICE68 = 68;//消息通知
	public static final int NOTICE69 = 69;//看过我的
	public static final int NOTICE70 = 70;//离线消息开始接受
	public static final int NOTICE71 = 71;//离线消息结束了

	public static final int NOTICE72 = 72;//秀场评论消息推送
	public static final int NOTICE73 = 73;//秀场语音消息推送
	public static final int NOTICE74 = 74;//秀场角色变化
	public static final int NOTICE75 = 75;//秀场信息变化
	public static final int NOTICE76 = 76;//消息变化通知改返回按钮
	public static final int NOTICE77 = 77;//秀场信息变化全局
	public static final int NOTICE78 = 78;//秀场语音波动关闭
	public static final int NOTICE79 = 79;//秀场语音波动开始

	public static final int NOTICE80 = 80;//系统通知分享
	public static final int NOTICE81 = 81;//连刷动画通知到大厅
	public static final int NOTICE82 = 82;//宝箱
	public static final int NOTICE83 = 83;//刷新秀场
	public static final int NOTICE84 = 84;//仙术回放

	public static final int NOTICE85 = 85;//viewPage item点击

	public static final int NOTICE86 = 86;//关闭Activity

	public static final int NOTICE87 = 87;//动态回复(关于我)
	public static final int NOTICE88 = 88;//取消关于我提示
	public static final int NOTICE89 = 89;//关于二次刷新
	public static final int NOTICE90 = 90;//刷新关于我界面 
	public static final int NOTICE91 = 91;//动态详情页点赞，刷新全部动态，我的动态
	public static final int NOTICE92 = 92;//动态不存在，刷新关于我界面

	public static final int NOTICE93 = 93;//通知frament刷新悬浮窗数据
	public static final int NOTICE94 = 94;//发布悬赏成功
	public static final int NOTICE95 = 95;//发布女神技状态更新
	public static final int NOTICE96 = 96;//保存女神技成功
	public static final int NOTICE97 = 97;//赠送实物成功刷新聊天界面

	public static final int NOTICE98 = 98;//匿名绑定成功

	public static final int NOTICE99 = 99;//更新可以抢的红包数量

	public static final int NOTICE100 = 100;//匿名悬赏开关
	
	public static final int NOTICE101 = 101;//大厅接龙红包发布成功了

	private int flag;
	private int num;
	private int num2;
	private int num3;
	private long num4;

	private int retcode;
	private Object obj;

	public int getNum2() {
		return num2;
	}

	public void setNum2(int num2) {
		this.num2 = num2;
	}

	public int getNum3() {
		return num3;
	}

	public void setNum3(int num3) {
		this.num3 = num3;
	}

	public int getRetcode() {
		return retcode;
	}

	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public long getNum4() {
		return num4;
	}

	public void setNum4(long num4) {
		this.num4 = num4;
	}

}
