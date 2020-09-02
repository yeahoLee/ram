package com.mine.product.czmtr.ram.asset.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetInventoryDao;
import com.mine.product.czmtr.ram.asset.dao.MyAssetInventoryModelDao;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetInventoryDto;
import com.mine.product.czmtr.ram.asset.dto.AssetInventoryScopeDto;
import com.mine.product.czmtr.ram.asset.dto.AssetInventoryTempDto;
import com.mine.product.czmtr.ram.asset.dto.AssetUploadFileDto;
import com.mine.product.czmtr.ram.asset.dto.MyAssetInventoryDto;
import com.mine.product.czmtr.ram.asset.service.IAssetInventoryService;
import com.mine.product.czmtr.ram.asset.service.IAssetInventoryTempService;
import com.mine.product.czmtr.ram.asset.service.IAssetService.INVENTORY_OPERATION;
import com.mine.product.czmtr.ram.asset.service.IAssetService.INVENTORY_RESULT;
import com.mine.product.czmtr.ram.asset.service.IAssetService.INVENTORY_WAY;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;

@Controller
@RequestMapping(value = "/assetInventory/")
@SessionAttributes(value = {"LoginUserInfo"})
@Transactional(propagation = Propagation.REQUIRED)
public class AssetInventoryController {

    private static final Logger logger = LoggerFactory.getLogger(AssetInventoryController.class);

    @Autowired
    private IAssetInventoryService assetInventoryService;
    @Autowired
    private AssetInventoryDao assetInventoryDao;
    @Autowired
    private MyAssetInventoryModelDao myAssetInventoryModelDao;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IAssetInventoryService inventoryService;
    @Autowired
    private IAssetInventoryTempService inventoryTempService;

    /**************************************
     * 我的盘点单 start
     ******************************************************/
    /***
     * 导出excel
     */
    @GetMapping(value = "export_assetinventory_xls")
    @ResponseBody
    public void exportAssetInventoryFile(@RequestParam String assetInventoryDtoList, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Controller: export AssetInventory File");
        // HttpHeaders headers = new HttpHeaders();
        String prefix = "";
        String fileName = "盘点清单" + VGUtility.toDateStr(new Date(), "yyyy-MM-dd") + ".xls";
        if (fileName != null && fileName.length() > 0) {
            prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        List<String> list = new ArrayList<String>();

        if (!VGUtility.isEmpty(assetInventoryDtoList))
            list = Arrays.asList(assetInventoryDtoList.split(","));
        else
            throw new RuntimeException("资产列表不能为空！");

        byte[] bytes = inventoryTempService.exportAssetInventoryList(list);
        if ("pdf".equals(prefix.toLowerCase())) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Length", Integer.toString(bytes.length));
        } else {
            // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            // 2.设置文件头
            try {
                if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                    // IE的话，通过URLEncoder对filename进行UTF8编码
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    // 而其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1了
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                }
            } catch (Exception e) {
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        }

        try {
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            logger.error("Exception when write result", e);
            throw new RuntimeException("导出出错！");
        }
    }

    // headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    // try {
    // headers.setContentDispositionFormData("attachment", new
    // String(fileName.getBytes("gb2312"), "iso-8859-1"));
    // } catch (UnsupportedEncodingException e) {
    // logger.error("exception when return controller", e);
    // throw new RuntimeException("相应出错");
    // }
    //
    // List<String> list = new ArrayList<String>();
    //
    // if (!VGUtility.isEmpty(assetInventoryDtoList))
    // list = Arrays.asList(assetInventoryDtoList.split(","));
    // else
    // throw new RuntimeException("资产列表不能为空！");
    //
    // byte[] bytes = inventoryTempService.exportAssetInventoryList(list);
    // return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    //}

    /***
     * 导出所有excel
     */
    @GetMapping(value = "export_assetinventoryAll_xls")
    @ResponseBody
    public void exportAssetInventoryFileForAll(@RequestParam String myAssetInventoryId, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Controller: export all AssetInventory File");
        String fileName = "盘点清单" + VGUtility.toDateStr(new Date(), "yyyy-MM-dd") + ".xls";
        String prefix = "";
        List<String> list = inventoryTempService.assetInventoryList(myAssetInventoryId);
        byte[] bytes = inventoryTempService.exportAssetInventoryList(list);
        if ("pdf".equals(prefix.toLowerCase())) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Length", Integer.toString(bytes.length));
        } else {
            // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            // 2.设置文件头
            try {
                if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                    // IE的话，通过URLEncoder对filename进行UTF8编码
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    // 而其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1了
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                }
            } catch (Exception e) {
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        }

