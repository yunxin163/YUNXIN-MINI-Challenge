package xyh.lixue.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author XiangYida
 * @version 2019/5/4 14:25
 * 记录请求URL，IP已经请求耗时
 */
@Aspect
@Slf4j
@Component
public class WebAspect {

    ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    @Pointcut("execution(public * xyh.lixue.*.web.*.*(..))")
    public void print() {
    }

    @Before("print()")
    public void doBefore(){
        threadLocal.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        log.info("URL----> "+attributes.getRequest().getRequestURL());
        log.info("IP----> "+attributes.getRequest().getRemoteAddr());
    }

    @After("print()")
    public void deAfter(){
        log.info("RequestTimeConsuming----> "+(System.currentTimeMillis()-threadLocal.get())+" ms");
    }
}
