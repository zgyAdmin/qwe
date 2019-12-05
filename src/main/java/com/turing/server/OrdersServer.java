package com.turing.server;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.turing.entity.Orders;

public interface OrdersServer {

	int insert(Orders orders);
	
	List<Orders> list();
	
	Orders selectOne(String orderNum);
	
	PageInfo selecctAll(int page,int rows,String code,String name,String status);
	
	int delete(long id);
	
	Orders selectOrder(long id) ;
	
	int update(Orders orders);
	
	List<Map<String, Object>> selectAllByStatus();
}
