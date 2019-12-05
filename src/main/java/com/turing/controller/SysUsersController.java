package com.turing.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.turing.entity.SysUsers;
import com.turing.server.SysUsersServer;

@Controller
public class SysUsersController {

	@Autowired
	private SysUsersServer sysUsersMapper;
	
	/***
	 * 处理登录的方法
	 * @param sysUsers
	 * @param session
	 * @return
	 */
	@PostMapping("/login")
	public String login(SysUsers sysUsers,HttpSession session) {
		SysUsers sysUsers2 = sysUsersMapper.login(sysUsers);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String time = df.format(new Date());
		if(sysUsers2!=null) {
			session.setAttribute("user", sysUsers2);
			session.setAttribute("times", time);
			return "/main";
		}
		return "index2";
	}
}
