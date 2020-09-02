package com.mine.product.czmtr.ram.base.jdbc;

import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

/**
 * @author 杨杰
 * @date 2020/5/6 11:20
 */
@Repository
public interface Rowmapper {
    //此处根据需求，将rs查询的结果集用对象接收；
    public Object rowMapper(ResultSet rs);
}
