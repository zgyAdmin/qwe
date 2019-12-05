package com.turing.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.turing.entity.SysUsers;

public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 SysUsers sysUsers = (SysUsers) request.getSession().getAttribute("user");
		 if(sysUsers!=null) {
			 return true;
		 }else {
			 response.sendRedirect("/");
			 return false;
		 }
	}
}
