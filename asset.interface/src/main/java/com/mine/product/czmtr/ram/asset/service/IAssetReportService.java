package com.mine.product.czmtr.ram.asset.service;

import java.util.List;
import java.util.Map;

import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetReportHistoryDto;
import com.mine.product.czmtr.ram.asset.dto.AssetUsabilityHistoryDto;

public interface IAssetReportService {

    public Map<String, Object> QueryAssetChangeHistory(ISearchExpression searchExpression, PageableDto pageableDto);

    public Map<String, Object> QueryAssetLoseHistory(ISearchExpression searchExpression, PageableDto pageableDto);

    public Map<String, Object> QueryAssetMoveHistory(AssetReportHistoryDto assetReportHistoryDto, PageableDto pageableDto);

    public Map<String, Object> QueryAssetUsablitityHistory(AssetUsabilityHistoryDto assetUsabilityHistoryDto);

    public Map<String, Object> GetAssetChangeHistory(AssetReportHistoryDto assetReportHistoryDto, PageableDto pageableDto);
}
