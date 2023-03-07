package cn.edu.hzu.common.constant;

/**
 * @author lzf
 * @createTime 2022/09/03
 * @description 全局常量
 */
public class Constant {

    /**
     * 标记需要在模板内另外加上的数据
     */
    public static String OFFICE_CONVERT_DATA_SON = "son";
    // ==================== sso-server start ========================
    /**
     * sso sessionid, between browser and sso-server (web + token client)
     */
    public static final String SSO_SESSIONID = "sso_sessionid";
    /**
     * redirect url (web client)
     */
    public static final String REDIRECT_URL = "redirect_url";
    /**
     * sso user, request attribute (web client)
     */
    public static final String SSO_USER = "sso_user";
    /**
     * sso server address (web + token client)
     */
    public static final String SSO_SERVER = "sso_server";
    /**
     * login url, server relative path (web client)
     */
    public static final String SSO_LOGIN = "/login";
    /**
     * logout url, server relative path (web client)
     */
    public static final String SSO_LOGOUT = "/logout";
    /**
     * logout path, client relatice path
     */
    public static final String SSO_LOGOUT_PATH = "SSO_LOGOUT_PATH";
    /**
     * excluded paths, client relatice path, include path can be set by "filter-mapping"
     */
    public static final String SSO_EXCLUDED_PATHS = "SSO_EXCLUDED_PATHS";
    // ========================== sso-server end ===================

    /**
     * 项目名称
     */
    public static final String PROJECT_NAME = "Easy Work";

    /**
     * 默认用户头像链接
     */
    public static final String DEFAULT_USER_PORTRAIT = "https://easywork23.oss-cn-shenzhen.aliyuncs.com/attachment/el_default_user.png";
}
