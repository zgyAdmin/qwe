package com.turing.server.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.turing.entity.IdMappingExample;
import com.turing.entity.Orders;
import com.turing.entity.OrdersExample;
import com.turing.entity.OrdersExample.Criteria;
import com.turing.mapper.OrdersMapper;
import com.turing.server.OrdersServer;

@Service
public class OrdersServerImpl implements OrdersServer {

	@Autowired
	private OrdersMapper ordersMapper;
	
	
	/**
	  * 添加订单需求
	 */
	@Override
	public int insert(Orders orders) {
		return ordersMapper.insert(orders);
	}


	/**
	 *  查询所有需求
	 */
	@Override
	public List<Orders> list() {
		return ordersMapper.selectByExample(null);
	}

	/***
	 * 根据流水号查询单条需求信息
	 */
	@Override
	public Orders selectOne(String orderNum) {
		OrdersExample ordersExample = new OrdersExample();
	 	Criteria criteria =  ordersExample.createCriteria();
	 	criteria.andOrderNumEqualTo(orderNum);
		return ordersMapper.selectByExample(ordersExample).get(0);
	}


	//查询所有的需求以及id对应表
	@Override
	public PageInfo selecctAll(int page,int rows,String code,String name,String status) {
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> list = ordersMapper.selectAll(code, name, status);
		PageInfo pageInfo = new PageInfo<>(list);
		return pageInfo;
	}


	/**
	 * 删除
	 */
	@Override
	public int delete(long id) {
		return ordersMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 查询
	 */
	@Override
	public Orders selectOrder(long id) {
		return ordersMapper.selectByPrimaryKey(id);
	}

	/**
	 * 修改
	 */
	@Override
	public int update(Orders orders) {
		return ordersMapper.updateByPrimaryKey(orders);
	}


	@Override
	public List<Map<String, Object>> selectAllByStatus() {
		return ordersMapper.selectAllByStatus();
	}

}
