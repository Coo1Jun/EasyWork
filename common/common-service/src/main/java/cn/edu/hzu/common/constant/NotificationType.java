package cn.edu.hzu.common.constant;

/**
 * @author lzf
 * @create 2023/04/25
 * @description 通知类型常量
 */
public class NotificationType {

    /**
     * from_id请求添加user_id为好友
     */
    public static final String FRIEND = "friend";
    /**
     * from_id邀请user_id进入group_id项目组
     */
    public static final String GROUP = "group";
    /**
     * from_id将user_id添加为work_item_id的负责人
     */
    public static final String WORK = "work";
    /**
     * 预警，工作项work_item_id即将截止
     */
    public static final String WARN = "warn";
}
