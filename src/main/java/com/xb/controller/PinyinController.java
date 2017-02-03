package com.xb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xb.services.hmm.HmmService;

/**
 * Created by kevin on 2016/5/23.
 */
@RestController
@EnableAutoConfiguration
public class PinyinController {

	@Autowired
	private HmmService hmmService;

	public String pinyin2hanzi(@RequestParam("pinyin") String pinyin) {
		return hmmService.getHanzi(pinyin);
	}
}
