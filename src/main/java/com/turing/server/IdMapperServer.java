package com.turing.server;


import java.util.List;

import com.turing.entity.IdMapping;
import com.turing.entity.IdMappingExample;

public interface IdMapperServer {

	int save(IdMapping idMapping);
	
	int update(IdMapping idMapping);
	
	IdMapping select(long id);
	
	int delete(long id);
	
	List<IdMapping> list(IdMappingExample idMappingExample);
}
