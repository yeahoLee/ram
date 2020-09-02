package com.mine.product.czmtr.ram.outapi.service;

import com.alibaba.fastjson.JSON;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetBean;
import com.mine.product.czmtr.ram.asset.dto.ErrorBean;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeTempDto;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.asset.service.IMaterialCodeService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.mine.product.czmtr.ram.outapi.dto.AssetCodeBean;
import com.mine.product.czmtr.ram.outapi.dto.OutApiResultDto;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebService
public class OutApiService {
    @Autowired
    private IMaterialCodeService materialCodeService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;

    //传物资编码——>资产编码和验证码
    public OutApiResultDto getAssetCodesByMaterialCode(String materialCode, Integer materialQuantity) {
        OutApiResultDto resultDto = new OutApiResultDto();
        List<AssetCodeBean> beans = new ArrayList<AssetCodeBean>();

        List<MaterialCodeTempDto> dto = materialCodeService.getMultipleMaterialCode(materialCode, materialQuantity);
        if (!VGUtility.isEmpty(dto)) {
            resultDto.setResult(1);
            //resultDto.setErrorDesc("成功");
            for (MaterialCodeTempDto temp : dto) {
                AssetCodeBean bean = new AssetCodeBean();
                bean.setAssetCode(temp.getCodeTemp());
                bean.setValidateCode(temp.getTempNum());
                beans.add(bean);
            }
        } else {
            resultDto.setResult(0);
            resultDto.setErrorDesc("物资编码不存在或已经查过系统最大值");
        }
        resultDto.setCodeBeans(beans);
        return resultDto;
    }

    /****
     * 传物资信息
     * @param assetBeanList
     * @return
     */
    public OutApiResultDto createAssert(List<AssetBean> assetBeanList) {
        OutApiResultDto resultDto = new OutApiResultDto();
        List<ErrorBean> errorBeanList = new ArrayList<ErrorBean>();
        List<AssetAssetDto> assetDtoList = new ArrayList<AssetAssetDto>();

        if (assetBeanList.isEmpty()) {
            resultDto.setErrorDesc("物资不能为空！");
        } else {
            //检查错误数据
            errorBeanList = assetService.commonCheckAssetForOutApi(assetBeanList);
            if (!errorBeanList.isEmpty()) {
                resultDto.setErrorDesc("物资信息有误！");
                resultDto.setErrorBeans(errorBeanList);
            } else {
                resultDto.setResult(1);
                //转换数据 bean to dto
                //TODO 根据资产编码 判断是否是固定资产
                Map<String, UserInfoDto> userMap = baseService.getUserInfoByRoleCode("5-bmzcgly");
                assetBeanList.stream().forEach(o -> {
                	String deptCode = "";
                    
                    if(o.getManageDeptCode().length()>=5) {
                    	deptCode = o.getManageDeptCode().substring(0, 5);
                    }
                	UserInfoDto info = userMap.get(deptCode);
                    assetDtoList.add(convertAssetBeanToDto(o,info));
                });
                assetService.createAssetByDtos(assetDtoList);
            }
        }

        return resultDto;
    }

