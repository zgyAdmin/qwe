package com.turing.server.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turing.entity.Material;
import com.turing.mapper.MaterialMapper;
import com.turing.server.MaterialServer;

@Service
public class MaterialServerImpl implements MaterialServer {

	@Autowired
	private MaterialMapper materialMapper;
	
	/**
	  *  查询所有
	 */
	@Override
	public List<Material> materials() {
		return materialMapper.selectByExample(null);
	}

	/**
	 *  添加
	 */
	@Override
	public int insert(Material material) {
		return materialMapper.insert(material);
	}

	/**
	  * 查询单个
	 */
	@Override
	public Material getMaterialById(long id) {
		return materialMapper.selectByPrimaryKey(id);
	}

}
