package com.psc.pt.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psc.pt.dao.user.UserCheckMapper;
import com.psc.pt.model.user.UserCheck;

@Service
public class UserCheckService {
	@Autowired UserCheckMapper userCheckMapper;
	/**
	 * 插入
	 * @param userCheck
	 */
	public void insertUserCheck(UserCheck userCheck) {
		// TODO Auto-generated method stub
		userCheckMapper.insertSelective(userCheck);
	}
	/**
	 * 根据userName检索
	 * @param userName
	 * @return
	 */
	public UserCheck selectByUserName(String userName) {
		// TODO Auto-generated method stub
		return userCheckMapper.selectByUserName(userName);
	}
	/**
	 * 更新
	 * @param ucTr
	 */
	public void updateUserCheck(UserCheck ucTr) {
		// TODO Auto-generated method stub
		userCheckMapper.updateByPrimaryKeySelective(ucTr);
	}

}
