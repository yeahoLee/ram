package com.mine.product.czmtr.ram.asset.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : yeaho_lee
 * @Description : 审批流程信息
 * @createTime : 2020年07月08日 10:47
 */
public class FlowableInfo {

    /**
     * 系统表识
     */
    public static final String SYSMARK = "RAM_WEB";


    /**
     * 固定资产盘点结果流程
     */
    public static final String ASSETS_INVENTORY_RESULT = "ASSETS_INVENTORY_RESULT";

    /**
     * 固定资产盘点任务流程
     */
    public static final String ASSETS_INVENTORY_TASK = "ASSETS_INVENTORY_TASK";

    /**
     * 固定资产调拨流程
     */
    public static final String ASSETS_TRANSFER = "ASSETS_TRANSFER";

    /**
     * 固定资产使用人变更/安装位置变更流程
     */
    public static final String ASSETS_USER_LOCATION_CHANGE = "ASSETS_USER_LOCATION_CHANGE";

    /**
     * 固定资产领用流程
     */
    public static final String ASSETS_USE = "ASSETS_USE";

    /**
     * 领用归还
     */
    public static final String ASSETS_USE_RETURN = "ASSETS_USE_RETURN";
    public static final String ASSETS_USE_RETURN_DEPT = "ASSETS_USE_RETURN_DEPT";
    public static final String ASSETS_USE_RETURN_CENTER = "ASSETS_USE_RETURN_CENTER";

    /**
     * 固定资产封存启封流程
     */
    public static final String FIXED_ASSETS_ARCHIVE_CENTER = "FIXED_ASSETS_ARCHIVE_CENTER";

    /**
     * 固定资产减损流程
     */
    public static final String ASSETS_IMPAIRMENT = "ASSETS_IMPAIRMENT";

    /**
     * 固定资产借用归还流程
     */
    public static final String ASSETS_BORROW_RETURN = "ASSETS_BORROW_RETURN";
    public static final String ASSETS_BORROW_RETURN_DEPT = "ASSETS_BORROW_RETURN_DEPT";
    public static final String ASSETS_BORROW_RETURN_CENTER = "ASSETS_BORROW_RETURN_CENTER";

    /**
     * 固定资产新增流程
     */
    public static final String ASSETS_ADD = "ASSETS_ADD";

    /**
     * 固定资产借用流程
     */
    public static final String ASSETS_BORROW = "ASSETS_BORROW";
    public static final String ASSETS_BORROW_DEPT = "ASSETS_BORROW_DEPT";
    public static final String ASSETS_BORROW_CENTER = "ASSETS_BORROW_CENTER";

    /**
     * 部门资产管理员（审批判断）
     */
    public static final String DEPARTMENT_ADMINISTRATOR = "dept_asset_admin";

    /**
     * 中心资产管理员（审批判断）
     */
    public static final String CENTER_ADMINISTRATOR = "center_asset_admin";

    /**
     * 提交人节点ID
     */
    public static final String FLOW_SUBMITTER_ID = "draft";

    /**
     * 提交人
     */
    public static final String FLOW_SUBMITTER_VAR = "draftUser";

    //中心主任
    public static final String PROCESS_CENTER = "centerDirector";
    //部门部长
    public static final String PROCESS_DEPT = "deptAdmin";
    //部门资产管理员
    public static final String PROCESS_DEPT_ASSET_ADMIN = "deptAssetAdmin";
    //生产性（企业管理部）
    public static final String PROCESS_PRODUCTIVE_ASSET_ADMIN = "productiveAssetAdmin";
    //非生产性（综合管理部）
    public static final String PROCESS_NO_PRODUCTIVE_ASSET_ADMIN = "noProductiveAssetAdmin";
    //财务告知
    public static final String FINANCE_DEPT_MANAGER_2 = "financeDeptManager2";

    //借入中心固定资产管理员
    public static final String BORROW_IN_CENTER_ADMIN = "borrowInCenterAdmin";
    //借入部门资产管理员
    public static final String BORROW_IN_DEPT_ADMIN = "borrowInDeptAdmin";

    //还入中心固定资产管理员
    public static final String RETURNED_CENTER_ADMIN = "returnedCenterAdmin";
    //还入部门资产管理员
    public static final String RETURNED_DEPT_ADMIN = "returnedDeptAdmin";


    public static final Map<String, Object> mapService = new HashMap<String, Object>() {
        {
            //调拨
            put(ASSETS_TRANSFER, "assetAllocationService");
            //借用
            put(ASSETS_BORROW_DEPT, "assetBorrowService");
            put(ASSETS_BORROW_CENTER, "assetBorrowService");
            //借用归还
            put(ASSETS_BORROW_RETURN_DEPT, "assetRevertService");
            put(ASSETS_BORROW_RETURN_CENTER, "assetRevertService");
            //领用
            put(ASSETS_USE, "assetReceiveUseService");
            //领用归还
            put(ASSETS_USE_RETURN_DEPT, "assetReceiveRevertService");
            put(ASSETS_USE_RETURN_CENTER, "assetReceiveRevertService");
            //管理员，使用人，位置变更
            Map<String, String> map  = new HashMap<>();
            /*管理员*/
            map.put("0", "assetAdminChangeService");
            /*使用人*/
            map.put("1", "assetUserChangeService");
            /*安装位置*/
            map.put("2", "assetSavePlaceChangeService");
            put(ASSETS_USER_LOCATION_CHANGE, map);
            //封存/启封
            Map<String, String> map1  = new HashMap<>();
            /*封存*/
            map1.put("0", "assetSealService");
            /*启封*/
            map1.put("1", "assetUnsealService");
            put(FIXED_ASSETS_ARCHIVE_CENTER, map1);
            //新增
            put(ASSETS_ADD, "materialReceiptService");
            //减损
            put(ASSETS_IMPAIRMENT, "assetReduceService");
            //盘点任务
            put(ASSETS_INVENTORY_TASK, "assetInventoryService");
            //盘点结果
            put(ASSETS_INVENTORY_RESULT, "assetInventoryTempService");



        }
    };




