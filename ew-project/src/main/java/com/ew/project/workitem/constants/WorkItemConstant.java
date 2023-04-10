package com.ew.project.workitem.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lzf
 * @create 2023/04/07
 * @description 工作项模块的常量
 */
public class WorkItemConstant {

    // 工作项类型 start =============================
    /**
     * 计划集
     */
    public static final String PLANS = "Plans";
    public static final String EPIC = "Epic";
    public static final String FEATURE = "Feature";
    public static final String STORY = "Story";
    public static final String TASK = "Task";
    public static final String BUG = "Bug";
    // 工作项类型 end =================================
    /**
     * 规定任务完成的状态
     */
    public static final Set<String> TASK_COMPLETION_FLAG = new HashSet<>(Arrays.asList("已完成", "关闭", "已取消", "未复现"));

    // 修改类型 start ======================================================
    /**
     * 修改标题
     */
    public static final String EDIT_TITLE = "title";
    /**
     * 修改状态
     */
    public static final String EDIT_STATUS = "status";
    /**
     * 修改日期
     */
    public static final String EDIT_DATE = "date";
    // 修改类型 end ======================================================
}
