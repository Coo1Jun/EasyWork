package cn.edu.hzu.common.api.utils;

import cn.edu.hzu.common.annotation.Excel;
import cn.edu.hzu.common.annotation.Excel.ColumnType;
import cn.edu.hzu.common.annotation.Excel.Type;
import cn.edu.hzu.common.annotation.Excels;
import cn.edu.hzu.common.api.utils.bean.ExcelOperate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lzf
 * @createTime 2022/08/14
 * @description Excel相关处理
 */
@Slf4j
public class ExcelUtils<T> {

    /**
     * Excel sheet最大行数
     */
    public static final int SHEET_SIZE = 100000;

    /**
     * 实体数据对象类型
     */
    private Class<T> clazz;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Type type;

    /**
     * 工作簿对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private XSSFSheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 导入Excel时，Excel有效数据的最后一行，传入该参数可以准确读取.
     * 传入该参数时，则只会读取到该参数指定的最后一行。
     * 导入一次后，不管成功与否，该参数失效。
     */
    Integer lastLine;

    /**
     * 导出列计数器
     */
    private AtomicInteger atomicInteger = new AtomicInteger(1);
    /**
     * sheet计数器
     */
    private AtomicInteger sheetAtomicInt = new AtomicInteger(1);
    /**
     * 导出开始时间
     */
    private long startTime;
    /**
     * 导出结束时间
     */
    private long endTime;

    public ExcelUtils(Class<T> clazz) {
        this.clazz = clazz;
    }

