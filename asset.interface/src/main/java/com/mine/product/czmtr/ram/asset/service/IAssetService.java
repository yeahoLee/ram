package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface IAssetService {
    /** 
     * @Description: 变更资产管理员
     * @Param:  
     * @return:  
     * @Author: lichuan.zhang 
     * @Date: 2020/8/6 
     */ 

	void changeAssetManager(List<String> assetIdList, String assetManagerId);

    /**
     * @Description: 使用人变更
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/8/6
     */

    void changeAssetUser(List<String> assetIdList, String assetUserId);

    /**
     * @Description: 安装位置变更
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/8/6
     */
    void changeAssetSavePlace(List<String> assetIdList, String assetSavePlaceId);


    /**
     * @Description: 资产调拨；
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/8/11
     */
    void doAssetAllocation(List<String> assetIdList, String departmentId, String managerId, String savePlaceId);

    /**
     * @Description: 资产使用人和使用部门变更
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/8/11
     */
    void doAssetUserUpdate( Map<String, String> assetMap, String uerID, String departmentID);

    /**
     * @Description: 资产使用人和使用部门变更安装位置变更；
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/8/11
     */
    void doAssetUserDepAndPlaceUpdate(Map<String,String> map, String uerID, String departmentID);

/**
 * @Description: 封存资产 安装位置变更
 * @Param:
 * @return:
 * @Author: lichuan.zhang
 * @Date: 2020/8/14
 */
    void doAssetPlaceUpdate( List<String> assetIdList,String sealPlaceId);




	/**
     * 资产类型
     */
    public static enum ASSET_TYPE {
        固定资产, 列管物资, 组合资产, 子资产_固定资产, 子资产_列管物资
    }

    /**
     * 来源方式
     */
    public static enum SOURCE_TYPE {
        建设移交, 采购, 投资, 捐入, 盘盈
    }

    /**
     * 资产状态
     */
    public static enum ASSET_STATUS {
        使用, 借出, 封存, 报废, 丢失,
        捐出, 停用, 闲置, 冻结, 盘亏, 调拨
    }

    /**
     * 变更方式
     */
    public static enum CHANGE_TYPE {
        资产借用, 资产领用, 管理人变更, 使用人变更, 位置变更, 资产借用归还, 资产领用归还
    }

    /**
     * 历史记录类型
     */
    public static enum HISTORY_TYPE {
        基础信息变更, 变更记录, 封存启封记录, 调拨记录, 盘点记录,
        新增记录, 减少记录
    }

    /**
     * 封存状态
     */
    public static enum SEALED_UNSEALED {
        启封, 封存
    }

    /**
     * 盘点状态
     */
    public static enum INVENTORY_CHECK {
        拟稿, 盘点中, 盘点报告待审批, 盘点报告审批中, 盘点完成
    }

    /**
     * 我的盘点状态
     */
    public static enum MY_INVENTORY_CHECK {
        盘点中, 已提交, 已完成
    }

    /**
     * 盘点操作
     */
    public static enum INVENTORY_OPERATION {
        手工录入, 自动录入
    }

    /**
     * 盘点结果
     */
    public static enum INVENTORY_RESULT {
        盘亏, 盘盈, 相符
    }

    /**
     * 盘点方式
     */
    public static enum INVENTORY_WAY {
        手工, 扫码
    }

    /**
     * 减少方式
     */
    public static enum REDUCE_TYPE {
        盘亏, 捐出, 报废, 丢失
    }

    enum ASSET_PRODUCE_TYPE {
        生产性物资, 非生产性物资
    }

    /**
     * @Description: 冻结资产
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/28
     */
    void LockAssetState(List<String> assetIdList);
    /**
     * @Description: 审批失败资产状态回滚
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/28
     */
    void approveFailureAssetStateRollback(List<String> assetIdList);

    /**
     * @Description: 审批成功资产更新资产状态
     * @Param: status 目标状态
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/28
     */

    void approveSuccessUpdateAssetState(List<String> assetIdList, ASSET_STATUS status);

    /**
     * @Description: 审批前进行资产状态校验
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/28
     */
    AssetApprove doAssetApproveCheck(List<String> assetIdList, ASSET_STATUS status);

    public void createAsset(AssetAssetDto dto, UserInfoDto userInfoDto);

    public void updateAsset(AssetAssetDto dto);

    public AssetAssetDto getAssetByAssetId(String assetId);

    public void deleteAsset(String assetId);

    Map<String, Object> getHistoryByQuerysForDataGrid(String historyType, String assetId, PageableDto pageableDto);

    Map<String, Object> getHistoryByQuerysForInterface(String assetId);

    public void uploadFile(String assetModelId, String string, byte[] bytesArray, String fileSpec);

    public Map<String, Object> getUploadFileByQuerysForDataGrid(String assetModelId, PageableDto pageableDto);

    public AssetDownLoadFileDto getDownloadFileById(String uploadId);

    public void deleteUploadFile(String id);

    //检查历史记录
    void commonCheckHistoryDto(AssetHistoryDto historyDto);

    public void batchUpdateAsset(String assetIdList, AssetAssetDto assetDto, UserInfoDto userInfoDto);

    public byte[] exportAssetStanBookByQuerys(Object assetDtoList);


    Map<String, Object> getAssetByQuerys(AssetAssetDto assetDto, String showType);
    Map<String, Object> getAssetByQuerysForDataGrid(String isFilt, ISearchExpression searchExpression, PageableDto pageableDto);

    public void createDynamicView(AssetDynamicViewDto dynamicViewDto);

    public Map<String, Object> getAssetDynamicForDataGrid(PageableDto pageableDto, UserInfoDto dto);

    public AssetDynamicViewDto getDynamicViewById(String dynamicViewId);

    public void updateAssetDynamicById(AssetDynamicViewDto dto);

    public void deleteDynamicViewById(String dynamicViewId);

    List<AssetAssetDto> findAssetByHeadId(String headId);

    public List<AssetAssetDto> excuteAssetViewConditionForDataGrid(String id);

    public void changeDynamicViewState(String id, String assetViewState);

    public List<AssetAssetDto> getAssetDtoByMaterialCode(String materialCode, AssetAssetDto assetAssetDto);

    List<AssetAssetDto> getListByIdList(List<String> assetIdList);

    public List<AssetAssetDto> assetDetailForDataGrid(String materialCode);

    //获取资产名称
    public String getAssetNameByAssetId(String assetId);

    public Boolean getAssetStatus(ASSET_STATUS oldstatus, ASSET_STATUS newstatus);

    public AssetHistoryDto createHistory(AssetHistoryDto historyDto);

    public void updateAssetBeforeAssetStatus(AssetAssetDto dto);

	void commonCheck(AssetAssetDto dto);

    void commonCheckBySearch(AssetAssetDto dto);

    public void createAssetByDtos(List<AssetAssetDto> saveDto);

    //检查传递过来的资产是否有误
    public List<ErrorBean> commonCheckAssetForOutApi(List<AssetBean> assetBeanList);

    Map<String, Object> getAssetByCondition(ISearchExpression searchExpression);

    Map<String, Object> getAssetByList(ISearchExpression searchExpression, PageableDto pageableDto);

    public byte[] printAssetCardByAssetIdList(String assetIdList);

    public long countAssetByExpressStr(ISearchExpression iSearchExpression);

    /**
     * @Description: 从物资系统申请物资编码
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/8/26
     */
    AssetApprove applyAssetCode(String id);

    public void finishApprove(String id);

    public Set<String> getAssetTypeIdSetByUserId(String id);

    /**
     * @Description: 根据当前登录人的管理角色查询其名下所有的资产对应的资产物资编码
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/8/12
     */
    Set<String> getAssetTypeIdSetByUserRole();

    List<String> getAssetIdByExpressStr(ISearchExpression searchExpression);

    List<AssetAssetDto> getNoSyncAddAsset();

    public Object getAssetByAssetCode(String assetCode);

    public String getAllSyncAssetBean();

    /**
     * @Description: 定时任务：将闲置的资产转化为停用状态；
     * @Param: []
     * @return: void
     * @Author: lichuan.zhang
     * @Date: 2020/7/13
     */
    void convertToStop();


    /**
     * @Description: 发起审批 ，冻结资产
     * @Param: Id 资产id
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/16
     */
    void doApprove(String Id);

    /**
     * @Description: 审批成功  ，设置新资产状态
     * @Param: Id 资产id
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/16
     */
    void approveSuccess(String Id, ASSET_STATUS newState);


    /**
     * @Description: 审批失败 ，资产状态回滚
     * @Param: Id 资产id
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/16
     */
    void approveFailure(String Id);

    /** 
     * @Description: 根据资产编码获取物资类型
     * @Param:  
     * @return:  
     * @Author: lichuan.zhang 
     * @Date: 2020/7/30 
     */
    ASSET_PRODUCE_TYPE getProduceTypeByMaterialCode(String materialCode);
}
