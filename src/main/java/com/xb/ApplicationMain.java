package com.xb;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//配置控制
@EnableAutoConfiguration
//启用自动配置
@ComponentScan
//组件扫描
public class ApplicationMain {
	//private static final Logger logger = LoggerFactory.getLogger(ApplicationMain.class);

	static {
		try {
			// 初始化log4j
			String log4jPath = "";
			// 配置本地地址
			log4jPath = ApplicationMain.class.getClassLoader().getResource("").getPath() + "log4j.properties";

			//logger.info("初始化Log4j。。。。");
			//logger.info("path is " + log4jPath);
			PropertyConfigurator.configure(log4jPath);
		} catch (Exception e) {
			//logger.error(e.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		//启动Spring Boot项目的唯一入口
		SpringApplication.run(ApplicationMain.class, args);
	}
}