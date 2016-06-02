package com.dawn.bgSys.dao;

import com.dawn.bgSys.domain.ControlPanel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IControlPanelDao {
    int deleteByPrimaryKey(List<String> ids);

    int insert(ControlPanel record);

    int insertSelective(ControlPanel record);

    ControlPanel selectByPrimaryKey(Long itemId);

    List<ControlPanel> select(@Param("searchStr")String searchStr,@Param("classLength")String classLength,@Param("status")String status,@Param("parentClassId")String parentClassId);

    int updateByPrimaryKeySelective(ControlPanel record);

    int updateStatusByClassId(@Param("status")Byte status,@Param("classId")String classId);

    int updateByPrimaryKey(ControlPanel record);
}