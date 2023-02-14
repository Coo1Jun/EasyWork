package cn.edu.hzu.common.constant;

/**
 * @author lzf
 * @createTime 2022/09/02
 * @description 文件导出日志常量
 */
public class FileExportLogConstant {

    /**
     * 系统标识
     */
    public static final String SYS_CODE = "";

    /*======================文件类型=========================*/
    public static final String FILE_TYPE_EXCEL_EXPORT = "EXCEL";
    public static final String FILE_TYPE_WORD_EXPORT = "WORD";
    public static final String FILE_TYPE_PDF_EXPORT = "PDF";
    public static final String FILE_TYPE_ACCESSORY_EXPORT = "附件";

    /**
     * 导出情况：0 正常
     */
    public static final String EXPORT_RESPONSE_OK = "0";

    /**
     * 导出情况：-1 失败
     */
    public static final String EXPORT_RESPONSE_NO = "-1";

    /**
     * 文件下载地址
     */
    public static final String FILE_DOWNLOAD_URL = "FILE_DOWNLOAD_URL";

    /**
     * 下载成功状态
     */
    public static final String DOWNLOAD_STATE_SUCCESS = "0";

    /**
     * 下载成功状态LABEL
     */
    public static final String DOWNLOAD_STATE_SUCCESS_LABEL = "导出成功";

    /**
     * 下载失败状态
     */
    public static final String DOWNLOAD_STATE_FAIL = "-1";

    /**
     * 下载成功状态LABEL
     */
    public static final String DOWNLOAD_STATE_FAIL_LABEL = "导出失败";

    /**
     * 下载成功状态LABEL
     */
    public static final String DOWNLOAD_STATE_ING_LABEL = "正在导出";

    /**
     * 站内信消息ID
     */
    public static final String EXPORT_MESSAGE_CONTENT = "已导出完成，请到个人下载列表下载";

    /**
     * 租户ID
     */
    public static String TENANT_ID = "TENANT_ID";
}
