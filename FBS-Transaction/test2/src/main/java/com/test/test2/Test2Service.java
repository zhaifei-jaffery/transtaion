package com.test.test2;

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
public class Test2Service {
    @Autowired
    private Test2Mapper test2Mapper;

    @Transactional
    public void test(){
        test2Mapper.insert();
    }
}
    