package com.turing.server;

import java.util.List;

import com.turing.entity.Material;

public interface MaterialServer {

	List<Material> materials();
	
	int insert(Material material);
	
	Material getMaterialById(long id);
}
