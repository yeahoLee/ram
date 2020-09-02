package com.mine.product.czmtr.ram.asset.service;

import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetReduceDto;
import com.mine.product.czmtr.ram.asset.dto.AssetReduceTempDto;

import java.util.List;
import java.util.Map;

/***
 * 资产减损管理
 * @author guoli.sun
 *
 */
public interface IAssetReduceService extends IApproveNotify{
    AssetReduceDto saveReduce(AssetReduceDto dto);

    void saveAndCheckReduce(AssetReduceDto dto);

    public Map<String, Object> getAssetReduceForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    void deleteReduceReceipt(String id);

    void checkReduceReceipt(String id);

    AssetReduceDto getReduceReceiptById(String id);

    void updateReduceReceipt(AssetReduceDto dto);

    List<AssetReduceTempDto> getNoSysnReduceAsset();

    void updateReduceTempSync(String id, boolean sync);
}
