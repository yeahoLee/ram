package com.mine.product.czmtr.ram.asset.service;

import java.util.Map;

import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.DictionDto;


public interface IDictionService {
    /**
     * 添加数据字典
     */
    public void createDiction(DictionDto dto);

    /**
     * 修改数据字典
     */
    public void updateDiction(DictionDto dto);

    /**
     * 删除数据字典
     */
    public void deleteDiction(String dictionId);

    /**
     * 查询数据字典
     */
    PageDto<DictionDto> getDictionList(Map<String, Object> params, PageableDto pageable);
}	
