package com.xb;

import com.xb.algoritm.segment.MaxMatchingPinYinSegmenter;
import com.xb.business.hmm.Director;
import com.xb.business.hmm.HmmAbstractFactory;
import com.xb.business.hmm.builderImpl.AbstractPinyingToHanziModel;
import com.xb.business.hmm.factoryImpl.PinyingToHanziFactory;
import com.xb.constant.FileConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationMain {
	private static Logger log = LoggerFactory.getLogger(ApplicationMain.class);

	public static void main(String[] args) throws Exception {
		//启动Spring Boot项目的唯一入口  
		SpringApplication.run(ApplicationMain.class, args);

		//加载拼音语料库
        MaxMatchingPinYinSegmenter.getInstance(FileConstant.PINYIN_TRIE_TREE);

		HmmAbstractFactory factory = new PinyingToHanziFactory();
		AbstractPinyingToHanziModel builder = factory.createPinyingToHanziModelBuilder2();
		Director director = new Director(builder);
		director.constructHmmModel();

		log.info("启动完成");
	}
}
