package cn.edu.hzu.common.exception;

import cn.edu.hzu.common.api.IResultCode;
import lombok.ToString;

/**
 * @author lzf
 * @createTime 2022/08/23
 * @description 自定义通用异常
 */
@ToString
public class CommonException extends RuntimeException{
    // transient修饰过的不参与序列化过程
    private final transient IResultCode resultCode;

    public static CommonException.CustomExceptionBuilder builder() {
        return new CommonException.CustomExceptionBuilder();
    }

    public IResultCode getResultCode() {
        return this.resultCode;
    }

    public CommonException(final IResultCode resultCode) {
        this.resultCode = resultCode;
    }

    @ToString
    public static class CustomExceptionBuilder {
        private IResultCode resultCode;

        CustomExceptionBuilder() {
        }

        public CommonException.CustomExceptionBuilder resultCode(final IResultCode resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public CommonException build() {
            return new CommonException(this.resultCode);
        }
    }
}
