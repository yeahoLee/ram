package com.mine.product.czmtr.ram.base.jdbc;

import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author 杨杰
 * @date 2020/5/6 11:25
 */
@Repository
public interface RowmapperAll {
    public List<Object> rowMapperAll(ResultSet rs);
}
