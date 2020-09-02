/**
 * 
 */
package com.mine.product.czmtr.ram.main.util;

import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 李一豪
 *
 */
@Component(value="expaiSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)throws IOException, ServletException {
		String userName = ((SpringSecureUserInfo) auth.getPrincipal()).getUserInfo().getUserName();
		String workDetailUrl = request.getParameter("workDetailUrl");
        if (!VGUtility.isEmpty(workDetailUrl)) {
        	request.getRequestDispatcher(workDetailUrl).forward(request, response);
        }else{
            super.onAuthenticationSuccess(request,response,auth);
        }
	}
}
