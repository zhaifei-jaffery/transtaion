package com.test.txmanager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.*;

/**
 * @program: FBS-Transaction
 * @description:
 * @author: 翟飞
 * @create: 2019-07-31 01:18
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //事务组中的事务状态列表   事务组id和对应的事务组列表
    private static Map<String,List<String>> transactionTypeMap=new HashMap<>();
    // 事务组是否已经接收到结束的标记
    private static Map<String,Boolean> isEndMap=new HashMap<>();
    //事务组中应该有的事务个数
    private static Map<String,Integer> transactionCountMap=new HashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel =ctx.channel();
        channelGroup.add(ctx.channel());
    }

    /**
     * 创建事务组 并且保存事务
     * 并且需要判断,如果所有事务都已经执行了(有结果了,要么回滚，要么提交) 且其中有一个事务需要回滚，那么通知所有的客户端进行回滚
     * 否则 则通知所有客户端进行提交
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受数据:"+msg.toString());
        JSONObject jsonpObject = JSON.parseObject((String) msg);

        // create->创建事务组  add->添加事务组
        String command=jsonpObject.getString("command");
        //  事务组id
        String groupId=jsonpObject.getString("groupId");
        // 子事务类型  commit->待提交   rollback->待回滚
        String transactionType=jsonpObject.getString("transactionType");
        //  事务数量
        Integer transactionCount=jsonpObject.getInteger("transactionCount");
        //  是否结束事务
        Boolean isEnd=jsonpObject.getBoolean("isEnd");
        if ("create".equals(command)){
            // 创建事务组
            transactionTypeMap.put(groupId,new ArrayList<String>());
        }else if ("add".equals(command)){
            //  加入事务组
            transactionTypeMap.get(groupId).add(transactionType);
            // 如果是结束事务
            if (isEnd){
                isEndMap.put(groupId,true);
                transactionCountMap.put(groupId,transactionCount);
            }

            JSONObject result = new JSONObject();
            result.put("groupId",groupId);
            // 如果已经接收到结束事务的标记 比较事务是否已经全部到达 如果已经全部到达则看是否需要回滚
            if(isEndMap.get(groupId) && transactionCountMap.get(groupId).equals(transactionTypeMap.get(groupId).size())){
                if (transactionTypeMap.get(groupId).contains("rollback")){
                    result.put("command","rollback");
                    sendResult(result);
                }else{
                    result.put("command","commit");
                    sendResult(result);
                }
            }
        }

    }
    private void sendResult(JSONObject result){
        for (Channel channel : channelGroup) {
            System.out.println("发送数据:"+result.toJSONString());
            channel.writeAndFlush(result.toJSONString());
        }
    }
}
    