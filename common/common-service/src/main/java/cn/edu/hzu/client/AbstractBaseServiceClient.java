package cn.edu.hzu.client;


import cn.edu.hzu.common.api.RestResponse;
import cn.edu.hzu.common.api.ResultCode;
import cn.edu.hzu.common.enums.CommonErrorEnum;
import cn.edu.hzu.common.enums.ProjectEnum;
import cn.edu.hzu.common.exception.CommonException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBaseServiceClient {
    protected void execAfter(boolean throwEx, ProjectEnum projectEnum, RestResponse response) {
        if (ResultCode.SUCCESS.getCode() != response.getCode() && throwEx) {
            log.error("跨服务调用【{}-{}】::结果:{}", projectEnum.getCode(), projectEnum.getMsg(), JSON.toJSONString(response));
            throw CommonException.builder().resultCode(
                    CommonErrorEnum.COMMON_CLIENT_SERVICE_ERROR.setParams(new String[]{projectEnum.getMsg(), response.getMsg()})
            ).build();
        }

    }

    protected void execBefore(ProjectEnum projectEnum, Object... param) {
        log.info("跨服务调用【{}-{}】::参数:{}", projectEnum.getCode(), projectEnum.getMsg(), JSON.toJSONString(param));

    }

    protected void throwCheckOne(ProjectEnum projectEnum, String id) {

        throw CommonException.builder().resultCode(
                CommonErrorEnum.COMMON_CLIENT_SERVICE_ERROR.setParams(new String[]{projectEnum.getMsg(), id})
        ).build();


    }
}
