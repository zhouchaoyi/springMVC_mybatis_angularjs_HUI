package com.dawn.bgSys.common.consts;

/**
 * Created with IntelliJ IDEA.
 * User: malone
 * Date: 13-11-4
 * Time: 下午4:03
 */
public class Consts {

    /**
     * 推送种类：每天九点推送
     */
    public static final String PUSH_AT_9 = "PUSH_AT_9";

    /**
     * 推送种类：每天九点推送
     */
    public static final String PUSH_PER_HOUR = "PUSH_PER_HOUR";

    /**
     *  通用apk版本上传配置文件中，版本key开头标识
     */
    public static final String COMMON_APK_VERSION_START = "APK.VERSION";

    /**
     * 通用apk版本上传配置文件中，路径key开头标识
     */
    public static final String COMMON_APK_FILEDIR_START = "APK.FILEDIR";

    /**
     * 通用apk版本上传配置文件中，更新开头key开头标识
     */
    public static final String COMMON_APK_UPDATENOTE_START = "APK.UPDATENOTE";

    /**
     * 通用apk版本上传配置文件中，apk存放根路径
     */
    public static final String COMMON_APK_STORE_BASELOCATION = "APK.BASELOCATION";

    /**
     * 通用apk版本上传配置文件中，配置文件存放根路径
     */
    public static final String COMMON_APK_PROPERTIES_BASELOCATION = "COMMON_PROPERTIES_BASELOCATION";

    /**
     *  rsp session超时机制验证开关
     */
    public static final String RSP_SESSIONTIMEOUT_FLAG = "RSP_SESSIONTIMEOUT_FLAG";

    /**
     *  rsp session超时时间
     */
    public static final String RSP_SESSIONTIMEOUT = "RSP_SESSIONTIMEOUT";

    /**
     *  sr session超时机制验证开关
     */
    public static final String SR_SESSIONTIMEOUT_FLAG = "SR_SESSIONTIMEOUT_FLAG";

    /**
     *  权限错误码
     */
    public static final String PERM_ERROR_CODE = "000003";

    /**
     *  token异常错误码
     */
    public static final String TOKEN_ERROR_CODE = "000002";

    /**
     *  普通异常错误码
     */
    public static final String COMMON_ERROR_CODE = "000001";

    /**
     *  普通正常返回请求错误码
     */
    public static final String COMMON_SUCCESS_CODE = "000000";

    /**
     *  无法解析的错误统一用语
     */
    public static final String COMMON_ERROR_MSG = "请检查网络连接，或服务异常，请联系管理员！";

    /**
     * diy角色
     */
    public static final int ROLE_DIY = 77;

    /**
     * oem角色
     */
    public  static final int ROLE_OEM = 78;

    /**
     * 消息角色Mr
     */
    public static final int MESSAGE_TYPE_MR = 3;

    /**
     *  AWS S3 ACCESS KEY
     */
    public static final String S3_ACCESS_KEY = "ACCESS_KEY";

    /**
     * AWS S3 SCERET KEY
     */
    public static final String S3_SCERET_KEY = "SECRET_KEY";

    /**
     * s3上传验证的用户名
     */
    public static final String S3_UPLOAD_USERNAME = "S3_UPLOAD_USERNAME";

    /**
     * s3上传验证的密码
     */
    public static final String S3_UPLOAD_PASSWORD = "S3_UPLOAD_PASSWORD";

    /**
     * s3存放apk的bucket名称
     */
    public static final String S3_BUCKET_NAME = "retail-apks-update";

    /**
     * s3 上传版本类型 sr
     */
    public static final int S3_APK_UPLOAD_TYPE_SR = 1;

    /**
     * s3 上传版本类型 rsp
     */
    public static final int S3_APK_UPLOAD_TYPE_RSP = 2;

    /**
     * s3 上传版本类型 mr
     */
    public static final int S3_APK_UPLOAD_TYPE_MR = 3;

    /**
     * s3 上传版本类型 agency
     */
    public static final int S3_APK_UPLOAD_TYPE_AGENCY = 4;

    /**
     * 短信密码服务rsp类型
     */
    public static final int PWD_FIND_TYPE_RSP = 2;

    /**
     * 版本号长度
     */
    public static final int APK_VERSION_LENGTH = 4;

    /**
     * s3上传文件根路径
     */
    public static final String S3_BASEPATH = "http://s3-ap-northeast-1.amazonaws.com/";

    public static final String MR_TYPE ="mr" ;

    /**
     * mr店长角色
     */
    public static final int MR_OWNER_ROLE = 1;

    /**
     * mr店员角色
     */
    public static final int MR_CLERK_ROLE = 2;

    /**
     * s3指定bucket下指定文件夹名称
     */
    public static final String S3_BUCKTE_FILENAME = "Update";

    /**
     * s3中国上传文件根路径
     */
    public static final String S3_BASEPATH_CHINA = "http://s3.cn-north-1.amazonaws.com.cn/";


    /**
     * MR  type
     */
    public static final String MR_TYPE_SERVICE_MR="serviceMr";
    public static final String MR_TYPE_SERVICE_AGENCY="serviceAgency";

    public static final String SVR_PRVDR_TYPE_ID_SERVICE_MR="1";
    public static final String SVR_PRVDR_TYPE_ID_SERVICE_AGENCY="2";
    public static final String SVR_PRVDR_TYPE_ID_TEMP_T5 = "3";




    /**
     * MR  type mr
     */
    public static final String MR_TYPE_MR="mr";

    /**
     * MR  type xianzhen
     */
    public static final String MR_TYPE_XIANZHEN="xianzhen";
}
