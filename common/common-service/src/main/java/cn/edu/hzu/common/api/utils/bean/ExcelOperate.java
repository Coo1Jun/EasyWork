package cn.edu.hzu.common.api.utils.bean;

import cn.edu.hzu.common.api.utils.ExcelUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author lzf
 * @createTime 2022/08/15
 * @description Excel操作类
 */
@Getter
@Slf4j
@AllArgsConstructor
public class ExcelOperate {
    /**
     * excel文件名
     */
    private String fileName;

    /**
     * 工作簿对象
     */
    private Workbook wb;

    /**
     * 设置响应头，下载文件
     *
     * @param response 响应请求
     */
    public ExcelOperate downLoadExcel(HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        try {
            if (wb != null){
                ExcelUtils.downLoadExcel(fileName, response, wb);
            }
            log.info("下载->" + fileName + ".xlsx总共用时{}ms", System.currentTimeMillis() - startTime);
        } catch (IOException e) {
            log.error("导出Excel异常{}", e);
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 输出到指定流
     *
     * @param outputStream 输出流
     * @return
     */
    public ExcelOperate output(OutputStream outputStream) {
        if (wb != null){
            long startTime = System.currentTimeMillis();
            try {
                wb.write(outputStream);
            } catch (IOException e) {
                log.error("导出Excel异常{}", e);
                e.printStackTrace();
            }
            log.info("输出->" + fileName + ".xlsx总共用时{}ms", System.currentTimeMillis() - startTime);
        }
        return this;
    }

    /**
     * 关闭资源
     */
    public void close() {
        if (wb != null) {
            try {
                wb.close();
            } catch (IOException e1) {
                log.error("导出Excel异常{}", e1);
            }
        }
    }
}
