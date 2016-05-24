package com.dawn.bgSys.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouchaoyi on 2016/5/19.
 */
public interface ICommonDao {

    List<Map> select(@Param("sql")String sql);

    int insert(@Param("sql")String sql);

    int update(@Param("sql")String sql);

    int delete(@Param("sql")String sql);


}
