package cn.edu.hzu.office.poi;

import cn.edu.hzu.common.api.utils.FileExportLogUtils;
import cn.edu.hzu.common.constant.FileExportLogConstant;
import cn.edu.hzu.common.enums.PoiErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.office.utils.HtmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author lzf
 * @createTime 2022/09/02
 * @description
 */
@Slf4j
public class ConvertOfficeFactory {

    // 默认模板在resources下的路径
    private static final String DEFAULT_PATH_WORD = "/template/word/";

    private ConvertOfficeFactory() {
    }

    public static ConvertOffice build(ExportType exportType, OfficeTemplate template) {
        return build(true, exportType, template);
    }

    public static ConvertOffice build(boolean throwExp, ExportType exportType, OfficeTemplate template) {
        long startTime = System.currentTimeMillis();
        InputStream resourceAsStream = ConvertOfficeFactory.class.getResourceAsStream(DEFAULT_PATH_WORD + template.getTemplateName() + ExportType.WORD.getSuffix());
        ConvertOperation convertOperation = null;
        //状态
        String state = FileExportLogConstant.EXPORT_RESPONSE_OK;
        //类型
        String type = null;
        switch (exportType) {
            case WORD:
                type = FileExportLogConstant.FILE_TYPE_WORD_EXPORT;
                convertOperation = (document, outputStream) -> {
                    try {
                        document.write(outputStream);
                        outputStream.flush();
                    } catch (Exception e) {
                        throwException(throwExp, exportType, e);
                    } finally {
                        closeStream(throwExp, exportType, document, outputStream);
                    }

                };
                break;
            case PDF:
                type = FileExportLogConstant.FILE_TYPE_PDF_EXPORT;
                convertOperation = (document, outputStream) -> {
                    try {
                        String docxHtml = HtmlUtils.docx2Html(document);
                        docxHtml = HtmlUtils.formatHtml(docxHtml);
                        HtmlUtils.htmlToPdf(docxHtml, outputStream);
                        outputStream.flush();
                    } catch (Exception e) {
                        throwException(throwExp, exportType, e);
                    } finally {
                        closeStream(throwExp, exportType, document, outputStream);
                    }
                };
                break;
            default:
                state = FileExportLogConstant.EXPORT_RESPONSE_NO;
                log.error("不支持的文件类型");
                if (throwExp) {
                    throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_TYPE_ERROR).build();
                }

                break;
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        //记录导出日志
        FileExportLogUtils.exportLog(responseTime, state, template.getTemplateName(), 0, type);
        return new ConvertOffice(resourceAsStream, convertOperation, template, exportType);
    }

    private static void throwException(boolean throwExp, ExportType type, Throwable throwable) {
        log.error("导出 {} close document 异常{}", type, throwable);
        if (throwExp) {
            throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_ERROR.setParams(
                    new String[]{type.getType(), throwable.getMessage()}
            )).build();
        }
    }

    private static void closeStream(boolean throwExp, ExportType type, XWPFDocument document, OutputStream outputStream) {

        if (document != null) {
            try {
                document.close();
            } catch (Exception e) {
                log.error("导出 {} close document 异常{}", type.getType(), e);
                if (throwExp) {
                    throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_CLOSE_STREAM_ERROR.setParams(
                            new String[]{type.getType(), "document"}
                    )).build();
                }
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Exception e) {
                log.error("导出 {} close outputStream 异常{}", type.getType(), e);
                if (throwExp) {
                    throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_CLOSE_STREAM_ERROR.setParams(
                            new String[]{type.getType(), "outputStream"}
                    )).build();
                }
            }
        }
    }

}