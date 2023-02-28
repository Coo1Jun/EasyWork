package com.ew.server.config;


import cn.edu.hzu.common.constant.Constant;
import com.sso.core.filter.SsoWebFilter;
import com.sso.core.store.SsoLoginStore;
import com.sso.core.util.JedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class SsoConfig implements InitializingBean, DisposableBean {

    @Value("${sso.redis.address}")
    private String redisAddress;

    @Value("${sso.redis.expire.minute}")
    private int redisExpireMinute;

    @Override
    public void afterPropertiesSet() throws Exception {
        SsoLoginStore.setRedisExpireMinute(redisExpireMinute);
        JedisUtil.init(redisAddress);
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }

    /**
     * SSO Server认证中心地址
     */
    @Value("${sso.server}")
    private String ssoServer;

//    @Value("${sso.logout.path}")
//    private String ssoLogoutPath;

    /**
     * 路径排除Path，允许设置多个，且支持Ant表达式。用于排除SSO客户端不需要过滤的路径
     */
    @Value("${sso.excluded.paths}")
    private String ssoExcludedPaths;
    /**
     * redis地址
     */
    @Value("${sso.redis.address}")
    private String ssoRedisAddress;


    @Bean
    public FilterRegistrationBean ssoFilterRegistration() {

        // redis init
        JedisUtil.init(ssoRedisAddress);

        // filter init
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setName("SsoWebFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*"); // 拦截所有请求
        registration.setFilter(new SsoWebFilter()); // 拦截之后所要处理的过滤器
        registration.addInitParameter(Constant.SSO_SERVER, ssoServer);
//        registration.addInitParameter(Conf.SSO_LOGOUT_PATH, ssoLogoutPath);
        registration.addInitParameter(Constant.SSO_EXCLUDED_PATHS, ssoExcludedPaths);

        return registration;
    }
}