    public enum WORKSTATUS {
        拟稿, 审批中, 驳回, 已审批, 作废, 撤回
    }

    public enum GatewayEnum {
        DEPT_ADMIN_LINE("draftNextNode", "deptAdmin"),
        CENTER_DIRECTOR_LINE("draftNextNode", "centerDirector"),
        PRODUCTIVE_ASSET_ADMIN_LINE("deptAdminNextNode", "productiveAssetAdmin"),
        NO_PRODUCTIVE_ASSET_ADMIN_LINE("deptAdminNextNode", "noProductiveAssetAdmin"),

        //借用流程
        BORROW_DEPT_ADMIN_LINE("draftNextNode", "borrowInDeptAdmin"),
        RBORROW_CENTER_ADMIN_LINE("draftNextNode", "borrowInCenterAdmin"),
        BORROW_ASSET_DEPT_ADMIN_LINE("deptAssetAdminNextNode", "borrowInDeptAdmin"),
        RBORROW_ASSET_CENTER_ADMIN_LINE("deptAssetAdminNextNode", "borrowInCenterAdmin"),
        NO_PRODUCTIVE_ASSET_ADMIN_LINE2("borrowInDeptAdminNextNode", "noProductiveAssetAdmin"),
        PRODUCTIVE_ASSET_ADMIN_LINE2("borrowInDeptAdminNextNode", "productiveAssetAdmin"),

        //借用归还
        RETURNED_DEPT_ADMIN_LINE("draftNextNode", "returnedDeptAdmin"),
        RETURNED_CENTER_ADMIN_LINE("draftNextNode", "returnedCenterAdmin"),
        RETURNED_ASSET_DEPT_ADMIN_LINE("deptAssetAdminNextNode", "returnedDeptAdmin"),
        RETURNED_ASSET_CENTER_ADMIN_LINE("deptAssetAdminNextNode", "returnedCenterAdmin"),
        NO_PRODUCTIVE_ASSET_ADMIN_LINE3("returnedDeptAdminNextNode", "noProductiveAssetAdmin"),
        PRODUCTIVE_ASSET_ADMIN_LINE3("returnedDeptAdminNextNode", "productiveAssetAdmin"),

        //领用归还
        NO_PRODUCTIVE_ASSET_ADMIN_LINE4("deptAssetAdminNextNode", "noProductiveAssetAdmin"),
        PRODUCTIVE_ASSET_ADMIN_LINE4("deptAssetAdminNextNode", "productiveAssetAdmin"),
        NO_PRODUCTIVE_DRAFT_LINE("draftNextNode", "noProductiveAssetAdmin"),
        PRODUCTIVE_DRAFT_LINE("draftNextNode", "productiveAssetAdmin")
                ;

        private String key;
        private String value;

        GatewayEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public enum WorkFlowEnum {
        ASSETS_ADD("ASSETS_ADD","固定资产新增流程"),
        ASSETS_TRANSFER("ASSETS_TRANSFER", "固定资产调拨流程"),
        ASSETS_USER_LOCATION_CHANGE("ASSETS_USER_LOCATION_CHANGE", "固定资产位置人员变更流程"),
        ASSETS_USE("ASSETS_USE", "固定资产领用流程"),
        ASSETS_INVENTORY_RESULT("ASSETS_INVENTORY_RESULT", "固定资产盘点结果流程"),
        FIXED_ASSETS_ARCHIVE_CENTER("FIXED_ASSETS_ARCHIVE_CENTER", "固定资产分寸/启封流程"),
        ASSETS_IMPAIRMENT("ASSETS_IMPAIRMENT", "固定资产减损流程"),
        ASSETS_BORROW_RETURN_DEPT("ASSETS_BORROW_RETURN_DEPT", "固定资产借用归还流程（部门）"),
        ASSETS_BORROW_RETURN_CENTER("ASSETS_BORROW_RETURN_CENTER", "固定资产借用归还流程（中心）"),
        ASSETS_BORROW_DEPT("ASSETS_BORROW_DEPT", "固定资产借用流程（部门）"),
        ASSETS_BORROW_CENTER("ASSETS_BORROW_CENTER", "固定资产借用流程（中心）"),
        ASSETS_USE_RETURN_DEPT("ASSETS_USE_RETURN_DEPT", "固定资产领用归还流程（部门）"),
        ASSETS_USE_RETURN_CENTER("ASSETS_USE_RETURN_CENTER", "固定资产领用归还流程（中心）"),
        ASSETS_INVENTORY_TASK("ASSETS_INVENTORY_TASK", "固定资产盘点任务流程")
        ;



        private String key;
        private String value;

        WorkFlowEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 需要指定人员的环节
     */
    public class AssignUserProcess {
        /*公用部分*/
        public static final String centerDirector = "centerDirector";
        public static final String deptAssetAdmin = "deptAssetAdmin";
        public static final String deptAdmin = "deptAdmin";

        /*借用流程*/
        //借入部门管理员
        public static final String borrowInDeptAdmin = "borrowInDeptAdmin";
        //借入中心固定资产管理员审核
        public static final String borrowInCenterAdmin = "borrowInCenterAdmin";

        /*借用归还流程*/
        //还入部门管理员
        public static final String returnedDeptAdmin = "returnedDeptAdmin";
        //还入中心固定资产管理员审核
        public static final String returnedCenterAdmin = "returnedCenterAdmin";
    }





}
