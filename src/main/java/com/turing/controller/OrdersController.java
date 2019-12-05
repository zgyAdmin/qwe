package com.turing.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.turing.entity.Enquire;
import com.turing.entity.IdMapping;
import com.turing.entity.IdMappingExample;
import com.turing.entity.Orders;
import com.turing.entity.SysUsers;
import com.turing.mapper.IdMappingMapper;
import com.turing.mapper.SupplierMapper;
import com.turing.server.OrdersServer;

@Controller
public class OrdersController {
	
	private Map<String, Orders> map = new HashMap<String, Orders>();
	
	@Autowired
	private OrdersServer ordersServer;
	
	//供应商
	@Autowired
	private SupplierMapper supplierMapper;
	
	@Autowired 
	private IdMappingMapper idMappingMapper;
	
	@PostMapping("/saveOrders")
	public String addOrders(Orders orders,HttpSession session) {
		//获取当前登录的用户信息
		SysUsers sysUsers = (SysUsers) session.getAttribute("user");
		
		//需求计划编号
		String countNum = "100";
		//获取系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		String time = df.format(new Date());
		//生成五位随机数
		Random random = new Random();
		int rannum= (int)(random.nextDouble()*(99999-10000 + 1))+ 10000;
		countNum += time + rannum;
		System.out.println(countNum);
		
		//封装到orders实体中
		orders.setAuthorId(sysUsers.getId()+"");
		orders.setAuthor(sysUsers.getLoginId());
		orders.setOrderNum(countNum);
		
		//添加到orders表格中
		ordersServer.insert(orders);
		
		return "redirect:/saveIdMapper?orderNum="+countNum;
	}
	
	/**
	  *  查询所有需求
	 * @return
	 */
	@GetMapping("/selectAlls")
	@ResponseBody
	public Map alls(Model model,@RequestParam(defaultValue="1")int page,@RequestParam(defaultValue="4")int rows,
			String code,String name,String status) {
		System.out.println(code+""+name+""+status);
		PageInfo maps = ordersServer.selecctAll(page,rows,code,name,status);
		Map data = new HashMap<>();
		data.put("total", maps.getTotal());
		data.put("rows", maps.getList());
		return data;
	}
	
	
	/**
	  *  查询单个
	 * @param id
	 * @return.
	 */
	@GetMapping("/jump")
	public String hello(Model model,String id) {
		long l = Long.parseLong(id);
		model.addAttribute("order", ordersServer.selectOrder(l));
		return "print_order_detail1";
	}
	
	/***
	 * 去修改
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/toupdate")
	public String toUpdate(Model model,String id) {
		long l = Long.parseLong(id);
		model.addAttribute("order", ordersServer.selectOrder(l));
		return "update_order_detail";
	}
	 
	/**
	 * 修改
	 * @param orders
	 * @return
	 */
	@PostMapping("/update")
	public String update(Orders orders) {
	    Orders orders2 = ordersServer.selectOrder(orders.getId());
	    orders2.setAmount(orders.getAmount());
	    orders2.setUnitPrice(orders.getUnitPrice());
	    ordersServer.update(orders2);
		return "Order_ytb_list2";
	}
	
	/**
	 * 查询状态为确认
	 * @return
	 */
	@GetMapping("/selectAllByStatus")
	@ResponseBody
	public List<Map<String, Object>> list(){
		return ordersServer.selectAllByStatus();
	}
	
	/**
	 * 去编写采购机会
	 * @param model 保存值
	 * @param ids 前台传过来选中的数据的id数组
	 * @return 
	 */
	
	@GetMapping("/list")
	public String getById(HttpSession session,int[] ids) {
		for (int l : ids) {
			Orders orders = ordersServer.selectOrder(l);
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
		List<Orders> list = new ArrayList<>(map.values());
		List<Map<String, Object>> maps = supplierMapper.selectSuppAll(list.get(0).getMaterialName());
		session.setAttribute("id", ids);
		//获取系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String time = df.format(new Date());
		
		session.setAttribute("time", time);
		session.setAttribute("orderList", list);
		session.setAttribute("ListMap", maps);
		return "bianzhicaigoujihua";
	}
	
	@PostMapping("/change")
	@ResponseBody
	public String changeSupp(HttpSession session,String name) {
		List<Map<String, Object>> maps = supplierMapper.selectSuppAll(name);
		session.setAttribute("ListMap", maps);
		return "";
	}
	
	/**
	  *  id需求的所有需求
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/ordersCai")
	public String historyOrders(long id,Model model,HttpSession session) {
		map.clear();
		IdMappingExample idMappingExample = new IdMappingExample();
		idMappingExample.createCriteria().andStockIdEqualTo(id);
		List<IdMapping> idMappings = idMappingMapper.selectByExample(idMappingExample);
		for (IdMapping idMapping : idMappings) {
			Orders orders = ordersServer.selectOrder(idMapping.getOrderId());
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
		List<Orders> orders = new ArrayList<Orders>(map.values());
		model.addAttribute("OrdersList", orders);
		session.setAttribute("id", id);
		return "Enquire_bianzhi";
	}
	
	/***
	  * 编辑询价书、
	 * @param enquire
	 * @return 编辑采购书页面
	 */
	@PostMapping("/addEnquire")
	public String addEn(Enquire enquire) {
		return "";
	}
}
