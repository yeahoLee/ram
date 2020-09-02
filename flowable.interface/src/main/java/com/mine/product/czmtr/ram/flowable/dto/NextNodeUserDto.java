package com.mine.product.czmtr.ram.flowable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName : NextNodeUserDto
 * @Description : 下一环节用户
 * @Author : Mr.Chai
 * @Date : 2020-06-30 18:46
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NextNodeUserDto implements Serializable {
    private static final long serialVersionUID = 1613575974596898182L;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 中文名
     */
    private String chsName;
    /**
     * 部门Id
     */
    private String deptId;
    /**
     * 部门编码
     */
    private String deptCode;
    /**
     * 部门名称
     */
    private String deptName;

}
