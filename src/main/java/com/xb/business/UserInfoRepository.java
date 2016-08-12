package com.xb.business;

import com.xb.bean.shiro.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * UserInfo持久化类
 * Created by kevin on 2016/8/8 0008.
 */
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

	/**通过username查找用户信息;*/
	public UserInfo findByUsername(String username);

}