    /**
     * 实物资产主数据 同步
     *
     * @param assetCodeList  资产编码
     * @param lastUpdateTime 最后更新时间
     * @param index          当前页
     * @return
     */
    public String getAssetResultByQuerys(@WebParam(name = "assetCodeList") List<String> assetCodeList, @WebParam(name = "lastUpdateTime") String lastUpdateTime, @WebParam(name = "index") int index) {
        int pageSize = 1000;
        PageableDto pageableDto = new PageableDto();
        if (!VGUtility.isEmpty(index)) {
            if (index > 0)
                pageableDto.setPage(index);
            else
                pageableDto.setPage(1);
            pageableDto.setSize(pageSize);
        }
        OutApiResultDto resultDto = new OutApiResultDto();
        Map<String, Object> map = assetService.getAssetByList(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate finalPred = getPredicateByAssetDto(builder, root, assetCodeList, lastUpdateTime);
                return finalPred;
            }
        }, pageableDto);
        List<AssetBean> saveDto = (List<AssetBean>) map.get("rows");
        resultDto.setResult(1);
        resultDto.setAssetDtoList(saveDto);
        if (!VGUtility.isEmpty(map.get("total")))
            resultDto.setCount(VGUtility.toInteger(map.get("total").toString()));
        else
            resultDto.setCount(0);
        return JSON.toJSONString(resultDto);
    }

    /**
     * 查询条件动态拼接
     *
     * @param builder
     * @param root
     * @param assetCodeList
     * @param lastUpdateTime
     * @return
     */
    private Predicate getPredicateByAssetDto(CriteriaBuilder builder, Root root, List<String> assetCodeList, String lastUpdateTime) {
        Predicate finalPred = null;
        List<Predicate> andList = new ArrayList<Predicate>();
        List<String> strlist = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetCodeList) && assetCodeList.size() > 0) {
            for (String str : assetCodeList) {
                if (!VGUtility.isEmpty(str))
                    strlist.add(str);
            }
        }
        if (!VGUtility.isEmpty(strlist) && strlist.size() > 0) {
            Path<Object> path = root.get("assetCode");
            CriteriaBuilder.In<Object> in = builder.in(path);
            for (String str : strlist) {
                in.value(str);
            }
            andList.add(in);
        }
        if (!VGUtility.isEmpty(lastUpdateTime)) {
            andList.add(builder.greaterThanOrEqualTo(root.get("lastUpdateTimestamp"), VGUtility.toDateObj(lastUpdateTime, "yyyy-MM-dd HH:mm:ss")));
        }
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }


    private AssetAssetDto convertAssetBeanToDto(AssetBean asset,UserInfoDto info) {
        AssetAssetDto aDto = new AssetAssetDto();
        aDto.setAssetCode(asset.getAssetCode());//资产编码
        aDto.setMaterialCode(asset.getAssetCode().substring(0, 12));//物资编码
        aDto.setAssetType(asset.getAssetTypeCode()); //资产类型
        aDto.setSeriesNum(asset.getSeriesNum()); //序列号
        aDto.setAssetBrand(asset.getAssetBrand()); //资产品牌
        aDto.setTechPara(asset.getTechPara()); //技术参数 technical parameters
        aDto.setPurcPrice(asset.getPurcPrice()); //采购价 purchase price
        aDto.setEquiOrigValue(asset.getEquiOrigValue());
        aDto.setRemark(asset.getRemark()); //备注
        //基本信息End

        //延伸信息Start
        aDto.setBuyDate(asset.getBuyDate()); //购置日期
        aDto.setContractNum(asset.getContractNum()); //合同编号
        aDto.setTendersNum(asset.getTendersNum()); //标段编号
		if(!VGUtility.isEmpty(asset.getCompanyCode()))
			aDto.setCompanyId(userService.getDeptInfo("{code:'"+asset.getCompanyCode()+"'}", null, null).getRowData().get(0).getId()); //所属公司id

        aDto.setManageDeptId(userService.getDeptInfo("{code:'" + asset.getManageDeptCode() + "'}", null, null)
                .getRowData().get(0).getId()); //主管部门id
		
        if (!VGUtility.isEmpty(asset.getManagerCode())) {
            UserInfoDto userInfoDto = userService.getUserInfo("{userName:'" + asset.getManagerCode() + "'}", null, null).getRowData().get(0);
            String id = userInfoDto.getId();
            aDto.setManagerId(id); //资产管理员id
            if (!VGUtility.isEmpty(userInfoDto.getPropertyMap()) && !VGUtility.isEmpty(userInfoDto.getPropertyMap().get("EMP_NO"))) {
                aDto.setManagerEmpNo(userInfoDto.getPropertyMap().get("EMP_NO").toString());
            }
        }else {
        	 aDto.setManagerId(info.getId()); //资产管理员id
        	 if (!VGUtility.isEmpty(info.getPropertyMap()) && !VGUtility.isEmpty(info.getPropertyMap().get("EMP_NO"))) {
                 aDto.setManagerEmpNo(info.getPropertyMap().get("EMP_NO").toString());
             }
        }
        if(!VGUtility.isEmpty(asset.getUseDeptCode())) {
        	aDto.setUseDeptId(userService.getDeptInfo("{code:'" + asset.getUseDeptCode() + "'}", null, null)
                    .getRowData().get(0).getId()); //使用部门id
        }
        if(!VGUtility.isEmpty(asset.getUserCode())) {
        	 UserInfoDto userInfoDto = userService.getUserInfo("{userName:'" + asset.getUserCode() + "'}", null, null).getRowData().get(0);
             String id = userInfoDto.getId();
             aDto.setUserId(id); //使用人id
        }
        aDto.setBelongLine(dictService.getCommonCode("{code:'" + asset.getBelongLine() + "'}", null, null)
                .getRowData().get(0).getId()); //所属建筑
        //位置编码 = 资产线路 + 具体位置 + 设备房间号（建筑物号+楼层号+房间号）
        aDto.setAssetSource(asset.getAssetSource()); //资产来源
        if (!VGUtility.isEmpty(asset.getSavePlaceCode())) {
            aDto.setSavePlaceId(dictService.getCommonCode("{code:'" + asset.getSavePlaceCode() + "'}", null, null)
                    .getRowData().get(0).getId());
        }
        aDto.setAssetSource(asset.getAssetSource());//资产来源
        aDto.setSourceType(asset.getSourceType()); //来源方式
        aDto.setSourceUser(asset.getSourceUser()); //联系人
        aDto.setSourceContactInfo(asset.getSourceContactInfo()); //联系方式
        aDto.setProdTime(asset.getProdTime()); //出厂时间 production Time
        aDto.setMainPeriod(asset.getMainPeriod()); //维保期 maintenance period

        //根据物资师是否进设备台帐
        //是：生产性物资
        //否：非生产性物资

        IAssetService.ASSET_PRODUCE_TYPE type = assetService.getProduceTypeByMaterialCode(aDto.getMaterialCode());
        aDto.setProduceType(type);
        return aDto;
    }
	
}
