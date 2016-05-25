package com.dawn.bgSys.dao;

import com.dawn.bgSys.domain.UserGroupRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IUserGroupRelationDao {
    int insert(UserGroupRelation record);

    int insertSelective(UserGroupRelation record);

    List<Map> select(@Param("groupId")String groupId,@Param("searchStr")String searchStr);

    int delete(@Param("list")List<String> ids,@Param("groupId")String groupId);
}