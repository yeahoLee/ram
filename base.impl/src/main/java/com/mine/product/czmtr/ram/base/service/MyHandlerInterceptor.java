package com.mine.product.czmtr.ram.base.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 自定义拦截器
 * @author 杨杰
 *
 */
public class MyHandlerInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String paramvalue = "";
		Enumeration<String> parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			String param = parameterNames.nextElement();
			String[] values = request.getParameterValues(param);
			for(int i=0;i<values.length;i++) {
				paramvalue = StringEscapeUtils.escapeSql(values[i]);
			}
			request.setAttribute(param, paramvalue);
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	
}
