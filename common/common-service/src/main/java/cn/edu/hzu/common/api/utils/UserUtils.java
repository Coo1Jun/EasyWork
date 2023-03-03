package cn.edu.hzu.common.api.utils;

import cn.edu.hzu.common.constant.Constant;
import cn.edu.hzu.common.entity.SsoUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lzf
 * @create 2023/02/14
 * @description 用户相关工具类
 */
public class UserUtils {

    private static final SsoUser baseUser = new SsoUser();

    public static SsoUser getBaseUser() {
        baseUser.setRealName("ew_root");
        baseUser.setEngName("ew_root");
        baseUser.setUserid("ew_root_id");
        return baseUser;
    }

    /**
     * 获取当前Request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return null == attributes ? null : attributes.getRequest();
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    public static SsoUser getCurrentUser() {
        HttpServletRequest request = getRequest();
        if (null != request) {
            SsoUser ssoUser = (SsoUser) request.getAttribute(Constant.SSO_USER);
            if (null != ssoUser) {
                return ssoUser;
            }
        }
        return getBaseUser();
    }
}
