package cn.edu.hzu.office.utils;

import cn.edu.hzu.common.api.utils.StringUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author lzf
 * @createTime 2022/09/03
 * @description 使用poi+iTextPDF进行word转pdf(先将word转成html，再将html转成pdf)
 */
@Slf4j
public class HtmlUtils {

    /**
     * 将docx格式文件转成html
     *
     * @return html
     */
    public static String docx2Html(XWPFDocument document) {
        String content = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayOutputStream documentOutputStream = new ByteArrayOutputStream()) {
            document.write(documentOutputStream);
            XWPFDocument doc = null;
            try (ByteArrayInputStream docInputStream = new ByteArrayInputStream(documentOutputStream.toByteArray())) {
                doc = new XWPFDocument(docInputStream);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new IOException();
            }
            XHTMLOptions options = XHTMLOptions.create();
            XHTMLConverter.getInstance().convert(doc, baos, options);
            options.setIgnoreStylesIfUnused(false);
            options.setFragment(true);
            content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件输出错误");
        }
        return content;
    }

    /**
     * 使用jsoup规范化html
     *
     * @param html html内容
     * @return 规范化后的html
     */
    public static String formatHtml(String html) {
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        // 去除过大的宽度
        if (checkStyle(doc, "width")) {
            doc.attr("style", "");
        }

        doc.select("div")
                .stream()
                .filter(e -> checkStyle(e, "width"))
                .forEach(e -> e.attr("style", ""));

        String temptd = null;
        for (Element td : doc.select("td")) {
            if (!checkStyle(td)) {
                td.attr("style", temptd);
            }
            temptd = td.attr("style");
        }

        String temp = null;
        for (Element p : doc.select("p")) {
            if (!checkStyle(p)) {
                p.attr("style", temp);
            }
            temp = p.attr("style");
        }

        // jsoup生成闭合标签
        doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return doc.html();
    }

    /**
     * html转成pdf
     *
     * @param html html
     */
    public static void htmlToPdf(String html, OutputStream outputStream) {
        com.itextpdf.text.Document document = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(html.getBytes())) {
            document = new com.itextpdf.text.Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            // html转pdf
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, bais, StandardCharsets.UTF_8,
                    new FontProviderAdapter() {
                        @Override
                        public Font getFont(String s, String s1, boolean embedded, float size, int style, BaseColor baseColor) {
                            // 配置字体
                            Font font = null;
                            try {
                                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                                font = new Font(bf, size, style, baseColor);
                                font.setColor(baseColor);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return font;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    private static boolean checkStyle(Node doc) {
        String style = doc.attr("style");
        return StringUtils.isNotEmpty(style);
    }

    private static boolean checkStyle(Node doc, String containStyle) {
        String style = doc.attr("style");
        return StringUtils.isNotEmpty(style) && style.contains(containStyle);
    }

    private static class FontProviderAdapter implements FontProvider {

        @Override
        public boolean isRegistered(String s) {
            return false;
        }

        @Override
        public Font getFont(String s, String s1, boolean b, float v, int i, BaseColor baseColor) {
            return null;
        }
    }
}
