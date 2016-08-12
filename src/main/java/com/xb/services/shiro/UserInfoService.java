package com.xb.services.shiro;

import com.xb.bean.shiro.UserInfo;
import com.xb.business.UserInfoRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoService {

	@Resource
	private UserInfoRepository userInfoRepository;

	public UserInfo findByUsername(String username) {
		System.out.println("UserInfoServiceImpl.findByUsername()");
		return userInfoRepository.findByUsername(username);
	}

}
