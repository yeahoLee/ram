package com.mine.product.czmtr.ram.asset.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.api.pdf.PDFUtility;
import com.mine.platform.api.pdf.item.PageItem;
import com.mine.platform.api.pdf.item.TableCellItem;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.*;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.*;
import com.mine.product.czmtr.ram.asset.taskclient.CodeBean;
import com.mine.product.czmtr.ram.asset.taskclient.EquipCode;
import com.mine.product.czmtr.ram.asset.taskclient.GetMaterialsDataImplService;
import com.mine.product.czmtr.ram.asset.util.QRcodeImageUtil;
import com.mine.product.czmtr.ram.base.dto.SyncAssetBean;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.imageio.stream.FileImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产管理
 *
 * @author rockh
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetService implements IAssetService {
    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);
    @Autowired
    private AssetAssetDao assetDao; //资产
    @Autowired
    private AssetHistoryDao historyDao; //历史
    @Autowired
    private AssetUploadFileDao uploadFileDao; //附件
    @Autowired
    private AssetDynamicViewDao dynamicViewDao; //视图
    @Autowired
    private MaterialReceiptDao materialReceiptDao;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private IAssetTempService assetTempService;
    @Autowired
    private IMaterialReceiptService materialReceiptService;

    @Override
    public void changeAssetManager(List<String> assetIdList, String assetManagerId) {
        UserInfoDto userInfo = userService.getUserInfo(assetManagerId);
        String managerEmpNo = null;
        if (!VGUtility.isEmpty(userInfo.getPropertyMap())) {
            managerEmpNo = userInfo.getPropertyMap().get("EMP_NO").toString();
        }
        List<DeptInfoDto> deptList = baseService.getDeptDtoListByUserId(userInfo.getId());
        String deptId = null;
        if (deptList.size() > 0) {
            deptId = deptList.get(0).getId();
        }
        for (int i = 0; i < assetIdList.size(); i++) {
            String assetId = assetIdList.get(i);
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setManagerId(assetManagerId);
            assetModel.setManagerEmpNo(managerEmpNo);
            assetModel.setManageDeptId(deptId);
        }
    }

    @Override
    public void changeAssetUser(List<String> assetIdList, String assetUserId) {
        UserInfoDto userInfo = userService.getUserInfo(assetUserId);
        List<DeptInfoDto> deptList = baseService.getDeptDtoListByUserId(userInfo.getId());
        String deptId = null;
        if (deptList.size() > 0) {
            deptId = deptList.get(0).getId();
        }
        for (int i = 0; i < assetIdList.size(); i++) {
            String assetId = assetIdList.get(i);
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setOldUserId(assetModel.getUserId());
            assetModel.setUserId(assetUserId);
            assetModel.setUseDeptId(deptId);
        }

    }

    @Override
    public void changeAssetSavePlace(List<String> assetIdList, String assetSavePlaceId) {
        for (int i = 0; i < assetIdList.size(); i++) {
            String assetId = assetIdList.get(i);
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setOldSavePlaceId(assetModel.getSavePlaceId());
            assetModel.setSavePlaceId(assetSavePlaceId);
        }
    }

    @Override
    public void doAssetAllocation(List<String> assetIdList, String departmentId, String managerId, String savePlaceId) {
        for (int i = 0; i < assetIdList.size(); i++) {
            String assetId = assetIdList.get(i);
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setOldSavePlaceId(assetModel.getSavePlaceId());
            assetModel.setSavePlaceId(savePlaceId);
            assetModel.setManageDeptId(departmentId);
            assetModel.setManagerId(managerId);
        }
    }

    @Override
    public void doAssetUserUpdate(Map<String, String> assetMap, String assetborrowUserID, String assetborrowDepartmentID) {
        for (String assetId : assetMap.keySet()) {
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setUseDeptId(assetborrowDepartmentID);
            assetModel.setUserId(assetborrowUserID);
            String savePlaceId = assetMap.get(assetId);
            if (!VGUtility.isEmpty(savePlaceId) && !savePlaceId.equals(assetModel.getSavePlaceId())) {
                assetModel.setOldSavePlaceId(assetModel.getSavePlaceId());
                assetModel.setSavePlaceId(savePlaceId);
            }
        }
    }

    @Override
    public void doAssetUserDepAndPlaceUpdate(Map<String, String> map, String uerID, String departmentID) {
        for (String assetId : map.keySet()) {
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            String savePlaceId = map.get(assetId);
            if (!VGUtility.isEmpty(savePlaceId) && !savePlaceId.equals(assetModel.getSavePlaceId())) {
                assetModel.setOldSavePlaceId(assetModel.getSavePlaceId());
                assetModel.setSavePlaceId(savePlaceId);
            }
            assetModel.setUseDeptId(departmentID);
            assetModel.setUserId(uerID);
        }
    }

    @Override
    public void doAssetPlaceUpdate(List<String> assetIdList, String sealPlaceId) {
        for (int i = 0; i < assetIdList.size(); i++) {
            String assetId = assetIdList.get(i);
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setOldSavePlaceId(assetModel.getSavePlaceId());
            assetModel.setSavePlaceId(sealPlaceId);
        }
    }

    @Override
    public void approveFailureAssetStateRollback(List<String> assetIdList) {
        for (int i = 0; i < assetIdList.size(); i++) {
            String assetId = assetIdList.get(i);
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setAssetStatus(assetModel.getBeforeChangeAssetStatus());
//            assetModel.setBeforeChangeAssetStatus(ASSET_STATUS.冻结);
        }
    }

    @Override
    public void approveSuccessUpdateAssetState(List<String> assetIdList, ASSET_STATUS status) {
        for (int i = 0; i < assetIdList.size(); i++) {
            String assetId = assetIdList.get(i);
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setAssetStatus(status != null ? status : assetModel.getBeforeChangeAssetStatus());
//            assetModel.setBeforeChangeAssetStatus(ASSET_STATUS.冻结);
        }
    }


    @Override
    public AssetApprove doAssetApproveCheck(List<String> assetIdList, ASSET_STATUS status) {
        AssetApprove approve = new AssetApprove();
        approve.setCanApprove(true);
        for (String assetId : assetIdList) {
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            ASSET_STATUS assetStatus = assetModel.getAssetStatus();
            boolean flag = true;
            //没有目标状态时，资产状态需为非冻结状态
            if (null == status) {
                flag = assetStatus == ASSET_STATUS.冻结 ? false : true;
            } else {
                flag = this.getAssetStatus(assetStatus, status);
            }

            if (!flag) {
                ApprovenAssetDto approvenAssetDto = new ApprovenAssetDto();
                approvenAssetDto.setAssetChsName(assetModel.getAssetChsName()).setAssetCode(assetModel.getAssetCode()).setAssetStatus(assetModel.getAssetStatus());
                approve.setCanApprove(false);
                approve.getUnAssetList().add(approvenAssetDto);
            }
        }

        return approve;
    }


    @Override
    public void LockAssetState(List<String> assetIdList) {
        for (int i = 0; i < assetIdList.size(); i++) {
            String assetId = assetIdList.get(i);
            //冻结资产
            AssetAssetModel assetModel = assetDao.findById(assetId).get();
            assetModel.setBeforeChangeAssetStatus(assetModel.getAssetStatus());
            assetModel.setAssetStatus(ASSET_STATUS.冻结);
            assetDao.save(assetModel);
        }
    }


    //********************资产正式表 Start********************
    //通用检查
//	@Override
//	public void commonCheckAssetDto(AssetAssetDto dto, boolean createCheck) {
    //计量单位
//		if(VGUtility.isEmpty(dto.getUnitOfMeasId())) {
//			throw new RuntimeException("计量单位为必填项，不能为空！");
//		} else {
//			if(createCheck && VGUtility.isEmpty(dictService.getCommonCode(dto.getUnitOfMeasId())))
//				throw new RuntimeException("计量单位格式不正确！");
//		}
    //所属公司
//		if(VGUtility.isEmpty(dto.getCompanyId())) {
//			throw new RuntimeException("所属公司为必填项，不能为空！");
//		} else {
//			if(createCheck && VGUtility.isEmpty(userService.getDeptInfo(dto.getCompanyId())))
//				throw new RuntimeException("所属公司格式不正确！");
//		}
    //所属线路/建筑
//		if(VGUtility.isEmpty(dto.getBelongLine())) {
//			throw new RuntimeException("所属线路为必填项，不能为空！");
//		} else {
//			if(createCheck && VGUtility.isEmpty(dictService.getCommonCode(dto.getBelongLine())))
//				throw new RuntimeException("所属线路格式不正确！");
//		}
//		if(VGUtility.isEmpty(VGUtility.toDateObj(dto.getBuyDate(), "yyyy/M/d")))
//			throw new RuntimeException("购置日期为必填项,不能为空！");
    //主管部门
//		if(VGUtility.isEmpty(dto.getManageDeptId())) {
//			throw new RuntimeException("主管部门为必填项，不能为空！");
//		} else {
//			if(createCheck && VGUtility.isEmpty(userService.getDeptInfo(dto.getCompanyId())))
//				throw new RuntimeException("主管部门格式不正确！");
//		}
//		if(VGUtility.isEmpty(dto.getManagerId())) {
//			throw new RuntimeException("资产管理员为必填项，不能为空！");
//		} else {
//			if(createCheck && VGUtility.isEmpty(userService.getUserInfo(dto.getManagerId()))) 
//				throw new RuntimeException("资产管理员格式不正确！");
//		}
    //检查savePlaceId是否存在
//		if(VGUtility.isEmpty(dto.getSavePlaceId())) {
//			throw new RuntimeException("安装位置为必填项，不能为空！");
//		}else {
//			if(createCheck && VGUtility.isEmpty(dictService.getCommonCode(dto.getSavePlaceId()))) 
//				throw new RuntimeException("安装位置不存在！");
//		}
    //检查金额是否为数字且保留两位小数
