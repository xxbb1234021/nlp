package com.xb.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xb.bean.student.Student;
import com.xb.bean.tree.TreeRoot;
import com.xb.services.hmm.HmmService;
import com.xb.services.student.StudentService;

@RestController
@EnableAutoConfiguration
public class TestController {
	private Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private HmmService hmmService;

	@Autowired
	private StudentService studentService;

	@RequestMapping(value = "/test")
	public String test() {
		JSONObject obj = new JSONObject();
		obj.put("num", 12534);
		logger.info(obj.toJSONString());
		return obj.toJSONString();
	}

	@RequestMapping(value = "/hmm")
	public void hmm() {
		String source = "wodasini";
		hmmService.getHanzi(source);
	}

	@RequestMapping("/list")
	public List<Student> getStus() {
		logger.info("从数据库读取Student集合");
		return studentService.getList();
	}

	@RequestMapping("/testD3")
	public String testD3() {
		TreeRoot inn = new TreeRoot("浙江");
		for (int i = 0; i < 10; i++) {
			List<TreeRoot> stus = new ArrayList<TreeRoot>();
			for (int j = 0; j < 4; j++) {
				TreeRoot s = new TreeRoot("children" + j);
				stus.add(s);
			}
			inn.setChildren(stus);
		}

		TreeRoot out = new TreeRoot("中国");
		List<TreeRoot> stus = new ArrayList<TreeRoot>();
		stus.add(inn);
		out.setChildren(stus);

		String jsonTeach = JSON.toJSONString(out);
		System.out.println(jsonTeach);

		return jsonTeach;
	}
}
