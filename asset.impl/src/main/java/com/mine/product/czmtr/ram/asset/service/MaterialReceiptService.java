package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dao.MaterialReceiptDao;
import com.mine.product.czmtr.ram.asset.dto.AssetApprove;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.dto.MaterialReceiptDto;
import com.mine.product.czmtr.ram.asset.model.MaterialReceiptModel;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Duration;
import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MaterialReceiptService implements IMaterialReceiptService {
    @Autowired
    private MaterialReceiptDao materialReceiptDao;
    @Autowired
    private IMaterialReceiptService mateReceService;
    @Autowired
    private IMaterialCodeService materialCodeService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private IAssetTempService assetTempService;
    @Autowired
    private IAssetService assetService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public MaterialReceiptDto createMaterialReceipt(MaterialReceiptDto dto) {
        if (VGUtility.isEmpty(dto)) throw new RuntimeException("MaterialReceiptDto is null!");
        MaterialReceiptModel model = convertDtoToModel(dto);
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        MaterialReceiptDto materialReceiptDto = convertModelToDto(materialReceiptDao.save(convertDtoToModel(dto)));
        return materialReceiptDto;
    }

    @Override
    public void deleteMaterialReceipt(String id) {
        if (VGUtility.isEmpty(id)) throw new RuntimeException("Id is null!");
        materialReceiptDao.deleteById(id);
    }

    @Override
    public MaterialReceiptDto updateMaterialReceipt(MaterialReceiptDto dto) {
        if (VGUtility.isEmpty(dto)) throw new RuntimeException("MaterialReceiptDto is null!");
        if (VGUtility.isEmpty(dto.getId())) throw new RuntimeException("Id is null!");
        Optional<MaterialReceiptModel> modelOpt = materialReceiptDao.findById(dto.getId());
        if (modelOpt.isPresent()) {
            MaterialReceiptModel model = modelOpt.get();
            model.setProduceType(dto.getProduceType());
            model.setReceiptName(dto.getReceiptName());
            model.setSourceType(IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())]);
            model.setReason(dto.getReason());
            model.setRemark(dto.getRemark());
            if (!VGUtility.isEmpty(dto.getReceiptStatusEnum()))
                model.setReceiptStatus(dto.getReceiptStatusEnum());
            materialReceiptDao.save(model);
            return convertModelToDto(model);
        } else
            throw new RuntimeException("Can not found result!");
    }

    @Override
    public MaterialReceiptDto getMaterialReceiptById(String id) {
        if (VGUtility.isEmpty(id)) throw new RuntimeException("Id is null!");
        Optional<MaterialReceiptModel> modelOpt = materialReceiptDao.findById(id);
        if (modelOpt.isPresent()) {
            MaterialReceiptModel model = modelOpt.get();
            return convertModelToDto(model);
        } else
            throw new RuntimeException("Can not found result!");
    }

    @Override
    public PageDto<MaterialReceiptDto> getMaterialReceipt(String hql, Map<String, Object> params, PageableDto pageable) {
        return getMaterialReceipt(hql, "select count(id) " + hql, params, pageable);
    }

    @Override
    public PageDto<MaterialReceiptDto> getMaterialReceipt(String hql, String countHql, Map<String, Object> params,
                                                          PageableDto pageable) {
        TypedQuery<MaterialReceiptModel> query = entityManager.createQuery(hql, MaterialReceiptModel.class);
        TypedQuery<Long> countQuery = entityManager.createQuery("select count(id) " + hql, Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        if (!VGUtility.isEmpty(pageable))
            query.setFirstResult((pageable.getPage() - 1) * pageable.getSize()).setMaxResults(pageable.getSize());
        List<MaterialReceiptModel> modelList = query.getResultList();

        List<MaterialReceiptDto> resultList = new ArrayList<MaterialReceiptDto>();
        for (MaterialReceiptModel model : modelList) {
            resultList.add(convertModelToDto(model));
        }
        return new PageDto<MaterialReceiptDto>(countQuery.getSingleResult(), resultList);
    }

    private MaterialReceiptDto convertModelToDto(MaterialReceiptModel model) {
        MaterialReceiptDto dto = new MaterialReceiptDto();

        dto.setId(model.getId());
        dto.setRunningNum(model.getRunningNum());
        dto.setSourceType(Integer.toString(model.getSourceType().ordinal()));
        dto.setSourceTypeStr(model.getSourceType().name());
        dto.setReason(model.getReason());
        dto.setRemark(model.getRemark());
        dto.setPersonId(model.getPersonId());
        dto.setWorkAddress(model.getWorkAddress());
        dto.setWorkPhone(model.getWorkPhone());
        dto.setReceiptStatus(Integer.toString(model.getReceiptStatus().ordinal()));
        dto.setReceiptStatusStr(model.getReceiptStatus().name());
        dto.setReceiptName(model.getReceiptName());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy/M/d"));
        dto.setLastUpdateTimestamp(VGUtility.toDateStr(model.getLastUpdateTimestamp(), "yyyy/M/d"));
        dto.setProduceType(model.getProduceType());
        return dto;
    }

    private MaterialReceiptModel convertDtoToModel(MaterialReceiptDto dto) {
        MaterialReceiptModel model = new MaterialReceiptModel();

        model.setId(dto.getId());
        model.setRunningNum(dto.getRunningNum());
        model.setSourceType(IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())]);
        model.setReason(dto.getReason());
        model.setRemark(dto.getRemark());
        model.setPersonId(dto.getPersonId());
        model.setWorkAddress(dto.getWorkAddress());
        model.setWorkPhone(dto.getWorkPhone());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.values()[VGUtility.toInteger(dto.getReceiptStatus())]);
        model.setReceiptName(dto.getReceiptName());
        model.setProduceType(dto.getProduceType());

        return model;
    }

    @Override
    public String getMaxRunningNum(String runningNum) {
        String maxRunningNum = materialReceiptDao.getMaxRunningNum(runningNum + "%");
        if (VGUtility.isEmpty(maxRunningNum)) {
            return runningNum + "001";
        } else {
            String newNum = "";
            String num = maxRunningNum.substring(maxRunningNum.length() - 3, maxRunningNum.length());
            String n = String.valueOf((VGUtility.toInteger(num) + 1));
            if (n.length() == 1)
                newNum = "00" + n;
            else if (n.length() == 2)
                newNum = "0" + n;
            else
                newNum = n;
            return runningNum + newNum;
        }
    }

    @Override
    public void changeReceiptStatus(String id, FlowableInfo.WORKSTATUS exStatus, FlowableInfo.WORKSTATUS status) {
        MaterialReceiptModel model = materialReceiptDao.findById(id).get();

        if (VGUtility.isEmpty(model))
            throw new RuntimeException("MaterialReceiptModel Is Deleted! id [" + id + "]");
        if (model.getReceiptStatus() != exStatus)
            throw new RuntimeException("新增单状态当前状态：" + model.getReceiptStatus().toString() + "需要的前置状态：" + exStatus.toString());

        switch (exStatus.ordinal()) {
            case 0: //拟稿
                if (status.ordinal() == 1) {
                    model.setReceiptStatus(status);
                    break;
                }
            case 1: //审批中
                if (status.ordinal() == 2 || status.ordinal() == 3) {
                    model.setReceiptStatus(status);
                    break;
                }
            default:
                throw new RuntimeException("ExStatus And Status Can't Match");
        }

        materialReceiptDao.save(model);
    }

    @Override
    public void checkReceipt(String id) {
        String hql = "from AssetTempModel m where 1=1 and m.recId = :recId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("recId", id);

        List<AssetAssetDto> assetDtolist = assetTempService.getAssetTempByRecId(hql, params, null).getRowData();
        if (assetDtolist.size() == 0)
            throw new RuntimeException("资产清单不能为空!");
    }

    @Override
    public void createAssetListByMateReceId(String mateReceId) {
        String mCode = new String();
        String hql = "from AssetTempModel m where 1=1 and m.recId = :recId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("recId", mateReceId);

        List<AssetAssetDto> assetDtolist = assetTempService.getAssetTempByRecId(hql, params, null).getRowData();
		
		/*MaterialReceiptDto rDto = mateReceService.getMaterialReceiptById(mateReceId);
		rDto.setReceiptStatusEnum(IMaterialReceiptService.FlowableInfo.WORKSTATUS.审核通过);
		mateReceService.updateMaterialReceipt(rDto);*/

        for (AssetAssetDto dto : assetDtolist) {
            mCode = materialCodeService.getMaterialCodeByAssetCode(dto.getMaterialCode());
            if (VGUtility.isEmpty(mCode))
                throw new RuntimeException("物资编码不存在或已经超过系统最大值");
            else {
                dto.setId(null);
                dto.setAssetCode(mCode);
                assetService.createAsset(dto, ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo());
            }
        }
    }

    @Override
    public List<String> checkAssetProduceType(List<AssetAssetDto> assetDtoList, IAssetService.ASSET_PRODUCE_TYPE type) {
        List<String> notAllowAssetList = new ArrayList<>();
        for (AssetAssetDto dto : assetDtoList) {
            IAssetService.ASSET_PRODUCE_TYPE produceType = assetService.getProduceTypeByMaterialCode(dto.getMaterialCode());
            if(type!=produceType){
                notAllowAssetList.add(dto.getMaterialCode()+"--"+dto.getAssetChsName());
            }
        }
        return notAllowAssetList;
    }


    @Override
    public AssetApprove doApproveNotify(String id) {
        AssetApprove approve = new AssetApprove();
        List<AssetAssetDto> assetDtos = assetTempService.getAssetTempByRecId(id);
        if (assetDtos == null) {
            approve.setCanApprove(false);
            approve.setMessage("资产列表不能为空");
            return approve;
        }

        //发起审批之前就开始获取资产编码！，如果获取失败就不发送审批
        ResourceBundle resource = ResourceBundle.getBundle("config.webservice");
        String ENV = resource.getString("ENV");
//        if (ENV.contains("production") || ENV.equals("test")) {
        if (ENV.contains("production")) {
            //调用接口生成资产编码；
            //调用物资系统申请资产编码时时间较长，会触发dubbo的重试机制，为保证接口只被调用一次，通过redis加锁。
            Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(id, 1000, Duration.ofSeconds(5));//
            //接口幂等性保证！看到此处如果你有什么疑问，请研究分布式锁！
            if (setIfAbsent) {
                return assetService.applyAssetCode(id);
            }else {
                //此时是dubbo的重试机制进来的请求，需要等待上面的逻辑执行完成；
                try{
                    Thread.currentThread().sleep(8000);
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        } else {
            //本地生成资产编码；
            MaterialReceiptModel materialReceiptModel = materialReceiptDao.findById(id).get();
            //物资类型
            IAssetService.ASSET_PRODUCE_TYPE produceType = materialReceiptModel.getProduceType();
            //资产进入台账
            List<AssetAssetDto> assetDtolist = assetTempService.getAssetTempByRecId(id);
            for (AssetAssetDto dto : assetDtolist) {
                String mCode = materialCodeService.getMaterialCodeByAssetCode(dto.getMaterialCode());
                if (VGUtility.isEmpty(mCode)) {
                    approve.setCanApprove(false);
                    approve.setMessage("物资编码不存在或已经超过系统最大值");
                    return approve;
                }
                dto.setProduceType(produceType);
                dto.setAssetCode(mCode);
                assetTempService.updateAssetTemp(dto);
            }
        }
        approve.setCanApprove(true);
        return approve;
    }

    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        switch (workStatus) {
            case 已审批:
                MaterialReceiptModel materialReceiptModel = materialReceiptDao.findById(id).get();
                String sourceType = Integer.toString(materialReceiptModel.getSourceType().ordinal());
                //物资类型
                IAssetService.ASSET_PRODUCE_TYPE produceType = materialReceiptModel.getProduceType();

                UserInfoDto userInfoDto = new UserInfoDto();
                userInfoDto.setId(materialReceiptModel.getPersonId());
                //资产进入台账
                List<AssetAssetDto> assetDtolist = assetTempService.getAssetTempByRecId(id);
                for (AssetAssetDto dto : assetDtolist) {
                    dto.setId(null);
                    dto.setSourceType(sourceType);
                    dto.setProduceType(produceType);
                    assetService.createAsset(dto, userInfoDto);
                }
                break;
            default:
                break;

        }
        //更新主表状态
        MaterialReceiptModel model = materialReceiptDao.findById(id).get();
        model.setReceiptStatus(workStatus);

    }




    /*
    此版本先审批再申请资产编码！如果审批通过后资产编码没有申请成功 就会出问题。

    @Override
    public AssetApprove doApproveNotify(String id) {
        List<AssetAssetDto> assetDtolist = assetTempService.getAssetTempByRecId(id);
        AssetApprove approve = new AssetApprove();
        approve.setCanApprove(assetDtolist == null ? false : true);
        return approve;
    }

    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        switch (workStatus) {
            case 已审批:
                ResourceBundle resource = ResourceBundle.getBundle("config.webservice");
                String ENV = resource.getString("ENV");
                if (ENV.contains("production") || ENV.equals("test")) {
                    //调用接口生成资产编码；
                    Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(id, 1000, Duration.ofMinutes(5));
                    //接口幂等性保证！
                    if (setIfAbsent) {
                        assetService.finishApprove(id);
                    } else {
                        return;
                    }
                } else {
                    //本地生成资产编码；
                    MaterialReceiptModel materialReceiptModel = materialReceiptDao.findById(id).get();
                    String createPersonId = materialReceiptModel.getPersonId();
                    //物资类型
                    IAssetService.ASSET_PRODUCE_TYPE produceType = materialReceiptModel.getProduceType();

                    UserInfoDto userInfoDto = new UserInfoDto();
                    userInfoDto.setId(createPersonId);
                    //资产进入台账
                    List<AssetAssetDto> assetDtolist = assetTempService.getAssetTempByRecId(id);
                    Map<String, String> map = new HashMap<>();
                    for (AssetAssetDto dto : assetDtolist) {
                        String mCode = materialCodeService.getMaterialCodeByAssetCode(dto.getMaterialCode());
                        if (VGUtility.isEmpty(mCode)) {
                            throw new RuntimeException("物资编码不存在或已经超过系统最大值");
                        }
                        map.put(dto.getId(), mCode);
                        dto.setId(null);
                        dto.setAssetCode(mCode);
                        dto.setProduceType(produceType);
                        assetService.createAsset(dto, userInfoDto);
                    }
                    //插入已经审批的资产编号；
                    assetTempService.insertAssetCode(map);
                }

                break;
            default:
                break;

        }
        //更新主表状态
        MaterialReceiptModel model = materialReceiptDao.findById(id).get();
        model.setReceiptStatus(workStatus);

    }*/
}
