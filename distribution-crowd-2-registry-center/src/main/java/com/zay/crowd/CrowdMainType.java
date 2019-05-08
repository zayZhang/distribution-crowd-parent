package com.zay.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * 注册中心
 * @author zay
 */
@EnableEurekaServer
@SpringBootApplication
public class CrowdMainType {

	public static void main(String[] args) {
		SpringApplication.run(CrowdMainType.class, args);
	}
}
