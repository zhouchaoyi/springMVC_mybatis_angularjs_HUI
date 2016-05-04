package com.dawn.bgSys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.common.JWTUtils;
import com.dawn.bgSys.dao.IUserDao;
import com.dawn.bgSys.dao.IUserTypeDao;
import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.domain.UserType;
import com.dawn.bgSys.exception.OperateFailureException;
import com.dawn.bgSys.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouchaoyi on 2016/4/5.
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserTypeDao userTypeDao;

    @Autowired
    private IUserDao userDao;

    @Value("${APP_T_K}")
    private String appTK;

    @Value("${APP_EXP_TIME}")
    private String appExpTime;

    public JSONObject queryUserTypeById(Long typeId) {
        UserType userType=userTypeDao.selectByPrimaryKey(typeId);
        String jsonStr= JSON.toJSONString(userType);
        JSONObject json=JSONObject.parseObject(jsonStr);
        return json;
    }

    public JSONObject queryUserByNameAndPass(String loginName, String password) {
        User user=userDao.selectUserByNameAndPass(loginName, password);
        if(null==user) {
            throw new OperateFailureException("登录名或密码错误");
        }
        String jsonStr= JSON.toJSONString(user);
        JSONObject json=JSONObject.parseObject(jsonStr);

        Map params = new HashMap();
        long iat = new Date().getTime();
        params.put("iat", iat);
        long exp = new Date().getTime()+Long.valueOf(appExpTime);
        params.put("exp", exp);
        params.put("userId",user.getUserId());
        String token= JWTUtils.signerToken(params, appTK);
        json.put("token",token);

        return json;
    }

    public User queryUserById(String id) {
        User user=userDao.selectByPrimaryKey(id);
        return user;
    }

    public User queryUserByLoginName(String loginName) {
        User user=userDao.selectByLoginName(loginName);
        return user;
    }

    public int insertUserType(UserType userType) {
        int success=0;
        success=userTypeDao.insert(userType);
        return success;
    }

    public int insertUser(User user) throws Exception {
        User checkUser=userDao.selectByLoginName(user.getLoginName());
        if(null!=checkUser) {
            throw new OperateFailureException("此登录名已被其他用户使用");
        }
        int success=0;
        success=userDao.insert(user);
        return success;
    }

    public int updateUserType(UserType userType) {
        int success=0;
        success=userTypeDao.updateByPrimaryKey(userType);
        return success;
    }

    public int updateUser(User user) throws Exception {
        User checkUser=userDao.selectByLoginName(user.getLoginName());
        if(null!=checkUser&&!StringUtils.equals(user.getUserId(),checkUser.getUserId())) {
            throw new OperateFailureException("此登录名已被其他用户使用");
        }
        int success=0;
        success=userDao.updateByPrimaryKey(user);
        return success;
    }

    public int deleteUserType(List<String> ids) {
        int success=0;
        success=userTypeDao.deleteByPrimaryKey(ids);
        return success;
    }

    public int deleteUser(List<String> ids) {
        int success=0;
        success=userDao.deleteByPrimaryKey(ids);
        return success;
    }

    public List<UserType> listUserType() {
        List<UserType> list = userTypeDao.listUserType();
        return list;
    }

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
