package com.mine.product.czmtr.ram.asset.service;

import java.util.List;
import java.util.Map;

import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeTempDto;

public interface IMaterialCodeService {

    String getMaterialCodeByAssetCode(String code);

    List<MaterialCodeTempDto> getMultipleMaterialCode(String code, Integer num);

    MaterialCodeDto createMaterialCode(MaterialCodeDto dto);

    void deleteMaterialCode(String id);

    MaterialCodeDto updateMaterialCode(MaterialCodeDto dto);

    MaterialCodeDto getMaterialCodeById(String id);

    PageDto<MaterialCodeDto> getMaterialCode(String hql, Map<String, Object> params, PageableDto pageable);

    PageDto<MaterialCodeDto> getMaterialCode(String hql, String countHql, Map<String, Object> params, PageableDto pageable);
}
