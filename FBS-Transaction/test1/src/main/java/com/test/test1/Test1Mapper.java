package com.test.test1;

import org.apache.ibatis.annotations.Insert;

/**
 * @program: FBS-Transaction
 * @description:
 * @author: 翟飞
 * @create: 2019-07-30 23:47
 **/
public interface Test1Mapper {
    @Insert("insert into user values(1,'鲁班')")
    public int insert();

}
