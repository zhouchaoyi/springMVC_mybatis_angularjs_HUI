package com.dawn.bgSys.dao;

import com.dawn.bgSys.domain.UserType;

import java.util.List;

public interface IUserTypeDao {
    int deleteByPrimaryKey(List<String> ids);

    int insert(UserType record);

    int insertSelective(UserType record);

    UserType selectByPrimaryKey(Long typeId);

    int updateByPrimaryKeySelective(UserType record);

    int updateByPrimaryKey(UserType record);

    List<UserType> listUserType();
}