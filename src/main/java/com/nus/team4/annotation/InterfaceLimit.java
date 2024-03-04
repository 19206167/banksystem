package com.nus.team4.annotation;

import java.lang.annotation.*;

/**
 * 接口访问频率注解，默认一分钟只能访问10次
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceLimit {
    long time() default 60000; // 限制时间 单位：毫秒(默认值：一分钟）

    int value() default 10; // 允许请求的次数(默认值：5次）
}
