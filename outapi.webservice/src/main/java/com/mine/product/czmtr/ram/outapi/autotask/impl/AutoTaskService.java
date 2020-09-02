package com.mine.product.czmtr.ram.outapi.autotask.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.dto.DictTypeDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetReduceTempDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeSyncBean;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.service.IAssetReduceService;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.base.dto.FaCardViewDto;
import com.mine.product.czmtr.ram.base.jdbc.FaCardViewDao;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.mine.product.czmtr.ram.outapi.autotask.AutoTask;
import com.mine.product.czmtr.ram.outapi.dto.DeviceCodeLvSyncBean;
import com.mine.product.czmtr.ram.outapi.dto.LocationSyncBean;
import com.mine.product.czmtr.ram.outapi.taskclient.asset.AssetMDImplService;
import com.mine.product.czmtr.ram.outapi.taskclient.changeAsset.NCDrIxml;
import com.mine.product.czmtr.ram.outapi.taskclient.location.LocationMDImplService;
import com.mine.product.czmtr.ram.outapi.taskclient.materials.MaterialsMasterDataImplService;
import com.mine.product.czmtr.ram.outapi.taskclient.osbData.OsbData_Service;
import com.vgtech.platform.common.utility.VGUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional(propagation = Propagation.REQUIRED)
public class AutoTaskService {
    private static final Logger logger = LoggerFactory.getLogger(AutoTaskService.class);

    @Autowired
    private IBaseService baseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IAssetReduceService assetReduceService;
    @Autowired
    private AutoTask autoTask;
    @Autowired
    private FaCardViewDao faCardViewDao;
    @Autowired
    private AssetAssetDao assetDao; //资产

//    String urlApi = "http://10.0.55.10:8888/service/XChangeServlet?account=CS";
//    String dbUrl = "jdbc:oracle:thin:@10.0.55.10:1521:orcl";
//    String dbUserName = "dr123";
//    String dbPassword = "123";

    static final String urlApi = ResourceBundle.getBundle("config.serviceIp").getString("urlApi").trim();
    static final String dbUrl = ResourceBundle.getBundle("config.serviceIp").getString("dbUrl").trim();
    static final String dbUserName = ResourceBundle.getBundle("config.serviceIp").getString("dbUserName").trim();
    static final String dbPassword = ResourceBundle.getBundle("config.serviceIp").getString("dbPassword").trim();

