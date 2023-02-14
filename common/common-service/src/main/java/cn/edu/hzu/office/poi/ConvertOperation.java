package cn.edu.hzu.office.poi;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.OutputStream;

@FunctionalInterface
public interface ConvertOperation {
    void convert(XWPFDocument templateDocument, OutputStream outputStream);
}