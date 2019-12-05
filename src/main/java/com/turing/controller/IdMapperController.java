package com.turing.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turing.entity.IdMapping;
import com.turing.entity.IdMappingExample;
import com.turing.entity.Orders;
import com.turing.entity.Stock;
import com.turing.entity.StockExample;
import com.turing.mapper.IdMappingMapper;
import com.turing.mapper.StockMapper;
import com.turing.server.IdMapperServer;
import com.turing.server.OrdersServer;

@Controller
public class IdMapperController {

	@Autowired
	private OrdersServer ordersServer;
	
	@Autowired
	private IdMapperServer idMapperServer;
	
	@Autowired
	private StockMapper stockMapper;
	
	@Autowired
	private IdMappingMapper idMapping;
	
	/**
	 *   编号对照
	 * @param orderNum
	 * @return
	 */
	@GetMapping("/saveIdMapper")
	public String saveIdMapper(String orderNum) {
		System.out.println(orderNum);
		//根据id查询一条需求
		Orders orders = ordersServer.selectOne(orderNum);
		IdMapping idMapping = new IdMapping();
		//拿到id封装到IdMapper实体类
		System.out.println(orders.getId());
		idMapping.setOrderId(orders.getId());
		idMapping.setStatus("C001-10");
		idMapperServer.save(idMapping);
		return "Order_newform";
	}
	
	/***
	  *  确认状态
	 * @param id
	 * @return
	 */
	@PostMapping("/updateIdMapper")
	@ResponseBody
	public String update(@RequestBody()long id[]) {
		//System.out.println(id.length);
		for (long l : id) {
			//根据前台传过来的数据查询
			IdMapping idMapping = idMapperServer.select(l);
			//修改值
			idMapping.setStatus("C001-20");
			//最后修改
			idMapperServer.update(idMapping);
		}
		return "";
	}
	
	/**
	  *  删除
	 * @param id
	 * @return
	 */
	@PostMapping("/delete")
	@ResponseBody
	public String delete(@RequestBody()long id[]) {
		for (long l : id) {
			//删除idmapper表里面的数据
			idMapperServer.delete(l);
			//删除orders表里面的数据
			ordersServer.delete(l);
		}
		return "";
	}
	
	@GetMapping("/updateIdMapper")
	public String update(String stockNum,HttpSession session) {
		//根据传过来的stockNum的值查询
		StockExample stockExample = new StockExample();
		stockExample.createCriteria().andStockNumEqualTo(stockNum);
		List<Stock> sList = stockMapper.selectByExample(stockExample);
		//从sesssion中获取一个id数组
		int[] id = (int[]) session.getAttribute("id");
		//循环遍历查询
		for (int i = 0; i < id.length; i++) {
			IdMappingExample idMappingExample = new IdMappingExample();
			idMappingExample.createCriteria().andOrderIdEqualTo((long)id[i]);
			List<IdMapping> idMappings = idMapperServer.list(idMappingExample);
			//拿idMappings中的第一个对象
			IdMapping idMapping = idMappings.get(0);
			//设置id
			idMapping.setStockId(sList.get(0).getId());
			//设置状态
			idMapping.setStatus("C001-30");
			//修改
			idMapperServer.update(idMapping);
		}
		return "bianzhicaigoujihua";
	}
	/**
	 * 修改状态为C001-40
	 * @param id
	 * @return
	 */
	@GetMapping("/updateIdStatus")
	@ResponseBody
	public String updateStatus(String id,String status) {
		long ids = Long.valueOf(id);
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andStockIdEqualTo(ids);
		List<IdMapping> list = idMapping.selectByExample(idMappingExample);
		for (IdMapping idMapping : list) {
			idMapping.setStatus(status);
			idMapperServer.update(idMapping);
		}
		return "";
	}
	

	/**
	 * 跳转页面
	 * @param id
	 * @return
	 */
	@GetMapping("/jumps")
	public String jumpPage(long id,Model model) {
		model.addAttribute("id", id);
		return "Apply_caiwushenpi";
	}
	
	@GetMapping("/changeStatus")
	public String changeStatus(Stock stock) {
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andStockIdEqualTo(stock.getId());
		List<IdMapping> idMappings = idMapping.selectByExample(idMappingExample);
		Stock stock2 = stockMapper.selectByPrimaryKey(stock.getId());
		for (int i = 0; i < idMappings.size(); i++) {
			IdMapping idMapping = idMappings.get(i);
			if(stock.getLeadAgree().equals("同意")) {
				idMapping.setStatus("C001-50");
				stock2.setLeadAgree("S002-1");
			}else if(stock.getLeadAgree().equals("驳回")) {
				idMapping.setStatus("C001-51");
				stock2.setLeadAgree("S002-0");
			}else if(stock.getLeadAgree().equals("待审")){
				idMapping.setStatus("C001-40");
				stock2.setLeadAgree("S002-0");
			}else if(stock.getLeadAgree().equals("废弃")) {
				idMapping.setStatus("C001-51");
				stock2.setLeadAgree("S002-0");
			}
			stock2.setLeader(stock.getLeader());
			stock2.setLeadDate(stock.getLeadDate());
			stock2.setLeadOpinion(stock.getLeadOpinion());
			idMapperServer.update(idMapping);
			stockMapper.updateByPrimaryKey(stock2);
		}
		return "Apply_daishencaiwu";
	}
}
