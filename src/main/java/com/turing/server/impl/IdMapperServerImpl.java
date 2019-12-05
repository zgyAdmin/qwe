package com.turing.server.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turing.entity.IdMapping;
import com.turing.entity.IdMappingExample;
import com.turing.mapper.IdMappingMapper;
import com.turing.server.IdMapperServer;

@Service
public class IdMapperServerImpl implements IdMapperServer {

	@Autowired
	private IdMappingMapper idMappingMapper;
	
	@Override
	public int save(IdMapping idMapping) {
		return idMappingMapper.insert(idMapping);
	}

	@Override
	public int update(IdMapping idMapping) {
		return idMappingMapper.updateByPrimaryKey(idMapping);
	}

	@Override
	public IdMapping select(long id) {
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andOrderIdEqualTo(id);
		List<IdMapping> list = idMappingMapper.selectByExample(idMappingExample);
		if(list.size()!=0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int delete(long id) {
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andOrderIdEqualTo(id);
		return idMappingMapper.deleteByExample(idMappingExample);
	}

	@Override
	public List<IdMapping> list(IdMappingExample idMappingExample) {
		return idMappingMapper.selectByExample(idMappingExample);
	}
}
