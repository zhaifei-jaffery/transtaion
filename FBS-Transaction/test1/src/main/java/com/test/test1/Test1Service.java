package com.test.test1;

import com.test.test1.lbtransaction.annotation.Lbtransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: FBS-Transaction
 * @description:
 * @author: 翟飞
 * @create: 2019-07-30 23:48
 **/
@Service
public class Test1Service {
    @Autowired
    private Test1Mapper test1Mapper;

    @Lbtransactional(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public void test(){
        test1Mapper.insert();
        //test2
        HttpUtils.get("http://localhost:8002/test2");

        int i=100/0;
    }
}
    