package com.mine.product.czmtr.ram.asset.service;

import java.util.Map;

import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeTempDto;

public interface IMaterialCodeTempService {
    boolean findMaterialCodeTempModelByCodeTempAndTempNum(String codeTemp, String tempNum);

    MaterialCodeTempDto createMaterialCodeTemp(MaterialCodeTempDto dto);

    void deleteMaterialCodeTemp(String id);

    MaterialCodeTempDto updateMaterialCodeTemp(MaterialCodeTempDto dto);

    MaterialCodeTempDto getMaterialCodeTempById(String id);

    PageDto<MaterialCodeTempDto> getMaterialCodeTemp(String hql, Map<String, Object> params, PageableDto pageable);

    PageDto<MaterialCodeTempDto> getMaterialCodeTemp(String hql, String countHql, Map<String, Object> params, PageableDto pageable);
}
