package com.sso.core.filter;

import com.sso.core.conf.Conf;
import com.sso.core.login.SsoWebLoginHelper;
import com.sso.core.path.impl.AntPathMatcher;
import com.sso.core.user.SsoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * web sso filter
 *
 *
 */
public class SsoWebFilter extends HttpServlet implements Filter {
    private static Logger logger = LoggerFactory.getLogger(SsoWebFilter.class);

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * SSO Server认证中心地址
     */
    private String ssoServer;
    /**
     * 注销登陆path
     */
    private String logoutPath;
    /**
     * 路径排除Path
     */
    private String excludedPaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ssoServer = filterConfig.getInitParameter(Conf.SSO_SERVER);
        logoutPath = filterConfig.getInitParameter(Conf.SSO_LOGOUT_PATH);
        excludedPaths = filterConfig.getInitParameter(Conf.SSO_EXCLUDED_PATHS);

        logger.info(">>>>>>>>> SsoWebFilter init. >>>>>>>>>");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 请求地址
        String servletPath = req.getServletPath();

        // 匹配排除的路径（不需要验证的路径）
        if (excludedPaths != null && excludedPaths.trim().length() > 0) {
            for (String excludedPath : excludedPaths.split(",")) {
                String uriPattern = excludedPath.trim();

                // 支持ANT表达式
                if (antPathMatcher.match(uriPattern, servletPath)) {
                    // 匹配成功，放行
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        // 校验登录的用户, cookie
        SsoUser ssoUser = SsoWebLoginHelper.loginCheck(req, res);

        // 如果没有登录
        if (ssoUser == null) {

            // json msg
            res.setContentType("application/json;charset=utf-8");
            res.getWriter().println("{\"code\":" + Conf.SSO_LOGIN_FAIL_RESULT.getCode() + ", \"msg\":\"" + Conf.SSO_LOGIN_FAIL_RESULT.getMsg() + "\"}");
            return;
        }

        // 可以让请求随时可以拿到ssoUser实体
        request.setAttribute(Conf.SSO_USER, ssoUser);

        // 已经登录 放行
        chain.doFilter(request, response);
    }

}
