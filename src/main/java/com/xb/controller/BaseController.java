package com.xb.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class BaseController {
    private Logger logger = Logger.getLogger(BaseController.class);

    @RequestMapping(value = "/test")
	//是springmvc中的注解
	String home() {
        //记录分享日志记录
        logger.info("test mesg" + 12345);
        JSONObject obj = new JSONObject();
        obj.put("num", 12345);
        return obj.toJSONString();

    }
}
