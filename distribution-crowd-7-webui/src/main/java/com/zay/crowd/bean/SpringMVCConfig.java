package com.zay.crowd.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer{

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		String urlPath = "/member/to/login/page";
		String viewName = "member_login";
		
		registry.addViewController(urlPath).setViewName(viewName);
		
		urlPath = "/member/to/member/center/page";
		viewName = "member_center";
		
		registry.addViewController(urlPath).setViewName(viewName);
		
		urlPath = "/project/to/agree/page";
		viewName = "project_1_start";
		
		registry.addViewController(urlPath).setViewName(viewName);
	}
}
