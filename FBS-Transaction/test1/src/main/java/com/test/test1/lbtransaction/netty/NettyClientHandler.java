package com.test.test1.lbtransaction.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.test1.lbtransaction.transactional.LbTransaction;
import com.test.test1.lbtransaction.transactional.LbTransactionManager;
import com.test.test1.lbtransaction.transactional.TransactionType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * @program: FBS-Transaction
 * @description:
 * @author: 翟飞
 * @create: 2019-07-31 02:00
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受数据:"+msg.toString());
        JSONObject jsonpObject = JSON.parseObject((String) msg);

        String command=jsonpObject.getString("command");
        String groupId=jsonpObject.getString("groupId");

        // 接受整个事务组的结果 (提交 or 回滚)
        System.out.println("接收command:"+command);

        // 对事务进行操作

        // 根据事务组id拿到列表列表
        List<LbTransaction> list = LbTransactionManager.getLbTransaction(groupId);
        //如果事务组需要提交   通知子事务去执行
        if ("commit".equals(command)){
            for (LbTransaction lbTransaction : list) {
                // 修改状态为待提交
                lbTransaction.setTransactionType(TransactionType.commit);
                // 唤醒
                lbTransaction.getTask().signalTask();
            }
        }else {
            for (LbTransaction lbTransaction : list) {
                // 修改状态为待回滚
                lbTransaction.setTransactionType(TransactionType.rollback);
                // 唤醒
                lbTransaction.getTask().signalTask();
            }
        }
    }

    public synchronized Object call(JSONObject data) throws Exception{
        context.writeAndFlush(data.toJSONString());
        return null;
    }
}
    