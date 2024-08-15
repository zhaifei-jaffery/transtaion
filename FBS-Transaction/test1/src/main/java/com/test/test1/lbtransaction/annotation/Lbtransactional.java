package com.test.test1.lbtransaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *   类似于Spring的Transactional
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lbtransactional {

    /**
     *  是否是第一个事务  默认为false
     * @return
     */
    boolean isStart() default false;

    /**
     *  是否是最后一个事务  默认为false
     * @return
     */
    boolean isEnd() default false;
}
