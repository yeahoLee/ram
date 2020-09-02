package com.mine.product.czmtr.ram.base.jdbc;

import com.mine.product.czmtr.ram.base.dto.FaCardViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨杰
 * @date 2020/5/6 11:59
 */
@Service
public class FaCardViewDao implements Rowmapper, RowmapperAll {
    @Autowired
    private JDBCUtils jdbcUtils;

    @Override
    public Object rowMapper(ResultSet rs) {
        FaCardViewDto view = null;
        try {
            if (rs.next()) {
                view = new FaCardViewDto();
                view.setAssetCode(rs.getNString("ASSET_CODE"));
                view.setAssetName(rs.getNString("ASSET_NAME"));
                view.setSpec(rs.getNString("SPEC"));
                view.setDepamount(rs.getNString("DEPAMOUNT"));
                view.setNetamount(rs.getNString("NETAMOUNT"));
                view.setLocaloriginvalue(rs.getNString("LOCALORIGINVALUE"));
                view.setSalvage(rs.getNString("SALVAGE"));
                view.setServiceyear(rs.getNString("SERVICEYEAR"));
                view.setPkCorp(rs.getNString("PK_CORP"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public List<Object> rowMapperAll(ResultSet rs) {
        List<Object> list = new ArrayList<Object>();
        FaCardViewDto view = null;
        try {
            while (rs.next()) {
                view = new FaCardViewDto();
                view.setAssetCode(rs.getNString("ASSET_CODE"));
                view.setAssetName(rs.getNString("ASSET_NAME"));
                view.setSpec(rs.getNString("SPEC"));
                view.setDepamount(rs.getNString("DEPAMOUNT"));
                view.setNetamount(rs.getNString("NETAMOUNT"));
                view.setLocaloriginvalue(rs.getNString("LOCALORIGINVALUE"));
                view.setSalvage(rs.getNString("SALVAGE"));
                view.setServiceyear(rs.getNString("SERVICEYEAR"));
                view.setPkCorp(rs.getNString("PK_CORP"));
                list.add(view);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //封装单条查询
    public FaCardViewDto load(String classname, String url, String usename, String password, String assetCode) {
        String sql = "SELECT * FROM NC0111.FA_CARD_V where ASSET_CODE=?";
        FaCardViewDto viewDto = (FaCardViewDto) jdbcUtils.load(classname, url, usename, password, sql, new Object[]{assetCode}, this);
        return viewDto;
    }

    //多条查询
    public List<FaCardViewDto> queryAllDao(String classname, String url, String usename, String password) {
        String sql = "SELECT * FROM NC0111.FA_CARD_V";
        List<Object> list = jdbcUtils.queryAll(classname, url, usename, password, sql, null, this);
        List<FaCardViewDto> dtoList = new ArrayList<>();
        for (Object obj : list) {
            dtoList.add((FaCardViewDto) obj);
        }
        return dtoList;
    }
}
