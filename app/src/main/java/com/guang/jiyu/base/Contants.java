package com.guang.jiyu.base;

import okhttp3.MediaType;

/**
 * Created by admin on 2018/6/25.
 */

public class Contants {

    //调用接口时传的数据类型
    public static final MediaType JSON =MediaType.parse("application/json; charset=utf-8");


    public static final String QINIU_UPLOAD_URL = "http://code296344362.u.qiniudn.com/";

    //常量
    public static final String TAG_LOADMORE_STATE = "LOADMORE_STATE";

    public static final String TAG_COMEPLETE_RANK = "COMEPLETE_RANK";//排行榜
    public static final String TAG_HASHRATE_RECORD = "HASHRATE_RECORD";//算力记录
    public static final String TAG_INVITE_REMARK = "INVITE_REMARK";//邀请记录
    public static final String TAG_GETTING_CANDY_RECORD = "GETTING_CANDY_RECORD";//糖果获取记录
    public static final String EVENT_INFORMATION = "HASHRATE_RECORD";
    public static final String EVENT_CHANGE_BLIGHTNESS = "EVENT_CHANGE_BLIGHTNESS";//调整屏幕亮度
    public static final String EVENT_RECOVER_BLIGHTNESS = "EVENT_RECOVER_BLIGHTNESS";//恢复屏幕亮度
    public static final String EVENT_REFRESH_FRAGMENT= "EVENT_REFRESH_FRAGMENT";//重新加载fragment
    public static final String EVENT_REFRESH_FRAGMENT_DATA= "EVENT_REFRESH_FRAGMENT_DATA";//重新加载fragment数据
    public static final String JYCJ_currencyValue = "JYCJ";//集鱼币和人民币的比值
    public static final int GET_DATA_FAILURE = 0x01;
    public static final int GET_DATA_SUCCESS = 0x02;
    public static final int GET_DATA_NODATA = 0x03;
    public static final int GET_DATA_NOMOREDATA = 0x04;
    public static final int GET_DATA_UNNORMAL = 0x05;


    //存储用户信息
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PWD = "USER_PWD";
    public static final String MOBILE_NO = "MOBILE_NO";
    public static final String AUTHORIZATION = "authorization";
    public static final String USER_ID = "user_id";
    public static final String QINIU_TOKEN = "QINIU_TOKEN";
    public static final String USER_UUID = "USER_UUID";

    //资讯
    public static final int FASTINFO_REFRESH_FAILURE = 0x09;
    public static final int INFO_REFRESH_SUCCESS = 0x10;
    public static final int FASTINFO_REFRESH_SUCCESS = 0x11;
    public static final int INFO_REFRESH_NOMORE = 0x12;
    public static final int FASTINFO_REFRESH_NOMORE = 0x13;
    public static final int INFO_GET_FAILURE = 0x14;
    public static final int INFODetails_GET_SUCCESS = 0x15;
    public static final int INFODetails_GET_FAILURE = 0x16;
    public static final int INFOSearch_GET_SUCCESS = 0x17;
    public static final int INFOSearch_GET_FAILURE = 0x18;
    public static final int INFOSearch_GET_NOTHING = 0x19;
    public static final int INFO_BULISH = 0x100;
    public static final int INFO_BEARISH = 0x101;
    public static final int INFO_SHARE = 0x102;
    public static final int INFO_READING_SUCCESS = 0x103;

    //用户信息
    public static final int USER_LOGIN_SUCCESS = 0x20;
    public static final int USER_LOGIN_FAILURE = 0x21;
    public static final int Reset_PWD_SUCCESS = 0x22;
    public static final int Reset_PWD_FAILURE = 0x23;
    public static final int LOGIN_failure = 0x24;

    //收藏
    public static final int INFOConnect_SUCCESS = 0x30;
    public static final int INFOConnect_ALREADY_EXICT = 0x31;
    public static final int INFOCollect_NOData = 0x32;
    public static final int INFOCollect_HaveData = 0x33;
    public static final int INFOCollect_Failure = 0x34;
    public static final String CONNECT_totalRow = "CONNECT_totalRow";

    //搜索关键字
    public static final String INFOSearch_KEYWORD = "INFOSearch_KEYWORD";

    //发送验证码
    public static final int SendCode_Failure = 0x40;
    public static final int SendCode_Success = 0x41;
    public static final int SendCode_ChangeMobile = 0x42;
    public static final int SendCode_ForgetPWD = 0x43;
    public static final int SendCode_Register = 0x44;

    //项目
    public static final int PROJECT_GET_SUCCESS = 0x50;
    public static final int PROJECT_ADD_SUCCESS = 0x51;
    public static final int PROJECT_GET_NOMORE = 0x52;
    public static final int CURRENCY_GET_SUCCESS = 0x53;

    //钱包
    public static final int WALLET_GET_SUCCESS = 0x60;
    public static final int WALLET_GET_FAILURE = 0x61;
    public static final int WALLETDetail_GET_Nodata = 0x62;

    //消息
    public static final int MSGRecord_GET_SUCCESS = 0x70;
    public static final int MSGRecord_GET_FAILURE = 0x71;
    public static final int MSGRecord_GET_Nodata = 0x72;

    //邀请好友
    public static final int INVITE_FRIENDrecord_GET_SUCCESS = 0x80;
    public static final int INVITE_FRIENDrecord_GET_FAILURE = 0x81;
    public static final int INVITE_FRIENDrecord_GET_Nodata = 0x82;
    public static final int INVITE_FRIEND_code = 0x83;

    //算力
    public static final int STUDY_SUCCESS = 0x90;
    public static final int CKECKIN_SUCCESS = 0x91;
    public static final int GET_HASHRATE_SUCCESS = 0x92;
    public static final int GET_HASHRATE_FAILURE = 0x93;
    public static final int CKECKIN_ALREADY = 0x94;
}
