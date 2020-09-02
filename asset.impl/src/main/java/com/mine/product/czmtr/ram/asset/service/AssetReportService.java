package com.mine.product.czmtr.ram.asset.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetReportHistoryDto;
import com.mine.product.czmtr.ram.asset.dto.AssetUsabilityHistoryDto;
import com.mine.product.czmtr.ram.asset.model.AssetHistoryModel;
import com.mine.product.czmtr.ram.asset.model.AssetReduceModel;
import com.mine.product.czmtr.ram.asset.model.AssetReduceTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.CHANGE_TYPE;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;

/***
 * 报表服务类
 *
 * @author yangjie
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetReportService implements IAssetReportService {

    @Autowired
    private IAssetService assetService;
    @Autowired
    private IBaseService baseService;
    @PersistenceContext
    private EntityManager entityManager;

    /********************* 资产变更历史 start *******************************/
    @Override
    public ModelMap QueryAssetChangeHistory(ISearchExpression searchExpression, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetHistoryModel> Criteria = builder.createQuery(AssetHistoryModel.class);
        Root<AssetHistoryModel> root = Criteria.from(AssetHistoryModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            Criteria.where((Predicate) predicate);

        Criteria.orderBy(builder.desc(root.get("createTimestamp")));
        Query<AssetHistoryModel> query = session.createQuery(Criteria);
        // 分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize())
                    .setMaxResults(pageableDto.getSize());
        List<AssetHistoryModel> modelList = query.getResultList();
        // total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetHistoryModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();
        // convert 需要转换成 AssetChangeHistoryDto
        List<AssetReportHistoryDto> dtoList = new ArrayList<AssetReportHistoryDto>();
        for (AssetHistoryModel model : modelList) {
            dtoList.add(convertModelToDto(model));
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    @Override
    public ModelMap GetAssetChangeHistory(AssetReportHistoryDto assetReportHistoryDto, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        StringBuffer sql = new StringBuffer("select * from ( select a.ID, "
                + " h.CHANGECONTENT,"
                + " to_char(h.CREATETIMESTAMP,'yyyy-MM-dd') as CREATETIMESTAMP, "
                + " h.CHANGETYPE, "
                + " rownum RN "
                + " from ASSETHISTORYMODEL h "
                + " left join ASSETASSETMODEL a on h.ASSETMODELID = a.ID where 1=1");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getHistoryTypeStr()))
            sql.append(" and h.historyType ='" + StringEscapeUtils.escapeSql(assetReportHistoryDto.getHistoryTypeStr()) + "'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getChangeTypeStr()))
            sql.append(" and h.changeTypeStr='" + StringEscapeUtils.escapeSql(assetReportHistoryDto.getChangeTypeStr()) + "'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getManageDeptId()))
            sql.append(" and a.MANAGEDEPTID='" + StringEscapeUtils.escapeSql(assetReportHistoryDto.getManageDeptId()) + "'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getMaterialCode()))
            sql.append(" and a.MATERIALCODE like '%" + StringEscapeUtils.escapeSql(assetReportHistoryDto.getMaterialCode()) + "%'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampStart()))
            sql.append(" and h.CREATETIMESTAMP>=TO_DATE('" + VGUtility.toDateStr((VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampStart(), "yyyy/M/d")), "yyyy-MM-dd") + "','yyyy-MM-dd') ");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampEnd()))
            sql.append(" and h.CREATETIMESTAMP<=TO_DATE('" + VGUtility.toDateStr((VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampEnd(), "yyyy/M/d")), "yyyy-MM-dd") + "','yyyy-MM-dd') ");
        sql.append(" ) m ");
        if (!VGUtility.isEmpty(pageableDto))
            sql.append(" where m.RN between " + ((pageableDto.getPage() - 1) * pageableDto.getSize() + 1) + " and " + pageableDto.getPage() * pageableDto.getSize());

        sql.append(" ORDER BY m.CREATETIMESTAMP DESC");
        // 创建查询器
        SQLQuery query = session.createSQLQuery(sql.toString());
        List<Object> resultList = query.getResultList();
        List<AssetReportHistoryDto> list = new ArrayList<AssetReportHistoryDto>();
        for (Object obj : resultList) {
            Object[] objs = (Object[]) obj;
            AssetHistoryModel model = new AssetHistoryModel();
            if (!VGUtility.isEmpty(objs[0]))
                model.setAssetModelId(objs[0].toString());
            if (!VGUtility.isEmpty(objs[1]))
                model.setChangeContent(objs[1].toString());
            if (!VGUtility.isEmpty(objs[2]))
                model.setCreateTimestamp(VGUtility.toDateObj(objs[2].toString(), "yyyy-MM-dd"));
            if (!VGUtility.isEmpty(objs[3]))
                model.setChangeType(CHANGE_TYPE.values()[VGUtility.toInteger(objs[3].toString())]);
            list.add(convertModelToDto(model));
        }
        // 总条数
        StringBuffer totalsql = new StringBuffer("select a.ID, " +
                "h.CHANGECONTENT, " +
                "to_char(h.CREATETIMESTAMP,'yyyy-MM-dd') as CREATETIMESTAMP " +
                "from ASSETHISTORYMODEL h " +
                "left join ASSETASSETMODEL a on h.ASSETMODELID = a.ID where 1=1");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getHistoryTypeStr()))
            totalsql.append(" and h.historyType ='" + StringEscapeUtils.escapeSql(assetReportHistoryDto.getHistoryTypeStr()) + "'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getChangeTypeStr()))
            totalsql.append(" and h.changeTypeStr='" + StringEscapeUtils.escapeSql(assetReportHistoryDto.getChangeTypeStr()) + "'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getManageDeptId()))
            totalsql.append(" and a.MANAGEDEPTID='" + assetReportHistoryDto.getManageDeptId() + "'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getMaterialCode()))
            totalsql.append(" and a.MATERIALCODE like '%" + StringEscapeUtils.escapeSql(assetReportHistoryDto.getMaterialCode()) + "%'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampStart()))
            totalsql.append(" and h.CREATETIMESTAMP>=TO_DATE('" + VGUtility.toDateStr((VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampStart(), "yyyy/M/d")), "yyyy-MM-dd") + "','yyyy-MM-dd') ");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampEnd()))
            totalsql.append(" and h.CREATETIMESTAMP<=TO_DATE('" + VGUtility.toDateStr((VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampEnd(), "yyyy/M/d")), "yyyy-MM-dd") + "','yyyy-MM-dd') ");
        // 创建查询器
        SQLQuery totalquery = session.createSQLQuery(totalsql.toString());
        List<Object> totalList = totalquery.getResultList();
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", list);
        modelMap.addAttribute("total", totalList.size());
        return modelMap;
    }

    public AssetReportHistoryDto convertModelToDto(AssetHistoryModel model) {
        AssetReportHistoryDto dto = new AssetReportHistoryDto();
        if (!VGUtility.isEmpty(model.getAssetModelId())) {
            AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetModelId());
            dto.setAssetCode(asset.getAssetCode());
            dto.setAssetChsName(asset.getAssetChsName());
            dto.setAssetLineId(asset.getAssetLineId());
            dto.setAssetLineStr(asset.getAssetLineStr());
            dto.setAssetStatus(asset.getAssetStatus());
            dto.setAssetStatusStr(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())].name());
            dto.setAssetType(asset.getAssetType());
            dto.setAssetTypeStr(asset.getAssetTypeStr());
            dto.setChangeContent(model.getChangeType().name());
            dto.setChangeDate(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
            dto.setCombinationAssetName(asset.getCombinationAssetName());
            dto.setCombinationAssetType(asset.getCombinationAssetType());
            dto.setManageDeptId(asset.getManageDeptId());
            dto.setManageDeptStr(asset.getManageDeptStr());
            dto.setManagerId(asset.getManagerId());
            dto.setManagerStr(asset.getManagerStr());
            dto.setSpecAndModels(asset.getSpecAndModels());
            dto.setUnitOfMeasId(asset.getUnitOfMeasId());
            dto.setUnitOfMeasStr(asset.getUnitOfMeasStr());
            dto.setUseDeptId(asset.getUseDeptId());
            dto.setUseDeptStr(asset.getUseDeptStr());
            dto.setUserId(asset.getUserId());
            dto.setUseStr(asset.getUseStr());
            dto.setBelongLineStr(asset.getBelongLineStr());
            dto.setMaterialCode(asset.getMaterialCode());
            if (!VGUtility.isEmpty(model.getChangeContent()))
                dto.setAssetMoveHistory(
                        "移动至" + model.getChangeContent().substring(model.getChangeContent().indexOf("->") + 1));
            dto.setAssetMoveDate(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        }
        return dto;
    }

    /********************* 资产变更历史 end *******************************/

    /********************* 资产移动历史 start *******************************/
    @Override
    public ModelMap QueryAssetMoveHistory(AssetReportHistoryDto assetReportHistoryDto, PageableDto pageableDto) {
        // 通过hibernate获得Session
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        StringBuffer sql = new StringBuffer("select * from ( select a.ID " +
                ",a.ASSETCODE " +
                ",a.MATERIALCODE " +
                ",asm.CHANGECONTENT " +
                ",to_char(asm.CREATETIMESTAMP,'yyyy-MM-dd') as CREATETIMESTAMP " +
                ",a.ASSETTYPE " +
                ",a.SPECANDMODELS " +
                ",a.ASSETBRAND " +
                ",a.UNITOFMEASID " +
                ",a.USEDEPTID " +
                ",a.USERID " +
                ",a.MANAGEDEPTID " +
                ",a.MANAGERID " +
                ",a.BELONGLINE " +
                ",a.ASSETSTATUS " +
                ",rownum RN " +
                "from ASSETHISTORYMODEL asm " +
                "inner join ASSETASSETMODEL a on ASM.ASSETMODELID=a.ID where 1=1 and ((asm.HISTORYTYPE=1 and asm.CHANGETYPE=4) or asm.HISTORYTYPE=3) and asm.CHANGECONTENT is not null");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getManageDeptId()))
            sql.append(" and a.MANAGEDEPTID='" + assetReportHistoryDto.getManageDeptId() + "'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getMaterialCode()))
            sql.append(" and a.MATERIALCODE like '%" + assetReportHistoryDto.getMaterialCode() + "%'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampStart()))
            sql.append(" and ASM.CREATETIMESTAMP>=TO_DATE('" + VGUtility.toDateStr((VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampStart(), "yyyy/M/d")), "yyyy-MM-dd") + "','yyyy-MM-dd') ");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampEnd()))
            sql.append(" and ASM.CREATETIMESTAMP<=TO_DATE('" + VGUtility.toDateStr((VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampEnd(), "yyyy/M/d")), "yyyy-MM-dd") + "','yyyy-MM-dd') ");
        sql.append(" ) m ");
        if (!VGUtility.isEmpty(pageableDto))
            sql.append(" where m.RN between " + ((pageableDto.getPage() - 1) * pageableDto.getSize() + 1) + " and " + pageableDto.getPage() * pageableDto.getSize());

        sql.append(" ORDER BY m.CREATETIMESTAMP DESC");
        // 创建查询器
        SQLQuery query = session.createSQLQuery(sql.toString());
        List<Object> resultList = query.getResultList();
        List<AssetReportHistoryDto> list = new ArrayList<AssetReportHistoryDto>();
        if (resultList.size() > 0) {
            list = convertForHtml(resultList);
        }
        //总条数
        StringBuffer totalsql = new StringBuffer("select " +
                " a.* " +
                "from ASSETHISTORYMODEL asm " +
                "inner join ASSETASSETMODEL a on ASM.ASSETMODELID=a.ID where 1=1 and ((asm.HISTORYTYPE=1 and asm.CHANGETYPE=4) or asm.HISTORYTYPE=3) and asm.CHANGECONTENT is not null");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getManageDeptId()))
            totalsql.append(" and a.MANAGEDEPTID='" + assetReportHistoryDto.getManageDeptId() + "'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getMaterialCode()))
            totalsql.append(" and a.MATERIALCODE like '%" + assetReportHistoryDto.getMaterialCode() + "%'");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampStart()))
            totalsql.append(" and ASM.CREATETIMESTAMP>=TO_DATE('" + VGUtility.toDateStr((VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampStart(), "yyyy/M/d")), "yyyy-MM-dd") + "','yyyy-MM-dd') ");
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampEnd()))
            totalsql.append(" and ASM.CREATETIMESTAMP<=TO_DATE('" + VGUtility.toDateStr((VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampEnd(), "yyyy/M/d")), "yyyy-MM-dd") + "','yyyy-MM-dd') ");
        // 创建查询器
        SQLQuery totalquery = session.createSQLQuery(totalsql.toString());
        List<Object> totalList = totalquery.getResultList();
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", list);
        modelMap.addAttribute("total", totalList.size());
        return modelMap;
    }

    public List<AssetReportHistoryDto> convertForHtml(List<Object> resultList) {
        DictDto dictDto = new DictDto();
        DeptInfoDto deptInfoDto = new DeptInfoDto();
        UserInfoDto userInfoDto = new UserInfoDto();
        Set<String> dictIdSet = new HashSet<String>();
        Set<String> deptIdSet = new HashSet<String>();
        Set<String> userIdSet = new HashSet<String>();
        Set<String> materialCodeSet = new HashSet<String>();
        Set<String> codeLv1Set = new HashSet<String>();
        Map<String, DictDto> dictMap = new HashMap<String, DictDto>();
        Map<String, DictDto> dictCodeMap = new HashMap<String, DictDto>();
        Map<String, DeptInfoDto> deptMap = new HashMap<String, DeptInfoDto>();
        Map<String, UserInfoDto> userMap = new HashMap<String, UserInfoDto>();
        Map<String, DictDto> codeLv1Map = new HashMap<String, DictDto>();
        //获取所有需要查询数据库的id集合
        for (Object obj : resultList) {
            Object[] objs = (Object[]) obj;
            if (!VGUtility.isEmpty(objs[8]))
                dictIdSet.add(objs[8].toString());
            if (!VGUtility.isEmpty(objs[13]))
                dictIdSet.add(objs[13].toString());
            if (!VGUtility.isEmpty(objs[11]))
                deptIdSet.add(objs[11].toString());
            if (!VGUtility.isEmpty(objs[12]))
                userIdSet.add(objs[12].toString());
            if (!VGUtility.isEmpty(objs[9]))
                deptIdSet.add(objs[9].toString());
            if (!VGUtility.isEmpty(objs[10]))
                userIdSet.add(objs[10].toString());
            if (!VGUtility.isEmpty(objs[2])) {
                materialCodeSet.add(objs[2].toString());
                codeLv1Set.add(objs[2].toString().substring(0, 2));
            }
        }

        dictMap = baseService.getDictDtoByIdSet(dictIdSet);
        deptMap = baseService.getDeptDtoByIdSet(deptIdSet);
        userMap = baseService.getUserDtoByIdSet(userIdSet);
        dictCodeMap = baseService.getDictByMaterialCodeSet(materialCodeSet);
        codeLv1Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV1, codeLv1Set);
        List<AssetReportHistoryDto> list = new ArrayList<AssetReportHistoryDto>();
        for (Object obj : resultList) {
            Object[] objs = (Object[]) obj;
            AssetReportHistoryDto dto = new AssetReportHistoryDto();
            if (!VGUtility.isEmpty(objs[3])) {
                String content = objs[3].toString();
                if (content.contains(">"))
                    dto.setAssetMoveHistory("移动至" + content.substring(content.lastIndexOf(">") + 1));
                else
                    dto.setAssetMoveHistory(content);
            }
            if (!VGUtility.isEmpty(objs[6]))
                dto.setSpecAndModels(objs[6].toString());
            if (!VGUtility.isEmpty(objs[7]))
                dto.setAssetBrand(objs[7].toString());
            if (!VGUtility.isEmpty(objs[4]))
                dto.setAssetMoveDate(objs[4].toString());
            if (!VGUtility.isEmpty(objs[5]))
                dto.setAssetTypeStr(ASSET_TYPE.values()[VGUtility.toInteger(objs[5].toString())].name());
            if (!VGUtility.isEmpty(objs[14]))
                dto.setAssetStatusStr(ASSET_STATUS.values()[VGUtility.toInteger(objs[14].toString())].name());
            if (!VGUtility.isEmpty(objs[8])) {
                dictDto = dictMap.get(objs[8].toString());
                if (!VGUtility.isEmpty(dictDto))
                    dto.setUnitOfMeasStr(dictDto.getChsName());
            }
            if (!VGUtility.isEmpty(objs[13])) {
                dictDto = dictMap.get(objs[13].toString());
                if (!VGUtility.isEmpty(dictDto))
                    dto.setBelongLineStr(dictDto.getChsName());
            }
            if (!VGUtility.isEmpty(objs[11])) {
                deptInfoDto = deptMap.get(objs[11].toString());
                if (!VGUtility.isEmpty(deptInfoDto))
                    dto.setManageDeptStr(deptInfoDto.getDeptName());
            }
            if (!VGUtility.isEmpty(objs[12])) {
                userInfoDto = userMap.get(objs[12].toString());
                if (!VGUtility.isEmpty(userInfoDto))
                    dto.setManagerStr(userInfoDto.getChsName());
            }
            if (!VGUtility.isEmpty(objs[9])) {
                deptInfoDto = deptMap.get(objs[9].toString());
                if (!VGUtility.isEmpty(deptInfoDto))
                    dto.setUseDeptStr(deptInfoDto.getDeptName());
            }
            if (!VGUtility.isEmpty(objs[10])) {
                userInfoDto = userMap.get(objs[10].toString());
                if (!VGUtility.isEmpty(userInfoDto))
                    dto.setUseStr(userInfoDto.getChsName());
            }
            if (!VGUtility.isEmpty(objs[2])) {
                dto.setMaterialCode(objs[2].toString());
                dictDto = dictCodeMap.get(objs[2].toString());
                if (!VGUtility.isEmpty(dictDto))
                    dto.setCombinationAssetName(dictDto.getChsName());

                String tempStr = "";
                dictDto = codeLv1Map.get(objs[2].toString().substring(0, 2));
                if (!VGUtility.isEmpty(dictDto))
                    tempStr += dictDto.getChsName();
                if (!VGUtility.isEmpty(dictDto))
                    tempStr += dictDto.getChsName();
                dto.setCombinationAssetType(tempStr);
            }
            list.add(dto);
        }

        return list;
    }

    /********************* 资产移动历史 end *******************************/

    /********************* 资产丢失,报废 start *******************************/
    @Override
    public ModelMap QueryAssetLoseHistory(ISearchExpression searchExpression, PageableDto pageableDto) {
        // 通过hibernate获得Session
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        // 通过session获得标准生成器 CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        // 标准化对象查询
        CriteriaQuery<AssetReduceModel> assetCriteria = builder.createQuery(AssetReduceModel.class);
        // 获取根
        Root<AssetReduceModel> root = assetCriteria.from(AssetReduceModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        // 根据查询器添加顺序
        Order order = builder.asc(root.get("createTimestamp"));
        assetCriteria.orderBy(order);
        // 创建查询器
        Query<AssetReduceModel> query = session.createQuery(assetCriteria);
        // 分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize())
                    .setMaxResults(pageableDto.getSize());
        List<AssetReduceModel> modelList = query.getResultList();
        // convert
        List<AssetReportHistoryDto> dtoList = new ArrayList<AssetReportHistoryDto>();
        for (AssetReduceModel model : modelList) {
            dtoList.addAll(convertModelToDtoForReduce(model));
        }
        // total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetReduceModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    public List<AssetReportHistoryDto> convertModelToDtoForReduce(AssetReduceModel model) {
        List<AssetReportHistoryDto> list = new ArrayList<AssetReportHistoryDto>();
        String hql = "from AssetReduceTempModel where 1=1 ";
        if (!VGUtility.isEmpty(model.getId())) {
            hql += "and assetReduceModelId = " + "'" + model.getId() + "'";
        }
        TypedQuery<AssetReduceTempModel> query = entityManager.createQuery(hql, AssetReduceTempModel.class);
        List<AssetReduceTempModel> resultList = query.getResultList();
        for (AssetReduceTempModel temp : resultList) {
            AssetReportHistoryDto dto = new AssetReportHistoryDto();
            AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
            dto.setAssetCode(asset.getAssetCode());
            dto.setAssetChsName(asset.getAssetChsName());
            dto.setAssetLineId(asset.getAssetLineId());
            dto.setAssetLineStr(asset.getAssetLineStr());
            dto.setAssetStatus(asset.getAssetStatus());
            dto.setAssetStatusStr(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())].name());
            dto.setAssetType(asset.getAssetType());
            dto.setAssetTypeStr(asset.getAssetTypeStr());
            dto.setCombinationAssetName(asset.getCombinationAssetName());
            dto.setCombinationAssetType(asset.getCombinationAssetType());
            dto.setManageDeptId(asset.getManageDeptId());
            dto.setManageDeptStr(asset.getManageDeptStr());
            dto.setManagerId(asset.getManagerId());
            dto.setManagerStr(asset.getManagerStr());
            dto.setSpecAndModels(asset.getSpecAndModels());
            dto.setUnitOfMeasId(asset.getUnitOfMeasId());
            dto.setUnitOfMeasStr(asset.getUnitOfMeasStr());
            dto.setUseDeptId(asset.getUseDeptId());
            dto.setUseDeptStr(asset.getUseDeptStr());
            dto.setUserId(asset.getUserId());
            dto.setUseStr(asset.getUseStr());
            dto.setBelongLineStr(asset.getBelongLineStr());
            dto.setMaterialCode(asset.getMaterialCode());
            list.add(dto);
        }
        return list;
    }

    /********************* 资产丢失,报废 end *******************************/

    /********************* 资产可用性统计 start *******************************/
    @Override
    public ModelMap QueryAssetUsablitityHistory(AssetUsabilityHistoryDto assetUsabilityHistoryDto) {
        // 通过hibernate获得Session
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        StringBuffer sql = new StringBuffer("select a.MANAGEDEPTID,substr(MATERIALCODE,0,2),"
                + "sum(case when  a.ASSETSTATUS = 0 then 1 else 0  end) as use,"
                + "sum(case when  a.ASSETSTATUS = 7 then 1 else 0  end) as idle,"
                + "sum(case when  a.ASSETSTATUS = 2 then 1 else 0  end) as sealup,"
                + "sum(case when  a.ASSETSTATUS = 6 then 1 else 0  end) as disuse,"
                + "sum(case when  a.ASSETSTATUS = 1 then 1 else 0  end) as borrow"
                + " from ASSETASSETMODEL a where 1=1");
        if (!VGUtility.isEmpty(assetUsabilityHistoryDto.getManageDeptId()))
            sql.append(" and a.MANAGEDEPTID='" + assetUsabilityHistoryDto.getManageDeptId() + "'");
        if (!VGUtility.isEmpty(assetUsabilityHistoryDto.getMaterialCode()))
            sql.append(" and a.MATERIALCODE like '%" + assetUsabilityHistoryDto.getMaterialCode() + "%'");

        sql.append(" group by a.MANAGEDEPTID,substr(MATERIALCODE,0,2)");
        // System.out.println("**********start************");
        // System.out.println(sql.toString());
        // System.out.println("**********end************");
        // 创建查询器
        SQLQuery query = session.createSQLQuery(sql.toString());
        List<Object> resultList = query.getResultList();
        List<AssetUsabilityHistoryDto> list = new ArrayList<AssetUsabilityHistoryDto>();
        DictDto dictDto = new DictDto();
        Set<String> codeLv1Set = new HashSet<String>();
        Map<String, DictDto> codeLv1Map = new HashMap<String, DictDto>();
        if (resultList.size() > 0) {
            for (Object obj : resultList) {
                Object[] objs = (Object[]) obj;
                codeLv1Set.add(objs[1].toString());
            }

            codeLv1Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV1, codeLv1Set);
            for (Object obj : resultList) {
                Object[] objs = (Object[]) obj;
                AssetUsabilityHistoryDto dto = new AssetUsabilityHistoryDto();
                if (!VGUtility.isEmpty(objs[0].toString()))
                    dto.setManageDeptStr(((DeptInfoDto) baseService.getDeptInfo(objs[0].toString())).getDeptName());
                if (!VGUtility.isEmpty(objs[1].toString())) {
                    dictDto = codeLv1Map.get(objs[1].toString());
                    if (!VGUtility.isEmpty(dictDto))
                        dto.setAssetTypeStr(dictDto.getChsName());
                }
                dto.setMaterialCode(objs[1].toString());
                dto.setUse(objs[2].toString());
                dto.setIdle(objs[3].toString());
                dto.setSealup(objs[4].toString());
                dto.setDisuse(objs[5].toString());
                dto.setBorrow(objs[6].toString());
                list.add(dto);
            }
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", list);
        modelMap.addAttribute("total", list.size());
        return modelMap;
    }

    public AssetUsabilityHistoryDto convertDtoToDto(AssetUsabilityHistoryDto assetUsabilityHistoryDto) {
        if (!VGUtility.isEmpty(assetUsabilityHistoryDto.getManageDeptId()))
            assetUsabilityHistoryDto.setManageDeptStr(
                    ((DeptInfoDto) baseService.getDeptInfo(assetUsabilityHistoryDto.getManageDeptId())).getDeptName());
        if (!VGUtility.isEmpty(assetUsabilityHistoryDto.getMaterialCode()))
            assetUsabilityHistoryDto.setAssetTypeStr(
                    baseService.getAssetNameByMaterialCode(assetUsabilityHistoryDto.getMaterialCode()));
        return assetUsabilityHistoryDto;
    }
    /********************* 资产可用性统计 end *******************************/
}
