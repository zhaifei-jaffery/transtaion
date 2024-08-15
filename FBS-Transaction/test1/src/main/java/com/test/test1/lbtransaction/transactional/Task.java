package com.test.test1.lbtransaction.transactional;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: FBS-Transaction
 * @description:  自定义任务类
 * @author: 翟飞
 * @create: 2019-07-31 04:27
 **/
public class Task {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     *   任务等待
     */
    public void waitTask(){
        try {
            lock.lock();
            condition.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 任务唤醒
     */
    public void signalTask(){
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
    