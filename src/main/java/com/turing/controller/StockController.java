package com.turing.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turing.entity.IdMappingExample;
import com.turing.entity.Orders;
import com.turing.entity.Stock;
import com.turing.entity.SysUsers;
import com.turing.mapper.IdMappingMapper;
import com.turing.mapper.StockMapper;
import com.turing.mapper.SupplierMapper;
import com.turing.server.OrdersServer;

@Controller
public class StockController {

	@Autowired
	private StockMapper stockMapper;
	
	
	@Autowired
	private IdMappingMapper idMappingMapper;
	
	@Autowired
	private OrdersServer ordersServer;
	
	@Autowired
	private SupplierMapper supplierMapper;
	
	@PostMapping("/saveStock")
	public String saveStock(Stock stock,HttpSession session) {
		//当前登录用户
		SysUsers sysUsers = (SysUsers) session.getAttribute("user");
		
		stock.setAuthorId(sysUsers.getId()+"");
		stock.setAuthor(sysUsers.getLoginId());
		
		//需求计划编号
		String stockNum = "200";
		//获取系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		String time = df.format(new Date());
		//生成五位随机数
		Random random = new Random();
		int rannum= (int)(random.nextDouble()*(99999-10000 + 1))+ 10000;
		stockNum += time + rannum;
		System.out.println(stockNum);
		stock.setStockNum(stockNum);
		
		stockMapper.insert(stock);

		return "redirect:/updateIdMapper?stockNum="+stockNum;
	}
	
	/**
	 * 查询所有采购信息
	 * @return
	 */
	@GetMapping("/findStock")
	@ResponseBody
	public List<Map<String, Object>> getAlls(){
		List<Map<String, Object>> list = stockMapper.findStock();
		List<Map<String,Object>> tmpList=new ArrayList<Map<String,Object>>();
		Set<Long> keysSet = new HashSet<Long>();
		for(Map<String, Object> collisionMap : list){
			Long keys = (Long) collisionMap.get("stock_id");
			int beforeSize = keysSet.size();
			keysSet.add(keys);
			int afterSize = keysSet.size();
			if(afterSize == beforeSize + 1){
			tmpList.add(collisionMap);
			}
		}
		return tmpList;
	}
	
	@GetMapping("/selectById")
	public String getById(HttpSession s,long id) {
		Stock stock = stockMapper.selectByPrimaryKey(id);
		s.setAttribute("stock", stock);
		return "redirect:/selectList?id="+id;
	}
	
	@GetMapping("/selectList")
	public String getBySId(HttpSession session,long id) {
		Map<String, Orders> map = new HashMap<String, Orders>();
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andStockIdEqualTo(id);
		for (int i=0;i<idMappingMapper.selectByExample(idMappingExample).size();i++) {
			Orders orders = ordersServer.selectOrder(idMappingMapper.selectByExample(idMappingExample).get(i).getOrderId());
			//判断是否存在
			if(map.containsKey(orders.getMaterialName())) {
				//拿到当前的对象
				Orders orders2 = (Orders) map.get(orders.getMaterialName());
				//修改当前的值
				//转int类型然后增加
				int num = Integer.parseInt(orders.getAmount());
				int num2 = Integer.parseInt(orders2.getAmount());
				int count = num+num2;
				//封装到Amount属性中
				orders.setAmount(count+"");
				//然后添加到map集合中
				map.put(orders.getMaterialName(), orders);
			}else {
				//如果map中不存在这个key直接添加
				map.put(orders.getMaterialName(), orders);
			}
		}
		//将map集合转换成list集合
		List<Orders> list = new ArrayList<Orders>(map.values());
		List<Map<String, Object>> maps = supplierMapper.selectSuppAll(list.get(0).getMaterialName());
		session.setAttribute("orderList", list);
		session.setAttribute("mapss", maps);
		return "xjfatz_xjfamx";
	}
	
	@GetMapping("/selectById2")
	public String getById2(HttpSession s,long id) {
		Stock stock = stockMapper.selectByPrimaryKey(id);
		s.setAttribute("stock", stock);
		return "redirect:/selectList2?id="+id;
	}
	
	@GetMapping("/selectList2")
	public String getBySId2(HttpSession session,long id) {
		Map<String, Orders> map = new HashMap<String, Orders>();
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andStockIdEqualTo(id);
		for (int i=0;i<idMappingMapper.selectByExample(idMappingExample).size();i++) {
			Orders orders = ordersServer.selectOrder(idMappingMapper.selectByExample(idMappingExample).get(i).getOrderId());
			//判断是否存在
			if(map.containsKey(orders.getMaterialName())) {
				//拿到当前的对象
				Orders orders2 = (Orders) map.get(orders.getMaterialName());
				//修改当前的值
				//转int类型然后增加
				int num = Integer.parseInt(orders.getAmount());
				int num2 = Integer.parseInt(orders2.getAmount());
				int count = num+num2;
				//封装到Amount属性中
				orders.setAmount(count+"");
				//然后添加到map集合中
				map.put(orders.getMaterialName(), orders);
			}else {
				//如果map中不存在这个key直接添加
				map.put(orders.getMaterialName(), orders);
			}
		}
		//将map集合转换成list集合
		List<Orders> list = new ArrayList<Orders>(map.values());
		List<Map<String, Object>> maps = supplierMapper.selectSuppAll(list.get(0).getMaterialName());
		session.setAttribute("orderList", list);
		session.setAttribute("maps", maps);
		session.setAttribute("stocks", stockMapper.selectByPrimaryKey(id));
		return "xjfatz_xjfamx3";
	}
	
	/**
	 * 查询采购计划状态为待审批/未下达/未编制询价书/审批不通过
	 * @return 
	 */ 
	@GetMapping("/findByIdStock")
	@ResponseBody
	public List<Map<String, Object>> findByIdStock(@RequestParam(defaultValue="C001-60")String status) {
		List<Map<String, Object>> list = stockMapper.findByIdStock(status);
		List<Map<String,Object>> tmpList=new ArrayList<Map<String,Object>>();
		Set<Long> keysSet = new HashSet<Long>();
		for(Map<String, Object> collisionMap : list){
			Long keys = (Long) collisionMap.get("stock_id");
			int beforeSize = keysSet.size();
			keysSet.add(keys);
			int afterSize = keysSet.size();
			if(afterSize == beforeSize + 1){
				tmpList.add(collisionMap);
			}
		}
		return tmpList;
	}
	/**
	  *  删除
	 * @param id
	 * @return
	 */
	@PostMapping("/deletes")
	@ResponseBody
	public String delete(long id) {
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andStockIdEqualTo(id);
		idMappingMapper.deleteByExample(idMappingExample);
		stockMapper.deleteByPrimaryKey(id);
		return "";
	}
	
	/***
	  * 未编制询价书
	 * @return
	 */
	@GetMapping("/nuToCompile")
	@ResponseBody
	public List<Map<String, Object>> nobainzhi(@RequestParam(defaultValue="C001-60")String status) {
		List<Map<String, Object>> list = stockMapper.findByIdStock(status);
		List<Map<String,Object>> tmpList=new ArrayList<Map<String,Object>>();
		Set<Long> keysSet = new HashSet<Long>();
		for(Map<String, Object> collisionMap : list){
			Long keys = (Long) collisionMap.get("stock_id");
			int beforeSize = keysSet.size();
			keysSet.add(keys);
			int afterSize = keysSet.size();
			if(afterSize == beforeSize + 1){
				tmpList.add(collisionMap);
			}
		}
		return tmpList;
	}
	
}
