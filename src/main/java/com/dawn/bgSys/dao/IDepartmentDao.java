package com.dawn.bgSys.dao;

import com.dawn.bgSys.domain.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IDepartmentDao {
    int deleteByPrimaryKey(List<String> departmentId);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Long departmentId);

    List<Department> select(@Param("searchStr")String searchStr);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

}