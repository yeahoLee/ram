package com.mine.product.czmtr.ram.outapi.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.vgtech.platform.common.utility.VGUtility;

@Component(value = "expaiSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        String messageUrl = request.getParameter("messageUrl");
        if (!VGUtility.isEmpty(messageUrl)) {
            request.getRequestDispatcher(messageUrl).forward(request, response);
        } else {
            super.onAuthenticationSuccess(request, response, auth);
        }
    }
}