package com.turing.server.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turing.entity.SysMenus;
import com.turing.mapper.SysMenusMapper;
import com.turing.server.SysMenusService;

@Service
public class SysmenusServiceImpl implements SysMenusService {

	@Autowired
	private SysMenusMapper sysMenusMapper;
	
	/**
	  *  查询所有
	 */
	@Override
	public List<SysMenus> all() {
		return sysMenusMapper.selectByExample(null);
	}

}
