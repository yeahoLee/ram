package com.mine.product.czmtr.ram.asset.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mine.product.czmtr.ram.flowable.dto.WorksDto;
import com.mine.product.czmtr.ram.flowable.service.IFlowableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialReceiptDto;
import com.mine.product.czmtr.ram.asset.service.IAssetTempService;
import com.mine.product.czmtr.ram.asset.service.IMaterialReceiptService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;

import net.bytebuddy.implementation.bytecode.Throw;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class MaterialReceiptPageController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialReceiptPageController.class);

    @Autowired
    private IMaterialReceiptService materialReceiptService;
    @Autowired
    private IAssetTempService assetTempService;
    @Autowired
    private IBaseService baseService;

    @Autowired
    private IFlowableService flowableService;

    @GetMapping(value = "/receiptList")
    public String receiptList(ModelMap map) {
        logger.info("Enter Receipt List Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        return "asset/materialReceiptList";
    }

    @GetMapping(value = "/addReceipt")
    public String addReceipt(HttpServletRequest request) {
        logger.info("Enter Add Receipt Page");
        if (!baseService.getPermCheck("zcxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产添加");
        }
        request.getSession().removeAttribute("materials");
        List<AssetAssetDto> materials = new ArrayList<AssetAssetDto>();
        request.getSession().setAttribute("materials", materials);
        return "asset/addMaterialReceipt";
    }

    @GetMapping(value = "/updateReceipt")
    public String updateReceipt(String id, ModelMap modelMap) {
        logger.info("Enter Update Receipt Page");
        if (!baseService.getPermCheck("zcxzd_update")) {
            throw new RuntimeException("该用户没有权限进行资产修改");
        }
        modelMap.addAttribute("receiptDto", materialReceiptService.getMaterialReceiptById(id));
        return "asset/updateMaterialReceipt";
    }
	
	/*
	@GetMapping(value = "/asset_update")
	public String assetUpdatePage(@RequestParam String assetId, ModelMap modelMap) {
		logger.info("Enter Asset Update Page");


		modelMap.addAttribute("assetDto", assetService.getAssetByAssetId(assetId));
		return "asset/assetAssetUpdate";
	}
	*/

    @GetMapping(value = "/lookReceipt")
    public String lookReceipt(String id, ModelMap modelMap) {
        logger.info("Look Receipt Page");
        MaterialReceiptDto dto = materialReceiptService.getMaterialReceiptById(id);
        WorksDto worksDto = flowableService.getWorksModelByBusinessKey(id);
        if (!VGUtility.isEmpty(worksDto)) {
            modelMap.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }
        modelMap.addAttribute("receiptDto", dto);
        

//		Map<String, Object> params = new HashMap<String, Object>();
//		PageableDto pageable = new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows));
//		
//		String hql = "from AssetTempModel m where 1=1";
//		if (!VGUtility.isEmpty(id)) {
//			hql += " and m.recId = :recId ";
//			params.put("recId", id);
//		}
//		hql += " order by m.createTimestamp desc";
//		
//		List<AssetAssetDto> materials = assetTempService.getAssetTempByRecId(id);
//		if(!VGUtility.isEmpty(materials)) {
//			//List<AssetAssetDto> materials = assetService.findAssetByHeadId(id);
//			modelMap.addAttribute("materials", materials);
//		}else {
//			materials = new ArrayList<AssetAssetDto>();
//			modelMap.addAttribute("materials", materials);
//		}

        return "asset/materialReceiptVew";
    }
}
