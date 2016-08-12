package com.xb.common.config.security;

import com.xb.bean.shiro.SysRole;
import com.xb.bean.shiro.UserInfo;
import com.xb.business.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserInfoRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo userInfo = userRepository.findByUsername(username);
		if (userInfo == null) {
			throw new UsernameNotFoundException("not found");
		}
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for(SysRole role : userInfo.getRoleList()) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		//        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
		//        System.err.println("username is " + username + ", " + user.getRole().name());
		return new org.springframework.security.core.userdetails.User(userInfo.getUsername(), userInfo.getPassword(),
				authorities);
	}

}