    private void init(List<T> list, String sheetName, Type type) {
        if (list == null){
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
    }

    /**
     * 得到数据对象定义的所有字段（属性）
     */
    private void createExcelField() {
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        // 获取父类定义的所有字段
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        // 获取自己定义的所有字段
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields) {
            // 单注解
            if (field.isAnnotationPresent(Excel.class)) {
                putToField(field, field.getAnnotation(Excel.class));
            }
            // 多注解
            if (field.isAnnotationPresent(Excels.class)) {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel excel : excels) {
                    putToField(field, excel);
                }
            }
        }
    }

    /**
     * 将字段和对应的Excel注解放到集合中
     * @param field 字段
     * @param excelAnnotation Excel注解
     */
    private void putToField(Field field, Excel excelAnnotation) {
        if (excelAnnotation != null && (excelAnnotation.type() == Type.ALL || excelAnnotation.type() == type)) {
            this.fields.add(new Object[]{field, excelAnnotation});
        }
    }

    /**
     * 创建一个工作簿
     */
    private void createWorkbook() {
        this.wb = new XSSFWorkbook();
    }

    /**
     * 将list数据源的数据导入到excel表单中
     *
     * @param list 数据集
     * @param sheetName 工作表的名称
     */
    public ExcelOperate exportExcel(List<T> list, String sheetName) {
        long startTime = System.currentTimeMillis();
        this.init(list, sheetName, Type.EXPORT);
        exportExcel();
        log.info("生成Excel数据总共用时{}ms", System.currentTimeMillis() - startTime);
        return new ExcelOperate(sheetName, wb);
    }

    private void exportExcel() {
        // 填充头部和数据
        setHeaderAndData();
    }

    /**
     * 将list数据源的数据导入到excel表单并下载
     *
     * @param list 数据集
     * @param sheetName 工作表的名称
     * @param response 响应
     */
    public void exportExcelAndDownload(List<T> list, String sheetName, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        this.init(list, sheetName, Type.EXPORT);
        exportExcelAndDownload(response, startTime);
    }

    private void exportExcelAndDownload(HttpServletResponse response, long startTime) {
        try {
            // 填充头部和数据
            setHeaderAndData();
            // 下载文件
            if (wb != null) {
                downLoadExcel(sheetName, response, wb);
            }

        } catch (IOException e) {
            log.error("导出Excel异常{}", e);
            e.printStackTrace();
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e1) {
                    log.error("导出Excel异常{}", e1);
                }
            }
            endTime = System.currentTimeMillis();
            log.info("导出==> " + sheetName + ".xlsx 总共用时 {} ms", endTime - startTime);
        }
    }

    private void setHeaderAndData(){
        // 计算一同有多少sheet
        int sheetNo = list.size() / SHEET_SIZE;
        for (int index = 0;index <= sheetNo;index++){
            createSheet(sheetNo, index);
            // 产生一行
            Row row = sheet.createRow(0);
            int column = 0;
            // 写入各个字段的列头名称
            for (Object[] field : fields) {
                // 取该字段的Excel注解
                Excel excel = (Excel) field[1];
                // 填入字段名字，column++指向下个字段单元格的位置
                this.createCell(excel, row, column++);
            }
            // 填入数据
            if (Type.EXPORT.equals(type)) {
                fillExcelData(index, row);
            }
        }
    }

    /**
     * 创建单元格
     *
     * @param excel 字段注解
     * @param row 行
     * @param column 单元格的位置（列）
     */
    private Cell createCell(Excel excel, Row row, int column) {
        // 创建列
        Cell cell = row.createCell(column);
        // 写入列信息
        cell.setCellValue(excel.name());
        setDataValidation(excel, row, column);
        // 设置样式
        cell.setCellStyle(styles.get("header"));
        return cell;
    }

    /**
     * 创建表格样式（处理Excel注解的其他属性）
     *
     * @param excel excel注解
     * @param row 行
     * @param column 单元格的位置（列）
     */
    private void setDataValidation(Excel excel, Row row, int column) {
        // 设置列宽，Excel注解设置的宽度单位是字符，setColumnWidth是以一个字符的1/256的宽度作为一个单位
        sheet.setColumnWidth(column, (int) ((excel.width() + 0.72) * 256));
        row.setHeight((short) (excel.height() * 20));
        // 如果设置了提示信息则鼠标放上去提示
        if (StringUtils.isNotEmpty(excel.prompt())) {
            // 从第2行开始到整个数据的结尾每一行提示.
            setXssfPrompt(sheet, "", excel.prompt(), 1, list.size(), column, column);
        }
        // 如果设置了combo属性则本列只能选择不能输入
        if (excel.combo().length > 0) {
            // 从第2行开始到整个数据的结尾每一行只能选择不能输入.
            setXssfValidation(sheet, excel.combo(), 1, list.size(), column, column);
        }
    }

    /**
     * 设置 POI XSSFSheet 单元格提示
     *
     * @param sheet         表单
     * @param promptTitle   提示标题
     * @param promptContent 提示内容
     * @param firstRow      开始行
     * @param endRow        结束行
     * @param firstCol      开始列
     * @param endCol        结束列
     */
    private void setXssfPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
                              int firstCol, int endCol) {
        // 创建数据验证类
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 创建数据验证约束的单元格是DD1（一般不会用到这么多字段，所以DD1一般不会被用到），提示标题 和 提示内容 的数据就放在这个单元格
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        // 设置数据有效性加载在哪个单元格上
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 创建数据校验的对象
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 传入数据（标题和内容）
        dataValidation.createPromptBox(promptTitle, promptContent);
        // 设置显示提示框
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet    要设置的sheet.
     * @param textList 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return 设置好的sheet.
     */
    private void setXssfValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
    }

    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row   单元格行
     */
    private void fillExcelData(int index, Row row) {
        int startNo = index * SHEET_SIZE;
        int endNo = Math.min(startNo + SHEET_SIZE, list.size());
        for (int i = startNo; i < endNo; i++) {
            row = sheet.createRow(i + 1 - startNo);
            // 得到导出对象.
            T vo = list.get(i);
            int column = 0;
            for (Object[] os : fields) {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                // 设置实体类私有属性可访问
                // 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
                if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                        || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
                    field.setAccessible(true);
                }
                this.addCell(excel, row, vo, field, column++);
            }
        }
    }

    /**
     * 添加单元格
     * @param excel Excel注解
     * @param row 行
     * @param vo 数据对象
     * @param field 元素
     * @param column 第几列
     */
    private Cell addCell(Excel excel, Row row, T vo, Field field, int column) {
        Cell cell = null;
        try {
            // 设置行高
            row.setHeight((short) (excel.height() * 20));
            // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
            if (excel.isExportData()) {
                // 创建cell
                cell = row.createCell(column);
                cell.setCellStyle(styles.get("data")); // 内容的样式

                // 用于读取对象中的属性   
                Object value = getTargetValue(vo, field, excel);
                String dateFormat = excel.dateFormat();
                String readConverterExp = excel.readConverterExp();
                if (StringUtils.isNotEmpty(dateFormat) && StringUtils.isNotNull(value)) {
                    cell.setCellValue(DateUtils.parseDateToStr(dateFormat, (Date) value));
                } else if (StringUtils.isNotEmpty(readConverterExp) && StringUtils.isNotNull(value)) {
                    cell.setCellValue(convertByExp(String.valueOf(value), readConverterExp));
                } else {
                    // 设置列类型
                    setCellVo(value, excel, cell);
                }
            }
        } catch (Exception e) {
            log.error("导出Excel失败{}", e);
        }
        return cell;
    }

    /**
     * 获取bean中的属性值
     *
     * @param vo    实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception {
        Object o = field.get(vo);
        // 如果是另一个类中的属性名称
        if (StringUtils.isNotEmpty(excel.targetAttr())) {
            String target = excel.targetAttr();
            if (target.indexOf(".") > -1) {
                String[] targets = target.split("[.]");
                for (String name : targets) {
                    o = getValue(o, name);
                }
            } else {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法方法形式获取值
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception {
        if (StringUtils.isNotEmpty(name)) {
            Class<?> clazz = o.getClass();
            // 获取get方法的名称
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName);
            method.setAccessible(true); // 设置可访问，防止抛异常
            // 调用getter方法
            o = method.invoke(o);
        }
        return o;
    }

    /**
     * 设置单元格信息
     *
     * @param value 单元格值
     * @param excel  注解相关
     * @param cell  单元格信息
     */
    private void setCellVo(Object value, Excel excel, Cell cell) {
        // 如果输出类型是字符串
        if (ColumnType.STRING == excel.cellType()) {
            cell.setCellValue(StringUtils.isNull(value) ? excel.defaultValue() : value + excel.suffix()); // 判空后，加设置的后缀
        } else if (ColumnType.NUMERIC == excel.cellType()) {
            cell.setCellValue(Integer.parseInt(value + ""));
        }
    }

    /**
     * 创建工作表
     * @param sheetNo 工作表总数
     * @param index 序号
     */
    private void createSheet(int sheetNo, int index) {
        this.sheet = (XSSFSheet) wb.createSheet();
        this.styles = createStyles(wb);
        // 设置工作表名称
        if (sheetNo == 0) {
            wb.setSheetName(index, sheetName);
        } else {
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作簿对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        // =========== 设置内容的样式 ===========================
        // 设置水平对齐的样式为：水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直方向对齐的样式为：居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置 上下左右 的边宽均为 细线，并且设置变宽颜色为 浅灰色（#808080）
        style.setBorderRight(BorderStyle.THIN); // 右
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN); // 左
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN); // 上
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN); // 底
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        // 设置字体
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial"); // 字体的样式为：Arial
        dataFont.setFontHeightInPoints((short) 10); // 字体大小为：10
        style.setFont(dataFont);
        styles.put("data", style);// 保存内容（数据）的样式

        // =========== 设置头部的的样式==========================
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data")); // 复制数据的样式，在其基础上再做修改
        // 设置水平对齐的样式为：水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直方向对齐的样式为：居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置单元格背景色为：浅灰色（#808080）
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        // 设置背景的渲染效果为：满渲染，即全部填充
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置字体
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial"); // 字体的样式为：Arial
        headerFont.setFontHeightInPoints((short) 10); // 字体大小为：10
        headerFont.setBold(true); // 字体加粗
        headerFont.setColor(IndexedColors.WHITE.getIndex()); // 字体颜色为：纯白色（#ffffff）
        style.setFont(headerFont);
        styles.put("header", style); // 保存头部样式

        return styles;
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename) {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 解析字符串，如"0=男,1=女"
     *
     * @param propertyValue '='前的参数值
     * @param converterExp 要被解析的字符串
     * @return 解析后的值，'='后的结果
     */
    public static String convertByExp(String propertyValue, String converterExp) {
        if (StringUtils.isEmpty(propertyValue)) {
            return propertyValue;
        }
        if (StringUtils.isNull(converterExp)) {
            throw new NullPointerException("解析的对象为空");
        }
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (itemArray[0].equals(propertyValue)) {
                return itemArray[1];
            }
        }
        return propertyValue;
    }

    /**
     * 反向解析字符串，如"0=男,1=女"
     *
     * @param propertyValue '='后的参数值
     * @param converterExp 要被解析的字符串
     * @return 解析后的值，'='前的结果
     */
    public static String reverseByExp(String propertyValue, String converterExp) {
        if (StringUtils.isEmpty(propertyValue)) {
            return propertyValue;
        }
        if (StringUtils.isNull(converterExp)) {
            throw new NullPointerException("解析的对象为空");
        }
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (itemArray[1].equals(propertyValue)) {
                return itemArray[0];
            }
        }
        return propertyValue;
    }

    /**
     * 设置响应头，下载文件
     *
     * @param fileName 文件名称
     * @param response 响应请求
     * @param workbook 工作簿对象
     * @throws IOException
     */
    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        fileName = encodingFilename(fileName);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }


    /**
     * ========================== Excel的导入 ==================================================================
     */

    /**
     * 解析上传的Excel
     *
     * @param file 文件
     * @return 转换后的结果集
     */
    public List<T> importExcel(MultipartFile file) {
        try {
            return importExcel(StringUtils.EMPTY, file.getInputStream());
        } catch (IOException e) {
            log.error("导入Excel失败{}", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析上传的Excel
     *
     * @param file 文件
     * @param lastLine 导入Excel时，Excel有效数据的最后一行，传入该参数可以准确读取.
     *      传入该参数时，则只会读取到该参数指定的最后一行。
     *      导入一次后，不管成功与否，该参数失效。
     * @return 转换后的结果集
     */
    public List<T> importExcel(MultipartFile file, int lastLine) {
        this.lastLine = lastLine;
        try {
            return importExcel(StringUtils.EMPTY, file.getInputStream());
        } catch (IOException e) {
            log.error("导入Excel失败{}", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对excel表单默认第一个索引名转换成list
     *
     * @param is 输入流
     * @return 转换后的结果集
     */
    public List<T> importExcel(InputStream is) {
        return importExcel(StringUtils.EMPTY, is);
    }

    /**
     * 对excel表单默认第一个索引名转换成list
     *
     * @param is 输入流
     * @param lastLine 导入Excel时，Excel有效数据的最后一行，传入该参数可以准确读取.
     *      传入该参数时，则只会读取到该参数指定的最后一行。
     *      导入一次后，不管成功与否，该参数失效。
     * @return 转换后的结果集
     */
    public List<T> importExcel(InputStream is, int lastLine) {
        this.lastLine = lastLine;
        return importExcel(StringUtils.EMPTY, is);
    }

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param is 输入流
     * @param lastLine 导入Excel时，Excel有效数据的最后一行，传入该参数可以准确读取.
     *      传入该参数时，则只会读取到该参数指定的最后一行。
     *      导入一次后，不管成功与否，该参数失效。
     * @return 转换后的结果集
     */
    public List<T> importExcel(String sheetName, InputStream is, int lastLine){
        this.lastLine = lastLine;
        return importExcel(sheetName, is);
    }

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param is 输入流
     * @return 转换后的结果集
     */
    public List<T> importExcel(String sheetName, InputStream is) {
        if (is == null) {
            log.error("输入流为空！");
            throw new NullPointerException("输入流为空！");
        }
        this.type = Type.IMPORT;
        List<T> list = new ArrayList<>();
        try {
            // 根据输入流获得工作簿对象
            this.wb = WorkbookFactory.create(is);
            Sheet sheet = null;
            if (StringUtils.isNotEmpty(sheetName)) {
                // 如果指定sheet名,则取指定sheet中的内容.
                sheet = wb.getSheet(sheetName);
            } else {
                // 如果传入的sheet名不存在则默认指向第1个sheet.
                sheet = wb.getSheetAt(0);
            }

            if (sheet == null) {
                throw new IOException("文件sheet不存在");
            }

            int rows;
            if (this.lastLine == null) {
                rows = sheet.getPhysicalNumberOfRows();
            } else {
                rows = this.lastLine;
            }
// ============================= 以上工作簿对象创建完毕，接下来就是从工作簿对象中读取数据 ======================
            if (rows > 0) {
                // 定义一个map用于存放excel列头部的内容和序号
                Map<String, Integer> headMap = new HashMap<>();
//  =========================== 获取表头 ===========================
                Row head = sheet.getRow(0);
                for (int i = 0;i < head.getPhysicalNumberOfCells();i++) {
                    Cell cell = head.getCell(i);
                    if (cell != null) {
                        String value = this.getCellValue(head, i).toString();
                        headMap.put(value, i);
                    } else {
                        headMap.put(null, i);
                    }
                }
//  ======================= 表头读取完毕 ===================

//  ======================= 获取字段元素和在表格中是第几列 ===============
                // 有数据时才处理 得到类的所有field.
                Field[] allFields = clazz.getDeclaredFields();
                // 定义一个map用于存放列的序号和field.
                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
                for (int col = 0; col < allFields.length; col++) {
                    // 获得元素
                    Field field = allFields[col];
                    // 获取元素的@Excel注解
                    Excel attr = field.getAnnotation(Excel.class);
                    if (attr != null && (attr.type() == Type.ALL || attr.type() == type)) {
                        // 设置类的私有字段属性可访问.
                        field.setAccessible(true);
                        // 获得该元素在Excel表格中是第几列
                        Integer column = headMap.get(attr.name());
                        fieldsMap.put(column, field);
                    }
                }
                // ============= 读取数据 ==============
                for (int i = 1;i < rows;i++) {
                    // 从第2行开始取数据,默认第一行是表头.
                    Row row = sheet.getRow(i);
                    // 每一行数据的实体
                    T entity = null;
                    // 遍历所有字段的field，根据属性给entity的字段赋值
                    for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet()) {
                        // 获得单元格的值，key就是该元素在第几列
                        Object val = this.getCellValue(row, entry.getKey());
                        // 如果不存在实例则新建
                        entity = (entity == null ? clazz.newInstance() : entity);
                        // 从map中得到对应的field
                        Field field = fieldsMap.get(entry.getKey());
                        // 取得实体类字段定义的类型,并根据字段的类型 将单元格的值val赋过去.
                        Class<?> fieldType = field.getType();
                        // 获得@Excel注解
                        Excel excel = field.getAnnotation(Excel.class);
                        // 处理targetAttr属性
                        if (StringUtils.isNotEmpty(excel.targetAttr())) {
                            String[] names = excel.targetAttr().split("[.]");
                            for (String name : names) {
                                fieldType = getTargetAttrType(fieldType, name);
                            }
                        }
//          =============== 接下来是判断实体类中字段的数据类型 =============
                        val = changeType(val, fieldType);
//              ============= 到这里，val就是实体entity一个字段的值 =======================

//              ============= 接下来处理@Excel注解属性 ==========================
                        String propertyName = field.getName(); // 这里获取到是实体类的属性的名称，如 Integer age; 获取到就是"age".
                        // @Excel注解的处理targetAttr属性 和 readConverterExp属性
                        if (StringUtils.isNotEmpty(excel.targetAttr())) {
                            propertyName = field.getName() + "." + excel.targetAttr();
                        } else if (StringUtils.isNotEmpty(excel.readConverterExp())) {
                            val = reverseByExp(String.valueOf(val), excel.readConverterExp());
                        }
                        // ====== 通过反射调用实体的setter，将值注入 ======
                        ReflectUtils.invokeSetter(entity, propertyName, val); // 这里entity是引用传递，即方法里面的修改会影响entity本身
                    }
                    list.add(entity);
                }
            }
        } catch (Exception e) {
            log.error("导入Excel失败{}", e);
            e.printStackTrace();
        } finally {
            // 导入一次后，不管成功与否，该参数失效。
            this.lastLine = null;
        }
        return list;
    }

    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column) {
        if (row == null) {
            return null;
        }
        Object val = "";
        Cell cell = row.getCell(column);
        if (cell != null) {
// ================ 判断单元格中数据的类型，并获取 =================
            // 1.如果是数字或公式
            if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                val = cell.getNumericCellValue(); // 得到的是double
                // 1.1 如果是日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                } else { // 1.2 不是日期，就是数字
                    val = new BigDecimal(String.valueOf(val)).toPlainString();
                }
            // 2. 如果是字符串
            } else if (cell.getCellType() == CellType.STRING) {
                val = cell.getStringCellValue();
            // 3. 如果是boolean类型
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                val = cell.getBooleanCellValue();
            // 4. 如果单元格内容是否产生错误
            } else if (cell.getCellType() == CellType.ERROR) {
                val = cell.getErrorCellValue();
            }
        }
        return val;
    }

    /**
     * 将val根据type的类型转换
     *
     * @param val
     * @param type
     * @return
     */
    private Object changeType(Object val, Class<?> type) {
        if (val == null) {
            return null;
        }
        if (type == null) {
            return val;
        }
        // 1、String类型
        if (String.class == type) {
            String s = Convert.toStr(val); // Object -> String
            // 如果是读取数字转为字符串，则将".0"结尾去除
            if (StringUtils.endsWith(s, ".0")) {
                val = StringUtils.substringBefore(s, ".0");
            } else {
                val = Convert.toStr(val);
            }
        } else if ((Integer.TYPE == type) || (Integer.class == type)) {
            val = Convert.toInt(val); // Object -> Integer
        } else if ((Long.TYPE == type) || (Long.class == type)) {
            val = Convert.toLong(val); // Object -> Long
        } else if ((Double.TYPE == type) || (Double.class == type)) {
            val = Convert.toDouble(val); // Object -> Double
        } else if ((Float.TYPE == type) || (Float.class == type)) {
            val = NumberUtils.toFloat((String)val); // Object -> Float
        } else if (BigDecimal.class == type) {
            val = Convert.toBigDecimal(val); // Object -> BigDecimal
        } else if (Date.class == type) { // Object -> Date
            if (val instanceof String) {
                val = DateUtils.parseDate(val);
            } else if (val instanceof Double) {
                val = DateUtil.getJavaDate((Double) val);
            }
        }
        return val;
    }

    /**
     *
     * @param clazz 类型
     * @param name 属性名称
     * @return 获得clazz类型下属性名为name的Class类型
     */
    private Class<?> getTargetAttrType(Class<?> clazz, String name) {
        if (clazz == null) {
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field.getType();
            }
        }
        return clazz;
    }
}
