package cn.edu.hzu.common.api;

/**
 *公共错误码枚举基类
 *
 * @author lzf
 * @createTime 2022/09/03
 */
public interface CustomResultCode extends IResultCode{

    /**
     * 动态参数
     *
     * @return
     */
    Object[] getParams();

    /**
     * 设置参数
     *
     * @param params
     * @return
     */
    CustomResultCode setParams(Object[] params);
}
