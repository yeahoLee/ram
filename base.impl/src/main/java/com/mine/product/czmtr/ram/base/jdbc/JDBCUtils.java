package com.mine.product.czmtr.ram.base.jdbc;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨杰
 * @date 2020/5/6 11:12
 */
@Service
public class JDBCUtils {

    //数据库的链接
    public Connection getConn(String classname, String url, String usename, String password) {
        Connection conn = null;
        try {
            //加载驱动
            Class.forName(classname);
            conn = DriverManager.getConnection(url, usename, password);
        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载失败！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("链接数据库失败！");
            e.printStackTrace();
        }
        return conn;
    }

    //update()可以实现增删改,注意数组中的元素要与sql中一致；
    public void update(String classname, String url, String usename, String password, String sql, Object[] values) {
        Connection conn = getConn(classname, url, usename, password);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("update方法创建Prepare失败");
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    //单条查询
    public Object load(String classname, String url, String usename, String password, String sql, Object[] values, Rowmapper rm) {
        Object ojb = null;
        Connection conn = getConn(classname, url, usename, password);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            ResultSet rs = ps.executeQuery();
            //接收查询出的结果
            ojb = rm.rowMapper(rs);
        } catch (SQLException e) {
            System.out.println("单条查询中创建PrepareStatement失败");
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ojb;
    }

    //多条查询
    public List<Object> queryAll(String classname, String url, String usename, String password, String sql, Object[] values, RowmapperAll ra) {
        Connection conn = getConn(classname, url, usename, password);
        PreparedStatement ps = null;
        List<Object> list = new ArrayList<Object>();
        try {
            ps = conn.prepareStatement(sql);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    ps.setObject(i + 1, values[i]);
                }
            }
            ResultSet rs = ps.executeQuery();
            //接收查询出的结果
            list = ra.rowMapperAll(rs);
        } catch (SQLException e) {
            System.out.println("多条查询中创建PrepareStatement失败");
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
