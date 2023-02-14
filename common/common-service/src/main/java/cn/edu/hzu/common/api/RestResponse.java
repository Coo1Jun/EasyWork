package cn.edu.hzu.common.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;

/**
 * @author lzf
 * @createTime 2022/08/06
 * @description 统一返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestResponse <T>{
    /**
     * // @JsonInclude(JsonInclude.Include.NON_NULL)
     * 作用：实体类与json互转的时候属性值为null的不参与序列化，即该属性为null时，返回的json数据不带该属性
     */

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;

    /**
     * 返回数据对象
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private boolean success = true;

    @JsonIgnore
    @Transient
    public boolean isOk() {
        return code == ResultCode.SUCCESS.getCode() && success;
    }

    /**
     * 无数据的成功实体
     */
    public static <T> RestResponse<T> ok(){
        return restResult(null, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
    }

    /**
     * 有数据的成功实体
     */
    public static <T> RestResponse<T> ok(T data){
        return restResult(data, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
    }

    /**
     * 有数据、有状态信息的成功实体
     */
    public static <T> RestResponse<T> ok(T data, String msg){
        return restResult(data, ResultCode.SUCCESS.getCode(), msg);
    }

    /**
     * 默认失败实体
     */
    public static <T> RestResponse<T> failed() {
        return restResult(null, ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMsg(),false);
    }

    /**
     * 有状态信息的失败实体
     */
    public static <T> RestResponse<T> failed(String msg) {
        return restResult(null, ResultCode.FAILURE.getCode(), msg,false);
    }

    /**
     * 有返回码实体的失败实体
     */
    public static <T> RestResponse<T> failed(IResultCode resultCode) {
        return restResult(null, resultCode.getCode(), resultCode.getMsg(),false);
    }

    /**
     * 有返回码实体、有状态信息的失败实体
     */
    public static <T> RestResponse<T> failed(IResultCode resultCode, String message) {
        return restResult(null, resultCode.getCode(), message,false);
    }

    /**
     * 有数据的失败实体
     */
    public static <T> RestResponse<T> failed(T data) {
        return restResult(data, ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMsg(),false);
    }

    /**
     * 有数据、有状态信息的失败实体
     */
    public static <T> RestResponse<T> failed(T data, String msg) {
        return restResult(data, ResultCode.FAILURE.getCode(), msg,false);
    }

    /**
     * 有返回码、有状态信息的失败实体
     */
    public static <T> RestResponse<T> failed(int code, String msg) {
        return restResult(null, code, msg,false);
    }

    private static <T> RestResponse<T> restResult(T data, int code, String msg) {
        return restResult(data, code, msg,true);
    }

    /**
     * 构建返回实体
     */
    private static <T> RestResponse<T> restResult(T data, int code, String msg, boolean success){
        return RestResponse.<T>builder()
                .data(data)
                .code(code)
                .msg(msg)
                .success(success)
                .build();
    }
}
