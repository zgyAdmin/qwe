package com.turing.server.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turing.entity.SysUsers;
import com.turing.entity.SysUsersExample;
import com.turing.entity.SysUsersExample.Criteria;
import com.turing.mapper.SysUsersMapper;
import com.turing.server.SysUsersServer;

@Service
public class SysUsersServerImpl implements SysUsersServer{

	@Autowired
	private SysUsersMapper sysUsersMapper;
	
	/**
	  *  登录的方法
	 */
	@Override
	public SysUsers login(SysUsers sysUsers) {
		SysUsersExample sysUsersExample = new SysUsersExample();
		Criteria criteria = sysUsersExample.createCriteria();
		criteria.andLoginIdEqualTo(sysUsers.getLoginId()).andPasswordEqualTo(sysUsers.getPassword());
		List<SysUsers> list = sysUsersMapper.selectByExample(sysUsersExample);
		if(list.size()!=0) {
			return list.get(0);
		}
		return null;
	}

	
}
