package cn.edu.hzu.office.poi;

import cn.edu.hzu.common.annotation.Image;
import cn.edu.hzu.common.api.utils.BeanOperate;
import cn.edu.hzu.common.api.utils.DateUtils;
import cn.edu.hzu.common.constant.Constant;
import cn.edu.hzu.common.constant.WordExportTypeConstants;
import cn.edu.hzu.common.enums.PoiErrorEnum;
import cn.edu.hzu.common.exception.CommonException;
import cn.edu.hzu.office.dtotolist.DTOToList;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lzf
 * @createTime 2022/09/02
 * @description
 */
@Slf4j
public class ConvertOffice {
    /**
     * 模板的输入流
     */
    private InputStream modelInputStream;

    /**
     * 转换行为：模板对象如何转换成输入流
     */
    private ConvertOperation convertOperation;

    /**
     * 导出文件信息（模板名称、文件名称、外加属性）
     */
    private OfficeTemplate template;

    /**
     * 是否需要时间格式化
     */
    private boolean dateFmt;


    /**
     * 数据实体的map对象，key为属性名称，value为属性值
     */
    private Map tableDataMap;

    /**
     * 外加属性的数据集
     */
    private List<String[]> tableRowDataList = new ArrayList<>();

    /**
     * 导出类型
     */
    private ExportType exportType;

    ConvertOffice(InputStream modelInputStream, ConvertOperation convertOperation, OfficeTemplate template, ExportType exportType) {
        this.modelInputStream = modelInputStream;
        this.convertOperation = convertOperation;
        this.exportType = exportType;
        this.template = template;
    }

    public void convert(HttpServletResponse response, Map tableDataMap) {
        convert(response, false, false, tableDataMap);
    }

    /**
     * @param dateFmt   是否时间格式化
     * @param bean      要用于替换的数据对象
     * @param tableData 需要另外加的数据（模板内暂无替换的）
     * @param <T>
     * @return
     */
    public <T> ConvertOffice convert(boolean dateFmt, T bean, List tableData) {
        // 解决时间转换错误问题
        Map tableDataMap = beanToMap(bean);
        if (CollectionUtil.isEmpty(tableData)) {
            convert(null, dateFmt, false, tableDataMap);
            return this;
        }
        tableDataMap.put(Constant.OFFICE_CONVERT_DATA_SON, tableData);
        convert(null, dateFmt, true, tableDataMap);
        return this;
    }

    public <T> ConvertOffice convertHeader(boolean dateFmt, T bean, List tableData) {
        // 解决时间转换错误问题
        Map tableDataMap = beanToMap(bean);
        if (CollectionUtil.isEmpty(tableData)) {
            convertHeader(null, dateFmt, false, tableDataMap);
            return this;
        }
        tableDataMap.put(Constant.OFFICE_CONVERT_DATA_SON, tableData);
        convertHeader(null, dateFmt, true, tableDataMap);
        return this;
    }

    public void convert(HttpServletResponse response, boolean dateFmt, boolean isSon, Map tableDataMap) {
        this.dateFmt = dateFmt;
        this.tableDataMap = tableDataMap;
        if (isSon) {
            List data = (List) tableDataMap.get(Constant.OFFICE_CONVERT_DATA_SON);
            if (CollectionUtil.isNotEmpty(data)) {
                data.forEach(e -> {
                    String[] toArray = DTOToList.toArray(e, template.getFullField());
                    tableRowDataList.add(toArray);
                });
            }
        }
        if (modelInputStream == null) {
            throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_ERROR.setParams(new String[]{
                    exportType.getType(), "该对象已失效，请勿重复调用"
            })).build();
        }

        if (response != null) {
            export(response);
        }
    }

    public void convertHeader(HttpServletResponse response, boolean dateFmt, boolean isSon, Map tableDataMap) {
        this.dateFmt = dateFmt;
        this.tableDataMap = tableDataMap;
        if (isSon) {
            List data = (List) tableDataMap.get(Constant.OFFICE_CONVERT_DATA_SON);
            if (CollectionUtil.isNotEmpty(data)) {
                data.forEach(e -> {
                    String[] toArray = DTOToList.toArray(e, template.getFullField());
                    tableRowDataList.add(toArray);
                });
            }
        }
        if (modelInputStream == null) {
            throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_ERROR.setParams(new String[]{
                    exportType.getType(), "该对象已失效，请勿重复调用"
            })).build();
        }

        if (response != null) {
            exportHeader(response);
        }
    }

    /**
     * 执行导出操作
     *
     * @param response
     */
    public void export(HttpServletResponse response) {
        try {
            configResponse(response, template);
        } catch (Exception e) {
            log.error("设置异常：{}", e);
            throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_ERROR.setParams(new String[]{
                    exportType.getType(), e.getMessage()
            })).build();

        }
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(modelInputStream)) {
            if (dateFmt) {
                DateUtils.convertToDate(tableDataMap);
            }
            XWPFDocument templateDocument = new XWPFDocument(bufferedInputStream);


            OfficeWordTableUtils.fillTableData(templateDocument, tableDataMap, tableRowDataList);
            convertOperation.convert(templateDocument, response.getOutputStream());
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            log.error("导出异常：{}", e);
            throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_ERROR.setParams(new String[]{
                    exportType.getType(), e.getMessage()
            })).build();
        }
    }

    /**
     * 执行导出操作(带页眉)
     *
     * @param response
     */
    public void exportHeader(HttpServletResponse response) {
        try {
            configResponse(response, template);
        } catch (Exception e) {
            log.error("设置异常：{}", e);
            throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_ERROR.setParams(new String[]{
                    exportType.getType(), e.getMessage()
            })).build();

        }
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(modelInputStream)) {
            if (dateFmt) {
                DateUtils.convertToDate(tableDataMap);
            }
            XWPFDocument templateDocument = new XWPFDocument(bufferedInputStream);


            OfficeWordTableUtils.replaceHeaderAndBody(templateDocument, tableDataMap, tableRowDataList);
            convertOperation.convert(templateDocument, response.getOutputStream());
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            log.error("导出异常：{}", e);
            throw CommonException.builder().resultCode(PoiErrorEnum.COMMON_POI_EXPORT_ERROR.setParams(new String[]{
                    exportType.getType(), e.getMessage()
            })).build();
        }
    }

    private void configResponse(HttpServletResponse response, OfficeTemplate template) throws Exception {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(template.getFileName(), "UTF-8") + exportType.getSuffix());
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=UTF-8");
        if (ExportType.PDF.getType().equals(exportType.getType())) {
            response.setContentType("application/pdf;charset=UTF-8");
        }
        response.setHeader("Cache-Control", "no-cache");
    }

    /**
     * 实体类对象转化成bean，key为属性的名称
     * @param object
     * @return
     */
    public static Map beanToMap(Object object) {
        Map map = BeanOperate.beanToMap(object);
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断是否是图片
            if (field.isAnnotationPresent(Image.class)) {
                String name = field.getName();
                Object value = map.remove(name);
                map.put(name + WordExportTypeConstants.WORD_EXPORT_TYPE_IMAGE, value);
            }
        }
        return map;
    }

}
