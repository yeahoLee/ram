package com.mine.product.czmtr.ram.asset.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.dto.MaterialReceiptDto;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.asset.service.IAssetTempService;
import com.mine.product.czmtr.ram.asset.service.IMaterialCodeService;
import com.mine.product.czmtr.ram.asset.service.IMaterialReceiptService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/receipt/")
@SessionAttributes(value = {"LoginUserInfo"})
public class MaterialReceiptController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialReceiptController.class);

    @Autowired
    private IMaterialCodeService materialCodeService;
    @Autowired
    private IMaterialReceiptService materialReceiptService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IAssetTempService assetTempService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IBaseService baseService;

    /***
     * 资产新增单 DataGrid
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "/receipt_datagrid")
    @ResponseBody
    public Map<String, Object> receiptDatagrid(MaterialReceiptDto dto,
                                               @RequestParam(defaultValue = "1") String page,
                                               @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Receipt Model By Query For DataGrid");
        Map<String, Object> params = new HashMap<String, Object>();
        PageableDto pageable = new PageableDto(Integer.parseInt(page), Integer.parseInt(rows));
	    UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
	    dto.setPersonId(userInfoDto.getId());

        String hql = "from MaterialReceiptModel m where 1=1";
        if (!VGUtility.isEmpty(dto.getPersonId())) {
            hql += " and m.personId = :personId ";
            params.put("personId", dto.getPersonId());
        }
        if (!VGUtility.isEmpty(dto.getRunningNum())) {
            hql += " and m.runningNum like :runningNum ";
            params.put("runningNum", "%" + dto.getRunningNum() + "%");
        }
        if (!VGUtility.isEmpty(dto.getReceiptName())) {
            hql += " and m.receiptName like :receiptName ";
            params.put("receiptName", "%" + dto.getReceiptName() + "%");
        }
        if (!VGUtility.isEmpty(dto.getSourceType())) {
            hql += " and m.sourceType = :sourceType ";
            params.put("sourceType", IAssetService.SOURCE_TYPE.values()[Integer.parseInt(dto.getSourceType())]);
        }
        if (!VGUtility.isEmpty(dto.getReceiptStatus())) {
            hql += " and m.receiptStatus = :receiptStatus ";
            params.put("receiptStatus", FlowableInfo.WORKSTATUS.values()[Integer.parseInt(dto.getReceiptStatus())]);
        }
        hql += " order by m.createTimestamp desc";

        PageDto<MaterialReceiptDto> pageDto = materialReceiptService.getMaterialReceipt(hql, params, pageable);
        List<MaterialReceiptDto> materialReceiptDtos = pageDto.getRowData();
        for (MaterialReceiptDto tempDto : materialReceiptDtos)
            tempDto.setAssetCount(Integer.toString(assetTempService.getAssetTempCountByRecId(tempDto.getId())));

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", materialReceiptDtos);
        result.put("total", pageDto.getTotalCount());
        return result;
    }

    @PostMapping(value = "/create_receipt")
    @ResponseBody
    public MaterialReceiptDto createReceipt(MaterialReceiptDto dto, String assetList) {
        logger.info("Create Receipt");
        if (VGUtility.isEmpty(dto.getReceiptName())) throw new RuntimeException("资产新增单名称不能为空！");
        if (VGUtility.isEmpty(dto.getReason())) throw new RuntimeException("新增原因不能为空！");

        String runningNum = "XZ-";
        if (IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("投资"))
            runningNum += "TZ-";
        else if (IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("捐入")) {
//            runningNum += "JZ-";
            runningNum += "JR-";
        }
        else if (IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("盘盈"))
            runningNum += "PY-";
        else
            throw new RuntimeException("来源方式格式不正确！");
        runningNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        dto.setRunningNum(materialReceiptService.getMaxRunningNum(runningNum));

        dto.setReceiptStatus(Integer.toString(FlowableInfo.WORKSTATUS.拟稿.ordinal()));

        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        dto.setPersonId(userInfoDto.getId());
        ArrayList<AssetAssetDto> assetDtoList = JSON.parseObject(assetList, new TypeReference<ArrayList<AssetAssetDto>>() {
        });
        if (assetDtoList.size() == 0) {
            throw new RuntimeException("物资信息不能为空！");
        }
        //校验添加的资产是否与选中的物资类型一致；
        List<String> list = materialReceiptService.checkAssetProduceType(assetDtoList, dto.getProduceType());
        if (list.size() > 0) {
            throw new RuntimeException("以下物资类型与所选择的物资的类型不一致，请检查！" + Arrays.toString(list.toArray()));
        }

        MaterialReceiptDto mateReceDto = materialReceiptService.createMaterialReceipt(dto);
        for (AssetAssetDto assetDto : assetDtoList) {
            assetDto.setRecId(mateReceDto.getId());
            assetDto.setSourceType(dto.getSourceType());
            assetTempService.createAssetTemp(assetDto);
        }
        return mateReceDto;
    }


    @PostMapping(value = "/update_receipt")
    @ResponseBody
    public String updateReceipt(MaterialReceiptDto dto) {
        logger.info("Update Receipt");
        if (VGUtility.isEmpty(dto.getReceiptName())) throw new RuntimeException("资产新增单名称不能为空！");
        if (VGUtility.isEmpty(dto.getReason())) throw new RuntimeException("新增原因不能为空！");
        if (!(IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("投资")
                || IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("捐入")
                || IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("盘盈")))
            throw new RuntimeException("来源方式格式不正确！");
        materialReceiptService.updateMaterialReceipt(dto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "/update_receipt2")
    @ResponseBody
    public String updateReceipt2(MaterialReceiptDto dto, String assetList) {
        logger.info("Update Receipt");
        if (VGUtility.isEmpty(dto.getReceiptName())) throw new RuntimeException("资产新增单名称不能为空！");
        if (VGUtility.isEmpty(dto.getReason())) throw new RuntimeException("新增原因不能为空！");
        if (!(IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("投资")
                || IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("捐入")
                || IAssetService.SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourceType())].name().equals("盘盈")))
            throw new RuntimeException("来源方式格式不正确！");

        ArrayList<AssetAssetDto> assetDtoList = JSON.parseObject(assetList, new TypeReference<ArrayList<AssetAssetDto>>() {
        });
        if (assetDtoList.size() == 0) {
            throw new RuntimeException("物资信息不能为空！");
        }
        //校验添加的资产是否与选中的物资类型一致；
        List<String> list = materialReceiptService.checkAssetProduceType(assetDtoList, dto.getProduceType());
        if (list.size() > 0) {
            throw new RuntimeException("以下物资类型与所选择的物资的类型不一致，请检查！" + Arrays.toString(list.toArray()));
        }
        //删除之前的数据
        assetTempService.deleteAssetTempByRecId(dto.getId());
        //新增数据
        for (AssetAssetDto assetDto : assetDtoList) {
            assetDto.setRecId(dto.getId());
            assetDto.setSourceType(dto.getSourceType());
            assetTempService.createAssetTemp(assetDto);
        }
        materialReceiptService.updateMaterialReceipt(dto);

        return "{\"success\":true}";
    }

    @PostMapping(value = "/create_temp_asset")
    @ResponseBody
    public String createTempAsset(AssetAssetDto dto) {
        logger.info("c:Create Temp Asset By Receipt Id {}", dto.getRecId());
        assetTempService.createAssetTemp(dto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "/update_temp_asset")
    @ResponseBody
    public String updateTempAsset(AssetAssetDto dto) {
        logger.info("c:Update Temp Asset By Receipt Id {}", dto.getRecId());
        assetTempService.updateAssetTemp(dto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "/update_temp_asset_batch")
    @ResponseBody
    public String updateTempAssetBatch(AssetAssetDto dto, String assetList) {
        logger.info("c:Update Temp Asset By Receipt Id {}", dto.getRecId());
        ArrayList<AssetAssetDto> assetDtoList = JSON.parseObject(assetList, new TypeReference<ArrayList<AssetAssetDto>>() {
        });
        if (assetDtoList.size() == 0) {
            throw new RuntimeException("物资信息不能为空！");
        }


        assetTempService.updateAssetTemp(dto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "/delete_temp_asset")
    @ResponseBody
    public String deleteTempAsset(String deleteIdListStr, String recId) {
        logger.info("c:Delete Temp Asset By asset Id List {}", deleteIdListStr);

        String[] deleteIdList = deleteIdListStr.substring(0, deleteIdListStr.length() - 1).split(",");
        assetTempService.deleteTempAssetByIdList(deleteIdList, recId);
        return "{\"success\":true}";
    }

    @PostMapping(value = "/deleteReceipt")
    @ResponseBody
    public String deleteReceipt(@RequestParam(required = true) String id) {
        logger.info("delete Receipt By Id id=" + id);
        MaterialReceiptDto dto = materialReceiptService.getMaterialReceiptById(id);
        if (!(FlowableInfo.WORKSTATUS.values()[VGUtility.toInteger(dto.getReceiptStatus())].name()).equals("拟稿")) {
            throw new RuntimeException("非拟稿状态无法删除！");
        } else {
            materialReceiptService.deleteMaterialReceipt(id);

            Map<String, Object> params = new HashMap<String, Object>();

            String hql = "from AssetTempModel m where 1=1";
            if (!VGUtility.isEmpty(id)) {
                hql += " and m.recId = :recId ";
                params.put("recId", id);
            }

            PageDto<AssetAssetDto> assetPageDto = assetTempService.getAssetTempByRecId(hql, params, null);
            if (assetPageDto.getRowData().size() > 0)
                assetTempService.deleteAssetTempByRecId(id);

            return "{\"success\":true}";
        }
    }

    @PostMapping(value = "/get_materials")
    @ResponseBody
    public Map<String, Object> getMaterials(String recId) {
        logger.info("Get Materials By Receipt Id {}", recId);

        Map<String, Object> params = new HashMap<String, Object>();
//		PageableDto pageable = new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows));

        String hql = "from AssetTempModel m where 1=1";
        if (!VGUtility.isEmpty(recId)) {
            hql += " and m.recId = :recId ";
            params.put("recId", recId);
        }
        hql += " order by m.createTimestamp desc";

        PageDto<AssetAssetDto> assetPageDto = assetTempService.getAssetTempByRecId(hql, params, null);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", assetPageDto.getRowData());
        result.put("total", assetPageDto.getTotalCount());
        return result;
    }

    @GetMapping(value = "/getAssetById")
    @ResponseBody
    public AssetAssetDto getAssetById(HttpServletRequest request, String id, ModelMap modelMap) {
        logger.info("Get Asset By Id");
        List<AssetAssetDto> materials = (List<AssetAssetDto>) request.getSession().getAttribute("materials");
        AssetAssetDto dto = new AssetAssetDto();
        for (AssetAssetDto a : materials) {
            if (id.equals(a.getId())) {
                dto = a;
                String assetTypeName = baseService.getAssetTypeByMaterialCode(dto.getMaterialCode());
                dto.setAssetLeave1Str(assetTypeName);
                String assetName = baseService.getAssetNameByMaterialCode(dto.getMaterialCode());
                dto.setAssetChsName(assetName);
                if (!VGUtility.isEmpty(dto.getSavePlaceId())) {
                    DictDto savePlaceDto = dictService.getCommonCode(dto.getSavePlaceId());
                    if (!VGUtility.isEmpty(savePlaceDto)) {
                        dto.setSavePlaceCode(savePlaceDto.getCode());
                        dto.setSavePlaceName(savePlaceDto.getChsName());
                        dto.setSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
                    }
                }
            }
        }
        return dto;
    }

    @PostMapping(value = "/save_asset")
    @ResponseBody
    public String saveAsset(AssetAssetDto assetDto, HttpServletRequest request) {
        logger.info("Receipt save Asset");
//		assetService.commonCheckAssetDto(assetDto);

        List<AssetAssetDto> materials = (List<AssetAssetDto>) request.getSession().getAttribute("materials");

        if (VGUtility.isEmpty(assetDto.getId())) {
            assetDto.setId(UUID.randomUUID().toString());
            materials.add(assetDto);
        } else {
            Iterator<AssetAssetDto> iterator = materials.iterator();
            while (iterator.hasNext()) {
                AssetAssetDto temp = iterator.next();
                if (temp.getId().equals(assetDto.getId())) {
                    iterator.remove();
                }
            }
            materials.add(assetDto);
        }
        request.getSession().setAttribute("materials", materials);
        return "ok";
    }

    @RequestMapping(value = "/del_asset", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String delAsset(HttpServletRequest request, String id) throws Exception {
        logger.info("Delete Receipt Asset");
        List<AssetAssetDto> materials = (List<AssetAssetDto>) request.getSession().getAttribute("materials");
        Iterator<AssetAssetDto> iterator = materials.iterator();
        while (iterator.hasNext()) {
            AssetAssetDto temp = iterator.next();
            if (temp.getId().equals(id)) {
                iterator.remove();
            }
        }
        return "ok";
    }

    @RequestMapping(value = "/check_receipt", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String checkReceipt(@RequestParam String id) throws Exception {
        logger.info("Check Receipt Asset");
        assetService.finishApprove(id);
        return "{\"success\":true}";
    }

//	//导出上传模板
//	@GetMapping(value="/export_excel")
//    @ResponseBody
//    public ResponseEntity<byte[]> exportAssetStanBook(){
//        logger.info("Export Asset Excel ");
//        HttpHeaders headers = new HttpHeaders();
//        String fileName = "物资导入模板.xls";
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        try {
//            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("gb2312"), "iso-8859-1"));
//        } catch (UnsupportedEncodingException e) {
//            logger.error("exception when return controller",e);
//            throw new RuntimeException("响应出错");
//        }
//        /*
//        Map<String, Object> assetQueryMap = assetService.getAssetByQuerysForDataGrid(new ISearchExpression() {
//			@Override
//			public Object change(Object... arg0) {
//				CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
//				Root root = (Root) arg0[0];
//				return getPredicateByAssetDto(builder, root, assetDto);
//			}
//		}, null);
//        */
//        byte[] bytes = assetService.exportAssetStanBookByQuerys(new ArrayList<AssetAssetDto>());
//        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
//    }

    public static String getReceiptStatusOrdinal(String s) {
        for (IAssetService.SOURCE_TYPE c : IAssetService.SOURCE_TYPE.values()) {
            if (c.name().equals(s)) {
                return String.valueOf(c.ordinal());
            }
        }
        return null;
    }
}
