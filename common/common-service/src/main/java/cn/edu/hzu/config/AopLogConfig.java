package cn.edu.hzu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author lzf
 * @create 2022/11/17
 * @description http调用的日志打印
 */
@Slf4j
@Aspect
@Component
public class AopLogConfig {
    //    @Autowired
    static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ssoServer服务的controller切入点
     */
    final String ssoServerExecution = "execution(public * com.ew.server..controller.*.*(..))";
    final String ewProjectExecution = "execution(public * com.ew.project..controller.*.*(..))";
    /**
     * ew-server 的client接口，用于服务调用
     */
    final String ewServerClientExecution = "execution(public * com.ew.server.client..*.*(..))";
    final String test = "execution(public * com.ew.server..service.*.*(..))";

    private static long startTime;

    /**
     * 切点路径：Controller层的所有方法
     */
//    @Pointcut(value = ssoServerExecution + "||" + test) // 多个execution用逻辑运算符
    @Pointcut(value = ssoServerExecution
            + "||" + ewProjectExecution
            + "||" + ewServerClientExecution) // 使用变量传参，缺点就是idea的插件不能生效，在切入的方法左边看不到小图标，但不影响使用
    public void methodPath() {
    }

    /**
     * 入参
     *
     * @param joinPoint 切点
     */
    @Before(value = "methodPath()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String url = requestAttributes.getRequest().getRequestURL().toString();
        log.info("HTTP请求 = {}:{}, 入参 = {}", requestAttributes.getRequest().getMethod(), url, toJsonString(joinPoint.getArgs()));
        startTime = System.currentTimeMillis();
    }

    /**
     * 出参
     *
     * @param res 返回
     */
    @AfterReturning(returning = "res", pointcut = "methodPath()")
    public void after(Object res) {
        long endTime = System.currentTimeMillis();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String url = requestAttributes.getRequest().getRequestURL().toString();
        log.info("HTTP请求 = {}:{}, 出参 = {}", requestAttributes.getRequest().getMethod(), url, toJsonString(res));
        log.info("HTTP总耗时 {} ms", endTime - startTime);
    }

    private Object toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
//            e.printStackTrace();
            log.warn("======参数{{}}转换json出错======", obj);
        }
        return obj;
    }

    private Object toJsonString(Object[] objs) {
        if (objs == null)
            return "null";

        int iMax = objs.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.valueOf(toJsonString(objs[i])));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
