package cn.edu.hzu.office.poi;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzf
 * @createTime 2022/09/02
 * @description office导出类型
 */
@Getter
@AllArgsConstructor
public enum ExportType {

    WORD("word",".docx"),
    PDF("pdf",".pdf");

    private String type;
    private String suffix;

}