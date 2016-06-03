package com.dawn.bgSys.dao;

import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.domain.UserGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IUserGroupDao {
    int deleteByPrimaryKey(List<String> ids);

    int insert(UserGroup record);

    int insertSelective(UserGroup record);

    UserGroup selectByPrimaryKey(Long groupId);

    List<UserGroup> select(@Param("searchStr")String searchStr);

    int updateByPrimaryKeySelective(UserGroup record);

    int updateByPrimaryKey(UserGroup record);

    List<Map> selectMemberCount(List<String> ids);

    List<UserGroup> selectUserGroupForUser(@Param("userId")String groupId,@Param("searchStr")String searchStr);
}