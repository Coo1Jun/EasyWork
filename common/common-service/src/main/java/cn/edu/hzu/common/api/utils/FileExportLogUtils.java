package cn.edu.hzu.common.api.utils;

import cn.edu.hzu.common.constant.FileExportLogConstant;
import cn.edu.hzu.common.dto.SystemFileLogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @author lzf
 * @createTime 2022/09/03
 * @description 文件导出日志工具类
 */
@Slf4j
public class FileExportLogUtils {

    /**
     * 导出日志记录
     *
     * @param responseTime 响应时长
     * @param state        响应状态
     * @param sheetName    文件名称
     * @param size         数据行
     * @param type         文件类型
     */
    public static void exportLog(long responseTime, String state, String sheetName, Integer size, String type) {
        try {
            SystemFileLogDto systemFileLogDto = new SystemFileLogDto();
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            String realIp = null;
            try {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                //获取真实ip
                realIp = getRealIp(request);
                //附件地址
                systemFileLogDto.setFileUrl(request.getRequestURL().toString());
                //模块标识
                systemFileLogDto.setModelCode(request.getRequestURI());
            } catch (Exception e1) {
                log.info("{}导出日志获取客户端IP失败-------{}", type, e1.getMessage());
                e1.printStackTrace();
            }
            //日志对象
            //文件名称
            systemFileLogDto.setFileName(sheetName);
            //数据大小
            systemFileLogDto.setFileExportNum(size.toString());
            //系统标识
            systemFileLogDto.setSysCode(FileExportLogConstant.SYS_CODE);
            //导出状态
            systemFileLogDto.setResponse(state);
            //响应时长
            systemFileLogDto.setResponseDate(responseTime + "ms");
            //客户端主机IP
            systemFileLogDto.setClientIp(realIp);
            //导出类型
            systemFileLogDto.setFileType(type);
            CompletableFuture.runAsync(() -> {
                log.info("{}导出日志开始-------对象{}", type, systemFileLogDto);
                RequestContextHolder.setRequestAttributes(requestAttributes);
                // TODO 系统导出下载文件记录日志(systemFileLogDto添加到数据库)
                log.info("{}导出日志结束-------", type);
            });
        } catch (Exception e) {
            log.info("{}导出日志失败-------{}", type, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取真实IP
     *
     * @param request 请求体
     * @return 真实IP
     */
    public static String getRealIp(HttpServletRequest request) {
        // 这个一般是Nginx反向代理设置的参数
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP的情况（只取第一个IP）
        if (ip != null && ip.contains(",")) {
            String[] ipArray = ip.split(",");
            ip = ipArray[0];
        }
        return ip;
    }
}
