package com.dawn.bgSys.service;

import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.domain.Department;
import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.domain.UserGroup;
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

    public int addDepartment(Department dept) throws Exception;

    public JSONObject listDepartment(int currentPage,int pageSize,String orderBy,String searchStr);

    public Department queryDepartmentById(String id);

    public int updateDepartment(Department dept);

    public int deleteDepartment(List<String> ids);

    public int doMoveDepartment(String departmentId,String move);

    public int addUserGroup(UserGroup userGroup) throws Exception;

    public JSONObject listUserGroup(int currentPage,int pageSize,String orderBy,String searchStr);

    public UserGroup queryUserGroupById(String id);

    public int updateUserGroup(UserGroup userGroup) throws Exception;

    public int deleteUserGroup(List<String> ids);

    public int addUserGroupMember(List<String> ids,String groupId);

    public JSONObject listUserForUserGroup(String groupId,int currentPage,int pageSize,String orderBy,String searchStr);

    public JSONObject listUserGroupMember(String groupId,int currentPage,int pageSize,String orderBy,String searchStr);

    public int deleteUserGroupMember(List<String> ids,String groupId);

    public JSONObject listUserJoinGroup(String userId,int currentPage,int pageSize,String orderBy,String searchStr);

    public JSONObject listUserGroupForUser(String userId,int currentPage,int pageSize,String orderBy,String searchStr);

    public int joinGroup(List<String> ids,String userId);

    public int cancelJoinGroup(List<String> ids,String userId);
}
