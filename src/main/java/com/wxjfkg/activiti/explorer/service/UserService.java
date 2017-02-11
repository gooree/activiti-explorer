package com.wxjfkg.activiti.explorer.service;

import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxjfkg.activiti.explorer.dao.UserDao;

@Service
@Transactional(readOnly = true)
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public User findUserByName(String username) {
		return userDao.selectUser(username);
	}
	
}
