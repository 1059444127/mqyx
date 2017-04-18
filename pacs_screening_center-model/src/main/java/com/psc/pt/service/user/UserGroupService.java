package com.psc.pt.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psc.pt.dao.user.UserGroupMapper;
import com.psc.pt.model.user.UserGroup;

@Service
public class UserGroupService {
@Autowired UserGroupMapper userGroupMapper;
	/**
	 * 通过id获取group
	 * @param groupId
	 * @return
	 */
	public UserGroup selectById(Long groupId) {
		// TODO Auto-generated method stub
		return userGroupMapper.selectByPrimaryKey(groupId);
	}

}