//		if(!VGUtility.isEmpty(dto.getPurcPrice()) && !dto.getPurcPrice().matches("[0-9]\\d*\\.?\\d*")) 
//			throw new RuntimeException("金额必须为数字！");
//		if(!VGUtility.isEmpty(dto.getEquiOrigValue()) && !dto.getEquiOrigValue().matches("[0-9]\\d*\\.?\\d*")) 
//			throw new RuntimeException("金额必须为数字！");
//	}

    @Override
    public void commonCheck(AssetAssetDto dto) {
        if (VGUtility.isEmpty(dto.getMaterialCode()))
            throw new RuntimeException("物资编号为必填项，不能为空！");
        if (VGUtility.isEmpty(dto.getCompanyStr()))
            throw new RuntimeException("所属公司为必填项，不能为空！");
        if (VGUtility.isEmpty(dto.getBelongLineStr()))
            throw new RuntimeException("所属线路为必填项，不能为空！");
        if (VGUtility.isEmpty(VGUtility.toDateObj(dto.getBuyDate(), "yyyy/M/d")))
            throw new RuntimeException("购置日期为必填项，不能为空！");
        if (VGUtility.isEmpty(dto.getManageDeptStr()))
            throw new RuntimeException("主管部门为必填项，不能为空！");
        if (VGUtility.isEmpty(dto.getManagerStr()))
            throw new RuntimeException("资产管理员为必填项，不能为空！");
        if (VGUtility.isEmpty(dto.getSavePlaceStr()))
            throw new RuntimeException("安装位置为必填项，不能为空！");
        if (!VGUtility.isEmpty(dto.getPurcPrice()) && !dto.getPurcPrice().matches("[0-9]\\d*\\.?\\d*"))
            throw new RuntimeException("金额必须为数字！");
        if (!VGUtility.isEmpty(dto.getEquiOrigValue()) && !dto.getEquiOrigValue().matches("[0-9]\\d*\\.?\\d*"))
            throw new RuntimeException("金额必须为数字！");
    }

    @Override
    public void commonCheckBySearch(AssetAssetDto dto) {
        if (VGUtility.isEmpty(baseService.getAssetNameByMaterialCode(dto.getMaterialCode()))) {
            throw new RuntimeException("物资编号选择不正确！");
        }

        if (VGUtility.isEmpty(dto.getCompanyId())) {
            throw new RuntimeException("所属公司选择不正确！");
        } else {
            try {
                baseService.getDeptInfo(dto.getCompanyId());
            } catch (Exception e) {
                throw new RuntimeException("所属公司选择不正确！");
            }
        }

        if (VGUtility.isEmpty(dto.getBelongLine())) {
            throw new RuntimeException("所属线路选择不正确！");
        } else {
            try {
                dictService.getCommonCode(dto.getBelongLine());
            } catch (Exception e) {
                throw new RuntimeException("所属线路选择不正确！");
            }
        }

        if (VGUtility.isEmpty(dto.getManageDeptId())) {
            throw new RuntimeException("主管部门选择不正确！");
        } else {
            try {
                baseService.getDeptInfo(dto.getManageDeptId());
            } catch (Exception e) {
                throw new RuntimeException("主管部门选择不正确！");
            }
        }

        if (VGUtility.isEmpty(dto.getManagerId())) {
            throw new RuntimeException("资产管理员选择不正确！");
        } else {
            try {
                userService.getUserInfo(dto.getManagerId());
            } catch (Exception e) {
                throw new RuntimeException("资产管理员选择不正确！");
            }
        }

        if (VGUtility.isEmpty(dto.getSavePlaceId())) {
            throw new RuntimeException("安装位置选择不正确！");
        } else {
            try {
                dictService.getCommonCode(dto.getSavePlaceId());
            } catch (Exception e) {
                throw new RuntimeException("安装位置选择不正确！");
            }
        }
    }

    @Override
    public void createAsset(AssetAssetDto assetDto, UserInfoDto userInfoDto) {
        commonCheck(assetDto);
        commonCheckBySearch(assetDto);

        AssetAssetModel assetModel = convertAssetDtoToModel(assetDto, null);
        assetModel.setAssetStatus(ASSET_STATUS.闲置);
        assetModel.setIsSysn(false);
        assetModel = assetDao.save(assetModel);

        AssetHistoryDto historyDto = new AssetHistoryDto();
        historyDto.setHistoryType(HISTORY_TYPE.新增记录);
        historyDto.setAssetModelId(assetModel.getId());
        historyDto.setCreateUserId(userInfoDto.getId());
        historyDto.setModifyContent("创建资产");
        if (!VGUtility.isEmpty(assetModel.getAssetSource())) {

            historyDto.setAssetSource(assetModel.getAssetSource());
        } else {
            historyDto.setAssetSource("未知");
        }
        if (!VGUtility.isEmpty(assetModel.getSourceType())) {
            historyDto.setSourctType(Integer.toString(assetModel.getSourceType().ordinal()));
        } else {
            historyDto.setSourctType(Integer.toString(SOURCE_TYPE.投资.ordinal()));
        }
        createHistory(historyDto);
		
		/*//单个数据转换
		AssetAssetDto dto = convertAssetModelToDto(assetModel);
		List<AssetAssetDto> list = new ArrayList<>();
		list.add(dto);
		List<AssetAssetDto> dtos= convertForHtml(list);
		AssetAssetDto assetAssetDto = new AssetAssetDto();
		if (!dtos.isEmpty()) {
			assetAssetDto = dtos.get(0);
		}*/
    }

    @Override
    public void updateAsset(AssetAssetDto dto) {
        AssetAssetModel model = assetDao.findById(dto.getId()).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("该数据已被删除！");
        commonCheck(dto);

        AssetAssetModel asset = convertAssetDtoToModel(dto, model);
        asset.setAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(dto.getAssetStatusStr())]);
        assetDao.save(asset);
    }

    /**
     * 更新资产状态
     */
    @Override
    public void updateAssetBeforeAssetStatus(AssetAssetDto dto) {
        AssetAssetModel model = assetDao.findById(dto.getId()).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("该数据已被删除！");

        AssetAssetModel asset = convertAssetDtoToModel(dto, model);
        asset.setAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(dto.getAssetStatusStr())]);
        assetDao.save(asset);
    }

    @Override
    public void batchUpdateAsset(String assetIdListStr, AssetAssetDto assetDto, UserInfoDto userInfoDto) {
        List<String> assetIdList = new ArrayList<String>();

        if (VGUtility.isEmpty(assetIdList))
            throw new RuntimeException("资产列表不能为空！");
        else
            Arrays.stream(assetIdListStr.split(",")).forEach(arr -> assetIdList.add(arr));
        if (!VGUtility.isEmpty(assetDto.getPurcPrice())) {
            if (!assetDto.getPurcPrice().matches("[0-9]\\d*\\.?\\d*")) {
                throw new RuntimeException("金额必须位数字！");
            }
        }
        for (String assetId : assetIdList) {
            AssetAssetModel model = assetDao.findById(assetId).get();
            if (VGUtility.isEmpty(model))
                throw new RuntimeException(assetId + "数据已被删除！");
            //创建修改的历史记录
            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(HISTORY_TYPE.基础信息变更);
            historyDto.setAssetModelId(assetId);
            historyDto.setCreateUserId(userInfoDto.getId());
            //TODO
            String modifyContent = "";
            //规格型号
            if (!VGUtility.isEmpty(assetDto.getSpecAndModels())) {
                if (VGUtility.isEmpty(model.getSpecAndModels())) {
                    modifyContent += ", 规格型号[无 -> " + assetDto.getSpecAndModels() + "]";
                } else if (assetDto.getSpecAndModels() != model.getSpecAndModels()) {
                    modifyContent += ", 规格型号[" + model.getSpecAndModels() + " -> " + assetDto.getSpecAndModels() + "]";
                }
            }
            //序列号
            if (!VGUtility.isEmpty(assetDto.getSeriesNum())) {
                if (VGUtility.isEmpty(model.getSeriesNum())) {
                    modifyContent += ", 序列号[无 -> " + assetDto.getSeriesNum() + "]";
                } else if (!VGUtility.isEmpty(model.getSeriesNum()) && assetDto.getSeriesNum() != model.getSeriesNum()) {
                    modifyContent += ", 序列号[" + model.getSeriesNum() + " -> " + assetDto.getSeriesNum() + "]";
                }
            }
            //计量单位
            if (!VGUtility.isEmpty(assetDto.getUnitOfMeasId())) {
                String modelUnitOfMeasStr = dictService.getCommonCode(model.getUnitOfMeasId()).getChsName();
                String dtoUnitOfMeasStr = dictService.getCommonCode(assetDto.getUnitOfMeasId()).getChsName();
                if (VGUtility.isEmpty(model.getUnitOfMeasId())) {
                    modifyContent += ", 计量单位[无 -> " + dtoUnitOfMeasStr + "]";
                } else if (!VGUtility.isEmpty(model.getUnitOfMeasId()) && assetDto.getUnitOfMeasId() != model.getUnitOfMeasId()) {
                    modifyContent += ", 计量单位[" + modelUnitOfMeasStr + " -> " + dtoUnitOfMeasStr + "]";
                }
            }
            //资产品牌assetBrand
            if (!VGUtility.isEmpty(assetDto.getAssetBrand())) {
                if (VGUtility.isEmpty(model.getAssetBrand())) {
                    modifyContent += ", 资产品牌[无 -> " + assetDto.getAssetBrand() + "]";
                } else if (!VGUtility.isEmpty(model.getAssetBrand()) && assetDto.getAssetBrand() != model.getAssetBrand()) {
                    modifyContent += ", 资产品牌[" + model.getAssetBrand() + " -> " + assetDto.getAssetBrand() + "]";
                }
            }
            //采购价purcPrice
            if (!VGUtility.isEmpty(assetDto.getPurcPrice())) {
                String modelPurcPriceStr = VGUtility.toDoubleStr(model.getPurcPrice(), "0.##");
                String dtoPurcPriceStr = Double.toString(VGUtility.toDouble(assetDto.getPurcPrice()));
                if (VGUtility.isEmpty(model.getPurcPrice())) {
                    modifyContent += ", 采购价[无 -> " + dtoPurcPriceStr + "]";
                } else if (!VGUtility.isEmpty(model.getPurcPrice()) && assetDto.getPurcPrice() != Double.toString(model.getPurcPrice())) {
                    modifyContent += ", 采购价[" + modelPurcPriceStr + " -> " + dtoPurcPriceStr + "]";
                }
            }
            //技术参数techPara
            if (!VGUtility.isEmpty(assetDto.getTechPara())) {
                if (VGUtility.isEmpty(model.getTechPara())) {
                    modifyContent += ", 技术参数[无 -> " + assetDto.getTechPara() + "]";
                } else if (!VGUtility.isEmpty(model.getTechPara()) && assetDto.getTechPara() != model.getTechPara()) {
                    modifyContent += ", 技术参数[" + model.getTechPara() + " -> " + assetDto.getTechPara() + "]";
                }
            }
            //备注remark
            if (!VGUtility.isEmpty(assetDto.getRemark())) {
                if (VGUtility.isEmpty(model.getRemark())) {
                    modifyContent += ", 备注[无 -> " + assetDto.getRemark() + "]";
                } else if (!VGUtility.isEmpty(model.getRemark()) && assetDto.getRemark() != model.getRemark()) {
                    modifyContent += ", 备注[" + model.getRemark() + " -> " + assetDto.getRemark() + "]";
                }
            }
            //购置日期buyDate
            if (!VGUtility.isEmpty(assetDto.getBuyDate())) {
                String dtoDateStr = VGUtility.toDateStr(VGUtility.toDateObj(assetDto.getBuyDate(), "yyyy/M/d"), "yyyy/M/d");
                String modelDateStr = VGUtility.toDateStr(model.getBuyDate(), "yyyy/M/d");
                if (VGUtility.isEmpty(model.getBuyDate())) {
                    modifyContent += ", 购置日期[无 -> " + dtoDateStr + "]";
                } else if (!VGUtility.isEmpty(model.getBuyDate()) && dtoDateStr != modelDateStr) {
                    modifyContent += ", 购置日期[" + modelDateStr + " -> " + dtoDateStr + "]";
                }
            }
            //维保期mainPeriod
            if (!VGUtility.isEmpty(assetDto.getMainPeriod())) {
                String dtoDateStr = VGUtility.toDateStr(VGUtility.toDateObj(assetDto.getMainPeriod(), "yyyy/M/d"), "yyyy/M/d");
                String modelDateStr = VGUtility.toDateStr(model.getMainPeriod(), "yyyy/M/d");
                if (VGUtility.isEmpty(model.getMainPeriod())) {
                    modifyContent += ", 维保期[无 -> " + dtoDateStr + "]";
                } else if (!VGUtility.isEmpty(model.getMainPeriod()) && dtoDateStr != modelDateStr) {
                    modifyContent += ", 维保期[" + modelDateStr + " -> " + dtoDateStr + "]";
                }
            }
            //出厂日期prodTime
            if (!VGUtility.isEmpty(assetDto.getProdTime())) {
                String dtoDateStr = VGUtility.toDateStr(VGUtility.toDateObj(assetDto.getProdTime(), "yyyy/M/d"), "yyyy/M/d");
                String modelDateStr = VGUtility.toDateStr(model.getProdTime(), "yyyy/M/d");
                if (VGUtility.isEmpty(model.getProdTime())) {
                    modifyContent += ", 出厂日期[无 -> " + dtoDateStr + "]";
                } else if (!VGUtility.isEmpty(model.getProdTime()) && dtoDateStr != modelDateStr) {
                    modifyContent += ", 出厂日期[" + modelDateStr + " -> " + dtoDateStr + "]";
                }
            }
            //合同编号contractNum
            if (!VGUtility.isEmpty(assetDto.getContractNum())) {
                if (VGUtility.isEmpty(model.getContractNum())) {
                    modifyContent += ", 合同编号[无 -> " + assetDto.getContractNum() + "]";
                } else if (!VGUtility.isEmpty(model.getContractNum()) && assetDto.getContractNum() != model.getContractNum()) {
                    modifyContent += ", 合同编号[" + model.getContractNum() + " -> " + assetDto.getContractNum() + "]";
                }
            }
            //联系人sourceUser
            if (!VGUtility.isEmpty(assetDto.getSourceUser())) {
                if (VGUtility.isEmpty(model.getSourceUser())) {
                    modifyContent += ", 联系人[无 -> " + assetDto.getSourceUser() + "]";
                } else if (!VGUtility.isEmpty(model.getSourceUser()) && assetDto.getSourceUser() != model.getSourceUser()) {
                    modifyContent += ", 联系人[" + model.getSourceUser() + " -> " + assetDto.getSourceUser() + "]";
                }
            }
            //联系方式sourceContactInfo
            if (!VGUtility.isEmpty(assetDto.getSourceContactInfo())) {
                if (VGUtility.isEmpty(model.getSourceContactInfo())) {
                    modifyContent += ", 联系方式[无 -> " + assetDto.getSourceContactInfo() + "]";
                } else if (!VGUtility.isEmpty(model.getSourceContactInfo()) && assetDto.getSourceContactInfo() != model.getSourceContactInfo()) {
                    modifyContent += ", 联系方式[" + model.getSourceContactInfo() + " -> " + assetDto.getSourceContactInfo() + "]";
                }
            }
            if (!VGUtility.isEmpty(modifyContent)) {
                modifyContent = modifyContent.substring(1);
                historyDto.setModifyContent(modifyContent);
            }

            if (!VGUtility.isEmpty(assetDto.getSpecAndModels()))
                model.setSpecAndModels(assetDto.getSpecAndModels());
            if (!VGUtility.isEmpty(assetDto.getSeriesNum()))
                model.setSeriesNum(assetDto.getSeriesNum());
            if (!VGUtility.isEmpty(assetDto.getUnitOfMeasId()))
                model.setUnitOfMeasId(assetDto.getUnitOfMeasId());
            if (!VGUtility.isEmpty(assetDto.getAssetBrand()))
                model.setAssetBrand(assetDto.getAssetBrand());
            if (!VGUtility.isEmpty(assetDto.getPurcPrice()))
                model.setPurcPrice(VGUtility.toDouble(assetDto.getPurcPrice()));
            if (!VGUtility.isEmpty(assetDto.getTechPara()))
                model.setTechPara(assetDto.getTechPara());
            if (!VGUtility.isEmpty(assetDto.getRemark()))
                model.setRemark(assetDto.getRemark());
            if (!VGUtility.isEmpty(assetDto.getBuyDate()))
                model.setBuyDate(VGUtility.toDateObj(assetDto.getBuyDate(), "yyyy/M/d"));
            if (!VGUtility.isEmpty(assetDto.getContractNum()))
                model.setContractNum(assetDto.getContractNum());
            if (!VGUtility.isEmpty(assetDto.getSourceUser()))
                model.setSourceUser(assetDto.getSourceUser());
            if (!VGUtility.isEmpty(assetDto.getSourceContactInfo()))
                model.setSourceContactInfo(assetDto.getSourceContactInfo());
            if (!VGUtility.isEmpty(assetDto.getProdTime()))
                model.setProdTime(VGUtility.toDateObj(assetDto.getProdTime(), "yyyy/M/d"));
            if (!VGUtility.isEmpty(assetDto.getMainPeriod()))
                model.setMainPeriod(VGUtility.toDateObj(assetDto.getMainPeriod(), "yyyy/M/d"));
            if (!VGUtility.isEmpty(assetDto.getProduceType()))
                model.setProduceType(assetDto.getProduceType());
            assetDao.save(model);
            createHistory(historyDto);
        }
    }

    @Override
    public void deleteAsset(String assetId) {
        //TODO 删除关联数据
        historyDao.deleteByAssetModelId(assetId);
        assetDao.deleteById(assetId);
    }

    @Override
    public AssetAssetDto getAssetByAssetId(String assetId) {
        AssetAssetModel model = new AssetAssetModel();

        try {
            model = assetDao.findById(assetId).get();
        } catch (Exception e) {
            throw new RuntimeException("该资产不存在");
        }

        AssetAssetDto dto = this.convertAssetModelToDto(model);
        return this.convertForHtml(new ArrayList<AssetAssetDto>(Arrays.asList(dto))).get(0);
    }

    @Override
    public AssetAssetModel getAssetByAssetCode(String assetCode) {
        return assetDao.findByAssetCode(assetCode);
    }

    @Override
    public String getAssetNameByAssetId(String assetId) {
        AssetAssetModel model = new AssetAssetModel();
        try {
            model = assetDao.findById(assetId).get();
        } catch (Exception e) {
            throw new RuntimeException("该资产不存在");
        }
        return baseService.getAssetNameByMaterialCode(model.getMaterialCode());
    }

    @Override
    public Map<String, Object> getAssetByQuerys(AssetAssetDto assetDto, String showType) {
        ISearchExpression iSearchExpression = new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];

                Predicate finalPred = null;
                List<Predicate> andList = new ArrayList<>();

                if (!VGUtility.isEmpty(assetDto.getAssetCode()))
                    andList.add(builder.like(root.get("assetCode"), "%" + assetDto.getAssetCode() + "%"));
                if (!VGUtility.isEmpty(assetDto.getAssetStatus())) {
                    if (assetDto.getAssetStatus().contains(",")) {
                        Path<Object> path = root.get("assetStatus");
                        CriteriaBuilder.In<Object> in = builder.in(path);
                        for (String str : assetDto.getAssetStatus().split(",")) {
                            in.value(ASSET_STATUS.values()[VGUtility.toInteger(str)]);
                        }
                        andList.add(in);
                    } else {
                        andList.add(builder.equal(root.get("assetStatus"), ASSET_STATUS.values()[VGUtility.toInteger(assetDto.getAssetStatus())]));
                    }
                }
                //现资产类别是根据资产编码分类的,故前台传递资产类别时,是根据资产编码进行查询的
                if (!VGUtility.isEmpty(assetDto.getAssetType()))
                    andList.add(builder.equal(root.get("materialCode"), assetDto.getAssetType()));


                if (!VGUtility.isEmpty(assetDto.getProduceType())) {
                    andList.add(builder.equal(root.get("produceType"), assetDto.getProduceType()));
                }

                UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
                boolean flag = baseService.getUserIDByRoleCode("admin", userInfoDto.getId());
                Map<String, Object> map = userInfoDto.getPropertyMap();

                if (showType.equals("0")) {
                    //方法一：通过人员组织结构过来-------------start-------------------------
                    //如果当前登陆人有系统管理员的权限,则查询所有资产
            /*if (flag == false) {
                //需判断当前登陆人是否是部门领导,如果是部门领导,则可以查询出当前部门所有资产管理员的资产
                List<UserInfoDto> userlist = baseService.getUserInfoDtoByDeptSetId(userInfoDto.getId());
                if (userlist.size() > 0) {
                    Path<Object> path = root.get("managerId");
                    CriteriaBuilder.In<Object> in = builder.in(path);
                    for (UserInfoDto dto : userlist) {
                        in.value(dto.getId());
                    }
                    andList.add(in);
                } else {
                    andList.add(builder.equal(root.get("managerId"), userInfoDto.getId()));
                }
            }*/

                    //方法三：
                    if (flag == false || "0".equals(map.get("isboss"))) {
                        if (!VGUtility.isEmpty(map.get("depNoCut")) && "0".equals(map.get("iszcgly"))) {
                            String deptCode = map.get("depNoCut").toString();
                            Set<String> depIdList = baseService.getDeptIdListByDeptCode(deptCode);
                            if (depIdList.size() > 0) {
                                Path<Object> path = root.get("manageDeptId");
                                CriteriaBuilder.In<Object> in = builder.in(path);
                                for (String depcode : depIdList) {
                                    in.value(depcode);
                                }
                                andList.add(in);
                            } else {
                                andList.add(builder.equal(root.get("managerId"), userInfoDto.getId()));
                            }
                        } else if ("1".equals(map.get("iszcgly"))) {
                            andList.add(builder.equal(root.get("managerId"), userInfoDto.getId()));
                        }
                    }

                } else {
                    if (!VGUtility.isEmpty(assetDto.getManageDeptId()))
                        andList.add(builder.equal(root.get("manageDeptId"), assetDto.getManageDeptId()));
                    if (!VGUtility.isEmpty(assetDto.getManagerId()))
                        andList.add(builder.equal(root.get("managerId"), assetDto.getManagerId()));
                }

                if (!VGUtility.isEmpty(assetDto.getUseDeptId()))
                    andList.add(builder.equal(root.get("useDeptId"), assetDto.getUseDeptId()));
                if (!VGUtility.isEmpty(assetDto.getUserId()))
                    andList.add(builder.equal(root.get("userId"), assetDto.getUserId()));
                if (!VGUtility.isEmpty(assetDto.getSavePlaceId()))
                    andList.add(builder.equal(root.get("savePlaceId"), assetDto.getSavePlaceId()));
                if (!VGUtility.isEmpty(assetDto.getMaterialCode()))
                    andList.add(builder.like(root.get("materialCode"), "%" + assetDto.getMaterialCode() + "%"));
                if (!VGUtility.isEmpty(assetDto.getDeActiveTimeStr())) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.YEAR, -1);
                    Date y = c.getTime();
                    andList.add(builder.lessThanOrEqualTo(root.get("deActiveTime"), y));
                }

                if (andList.size() > 0)
                    finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

                return finalPred;
            }
        };

	    return this.getAssetByQuerysForDataGrid("0", iSearchExpression, null);
    }
    
    //GetdataGrid
    @Override
    public ModelMap getAssetByQuerysForDataGrid(String isFilt, ISearchExpression searchExpression, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetAssetModel> assetCriteria = builder.createQuery(AssetAssetModel.class);
        Root<AssetAssetModel> root = assetCriteria.from(AssetAssetModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        assetCriteria.orderBy(builder.asc(root.get("assetCode")));
        Query<AssetAssetModel> query = session.createQuery(assetCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetAssetModel> modelList = query.getResultList();
        //convert
        List<AssetAssetDto> dtoList = new ArrayList<AssetAssetDto>();
        List<AssetAssetDto> assetDtos = new ArrayList<AssetAssetDto>();
        for (AssetAssetModel model : modelList) {
	        if ("1".equals(isFilt)) {
		        //只可以修改 闲置、停用、使用、借出  的信息
		        Integer[] ints = {0, 1, 6, 7};
		        List<Integer> list = Arrays.asList(0, 1, 6, 7);
		        int status = model.getAssetStatus().ordinal();
		        if (list.indexOf(status) < 0) {
			        continue;
		        }
	        }
            assetDtos.add(convertAssetModelToDto(model));
        }

        if (!assetDtos.isEmpty()) {
            dtoList = convertForHtml(assetDtos);
        }
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetAssetModel.class);
        predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    @Override
    public long countAssetByExpressStr(ISearchExpression searchExpression) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        Root<AssetAssetModel> root = longCriteria.from(AssetAssetModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        return session.createQuery(longCriteria).uniqueResult();
    }


    /***
     * 获取资产id集合
     * @param searchExpression
     * @return
     */
    @Override
    public List<String> getAssetIdByExpressStr(ISearchExpression searchExpression) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetAssetModel> assetCriteria = builder.createQuery(AssetAssetModel.class);
        Root<AssetAssetModel> root = assetCriteria.from(AssetAssetModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        Query<AssetAssetModel> query = session.createQuery(assetCriteria);
        List<AssetAssetModel> modelList = query.getResultList();
        List<String> assetIdSet = new ArrayList<>();
        for (AssetAssetModel model : modelList) {
            assetIdSet.add(model.getId());
        }
        return assetIdSet;
    }

    @Override
    public byte[] exportAssetStanBookByQuerys(Object assetDtoList) {
        byte[] resultArray = null;
        try {
            int rowInt = 0;
            int cellInt = 0;

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            HSSFRow row = sheet.createRow(rowInt);
            HSSFCell cell = row.createCell(cellInt++);
            //title
            //基本信息 Start
            //cell.setCellValue("物资编码");
            cell.setCellValue("资产编码");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产类别");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产名称");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产类型");
            cell = row.createCell(cellInt++);
            cell.setCellValue("规格型号");
            cell = row.createCell(cellInt++);
            cell.setCellValue("序列号");
            cell = row.createCell(cellInt++);
            cell.setCellValue("计量单位");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产品牌");
            cell = row.createCell(cellInt++);
            cell.setCellValue("采购价");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产原值");
            cell = row.createCell(cellInt++);
            cell.setCellValue("技术参数");
            cell = row.createCell(cellInt++);
            cell.setCellValue("备注");
            //基本信息 End

            //延伸信息
            cell = row.createCell(cellInt++);
            cell.setCellValue("所属公司");
            cell = row.createCell(cellInt++);
            cell.setCellValue("所属线路_建筑");
            cell = row.createCell(cellInt++);
            cell.setCellValue("购置日期");
            cell = row.createCell(cellInt++);
            cell.setCellValue("主管部门");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产管理员");
            cell = row.createCell(cellInt++);
            cell.setCellValue("安装位置");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产来源");
            cell = row.createCell(cellInt++);
            cell.setCellValue("合同编号");
            cell = row.createCell(cellInt++);
            cell.setCellValue("标段编号");
            cell = row.createCell(cellInt++);
            cell.setCellValue("维保期");
            cell = row.createCell(cellInt++);
            cell.setCellValue("联系人");
            cell = row.createCell(cellInt++);
            cell.setCellValue("联系方式");
            cell = row.createCell(cellInt++);
            cell.setCellValue("出厂日期");
            //延伸信息 End

            for (AssetAssetDto assetDto : (List<AssetAssetDto>) assetDtoList) {
                cellInt = 0;
                row = sheet.createRow(++rowInt);
                cell = row.createCell(cellInt++);
                //cell.setCellValue(assetDto.getMaterialCode());
                cell.setCellValue(assetDto.getAssetCode());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getCombinationAssetType());//资产类别
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getCombinationAssetName());//资产名称
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getAssetTypeStr());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getSpecAndModels());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getSeriesNum());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getUnitOfMeasStr());//计量单位
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getAssetBrand());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getPurcPrice());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getEquiOrigValue());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getTechPara());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getRemark());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getCompanyStr());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getBelongLineStr());//所属线路建筑
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getBuyDate());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getManageDeptStr());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getManagerStr());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getSavePlaceStr());//安装位置
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getAssetSource());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getContractNum());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getTendersNum());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getMainPeriod());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getSourceUser());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getSourceContactInfo());
                cell = row.createCell(cellInt++);
                cell.setCellValue(assetDto.getProdTime());
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            resultArray = os.toByteArray();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultArray;
    }


    //********************资产正式表 End********************

    //********************历史记录 Start********************

    /**
     * 历史记录
     * 基础信息变更, 变更记录, 封存启封记录, 调拨记录, 盘点记录,
     * 新增记录, 减少记录
     */
    @Override
    public void commonCheckHistoryDto(AssetHistoryDto historyDto) {
        if (VGUtility.isEmpty(historyDto.getAssetModelId()))
            throw new RuntimeException("AssetModelId Is Null！");

        switch (historyDto.getHistoryType()) {
            case 基础信息变更:
                if (VGUtility.isEmpty(historyDto.getModifyContent()))
                    throw new RuntimeException("修改内容不能为空！");
                break;
            case 变更记录:
                if (VGUtility.isEmpty(historyDto.getChangeType()))
                    throw new RuntimeException("变更类型不能为空！");
                else if (VGUtility.isEmpty(historyDto.getChangeContent()))
                    throw new RuntimeException("变更内容不能为空！");
                break;
            case 封存启封记录:
                if (VGUtility.isEmpty(historyDto.getSealedUnsealed()))
                    throw new RuntimeException("操作方式不能为空！");
                break;
            case 调拨记录:
                if (VGUtility.isEmpty(historyDto.getCheckOutCom()))
                    throw new RuntimeException("调出公司不能为空！");
                else if (VGUtility.isEmpty(historyDto.getCheckInCom()))
                    throw new RuntimeException("调入公司不能为空！");
                else if (VGUtility.isEmpty(historyDto.getCheckOutDept()))
                    throw new RuntimeException("调出部门不能为空！");
                else if (VGUtility.isEmpty(historyDto.getCheckInDept()))
                    throw new RuntimeException("调入部门不能为空！");
                break;
            case 盘点记录:
                if (VGUtility.isEmpty(historyDto.getTakeStockResult()))
                    throw new RuntimeException("盘点结果不能为空！");
                break;
            case 新增记录:
                if (VGUtility.isEmpty(historyDto.getSourctType()))
                    throw new RuntimeException("来源方式不能为空！");
                else if (VGUtility.isEmpty(historyDto.getAssetSource()))
                    throw new RuntimeException("资产来源不能为空！");
                break;
            case 减少记录:
                if (VGUtility.isEmpty(historyDto.getReduceType()))
                    throw new RuntimeException("减少类型不能为空！");
                break;
            default:
                throw new RuntimeException("HistoryType Is Wrong！");
        }
    }

    @Override
    public AssetHistoryDto createHistory(AssetHistoryDto historyDto) {
        commonCheckHistoryDto(historyDto);
        return convertHistoryModelToDto(historyDao.save(convertHistoryDtoToModel(historyDto, null)));
    }

    @Override
    public ModelMap getHistoryByQuerysForDataGrid(String historyType, String assetId, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        //标准生成器
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //标准化对象查询
        CriteriaQuery<AssetHistoryModel> assetCriteria = builder.createQuery(AssetHistoryModel.class);
        //获取根
        Root<AssetHistoryModel> root = assetCriteria.from(AssetHistoryModel.class);
        //添加逻辑查询条件
        List<Predicate> andList = new ArrayList<>();
        andList.add(builder.equal(root.get("historyType"), HISTORY_TYPE.values()[VGUtility.toInteger(historyType)]));
        andList.add(builder.equal(root.get("assetModelId"), assetId));
        assetCriteria.where(builder.and(andList.toArray(new Predicate[andList.size()])));
        //添加查询顺序
        List<Order> listOrder = new ArrayList<Order>();
        listOrder.add(builder.asc(root.get("createTimestamp")));
        listOrder.add(builder.asc(root.get("lastUpdateTimestamp")));
        assetCriteria.orderBy(listOrder);
        //创建查询器
        Query<AssetHistoryModel> query = session.createQuery(assetCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetHistoryModel> modelList = query.list();
        //convert
        List<AssetHistoryDto> dtoList = new ArrayList<AssetHistoryDto>();
        for (AssetHistoryModel model : modelList) {
            //dtoList.add(convertHistoryModelToDto(model));
            dtoList.add(convertHistoryModelByTypeToDto(model, historyType));
        }

        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetHistoryModel.class);
        longCriteria.where(builder.and(andList.toArray(new Predicate[andList.size()])));
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    @Override
    public ModelMap getHistoryByQuerysForInterface(String assetId) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        //标准生成器
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //标准化对象查询
        CriteriaQuery<AssetHistoryModel> assetCriteria = builder.createQuery(AssetHistoryModel.class);
        //获取根
        Root<AssetHistoryModel> root = assetCriteria.from(AssetHistoryModel.class);
        //添加逻辑查询条件
        List<Predicate> andList = new ArrayList<>();
        andList.add(builder.equal(root.get("assetModelId"), assetId));
        assetCriteria.where(builder.and(andList.toArray(new Predicate[andList.size()])));
        //添加查询顺序
        List<Order> listOrder = new ArrayList<Order>();
        listOrder.add(builder.asc(root.get("createTimestamp")));
        listOrder.add(builder.asc(root.get("lastUpdateTimestamp")));
        assetCriteria.orderBy(listOrder);
        //创建查询器
        Query<AssetHistoryModel> query = session.createQuery(assetCriteria);
//        //分页
//        if(!VGUtility.isEmpty(pageableDto)) 
//        	query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetHistoryModel> modelList = query.list();
        //convert
        List<AssetHistoryDto> dtoList = new ArrayList<AssetHistoryDto>();
        for (AssetHistoryModel model : modelList) {
            //dtoList.add(convertHistoryModelToDto(model));
            dtoList.add(convertHistoryModelByTypeToDto(model, String.valueOf(model.getHistoryType().ordinal())));
        }

        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetHistoryModel.class);
        longCriteria.where(builder.and(andList.toArray(new Predicate[andList.size()])));
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }
    //********************历史记录 End********************

    //********************Convert Start********************

    /**
     * 数据转换 【惠逸锋】
     * convert model to dto
     * convert dto to model
     */
    private AssetAssetModel convertAssetDtoToModel(AssetAssetDto dto, AssetAssetModel model) {
        if (VGUtility.isEmpty(model))
            model = new AssetAssetModel();

        //基本信息
        model.setId(dto.getId());
        model.setAssetCode(dto.getAssetCode());
        model.setMaterialCode(dto.getMaterialCode());
        model.setAssetType(ASSET_TYPE.values()[VGUtility.toInteger(dto.getAssetType())]);
        model.setSpecAndModels(dto.getSpecAndModels());
        model.setSeriesNum(dto.getSeriesNum());
        model.setUnitOfMeasId(dto.getUnitOfMeasId());
        model.setAssetBrand(dto.getAssetBrand());
        model.setPurcPrice(VGUtility.toDouble(dto.getPurcPrice()));
        model.setEquiOrigValue(VGUtility.toDouble(dto.getEquiOrigValue()));
        model.setScrapValue(VGUtility.toDouble(dto.getScrapValue()));
        model.setTechPara(dto.getTechPara());
        model.setRemark(dto.getRemark());
        //延伸信息
        model.setCompanyId(dto.getCompanyId());
        model.setBelongLine(dto.getBelongLine());
        model.setAssetLineId(dto.getAssetLineId());
        if(!VGUtility.isEmpty(dto.getBuyDate())) {
        	model.setBuyDate(VGUtility.toDateObj(dto.getBuyDate(), "yyyy/M/d"));
        }
        model.setManageDeptId(dto.getManageDeptId());
        model.setManagerId(dto.getManagerId());
        model.setManagerEmpNo(dto.getManagerEmpNo());
        if (!VGUtility.isEmpty(dto.getUseDeptId()))
            model.setUseDeptId(dto.getUseDeptId());
        if (!VGUtility.isEmpty(dto.getUserId()))
            model.setUserId(dto.getUserId());
        model.setAssetSource(dto.getAssetSource());
        model.setContractNum(dto.getContractNum());
        model.setTendersNum(dto.getTendersNum());
        model.setSavePlaceId(dto.getSavePlaceId());
        if (!VGUtility.isEmpty(dto.getMainPeriod()))
            model.setMainPeriod(VGUtility.toDateObj(dto.getMainPeriod(), "yyyy/M/d"));
        model.setSourceUser(dto.getSourceUser());
        model.setSourceContactInfo(dto.getSourceContactInfo());
        if (!VGUtility.isEmpty(dto.getProdTime()))
            model.setProdTime(VGUtility.toDateObj(dto.getProdTime(), "yyyy/M/d"));
        model.setRecId(dto.getRecId());
        model.setIsSysn(dto.isSync());
        if (!VGUtility.isEmpty(dto.getSourceType())) {
            model.setSourceType(SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())]);
        }
        model.setProduceType(dto.getProduceType());
        return model;
    }

    private AssetAssetDto convertAssetModelToDto(AssetAssetModel model) {
        AssetAssetDto dto = new AssetAssetDto();
        //基本信息
        dto.setId(model.getId());
        dto.setAssetCode(model.getAssetCode());
        dto.setMaterialCode(model.getMaterialCode());
        dto.setSeriesNum(model.getSeriesNum());
        dto.setAssetBrand(model.getAssetBrand());
        dto.setPurcPrice(VGUtility.toDoubleStr(model.getPurcPrice(), "0.##"));
        dto.setEquiOrigValue(VGUtility.toDoubleStr(model.getEquiOrigValue(), "0.##"));
        dto.setScrapValue(VGUtility.toDoubleStr(model.getScrapValue(), "0.##"));
        dto.setTechPara(model.getTechPara());
        dto.setRemark(model.getRemark());
        //延伸信息
        dto.setCompanyId(model.getCompanyId());
        dto.setBelongLine(model.getBelongLine());
        dto.setBuyDate(VGUtility.toDateStr(model.getBuyDate(), "yyyy/M/d"));
        dto.setManageDeptId(model.getManageDeptId());
        dto.setManagerId(model.getManagerId());
        dto.setManagerEmpNo(model.getManagerEmpNo());
        dto.setAssetSource(model.getAssetSource());
        dto.setAssetType(String.valueOf(model.getAssetType().ordinal()));
        dto.setContractNum(model.getContractNum());
        dto.setTendersNum(model.getTendersNum());
        dto.setMainPeriod(VGUtility.toDateStr(model.getMainPeriod(), "yyyy/M/d"));
        dto.setSourceUser(model.getSourceUser());
        dto.setSourceContactInfo(model.getSourceContactInfo());
        dto.setProdTime(VGUtility.toDateStr(model.getProdTime(), "yyyy/M/d"));
        if (!VGUtility.isEmpty(model.getAssetStatus())) {
            dto.setAssetStatus(model.getAssetStatus().name());
            dto.setAssetStatusStr(Integer.toString(model.getAssetStatus().ordinal()));
        }
        if (!VGUtility.isEmpty(model.getBeforeChangeAssetStatus())) {
            dto.setBeforeChangeAssetStatus(model.getBeforeChangeAssetStatus().name());
            dto.setBeforeChangeAssetStatusStr(Integer.toString(model.getBeforeChangeAssetStatus().ordinal()));
        }
        dto.setRecId(model.getRecId());
        dto.setSavePlaceId(model.getSavePlaceId());
        dto.setUseDeptId(model.getUseDeptId());
        dto.setUserId(model.getUserId());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd HH:mm:ss"));
        dto.setLastUpdateTimestamp(VGUtility.toDateStr(model.getLastUpdateTimestamp(), "yyyy-MM-dd HH:mm:ss"));
        if (!VGUtility.isEmpty(model.getSourceType()))
            dto.setSourceType(model.getSourceType().name());

        dto.setProduceType(model.getProduceType());
        if (!VGUtility.isEmpty(model.getProduceType())) {
            dto.setProduceStr(model.getProduceType().name());
        }
        return dto;
    }

    @Override
    public List<AssetAssetDto> getAssetDtoByMaterialCode(String materialCode, AssetAssetDto assetAssetDto) {
        return getAssetStanBookMaterialByQuerys(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate = getPredicateByAssetDto(builder, root, assetAssetDto);
                return predicate;
            }
        }, materialCode);
    }

    private List<AssetAssetDto> getAssetStanBookMaterialByQuerys(ISearchExpression searchExpression, String materialCode) {
        //TODO
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetAssetModel> assetCriteria = builder.createQuery(AssetAssetModel.class);
        Root<AssetAssetModel> root = assetCriteria.from(AssetAssetModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        Query<AssetAssetModel> query = session.createQuery(assetCriteria);
        List<AssetAssetModel> resultList = query.getResultList();
        List<AssetAssetDto> showList = new ArrayList<AssetAssetDto>();
        for (AssetAssetModel assetModel : resultList) {
            if (assetModel.getMaterialCode().equals(materialCode)) {
                //TODO
                DictDto dictCommonDto = new DictDto();
                //计量单位的判断是否为空
                if (!VGUtility.isEmpty(assetModel.getUnitOfMeasId()))
                    dictCommonDto = dictService.getCommonCode(assetModel.getUnitOfMeasId());
                if (!VGUtility.isEmpty(dictCommonDto)) {
                    AssetAssetDto assetDton = new AssetAssetDto();
                    assetDton.setUnitOfMeasStr(dictCommonDto.getChsName());
                    assetDton.setId(assetModel.getId());
                    showList.add(assetDton);
                }
            }
        }
        return showList;
    }

    /***
     * 资产台账（物资模式）的搜索条件拼装
     * @param builder
     * @param root
     * @param assetDto
     * @return
     */
    private Predicate getPredicateByAssetDto(CriteriaBuilder builder, Root root, AssetAssetDto assetDto) {
        Predicate finalPred = null;
        List<Predicate> andList = new ArrayList<>();

        if (!VGUtility.isEmpty(assetDto.getMaterialCode()))//物资编码
            andList.add(builder.equal(root.get("materialCode"), assetDto.getMaterialCode()));
        if (!VGUtility.isEmpty(assetDto.getAssetCode()))//资产编码
            andList.add(builder.equal(root.get("assetCode"), assetDto.getAssetCode()));
        if (!VGUtility.isEmpty(assetDto.getAssetStatus()))//资产状态
            andList.add(builder.equal(root.get("assetStatus"), ASSET_STATUS.values()[VGUtility.toInteger(assetDto.getAssetStatus())]));

        if (!VGUtility.isEmpty(assetDto.getAssetType()))//资产类型
            andList.add(builder.equal(root.get("assetType"), ASSET_TYPE.values()[VGUtility.toInteger(assetDto.getAssetType())]));

        if (!VGUtility.isEmpty(assetDto.getManageDeptId()))//管理部门
            andList.add(builder.equal(root.get("manageDeptId"), assetDto.getManageDeptId()));
        if (!VGUtility.isEmpty(assetDto.getManagerId()))//管理员
            andList.add(builder.equal(root.get("managerId"), assetDto.getManagerId()));
        if (!VGUtility.isEmpty(assetDto.getUseDeptId()))//使用部门
            andList.add(builder.equal(root.get("useDeptId"), assetDto.getUseDeptId()));
        if (!VGUtility.isEmpty(assetDto.getUserId()))//使用人
            andList.add(builder.equal(root.get("userId"), assetDto.getUserId()));
        if (!VGUtility.isEmpty(assetDto.getSavePlaceId()))//安装位置
            andList.add(builder.equal(root.get("savePlaceId"), assetDto.getSavePlaceId()));

        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
        return finalPred;
    }

    private AssetHistoryModel convertHistoryDtoToModel(AssetHistoryDto dto, AssetHistoryModel model) {
        if (VGUtility.isEmpty(model))
            model = new AssetHistoryModel();

        model.setId(dto.getId());
        model.setCreateUserId(dto.getCreateUserId());
        model.setAssetModelId(dto.getAssetModelId());
        model.setHistoryType(dto.getHistoryType());
        model.setModifyContent(dto.getModifyContent());
        if(!VGUtility.isEmpty(dto.getChangeType())){
            model.setChangeType(CHANGE_TYPE.values()[VGUtility.toInteger(dto.getChangeType())]);
        }

        model.setChangeContent(dto.getChangeContent());
//        model.setSealedUnsealed(SEALED_UNSEALED.values()[VGUtility.toInteger(dto.getSealedUnsealed())]);
        if(!VGUtility.isEmpty(dto.getSealedUnsealed())){
            model.setSealedUnsealed(SEALED_UNSEALED.valueOf(dto.getSealedUnsealed()));
        }
        model.setCheckOutCom(dto.getCheckOutCom());
        model.setCheckInCom(dto.getCheckInCom());
        model.setCheckOutDept(dto.getCheckOutDept());
        model.setCheckInDept(dto.getCheckInDept());
        model.setTakeStockResult(dto.getTakeStockResult());
        if (!VGUtility.isEmpty(dto.getSourctType())) {
            model.setSourctType(SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourctType())]);
        }
        if (!VGUtility.isEmpty(dto.getReduceType())) {
            model.setReduceType(REDUCE_TYPE.values()[VGUtility.toInteger(dto.getReduceType())]);
        }
        model.setAssetSource(dto.getAssetSource());

        return model;
    }

    private AssetHistoryDto convertHistoryModelToDto(AssetHistoryModel model) {
        AssetHistoryDto dto = new AssetHistoryDto();

        dto.setId(model.getId());
        dto.setCreateUserId(model.getCreateUserId());
        dto.setCreateUserStr(userService.getUserInfo(model.getCreateUserId()).getChsName());
        dto.setAssetModelId(model.getAssetModelId());
        dto.setHistoryType(model.getHistoryType());
        dto.setModifyContent(model.getModifyContent());
        if (!VGUtility.isEmpty(model.getChangeType())) {
            dto.setChangeType(model.getChangeType().name());
        }
        dto.setChangeContent(model.getChangeContent());
        if (!VGUtility.isEmpty(model.getSealedUnsealed())) {
            dto.setSealedUnsealed(model.getSealedUnsealed().name());
        }
        dto.setCheckOutCom(model.getCheckOutCom());
        dto.setCheckInCom(model.getCheckInCom());
        dto.setCheckOutDept(model.getCheckOutDept());
        dto.setCheckInDept(model.getCheckInDept());
        dto.setTakeStockResult(model.getTakeStockResult());
        if (!VGUtility.isEmpty(model.getSourctType())) {
            dto.setSourctType(model.getSourctType().name());
        }
        dto.setAssetSource(model.getAssetSource());
        if (!VGUtility.isEmpty(model.getReduceType())) {
            dto.setReduceType(model.getReduceType().name());
        }
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy/M/d HH:mm"));

        return dto;
    }

    private AssetHistoryDto convertHistoryModelByTypeToDto(AssetHistoryModel model, String historyType) {
        AssetHistoryDto dto = new AssetHistoryDto();

        dto.setId(model.getId());
        dto.setCreateUserId(model.getCreateUserId());
        dto.setCreateUserStr(userService.getUserInfo(model.getCreateUserId()).getChsName());
        dto.setAssetModelId(model.getAssetModelId());
        dto.setHistoryType(model.getHistoryType());

        if (!VGUtility.isEmpty(model.getChangeType())) {
            dto.setChangeType(model.getChangeType().name());
        }
        dto.setChangeContent(model.getChangeContent());
        if (!VGUtility.isEmpty(model.getSealedUnsealed())) {
            dto.setSealedUnsealed(model.getSealedUnsealed().name());
        }
        if (!VGUtility.isEmpty(model.getCheckOutCom())) {
            dto.setCheckOutCom(model.getCheckOutCom());
            dto.setCheckOutComStr(model.getCheckOutCom());
        }
        if (!VGUtility.isEmpty(model.getCheckInCom())) {
            dto.setCheckInCom(model.getCheckInCom());
            dto.setCheckInComStr(model.getCheckInCom());
        }
        if (!VGUtility.isEmpty(model.getCheckOutDept())) {
            dto.setCheckOutDept(model.getCheckOutDept());
            dto.setCheckOutDeptStr(model.getCheckOutDept());
        }
        if (!VGUtility.isEmpty(model.getCheckInDept())) {
            dto.setCheckInDept(model.getCheckInDept());
            dto.setCheckInDeptStr(model.getCheckInDept());
        }
        dto.setTakeStockResult(model.getTakeStockResult());
        if (!VGUtility.isEmpty(model.getSourctType())) {
            dto.setSourctType(model.getSourctType().name());
        }
        dto.setAssetSource(model.getAssetSource());
        if (!VGUtility.isEmpty(model.getReduceType())) {
            dto.setReduceType(model.getReduceType().name());
        }
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy/M/d HH:mm"));

        dto.setModifyContent(model.getModifyContent());
        //根据类型拼接modifyContent
        if ("1".equals(historyType))
            dto.setModifyContent("变更类型:" + model.getChangeType() + " " + "变更内容:" + model.getChangeContent());
        if ("2".equals(historyType))
            dto.setModifyContent("操作方式:" + model.getSealedUnsealed().toString());
        if ("4".equals(historyType))
            dto.setModifyContent("盘点结果:" + model.getTakeStockResult());
        if ("5".equals(historyType))
//            dto.setModifyContent("来源方式:" + model.getSourctType() + " " + "资产来源:" + model.getAssetSource());
        if ("6".equals(historyType))
            dto.setModifyContent("减少类型:" + model.getReduceType().toString());

        return dto;
    }

    private AssetDownLoadFileDto convertUploadFileModelToDto(AssetUploadFileModel model) {
        AssetDownLoadFileDto dto = new AssetDownLoadFileDto();

        dto.setId(model.getId());
        dto.setAssetModelId(model.getAssetModelId());
        dto.setFileName(model.getFileName());
        dto.setFileSpec(model.getFileSpec());
        dto.setFileByteArray(model.getFileByteArray());
        String tempStr = "";
        double byteArrayKb = VGUtility.mathDivide(model.getFileByteArray().length * 1.0, 1024.0);
        if (byteArrayKb < 1024)
            tempStr = VGUtility.toDoubleStr(byteArrayKb, "#.##") + "kb";
        else
            tempStr = VGUtility.toDoubleStr(VGUtility.mathDivide(byteArrayKb, 1024.0), "#.##") + "mb";
        dto.setFileSize(tempStr);
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy/M/d HH:mm"));

        return dto;
    }
    //********************Convert End********************

    //********************上传附件 Start********************
    @Override
    public void uploadFile(String assetModelId, String fileName, byte[] byteArray, String fileSpec) {
        AssetUploadFileModel model = new AssetUploadFileModel();

        model.setAssetModelId(assetModelId);
        model.setFileName(fileName);
        model.setFileSpec(fileSpec);
        model.setFileByteArray(byteArray);

        uploadFileDao.save(model);
    }

    @Override
    public Map<String, Object> getUploadFileByQuerysForDataGrid(String assetModelId, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        //标准生成器
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //标准化对象查询
        CriteriaQuery<AssetUploadFileModel> assetCriteria = builder.createQuery(AssetUploadFileModel.class);
        //获取根
        Root<AssetUploadFileModel> root = assetCriteria.from(AssetUploadFileModel.class);
        //添加逻辑查询条件
        assetCriteria.where(builder.equal(root.get("assetModelId"), assetModelId));
        //创建查询器
        Query<AssetUploadFileModel> query = session.createQuery(assetCriteria);
        //分页
        if (VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetUploadFileModel> modelList = query.getResultList();

        //convert
        List<AssetDownLoadFileDto> dtoList = new ArrayList<AssetDownLoadFileDto>();
        for (AssetUploadFileModel model : modelList) {
            dtoList.add(convertUploadFileModelToDto(model));
        }

        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetUploadFileModel.class);
        longCriteria.where(builder.equal(root.get("assetModelId"), assetModelId));
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    @Override
    public AssetDownLoadFileDto getDownloadFileById(String uploadId) {
        AssetUploadFileModel model = uploadFileDao.getOne(uploadId);
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("文件已被删除！");
        return convertUploadFileModelToDto(model);
    }

    @Override
    public void deleteUploadFile(String uploadId) {
        uploadFileDao.deleteById(uploadId);
    }
    //********************上传附件 End********************

    @Override
    public List<AssetAssetDto> findAssetByHeadId(String headId) {
        List<AssetAssetModel> ms = assetDao.findAssetByHeadId(headId);
        List<AssetAssetDto> dtos = new ArrayList<AssetAssetDto>();
        List<AssetAssetDto> assetDtos = new ArrayList<AssetAssetDto>();
        for (AssetAssetModel model : ms) {
            AssetAssetDto dto = this.convertAssetModelToDto(model);
            assetDtos.add(dto);
        }
        if (!assetDtos.isEmpty()) {
            dtos = convertForHtml(assetDtos);
        }
        return dtos;
    }

    //*********************动态视图 Start**********************
    public void createDynamicView(AssetDynamicViewDto dynamicViewDto) {
        commonCheckDynamicAssetDto(dynamicViewDto);
        AssetDynamicViewModel model = convertDynamicViewDtoToModel(dynamicViewDto, null);
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        model.setCreator(userInfoDto.getId());
        model.setAssetViewState(1);//只要创建就默认状态时启用状态
        dynamicViewDao.save(model);
    }

    public void commonCheckDynamicAssetDto(AssetDynamicViewDto dynamicViewDto) {
        if (VGUtility.isEmpty(dynamicViewDto.getAssetViewName())) {
            throw new RuntimeException("视图名不能为空！");
        }
        if (VGUtility.isEmpty(dynamicViewDto.getAssetStatus()) && VGUtility.isEmpty(dynamicViewDto.getSavePlaceId())
                && VGUtility.isEmpty(dynamicViewDto.getAssetType()) && VGUtility.isEmpty(dynamicViewDto.getManageDeptId())
                && VGUtility.isEmpty(dynamicViewDto.getUseDeptId()) && VGUtility.isEmpty(dynamicViewDto.getUserId())
                && VGUtility.isEmpty(dynamicViewDto.getManagerId())) {
            throw new RuntimeException("视图条件不能为空！");
        }
    }

    //修改动态资产视图的分页bug
    @Override
    public Map<String, Object> getAssetDynamicForDataGrid(PageableDto pageableDto, UserInfoDto userInfoDto) {
        //通过hibernate获得Session
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        //通过session获得标准生成器 CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //标准化对象查询
        CriteriaQuery<AssetDynamicViewModel> assetCriteria = builder.createQuery(AssetDynamicViewModel.class);
        //获取根
        Root<AssetDynamicViewModel> root = assetCriteria.from(AssetDynamicViewModel.class);
        //根据查询器添加顺序TODO
        //assetCriteria.orderBy((List<javax.persistence.criteria.Order>) Order.desc("createTimestamp"));
        Order order = builder.asc(root.get("createTimestamp"));
        assetCriteria.orderBy(order);
        //创建查询器
        assetCriteria.select(root).where(root.get("creator").in(userInfoDto.getId()));
        Query<AssetDynamicViewModel> query = session.createQuery(assetCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetDynamicViewModel> modelList = query.getResultList();
        //convert
        List<AssetDynamicViewDto> dtoList = new ArrayList<AssetDynamicViewDto>();
        for (AssetDynamicViewModel model : modelList) {
            AssetDynamicViewDto dto = convertDynamicViewModelToDto(model);
            dtoList.add(dto);
        }
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetDynamicViewModel.class);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    @Override
    public void deleteDynamicViewById(String dynamicViewId) {
        dynamicViewDao.deleteById(dynamicViewId);
    }

    @Override
    public AssetDynamicViewDto getDynamicViewById(String dynamicViewId) {
        if (VGUtility.isEmpty(dynamicViewId)) {
            return null;
        } else {
            AssetDynamicViewModel model = dynamicViewDao.findById(dynamicViewId).get();
            return convertDynamicViewModelToDto(model);
        }
    }

    //有问题
    @Override
    public void updateAssetDynamicById(AssetDynamicViewDto dto) {
        commonCheckDynamicAssetDto(dto);
        AssetDynamicViewModel model = null;
        if (!VGUtility.isEmpty(dto.getId())) {
            int assetViewState = dynamicViewDao.findById(dto.getId()).get().getAssetViewState();
            dto.setAssetViewState(assetViewState);
            model = dynamicViewDao.findById(dto.getId()).get();
        } else {
            throw new RuntimeException("该视图已被删除！");
        }
        model = convertDynamicViewDtoToModel(dto, model);
        dynamicViewDao.save(model);
    }

    @Override
    public void changeDynamicViewState(String id, String assetViewState) {
        AssetDynamicViewModel model = dynamicViewDao.findById(id).get();
        model.setAssetViewState(Integer.valueOf(assetViewState));
    }

    private AssetDynamicViewModel convertDynamicViewDtoToModel(AssetDynamicViewDto dto, AssetDynamicViewModel model) {
        if (VGUtility.isEmpty(model))
            model = new AssetDynamicViewModel();
        model.setId(dto.getId());
        model.setAssetViewName(dto.getAssetViewName());
        if (!VGUtility.isEmpty(dto.getAssetStatus())) {
            model.setAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(dto.getAssetStatus())]);
        } else
            model.setAssetStatus(null);
        if (!VGUtility.isEmpty(dto.getAssetType())) {
            model.setAssetType(dto.getAssetType());
        } else
            model.setAssetType(null);
        model.setSavePlaceId(dto.getSavePlaceId());
        model.setManageDeptId(dto.getManageDeptId());
        model.setUseDeptId(dto.getUseDeptId());
        model.setManagerId(dto.getManagerId());
        model.setUserId(dto.getUserId());
        model.setAssetViewState(dto.getAssetViewState());
        return model;
    }

    private AssetDynamicViewDto convertDynamicViewModelToDto(AssetDynamicViewModel model) {
        String tempStr = "";
        String codeAndChsName = "";
        AssetDynamicViewDto dto = new AssetDynamicViewDto();
        DictDto dict = new DictDto();

        dto.setId(model.getId());
        dto.setAssetViewName(model.getAssetViewName());
        if (!VGUtility.isEmpty(model.getAssetStatus())) {
            dto.setAssetStatus(model.getAssetStatus().name());
            dto.setAssetStatusNum(Integer.toString(model.getAssetStatus().ordinal()));
        }
        if (!VGUtility.isEmpty(model.getAssetType())) {
            dict = (DictDto) baseService.getDictDtoByTypeAndCode(IBaseService.DICT_ASSET_TYPE, model.getAssetType());
            if (!VGUtility.isEmpty(dict))
                dto.setAssetType(dict.getCode() + " " + dict.getChsName());
            dto.setAssetTypeNum(model.getAssetType());
        }
        dto.setSavePlaceId(model.getSavePlaceId());
        dto.setManageDeptId(model.getManageDeptId());
        dto.setUseDeptId(model.getUseDeptId());
        dto.setManagerId(model.getManagerId());
        dto.setUserId(model.getUserId());
        dto.setAssetViewState(model.getAssetViewState());
        if (model.getAssetViewState() == 0)
            dto.setAssetViewStateStr("停用");
        else if (model.getAssetViewState() == 1)
            dto.setAssetViewStateStr("启用");

        if (!VGUtility.isEmpty(model.getSavePlaceId())) {
            DictDto commonCodeDto = dictService.getCommonCode(model.getSavePlaceId());
            if (!VGUtility.isEmpty(commonCodeDto)) {
                codeAndChsName = commonCodeDto.getCode() + "-" + commonCodeDto.getChsName();
                dto.setCodeAndChsName(codeAndChsName);
            }
        }
        if (!VGUtility.isEmpty(model.getAssetStatus()))
            tempStr += " 资产状态 = " + model.getAssetStatus().name() + ",";
        if (!VGUtility.isEmpty(model.getAssetType())) {

            if (!VGUtility.isEmpty(dict))
                tempStr += " 资产类型 = " + dict.getCode() + " " + dict.getChsName() + ",";
        }
        if (!VGUtility.isEmpty(codeAndChsName))
            tempStr += " 位置 = " + codeAndChsName + ",";
        if (!VGUtility.isEmpty(model.getManageDeptId())) {
            try {
                DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(model.getManageDeptId());
                tempStr += " 管理部门 = " + deptDto.getDeptName() + ",";
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(model.getUseDeptId())) {
            try {
                DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(model.getUseDeptId());
                tempStr += " 使用部门 = " + deptDto.getDeptName() + ",";
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(model.getManagerId())) {
            try {
                UserInfoDto userDto = userService.getUserInfo(model.getManagerId());
                tempStr += " 管理员 = " + userDto.getChsName() + ",";
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(model.getUserId())) {
            try {
                UserInfoDto userDto = userService.getUserInfo(model.getUserId());
                tempStr += " 使用人 = " + userDto.getChsName() + ",";
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(tempStr))
            tempStr = tempStr.substring(0, tempStr.length() - 1);
        dto.setAssetViewCondition(tempStr);
        return dto;
    }

    @Override
    public List<AssetAssetDto> excuteAssetViewConditionForDataGrid(String id) {
        List<AssetAssetDto> dtoList = new ArrayList<AssetAssetDto>();
        List<AssetAssetDto> assetDtos = new ArrayList<AssetAssetDto>();
        String hql = "from AssetAssetModel where 1=1";

        AssetDynamicViewModel model = dynamicViewDao.findById(id).get();
        if (!VGUtility.isEmpty(model.getAssetStatus()))
            hql += " and assetStatus = " + model.getAssetStatus().ordinal();
        if (!VGUtility.isEmpty(model.getAssetType()))
            hql += " and assetType = " + model.getAssetType();
        if (!VGUtility.isEmpty(model.getSavePlaceId()))
            hql += " and savePlaceId = " + model.getSavePlaceId();
        if (!VGUtility.isEmpty(model.getManageDeptId()))
            hql += " and manageDeptId = " + model.getManageDeptId();
        if (!VGUtility.isEmpty(model.getManagerId()))
            hql += " and managerId = " + model.getManagerId();
        if (!VGUtility.isEmpty(model.getUserId()))
            hql += " and userId = " + model.getUserId();

        TypedQuery<AssetAssetModel> query = entityManager.createQuery(hql, AssetAssetModel.class);
        List<AssetAssetModel> resultList = query.getResultList();
        for (AssetAssetModel assetmodel : resultList) {
            assetDtos.add(convertAssetModelToDto(assetmodel));
        }
        if (!assetDtos.isEmpty()) {
            dtoList = convertForHtml(assetDtos);
        }
        return dtoList;
    }
    //*********************动态视图 End**********************


    @Override
    public List<AssetAssetDto> getListByIdList(List<String> assetIdList) {
        List<AssetAssetDto> dtos = new ArrayList<AssetAssetDto>();
        List<AssetAssetModel> resultList = assetDao.findByIdIn(assetIdList);
        List<AssetAssetDto> assetDtoList = new ArrayList<AssetAssetDto>();
        for (AssetAssetModel model : resultList) {
            AssetAssetDto dto = convertAssetModelToDto(model);
            dtos.add(dto);
        }
        if (!dtos.isEmpty()) {
            assetDtoList = convertForHtml(dtos);
        }
        return assetDtoList;

    }
    @Override
    public List<AssetAssetDto> assetDetailForDataGrid(String materialCode) {
        String hql = "from AssetAssetModel where materialCode='" + materialCode + "' order by assetCode desc";
        TypedQuery<AssetAssetModel> query = entityManager.createQuery(hql, AssetAssetModel.class);
        List<AssetAssetModel> resultList = query.getResultList();
        List<AssetAssetDto> assetDtoList = new ArrayList<AssetAssetDto>();
        List<AssetAssetDto> dtos = new ArrayList<AssetAssetDto>();
        for (AssetAssetModel model : resultList) {
            AssetAssetDto dto = convertAssetModelToDto(model);
            dtos.add(dto);
        }
        if (!dtos.isEmpty()) {
            assetDtoList = convertForHtml(dtos);
        }
        return assetDtoList;
    }

    /*获取当前操作后续的资产状态
     * ASSET_STATUS oldstatus 当前资产状态
     * ASSET_STATUS newstatus 需要转换成的资产状态
     */
    @Override
    public Boolean getAssetStatus(ASSET_STATUS oldstatus, ASSET_STATUS newstatus) {
        boolean flag = false;
        switch (newstatus) {
            case 使用:
                if (oldstatus == ASSET_STATUS.闲置 || oldstatus == ASSET_STATUS.停用)
                    flag = true;
                break;
            case 借出:
                if (oldstatus == ASSET_STATUS.闲置 || oldstatus == ASSET_STATUS.停用)
                    flag = true;
                break;
            case 封存:
                if (oldstatus == ASSET_STATUS.停用)//且停用满一年
                    flag = true;
                break;
            case 报废:
                if (oldstatus == ASSET_STATUS.使用 || oldstatus == ASSET_STATUS.借出 || oldstatus == ASSET_STATUS.封存 || oldstatus == ASSET_STATUS.闲置)
                    flag = true;
                break;
            case 丢失:
                if (oldstatus == ASSET_STATUS.使用 || oldstatus == ASSET_STATUS.借出 || oldstatus == ASSET_STATUS.封存 || oldstatus == ASSET_STATUS.闲置)
                    flag = true;
                break;
            case 捐出:
                if (oldstatus == ASSET_STATUS.封存 || oldstatus == ASSET_STATUS.闲置)
                    flag = true;
                break;
            case 停用:
                if (oldstatus == ASSET_STATUS.闲置)
                    flag = true;
                break;
            case 闲置:
                if (oldstatus == ASSET_STATUS.使用 || oldstatus == ASSET_STATUS.借出 || oldstatus == ASSET_STATUS.封存 || oldstatus == ASSET_STATUS.报废)
                    flag = true;
                break;
            case 冻结:
                if (oldstatus == ASSET_STATUS.使用 || oldstatus == ASSET_STATUS.封存 || oldstatus == ASSET_STATUS.报废)
                    flag = true;
                break;
            case 调拨:
                if (oldstatus == ASSET_STATUS.闲置 || oldstatus == ASSET_STATUS.停用)
                    flag = true;
                break;
        }
        return flag;
    }

    @Override
    public void createAssetByDtos(List<AssetAssetDto> assetDtoList) {
        assetDtoList.stream().forEach(o -> {
            AssetAssetModel assetModel = this.convertAssetDtoToModel(o, null);
            assetModel.setAssetStatus(ASSET_STATUS.闲置);
            assetDao.save(assetModel);
        });
    }

    public List<AssetAssetDto> convertForHtml(List<AssetAssetDto> assetDtoList) {
        DictDto dictDto = new DictDto();
        DeptInfoDto deptInfoDto = new DeptInfoDto();
        UserInfoDto userInfoDto = new UserInfoDto();
        Set<String> dictIdSet = new HashSet<String>();
        Set<String> deptIdSet = new HashSet<String>();
        Set<String> userIdSet = new HashSet<String>();
        Set<String> consumeUserSet = new HashSet<String>();
        Set<String> consumeDeptSet = new HashSet<String>();
        Set<String> materialCodeSet = new HashSet<String>();
        Set<String> codeLv1Set = new HashSet<String>();
        Set<String> codeLv2Set = new HashSet<String>();
        Set<String> codeLv3Set = new HashSet<String>();
        Set<String> codeLv4Set = new HashSet<String>();
        Map<String, Object> propertyMap = new HashMap<String, Object>();
        Map<String, DictDto> dictMap = new HashMap<String, DictDto>();
        Map<String, DictDto> dictCodeMap = new HashMap<String, DictDto>();
        Map<String, DeptInfoDto> deptMap = new HashMap<String, DeptInfoDto>();
        Map<String, UserInfoDto> userMap = new HashMap<String, UserInfoDto>();
        Map<String, UserInfoDto> consumeUserMap = new HashMap<String, UserInfoDto>();
        Map<String, DeptInfoDto> consumeDeptMap = new HashMap<String, DeptInfoDto>();
        Map<String, DictDto> codeLv1Map = new HashMap<String, DictDto>();
        Map<String, DictDto> codeLv2Map = new HashMap<String, DictDto>();
        Map<String, DictDto> codeLv3Map = new HashMap<String, DictDto>();
        Map<String, DictDto> codeLv4Map = new HashMap<String, DictDto>();

        //获取所有需要查询数据库的id集合
        assetDtoList.stream().forEach(o -> {
            deptIdSet.add(o.getCompanyId());
            dictIdSet.add(o.getBelongLine());
            deptIdSet.add(o.getManageDeptId());
            userIdSet.add(o.getManagerId());
            consumeUserSet.add(o.getUserId());
            consumeDeptSet.add(o.getUseDeptId());
            dictIdSet.add(o.getSavePlaceId());
            materialCodeSet.add(o.getMaterialCode());
            if (!VGUtility.isEmpty(o.getMaterialCode())) {
                codeLv1Set.add(o.getMaterialCode().substring(0, 2));
                codeLv2Set.add(o.getMaterialCode().substring(0, 4));
                codeLv3Set.add(o.getMaterialCode().substring(0, 6));
                codeLv4Set.add(o.getMaterialCode().substring(0, 8));
            }
        });

        dictMap = baseService.getDictDtoByIdSet(dictIdSet);
        deptMap = baseService.getDeptDtoByIdSet(deptIdSet);
        userMap = baseService.getUserDtoByIdSet(userIdSet);
        consumeUserMap = baseService.getUserDtoByIdSet(consumeUserSet);
        consumeDeptMap = baseService.getDeptDtoByIdSet(consumeDeptSet);
        dictCodeMap = baseService.getDictByMaterialCodeSet(materialCodeSet);
        codeLv1Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV1, codeLv1Set);
        codeLv2Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV2, codeLv2Set);
        codeLv3Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV3, codeLv3Set);
        codeLv4Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV4, codeLv4Set);

        for (AssetAssetDto assetDto : assetDtoList) {
            deptInfoDto = deptMap.get(assetDto.getCompanyId());
            if (!VGUtility.isEmpty(deptInfoDto)) {
                assetDto.setCompanyStr(deptInfoDto.getDeptCode() + " " + deptInfoDto.getDeptName());
                assetDto.setCompanyCode(deptInfoDto.getDeptCode());
            }

            dictDto = dictMap.get(assetDto.getBelongLine());
            if (!VGUtility.isEmpty(dictDto))
                assetDto.setBelongLineStr(dictDto.getCode() + " " + dictDto.getChsName());

            deptInfoDto = deptMap.get(assetDto.getManageDeptId());
            if (!VGUtility.isEmpty(deptInfoDto)) {
                assetDto.setManageDeptStr(deptInfoDto.getDeptCode() + " " + deptInfoDto.getDeptName());
                assetDto.setManageDeptCode(deptInfoDto.getDeptCode());
            }

            userInfoDto = userMap.get(assetDto.getManagerId());
            if (!VGUtility.isEmpty(userInfoDto)) {
                Object EMP_NO = userInfoDto.getPropertyMap().get("EMP_NO");
                String EMP_NO_Str = "";
                if (!VGUtility.isEmpty(EMP_NO)) {
                    EMP_NO_Str = EMP_NO.toString();
                }
                assetDto.setManageCode(EMP_NO_Str);
                assetDto.setManagerStr(userInfoDto.getUserName() + " " + userInfoDto.getChsName());
            }
            deptInfoDto = consumeDeptMap.get(assetDto.getUseDeptId());
            if (!VGUtility.isEmpty(deptInfoDto)) {
                assetDto.setUseDeptStr(deptInfoDto.getDeptCode() + " " + deptInfoDto.getDeptName());
                assetDto.setUseDeptCode(deptInfoDto.getDeptCode());
            }

            userInfoDto = consumeUserMap.get(assetDto.getUserId());
            if (!VGUtility.isEmpty(userInfoDto)) {
                Object EMP_NO = userInfoDto.getPropertyMap().get("EMP_NO");
                String EMP_NO_Str = "";
                if (!VGUtility.isEmpty(EMP_NO)) {
                    EMP_NO_Str = EMP_NO.toString();
                }
                assetDto.setUseStr(userInfoDto.getUserName() + " " + userInfoDto.getChsName());
                assetDto.setUserCode(EMP_NO_Str);
            }

            dictDto = dictMap.get(assetDto.getSavePlaceId());
            if (!VGUtility.isEmpty(dictDto)) {
                assetDto.setSavePlaceCode(dictDto.getCode());
                assetDto.setSavePlaceName(dictDto.getChsName());
                assetDto.setSavePlaceStr(dictDto.getCode() + " " + dictDto.getChsName());
            }

            dictDto = dictCodeMap.get(assetDto.getMaterialCode());
            if (!VGUtility.isEmpty(dictDto)) {
                assetDto.setCombinationAssetName(dictDto.getChsName());
                propertyMap = dictDto.getPropertyMap();

                if (!VGUtility.isEmpty(propertyMap)) {
                    assetDto.setAssetTypeStr((String) propertyMap.get("W_PRO_CODE"));
                    assetDto.setSpecAndModels((String) propertyMap.get("MARTERIALS_SPEC"));
                    assetDto.setUnitOfMeasStr((String) propertyMap.get("W_UNIT_CODE"));
                }
            }

            if (!VGUtility.isEmpty(assetDto.getMaterialCode())) {
                String tempStr = "";

                dictDto = codeLv1Map.get(assetDto.getMaterialCode().substring(0, 2));
                if (!VGUtility.isEmpty(dictDto)) {
                    tempStr += dictDto.getChsName() + ",";
                }

                dictDto = codeLv2Map.get(assetDto.getMaterialCode().substring(0, 4));
                if (!VGUtility.isEmpty(dictDto)) {
                    tempStr += dictDto.getChsName() + ",";
                }

                dictDto = codeLv3Map.get(assetDto.getMaterialCode().substring(0, 6));
                if (!VGUtility.isEmpty(dictDto)) {
                    tempStr += dictDto.getChsName() + ",";
                }

                dictDto = codeLv4Map.get(assetDto.getMaterialCode().substring(0, 8));
                if (!VGUtility.isEmpty(dictDto)) {
                    tempStr += dictDto.getChsName();
                }

                if (tempStr.endsWith(",")) {
                    tempStr = tempStr.substring(0, tempStr.length() - 1);
                }

                assetDto.setCombinationAssetType(tempStr);
            }
        }

        return assetDtoList;
    }

    @Override
    public List<ErrorBean> commonCheckAssetForOutApi(List<AssetBean> assetBeans) {
        List<ErrorBean> list = new ArrayList<ErrorBean>();
        Map<String, UserInfoDto> userMap = baseService.getUserInfoByRoleCode("5-bmzcgly");
        for (AssetBean dto : assetBeans) {
            String desc = "";
            String assetCode = dto.getAssetCode();
            ErrorBean errorBean = new ErrorBean();
            
            if (VGUtility.isEmpty(assetCode)) {
				desc += "资产编码不能为空;";
            }
            
            AssetAssetModel assetModel = assetDao.findByAssetCode(assetCode);
            if (!VGUtility.isEmpty(assetModel)) {
//				desc += "资产编码["+assetCode+"]已存在;";
                continue;
            }

            if (VGUtility.isEmpty(dto.getAssetTypeCode())) {
            	desc += "资产类型不能为空;";
            }else if(!dto.getAssetTypeCode().equals("0") && !dto.getAssetTypeCode().equals("1")){
            	desc += "资产类型编码[" + dto.getAssetTypeCode() + "]不正确;";
            }
            
            
            if (VGUtility.isEmpty(dto.getBelongLine())) {
                desc += "所属线路不能为空;";
            } else {
                String typeId = dictService.getCommonCodeType(IBaseService.SYSMARK, IBaseService.DICT_BELONG_LINE).getId();
                List<DictDto> dictDtos = dictService.getCommonCode("{typeId:'" + typeId + "', code:'" + dto.getBelongLine() + "'}", null, null).getRowData();
                if (dictDtos.isEmpty()) {
                    desc += "所属线路编码[" + dto.getBelongLine() + "]不正确;";
                }
            }

            if (!VGUtility.isEmpty(dto.getManageDeptCode())) {
            	String deptCode = dto.getManageDeptCode();
                
                if(dto.getManageDeptCode().length()>=5) {
                	deptCode = dto.getManageDeptCode().substring(0, 5);
                }else {
                	desc += "主管部门编码[" + dto.getManageDeptCode() + "]不正确;";
                }
                
                List<DeptInfoDto> dictDtos = userService.getDeptInfo("{code:'" + deptCode + "'}", null, null).getRowData();
                if (dictDtos.isEmpty()) {
                    desc += "主管部门编码[" + dto.getManageDeptCode() + "]不正确;";
                }
                
                UserInfoDto info = userMap.get(deptCode);
                if(VGUtility.isEmpty(info)) {
                	desc += "主管部门[" + dto.getManageDeptCode() + "]下，没有资产管理员;";
                }
            }else {
            	desc += "主管部门不能为空;";
            }

            if(!VGUtility.isEmpty(dto.getUseDeptCode())) {
            	String deptCode = dto.getUseDeptCode();
                
                if(dto.getUseDeptCode().length()>=5) {
                	deptCode = dto.getUseDeptCode().substring(0, 5);
                }else {
                	desc += "使用部门编码[" + dto.getUseDeptCode() + "]不正确;";
                }
                
                List<DeptInfoDto> dictDtos = userService.getDeptInfo("{code:'" + deptCode + "'}", null, null).getRowData();
                if (dictDtos.isEmpty()) {
                    desc += "使用部门编码[" + dto.getUseDeptCode() + "]不正确;";
                }
            }
            
            if(!VGUtility.isEmpty(dto.getUserCode())) {
              List<UserInfoDto> userInfoDtos = userService.getUserInfo("{userName:'" + dto.getUserCode() + "'}", null, null).getRowData();
              if (userInfoDtos.isEmpty())
                  desc += "资产使用人编码[" + dto.getUserCode() + "]不正确;";
            }
            
            if (!VGUtility.isEmpty(dto.getManagerCode())) {
                List<UserInfoDto> userInfoDtos = userService.getUserInfo("{userName:'" + dto.getManagerCode() + "'}", null, null).getRowData();
                if (userInfoDtos.isEmpty())
                    desc += "资产管理员编码[" + dto.getManagerCode() + "]不正确;";
            }

            if (!VGUtility.isEmpty(dto.getSavePlaceCode())) {
                List<DictDto> dictDtos = dictService.getCommonCode("{code:'" + dto.getSavePlaceCode() + "'}", null, null).getRowData();
                if (dictDtos.isEmpty())
                    desc += "安装位置编码[" + dto.getSavePlaceCode() + "]不正确;";
            }

           if (VGUtility.isEmpty(dto.getCompanyCode())) {
	          desc = desc + "所属公司编码不能为空;";
	       }else {
	    	  List<DeptInfoDto> dictDtos = userService.getDeptInfo("{code:'" + dto.getCompanyCode() + "'}", null, null).getRowData();
              if (dictDtos.isEmpty()) {
                  desc += "所属公司编码[" + dto.getCompanyCode() + "]不正确;";
              }
	       }
            
//            if (VGUtility.isEmpty(dto.getPurcPrice())) {
//                desc = desc + "采购金额不能为空;";
//            }
            if (!VGUtility.isEmpty(dto.getPurcPrice()) && !dto.getPurcPrice().matches("[0-9]\\d*\\.?\\d*"))
                desc += "采购金额必须为数字;";

//            if (VGUtility.isEmpty(dto.getEquiOrigValue())) {
//                desc = desc + "原价金额不能为空;";
//            }
            if (!VGUtility.isEmpty(dto.getEquiOrigValue()) && !dto.getEquiOrigValue().matches("[0-9]\\d*\\.?\\d*"))
                desc += "原价金额必须为数字;";

            if (!VGUtility.isEmpty(desc)) {
                errorBean.setAssetCode(dto.getAssetCode());
                errorBean.setErrorDesc(desc);
                list.add(errorBean);
            }
        }
        return list;
    }

    private DeptInfoDto getDeptInfo(String parentId) {
        DeptInfoDto deptInfoDto = userService.getDeptInfo(parentId);
        return deptInfoDto;
    }

    /*
     * 动态获取资产信息；
     */
    @Override
    public Map<String, Object> getAssetByCondition(ISearchExpression searchExpression) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetAssetModel> assetCriteria = builder.createQuery(AssetAssetModel.class);
        Root<AssetAssetModel> root = assetCriteria.from(AssetAssetModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        assetCriteria.orderBy(builder.asc(root.get("assetCode")));
        Query<AssetAssetModel> query = session.createQuery(assetCriteria);
        List<AssetAssetModel> modelList = query.getResultList();
        Map<String, Object> map = new HashMap<>();
        map.put("rows", modelList);
        return map;

    }

    /*
     * 动态获取资产信息；资产价值信息同步
     */
    @Override
    public Map<String, Object> getAssetByList(ISearchExpression searchExpression, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetAssetModel> Criteria = builder.createQuery(AssetAssetModel.class);
        Root<AssetAssetModel> root = Criteria.from(AssetAssetModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            Criteria.where((Predicate) predicate);
        Criteria.orderBy(builder.desc(root.get("createTimestamp")));
        Query<AssetAssetModel> query = session.createQuery(Criteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetAssetModel> modelList = query.getResultList();
        List<AssetBean> dtolist = new ArrayList<AssetBean>();
        for (AssetAssetModel model : modelList) {
            dtolist.add(convertAssetBean(model));
        }
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetAssetModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtolist);
        modelMap.addAttribute("total", total);
        return modelMap;

        //return dtolist;
    }

    private AssetBean convertAssetBean(AssetAssetModel model) {
        AssetBean dto = new AssetBean();
        // 基本信息
        dto.setAssetCode(model.getAssetCode());
        dto.setAssetChsName(model.getAssetChsName());
        dto.setMaterialCode(model.getMaterialCode());
        dto.setAssetType(Integer.toString(model.getAssetType().ordinal()));
        //dto.setAssetTypeCode(Integer.toString(model.getAssetType().ordinal()));
        dto.setSpecAndModels(model.getSpecAndModels());
        dto.setSeriesNum(model.getSeriesNum());
        //dto.setUnitOfMeasCode(model.getUnitOfMeasId());
        dto.setAssetBrand(model.getAssetBrand());
        dto.setPurcPrice(VGUtility.toDoubleStr(model.getPurcPrice(), "0.##"));
        dto.setEquiOrigValue(VGUtility.toDoubleStr(model.getEquiOrigValue(), "0.##"));
        dto.setMonthDeprMoney(VGUtility.toDoubleStr(model.getMonthDeprMoney(), "0.##"));
        dto.setResidualValue(VGUtility.toDoubleStr(model.getResidualValue(), "0.##"));
        dto.setTechPara(model.getTechPara());
        dto.setRemark(model.getRemark());
        // 延伸信息
        if (!VGUtility.isEmpty(model.getManageDeptId())) {
            List<DeptInfoDto> dictDtos = new ArrayList<>();

            DeptInfoDto deptInfoDtos = ((DeptInfoDto) baseService.getDeptInfo(model.getManageDeptId()));
            dto.setCompanyCode(deptInfoDtos.getDeptCode());// 主管部门编码
            String parentId = deptInfoDtos.getPdId();
            if (!VGUtility.isEmpty(parentId)) {
                String topId = recursion(parentId);
                // 查询二级，遍历二级与传递过来的部门编码匹配
                dictDtos = userService.getDeptInfo("{parentId:'" + topId + "'}", null, null).getRowData();
                for (DeptInfoDto deptInfoDto : dictDtos) {
                    if (model.getManageDeptId().equals(deptInfoDto.getId())) {
                        dto.setManagerCode(deptInfoDto.getDeptCode());
                        break;
                    }
                }
            }
        } else
            dto.setManagerCode("");

        if (!VGUtility.isEmpty(model.getManagerId())) {
            UserInfoDto UserInfo = userService.getUserInfo(model.getManagerId());
            if (!VGUtility.isEmpty(UserInfo))
                dto.setManagerCode(userService.getUserInfo(model.getManagerId()).getUserName()); // 资产管理员编码
        } else
            dto.setManagerCode("");

        if (!VGUtility.isEmpty(model.getUseDeptId())) {
            List<DeptInfoDto> dictDtos = new ArrayList<>();

            DeptInfoDto deptInfoDtos = ((DeptInfoDto) baseService.getDeptInfo(model.getUseDeptId()));
            dto.setCompanyCode(deptInfoDtos.getDeptCode());// 主管部门编码
            String parentId = deptInfoDtos.getPdId();
            if (!VGUtility.isEmpty(parentId)) {
                String topId = recursion(parentId);
                // 查询二级，遍历二级与传递过来的部门编码匹配
                dictDtos = userService.getDeptInfo("{parentId:'" + topId + "'}", null, null).getRowData();
                for (DeptInfoDto deptInfoDto : dictDtos) {
                    if (model.getUseDeptId().equals(deptInfoDto.getId())) {
                        dto.setUseDeptCode(deptInfoDto.getDeptCode());
                        break;
                    }
                }
            }
        } else
            dto.setUseDeptCode("");

        if (!VGUtility.isEmpty(model.getUserId())) {
            UserInfoDto UserInfo = userService.getUserInfo(model.getUserId());
            if (!VGUtility.isEmpty(UserInfo))
                dto.setUserCode(userService.getUserInfo(model.getUserId()).getUserName()); // 资产管理员编码
        } else
            dto.setUserCode("");

        if (!VGUtility.isEmpty(model.getBelongLine()))
            dto.setBelongLine(dictService.getCommonCode("{id:'" + model.getBelongLine() + "'}", null, null).getRowData().get(0).getChsName()); // 所属建筑
        else
            dto.setBelongLine("");
        // 位置编码 = 资产线路 + 具体位置 + 设备房间号（建筑物号+楼层号+房间号）
        if (!VGUtility.isEmpty(model.getUnitOfMeasId()))
            dto.setUnitOfMeasCode(dictService.getCommonCode("{id:'" + model.getUnitOfMeasId() + "'}", null, null).getRowData().get(0).getChsName());
        else
            dto.setUnitOfMeasCode("");
        if (!VGUtility.isEmpty(model.getMaterialCode())) {
            MaterialCodeSyncBean bean = (MaterialCodeSyncBean) baseService.getMaterialCodeSyncBeanByMaterialCode(model.getMaterialCode());
            if (!VGUtility.isEmpty(bean))
                dto.setW_IS_PRO(bean.getW_IS_PRO());//是否进设备台账
        }

        dto.setBuyDate(VGUtility.toDateStr(model.getBuyDate(), "yyyy/M/d"));
        dto.setAssetSource(model.getAssetSource());
        dto.setContractNum(model.getContractNum());
        dto.setTendersNum(model.getTendersNum());
        dto.setMainPeriod(VGUtility.toDateStr(model.getMainPeriod(), "yyyy/M/d"));
        dto.setSourceUser(model.getSourceUser());
        dto.setSourceContactInfo(model.getSourceContactInfo());
        dto.setProdTime(VGUtility.toDateStr(model.getProdTime(), "yyyy/M/d"));
        dto.setAssetStatus(Integer.toString(model.getAssetStatus().ordinal()));
        if (!VGUtility.isEmpty(model.getSavePlaceId())) {
            DictDto savePlaceDto = dictService.getCommonCode(model.getSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                dto.setSavePlaceCode(savePlaceDto.getCode());
            }
        } else
            dto.setSavePlaceCode("");

        return dto;
    }

    private String recursion(String parentId) {
        String topId = "";
        do {
            DeptInfoDto deptInfo = userService.getDeptInfo(parentId);
            parentId = deptInfo.getPdId();
            topId = deptInfo.getId();
        } while (!VGUtility.isEmpty(parentId));
        return topId;
    }

    @Override
    public byte[] printAssetCardByAssetIdList(String assetIdListStr) {
        int pageNum = 1;
        byte[] result = null;
        List<String> assetIdList = new ArrayList<String>();
        List<byte[]> pageByteList = new ArrayList<byte[]>();

        if (VGUtility.isEmpty(assetIdList)) {
            throw new RuntimeException("至少选择一个资产！");
        } else {
            Arrays.stream(assetIdListStr.split(",")).forEach(arr -> assetIdList.add(arr.trim()));
        }

        List<AssetAssetModel> assetModelList = assetDao.findByIdIn(assetIdList);

        for (AssetAssetModel assetModel : assetModelList) {
            List<byte[]> tableArrayList = new ArrayList<byte[]>(); //表单数组集合
            List<TableCellItem> cellItemList = new ArrayList<TableCellItem>(); //单元格列表
            Map<String, String> assetMap = baseService.getAssetMapByMaterialCode(assetModel.getMaterialCode());

            TableCellItem cellItem = new TableCellItem("常州轨道交通固定资产卡片");
            cellItem.setBorderWidth(0);
            cellItem.setSize(13);
            cellItem.setTopPadding(-8f);
            cellItemList.add(cellItem);
            cellItem = new TableCellItem("资产编码：" + assetModel.getAssetCode());
            cellItem.setBorderWidth(0);
            cellItem.setSize(12);
            cellItemList.add(cellItem);
            cellItem = new TableCellItem("资产名称：" + assetMap.get("assetName"));
            cellItem.setBorderWidth(0);
            cellItem.setSize(12);
            cellItemList.add(cellItem);
            cellItem = new TableCellItem("规格型号：" + assetMap.get("MARTERIALS_SPEC"));
            cellItem.setBorderWidth(0);
            cellItem.setSize(12);
            cellItemList.add(cellItem);

            List<PdfPTable> tableList = new ArrayList<PdfPTable>();
            tableList.add(PDFUtility.CreateTable(new float[]{1f}, cellItemList, 210f, null, 0));
            //自定义页面大小 边距
            PageItem pageItem = new PageItem();
            pageItem.setMarginBotton(0);
            pageItem.setMarginLeft(0);
            pageItem.setMarginTop(0);
            pageItem.setMarginRight(0);
            pageItem.setPageSize(new Rectangle(227, 142));

            tableArrayList.add(PDFUtility.CreateTable(tableList, 0, pageItem));
            //合并表格
            pageByteList.add(PDFUtility.MergeFile(tableArrayList));
        }

        //合并页
        result = PDFUtility.MergeFile(pageByteList);
        //添加条形码
        for (AssetAssetModel assetModel : assetModelList) {
            result = PDFUtility.AddImg(result, pageNum++, this.getImageByteArrayByAssetCode(assetModel.getAssetCode()), 24f, 8f, 180f, 45f);
        }

        return result;
    }

    //资产编码转条形码
    private byte[] getImageByteArrayByAssetCode(String assetCode) {
        byte[] imageByteArray = null;

        try {
            int numBytesRead = 0;
            byte[] byteArray = new byte[1024];
            String imagePath = new QRcodeImageUtil().bulidBarCode(assetCode);
            FileImageInputStream imageInputStream = null;
            ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
            //读取本地文件byte数组
            imageInputStream = new FileImageInputStream(new File(imagePath));
            while ((numBytesRead = imageInputStream.read(byteArray)) != -1) {
                outPutStream.write(byteArray, 0, numBytesRead);
            }
            imageByteArray = outPutStream.toByteArray();
            outPutStream.close();
            imageInputStream.close();
        } catch (Exception e) {
            logger.error("Throw Exctpetion Where Read File：" + e);
        }

        return imageByteArray;
    }


    @Override
    public AssetApprove applyAssetCode(String id) {
        AssetApprove approve = new AssetApprove();
        approve.setCanApprove(true);
        String hql = "from AssetTempModel m where 1=1 and m.recId = :recId";
        JSONObject jsonObj = new JSONObject();
        List<AssetAssetDto> tempAssetDtoList = new ArrayList<AssetAssetDto>();
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        Map<String, List<String>> materialCodeBeanMap = new HashMap<String, List<String>>();
        Map<String, List<AssetAssetDto>> assetDtoMap = new HashMap<String, List<AssetAssetDto>>();
        GetMaterialsDataImplService service = new GetMaterialsDataImplService();

        //整理各类物资数量
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("recId", id);
        List<AssetAssetDto> assetDtolist = assetTempService.getAssetTempByRecId(hql, query, null).getRowData();
        if (assetDtolist.isEmpty()) {
            throw new RuntimeException("资产清单不能为空!");
        }
        for (AssetAssetDto dto : assetDtolist) {
            String materialCode = dto.getMaterialCode();
            Integer tempInt = countMap.get(materialCode);
            if (VGUtility.isEmpty(tempInt)) {
                countMap.put(materialCode, 1);
            } else {
                countMap.put(materialCode, ++tempInt);
            }

            tempAssetDtoList = assetDtoMap.get(materialCode);
            if (VGUtility.isEmpty(tempAssetDtoList)) {
                tempAssetDtoList = new ArrayList<AssetAssetDto>();
            }

            tempAssetDtoList.add(dto);
            assetDtoMap.put(materialCode, tempAssetDtoList);
        }

        //获取编码
        MaterialReceiptModel materialReceiptModel = materialReceiptDao.findById(id).get();
        ASSET_PRODUCE_TYPE produceType = materialReceiptModel.getProduceType();

        for (String key : countMap.keySet()) {
            String workNo = materialReceiptModel.getId() + "_" + key + "_" + countMap.get(key); //资产新增单Id+物资编码+数量
            CodeBean param = new CodeBean();
            List<EquipCode> params = param.getCODEBEAN();
            EquipCode s = new EquipCode();
            param.setNOTICENO(workNo);
            param.setNOTICEOS("2");
            s.setINNUM(Integer.toString(countMap.get(key)));
            s.setWID(key);
            params.add(s);

            jsonObj = JSON.parseObject(service.getGetMaterialsDataImplPort().getEquipCode(param));

            if ("200".equals(jsonObj.get("code").toString())) {
                String materialCode = new String();
                List<String> assetCodeList = new ArrayList<String>();
                List<MaterialCodeBean> materialCodeBeanList = new ArrayList<MaterialCodeBean>();

                materialCodeBeanList.addAll(JSON.parseObject(jsonObj.get("data").toString(), new TypeReference<ArrayList<MaterialCodeBean>>() {
                }));
                for (MaterialCodeBean materialCodeBean : materialCodeBeanList) {
                    materialCode = materialCodeBean.getCode();
                    assetCodeList.add(materialCodeBean.getEquipcode());
                }
                materialCodeBeanMap.put(materialCode, assetCodeList);
            } else {
                approve.setCanApprove(false);
                approve.setMessage("获取资产编码出错！ msg：" + jsonObj.get("msg").toString());
                return approve;
            }
        }

        for (String key : assetDtoMap.keySet()) {
            int tempInt = 0;
            for (AssetAssetDto dto : assetDtoMap.get(key)) {
                dto.setProduceType(produceType);
                dto.setAssetCode(materialCodeBeanMap.get(key).get(tempInt++));
                assetTempService.updateAssetTemp(dto);
            }
        }
        return approve;
    }


    @Override
    public void finishApprove(String id) {
        String hql = "from AssetTempModel m where 1=1 and m.recId = :recId";
        JSONObject jsonObj = new JSONObject();
        List<AssetAssetDto> tempAssetDtoList = new ArrayList<AssetAssetDto>();
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        Map<String, List<String>> materialCodeBeanMap = new HashMap<String, List<String>>();
        Map<String, List<AssetAssetDto>> assetDtoMap = new HashMap<String, List<AssetAssetDto>>();
        GetMaterialsDataImplService service = new GetMaterialsDataImplService();

        //整理各类物资数量
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("recId", id);
        List<AssetAssetDto> assetDtolist = assetTempService.getAssetTempByRecId(hql, query, null).getRowData();
        if (assetDtolist.isEmpty()) {
            throw new RuntimeException("资产清单不能为空!");
        }
        for (AssetAssetDto dto : assetDtolist) {
            String materialCode = dto.getMaterialCode();
            Integer tempInt = countMap.get(materialCode);
            if (VGUtility.isEmpty(tempInt)) {
                countMap.put(materialCode, 1);
            } else {
                countMap.put(materialCode, ++tempInt);
            }

            tempAssetDtoList = assetDtoMap.get(materialCode);
            if (VGUtility.isEmpty(tempAssetDtoList)) {
                tempAssetDtoList = new ArrayList<AssetAssetDto>();
            }

            tempAssetDtoList.add(dto);
            assetDtoMap.put(materialCode, tempAssetDtoList);
        }

        //获取编码
        MaterialReceiptModel materialReceiptModel = materialReceiptDao.findById(id).get();
        ASSET_PRODUCE_TYPE produceType = materialReceiptModel.getProduceType();
        String createPersonId = materialReceiptModel.getPersonId();
        //物资类型
        UserInfoDto user = new UserInfoDto();
        user.setId(createPersonId);

        for (String key : countMap.keySet()) {
            String workNo = materialReceiptModel.getId() + "_" + key + "_" + countMap.get(key); //资产新增单Id+物资编码+数量
            CodeBean param = new CodeBean();
            List<EquipCode> params = param.getCODEBEAN();
            EquipCode s = new EquipCode();
            param.setNOTICENO(workNo);
            param.setNOTICEOS("2");
            s.setINNUM(Integer.toString(countMap.get(key)));
            s.setWID(key);
            params.add(s);

            jsonObj = JSON.parseObject(service.getGetMaterialsDataImplPort().getEquipCode(param));

            if ("200".equals(jsonObj.get("code").toString())) {
                String materialCode = new String();
                List<String> assetCodeList = new ArrayList<String>();
                List<MaterialCodeBean> materialCodeBeanList = new ArrayList<MaterialCodeBean>();

                materialCodeBeanList.addAll(JSON.parseObject(jsonObj.get("data").toString(), new TypeReference<ArrayList<MaterialCodeBean>>() {
                }));
                for (MaterialCodeBean materialCodeBean : materialCodeBeanList) {
                    materialCode = materialCodeBean.getCode();
                    assetCodeList.add(materialCodeBean.getEquipcode());
                }
                materialCodeBeanMap.put(materialCode, assetCodeList);
            } else {
                throw new RuntimeException("获取资产编码出错！ msg：" + jsonObj.get("msg").toString());
            }
        }

        for (String key : assetDtoMap.keySet()) {
            int tempInt = 0;
            for (AssetAssetDto dto : assetDtoMap.get(key)) {
                dto.setProduceType(produceType);
                dto.setAssetCode(materialCodeBeanMap.get(key).get(tempInt++));
                assetTempService.updateAssetTemp(dto);
                dto.setId(null);
                //增加管理员工号字段；
                String managerId = dto.getManagerId();
                UserInfoDto userInfoDto = userService.getUserInfo(managerId);
                String managerEmpNo = null;
                if (!VGUtility.isEmpty(userInfoDto.getPropertyMap())) {
                    managerEmpNo = userInfoDto.getPropertyMap().get("EMP_NO").toString();
                }
                dto.setManagerEmpNo(managerEmpNo);
                this.createAsset(dto, user);

            }
        }

//        MaterialReceiptDto rDto = materialReceiptService.getMaterialReceiptById(id);
//        rDto.setReceiptStatusEnum(FlowableInfo.WORKSTATUS.已审批);
//        materialReceiptService.updateMaterialReceipt(rDto);
    }

    @Override
    public Set<String> getAssetTypeIdSetByUserId(String userId) {
        Set<String> materialCodeSet = new HashSet<String>();
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetAssetModel> assetCriteria = builder.createQuery(AssetAssetModel.class);
        Root<AssetAssetModel> root = assetCriteria.from(AssetAssetModel.class);
        if (!VGUtility.isEmpty(userId))
            assetCriteria.where(builder.equal(root.get("managerId"), userId));
        Query<AssetAssetModel> query = session.createQuery(assetCriteria);
        for (AssetAssetModel model : query.getResultList()) {
            materialCodeSet.add(model.getMaterialCode());
        }
        return materialCodeSet;
    }

    @Override
    public Set<String> getAssetTypeIdSetByUserRole() {
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        Map<String, Object> map = userInfoDto.getPropertyMap();
        boolean flag = baseService.getUserIDByRoleCode("admin", userInfoDto.getId());


        List<AssetAssetModel> list = null;
        String managerId = userInfoDto.getId();
        if (flag == false || "0".equals(map.get("isboss"))) {
            if (!VGUtility.isEmpty(map.get("depNoCut")) && "0".equals(map.get("iszcgly"))) {
                String deptCode = map.get("depNoCut").toString();
                Set<String> depIdList = baseService.getDeptIdListByDeptCode(deptCode);
                if (depIdList.size() > 0) {
                    list = assetDao.findByManageDeptIdIn(depIdList);
                } else {
                    list = assetDao.findByManagerIdIn(managerId);
                }
            } else if ("1".equals(map.get("iszcgly"))) {
                list = assetDao.findByManagerIdIn(managerId);
            }
        }
        Set<String> materialCodeSet = new HashSet<>();
        if (null != list && list.size() > 0) {
            materialCodeSet = list.stream().map(e -> e.getMaterialCode()).collect(Collectors.toSet());
        }

        return materialCodeSet;
    }


    /**
     * 获取当前未同步的新增资产
     *
     * @return
     */
    @Override
    public List<AssetAssetDto> getNoSyncAddAsset() {
        List<AssetAssetModel> list = assetDao.getNoSysnAddAsset();
        List<AssetAssetDto> dtolist = new ArrayList<>();
        for (AssetAssetModel model : list) {
            dtolist.add(this.convertAssetModelToDto(model));
        }
        if (dtolist.size() > 0) {
            dtolist = this.convertForHtml(dtolist);
        }
        return dtolist;
    }

    /**
     * 获取要推送的资产数据，并推送至主数据
     *
     * @return
     */
    @Override
    public String getAllSyncAssetBean() {
        List<AssetAssetModel> modelList = assetDao.findAll();
        List<AssetAssetDto> dtolist = new ArrayList<>();
        List<SyncAssetBean> list = new ArrayList<>();
        String json = "[]";
        for (AssetAssetModel model : modelList) {
            dtolist.add(this.convertAssetModelToDto(model));
        }
        if (dtolist.size() > 0) {
            dtolist = this.convertForHtml(dtolist);
            for (AssetAssetDto dto : dtolist) {
                list.add(this.convertSyncAssetBean(dto));
            }
        }
        if (list.size() > 0) {
            json = JSON.toJSONString(list, valueFilter);
        }
        if (json.startsWith("{")) {
            json = "[" + json + "]";
        }
        return json;
    }

    /**
     * 将null转换成“”
     */
    private ValueFilter valueFilter = (o, s, o1) -> o1 == null ? "" : o1;

    public SyncAssetBean convertSyncAssetBean(AssetAssetDto dto) {
        SyncAssetBean bean = new SyncAssetBean();
        bean.setAssetBrand(dto.getAssetBrand());
        bean.setAssetChsName(dto.getAssetChsName());
        bean.setAssetCode(dto.getAssetCode());
        bean.setAssetSource(dto.getAssetSource());
        bean.setAssetStatus(dto.getAssetStatusStr());
        bean.setAssetType(dto.getAssetType());
        bean.setBelongLine(dto.getBelongLineStr());
        bean.setBuyDate(dto.getBuyDate());
        bean.setCompanyCode(dto.getCompanyCode());
        bean.setMaterialCode(dto.getMaterialCode());
        bean.setSeriesNum(dto.getSeriesNum());
        bean.setPurcPrice(dto.getPurcPrice());
        bean.setEquiOrigValue(dto.getEquiOrigValue());
        bean.setScrapValue(dto.getScrapValue());
        bean.setTechPara(dto.getTechPara());
        bean.setRemark(dto.getRemark());
        bean.setContractNum(dto.getContractNum());
        bean.setMainPeriod(dto.getMainPeriod());
        bean.setMonthDeprMoney(dto.getMonthDeprMoney());
        bean.setProdTime(dto.getProdTime());
        bean.setResidualValue(dto.getResidualValue());
        bean.setCreateTime(dto.getCreateTimestamp());
        bean.setUpdateTime(dto.getLastUpdateTimestamp());
        bean.setManageCode(dto.getManageCode());
        bean.setManageDeptCode(dto.getManageDeptCode());
        bean.setSpecAndModels(dto.getSpecAndModels());
        bean.setSavePlaceCode(dto.getSavePlaceCode());
        bean.setScrapValue(dto.getScrapValue());
        bean.setSourceContactInfo(dto.getSourceContactInfo());
        bean.setUnitOfMeas(dto.getUnitOfMeasStr());
        return bean;
    }


    @Override
    @Scheduled(cron = "0 0 3 1/1 * ? ") // 每天03执行一次;
    public void convertToStop() {
        logger.info("convert from 闲置  to  停用");

        List<AssetAssetModel> list = assetDao.findByAssetStatus(ASSET_STATUS.闲置);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        //闲置满一年以上的资产状态自动变成停用状态
        for (AssetAssetModel assetAssetModel : list) {
            if (!VGUtility.isEmpty(assetAssetModel.getDeActiveTime()) && assetAssetModel.getDeActiveTime().before(y)) {
                //持久化状态的对象，自动更新数据；
                assetAssetModel.setAssetStatus(ASSET_STATUS.停用);
            }
        }
    }


    @Override
    public void doApprove(String Id) {
        AssetAssetModel model = assetDao.findById(Id).get();
        model.setBeforeChangeAssetStatus(model.getAssetStatus());
        model.setAssetStatus(ASSET_STATUS.冻结);
    }

    @Override
    public void approveSuccess(String Id, ASSET_STATUS newState) {
        AssetAssetModel model = assetDao.findById(Id).get();
        model.setAssetStatus(newState);
    }

    @Override
    public void approveFailure(String Id) {
        AssetAssetModel model = assetDao.findById(Id).get();
        model.setAssetStatus(model.getBeforeChangeAssetStatus());
    }

    @Override
    public ASSET_PRODUCE_TYPE getProduceTypeByMaterialCode(String materialCode) {

        //根据物资师是否进设备台帐
        // 是：生产性物资
        //否：非生产性物资
        Object W_IS_PRO = dictService.getCommonCode("{code:'" + materialCode + "'}", null, null)
                .getRowData().get(0).getPropertyMap().get("W_IS_PRO");
        String W_IS_PRO_STR = "";
        if (!VGUtility.isEmpty(W_IS_PRO)) {
            W_IS_PRO_STR = W_IS_PRO.toString();
        }
        if (W_IS_PRO_STR.equals("是")) {
            return IAssetService.ASSET_PRODUCE_TYPE.生产性物资;
        } else if (W_IS_PRO_STR.equals("否")) {
            return IAssetService.ASSET_PRODUCE_TYPE.非生产性物资;
        }

        return null;
    }
}