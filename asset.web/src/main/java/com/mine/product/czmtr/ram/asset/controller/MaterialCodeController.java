package com.mine.product.czmtr.ram.asset.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mine.product.czmtr.ram.asset.dto.MaterialCodeTempDto;
import com.mine.product.czmtr.ram.asset.service.IMaterialCodeService;

@Controller
//@RequestMapping(value = "/asset/")	
//@SessionAttributes(value = { "LoginUserInfo" })
public class MaterialCodeController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialCodeController.class);

    @Autowired
    private IMaterialCodeService materialCodeService;

    @PostMapping(value = "getMaterialCode")
    @ResponseBody
    public String getMaterialCode(String code) {
        logger.info("Get MaterialCode By Code");
        String materialCode = materialCodeService.getMaterialCodeByAssetCode(code);
        return materialCode;
    }

    @PostMapping(value = "getMultipleMaterialCode")
    @ResponseBody
    public List<MaterialCodeTempDto> getMultipleMaterialCode(String code, Integer num) {
        logger.info("Get Multiple MaterialCode By Code And Num");
        List<MaterialCodeTempDto> codes = materialCodeService.getMultipleMaterialCode(code, num);
        return codes;
    }
}
