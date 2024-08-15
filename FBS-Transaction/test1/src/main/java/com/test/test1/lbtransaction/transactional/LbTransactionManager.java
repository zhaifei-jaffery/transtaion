package com.test.test1.lbtransaction.transactional;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import com.test.test1.lbtransaction.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.UUID;

/**
 * @program: FBS-Transaction
 * @description:   自定义事务管理器
 * @author: 翟飞
 * @create: 2019-07-31 03:50
 **/
public class LbTransactionManager {

    private static NettyClient nettyClient;

    @Autowired
    public void setNettyClient(NettyClient nettyClient){
        LbTransactionManager.nettyClient=nettyClient;
    }

    /**
     *  事务组对应的事务列表
     */
    private static Map<String,List<LbTransaction>> map = new HashMap<>();
    /**
     *   同一个线程共享对象
     */
    private static ThreadLocal<LbTransaction> current = new ThreadLocal<>();
    /**
     *  事务组id放进ThreadLocal
     */
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    /**
     * 事务数量
     */
    private static ThreadLocal<Integer> transactionCount=new ThreadLocal<>();
    /**
     *   创建事务组
     * @return
     */
    public static String createLbTransactionGroup(){
        // 事务组id
        String groupId= UUID.randomUUID().toString();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",groupId);
        jsonObject.put("command","create");
        currentGroupId.set(groupId);
        // 发送请求到事务管理者
        nettyClient.send(jsonObject);

        return groupId;
    }

    /**
     * 把事务添加到事务组
     * @param groupId   事务组id
     * @param lbTransaction   事务对象
     * @param isEnd   是否是最后一个事务
     */
    public static void addLbTransaction(String groupId,LbTransaction lbTransaction,Boolean isEnd){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",lbTransaction.getGroupId());
        jsonObject.put("transactionId",lbTransaction.getTransactionId());
        jsonObject.put("transactionType",lbTransaction.getTransactionType());
        jsonObject.put("command","add");
        jsonObject.put("isEnd",isEnd);
         nettyClient.send(jsonObject);
        System.out.println("添加事务");
    }

    /**
     *   创建事务对象
     * @return
     */
    public static LbTransaction createLbTransaction(String groupId){
        String transactionId = UUID.randomUUID().toString();
        LbTransaction lbTransaction = new LbTransaction(groupId, transactionId);
        List<LbTransaction> lbTransactions = new ArrayList<>();
        lbTransactions.add(lbTransaction);
        map.put(groupId,lbTransactions);
        current.set(lbTransaction);
        return lbTransaction;
    }

    /**
     *   获取事务列表
     * @param groupId
     * @return
     */
    public static List<LbTransaction> getLbTransaction(String groupId){
        return map.get(groupId);
    }

    /**
     *  从ThreadLocal里获取对象
     * @return
     */
    public static LbTransaction getCurrent(){
        return current.get();
    }

    /**
     *  从ThreadLocal里获取事务组id
     * @return
     */
    public static String getCurrentGroupId(){
        return currentGroupId.get();
    }

    /**
     *  把事务组id放进ThreadLocal
     * @return
     */
    public static void setCurrentGroupId(String groupId){
       currentGroupId.set(groupId);
    }

    /**
     *  把数量设置进去
     */
    public static void setTransactionCount(Integer groupId){
        transactionCount.set(groupId);
    }
    public static Integer getTransactionCount(){
        return transactionCount.get();
    }
}
    