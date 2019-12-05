package com.turing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turing.entity.SysMenus;
import com.turing.server.SysMenusService;
import com.turing.util.TreeNodeUtil;

@Controller
public class SysmeunsController {

	@Autowired
	private SysMenusService sysMenusService;
	
	/**
	 *  创造树
	 * @return
	 */
	@PostMapping("/menus")
	@ResponseBody
	public List<SysMenus> all(){
		List<SysMenus> list = sysMenusService.all();
		List<SysMenus> treesList = TreeNodeUtil.getFatherList(list);
		return treesList;
	}
}
