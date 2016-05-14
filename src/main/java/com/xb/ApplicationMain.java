package com.xb;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//配置控制
@Configuration
//启用自动配置
@EnableAutoConfiguration
//组件扫描
@ComponentScan
public class ApplicationMain {
	private static Logger log = LoggerFactory.getLogger(ApplicationMain.class);

	public static void main(String[] args) throws Exception {
		//启动Spring Boot项目的唯一入口  
		SpringApplication.run(ApplicationMain.class, args);

		log.info("启动完成");
	}
}