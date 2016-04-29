package com.dawn.bgSys.service;

import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.domain.UserType;

import java.util.List;

/**
 * Created by zhouchaoyi on 2016/4/5.
 */
public interface IUserService {
    /**
     * 根据Id获取类别信息
     * @param typeId
     * @return
     */
    public JSONObject queryUserTypeById(Long typeId);

    public User queryUserById(String id);

    public int insertUserType(UserType userType);

    public int insertUser(User user);

    public int updateUserType(UserType userType);

    public int updateUser(User user);

    public int deleteUserType(List<String> ids);

    public int deleteUser(List<String> ids);

    public List<UserType> listUserType();

    public JSONObject listUserByType(String userType,int currentPage,int pageSize,String orderBy,String searchStr);
}
