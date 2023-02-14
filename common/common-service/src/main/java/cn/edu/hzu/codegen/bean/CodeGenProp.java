package cn.edu.hzu.codegen.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author lzf
 * @date 2022-8-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeGenProp {

    /**
     * 作者
     */
    private String author;

    /**
     * 代码放到哪个模块
     */
    private String moduleName="/codegen/"+  System.currentTimeMillis();

    /**
     * 所在的父级package
     */
    private String parentPackageName;

    /**
     * 要生成的表名，单表
     */
    private String tables;

    private String tablePrex="";

    /**
     * 主键类型: AUTO,NONE,INPUT,ASSIGN_ID,ASSIGN_UUID,
     */
    private String idType;

    /**
     * 启用多租户模式
     */
    private boolean tenantMode;

    /**
     * 生成的表对应功能，名词。。生成注释使用,比如：修改xx，查询xx列表，xx服务接口
     */
    private String functionName;

    /**
     * 生成的controller api，为空时默认取表名entityPath
     */
    private String businessName;

    /**
     * 所属微服务名称,用户生成组装api路径
     */
    private String serviceName;

    /**
     * 数据库连接地址
     */
    private String jdbcUrl;

    private String jdbcDriverClass;

    /**
     * 数据库用户名
     */
    private String jdbcUsername;

    /**
     * 数据库连接密码
     */
    private String jdbcPassword;

    /**
     * 数据库schema
     *
     * @param args
     */
    private String jdbcSchema;

    private String[] queryFieldList;

    private String[] addFieldList;

    private String[] editFieldList;

    private String[] resultFieldList;

    private String[] requiredFieldList;

    private Map<String,String> columnLabels;

    private String projectPath = "/tmp/";
}
