package com.zay.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * 用户管理模块
 * @author zay
 *
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class CrowdMainType {

	public static void main(String[] args) {
		SpringApplication.run(CrowdMainType.class, args);
	}
}