    /**
     * 同步物资编码
     */
    public void syncMaterialCode() {
        logger.info("Sync Material Code");
        int coundPage = 0;
        long startTime = Calendar.getInstance().getTimeInMillis();
        Date date = Calendar.getInstance().getTime();
        JSONObject jsonObj = new JSONObject();
        MaterialsMasterDataImplService service = new MaterialsMasterDataImplService();
        List<MaterialCodeSyncBean> materialCodeBeanList = new ArrayList<MaterialCodeSyncBean>();

        //获取资产编码数据
        while (true) {
            logger.info("CoundPage:" + coundPage);
            jsonObj = JSON.parseObject(service.getMaterialsMasterDataImplPort().getMaterials("", Integer.toString(coundPage++)));
            logger.info("Data:" + jsonObj.get("data"));
            if ("[]".equals(jsonObj.get("data").toString())) {
                break;
            } else {
                materialCodeBeanList.addAll(JSON.parseObject(jsonObj.get("data").toString(), new TypeReference<ArrayList<MaterialCodeSyncBean>>() {
                }));
            }
        }

        String typeId = dictService.getCommonCodeType("{$and:[{key: 'ASSET_TYPE'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData().get(0).getId();
        for (MaterialCodeSyncBean syncBean : materialCodeBeanList) {
            boolean isCreate = false;
            DictDto dictDto = (DictDto) baseService.getDictDtoByTypeAndCode("ASSET_TYPE", syncBean.getCode());
            if (VGUtility.isEmpty(dictDto.getId())) {
                dictDto = new DictDto();
                isCreate = true;
            }

            dictDto.setTypeId(typeId);
            dictDto.setCode(syncBean.getCode());
            dictDto.setChsName(syncBean.getName());

            Map<String, Object> map = new HashMap<String, Object>();
            if (!VGUtility.isEmpty(dictDto.getPropertyMap())) {
                map = dictDto.getPropertyMap();
            }

            if ("consume".equals(syncBean.getW_PRO_CODE())) {
                logger.info(syncBean.getCode() + "Continue");
                continue;
            }

            map.put("W_PRO_CODE", this.convertW_PRO_CODEtoChsName(syncBean.getW_PRO_CODE()));
            map.put("W_TYPE_CODE", this.convertW_TYPE_CODEtoChsName(syncBean.getW_TYPE_CODE()));
            map.put("PRICE", syncBean.getPRICE());
            map.put("MARTERIALS_SPEC", syncBean.getMARTERIALS_SPEC());
            map.put("W_UNIT_CODE", syncBean.getW_UNIT_CODE());
            map.put("W_IS_PRO", syncBean.getW_IS_PRO());
            map.put("IS_DAN", syncBean.getIS_DAN());
            map.put("IS_DIRECT", syncBean.getIS_DIRECT());
            map.put("MARTERIALS_STATE", syncBean.getMARTERIALS_STATE());
            map.put("BRAND_NAME", syncBean.getBRAND_NAME());
            map.put("EXPIRATION_DATE", syncBean.getEXPIRATION_DATE());
            map.put("IS_DEL", syncBean.getIS_DEL());
            map.put("lastUpdateTime", VGUtility.toDateStr(date, "yyyy-MM-dd HH:mm:ss"));

            dictDto.setPropertyMap(map);

            try {
                if (isCreate) {
                    dictService.addCommonCode(dictDto);
                    logger.info(syncBean.getCode() + "Create Success");
                } else {
                    dictService.updateCommonCode(dictDto);
                    logger.info(syncBean.getCode() + "Update Success");
                }
            } catch (Exception e) {
                if (isCreate) {
                    logger.info(syncBean.getCode() + "Create Failed");
                } else {
                    logger.info(syncBean.getCode() + "Update Failed");
                }
            }
        }

        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }

    private String convertW_PRO_CODEtoChsName(String W_PRO_CODE) {
        String resultStr = new String();
        switch (W_PRO_CODE) {
            case "fix":
                resultStr = "固定资产";
                break;
            case "belong":
                resultStr = "列管物资";
                break;
            case "consume":
                resultStr = "消耗物资";
                break;
            default:
                logger.info("Convert W_PRO_CODE Unknow [" + W_PRO_CODE + "]");
                break;
        }
        return resultStr;
    }

    private String convertW_TYPE_CODEtoChsName(String W_TYPE_CODE) {
        String resultStr = new String();
        switch (W_TYPE_CODE) {
            case "fix":
                resultStr = "固定资产";
                break;
            case "officeequipment":
                resultStr = "办公用品";
                break;
            case "labor":
                resultStr = "劳保用品";
                break;
            case "calculate":
                resultStr = "计量用具";
                break;
            case "tools":
                resultStr = "工器具";
                break;
            case "emergency":
                resultStr = "应急物资";
                break;
            case "sparepart":
                resultStr = "备品备件";
                break;
            case "consume":
                resultStr = "消耗性材料";
                break;
            case "officesupplies":
                resultStr = "办公用品";
                break;
            case "others":
                resultStr = "其他";
            default:
                logger.info("Convert W_TYPE_CODE Unknow [" + W_TYPE_CODE + "]");
                break;
        }
        return resultStr;
    }

    /*****
     * 同步资产分类编码 (设备分类编码)
     */
    public void syncDeviceCode() {
        logger.info("Sync Asset Code Type");
        int coundPage = 0;
        long startTime = Calendar.getInstance().getTimeInMillis();
        String assetLevel = new String();
        Date date = Calendar.getInstance().getTime();
        JSONObject jsonObj = new JSONObject();
        AssetMDImplService service = new AssetMDImplService(); //wsdl
        List<DeviceCodeLvSyncBean> deviceCodeBeanList = new ArrayList<DeviceCodeLvSyncBean>();
        Map<String, String> dictTypeDtoMap = new HashMap<String, String>();
        Map<String, List<DeviceCodeLvSyncBean>> deviceCodeBeanMap = new HashMap<String, List<DeviceCodeLvSyncBean>>();

        //循环获取资产分类数据 每次1000条
        while (true) {
            logger.info("CoundPage:" + coundPage);
            jsonObj = JSON.parseObject(service.getAssetMDImplPort().getAssets("", Integer.toString(coundPage++)));
            logger.info("Data:" + jsonObj.get("data"));
            if ("[]".equals(jsonObj.get("data").toString())) {
                break;
            } else {
                deviceCodeBeanList.addAll(JSON.parseObject(jsonObj.get("data").toString(), new TypeReference<ArrayList<DeviceCodeLvSyncBean>>() {
                }));
            }
        }

        //整理填充Map
        for (DeviceCodeLvSyncBean deviceCodeBean : deviceCodeBeanList) {
            assetLevel = deviceCodeBean.getASSETS_LEVEL();
            if ("0".equals(assetLevel)) {
                continue;
            } //越过0级

            List<DeviceCodeLvSyncBean> tempBeanList = deviceCodeBeanMap.get(assetLevel);
            if (VGUtility.isEmpty(tempBeanList)) {
                tempBeanList = new ArrayList<DeviceCodeLvSyncBean>();
            }

            tempBeanList.add(deviceCodeBean);
            deviceCodeBeanMap.put(assetLevel, tempBeanList);
        }

        //遍历map插入数据
        for (int i = 1; i <= 4; i++) {
            assetLevel = "DEVICE_CODE_LV" + Integer.toString(i);
            for (DeviceCodeLvSyncBean deviceCodeBean : deviceCodeBeanMap.get(Integer.toString(i))) {
                boolean isCreate = false;
                DictDto dictDto = (DictDto) baseService.getDictDtoByTypeAndCode("DEVICE_CODE_LV" + deviceCodeBean.getASSETS_LEVEL(), deviceCodeBean.getCODE());
                if (VGUtility.isEmpty(dictDto.getId())) {
                    dictDto = new DictDto();
                    isCreate = true;
                }

                if (VGUtility.isEmpty(dictTypeDtoMap.get(assetLevel))) {
                    List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{$and:[{key: '" + assetLevel + "'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData();
                    dictTypeDtoMap.put(assetLevel, dictTypeDtoList.get(0).getId());
                }

                dictDto.setTypeId(dictTypeDtoMap.get(assetLevel));
                dictDto.setCode(deviceCodeBean.getCODE());
                dictDto.setChsName(deviceCodeBean.getNAME());

                Map<String, Object> propertyMap = dictDto.getPropertyMap();
                if (VGUtility.isEmpty(propertyMap)) {
                    propertyMap = new HashMap<String, Object>();
                }


                if (i != 1) {
                    //填充父级id
                    DictDto parentDictDto = (DictDto) baseService.getDictDtoByTypeAndCode("DEVICE_CODE_LV" + Integer.toString(i - 1), deviceCodeBean.getPARENT_CODE());
                    if (!VGUtility.isEmpty(parentDictDto)) {
                        dictDto.setParentDictId(parentDictDto.getId());
                    }
                    //填充掩码converCode
                    switch (i) {
                        case 2:
                            propertyMap.put("converCode", deviceCodeBean.getCODE().substring(2));
                            break;
                        case 3:
                            propertyMap.put("converCode", deviceCodeBean.getCODE().substring(4));
                            break;
                        case 4:
                            propertyMap.put("converCode", deviceCodeBean.getCODE().substring(6));
                            break;
                        default:
                            break;
                    }
                }

                propertyMap.put("lastUpdateTime", VGUtility.toDateStr(date, "yyyy-MM-dd HH:mm:ss"));
                dictDto.setPropertyMap(propertyMap);

                try {
                    if (isCreate) {
                        dictService.addCommonCode(dictDto);
                        logger.info(deviceCodeBean.getCODE() + "Create Success");
                    } else {
                        dictService.updateCommonCode(dictDto);
                        logger.info(deviceCodeBean.getCODE() + "Update Success");
                    }
                } catch (Exception e) {
                    if (isCreate) {
                        logger.info(deviceCodeBean.getCODE() + "Create Failed");
                    } else {
                        logger.info(deviceCodeBean.getCODE() + "Update Failed");
                    }
                }
            }
        }

        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }

    /**
     * 同步位置信息
     */
    public void syncLocation() {
        logger.info("Sync Location Type ");
        int coundPage = 0;
        long startTime = Calendar.getInstance().getTimeInMillis();
        String locaLevel = new String();
        Date date = Calendar.getInstance().getTime();
        JSONObject jsonObj = new JSONObject();
        LocationMDImplService service = new LocationMDImplService();
        List<LocationSyncBean> locaSyncBeanList = new ArrayList<LocationSyncBean>();
        Map<String, String> dictTypeDtoMap = new HashMap<String, String>();
        Map<String, List<LocationSyncBean>> locaSyncBeanMap = new HashMap<String, List<LocationSyncBean>>();

        //循环获取资产分类数据 每次1000条
        while (true) {
            logger.info("CoundPage:" + coundPage);
            jsonObj = JSON.parseObject(service.getLocationMDImplPort().getLocations("", Integer.toString(coundPage++)));
            logger.info("Data:" + jsonObj.get("data"));
            if ("[]".equals(jsonObj.get("data").toString())) {
                break;
            } else {
                locaSyncBeanList.addAll(JSON.parseObject(jsonObj.get("data").toString(), new TypeReference<ArrayList<LocationSyncBean>>() {
                }));
            }
        }

        //整理填充Map
        for (LocationSyncBean locaSyncBean : locaSyncBeanList) {
            if (locaSyncBean.getLOCATION_STATUS() == "0") {
                continue;
            } //越过停用

            locaLevel = locaSyncBean.getLOCATION_LEVEL();
            List<LocationSyncBean> tempBeanList = locaSyncBeanMap.get(locaLevel);
            if (VGUtility.isEmpty(tempBeanList)) {
                tempBeanList = new ArrayList<LocationSyncBean>();
            }

            tempBeanList.add(locaSyncBean);
            locaSyncBeanMap.put(locaLevel, tempBeanList);
        }

        //遍历map插入数据 安装位置1-4
        for (int i = 1; i <= 4; i++) {
            locaLevel = "PLACE_CODE_LV" + Integer.toString(i);
            for (LocationSyncBean locaSyncBean : locaSyncBeanMap.get(Integer.toString(i))) {
                boolean isCreate = false;
                DictDto dictDto = (DictDto) baseService.getDictDtoByTypeAndCode(locaLevel, locaSyncBean.getLOCATION_CODE());
                if (VGUtility.isEmpty(dictDto.getId())) {
                    dictDto = new DictDto();
                    isCreate = true;
                }

                if (VGUtility.isEmpty(dictTypeDtoMap.get(locaLevel))) {
                    List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{$and:[{key: '" + locaLevel + "'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData();
                    dictTypeDtoMap.put(locaLevel, dictTypeDtoList.get(0).getId());
                }

                dictDto.setTypeId(dictTypeDtoMap.get(locaLevel));
                dictDto.setCode(locaSyncBean.getLOCATION_CODE());
                dictDto.setChsName(locaSyncBean.getLOCATION_DESC());

                Map<String, Object> propertyMap = dictDto.getPropertyMap();
                if (VGUtility.isEmpty(propertyMap)) {
                    propertyMap = new HashMap<String, Object>();
                }

                if (i != 1) {
                    //填充父级id
                    DictDto parentDictDto = (DictDto) baseService.getDictDtoByTypeAndCode("PLACE_CODE_LV" + Integer.toString(i - 1), locaSyncBean.getPARENTLOCATION_CODE());
                    if (!VGUtility.isEmpty(parentDictDto)) {
                        dictDto.setParentDictId(parentDictDto.getId());
                    }
                    //填充掩码converCode
                    switch (i) {
                        case 2:
                            propertyMap.put("converCode", locaSyncBean.getLOCATION_CODE().substring(2));
                            break;
                        case 3:
                            propertyMap.put("converCode", locaSyncBean.getLOCATION_CODE().substring(6));
                            break;
                        case 4:
                            propertyMap.put("converCode", locaSyncBean.getLOCATION_CODE().substring(8));
                            break;
                        default:
                            break;
                    }
                }

                propertyMap.put("lastUpdateTime", VGUtility.toDateStr(date, "yyyy-MM-dd HH:mm:ss"));
                dictDto.setPropertyMap(propertyMap);

                try {
                    if (isCreate) {
                        dictService.addCommonCode(dictDto);
                        logger.info(locaSyncBean.getLOCATION_CODE() + "Create Success");
                    } else {
                        dictService.updateCommonCode(dictDto);
                        logger.info(locaSyncBean.getLOCATION_CODE() + "Update Success");
                    }
                } catch (Exception e) {
                    if (isCreate) {
                        logger.info(locaSyncBean.getLOCATION_CODE() + "Create Failed");
                    } else {
                        logger.info(locaSyncBean.getLOCATION_CODE() + "Update Failed");
                    }
                }
            }
        }

        for (LocationSyncBean locaSyncBean : locaSyncBeanList) {
            boolean isCreate = false;
            DictDto dictDto = (DictDto) baseService.getDictDtoByTypeAndCode("SAVE_PLACE", locaSyncBean.getLOCATION_CODE());
            if (VGUtility.isEmpty(dictDto.getId())) {
                dictDto = new DictDto();
                isCreate = true;
            }

            List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{$and:[{key: 'SAVE_PLACE'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData();
            dictDto.setTypeId(dictTypeDtoList.get(0).getId());
            dictDto.setCode(locaSyncBean.getLOCATION_CODE());
            dictDto.setChsName(locaSyncBean.getLOCATION_DESC());

            Map<String, Object> propertyMap = dictDto.getPropertyMap();
            if (VGUtility.isEmpty(propertyMap)) {
                propertyMap = new HashMap<String, Object>();
            }

            propertyMap.put("lastUpdateTime", VGUtility.toDateStr(date, "yyyy-MM-dd HH:mm:ss"));
            dictDto.setPropertyMap(propertyMap);

            try {
                if (isCreate) {
                    dictService.addCommonCode(dictDto);
                    logger.info(locaSyncBean.getLOCATION_CODE() + "Create Success");
                } else {
                    dictService.updateCommonCode(dictDto);
                    logger.info(locaSyncBean.getLOCATION_CODE() + "Update Success");
                }
            } catch (Exception e) {
                if (isCreate) {
                    logger.info(locaSyncBean.getLOCATION_CODE() + "Create Failed");
                } else {
                    logger.info(locaSyncBean.getLOCATION_CODE() + "Update Failed");
                }
            }
        }

        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }


    /***
     * 资产新增同步至NC(财务系统)
     */
    public void syncAddAsset() {
        logger.info("Sync Add Asset");
        List<AssetAssetDto> list = assetService.getNoSyncAddAsset();
        long startTime = Calendar.getInstance().getTimeInMillis();
        Date date = new Date();
        NCDrIxml ncDrIxml = new NCDrIxml();
        List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{sysMark:'RAM_WEB'}", null, null).getRowData();
        for (AssetAssetDto dto : list) {
            //同步新增资产
            StringBuffer xml = new StringBuffer();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            xml.append("<ufinterface roottag=\"card\" billtype=\"h1\" subtype=\"\" treplace=\"Y\" receiver=\"0101\" sender=\"dr\" isexchange=\"Y\" proc=\"add\" tfilename=\"固定资产凭证.xml\">");
            //资产编码
            xml.append("<card id=\"" + dto.getAssetCode() + "\">");
            xml.append("<card_head>");
            //资产所属公司编码
//			if(!VGUtility.isEmpty(dto.getCompanyCode())){
//				DeptInfoDto dept = (DeptInfoDto) deptInfoDtoList.stream().filter(obj->obj.getDeptCode().equals(dto.getCompanyCode())).findAny().orElse(null);
//				if(!VGUtility.isEmpty(dept)) {
//					xml.append("<company>"+dept.getPropertyMap().get("CK_NO")+"</company>");
//				} else {
            xml.append("<company>0101</company>");
//				}
//			}
            //资产编码
            xml.append("<asset_code>" + dto.getAssetCode() + "</asset_code>");
            //资产名称(物资名称)
            xml.append("<asset_name>" + dto.getCombinationAssetName() + "</asset_name>");
            //固定资产创建日期
            Date createTime = VGUtility.toDateObj(dto.getCreateTimestamp(), "yyyy-MM-dd");
            xml.append("<begindate>" + VGUtility.toDateStr(createTime, "yyyy-MM-dd") + "</begindate>");
            xml.append("<fk_currency>CNY</fk_currency>");
            xml.append("<currunit>元</currunit>");
            xml.append("<recorder>clcs</recorder>");
            //数据推送日期
            xml.append("<recorddate>" + VGUtility.toDateStr(date, "yyyy-MM-dd") + "</recorddate>");
            //资产使用人编码(不必填)
//			xml.append("<assetuser>"+dto.getUserCode()+"</assetuser>");
            //规格型号
            xml.append("<spec>" + dto.getSpecAndModels() + "</spec>");
            //安装位置
            xml.append("<position>" + dto.getSavePlaceStr() + "</position>");
            xml.append("<workloanunit></workloanunit>");
            String fk_addreducestyle = "";
            switch (dto.getAssetType()) {
                case "0"://建设移交
                    fk_addreducestyle = "0106";
                    break;
                case "1"://采购
                    fk_addreducestyle = "0101";
                    break;
                case "2"://投资
                    fk_addreducestyle = "0102";
                    break;
                case "3"://捐赠
                    fk_addreducestyle = "0103";
                    break;
                case "4"://盘盈
                    fk_addreducestyle = "0104";
                    break;
            }
            //增加方式
            xml.append("<fk_addreducestyle>" + fk_addreducestyle + "</fk_addreducestyle>");
            xml.append("<assetsuit_code></assetsuit_code>");
            //使用部门编码
            if (!VGUtility.isEmpty(dto.getUseDeptCode())) {
                DeptInfoDto dept = (DeptInfoDto) deptInfoDtoList.stream().filter(obj -> obj.getDeptCode().equals(dto.getUseDeptCode())).findAny().orElse(null);
                if (!VGUtility.isEmpty(dept)) {
                    xml.append("<fk_usedept>" + dept.getPropertyMap().get("CK_NO") + "</fk_usedept>");
                } else {
                    xml.append("<fk_usedept></fk_usedept>");
                }
            }
            //管理部门编码
            if (!VGUtility.isEmpty(dto.getManageDeptCode())) {
                DeptInfoDto dept = (DeptInfoDto) deptInfoDtoList.stream().filter(obj -> obj.getDeptCode().equals(dto.getManageDeptCode())).findAny().orElse(null);
                if (!VGUtility.isEmpty(dept)) {
                    xml.append("<fk_mandept>" + dept.getPropertyMap().get("CK_NO") + "</fk_mandept>");
                } else {
                    xml.append("<fk_mandept></fk_mandept>");
                }
            }
            xml.append("<paydept_flag>0</paydept_flag>");
            //推送时间年份
            xml.append("<accyear>" + VGUtility.toDateStr(date, "yyyy") + "</accyear>");
            //推送时间的月份
            xml.append("<period>" + VGUtility.toDateStr(date, "MM") + "</period>");
            //资产原值
            xml.append("<originvalue>" + dto.getEquiOrigValue() + "</originvalue>");
            //资产原值
            xml.append("<localoriginvalue>" + dto.getEquiOrigValue() + "</localoriginvalue>");
            xml.append("<fracoriginvalue></fracoriginvalue>");
            xml.append("<allworkloan></allworkloan>");
            xml.append("<servicemonth>120</servicemonth>");
            xml.append("<accudep>0</accudep>");
            xml.append("<accuworkloan></accuworkloan>");
            xml.append("<predevaluate></predevaluate>");
            xml.append("<usedmonth>0</usedmonth>");
            xml.append("<monthworkloan>0</monthworkloan>");
            xml.append("<depunit></depunit>");
            xml.append("<salvagerate>5</salvagerate>");
            //计算方式为原值乘以净残值率，不做舍位处理
            xml.append("<salvage>" + VGUtility.toDouble(dto.getEquiOrigValue()) / 5 + "</salvage>");
            xml.append("<newasset_flag>1</newasset_flag>");
            //资产分类编码
            xml.append("<fk_category>" + dto.getMaterialCode() + "</fk_category>");
            xml.append("<fk_usingstatus>01</fk_usingstatus>");
            xml.append("<fk_depmethod>02</fk_depmethod>");
            xml.append("<fk_jobmngfil></fk_jobmngfil>");
            xml.append("<fk_provider></fk_provider>");
            xml.append("<source>7</source>");
            xml.append("</card_head>");
            xml.append("<card_body>");
            xml.append("<entry>");
            xml.append("<def_quote>记账日期</def_quote>");
            xml.append("<def_value></def_value>");
            xml.append("</entry>");
            xml.append("</card_body>");
            xml.append("</card>");
            xml.append("</ufinterface>");
            logger.info("推送资产:" + xml.toString());
            String result = ncDrIxml.getNCDrIxmlSOAP11PortHttp().login(urlApi, xml.toString());
            if (result.split(":")[0].equals("1")) {
                dto.setIsSync(true);
                assetService.updateAsset(dto);
            }
            logger.info("推送新增资产至财务系统:" + result);
        }
        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }

    /***
     * 资产减损同步至NC(财务系统)
     */
    public void syncReduceAsset() {
        logger.info("Sync Material Code");
        List<AssetReduceTempDto> list = assetReduceService.getNoSysnReduceAsset();
        long startTime = Calendar.getInstance().getTimeInMillis();
        Date date = new Date();
        NCDrIxml ncDrIxml = new NCDrIxml();
        List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{sysMark:'RAM_WEB'}", null, null).getRowData();
        for (AssetReduceTempDto dto : list) {
            //同步减损资产
            StringBuffer xml = new StringBuffer();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            xml.append("<ufinterface roottag=\"bill\" billtype=\"h2\" subtype=\"\" replace=\"Y\" receiver=\"0101\" sender=\"dr\" isexchange=\"Y\" proc=\"add\" filename=\"reduce_card.xml\">");
            //资产编码
            xml.append("<bill id=\"" + dto.getAssetCode() + "\">");
            xml.append("<bill_head>");
            xml.append("<source>7</source>");
            //资产所属公司编码
//			if(!VGUtility.isEmpty(dto.getCompanyCode())){
//				DeptInfoDto dept = deptInfoDtoList.stream().filter(obj->obj.getDeptCode().equals(dto.getCompanyCode())).findAny().orElse(null);;
//				if(!VGUtility.isEmpty(dept)) {
//					xml.append("<company>"+dept.getPropertyMap().get("CK_NO")+"</company>");
//				} else {
            xml.append("<company>0101</company>");
//				}
//			}
            //资产减损单号
            xml.append("<bill_code>" + dto.getChangeNum() + "</bill_code>");
            //资产编码
            xml.append("<asset_code>" + dto.getAssetCode() + "</asset_code>");
            xml.append("<recorder>clcs</recorder>");
            //数据推送日期
            xml.append("<recorddate>" + VGUtility.toDateStr(date, "yyyy-MM-dd") + "</recorddate>");
            String fk_addreducestyle = "";
            switch (dto.getReduceType().ordinal()) {
                case 0://盘亏
                    fk_addreducestyle = "0202";
                    break;
                case 1://捐赠
                    fk_addreducestyle = "0205";
                    break;
                case 2://报废
                    fk_addreducestyle = "0206";
                    break;
                case 3://丢失(其他)
                    fk_addreducestyle = "0211";
                    break;
            }
            //减少方式编码
            xml.append("<fk_addreducestyle>" + fk_addreducestyle + "</fk_addreducestyle>");
            xml.append("<income>0</income>");
            xml.append("<expense>0</expense>");
            //减少原因
            xml.append("<reason>" + dto.getReason() + "</reason>");
            xml.append("<edit></edit>");
            xml.append("</bill_head>");
            xml.append("</bill>");
            xml.append("</ufinterface>");
            logger.info("推送资产:" + xml.toString());
            String result = ncDrIxml.getNCDrIxmlSOAP11PortHttp().login(urlApi, xml.toString());
            if (result.split(":")[0].equals("1")) {
                assetReduceService.updateReduceTempSync(dto.getId(), true);
            }
            logger.info("推送减损资产至财务系统:" + result);
        }
        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }

    /**
     * 同步资产价值,同步财务系统提供的视图
     */
    public void updateAssetsalvage() {
        String sql = "SELECT * FROM FA_CARD_V";
        List<FaCardViewDto> list =
                faCardViewDao.queryAllDao("oracle.jdbc.driver.OracleDriver", dbUrl
                        , dbUserName, dbPassword);
        List<AssetAssetModel> modelList = new ArrayList<>();
        for (FaCardViewDto obj : list) {
            if (!VGUtility.isEmpty(assetService.getAssetByAssetCode(obj.getAssetCode()))) {
                System.out.println("更新成功!" + obj);
                AssetAssetModel model = (AssetAssetModel) assetService.getAssetByAssetCode(obj.getAssetCode());
                model.setScrapValue(VGUtility.toDouble(obj.getNetamount()));
                model.setResidualValue(VGUtility.toDouble(obj.getSalvage()));
                model.setMonthDeprMoney(VGUtility.toDouble(obj.getDepamount()));
                model.setEquiOrigValue(VGUtility.toDouble(obj.getLocaloriginvalue()));
                model.setNetWorth(VGUtility.toDouble(obj.getNetamount()));
                modelList.add(model);
            }
        }
        assetDao.saveAll(modelList);
    }

    /**
     * 向北明推送资产主数据
     */
    public void sendAssets() {
        OsbData_Service osbData = new OsbData_Service();
        String json = assetService.getAllSyncAssetBean();//JSON.toJSONString(modelList);
        logger.info(json);
        long startTime = Calendar.getInstance().getTimeInMillis();
        String result = osbData.getOsbDataPort().getData("S00010023", json);
        logger.info("返回结果：" + result + "; Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }

}
