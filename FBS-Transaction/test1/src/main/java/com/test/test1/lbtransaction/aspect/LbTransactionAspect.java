package com.test.test1.lbtransaction.aspect;

import com.test.test1.lbtransaction.annotation.Lbtransactional;
import com.test.test1.lbtransaction.transactional.LbTransaction;
import com.test.test1.lbtransaction.transactional.LbTransactionManager;
import com.test.test1.lbtransaction.transactional.TransactionType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * @program: FBS-Transaction
 * @description:   自定义注解切面
 * @author: 翟飞
 * @create: 2019-07-31 03:41
 **/
@Aspect
@Order(10000)
public class LbTransactionAspect {

    @Around("@annotation(com.test.test1.lbtransaction.annotation.Lbtransactional)")
    public void invoke(ProceedingJoinPoint joinPoint){
        //  拿到关于注解的对象
        MethodSignature signature =(MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        Lbtransactional lbtransactional=method.getAnnotation(Lbtransactional.class);

        // 第一个事务 需要创建事务组
        // 事务组id
        String groupId = "";
        if (lbtransactional.isStart()){
            // 创建事务组
          groupId = LbTransactionManager.createLbTransactionGroup();
        }else {
            groupId = LbTransactionManager.getCurrentGroupId();
        }

        //  创建事务对象
        LbTransaction lbTransaction = LbTransactionManager.createLbTransaction(groupId);
        try {
            // 执行原有的逻辑(Spring的事务切面)
            joinPoint.proceed();
            // 逻辑正常的话  将类型设置为待提交
            lbTransaction.setTransactionType(TransactionType.commit);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //  出错了就设置为待回滚
            lbTransaction.setTransactionType(TransactionType.rollback);
        }
        //添加事务
        LbTransactionManager.addLbTransaction(groupId,lbTransaction,lbtransactional.isEnd());
    }
}
    