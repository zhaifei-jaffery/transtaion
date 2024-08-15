package com.test.test1.lbtransaction.transactional;

/**
 * @program: FBS-Transaction
 * @description:    自定义事务类
 * @author: 翟飞
 * @create: 2019-07-31 04:00
 **/
public class LbTransaction {
    private String groupId;  // 事务组id
    private String transactionId;    // 事务id
    private TransactionType transactionType;   // 事务类型 (待提交/待回滚)
    private Task task;   // 每个事务会自带一个任务

    public LbTransaction(String groupId, String transactionId) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.task = new Task();
    }
    public LbTransaction(){}

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Task getTask() {
        return task;
    }
}
    