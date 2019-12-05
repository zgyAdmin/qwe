package com.turing.util;

import java.util.ArrayList;
import java.util.List;

import com.turing.entity.SysMenus;

public class TreeNodeUtil {

	/**
	 * 根据节点集合获得所有的父节点为pid的节点，并且都设置了子元素
	 * @param list 所有的菜单集合
	 * @param pid 父节点
	 * @return 父节点集合，父节点里装载了子节点
	 */
	public static List<SysMenus> getFatherList(List<SysMenus> list){
		List<SysMenus> getSysmeuns = new ArrayList<>();
		for (SysMenus sysmeun : list) {
			if(sysmeun.getParentId()==0) {
				//获取父节点下的子节点，根据父节点递归调用获取所有的子节点
				//sysmeun.setState("closed");
				sysmeun.setChildren(getChildrenNode(sysmeun.getId(), list));
				getSysmeuns.add(sysmeun);
			}
		}
		
		return getSysmeuns;
	}
	
	/**
	 * 递归调用
	 * @param pid 父节点
	 * @param list 要循环的集合 
	 * @return 将所有子节点设置到父节点对象里的list集合里
	 */
	public static List<SysMenus> getChildrenNode(long pid,List<SysMenus> list){
		List<SysMenus> getSysmeans = new ArrayList<>();
		for (SysMenus sysmean : list) {
			if(sysmean.getParentId()==0) {
				continue;
			}
			if(sysmean.getParentId()==pid) {
				//sysmean.setState("closed");
				//根据父id找到了一个子节点，加入到子节点集合中去
				getSysmeans.add(sysmean);
				//设置子节点集合，递归调用
				sysmean.setChildren(getChildrenNode(sysmean.getId(), list));
			}
		}
		return getSysmeans;
	}
}
