package com.mine.product.czmtr.ram.base.service;

import org.springframework.stereotype.Service;

@Service
public class CommonUtils implements ICommonUtils{

	/**
	 * 验证密码强度
	 * @param pwd
	 * @return
	 */
	@Override
	public Boolean RegexPWD(String pwd){
        String PW_PATTERN = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,}$";
        return pwd.matches(PW_PATTERN);
    }
}
