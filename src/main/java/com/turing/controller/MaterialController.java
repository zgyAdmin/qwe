package com.turing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turing.entity.Material;
import com.turing.server.MaterialServer;

@Controller
public class MaterialController {

	//用来存储materials数据
	public static List<Material> object = new ArrayList<Material>();
	
	@Autowired
	private MaterialServer materialServer;
	
	/**
	  *  查询所有物资信息
	 * @return
	 */
	@GetMapping("/all")
	public String getAll2(Model model) {
		model.addAttribute("list", materialServer.materials());
		return "pclass_select";
	}
	
	/**
	 * 根据id一条物资信息
	 * @param id
	 * @return
	 */
	@PostMapping("/selectOne")
	@ResponseBody
	public Material getMaterialById(long id) {
		return materialServer.getMaterialById(id);
	}
	
	/**
	 *  查询所有物资
	 * @param materials
	 * @param session
	 * @return
	 */
	@PostMapping(value="selectMaterials")
	@ResponseBody
	public long getList(@RequestBody()List<Material> materials,HttpSession session){
		//遍历集合添加到object集合里面去
		for (Material material : materials) {
			object.add(material);
		}
		//然后放入session中
		session.setAttribute("materials", object);
		return object.get(0).getId();
	}
	
	/**
	 *  查询
	 * @param id
	 * @param session
	 * @return
	 */
	@GetMapping("/one")
	public String selectOne(long id,Model model) {
		model.addAttribute("material", materialServer.getMaterialById(id));
		return "Order_newform";
	}
}
