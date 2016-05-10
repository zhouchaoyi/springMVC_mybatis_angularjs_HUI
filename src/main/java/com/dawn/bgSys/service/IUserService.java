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

    public JSONObject queryUserByNameAndPass(String loginName,String password);

    public User queryUserById(String id);

    public User queryUserByLoginName(String loginName);

    public int insertUserType(UserType userType);

    public int insertUser(User user) throws Exception;

    public int updateUserType(UserType userType);

    public JSONObject updateUser(User user,String currentUserId) throws Exception;

    public int deleteUserType(List<String> ids);

    public int deleteUser(List<String> ids);

    public List<UserType> listUserType();

    public JSONObject listUserByType(String userType,int currentPage,int pageSize,String orderBy,String searchStr);


}
