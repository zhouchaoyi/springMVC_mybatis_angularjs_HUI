package com.dawn.bgSys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.dao.IUserDao;
import com.dawn.bgSys.dao.IUserTypeDao;
import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.domain.UserType;
import com.dawn.bgSys.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhouchaoyi on 2016/4/5.
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserTypeDao userTypeDao;

    @Autowired
    private IUserDao userDao;


    /**
     * 根据Id获取类别信息
     *
     * @param typeId
     * @return
     */
    @Override
    public JSONObject queryUserTypeById(Long typeId) {
        UserType userType=userTypeDao.selectByPrimaryKey(typeId);
        String jsonStr= JSON.toJSONString(userType);
        JSONObject json=JSONObject.parseObject(jsonStr);
        return json;
    }

    @Override
    public User queryUserById(String id) {
        User user=userDao.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public int insertUserType(UserType userType) {
        int success=0;
        success=userTypeDao.insert(userType);
        return success;
    }

    @Override
    public int insertUser(User user) {
        int success=0;
        success=userDao.insert(user);
        return success;
    }

    @Override
    public int updateUserType(UserType userType) {
        int success=0;
        success=userTypeDao.updateByPrimaryKey(userType);
        return success;
    }

    @Override
    public int updateUser(User user) {
        int success=0;
        success=userDao.updateByPrimaryKey(user);
        return success;
    }

    @Override
    public int deleteUserType(List<String> ids) {
        int success=0;
        success=userTypeDao.deleteByPrimaryKey(ids);
        return success;
    }

    @Override
    public int deleteUser(List<String> ids) {
        int success=0;
        success=userDao.deleteByPrimaryKey(ids);
        return success;
    }

    @Override
    public List<UserType> listUserType() {
        List<UserType> list = userTypeDao.listUserType();
        return list;
    }

    @Override
    public JSONObject listUserByType(String userType,int currentPage,int pageSize,String orderByStr,String searchStr) {
        JSONObject result=new JSONObject();

        PageHelper.startPage(currentPage, pageSize);
        if(StringUtils.length(orderByStr)>0) {
            PageHelper.orderBy(orderByStr);
        }
        searchStr="%"+searchStr+"%";
        List<User> list = userDao.selectByUserType(userType,searchStr);
        PageInfo page = new PageInfo(list);

        result.put("items",list);
        result.put("currentPage",page.getPageNum());
        result.put("pageSize",page.getPageSize());
        result.put("total",page.getTotal());
        result.put("pages",page.getPages());

        return result;
    }
}
