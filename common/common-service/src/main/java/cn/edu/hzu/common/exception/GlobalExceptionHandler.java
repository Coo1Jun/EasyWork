package cn.edu.hzu.common.exception;

import cn.edu.hzu.common.api.CustomResultCode;
import cn.edu.hzu.common.api.IResultCode;
import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.ResultCode;
import cn.edu.hzu.common.api.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;


/**
 * @author lzf
 * @createTime 2022/08/07
 * @description 全局异常捕获类
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler({BindException.class})
    public RestResponse<Void> MethodArgumentNotValidExceptionHandler(BindException e){
        // 从异常对象中获取ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        log.error("校验参数失败：{}", objectError.getDefaultMessage());
        return RestResponse.failed(ResultCode.PARAM_VALID_ERROR, objectError.getDefaultMessage());
    }

    /**
     *
     * @param e 自定义的通用异常
     * @return
     */
    @ExceptionHandler({CommonException.class})
    public RestResponse handleException(CommonException e) {
        RestResponse result;
        IResultCode iResultCode = e.getResultCode();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 获取方法调用的开始时间
        long startTime = Optional.ofNullable((Long) requestAttributes.getAttribute("methodInvokeStart", RequestAttributes.SCOPE_REQUEST)).orElse(System.currentTimeMillis());
        if (iResultCode instanceof CustomResultCode) {
            CustomResultCode resultCode = (CustomResultCode) e.getResultCode();
            String msg = StringUtils.format(resultCode.getMsg(), resultCode.getParams());
            result = RestResponse.failed(resultCode.getCode(), msg);
        } else {
            result = RestResponse.failed(e.getResultCode());
        }
        log.info("ServiceException");
        try {
            log.info("耗时：{}，请求应答：{}", System.currentTimeMillis() - startTime, objectMapper.writeValueAsString(result));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("jackson转换请求应答出错");
            log.error("请求应答实体：{}", result);
        }
        return result;
    }
}
