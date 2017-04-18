package com.psc.pt.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psc.pt.dao.user.UserInfoMapper;
import com.psc.pt.model.user.UserInfo;

@Service
public class UserInfoService {
	@Autowired UserInfoMapper userInfoMapper;

	/**
	 * 根据checkId查询
	 * @param id
	 * @return
	 */
	public UserInfo selectByCheckId(Long id) {
		// TODO Auto-generated method stub
		return userInfoMapper.selectByCheckId(id);
	}
}
