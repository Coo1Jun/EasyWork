package cn.edu.hzu.office.poi;

/**
 * @author lzf
 * @createTime 2022/09/02
 * @description
 */
public interface OfficeTemplate {

    /**
     * 文件模板名称
     */
    String getTemplateName();

    /**
     * 属性名
     */
    String[] getFullField();

    /**
     * 导出文件的名称
     */
    String getFileName();
}