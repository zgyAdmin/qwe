package com.turing.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.turing.entity.Enquire;
import com.turing.entity.EnquireDetail;
import com.turing.entity.IdMapping;
import com.turing.entity.IdMappingExample;
import com.turing.entity.Orders;
import com.turing.mapper.EnquireDetailMapper;
import com.turing.mapper.EnquireMapper;
import com.turing.mapper.IdMappingMapper;
import com.turing.mapper.OrdersMapper;

@Controller
public class ConcentratedController {

	@Autowired
	private EnquireMapper enquireMapper;
	
	@Autowired
	private IdMappingMapper idMappingMapper;
	
	@Autowired
	private EnquireDetailMapper enquireDetailMapper;
	
	@Autowired
	private OrdersMapper ordersMapper;
	
	/***
	 * 添加询价书
	 * @param id 采购计划的id
	 * @param enquire 询价书实体类
	 * @return 查询没有编制询价书的页面
	 */ 
	@PostMapping("/saveEnquire")
	public String saveEnquire(Enquire enquire,HttpSession session) {
		//获取采购计划书id
		long id = (long) session.getAttribute("id");
		//System.out.println(id);
		//添加询价书
		enquireMapper.insert(enquire);
		//System.out.println(enquire.getId());
		
		//根据采购id修改编号对照表的询价id
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andStockIdEqualTo(id);
		List<IdMapping> list = idMappingMapper.selectByExample(idMappingExample);
		//遍历然后逐个修改
		for (IdMapping idMapping : list) {
			idMapping.setEnquireId(enquire.getId());
			idMapping.setStatus("C001-70");
			idMappingMapper.updateByPrimaryKeySelective(idMapping);
			
			//根据每次遍历id查询orders
			Orders orders = ordersMapper.selectByPrimaryKey(idMapping.getOrderId());
			//拿到实体类将值添加到询价书明细表
			//封装数据
			EnquireDetail enquireDetail = new EnquireDetail();
			enquireDetail.setEnquire(enquire.getId());
			enquireDetail.setOrderId(orders.getId());
			enquireDetail.setMaterialCode(orders.getMaterialCode());
			enquireDetail.setMaterialName(orders.getMaterialName());
			enquireDetail.setAmount(orders.getAmount());
			enquireDetail.setMeasureUnit(orders.getMeasureUnit());
			enquireDetail.setStandard("Z000-1");
			enquireDetail.setFactory("江南皮革厂");
			enquireDetail.setProdYear("S003-1");
			//添加询价书明细
			enquireDetailMapper.insert(enquireDetail);
		}
		
		return "Apply_weibianzhi";
	}
	
	/***
	 * 查询所有的询价书
	 * @param page
	 * @param rows
	 * @return
	 */
	@GetMapping("/selectEns")
	@ResponseBody()
	public Map getEnquires(@RequestParam(defaultValue="1")int page,@RequestParam(defaultValue="3")int rows,
			String status) {
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> list = enquireMapper.selectEnquires(status);
		List<Map<String,Object>> tmpList=new ArrayList<Map<String,Object>>();
		Set<Long> keysSet = new HashSet<Long>();
		for(Map<String, Object> collisionMap : list){
			Long keys = (Long) collisionMap.get("id");
			int beforeSize = keysSet.size();
			keysSet.add(keys);
			int afterSize = keysSet.size();
			if(afterSize == beforeSize + 1){
			tmpList.add(collisionMap);
			}
		}
		PageInfo pageInfo = new PageInfo<>(tmpList);
		Map data = new HashMap();
		data.put("total", pageInfo.getTotal());
		data.put("rows", pageInfo.getList());
		return data;
	}
}
