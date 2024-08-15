package com.test.test2;

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
@RequestMapping(value = "test2")
public class Test2Controller {

    @Autowired
    private Test2Service test2Service;

    @RequestMapping("")
    public void insert(){
        test2Service.test();
    }
}
    