package com.ew.project.group.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRoleEnum {

    ADMIN("admin", "管理员"),
    USER("user", "普通成员")

    ;
    private String code;
    private String title;

    public static String getTitle(String code) {
        MemberRoleEnum[] values = MemberRoleEnum.values();
        for (MemberRoleEnum role : values) {
            if (role.getCode().equals(code)) {
                return role.getTitle();
            }
        }
        return null;
    }
}
