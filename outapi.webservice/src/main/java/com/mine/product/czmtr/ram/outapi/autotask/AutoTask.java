package com.mine.product.czmtr.ram.outapi.autotask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.mine.product.czmtr.ram.outapi.autotask.impl.AutoTaskService;
import com.mine.product.czmtr.ram.outapi.autotask.interfaces.IAutoTaskService;

@Component
public class AutoTask implements IAutoTaskService {
    private static final Logger logger = LoggerFactory.getLogger(AutoTask.class);

    private boolean isSyncCom; //同步公司
    private boolean isSyncDept; //同步部门
    private boolean isSyncUser; //同步用户
    private boolean isSyncSavePlace; //同步安装位置
    private boolean isSyncAssetCode; //同步资产编码
    private boolean isSyncTypeCode; //同步分类编码
    private boolean isSyncAddAsset; //推送新增资产
    private boolean isSyncReduceAsset; //推送减损资产
    private boolean isUpdateAssetsalvage; //同步资产价值,同步财务系统提供的视图
    private boolean isSyncCK; //同步HR_ORG_CK(财务档案数据)
    @Autowired
    private IBaseService baseService;
    @Autowired
    private AutoTaskService autoTaskService;

    private void enableSyncCom() {
        isSyncCom = true;
    }

    private void disableSyncCom() {
        isSyncCom = false;
    }

    private void enableSyncDept() {
        isSyncDept = true;
    }

    private void disableSyncDept() {
        isSyncDept = false;
    }

    private void enableSyncUser() {
        isSyncUser = true;
    }

    private void disableSyncUser() {
        isSyncUser = false;
    }

    private void enableSyncSavePlace() {
        isSyncSavePlace = true;
    }

    private void disableSyncSavePlace() {
        isSyncSavePlace = false;
    }

    private void enableSyncAssetCode() {
        isSyncAssetCode = true;
    }

    private void disableSyncAssetCode() {
        isSyncAssetCode = false;
    }

    private void enableSyncTypeCode() {
        isSyncTypeCode = true;
    }

    private void disableSyncTypeCode() {
        isSyncTypeCode = false;
    }

    private void enableSyncAddAsset() {
        isSyncAddAsset = true;
    }

    private void disableSyncAddAsset() {
        isSyncAddAsset = false;
    }

    private void enableSyncReduceAsset() {
        isSyncReduceAsset = true;
    }

    private void disableSyncReduceAsset() {
        isSyncReduceAsset = false;
    }

    private void enableSyncCK() {
        isSyncCK = true;
    }

    private void disableSyncCK() {
        isSyncCK = false;
    }

    private void enableUpdateAssetsalvage() {
        isSyncCK = true;
    }

    private void disableUpdateAssetsalvage() {
        isSyncCK = false;
    }
    /***
     *
     * @return 获取当前同步状态
     */
    @Override
    public String getSyncStatus() {
        return "[syncCom]" + isSyncCom
                + "[syncDept]" + isSyncDept
                + "[syncUser]" + isSyncUser
                + "[syncSavePlace]" + isSyncSavePlace
                + "[syncTypeCode]" + isSyncTypeCode
                + "[syncAssetCode]" + isSyncAssetCode
                + "[syncAddAsset]" + isSyncAddAsset
                + "[syncReduceAsset]" + isSyncReduceAsset
                + "[updateAssetsalvage]" + isUpdateAssetsalvage
                + "[SyncCK]" + isSyncCK;
    }

    /***
     * 测试资产新增
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void syncTestAsset() {
////		autoTaskService.sendAssets();
////		autoTaskService.updateAssetsalvage();
////		autoTaskService.syncAddAsset();
////        autoTaskService.syncReduceAsset();
////		baseService.syncCom();
////		baseService.syncDept();
////		baseService.SyncUser();
////		baseService.syncCK();
////    	autoTaskService.syncLocation();
////    	autoTaskService.syncMaterialCode();
//    	autoTaskService.syncDeviceCode();
//    }

    /***
     * 每日零点定时同步
     */
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void enableEveryDay() {
        logger.info("System Synchronize Com Dept Post User");
        this.enableSyncCom();
        this.enableSyncDept();
        this.enableSyncUser();
        this.enableSyncSavePlace();
        this.enableSyncTypeCode();
        this.enableSyncAssetCode();
        this.enableSyncAddAsset();
        this.enableSyncReduceAsset();
        this.enableUpdateAssetsalvage();
        this.enableSyncCK();
    }

    /***
     * 禁用所有同步
     */
    @Override
    public void disableAll() {
        this.disableSyncCom();
        this.disableSyncDept();
        this.disableSyncUser();
        this.disableSyncSavePlace();
        this.disableSyncTypeCode();
        this.disableSyncAssetCode();
        this.disableSyncAddAsset();
        this.disableSyncReduceAsset();
        this.disableUpdateAssetsalvage();
        this.disableSyncCK();
    }

    /***
     * 每30秒扫描一次 检查同步
     */
    @Scheduled(fixedDelay = 30000)
    public void run() {
        logger.info(this.getSyncStatus());
        if (isSyncCom) {
            logger.info("Start Sync Com:");
            baseService.syncCom();
            this.disableSyncCom();
        } else if (isSyncDept) {
            logger.info("Start Sync Dept:");
            baseService.syncDept();
            this.disableSyncDept();
        } else if (isSyncUser) {
            logger.info("Start Sync User:");
            baseService.SyncUser();
            this.disableSyncUser();
        } else if (isSyncSavePlace) {
            logger.info("Start Sync Save Place");
            autoTaskService.syncLocation();
            this.disableSyncSavePlace();
        } else if (isSyncAssetCode) {
            logger.info("Start Sync Asset Code");
            autoTaskService.syncMaterialCode();
            this.disableSyncAssetCode();
        } else if (isSyncTypeCode) {
            logger.info("Start Sync Type Code");
            autoTaskService.syncDeviceCode();
            this.disableSyncTypeCode();
        } else if (isSyncAddAsset) {
            logger.info("Start Sync Add Asset");
            autoTaskService.syncAddAsset();
            this.disableSyncAddAsset();
        } else if (isSyncReduceAsset) {
            logger.info("Start Sync Reduce Asset");
            autoTaskService.syncReduceAsset();
            this.disableSyncReduceAsset();
        } else if (isUpdateAssetsalvage) {
            logger.info("Start UpdateAssetsalvage");
            autoTaskService.updateAssetsalvage();
            this.disableUpdateAssetsalvage();
        } else if (isSyncCK) {
            logger.info("Start Sync CK:");
            baseService.syncCK();
            this.disableSyncCK();
        }

    }
}