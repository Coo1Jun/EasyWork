package com.ew.project.netdisk.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NetDiskTypeEnum {

    DIR(1, "文件夹"),
    // 所属类型 start =====================
    PROJECT(0, "项目"),
    PERSONAL(1, "个人")
    // 所属类型 end =====================

    ;
    private final int code;
    private final String title;
}
