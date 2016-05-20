package com.xb.controller;

import com.alibaba.fastjson.JSONObject;
import com.xb.algoritm.segment.PinYinSegmenter;
import com.xb.constant.Constant;
import com.xb.services.hmm.HmmService;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class BaseController {
    //private org.slf4j.Logger logger =  LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private HmmService hmmService;

    @RequestMapping(value = "/test")
	//是springmvc中的注解
	String home() {
        //记录分享日志记录
        //logger.info("test mesg" + 12345);
        JSONObject obj = new JSONObject();
        obj.put("num", 12345);
        //logger.info(obj.toJSONString());
        return obj.toJSONString();
    }

    @RequestMapping(value = "/hmm")
    public void hmm(){
        String source = "wodasini";
        hmmService.getHanzi(source);
    }
}
