package com.dawn.bgSys.dao;

import com.dawn.bgSys.domain.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IModuleDao {
    int deleteByPrimaryKey(Long moduleId);

    int deleteByClassId(String classId);

    int insert(Module record);

    int insertSelective(Module record);

    Module selectByPrimaryKey(Long moduleId);

    List<Module> select(@Param("searchStr")String searchStr);

    Module selectByCode(String moduleCode);

    int updateByPrimaryKeySelective(Module record);

    int updateByPrimaryKey(Module record);
}