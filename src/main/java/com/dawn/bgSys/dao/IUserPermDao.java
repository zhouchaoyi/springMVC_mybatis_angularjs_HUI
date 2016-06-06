package com.dawn.bgSys.dao;

import com.dawn.bgSys.domain.UserPerm;
import org.apache.ibatis.annotations.Param;

public interface IUserPermDao {
    int deleteByPrimaryKey(Long permId);

    int delete(@Param("id")String id,@Param("type")String type);

    int insert(UserPerm record);

    int insertSelective(UserPerm record);

    UserPerm selectByPrimaryKey(Long permId);

    int updateByPrimaryKeySelective(UserPerm record);

    int updateByPrimaryKey(UserPerm record);
}