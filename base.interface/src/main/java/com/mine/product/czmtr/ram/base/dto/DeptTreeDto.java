package com.mine.product.czmtr.ram.base.dto;

import java.util.List;

/**
 * @author 杨杰
 * @date 2019/11/14 10:09
 * 部门树
 */
public class DeptTreeDto {
    private String id;          //部门ID
    private String code;        //部门编号
    private String pid;         //父级部门ID
    private String pname;         //父级部门名称
    private String pCode;         //父级部门code
    private String state;       //state: 节点状态, 'open' 或者 'closed', 默认是 'open'. 当设置为 'closed', 节点所有的子节点将从远程服务器站点加载
    private String text;        //部门名称
    private List<DeptTreeDto> children;   //子集部门集合

    public DeptTreeDto() {
    }

    public DeptTreeDto(String id, String code, String pid, String text, List<DeptTreeDto> children) {
        this.id = id;
        this.code = code;
        this.pid = pid;
        this.text = text;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<DeptTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<DeptTreeDto> children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }
}
