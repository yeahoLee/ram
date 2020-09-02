package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dao.AssetInventoryDao;
import com.mine.product.czmtr.ram.asset.dao.AssetInventoryTempDao;
import com.mine.product.czmtr.ram.asset.dao.MyAssetInventoryModelDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetInventoryModel;
import com.mine.product.czmtr.ram.asset.model.AssetInventoryTempModel;
import com.mine.product.czmtr.ram.asset.model.MyAssetInventoryModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.*;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetInventoryTempService implements IAssetInventoryTempService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IAssetInventoryService assetInventoryService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private AssetInventoryTempDao assetInventoryTempDao;
    @Autowired
    private MyAssetInventoryModelDao myAssetInventoryModelDao;
    @Autowired
    private AssetInventoryDao assetInventoryDao;
    @Autowired
    private AssetAssetDao assetDao; //资产

    @Override
    public List<String> getAssetIdList(String id) {
        List<AssetInventoryTempModel> dtolist = assetInventoryTempDao.findByassetInventoryModelId(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        return dtolist.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
    }

    /***
     * 更新盘点清单,返回我的盘点单详情,用于在前台显示盘亏和盘盈数量
     */
    @Override
    public void UpdateAssetInventoryTempModel(AssetInventoryTempDto dto) {
        AssetInventoryTempModel assetInventoryTempModel = assetInventoryTempDao.findById(dto.getId()).get();
        MyAssetInventoryModel myAssetInventoryModel = myAssetInventoryModelDao.findById(assetInventoryTempModel.getMyAssetInventoryModel().getId()).get();
        if (VGUtility.isEmpty(assetInventoryTempModel))
            throw new RuntimeException("资产清单不存在!");

        if (!VGUtility.isEmpty(dto.getInventoryWay()))
            assetInventoryTempModel.setInventoryWay(INVENTORY_WAY.valueOf(INVENTORY_WAY.class, (dto.getInventoryWay())));
        if (!VGUtility.isEmpty(dto.getOperation()))
            assetInventoryTempModel.setOperation(INVENTORY_OPERATION.valueOf(INVENTORY_OPERATION.class, (dto.getOperation())));
        if (!VGUtility.isEmpty(dto.getResult())) {
            switch (dto.getResult()) {
                case "盘亏":
                    if (!VGUtility.isEmpty(assetInventoryTempModel.getResult())) {
                        if (assetInventoryTempModel.getResult().ordinal() == 1) {
                            //更新我的盘点单中盘亏盘盈数量
                            if (VGUtility.isEmpty(myAssetInventoryModel.getInventoryLoss()))
                                myAssetInventoryModel.setInventoryLoss(1);
                            else
                                myAssetInventoryModel.setInventoryLoss(myAssetInventoryModel.getInventoryLoss() + 1);
                            if (!VGUtility.isEmpty(myAssetInventoryModel.getInventoryProfit()) && myAssetInventoryModel.getInventoryProfit() != 0)
                                myAssetInventoryModel.setInventoryProfit(myAssetInventoryModel.getInventoryProfit() - 1);

                        } else if (assetInventoryTempModel.getResult().ordinal() == 2) {
                            //更新我的盘点单中盘亏盘盈数量
                            if (VGUtility.isEmpty(myAssetInventoryModel.getInventoryLoss()))
                                myAssetInventoryModel.setInventoryLoss(1);
                            else
                                myAssetInventoryModel.setInventoryLoss(myAssetInventoryModel.getInventoryLoss() + 1);
                        }
                    } else {
                        //更新我的盘点单中盘亏盘盈数量
                        if (VGUtility.isEmpty(myAssetInventoryModel.getInventoryLoss()))
                            myAssetInventoryModel.setInventoryLoss(1);
                        else
                            myAssetInventoryModel.setInventoryLoss(myAssetInventoryModel.getInventoryLoss() + 1);
                    }
                    break;
                case "盘盈":
                    if (!VGUtility.isEmpty(assetInventoryTempModel.getResult())) {
                        if (assetInventoryTempModel.getResult().ordinal() == 0) {
                            if (!VGUtility.isEmpty(myAssetInventoryModel.getInventoryLoss()))
                                myAssetInventoryModel.setInventoryLoss(myAssetInventoryModel.getInventoryLoss() - 1);
                            if (VGUtility.isEmpty(myAssetInventoryModel.getInventoryProfit()))
                                myAssetInventoryModel.setInventoryProfit(1);
                            else
                                myAssetInventoryModel.setInventoryProfit(myAssetInventoryModel.getInventoryProfit() + 1);

                        } else if (assetInventoryTempModel.getResult().ordinal() == 2) {
                            if (VGUtility.isEmpty(myAssetInventoryModel.getInventoryProfit()))
                                myAssetInventoryModel.setInventoryProfit(1);
                            else
                                myAssetInventoryModel.setInventoryProfit(myAssetInventoryModel.getInventoryProfit() + 1);

                        }
                    } else {
                        if (VGUtility.isEmpty(myAssetInventoryModel.getInventoryProfit()))
                            myAssetInventoryModel.setInventoryProfit(1);
                        else
                            myAssetInventoryModel.setInventoryProfit(myAssetInventoryModel.getInventoryProfit() + 1);
                    }
                    break;
                case "相符":
                    if (!VGUtility.isEmpty(assetInventoryTempModel.getResult())) {
                        if (assetInventoryTempModel.getResult().ordinal() == 0) {
                            if (!VGUtility.isEmpty(myAssetInventoryModel.getInventoryLoss()))
                                myAssetInventoryModel.setInventoryLoss(myAssetInventoryModel.getInventoryLoss() - 1);
                        } else if (assetInventoryTempModel.getResult().ordinal() == 1) {
                            if (!VGUtility.isEmpty(myAssetInventoryModel.getInventoryProfit()))
                                myAssetInventoryModel.setInventoryProfit(myAssetInventoryModel.getInventoryProfit() - 1);
                        }
                    }
                    break;
            }
            assetInventoryTempModel.setResult(INVENTORY_RESULT.valueOf(INVENTORY_RESULT.class, (dto.getResult())));
            myAssetInventoryModelDao.save(myAssetInventoryModel);
        }
        assetInventoryTempModel.setRemark(dto.getRemark());
        assetInventoryTempDao.save(assetInventoryTempModel);
    }

    /***
     * 更新我的资产清单
     * @param dto
     */
    @Override
    public void UpdateMyAssetInventory(MyAssetInventoryDto dto) {
        MyAssetInventoryModel model = myAssetInventoryModelDao.findById(dto.getId()).get();

        List<AssetInventoryTempModel> list = assetInventoryTempDao.findBymyAssetInventoryModel(model);
        if (list.size() <= 0)
            throw new RuntimeException("资产清单不能为空!");

        if (!VGUtility.isEmpty(dto.getMyAssetInventoryCode()))
            model.setAssetInventoryCode(dto.getMyAssetInventoryCode());
        if (!VGUtility.isEmpty(dto.getDisposalAdvice()))
            model.setDisposalAdvice(dto.getDisposalAdvice());
        if (!VGUtility.isEmpty(dto.getInventoryLoss()) && dto.getInventoryLoss() != 0.0)
            model.setInventoryLoss(dto.getInventoryLoss());
        if (!VGUtility.isEmpty(dto.getInventoryProfit()) && dto.getInventoryLoss() != 0.0)
            model.setInventoryProfit(dto.getInventoryProfit());
        if (!VGUtility.isEmpty(dto.getQuantity()) && dto.getInventoryLoss() != 0.0)
            model.setQuantity(dto.getQuantity());
        if (!VGUtility.isEmpty(dto.getReason()))
            model.setReason(dto.getReason());
        myAssetInventoryModelDao.save(model);
    }

    /***
     * 判断我的盘点单是否可以提交
     * @param dto
     * @return
     */
    @Override
    public Boolean checkForSubmit(MyAssetInventoryDto dto) {
        boolean flag = true;
        MyAssetInventoryModel model = myAssetInventoryModelDao.findById(dto.getId()).get();

        List<AssetInventoryTempModel> list = assetInventoryTempDao.findBymyAssetInventoryModel(model);

        for (AssetInventoryTempModel temp : list) {
            if (VGUtility.isEmpty(temp.getResult())) {
                flag = false;
                break;
            }
        }

        return flag;
    }

    /***
     * 更新我的盘点单状态
     * @param dto
     */
    @Override
    public void UpdateMyAssetInventoryStatus(MyAssetInventoryDto dto) {
        MyAssetInventoryModel model = myAssetInventoryModelDao.findById(dto.getId()).get();

        /*Map<String, Object> map = getAssetInventoryTempForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate = null;
                predicate = builder.equal(root.get("myAssetInventoryModel"), model);
                return predicate;
            }
        }, null);
        if (VGUtility.isEmpty(map))
            throw new RuntimeException("资产清单不能为空!");

        if (!VGUtility.isEmpty(dto.getMyinventoryStatusStr())) {
            model.setInventoryStatus( FlowableInfo.WORKSTATUS.values()[VGUtility.toInteger(dto.getMyinventoryStatusStr())]);
        }
        myAssetInventoryModelDao.save(model);*/

        //更新盘点进度
        AssetInventoryModel assetInventoryModel = assetInventoryDao.findById(model.getAssetInventoryModel().getId()).get();
        assetInventoryModel.setInventoryProfit(assetInventoryModel.getInventoryProfit() + model.getInventoryProfit());
        assetInventoryModel.setInventoryLoss(assetInventoryModel.getInventoryLoss() + model.getInventoryLoss());
        List<MyAssetInventoryDto> mylist = assetInventoryService.getMyAssetInventoryDtoList(model.getAssetInventoryModel().getId());
        if (VGUtility.isEmpty(assetInventoryModel.getInventoryProcess())) {
            String str = "1/" + mylist.size();
            assetInventoryModel.setInventoryProcess(str);
        } else {
            String process = assetInventoryModel.getInventoryProcess();
            int num = VGUtility.toInteger(process.split("/")[0]);
            int total = VGUtility.toInteger(process.split("/")[1]);
            if (num < total) {
                String str = (num + 1) + "/" + total;
                assetInventoryModel.setInventoryProcess(str);
            }

            if ((num + 1) == total) {
//                assetInventoryModel.setInventoryStatus( FlowableInfo.WORKSTATUS.盘点结束);
                assetInventoryModel.setInventoryProcess("盘点已完成");
            }
        }

        assetInventoryDao.save(assetInventoryModel);
    }

    /***
     * 撤回我的盘点单
     * @param dto
     */
    @Override
    public void RecallMyInventory(MyAssetInventoryDto dto) {
        MyAssetInventoryModel model = myAssetInventoryModelDao.findById(dto.getId()).get();

        Map<String, Object> map = getAssetInventoryTempForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate = null;
                //if(!VGUtility.isEmpty(dto.getMyAssetInventoryCode()))
                predicate = builder.equal(root.get("myAssetInventoryModel"), model);
                return predicate;
            }
        }, null);
        if (VGUtility.isEmpty(map))
            throw new RuntimeException("资产清单不能为空!");

        if (!VGUtility.isEmpty(dto.getMyinventoryStatusStr())) {
            model.setInventoryStatus( FlowableInfo.WORKSTATUS.values()[VGUtility.toInteger(dto.getMyinventoryStatusStr())]);
        }
        myAssetInventoryModelDao.save(model);

        //更新盘点进度
        AssetInventoryModel assetInventoryModel = assetInventoryDao.findById(model.getAssetInventoryModel().getId()).get();
        assetInventoryModel.setInventoryProfit(assetInventoryModel.getInventoryProfit() - model.getInventoryProfit());
        assetInventoryModel.setInventoryLoss(assetInventoryModel.getInventoryLoss() - model.getInventoryLoss());
        List<MyAssetInventoryDto> mylist = assetInventoryService.getMyAssetInventoryDtoList(model.getAssetInventoryModel().getId());
        if (VGUtility.isEmpty(assetInventoryModel.getInventoryProcess())) {
            String str = "0/" + mylist.size();
            assetInventoryModel.setInventoryProcess(str);
        } else {
            String process = assetInventoryModel.getInventoryProcess();
            int num = VGUtility.toInteger(process.split("/")[0]);
            int total = VGUtility.toInteger(process.split("/")[1]);
            if (num < total) {
                String str = (num - 1) + "/" + total;
                assetInventoryModel.setInventoryProcess(str);
            }
        }

        assetInventoryDao.save(assetInventoryModel);
    }


    /***
     * 获取我的盘点清单
     * @param searchExpression
     * @param pageableDto
     * @return
     */
    @Override
    public Map<String, Object> getAssetInventoryTempForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        List<AssetInventoryTempDto> dtoList = new ArrayList<AssetInventoryTempDto>();
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetInventoryTempModel> assetinventoryCriteria = builder.createQuery(AssetInventoryTempModel.class);
        Root<AssetInventoryTempModel> root = assetinventoryCriteria.from(AssetInventoryTempModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetinventoryCriteria.where((Predicate) predicate);
        assetinventoryCriteria.orderBy(builder.asc(root.get("result")));
        Query<AssetInventoryTempModel> query = session.createQuery(assetinventoryCriteria);

        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());

        List<AssetInventoryTempModel> modelList = query.getResultList();

        for (AssetInventoryTempModel AssetInventoryTempModel : modelList) {
            dtoList.add(convertAssetInventoryTempModelToDto(AssetInventoryTempModel));
        }

        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetInventoryTempModel.class);
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
     * @param dto
     * @param pageableDto
     * @return
     */
    @Override
    public Map<String, Object> getAssetInventoryTempForDataGridByDto(AssetInventoryTempDto dto, PageableDto pageableDto) {
        List<AssetInventoryTempDto> dtoList = new ArrayList<AssetInventoryTempDto>();
        Pageable page = PageRequest.of(pageableDto.getPage() - 1, pageableDto.getSize());
        Page<AssetInventoryTempModel> modelList = assetInventoryTempDao.findAll(new Specification<AssetInventoryTempModel>() {
            @Override
            public Predicate toPredicate(Root<AssetInventoryTempModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!VGUtility.isEmpty(dto.getManageDeptId()))
                    predicateList.add(criteriaBuilder.equal(root.get("manageDeptId"), dto.getManageDeptId()));
                if (!VGUtility.isEmpty(dto.getAssetInventoryId()))
                    predicateList.add(criteriaBuilder.equal(root.get("assetInventoryModel"), assetInventoryDao.getOne(dto.getAssetInventoryId())));
                if (!VGUtility.isEmpty(dto.getMyAssetInventoryId()))
                    predicateList.add(criteriaBuilder.equal(root.get("myAssetInventoryModel"), myAssetInventoryModelDao.getOne(dto.getMyAssetInventoryId())));

                if (predicateList.size() > 0)
                    return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
                else
                    return null;
            }
        }, page);
        for (AssetInventoryTempModel tempModel : modelList.getContent()) {
            dtoList.add(convertAssetInventoryTempModelToDto(tempModel));
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", modelList.getTotalElements());
        return modelMap;
    }

    public List<AssetInventoryTempDto> convertForHtml(List<Object> resultList) {
        List<AssetInventoryTempDto> list = new ArrayList<AssetInventoryTempDto>();
        if (!VGUtility.isEmpty(resultList)) {
            for (Object obj : resultList) {
                Object[] objs = (Object[]) obj;
                AssetInventoryTempDto temp = new AssetInventoryTempDto();
                AssetAssetDto dto = new AssetAssetDto();
                if (!VGUtility.isEmpty(objs[1]))
                    dto = assetService.getAssetByAssetId(objs[1].toString());

                if (!VGUtility.isEmpty(objs[0]))
                    temp.setId(objs[0].toString());

                if (!VGUtility.isEmpty(objs[7]))
                    temp.setAssetInventoryId(objs[7].toString());

                temp.setAssetCode(dto.getAssetCode());
                temp.setAssetTypeStr(dto.getAssetTypeStr());
                temp.setSpecAndModels(dto.getSpecAndModels());
                temp.setUnitOfMeasStr(dto.getUnitOfMeasStr());
                if (!VGUtility.isEmpty(dto.getUnitOfMeasId())) {
                    DictDto dictCommonDto = dictService.getCommonCode(dto.getUnitOfMeasId());
                    if (!VGUtility.isEmpty(dictCommonDto))
                        temp.setUnitOfMeasStr(dictCommonDto.getChsName());
                }
                double equiOrigValue = Double.parseDouble(dto.getEquiOrigValue());
                String equiOrigValueStr = new String();
                if (equiOrigValue > 0)
                    equiOrigValueStr = VGUtility.toDoubleStr(equiOrigValue, "0.##");
                else
                    equiOrigValueStr = "";
                temp.setEquiOrigValue(equiOrigValueStr);

                if (!VGUtility.isEmpty(objs[4])) {
                    temp.setOperation(objs[4].toString());
                    temp.setOperationStr(INVENTORY_OPERATION.values()[VGUtility.toInteger(objs[4].toString())].name());
                }

                if (!VGUtility.isEmpty(objs[2])) {
                    temp.setInventoryWay(objs[2].toString());
                    temp.setInventoryWayStr(INVENTORY_WAY.values()[VGUtility.toInteger(objs[2].toString())].name());
                }

                if (!VGUtility.isEmpty(objs[6])) {
                    temp.setResult(objs[6].toString());
                    temp.setResultStr(INVENTORY_RESULT.values()[VGUtility.toInteger(objs[6].toString())].name());
                }

                if (!VGUtility.isEmpty(objs[5]))
                    temp.setRemark(objs[5].toString());

                if (!VGUtility.isEmpty(objs[8])) {
                    temp.setMyAssetInventoryId(objs[8].toString());
                }
                temp.setBuyDate(dto.getBuyDate());
                if (!VGUtility.isEmpty(dto.getManageDeptId())) {
                    try {
                        DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(dto.getManageDeptId());
                        if (!VGUtility.isEmpty(deptDto))
                            temp.setManageDeptStr(deptDto.getDeptName());
                    } catch (Exception e) {
                    }
                }
                if (!VGUtility.isEmpty(dto.getManagerId())) {
                    try {
                        UserInfoDto userDto = userService.getUserInfo(dto.getManagerId());
                        if (!VGUtility.isEmpty(userDto))
                            temp.setManagerStr(userDto.getChsName());
                    } catch (Exception e) {
                    }
                }
                temp.setAssetId(dto.getId());
                if (!VGUtility.isEmpty(dto.getAssetStatusStr()))
                    temp.setAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(dto.getAssetStatusStr())].toString());

                if (!VGUtility.isEmpty(temp.getId()))
                    temp.setId(temp.getId());

                //根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
                if (!VGUtility.isEmpty(dto.getSavePlaceId())) {
                    try {
                        DictDto savePlaceDto = dictService.getCommonCode(dto.getSavePlaceId());
                        if (!VGUtility.isEmpty(savePlaceDto)) {
                            temp.setSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
                        }
                    } catch (Exception e) {
                    }
                }

                list.add(temp);
            }
        }

        return list;
    }

    /***
     * 将我的盘点model转换成dto
     * @param model
     * @return
     */
    public AssetInventoryTempDto convertAssetInventoryTempModelToDto(AssetInventoryTempModel model) {
        AssetInventoryTempDto temp = new AssetInventoryTempDto();
        AssetAssetDto dto = assetService.getAssetByAssetId(model.getAssetId());
        temp.setId(model.getId());
        if (!VGUtility.isEmpty(model.getAssetInventoryModel()))
            temp.setAssetInventoryId(model.getAssetInventoryModel().getId());
        temp.setAssetCode(dto.getAssetCode());
        temp.setAssetTypeStr(dto.getAssetTypeStr());
        temp.setSpecAndModels(dto.getSpecAndModels());
        temp.setUnitOfMeasStr(dto.getUnitOfMeasStr());
        if (!VGUtility.isEmpty(dto.getUnitOfMeasId())) {
            DictDto dictCommonDto = dictService.getCommonCode(dto.getUnitOfMeasId());
            if (!VGUtility.isEmpty(dictCommonDto))
                temp.setUnitOfMeasStr(dictCommonDto.getChsName());
        }
        double equiOrigValue = Double.parseDouble(dto.getEquiOrigValue());
        String equiOrigValueStr = new String();
        if (equiOrigValue > 0)
            equiOrigValueStr = VGUtility.toDoubleStr(equiOrigValue, "0.##");
        else
            equiOrigValueStr = "";
        temp.setEquiOrigValue(equiOrigValueStr);

        if (!VGUtility.isEmpty(model.getOperation())) {
            temp.setOperation(String.valueOf(model.getOperation().ordinal()));
            temp.setOperationStr(model.getOperation().toString());
        }

        if (!VGUtility.isEmpty(model.getInventoryWay())) {
            temp.setInventoryWay(String.valueOf(model.getInventoryWay().ordinal()));
            temp.setInventoryWayStr(model.getInventoryWay().toString());
        }

        if (!VGUtility.isEmpty(model.getResult())) {
            temp.setResult(String.valueOf(model.getResult().ordinal()));
            temp.setResultStr(model.getResult().toString());
        }
        temp.setRemark(model.getRemark());
        if (!VGUtility.isEmpty(model.getMyAssetInventoryModel())) {
            if (!VGUtility.isEmpty(model.getMyAssetInventoryModel().getId()))
                temp.setMyAssetInventoryId(model.getMyAssetInventoryModel().getId());
        }
        temp.setBuyDate(dto.getBuyDate());
        if (!VGUtility.isEmpty(dto.getManageDeptId())) {
            try {
                DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(dto.getManageDeptId());
                if (!VGUtility.isEmpty(deptDto))
                    temp.setManageDeptStr(deptDto.getDeptName());
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(dto.getManagerId())) {
            try {
                UserInfoDto userDto = userService.getUserInfo(dto.getManagerId());
                if (!VGUtility.isEmpty(userDto))
                    temp.setManagerStr(userDto.getChsName());
            } catch (Exception e) {
            }
        }
        temp.setAssetId(dto.getId());
        if (!VGUtility.isEmpty(dto.getAssetStatusStr()))
            temp.setAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(dto.getAssetStatusStr())].toString());

        if (!VGUtility.isEmpty(temp.getId()))
            temp.setId(temp.getId());

        //根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        if (!VGUtility.isEmpty(dto.getSavePlaceId())) {
            try {
                DictDto savePlaceDto = dictService.getCommonCode(dto.getSavePlaceId());
                if (!VGUtility.isEmpty(savePlaceDto)) {
                    temp.setSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
                }
            } catch (Exception e) {
            }
        }

        return temp;
    }

    /***************************我的盘点单********************************************************/
    @Override
    public List<String> assetInventoryList(String myAssetInventoryId) {
        List<String> list = new ArrayList<String>();
        if (VGUtility.isEmpty(myAssetInventoryId))
            throw new RuntimeException("资产清单不能为空！");
        else {
            try {
                myAssetInventoryModelDao.findById(myAssetInventoryId).get();
            } catch (Exception ex) {
                throw new RuntimeException("盘点单，不能为空！");
            }
        }
        MyAssetInventoryModel model = myAssetInventoryModelDao.findById(myAssetInventoryId).get();
        List<AssetInventoryTempModel> tempModel = assetInventoryTempDao.findBymyAssetInventoryModel(model);
        for (AssetInventoryTempModel temp : tempModel) {
            if (!VGUtility.isEmpty(temp.getId()))
                list.add(temp.getId());
        }
        return list;
    }


    /*
     * 盘点单导出
     * @see com.mine.product.czmtr.ram.asset.service.IAssetInventoryService#exportAssetInventoryList(java.util.List)
     */
    @Override
    public byte[] exportAssetInventoryList(List<String> assetInventoryList) {
        byte[] resultArray = null;
        try {
            int rowInt = 0;
            int cellInt = 0;

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();

            //操作方式
            String[] textList1 = {"手工录入", "自动录入"};
            //操作方式下拉 首行，末行，首列，末列
            sheet = setHSSFValidation(sheet, textList1, 0, 50000, 2, 2);

            //盘点结果
            String[] textList2 = {"盘亏", "盘盈", "相符"};
            //操作方式下拉 首行，末行，首列，末列
            sheet = setHSSFValidation(sheet, textList2, 0, 50000, 3, 3);

            //盘点方式
            String[] textList3 = {"手工", "扫码"};
            //操作方式下拉 首行，末行，首列，末列
            sheet = setHSSFValidation(sheet, textList3, 0, 50000, 4, 4);

            HSSFRow row = sheet.createRow(rowInt);
            HSSFCell cell = row.createCell(cellInt++);
            cell.setCellValue("盘点单ID");
            cell = row.createCell(cellInt++);
            cell.setCellValue("盘点清单ID");
            cell = row.createCell(cellInt++);
            cell.setCellValue("操作");
            cell = row.createCell(cellInt++);
            cell.setCellValue("盘点结果");
            cell = row.createCell(cellInt++);
            cell.setCellValue("盘点方式");
            cell = row.createCell(cellInt++);
            cell.setCellValue("备注");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产编码");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产名称");
            cell = row.createCell(cellInt++);
            cell.setCellValue("规格型号");
            cell = row.createCell(cellInt++);
            cell.setCellValue("购置日期");
            cell = row.createCell(cellInt++);
            cell.setCellValue("计量单位");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产原值");
            cell = row.createCell(cellInt++);
            cell.setCellValue("残余价值");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产状态");
            cell = row.createCell(cellInt++);
            cell.setCellValue("安装位置");
            cell = row.createCell(cellInt++);
            cell.setCellValue("使用部门");
            cell = row.createCell(cellInt++);
            cell.setCellValue("使用人");
            cell = row.createCell(cellInt++);
            cell.setCellValue("主管部门");
            cell = row.createCell(cellInt++);
            cell.setCellValue("资产管理员");

            //模拟
            List<AssetInventoryTempDto> dtolist = new ArrayList<AssetInventoryTempDto>();
            List<AssetInventoryTempModel> modellist = assetInventoryTempDao.findAllById(assetInventoryList);

            for (AssetInventoryTempModel model : modellist) {
                dtolist.add(convertAssetInventoryTempModelToDto(model));
            }

            for (AssetInventoryTempDto TempDto : dtolist) {
                cellInt = 0;
                row = sheet.createRow(++rowInt);

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getMyAssetInventoryId());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getId());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getOperationStr());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getResultStr());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getInventoryWayStr());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getRemark());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getAssetCode());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getAssetChsName());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getSpecAndModels());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getBuyDate());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getUnitOfMeasStr());//计量单位

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getEquiOrigValue());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getResidualValue());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getAssetStatus());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getSavePlaceStr());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getUseDeptStr());//所属线路建筑

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getUseStr());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getManageDeptStr());

                cell = row.createCell(cellInt++);
                cell.setCellValue(TempDto.getManagerStr());
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

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet    要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return 设置好的sheet.
     */
    public HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    @Override
    public AssetApprove doApproveNotify(String id) {
        MyAssetInventoryDto myAssetInventoryDto = new MyAssetInventoryDto();
        myAssetInventoryDto.setId(id);
        boolean flag = this.checkForSubmit(myAssetInventoryDto);
        AssetApprove approve = new AssetApprove();
        approve.setCanApprove(flag);
        if (!flag) {
            MyAssetInventoryModel model = myAssetInventoryModelDao.findById(id).get();
            List<AssetInventoryTempModel> list = assetInventoryTempDao.findBymyAssetInventoryModel(model);
            for (AssetInventoryTempModel temp : list) {
                if (VGUtility.isEmpty(temp.getResult())) {
                    AssetAssetModel assetModel = assetDao.findById(temp.getAssetId()).get();
                    ApprovenAssetDto approvenAssetDto = new ApprovenAssetDto();
                    approvenAssetDto.setAssetChsName(assetModel.getAssetChsName()).setAssetCode(assetModel.getAssetCode()).setAssetStatus(assetModel.getAssetStatus());
                    approve.getUnAssetList().add(approvenAssetDto);
                }
            }
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
                //创建历史记录；
                MyAssetInventoryModel model = myAssetInventoryModelDao.findById(id).get();
                List<AssetInventoryTempModel> list = assetInventoryTempDao.findBymyAssetInventoryModel(model);
                this.createHistory(model, list);
                //更新主表盘点进度；
                MyAssetInventoryDto dto = new MyAssetInventoryDto();
                dto.setId(id);
                this.UpdateMyAssetInventoryStatus(dto);
                break;
            default:
                break;

        }
        //更新主表状态
        MyAssetInventoryModel model = myAssetInventoryModelDao.findById(id).get();
        model.setInventoryStatus(workStatus);
    }

    public void createHistory(MyAssetInventoryModel model, List<AssetInventoryTempModel> list) {
        for (AssetInventoryTempModel TempModel : list) {
            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setAssetModelId(TempModel.getAssetId());
            historyDto.setHistoryType(HISTORY_TYPE.盘点记录);
            historyDto.setTakeStockResult(TempModel.getResult().name());
            historyDto.setCreateUserId(model.getManagerId());
            assetService.createHistory(historyDto);
        }

    }
}
