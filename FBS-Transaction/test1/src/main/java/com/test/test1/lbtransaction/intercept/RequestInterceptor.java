package com.test.test1.lbtransaction.intercept;

import com.test.test1.lbtransaction.transactional.LbTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: FBS-Transaction
 * @description:
 * @author: 翟飞
 * @create: 2019-07-31 05:16
 **/
@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
       String groupId=request.getHeader("groupId");
       String transactionCount = request.getHeader("transactionCount");
       LbTransactionManager.setCurrentGroupId(groupId);
       LbTransactionManager.setTransactionCount(Integer.valueOf(transactionCount==null? "0":transactionCount));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
    