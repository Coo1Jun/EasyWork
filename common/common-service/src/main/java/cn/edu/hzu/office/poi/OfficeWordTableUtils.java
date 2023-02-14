package cn.edu.hzu.office.poi;

import cn.edu.hzu.common.constant.WordExportTypeConstants;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lzf
 * @createTime 2022/09/03
 * @description
 */
@Slf4j
public class OfficeWordTableUtils {

    /**
     * @param document      word模板对象
     * @param textMap       数据对象的map
     * @param tableDataList 要另外加上的数据集
     */
    public static void fillTableData(XWPFDocument document, Map textMap,
                                     List<String[]>... tableDataList) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        int index = 0, dz = tableDataList.length;
        for (int i = 0, z = tables.size(); i < z; i++) {
            //只处理行数大于等于2的表格，且不循环表头
            XWPFTable table = tables.get(i);
            if (table.getRows().size() > 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                int size = table.getRows().size();
                int EnterNum = table.getText().split("\\n").length;
                if (size >= EnterNum) {
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(document, rows, textMap);
                } else if (dz != 0 && index < dz) {
                    log.info("insert last {} row" + "\n", (size - EnterNum));
                    insertTable(table, tableDataList[index++], EnterNum);
                }
            }
        }
        // 获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph p : paragraphs) {
            replaceContext(document, p, textMap);
        }
    }

    public static void replaceHeaderAndBody(XWPFDocument document, Map textMap,
                                            List<String[]>... tableDataList) {
        // 替换页眉
        replaceHeaderOverWrite(document, textMap);
        fillTableData(document, textMap, tableDataList);
    }

    private static void replaceHeaderOverWrite(XWPFDocument doc, Map textMap) {
        List<XWPFHeader> xwpfHeaderList = doc.getHeaderList();
        Iterator<XWPFHeader> iterator = xwpfHeaderList.iterator();
        XWPFTable para;
        XWPFHeader xwpfHeader;
        while (iterator.hasNext()) {
            xwpfHeader = iterator.next();
            List<XWPFTable> tables = xwpfHeader.getTables();
            Iterator<XWPFTable> iteratorPara = tables.iterator();
            while (iteratorPara.hasNext()) {
                para = iteratorPara.next();
                if (checkText(para.getText())) {
                    eachTable(doc, para.getRows(), textMap);
                }
            }
        }
    }

    /**
     * 遍历表格
     *
     * @param rows    表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(XWPFDocument document, List<XWPFTableRow> rows, Map textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if (checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        replaceContext(document, paragraph, textMap);
                    }
                }
            }
        }
    }

    /**
     * 替换上下文
     *
     * @param document  文档对象
     * @param paragraph 文档中的一个段落
     * @param textMap   要替换的数据集
     */
    public static void replaceContext(XWPFDocument document, XWPFParagraph paragraph, Map textMap) {
        List<XWPFRun> runs = paragraph.getRuns();
        // 这里对runs进行判空是因为，paragraph可以标识表格，当paragraph是表格时，runs是为空的，表格的填充已经在别的地方了
        if (runs != null || runs.size() > 0) {
            String text = runs.stream().map(e -> e.toString()).reduce("", (l, r) -> l = l + r);
            String ob = changeValue(document, runs, text, textMap);
            if (ob != null) {
                if (!handleNewlineText(paragraph, ob)) {
                    for (int i = 0; i < runs.size(); i++) {
                        if (i == 0) {
                            runs.get(i).setText(ob, 0);
                        } else {
                            runs.get(i).setText("", 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断文本中时候包含$
     *
     * @param text 文本
     * @return 包含返回true, 不包含返回false
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.contains("$")) {
            check = true;
        }
        return check;
    }

    /**
     * 替换图片
     *
     * @param document 文档对象
     * @param runs
     * @param textMap 数据集
     */
    public static void replacePicture(XWPFDocument document,List<XWPFRun> runs, Map textMap, String key) {
        for (int i = 0; i < runs.size(); i++) {
            runs.get(i).setText("", 0);
        }
        String picId = null;
        try {
            picId = document.addPictureData((byte[]) textMap.get(key), Document.PICTURE_TYPE_PNG);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        addPictureToRun(runs.get(0), picId, Document.PICTURE_TYPE_PNG, WordExportTypeConstants.WORD_EXPORT_TYPE_IMAGE_WIDTH, WordExportTypeConstants.WORD_EXPORT_TYPE_IMAGE_HEIGHT);
    }

    /**
     * 单元格插入图片
     *
     * @param run
     * @param blipId
     * @param id
     * @param width
     * @param height
     */
    public static void addPictureToRun(XWPFRun run, String blipId, int id, int width, int height) {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;

        CTInline inline = run.getCTR().addNewDrawing().addNewInline();

        String picXml = "" +
                "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "         <pic:nvPicPr>" +
                "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" +
                "            <pic:cNvPicPr/>" +
                "         </pic:nvPicPr>" +
                "         <pic:blipFill>" +
                "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
                "            <a:stretch>" +
                "               <a:fillRect/>" +
                "            </a:stretch>" +
                "         </pic:blipFill>" +
                "         <pic:spPr>" +
                "            <a:xfrm>" +
                "               <a:off x=\"0\" y=\"0\"/>" +
                "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" +
                "            </a:xfrm>" +
                "            <a:prstGeom prst=\"rect\">" +
                "               <a:avLst/>" +
                "            </a:prstGeom>" +
                "         </pic:spPr>" +
                "      </pic:pic>" +
                "   </a:graphicData>" +
                "</a:graphic>";

        //CTGraphicalObjectData graphicData = inline.addNewGraphic().addNewGraphicData();
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            xe.printStackTrace();
        }
        inline.set(xmlToken);
        //graphicData.set(xmlToken);

        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }

    /**
     * 匹配传入信息集合与模板
     *
     * @param value   模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(XWPFDocument document, List<XWPFRun> runs, String value, Map textMap) {
        Set<Map.Entry> textSets = textMap.entrySet();
        for (Map.Entry textSet : textSets) {
            String key = textSet.getKey().toString();
            // 如果key包含图片的标记，说明此位置应该是要导出为图片
            if (key.contains(WordExportTypeConstants.WORD_EXPORT_TYPE_IMAGE)) {
                // 模板文档中的key
                String documentKey = key.substring(0, key.length() - WordExportTypeConstants.WORD_EXPORT_TYPE_IMAGE.length());
                documentKey = "${" + documentKey + "}";
                if (value.contains(documentKey)) {
                    replacePicture(document, runs, textMap, key);
                    return null;
                }
            }
            key = "${" + key + "}";
            //匹配模板与替换值 格式${key}
            if (value.contains(key)) {
                if (textSet.getValue() == null) {
                    value = value.replace(key, "");
                } else {
                    value = value.replace(key, textSet.getValue().toString());
                }
            }
        }
        //模板未匹配到区域替换为空
//        if (checkText(value)) {
//            value = "";
//        }
        return value;
    }

    // 关闭流
    public static void end(String outputUrl, HttpServletResponse response) {
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            FileInputStream fis = new FileInputStream(outputUrl);
            byte[] cache = new byte[fis.available()];
            fis.read(cache);
            output.write(cache);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException var) {
                    log.error("导出Word/pdf异常{}", var);
                }
            }
        }
    }

    /**
     * @param paragraph
     * @param text
     * @return
     */
    private static boolean handleNewlineText(XWPFParagraph paragraph, String text) {
        if (StrUtil.isNotEmpty(text) && text.contains("\n")) {
            String[] texts = text.split("\n");
            int rowLen = texts.length;
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun r : runs) {
                r.setText(StrUtil.EMPTY, 0);
            }
            String fontFamily = "宋体";
            int fontSize = 9;
            for (int i = 0; i < rowLen; i++) {
                //对某个段落设置格式
                XWPFRun run = paragraph.createRun();
                String row = ArrayUtil.get(texts, i);
                run.setFontFamily(fontFamily);
                run.setFontSize(fontSize);
                if (StrUtil.isNotEmpty(row)) {
                    run.setText(row, 0);
                    if (!(i == (rowLen - 1))) {
                        run.addBreak();
                    }
                } else {
                    run.setText(StrUtil.EMPTY, 0);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 为表格插入数据，行数不够添加新行
     *
     * @param table     需要插入数据的表格
     * @param tableList 插入数据集合
     */
    public static void insertTable(XWPFTable table, List<String[]> tableList, int pos) {
        List<XWPFTableRow> rows = table.getRows();
        for (int i = 0; i < tableList.size(); i++) {
            int dataColumn = tableList.get(0).length;
            boolean hasRow = rows.size() - pos > i;
            XWPFTableRow row = hasRow ? table.getRow(i + pos) : table.createRow();

            CTTrPr trPr = row.getCtRow().addNewTrPr();
            CTHeight ht = trPr.addNewTrHeight();
            ht.setVal(BigInteger.valueOf(500));

            for (int j = 0; j < dataColumn; j++) {
                boolean hasColumn = row.getTableCells().size() > j;
                XWPFTableCell cell = hasColumn ? row.getCell(j) : row.createCell();
                cell.setText(tableList.get(i)[j]);//不能使用该方法直接加内容，这样会在原内容后面追加，并且不能保证跟原字体样式一致
            }
        }
    }
}
