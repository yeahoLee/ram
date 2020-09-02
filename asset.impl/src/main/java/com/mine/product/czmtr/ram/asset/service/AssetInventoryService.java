package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetInventoryDao;
import com.mine.product.czmtr.ram.asset.dao.AssetInventoryScopeDao;
import com.mine.product.czmtr.ram.asset.dao.AssetInventoryTempDao;
import com.mine.product.czmtr.ram.asset.dao.MyAssetInventoryModelDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.*;
import com.mine.product.czmtr.ram.asset.service.IAssetService.*;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetInventoryService implements IAssetInventoryService {

    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);
    @Autowired
    private AssetInventoryDao assetInventoryDao;
    @Autowired
    private AssetInventoryScopeDao assetInventoryScopeDao;
    @Autowired
    private AssetInventoryTempDao assetInventoryTempDao;
    @Autowired
    private MyAssetInventoryModelDao myAssetInventoryModelDao;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IAssetInventoryTempService assetInventoryTempService;
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * 获取我的盘点清单
     *
     * @param searchExpression
     * @param pageableDto
     * @return
     */
    @Override
    public Map<String, Object> getMyAssetInventoryForDataGrid(ISearchExpression searchExpression,
                                                              PageableDto pageableDto) {
        List<MyAssetInventoryDto> dtoList = new ArrayList<MyAssetInventoryDto>();
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MyAssetInventoryModel> assetinventoryCriteria = builder.createQuery(MyAssetInventoryModel.class);
        Root<MyAssetInventoryModel> root = assetinventoryCriteria.from(MyAssetInventoryModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetinventoryCriteria.where((Predicate) predicate);
        assetinventoryCriteria.orderBy(builder.asc(root.get("createTimestamp")));
        Query<MyAssetInventoryModel> query = session.createQuery(assetinventoryCriteria);

        // 分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize())
                    .setMaxResults(pageableDto.getSize());

        List<MyAssetInventoryModel> modelList = query.getResultList();

        for (MyAssetInventoryModel myAssetInventoryModel : modelList) {
            dtoList.add(convertMyAssetInventoryModelToDto(myAssetInventoryModel));
        }

        // total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(MyAssetInventoryModel.class);
        longCriteria.select(builder.count(root));
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    /***
     * 获取我的盘点清单
     *
     * @param
     * @param pageableDto
     * @return
     */
    @Override
    public Map<String, Object> getMyAssetInventory(MyAssetInventoryDto dto, PageableDto pageableDto) {
        List<Map> myAssetInventoryList = myAssetInventoryModelDao.findByManagerid(dto.getManagerId());
        List<MyAssetInventoryDto> dtoList = new ArrayList<MyAssetInventoryDto>();
        if (myAssetInventoryList != null && myAssetInventoryList.size() > 0) {
            for (Map map : myAssetInventoryList) {
                MyAssetInventoryDto inventory = new MyAssetInventoryDto();
                inventory.setId((String) map.get("MyAssetInventoryId"));
                inventory.setAssetInventoryId((String) map.get("AssetInventoryId"));
                inventory.setMyAssetInventoryCode((String) map.get("ASSETINVENTORYCODE"));
                inventory.setInventoryName((String) map.get("INVENTORYNAME"));
                inventory.setLaunchDate(map.get("LAUNCHDATE").toString());

                if(!VGUtility.isEmpty(map.get("PRODUCETYPE"))){
                    String producetype = map.get("PRODUCETYPE").toString();
                    inventory.setProduceType(IAssetService.ASSET_PRODUCE_TYPE.values()[Integer.parseInt(producetype)]);
                }
                String managerDeptId = (String) map.get("MANAGERDEPTID");
                if (!VGUtility.isEmpty(managerDeptId)) {
                    inventory.setManagerDeptId(managerDeptId);
                    inventory.setManagerDeptStr(((DeptInfoDto) baseService.getDeptInfo(managerDeptId)).getDeptName());
                }
                inventory.setQuantity(Double.parseDouble(map.get("QUANTITY").toString()));
                inventory.setInventoryProfit(Double.parseDouble(map.get("INVENTORYPROFIT").toString()));
                inventory.setInventoryLoss(Double.parseDouble(map.get("INVENTORYLOSS").toString()));
                if (!VGUtility.isEmpty(map.get("MYINVENTORYSTATUS"))) {
                    String myinventoryStatus = map.get("MYINVENTORYSTATUS").toString();
                    inventory.setMyinventoryStatus(
                             FlowableInfo.WORKSTATUS.values()[VGUtility.toInteger(myinventoryStatus)].name());
                    inventory.setMyinventoryStatusStr(String
                            .valueOf( FlowableInfo.WORKSTATUS.values()[VGUtility.toInteger(myinventoryStatus)].ordinal()));
                }
                if (!VGUtility.isEmpty(map.get("INVENTORYSTATUS"))) {
                    String inventoryStatus = map.get("INVENTORYSTATUS").toString();
                    inventory.setInventoryStatus(FlowableInfo.WORKSTATUS.values()[VGUtility.toInteger(inventoryStatus)].name());
                    inventory.setInventoryStatusStr(
                            String.valueOf(FlowableInfo.WORKSTATUS.values()[VGUtility.toInteger(inventoryStatus)].ordinal()));
                }
                dtoList.add(inventory);
            }
        }
        int pagesize = 1;
        int page = 20;
        if (!VGUtility.isEmpty(pageableDto)) {
            pagesize = pageableDto.getSize();
            page = pageableDto.getPage();
        }

        ModelMap modelMap = new ModelMap();
        if (dtoList.size() >= pagesize)
            modelMap.addAttribute("rows", dtoList.subList((page - 1) * pagesize,
                    page * pagesize <= dtoList.size() ? page * pagesize : dtoList.size()));
        else
            modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", dtoList.size());
        return modelMap;
    }

    /***
     * 根据Id获取我的盘点单
     *
     * @param dto
     * @return
     */
    @Override
    public MyAssetInventoryDto getMyAssetInventoryDtoById(MyAssetInventoryDto dto) {
        MyAssetInventoryModel model = myAssetInventoryModelDao.findById(dto.getId()).get();
        return convertMyAssetInventoryModelToDto(model);
    }

    /***
     * 将我的盘点model转换成dto
     *
     * @param model
     * @return
     */
    public MyAssetInventoryDto convertMyAssetInventoryModelToDto(MyAssetInventoryModel model) {
        MyAssetInventoryDto dto = new MyAssetInventoryDto();
        dto.setId(model.getId());
        dto.setAssetInventoryId(model.getAssetInventoryModel().getId());
        dto.setMyAssetInventoryCode(model.getAssetInventoryCode());
        dto.setDisposalAdvice(model.getDisposalAdvice());
        dto.setInventoryLoss(model.getInventoryLoss());
        dto.setInventoryProfit(model.getInventoryProfit());
        dto.setManagerId(model.getManagerId());
        dto.setQuantity(model.getQuantity());
        dto.setReason(model.getReason());
        dto.setProduceType(model.getProduceType());
        if (!VGUtility.isEmpty(model.getManagerId())) {
            try {
                UserInfoDto userDto = userService.getUserInfo(model.getManagerId());
                if (!VGUtility.isEmpty(userDto))
                    dto.setManagerStr(userDto.getChsName());
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(model.getInventoryStatus())) {
            dto.setMyinventoryStatus(model.getInventoryStatus().name());
            dto.setMyinventoryStatusStr(String.valueOf(model.getInventoryStatus().ordinal()));
        }
        if (!VGUtility.isEmpty(model.getManagerDeptId())) {
            try {
                DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(model.getManagerDeptId());
                if (!VGUtility.isEmpty(deptDto))
                    dto.setManagerDeptStr(deptDto.getDeptName());
            } catch (Exception e) {
            }
        }
        return dto;
    }

    /**********************************
     * 我的盘点单
     ***********************************************/
    @Override
    public AssetInventoryDto createInventory(List<AssetAssetDto> assetDtoList,
                                             ArrayList<AssetInventoryScopeDto> assetInventoryScopeDtos, UserInfoDto userInfoDto, AssetInventoryDto dto) {
        AssetInventoryModel model = convertAssetInventoryDtoToModel(dto, null);
        model.setCreateId(userInfoDto.getId());
        model.setInventoryStatus(FlowableInfo.WORKSTATUS.拟稿);
        model.setCreateTimestamp(new Date());
        model.setLaunchDate(new Date());
        model.setInventoryRunningNum(getInventoryNum());
        model = assetInventoryDao.save(model);
        double amount = 0;
        if (assetDtoList.size() <= 0 && inventoryScopeExistAsset(assetInventoryScopeDtos)) {
//            throw new RuntimeException("必须存在被盘点的资产！");

            //没有添加盘点范围和附加资产，则盘点所有资产；
            AssetAssetDto dto1 = new AssetAssetDto();
            //非 捐出、报废、盘亏、丢失
            dto1.setAssetStatus("0,1,2,6,7,10");
            dto1.setProduceType(model.getProduceType());
            Map<String, Object> resultMap = assetService.getAssetByQuerys(dto1, "1");
            List<AssetAssetDto> DtoList = (List<AssetAssetDto>) resultMap.get("rows");
            amount = saveInventoryTempDuringCreate(DtoList, model, amount);

        } else {
            if (!assetInventoryScopeDtos.isEmpty()) {
                for (AssetInventoryScopeDto assetInventoryScopeDto : assetInventoryScopeDtos) {
                    if (!VGUtility.isEmpty(assetInventoryScopeDto.getInventoryContent())) {
                        AssetInventoryScopeModel aScopeModel = convertAssetInventoryScopeDtoToModel(assetInventoryScopeDto);
                        aScopeModel.setCreateTimestamp(new Date());
                        aScopeModel.setAssetInventoryModel(model);
                        amount += aScopeModel.getInventoryNum();
                        assetInventoryScopeDao.save(aScopeModel);
                    }
                }
            }
            amount = saveInventoryTempDuringCreate(assetDtoList, model, amount);
        }

        model.setQuantity(amount);
        model = assetInventoryDao.save(model);

        AssetInventoryDto assetInventoryDto = convertAssetInventoryModelToDto(model);
        return assetInventoryDto;
    }

    private double saveInventoryTempDuringCreate(List<AssetAssetDto> assetDtoList, AssetInventoryModel model, double amount) {
        Set<String> set = getAssetIdFromInventoryTemp(model.getId());
        if (!assetDtoList.isEmpty()) {
            for (AssetAssetDto assetDto : assetDtoList) {
                String mId = assetDto.getManagerId();
                String deptId = assetDto.getManageDeptId();
                // 资产是有管理员,有管理部门;
                if (!VGUtility.isEmpty(mId) && !VGUtility.isEmpty(deptId)) {
                    // 未盘点
                    if (set.isEmpty() || !set.contains(assetDto.getId())) {
                        AssetInventoryTempModel aTempModel = new AssetInventoryTempModel();
                        if (!VGUtility.isEmpty(assetDto.getId()))
                            aTempModel.setAssetId(assetDto.getId());
                        aTempModel.setAssetInventoryModel(model);
                        aTempModel.setCreateTimestamp(new Date());
                        aTempModel.setOperation(INVENTORY_OPERATION.手工录入);
                        //aTempModel.setInventoryWay(INVENTORY_WAY.手工);
                        aTempModel.setManageDeptId(assetDto.getManageDeptId());
                        assetInventoryTempDao.save(aTempModel);
                        set.add(assetDto.getId());
                        amount++;
                    }
                }
            }
        }
        return amount;
    }

    @Override
    public void deleteAssetInventory(String id) {
        if (VGUtility.isEmpty(id))
            throw new RuntimeException("选择的盘点单为空！");
        myAssetInventoryModelDao.deleteMyAssetInventoryByInventoryById(id);
        // 删除主表下的我的盘点清单；
        assetInventoryTempDao.deleteAssetInventoryTempByInventoryById(id);
        // 删除主表下的盘点单范围；
        assetInventoryScopeDao.deleteAssetInventoryScopeByInventoryById(id);
        // 删除主表；
        assetInventoryDao.deleteById(id);
    }

    @Override
    public Map<String, Object> getAssetInventoryForDataGrid(ISearchExpression searchExpression, AssetInventoryDto dto,
                                                            PageableDto pageableDto) {
        List<AssetInventoryDto> dtoList = new ArrayList<AssetInventoryDto>();
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetInventoryModel> assetinventoryCriteria = builder.createQuery(AssetInventoryModel.class);
        Root<AssetInventoryModel> root = assetinventoryCriteria.from(AssetInventoryModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetinventoryCriteria.where((Predicate) predicate);
        assetinventoryCriteria.orderBy(builder.asc(root.get("createTimestamp")));
        Query<AssetInventoryModel> query = session.createQuery(assetinventoryCriteria);

        // 分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize())
                    .setMaxResults(pageableDto.getSize());

        List<AssetInventoryModel> modelList = query.getResultList();

        for (AssetInventoryModel assetSequestrationModel : modelList) {
            dtoList.add(convertAssetInventoryModelToDto(assetSequestrationModel));
        }

        // total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetInventoryModel.class);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    /***
     * 根据盘点任务id获取盘点信息
     *
     * @param id
     * @return
     */
    @Override
    public AssetInventoryDto getAssetInventoryDtoById(String id) {
        AssetInventoryModel model = assetInventoryDao.findById(id).get();
        return convertAssetInventoryModelToDto(model);
    }

    /**
     * 把盘点任务model转成dto
     *
     * @param model
     * @return
     */
    private AssetInventoryDto convertAssetInventoryModelToDto(AssetInventoryModel model) {
        AssetInventoryDto dto = new AssetInventoryDto();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!VGUtility.isEmpty(model.getLaunchDate()))
            dto.setLanuchDateStr(dateFormat.format(model.getLaunchDate()));
        dto.setId(model.getId());
        dto.setInventoryLoss(model.getInventoryLoss());
        dto.setInventoryProfit(model.getInventoryProfit());
        dto.setLaunchDate(model.getLaunchDate());
        dto.setLanuchDateStr(VGUtility.toDateStr(model.getLaunchDate(), "yyyy-MM-dd"));
        dto.setInventoryProcess(model.getInventoryProcess());
        dto.setInventoryRunningNum(model.getInventoryRunningNum());
        dto.setQuantity(model.getQuantity());
        dto.setReason(model.getReason());
        dto.setInventoryName(model.getInventoryName());
        dto.setProduceType(model.getProduceType());
        if (!VGUtility.isEmpty(model.getCreateId())) {
            try {
                UserInfoDto userDto = userService.getUserInfo(model.getCreateId());
                if (!VGUtility.isEmpty(userDto))
                    dto.setCreaterName(userDto.getChsName());
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(model.getInventoryStatus())) {
            dto.setInventoryStatus(model.getInventoryStatus());
            dto.setInventoryStatusStr(model.getInventoryStatus().ordinal());
        }
        return dto;
    }

    /**
     * 把盘点资产范围Dto转成model
     *
     * @param assetInventoryScopeDto
     * @return
     */
    private AssetInventoryScopeModel convertAssetInventoryScopeDtoToModel(AssetInventoryScopeDto dto) {
        AssetInventoryScopeModel model = new AssetInventoryScopeModel();
        model.setInventoryContent(dto.getInventoryContent());
        model.setInventoryNum(dto.getInventoryNum());
        model.setManageDeptId(dto.getManageDeptId());
        model.setManagerId(dto.getManagerId());
        model.setUseDeptId(dto.getUseDeptId());
        if (dto.getAssetStatus() != null && !dto.getAssetStatus().equals("")) {
            model.setAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(dto.getAssetStatus())]);
        }
        if (dto.getAssetType() != null && !dto.getAssetType().equals("")) {
            model.setAssetType(dto.getAssetType());
        }
        model.setUserId(dto.getUserId());
        model.setSavePlaceId(dto.getSavePlaceId());
        model.setId(dto.getId());
        return model;
    }

    /**
     * 把盘点单转成model
     *
     * @param dto
     * @return
     */
    private AssetInventoryModel convertAssetInventoryDtoToModel(AssetInventoryDto dto, AssetInventoryModel oldModel) {
        if (VGUtility.isEmpty(oldModel))
            oldModel = new AssetInventoryModel();
        if (!VGUtility.isEmpty(dto.getId()))
            oldModel.setId(dto.getId());
        if (!VGUtility.isEmpty(dto.getInventoryLoss()))
            oldModel.setInventoryLoss(dto.getInventoryLoss());
        if (!VGUtility.isEmpty(dto.getInventoryName()))
            oldModel.setInventoryName(dto.getInventoryName());
        if (!VGUtility.isEmpty(dto.getQuantity()))
            oldModel.setQuantity(dto.getQuantity());
        if (!VGUtility.isEmpty(dto.getInventoryRunningNum()))
            oldModel.setInventoryRunningNum(dto.getInventoryRunningNum());
        if (!VGUtility.isEmpty(dto.getInventoryProfit()))
            oldModel.setInventoryProfit(dto.getInventoryProfit());
        if (!VGUtility.isEmpty(dto.getLaunchDate()))
            oldModel.setLaunchDate(VGUtility.toDateObj(dto.getLanuchDateStr(), "yyyy-MM-dd"));
        if (!VGUtility.isEmpty(dto.getInventoryProcess()))
            oldModel.setInventoryProcess(dto.getInventoryProcess());
        if (!VGUtility.isEmpty(dto.getReason()))
            oldModel.setReason(dto.getReason());
        if (dto.getInventoryStatus() != null && !dto.getInventoryStatus().equals(""))
            oldModel.setInventoryStatus(dto.getInventoryStatus());
        if (!VGUtility.isEmpty(dto.getProduceType()))
            oldModel.setProduceType(dto.getProduceType());
        return oldModel;
    }

    /**
     * 把盘点资产范围model转成dto
     *
     * @param model
     * @return
     */
    private AssetInventoryScopeDto convertAssetInventoryScopeModelToDto(AssetInventoryScopeModel model) {
        AssetInventoryScopeDto dto = new AssetInventoryScopeDto();
        dto.setId(model.getId());
        if (!VGUtility.isEmpty(model.getAssetInventoryModel())) {
            dto.setAssetInventoryId(model.getAssetInventoryModel().getId());
        }
        if (!VGUtility.isEmpty(model.getAssetStatus())) {
            dto.setAssetStatus(String.valueOf(model.getAssetStatus().ordinal()));
        }
        if (!VGUtility.isEmpty(model.getAssetType())) {
            dto.setAssetType(model.getAssetType());
        }
        dto.setInventoryContent(model.getInventoryContent());
        dto.setInventoryNum(model.getInventoryNum());
        dto.setManageDeptId(model.getManageDeptId());
        dto.setManagerId(model.getManagerId());
        dto.setUseDeptId(model.getUseDeptId());
        dto.setUserId(model.getUserId());
        dto.setSavePlaceId(model.getSavePlaceId());
        return dto;
    }

    private String getInventoryNum() {
        Date time = Calendar.getInstance().getTime();
        String date = VGUtility.toDateStr(time, "yyyy-MM-dd");
        String hql = "from AssetInventoryModel where to_char(createtimestamp,'yyyy-MM-dd')='" + date
                + "' order by inventoryRunningNum desc";
        TypedQuery<AssetInventoryModel> query = entityManager.createQuery(hql, AssetInventoryModel.class);

        List<AssetInventoryModel> modelList = query.getResultList();
        String code = "";
        if (modelList.size() > 0) {
            String borrowCode = modelList.get(0).getInventoryRunningNum();
            if (!VGUtility.isEmpty(borrowCode)) {
                code = borrowCode;
                Long num = Long.parseLong(code.substring(2)) + 1;
                code = "PD" + num;
            } else {
                code = "PD" + date.replace("-", "") + "001";
            }
        } else {
            code = "PD" + date.replace("-", "") + "001";
        }
        return code;
    }

    @Override
    public void distributionInventory(String assetInventoryModel_ID) {
        AssetInventoryModel assetInventoryModel = assetInventoryDao.findById(assetInventoryModel_ID).get();
        if (VGUtility.isEmpty(assetInventoryModel))
            throw new RuntimeException("该盘点任务单已被删除！");
        // 2.从盘点范围取出需要盘点的资产；
        List<AssetAssetModel> assetModelList = getAssetByInventoryModel_Id(assetInventoryModel_ID);
        // 3.创建盘点资产清单
        CreateTempModel(assetInventoryModel, assetModelList);
        // 4. 创建我的盘点清单中
        createMyAssetInventory(assetInventoryModel);
        // 1. 更新盘点任务状态；
//        assetInventoryModel.setInventoryStatus(FlowableInfo.WORKSTATUS.已审批);
        List<MyAssetInventoryModel> list = myAssetInventoryModelDao.findByInventoryId(assetInventoryModel_ID);
        List<AssetInventoryTempModel> templist = assetInventoryTempDao
                .findByassetInventoryModelId(assetInventoryModel_ID);
        double quantity = list.size();
        double tempquantity = templist.size();
        assetInventoryModel.setQuantity(tempquantity);
        assetInventoryModel.setInventoryProcess("0/" + (int) quantity);
        assetInventoryDao.save(assetInventoryModel);
    }

    /**
     * 通过盘点id从盘点资产范围中取出需要盘点的资产
     *
     * @param assetInventoryModel_ID 盘点任务id
     * @return
     */
    private List<AssetAssetModel> getAssetByInventoryModel_Id(String assetInventoryModel_ID) {
        List<AssetAssetModel> assetModelList = new ArrayList<AssetAssetModel>();

        List<AssetInventoryScopeModel> scopeModels = assetInventoryScopeDao
                .findAssetByInventoryModelId(assetInventoryModel_ID);

        if (scopeModels != null && !scopeModels.isEmpty()) {
            scopeModels.stream().forEach(e -> {
                AssetAssetDto assetDto = new AssetAssetDto();
                assetDto.setManageDeptId(e.getManageDeptId());
                if (!VGUtility.isEmpty(e.getAssetStatus())) {
                    assetDto.setAssetStatus(e.getAssetStatus().name());
                }
                assetDto.setUseDeptId(e.getUseDeptId());
                assetDto.setUserId(e.getUserId());
                assetDto.setManagerId(e.getManagerId());
                assetDto.setSavePlaceId(e.getSavePlaceId());
                if (!VGUtility.isEmpty(e.getAssetType())) {
                    assetDto.setAssetType(e.getAssetType());
                }

                Map<String, Object> resultMap = assetService.getAssetByCondition(new ISearchExpression() {
                    @Override
                    public Object change(Object... arg0) {
                        CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                        Root root = (Root) arg0[0];
                        Predicate predicate;
                        predicate = getPredicateByAssetDto(builder, root, assetDto);
                        return predicate;
                    }
                });
                if (resultMap != null && !resultMap.isEmpty()) {
                    List<AssetAssetModel> modelList = (List<AssetAssetModel>) resultMap.get("rows");
                    assetModelList.addAll(modelList);
                }
            });
        }
        return assetModelList;
    }

    /**
     * @param assetInventoryModel
     */
    private void createMyAssetInventory(AssetInventoryModel assetInventoryModel) {
        List<Map> managerS = assetInventoryTempDao.findManagerid(assetInventoryModel.getId());

        String code = "0";
        if (managerS != null && managerS.size() > 0) {
            for (Map map : managerS) {
                String managerId = (String) map.get("mId");
                double num = Double.parseDouble(map.get("num").toString());
                String managerDeptId = (String) map.get("managerDeptId");
                code = getNext(code);
                if (!VGUtility.isEmpty(managerId)) {
                    MyAssetInventoryModel myAssetInventoryModel = new MyAssetInventoryModel();
                    myAssetInventoryModel
                            .setAssetInventoryCode(assetInventoryModel.getInventoryRunningNum() + '-' + code);
                    myAssetInventoryModel.setAssetInventoryModel(assetInventoryModel);
                    myAssetInventoryModel.setManagerId(managerId);
                    myAssetInventoryModel.setQuantity(num);
                    myAssetInventoryModel.setManagerDeptId(managerDeptId);
                    //生产型 非生产型
                    myAssetInventoryModel.setProduceType(assetInventoryModel.getProduceType());
                    myAssetInventoryModel.setInventoryStatus( FlowableInfo.WORKSTATUS.拟稿);
                    MyAssetInventoryModel save = myAssetInventoryModelDao.save(myAssetInventoryModel);
                    // 为当前管理员下的所有盘点清单设置盘点单编号
                    List<AssetInventoryTempModel> tempModels = assetInventoryTempDao.findByManagerId(managerId,
                            assetInventoryModel.getId());
                    if (tempModels != null && tempModels.size() > 0) {
                        tempModels.stream().forEach(e -> {
                            if (!VGUtility.isEmpty(e.getManageDeptId()) && e.getManageDeptId().equals(save.getManagerDeptId())) {
                                e.setMyAssetInventoryModel(save);
                                assetInventoryTempDao.save(e);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 创建盘点资产清单
     *
     * @param assetInventoryModel
     * @param assetModelList
     * @param
     */
    private void CreateTempModel(AssetInventoryModel assetInventoryModel, List<AssetAssetModel> assetModelList) {
        if (assetModelList != null && !assetModelList.isEmpty()) {
            Set<String> set = getAssetIdFromInventoryTemp(assetInventoryModel.getId());
            assetModelList.stream().forEach(e -> {
                // 资产是有管理员
                String mId = e.getManagerId();
                String deptId = e.getManageDeptId();
                if (!VGUtility.isEmpty(mId) && !VGUtility.isEmpty(deptId)) {
                    // 未盘点
                    if (set.isEmpty() || !set.contains(e.getId())) {
                        AssetInventoryTempModel assetInventoryTempModel = new AssetInventoryTempModel();
                        assetInventoryTempModel.setAssetInventoryModel(assetInventoryModel);
                        assetInventoryTempModel.setAssetId(e.getId());
                        assetInventoryTempModel.setOperation(INVENTORY_OPERATION.手工录入);
                        //assetInventoryTempModel.setInventoryWay(INVENTORY_WAY.手工);
                        assetInventoryTempModel.setCreateTimestamp(new Date());
                        assetInventoryTempModel.setManageDeptId(e.getManageDeptId());
                        assetInventoryTempDao.save(assetInventoryTempModel);
                        set.add(e.getId());
                    }
                }
                ;
            });
        }
    }

    /**
     * 获取001-999的字符串 @return001-999
     */
    public String getNext(String num) {
        String rst = null;
        int tmp = Integer.valueOf(num);
        tmp++;
        if (tmp < 10) {
            rst = "00" + tmp;
        } else if (tmp >= 100) {
            rst = tmp + "";
        } else
            rst = "0" + tmp;
        num = rst;
        return rst;
    }

    Predicate finalPred = null;

    private Predicate getPredicateByAssetDto(CriteriaBuilder builder, Root root, AssetAssetDto assetDto) {
        List<Predicate> andList = new ArrayList<>();

        if (!VGUtility.isEmpty(assetDto.getAssetCode()))
            andList.add(builder.like(root.get("assetCode"), "%" + assetDto.getAssetCode() + "%"));
        if (!VGUtility.isEmpty(assetDto.getAssetStatus()))
            andList.add(builder.equal(root.get("assetStatus"), ASSET_STATUS.valueOf(assetDto.getAssetStatus())));
        if (!VGUtility.isEmpty(assetDto.getAssetType())) {
            andList.add(builder.equal(root.get("materialCode"), assetDto.getAssetType()));
        }
        if (!VGUtility.isEmpty(assetDto.getManageDeptId()))
            andList.add(builder.equal(root.get("manageDeptId"), assetDto.getManageDeptId()));
        if (!VGUtility.isEmpty(assetDto.getManagerId()))
            andList.add(builder.equal(root.get("managerId"), assetDto.getManagerId()));
        if (!VGUtility.isEmpty(assetDto.getUseDeptId()))
            andList.add(builder.equal(root.get("useDeptId"), assetDto.getUseDeptId()));
        if (!VGUtility.isEmpty(assetDto.getUserId()))
            andList.add(builder.equal(root.get("userId"), assetDto.getUserId()));
        if (!VGUtility.isEmpty(assetDto.getSavePlaceId()))
            andList.add(builder.equal(root.get("savePlaceId"), assetDto.getSavePlaceId()));
        if (!VGUtility.isEmpty(assetDto.getMaterialCode()))
            andList.add(builder.equal(root.get("materialCode"), assetDto.getMaterialCode()));
        if (!VGUtility.isEmpty(assetDto.getDeActiveTimeStr())) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.YEAR, -1);
            Date y = c.getTime();
            andList.add(builder.lessThanOrEqualTo(root.get("deActiveTime"), y));
        }
        //
        // Predicate
        // predicateByDictDto=(Predicate)baseService.getPredicateByDictDto(builder,
        // root, assetDto);
        // if(!VGUtility.isEmpty(predicateByDictDto)) {
        // andList.add(predicateByDictDto);
        // }
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }

    @Override
    public AssetInventoryDto getAssetInventoryById(String id) {
        if (VGUtility.isEmpty(id))
            throw new RuntimeException("选择的盘点单不存在！");
        AssetInventoryModel assetInventoryModel = assetInventoryDao.findById(id).get();
        return assetInventoryModel == null ? null : convertAssetInventoryModelToDto(assetInventoryModel);
    }

    /******* 资产盘点子表查询 start *****/
    @Override
    public List<AssetInventoryScopeDto> getAssetInventoryScopeDtoList(String assetInventoryId) {
        String hql = "from AssetInventoryScopeModel where AssetInventoryModel_ID='" + assetInventoryId
                + "' order by createTimestamp desc";
        TypedQuery<AssetInventoryScopeModel> query = entityManager.createQuery(hql, AssetInventoryScopeModel.class);
        List<AssetInventoryScopeModel> modelList = query.getResultList();
        List<AssetInventoryScopeDto> dto = new ArrayList<AssetInventoryScopeDto>();
        for (AssetInventoryScopeModel model : modelList) {
            dto.add(convertAssetInventoryScopeModelToDto(model));
        }
        return dto;
    }

    /******* 根据盘点任务ID获取所有我的盘点单 *****/
    @Override
    public List<MyAssetInventoryDto> getMyAssetInventoryDtoList(String assetInventoryId) {
        String hql = "from MyAssetInventoryModel where AssetInventoryModel_ID='" + assetInventoryId
                + "' order by createTimestamp desc";
        TypedQuery<MyAssetInventoryModel> query = entityManager.createQuery(hql, MyAssetInventoryModel.class);
        List<MyAssetInventoryModel> modelList = query.getResultList();
        List<MyAssetInventoryDto> dto = new ArrayList<MyAssetInventoryDto>();
        for (MyAssetInventoryModel model : modelList) {
            dto.add(convertMyAssetInventoryModelToDto(model));
        }
        return dto;
    }

    @Override // 通过ID删除盘点范围
    public void deleteAssetInventoryScopeById(String assetInventoryScopeId) {
        if (VGUtility.isEmpty(assetInventoryScopeId))
            throw new RuntimeException("选择的盘点任务为空！");
        assetInventoryScopeDao.deleteById(assetInventoryScopeId);
    }

    @Override // 通过ID删除盘点清单；
    public void deleteAssetInventoryTempById(String assetInventoryTempId) {
        if (VGUtility.isEmpty(assetInventoryTempId))
            throw new RuntimeException("选择的我的盘点任务单为空！");
        assetInventoryTempDao.deleteById(assetInventoryTempId);
    }

    @Override // 通过IDs批量删除盘点清单；
    public void deleteAssetInventoryTempByIds(String assetInventoryTempIds) {
        if (VGUtility.isEmpty(assetInventoryTempIds))
            throw new RuntimeException("必须至少选中一个要删除的资产！");
        List<String> inventoryTempIds = new ArrayList<String>();
        Arrays.stream(assetInventoryTempIds.split(",")).forEach(arr -> inventoryTempIds.add(arr));

        for (String inventoryTempId : inventoryTempIds) {
            AssetInventoryTempModel model = assetInventoryTempDao.findById(inventoryTempId).get();
            AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
            if (VGUtility.isEmpty(asset))
                throw new RuntimeException("该资产数据已被删除！");
            // asset.setAssetStatusStr(String.valueOf(ASSET_STATUS.闲置.ordinal()));
            // asset.setBeforeChangeAssetStatusStr(null);
            // assetService.updateAssetBeforeAssetStatus(asset);

            assetInventoryTempDao.deleteById(inventoryTempId);
        }
    }

    /*
     * 获取当前盘点任务中 加入盘点的资产IDs
     */
    @Override
    public Set<String> getAssetIdFromInventoryTemp(String assetInventoryModel_ID) {
        HashSet<String> set = new HashSet<String>();
        if (VGUtility.isEmpty(assetInventoryModel_ID))
            return set;
        List<AssetInventoryTempModel> list = assetInventoryTempDao.findByassetInventoryModelId(assetInventoryModel_ID);
        if (VGUtility.isEmpty(list) && list.size() <= 0)
            return set;
        List<String> newList = list.stream().map(AssetInventoryTempModel::getAssetId).collect(Collectors.toList());
        set.addAll(newList);
        return set;
    }

    /*
     * 新增盘点范围
     */
    @Override
    public void addAssetInventoryScope(AssetInventoryScopeDto assetInventoryScopeDto) {
        if (VGUtility.isEmpty(assetInventoryScopeDto.getAssetInventoryId()))
            throw new RuntimeException("盘点的任務单号不能为空！");
        AssetInventoryModel model = assetInventoryDao.findById(assetInventoryScopeDto.getAssetInventoryId()).get();
        if (model.getInventoryStatus() != FlowableInfo.WORKSTATUS.拟稿) {
            throw new RuntimeException("非拟稿状态不能编辑盘点任務！");
        }
        if (!VGUtility.isEmpty(assetInventoryScopeDto.getInventoryContent())) {
            AssetInventoryScopeModel aScopeModel = convertAssetInventoryScopeDtoToModel(assetInventoryScopeDto);
            aScopeModel.setCreateTimestamp(new Date());
            aScopeModel.setAssetInventoryModel(model);
            assetInventoryScopeDao.save(aScopeModel);
        }

    }

    /*
     * 新增盘点清单
     */
    @Override
    public void addAssetInventoryTemp(String assetInventoryId, String assetIdListStr) {
        List<String> assetIdList = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdListStr))
            Arrays.stream(assetIdListStr.split(",")).forEach(arr -> assetIdList.add(arr));

        AssetInventoryModel model = assetInventoryDao.findById(assetInventoryId).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("该盘点任务单已被删除！");
        if (model.getInventoryStatus() != FlowableInfo.WORKSTATUS.拟稿) {
            throw new RuntimeException("非拟稿状态不能编辑盘点任務！");
        }

        for (String assetId : assetIdList) {
            AssetAssetDto assetDto = assetService.getAssetByAssetId(assetId);
            if (VGUtility.isEmpty(assetDto))
                throw new RuntimeException("该资产数据已被删除！");

            AssetInventoryTempModel aTempModel = new AssetInventoryTempModel();
            if (!VGUtility.isEmpty(assetDto.getId()))
                aTempModel.setAssetId(assetDto.getId());
            if (!VGUtility.isEmpty(assetDto.getManageDeptId()))
                aTempModel.setManageDeptId(assetDto.getManageDeptId());

            aTempModel.setOperation(INVENTORY_OPERATION.手工录入);
            aTempModel.setAssetInventoryModel(model);
            aTempModel.setCreateTimestamp(new Date());
            assetInventoryTempDao.save(aTempModel);
        }
    }

    @Override
    public AssetInventoryDto updateAssetInventory(AssetInventoryDto dto) {
        if (VGUtility.isEmpty(dto.getId()))
            throw new RuntimeException("盘点的任務单号不能为空！");
        AssetInventoryModel oldModel = assetInventoryDao.findById(dto.getId()).get();
        if (oldModel.getInventoryStatus() != FlowableInfo.WORKSTATUS.拟稿) {
            throw new RuntimeException("非拟稿状态不能编辑盘点任務！");
        }
        // 统计数量;
        double amount = 0;
        // 通过任务Id查询盘点范围列表；
        List<AssetInventoryScopeModel> ScopeModelList = assetInventoryScopeDao.findAssetByInventoryModelId(dto.getId());
        if (!VGUtility.isEmpty(ScopeModelList)) {
            for (AssetInventoryScopeModel e : ScopeModelList) {
                amount += e.getInventoryNum();
            }
        }
        // 通过任务Id获取当前盘点任务中 盘点清单;
        List<AssetInventoryTempModel> assetModelList = assetInventoryTempDao.findByassetInventoryModelId(dto.getId());
        if (!VGUtility.isEmpty(assetModelList)) {
            amount += assetModelList.size();
        }
        // 该任务单下不存在被盘点的资产；
        if (ScopeModelList.size() <= 0 && assetModelList.size() <= 0)
            throw new RuntimeException("必须存在被盘点的资产！");
        dto.setQuantity(amount);
        AssetInventoryModel model = convertAssetInventoryDtoToModel(dto, oldModel);
        return convertAssetInventoryModelToDto(assetInventoryDao.save(model));
    }

    @Override
    public AssetInventoryDto saveAndDistributionInventory(List<AssetAssetDto> assetDtoList,
                                                          ArrayList<AssetInventoryScopeDto> assetInventoryScopeDtos, UserInfoDto userInfoDto, AssetInventoryDto dto) {
        // 创建盘点单；
        AssetInventoryDto createInventory = createInventory(assetDtoList, assetInventoryScopeDtos, userInfoDto, dto);
        // 发布盘点单；
        distributionInventory(createInventory.getId());
        return createInventory;
    }

    /*
     * 查询我的盘点清单
     *
     */
    @Override
    public Map<String, Object> getMyAssetInventoryForDataGrid(String assetInventoryId, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "createTimestamp"));
        Page<MyAssetInventoryModel> list = myAssetInventoryModelDao.findByassetInventoryModel(assetInventoryId,
                pageRequest);

        List<MyAssetInventoryDto> dtolist = new ArrayList<MyAssetInventoryDto>();
        list.getContent().stream().forEach(e -> {
            dtolist.add(convertMyAssetInventoryModelToDto(e));
        });
        Map<String, java.lang.Object> map = new HashMap<String, java.lang.Object>();
        long total = list.getTotalElements();
        map.put("total", total);
        map.put("rows", dtolist);
        return map;
    }

    @Override // 保存并发布盘点单
    public AssetInventoryDto createAndDistributionInventory(List<AssetAssetDto> assetDtoList,
                                                            ArrayList<AssetInventoryScopeDto> assetInventoryScopeDtos, UserInfoDto userInfoDto, AssetInventoryDto dto) {
        AssetInventoryDto inventory = createInventory(assetDtoList, assetInventoryScopeDtos, userInfoDto, dto);
        distributionInventory(inventory.getId());
        return inventory;
    }

    @Override // 更新并发布盘点单任务
    public void updateAndDistributionAssetInventory(AssetInventoryDto assetInventoryDto) {
        AssetInventoryDto assetInventory = updateAssetInventory(assetInventoryDto);
        distributionInventory(assetInventory.getId());
    }

    @Override
    public Map<String, Object> getAssetInventory(AssetInventoryDto dto, PageableDto pageableDto) {
        Pageable pageable = PageRequest.of(pageableDto.getPage() - 1, pageableDto.getSize(), Sort.Direction.DESC,
                "createTimestamp");
        AssetInventoryModel assetInventoryModel = new AssetInventoryModel();
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        assetInventoryModel.setCreateId(userInfoDto.getId());

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("inventoryLoss").withIgnorePaths("quantity")
                .withIgnorePaths("inventoryProfit")// 忽略属性：是否关注。因为是基本类型，需要忽略掉
                .withMatcher("inventoryRunningNum", GenericPropertyMatchers.contains())// 全部模糊查询，即%{inventoryRunningNum}%
                .withMatcher("inventoryName", GenericPropertyMatchers.contains());

        if (!VGUtility.isEmpty(dto.getInventoryRunningNum())) {
            assetInventoryModel.setInventoryRunningNum(dto.getInventoryRunningNum());
        }
        if (!VGUtility.isEmpty(dto.getInventoryName())) {
            assetInventoryModel.setInventoryName(dto.getInventoryName());
        }
        if (!VGUtility.isEmpty(dto.getInventoryStatusStr())) {
            assetInventoryModel
                    .setInventoryStatus(FlowableInfo.WORKSTATUS.values()[dto.getInventoryStatusStr()]);
        }

        Example<AssetInventoryModel> example = Example.of(assetInventoryModel, matcher);
        Page<AssetInventoryModel> list = assetInventoryDao.findAll(example, pageable);
        List<AssetInventoryDto> dtolist = new ArrayList<AssetInventoryDto>();
        list.getContent().stream().forEach(e -> {
            dtolist.add(convertAssetInventoryModelToDto(e));
        });
        Map<String, java.lang.Object> map = new HashMap<String, java.lang.Object>();
        long total = list.getTotalElements();
        map.put("total", total);
        map.put("rows", dtolist);
        return map;
    }

    /**
     * 盘点范围中是否存在资产
     *
     * @param assetInventoryScopeDtos
     * @return
     */
    private boolean inventoryScopeExistAsset(List<AssetInventoryScopeDto> assetInventoryScopeDtos) {
        boolean flag = true;
        if (VGUtility.isEmpty(assetInventoryScopeDtos))
            return flag;
        for (AssetInventoryScopeDto assetInventoryScopeDto : assetInventoryScopeDtos) {
            double inventoryNum = assetInventoryScopeDto.getInventoryNum();
            if ((int) inventoryNum > 0) {
                flag = false;
                return flag;
            }
        }
        return flag;

    }

    /*
     * 执行发起审批逻辑
     */
    @Override
    public void doApproval(String assetInventoryId) {
        /*if (VGUtility.isEmpty(assetInventoryId))
            throw new RuntimeException("盘点的任務单号不能为空！");
        AssetInventoryModel model = assetInventoryDao.findById(assetInventoryId).get();
        if (model.getInventoryStatus() !=  FlowableInfo.WORKSTATUS.盘点报告待审批) {
            throw new RuntimeException("非盘点报告待审批状态不能发起审批！");
        }
        model.setInventoryStatus( FlowableInfo.WORKSTATUS.盘点完成);
        assetInventoryDao.save(model);
        // 更新我的盘点单状态为已完成;
        List<MyAssetInventoryModel> MyAssetInventoryModels = myAssetInventoryModelDao
                .findByInventoryId(assetInventoryId);
        if (!VGUtility.isEmpty(MyAssetInventoryModels)) {
            MyAssetInventoryModels.stream().forEach(e -> {
                e.setInventoryStatus( FlowableInfo.WORKSTATUS.已完成);
                myAssetInventoryModelDao.save(e);

            });
        }
        // 生成盘点处理记录
        productInventoryHistory(assetInventoryId);*/

    }

    /**
     * 生成盘点处理记录
     *
     * @param assetInventoryId
     */
    private void productInventoryHistory(String assetInventoryId) {
        List<AssetInventoryTempModel> AssetInventoryTempModels = assetInventoryTempDao
                .findByassetInventoryModelId(assetInventoryId);
        if (!VGUtility.isEmpty(AssetInventoryTempModels)) {
            AssetInventoryTempModels.stream().forEach(e -> {
                AssetHistoryDto historyDto = new AssetHistoryDto();
                historyDto.setAssetModelId(e.getAssetId());
                historyDto.setHistoryType(HISTORY_TYPE.盘点记录);
                historyDto.setTakeStockResult("与台账相符");
                UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
                historyDto.setCreateUserId(userInfoDto.getId());
                assetService.createHistory(historyDto);
            });
        }
    }

    @Override
    public AssetApprove doApproveNotify(String id) {
        List<String> assetIdList = assetInventoryTempService.getAssetIdList(id);
        //校验资产状态，是否可以审批；
//        assetService.doAssetApproveCheck(assetIdList, ASSET_STATUS.调拨);
        AssetApprove approve = new AssetApprove();
        approve.setCanApprove(false);
        if (null != assetIdList && assetIdList.size() > 0) {
            approve.setCanApprove(true);
        }
        return approve;
    }


    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        List<String> assetIdList;
        switch (workStatus) {
            case 驳回:
                //资产状态回滚
//                assetIdList = assetInventoryTempService.getAssetIdList(id);
//                assetService.approveFailureAssetStateRollback(assetIdList);
                break;
            case 已审批:
                //发布任务到我的盘点单中；
                this.distributionInventory(id);
                break;
            default:
                break;

        }
        //更新主表状态
        AssetInventoryModel model = assetInventoryDao.findById(id).get();
        model.setInventoryStatus(workStatus);

    }
}