        try {
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            logger.error("Exception when write result", e);
            throw new RuntimeException("导出出错！");
        }
    }

    /***
     * 导入excel
     */
    @PostMapping(value = "import_assetinventory_xls")
    @ResponseBody
    public Map<String, Object> importAssetInventoryFile(AssetUploadFileDto uploadFileDto) {
        logger.info("Controller: Import AssetInventory File");
        int i = 1;
        MyAssetInventoryDto myAssetInventoryDto = new MyAssetInventoryDto();
        ModelMap modelMap = new ModelMap();
        List<AssetInventoryTempDto> assetInventoryDtoList = new ArrayList<AssetInventoryTempDto>();

        try {
            HSSFWorkbook wb = new HSSFWorkbook(uploadFileDto.getUploadFileData().getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            if (VGUtility.isEmpty(sheet))
                throw new RuntimeException("表单内容为空！");
            for (; i < sheet.getPhysicalNumberOfRows(); i++) {
                HSSFRow row = sheet.getRow(i);
                if (VGUtility.isEmpty(row.getCell(0)))
                    continue;

                int colNum = 0;
                AssetInventoryTempDto assetInventoryTempDto = new AssetInventoryTempDto();
                String myAssetInventoryId = baseService.getCellFormatValue(row.getCell(colNum++));
                myAssetInventoryDto.setId(myAssetInventoryId);
                assetInventoryTempDto.setMyAssetInventoryId(myAssetInventoryId);
                String Id = baseService.getCellFormatValue(row.getCell(colNum++));
                assetInventoryTempDto.setId(Id);
                //// 验证
                // Map<String, Object> map = inventoryTempService
                // .getAssetInventoryTempForDataGrid(new ISearchExpression() {
                // @Override
                // public Object change(Object... arg0) {
                // CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                // Root root = (Root) arg0[0];
                // Predicate predicate;
                // predicate = getPredicateByAssetInventoryTempDto(builder, root,
                //// assetInventoryTempDto);
                // return predicate;
                // }
                // }, null);
                // if (VGUtility.isEmpty(map))
                // throw new RuntimeException("该资产不在盘点单内,不允许导入");

                String opeartion = baseService.getCellFormatValue(row.getCell(colNum++));
                if (!VGUtility.isEmpty(opeartion))
                    assetInventoryTempDto.setOperation(
                            String.valueOf(INVENTORY_OPERATION.valueOf(INVENTORY_OPERATION.class, (opeartion))));

                String result = baseService.getCellFormatValue(row.getCell(colNum++));
                if (!VGUtility.isEmpty(result))
                    assetInventoryTempDto
                            .setResult(String.valueOf(INVENTORY_RESULT.valueOf(INVENTORY_RESULT.class, (result))));

                String way = baseService.getCellFormatValue(row.getCell(colNum++));
                if (!VGUtility.isEmpty(way))
                    assetInventoryTempDto
                            .setInventoryWay(String.valueOf(INVENTORY_WAY.valueOf(INVENTORY_WAY.class, (way))));

                assetInventoryTempDto.setRemark(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setAssetCode(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setAssetChsName(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setSpecAndModels(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setBuyDate(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setUnitOfMeasStr(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setEquiOrigValue(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setResidualValue(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setAssetStatus(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setSavePlaceStr(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setUseDeptStr(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setUseStr(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setManageDeptStr(baseService.getCellFormatValue(row.getCell(colNum++)));
                assetInventoryTempDto.setManagerStr(baseService.getCellFormatValue(row.getCell(colNum++)));
                inventoryTempService.UpdateAssetInventoryTempModel(assetInventoryTempDto);
                assetInventoryDtoList.add(assetInventoryTempDto);
            }
        } catch (Exception e) {
            logger.error("Exception when read Excel File", e);
            modelMap.addAttribute("errorMessage", "第" + i + "行的" + e.getMessage());
        }
        myAssetInventoryDto = assetInventoryService.getMyAssetInventoryDtoById(myAssetInventoryDto);
        modelMap.addAttribute("assetInventoryDtoList", assetInventoryDtoList);
        modelMap.addAttribute("myAssetInventoryDto", myAssetInventoryDto);
        return modelMap;
    }

    /**
     * 更新我的盘点单
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "update_myinventory")
    @ResponseBody
    public Map<String, Object> UpdateMyAssetInventory(MyAssetInventoryDto dto) {
        logger.info("Controller: Update MyAssetInventory" + dto.toString());
        inventoryTempService.UpdateMyAssetInventory(dto);
        ModelMap map = new ModelMap();
        map.put("id", dto.getId());
        map.put("success", "true");
        return map;
    }

    /**
     * 更新我的盘点清单
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "update_inventorytemp")
    @ResponseBody
    public Map<String, Object> UpdateAssetInventoryTemp(AssetInventoryTempDto dto) {
        logger.info("Controller: Update AssetInventoryTemp" + dto.toString());
        inventoryTempService.UpdateAssetInventoryTempModel(dto);
        ModelMap map = new ModelMap();
        map.put("id", dto.getId());
        map.put("success", "true");
        return map;
    }

    /**
     * 更新我的盘点清单
     *
     * @param dtolist
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "update_inventorytemplist")
    @ResponseBody
    public Map<String, Object> UpdateAssetInventoryTempList(String templist) {
        logger.info("Controller: Update AssetInventoryTempList");
        if (VGUtility.isEmpty(templist))
            throw new RuntimeException("盘点清单不能为空");
        List<AssetInventoryTempDto> dtolist = JSON.parseObject(templist, new TypeReference<ArrayList<AssetInventoryTempDto>>() {
        });
        for (AssetInventoryTempDto dto : dtolist) {
            inventoryTempService.UpdateAssetInventoryTempModel(dto);
        }
        ModelMap map = new ModelMap();
        map.put("success", "true");
        return map;
    }

    /**
     * 查询我的盘点清单单datagrid
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "inventorytemp_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetInventoryTempByQuerysForDataGrid(AssetInventoryTempDto dto,
                                                                        @RequestParam(defaultValue = "1") String page, @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get AssetSequestration ForDataGrid" + dto.toString());
        return inventoryTempService.getAssetInventoryTempForDataGridByDto(dto, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    /**
     * 提交我的盘点单
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "myinventorytemp_submit")
    @ResponseBody
    public Map<String, Object> submitMyAssetInventory(MyAssetInventoryDto dto) {
        logger.info("Controller: submitMyAssetInventory" + dto.toString());
        // 验证是否可以提交
        boolean flag = inventoryTempService.checkForSubmit(dto);
        if (!flag)
            throw new RuntimeException("当前盘点单有未盘点的资产,不允许提交!");
        else
            inventoryTempService.UpdateMyAssetInventoryStatus(dto);
        ModelMap map = new ModelMap();
        map.put("id", dto.getId());
        map.put("success", "true");
        return map;
    }

    /**
     * 撤回我的盘点单
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "myinventorytemp_recall")
    @ResponseBody
    public Map<String, Object> recallMyAssetInventory(MyAssetInventoryDto dto) {
        logger.info("Controller: recallMyAssetInventory");
        inventoryTempService.RecallMyInventory(dto);
        ModelMap map = new ModelMap();
        map.put("id", dto.getId());
        map.put("success", "true");
        return map;
    }

    /**
     * 拼接查询条件
     *
     * @param builder
     * @param root
     * @param dto
     * @return
     */
    private Predicate getPredicateByAssetInventoryTempDto(CriteriaBuilder builder, Root root,
                                                          AssetInventoryTempDto dto) {
        List<Predicate> andList = new ArrayList<>();
        Predicate finalPred = null;
        if (!VGUtility.isEmpty(dto.getManageDeptId()))
            andList.add(builder.equal(root.get("manageDeptId"), dto.getManageDeptId()));
        if (!VGUtility.isEmpty(dto.getAssetInventoryId()))
            andList.add(builder.equal(root.get("assetInventoryModel"),
                    assetInventoryDao.findById(dto.getAssetInventoryId()).get()));
        if (!VGUtility.isEmpty(dto.getMyAssetInventoryId()))
            andList.add(builder.equal(root.get("myAssetInventoryModel"),
                    myAssetInventoryModelDao.findById(dto.getMyAssetInventoryId()).get()));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
        return finalPred;
    }

    /**************************************
     * 我的盘点单 end
     *****************************************************/
    /**
     * 创建盘点单
     *
     * @param inventoryScopeList
     * @param assetList
     * @param dto
     * @return
     */
    @PostMapping(value = "assetinventroy_create")
    @ResponseBody
    public ModelMap inventoryCreate(String inventoryScopeList, String assetList, AssetInventoryDto dto) {
        logger.info("Controller: Create AssetInventory" + dto.toString());
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        List<AssetAssetDto> assetDtoList = JSON.parseObject(assetList, new TypeReference<ArrayList<AssetAssetDto>>() {
        });
        ArrayList<AssetInventoryScopeDto> assetInventoryScopeDtos = JSON.parseObject(inventoryScopeList,
                new TypeReference<ArrayList<AssetInventoryScopeDto>>() {
                });
        AssetInventoryDto assetInventoryDto = inventoryService.createInventory(assetDtoList, assetInventoryScopeDtos,
                userInfoDto, dto);
        ModelMap map = new ModelMap();
        map.put("dto", assetInventoryDto);
        map.put("success", "true");
        return map;
    }

    /**
     * 搜索盘点单datagrid
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "inventory_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetInventory(AssetInventoryDto dto,
                                                 @RequestParam(defaultValue = "1") String page, @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get AssetSequestration ForDataGrid" + dto.toString());
        return inventoryService.getAssetInventory(dto, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    /**
     * 删除单个盘点任务
     *
     * @param id
     * @return
     */
    @PostMapping(value = "assetinventory_delete")
    @ResponseBody
    public String deleteAssetInventory(String id) {
        logger.info("Controller: delete AssetInventory ForDataGrid" + id);
        inventoryService.deleteAssetInventory(id);
        return "{\"success\":true}";
    }


    /**
     * 查询我的盘点单 MyAssetinventory datagrid
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "myinventory_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetInventoryByQuerysForDataGrid(MyAssetInventoryDto dto,
                                                                    @RequestParam(defaultValue = "1") String page, @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get AssetSequestration ForDataGrid" + dto.toString());
        return inventoryService.getMyAssetInventory(dto,
                new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    @PostMapping(value = "inventoryscope_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetInventoryScopeByIdForDataGrid(AssetInventoryDto dto) {
        logger.info("Controller: Get AssetInventoryScope ForDataGrid" + dto.toString());
        List<AssetInventoryScopeDto> dtoList = inventoryService.getAssetInventoryScopeDtoList(dto.getId());
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", dtoList.size());
        return modelMap;
    }

    /**
     * 批量删除我的盘点清单
     *
     * @param assetInventoryList
     * @return
     */
    @PostMapping(value = "delete_assetInventorytemps")
    @ResponseBody
    public String deleteAssetInventoryTemps(String assetInventoryTempIdListStr) {
        logger.info("Controller: Delete AssetnventoryTemps" + assetInventoryTempIdListStr);
        inventoryService.deleteAssetInventoryTempByIds(assetInventoryTempIdListStr);
        return "{\"success\":true}";
    }

    /**
     * 删除单个盘点范围
     *
     * @param assetInventoryId
     * @return
     */
    @PostMapping(value = "delete_assetinventoryscope")
    @ResponseBody
    public String deleteAssetInventoryScrop(String assetInventoryScopeId) {
        logger.info("Controller: Delete AssetInventoryScope" + assetInventoryScopeId);
        inventoryService.deleteAssetInventoryScopeById(assetInventoryScopeId);
        return "{\"success\":true}";
    }

    /**
     * 查询我的盘点清单单datagrid
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "inventorytemp_find")
    @ResponseBody
    public Map<String, Object> getAssetInventoryTemp(String assetInventoryId,
                                                     @RequestParam(defaultValue = "1") String page, @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get AssetInventorytemp ForDataGrid" + assetInventoryId);

        return inventoryService.getMyAssetInventoryForDataGrid(assetInventoryId,
                PageRequest.of(Integer.parseInt(page), Integer.parseInt(rows)));
    }

    /**
     * 创建盘点清单datagrid
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "create_assetreceiveusetemp")
    @ResponseBody
    public String createAssetInventoryTemp(String assetInventoryId, String assetIdListStr) {
        logger.info("Controller: create AssetInventoryTemp ForDataGrid 				assetInventoryId:"
                + assetInventoryId + "     		assetIdListStr:" + assetIdListStr);
        inventoryService.addAssetInventoryTemp(assetInventoryId, assetIdListStr);
        return "{\"success\":true}";
    }

    /**
     * 创建盘点范围清单datagrid
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "create_assetinventoryscope")
    @ResponseBody
    public String createAssetInventoryTemp(AssetInventoryScopeDto assetInventoryScopeDto) {
        logger.info("Controller: Create AssetInventoryTemp ForDataGrid" + assetInventoryScopeDto.toString());
        inventoryService.addAssetInventoryScope(assetInventoryScopeDto);
        return "{\"success\":true}";
    }

    /**
     * 更新盘点单任务
     *
     * @return
     */
    @PostMapping(value = "update_assetinventory")
    @ResponseBody
    public String updateAssetInventory(AssetInventoryDto assetInventoryDto) {
        logger.info("Controller: Update AssetInventory ForDataGrid" + assetInventoryDto.toString());
        inventoryService.updateAssetInventory(assetInventoryDto);
        return "{\"success\":true}";
    }

    /**
     * 保存并发布盘点单任务
     *
     * @param inventoryScopeList
     * @param assetList
     * @param dto
     * @return
     */
    @PostMapping(value = "assetinventroy_createanddistribution")
    @ResponseBody
    public ModelMap createAndDistributionInventory(String inventoryScopeList, String assetList, AssetInventoryDto dto) {
        logger.info("Controller: inventoryCreateAndDistribution" + dto.toString());
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        List<AssetAssetDto> assetDtoList = JSON.parseObject(assetList, new TypeReference<ArrayList<AssetAssetDto>>() {
        });
        ArrayList<AssetInventoryScopeDto> assetInventoryScopeDtos = JSON.parseObject(inventoryScopeList,
                new TypeReference<ArrayList<AssetInventoryScopeDto>>() {
                });
        AssetInventoryDto assetInventoryDto = inventoryService.createAndDistributionInventory(assetDtoList,
                assetInventoryScopeDtos, userInfoDto, dto);
        ModelMap map = new ModelMap();
        map.put("id", assetInventoryDto.getId());
        map.put("success", "true");
        return map;
    }

    /**
     * 发布盘点任务
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "distribution_assetinventory")
    @ResponseBody
    public String distributionAssetinventory(String assetInventoryId) {
        logger.info("Controller: Distribution Assetinventory " + assetInventoryId);
        inventoryService.distributionInventory(assetInventoryId);
        return "{\"success\":true}";
    }

    /**
     * 更新并发布盘点单任务
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "update_and_distribution_assetinventory")
    @ResponseBody
    public String updateAndDistributionAssetInventory(AssetInventoryDto assetInventoryDto) {
        logger.info("Controller: update And Distribution AssetInventory ForDataGrid" + assetInventoryDto.toString());
        inventoryService.updateAndDistributionAssetInventory(assetInventoryDto);
        return "{\"success\":true}";
    }

    /**
     * 通过ID查询盘点任务
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "find_assetinventory_byid")
    @ResponseBody
    public AssetInventoryDto findAssetInventoryById(String assetInventoryId) {
        logger.info("Controller: Get AssetInventory By Id" + assetInventoryId);
        AssetInventoryDto dto = inventoryService.getAssetInventoryDtoById(assetInventoryId);
        return dto;
    }

    /**
     * 发起审批
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "do_Approval")
    @ResponseBody
    public String doApproval(String assetInventoryId) {
        logger.info("Controller: do AssetInventory Approval " + assetInventoryId);
        // 执行发起审批逻辑
        inventoryService.doApproval(assetInventoryId);
        return "{\"success\":true}";
    }

}
