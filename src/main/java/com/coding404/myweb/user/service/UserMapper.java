package com.coding404.myweb.user.service;

import org.apache.ibatis.annotations.Mapper;

import com.coding404.myweb.command.UserVO;

@Mapper
public interface UserMapper {
	
	public UserVO getUserInfo(String id);
	
}
