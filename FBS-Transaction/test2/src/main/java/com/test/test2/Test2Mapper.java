package com.test.test2;

import org.apache.ibatis.annotations.Insert;

/**
 * @program: FBS-Transaction
 * @description:
 * @author: 翟飞
 * @create: 2019-07-30 23:47
 **/
public interface Test2Mapper {
    @Insert("insert into teacher values(1,9)")
    public int insert();
}
