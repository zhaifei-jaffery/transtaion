package com.test.test1.lbtransaction.aspect;

import com.test.test1.lbtransaction.connection.LbConnection;
import com.test.test1.lbtransaction.transactional.LbTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.sql.Connection;

/**
 * @program: FBS-Transaction
 * @description:   定义切面 切DataSource的getDataSource
 * @author: 翟飞
 * @create: 2019-07-31 00:37
 **/
@Aspect
public class LbDataSourceAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point){
        try {
            // 代理的ConnectionImpl
            Connection connection=(Connection) point.proceed();
            // 传入构造,把自己写的实现类返回给Spring
            return new LbConnection(connection, LbTransactionManager.getCurrent());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
    