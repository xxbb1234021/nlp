package com.xb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.xb.algoritm.segment.PinYinSegmenter;
import com.xb.business.hmm.Director;
import com.xb.business.hmm.HmmAbstractFactory;
import com.xb.business.hmm.builderImpl.AbstractPinyingToHanziModel;
import com.xb.business.hmm.factoryImpl.PinyingToHanziFactory;
import com.xb.constant.Constant;
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

		//加载拼音语料库
		PinYinSegmenter.getInstance(Constant.PINYIN_TRIE_TREE);

		HmmAbstractFactory factory = new PinyingToHanziFactory();
		AbstractPinyingToHanziModel builder = factory.createPinyingToHanziModelBuilder2();
		Director director = new Director(builder);
		director.constructHmmModel();

		log.info("启动完成");
	}
}