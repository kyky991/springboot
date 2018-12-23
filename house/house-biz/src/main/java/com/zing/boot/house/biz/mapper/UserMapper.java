package com.zing.boot.house.biz.mapper;

import com.zing.boot.house.common.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

	List<User>  selectUsers();
	
	int insert(User account);

	int delete(String email);

	int update(User updateUser);

	List<User> selectUsersByQuery(User user);
}
