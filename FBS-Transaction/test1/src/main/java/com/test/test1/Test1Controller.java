package com.test.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: FBS-Transaction
 * @description:
 * @author: 翟飞
 * @create: 2019-07-30 23:50
 **/
@RestController
@RequestMapping(value = "test1")
public class Test1Controller {

    @Autowired
    private Test1Service test1Service;

    @RequestMapping("")
    public void insert(){
        test1Service.test();
    }
}
    