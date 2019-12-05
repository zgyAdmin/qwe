package com.turing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/s").setViewName("Order_newform");
		registry.addViewController("/main").setViewName("main");
		registry.addViewController("/").setViewName("index2");
		registry.addViewController("/lists").setViewName("Order_ytb_list");
		registry.addViewController("/node").setViewName("print_order_detail1");
		registry.addViewController("/bian").setViewName("bianzhicaigoujihua");
		registry.addViewController("/Project").setViewName("Project_list");
		registry.addViewController("/Project4").setViewName("Project_list4");
		registry.addViewController("/Apply").setViewName("Apply_daishencaiwu");
		registry.addViewController("/list2").setViewName("Order_ytb_list2");
		registry.addViewController("/notpass").setViewName("sengpinotpass");
		registry.addViewController("/sengpiupdate").setViewName("xjfatz_xjfamx3");
		registry.addViewController("/pass").setViewName("Apply_weibianzhi");
		registry.addViewController("/Enquires").setViewName("Apply_alls");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
		.excludePathPatterns("/","/login","/css/**","/js/**");
	}
}
