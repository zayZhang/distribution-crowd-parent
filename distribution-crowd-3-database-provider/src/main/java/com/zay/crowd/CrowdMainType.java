package com.zay.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 相对于客户端的provider
 * @author zay
 */
@EnableDiscoveryClient
@MapperScan("com.zay.crowd.mapper")
@SpringBootApplication
public class CrowdMainType {

	public static void main(String[] args) {
		SpringApplication.run(CrowdMainType.class, args);
	}
}
