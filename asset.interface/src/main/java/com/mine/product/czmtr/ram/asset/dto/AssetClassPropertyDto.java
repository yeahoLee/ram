package com.mine.product.czmtr.ram.asset.dto;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class AssetClassPropertyDto implements Serializable {
    private String id; //系统自动生成id
    private String cate_pk;
    private String cate_level1; //一级分类
    private String cate_level1_desc;//一级分类名称
    private String cate_level2; //二级分类
    private String cate_level2_desc;//二级分类名称
    private String cate_level3; //三级分类
    private String cate_level3_desc; //三级分类名称
    private String cate_level4; //四级分类
    private String cate_level4_desc; //四级分类名称
    private String address_code; //位置编码
    private String address_info; //位置描述
    private String asset_type; //分类
    private String cate_comb;
    private String cate_type; //数据类型 assettype  位置assetaddress
    private String cate_comb_desc;
    private String item_cate;
    private String cate1_summary;
    private String cate_level1_pro;
    private String display_level;
    private String is_export;
    private String balance_book;
    private String balance_book_id;
    private String fa_flag;
    private Date lastUpdateDate;//最后更新时间
    private Date createTimestamp;
    private Date lastUpdateTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCate_pk() {
        return cate_pk;
    }

    public void setCate_pk(String cate_pk) {
        this.cate_pk = cate_pk;
    }

    public String getCate_level1() {
        return cate_level1;
    }

    public void setCate_level1(String cate_level1) {
        this.cate_level1 = cate_level1;
    }

    public String getCate_level1_desc() {
        return cate_level1_desc;
    }

    public void setCate_level1_desc(String cate_level1_desc) {
        this.cate_level1_desc = cate_level1_desc;
    }

    public String getCate_level2() {
        return cate_level2;
    }

    public void setCate_level2(String cate_level2) {
        this.cate_level2 = cate_level2;
    }

    public String getCate_level2_desc() {
        return cate_level2_desc;
    }

    public void setCate_level2_desc(String cate_level2_desc) {
        this.cate_level2_desc = cate_level2_desc;
    }

    public String getCate_level3() {
        return cate_level3;
    }

    public void setCate_level3(String cate_level3) {
        this.cate_level3 = cate_level3;
    }

    public String getCate_level3_desc() {
        return cate_level3_desc;
    }

    public void setCate_level3_desc(String cate_level3_desc) {
        this.cate_level3_desc = cate_level3_desc;
    }

    public String getCate_level4() {
        return cate_level4;
    }

    public void setCate_level4(String cate_level4) {
        this.cate_level4 = cate_level4;
    }

    public String getCate_level4_desc() {
        return cate_level4_desc;
    }

    public void setCate_level4_desc(String cate_level4_desc) {
        this.cate_level4_desc = cate_level4_desc;
    }

    public String getAddress_code() {
        return address_code;
    }

    public void setAddress_code(String address_code) {
        this.address_code = address_code;
    }

    public String getAddress_info() {
        return address_info;
    }

    public void setAddress_info(String address_info) {
        this.address_info = address_info;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getCate_comb() {
        return cate_comb;
    }

    public void setCate_comb(String cate_comb) {
        this.cate_comb = cate_comb;
    }

    public String getCate_type() {
        return cate_type;
    }

    public void setCate_type(String cate_type) {
        this.cate_type = cate_type;
    }

    public String getCate_comb_desc() {
        return cate_comb_desc;
    }

    public void setCate_comb_desc(String cate_comb_desc) {
        this.cate_comb_desc = cate_comb_desc;
    }

    public String getItem_cate() {
        return item_cate;
    }

    public void setItem_cate(String item_cate) {
        this.item_cate = item_cate;
    }

    public String getCate1_summary() {
        return cate1_summary;
    }

    public void setCate1_summary(String cate1_summary) {
        this.cate1_summary = cate1_summary;
    }

    public String getCate_level1_pro() {
        return cate_level1_pro;
    }

    public void setCate_level1_pro(String cate_level1_pro) {
        this.cate_level1_pro = cate_level1_pro;
    }

    public String getDisplay_level() {
        return display_level;
    }

    public void setDisplay_level(String display_level) {
        this.display_level = display_level;
    }

    public String getIs_export() {
        return is_export;
    }

    public void setIs_export(String is_export) {
        this.is_export = is_export;
    }

    public String getBalance_book() {
        return balance_book;
    }

    public void setBalance_book(String balance_book) {
        this.balance_book = balance_book;
    }

    public String getBalance_book_id() {
        return balance_book_id;
    }

    public void setBalance_book_id(String balance_book_id) {
        this.balance_book_id = balance_book_id;
    }

    public String getFa_flag() {
        return fa_flag;
    }

    public void setFa_flag(String fa_flag) {
        this.fa_flag = fa_flag;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }
}
