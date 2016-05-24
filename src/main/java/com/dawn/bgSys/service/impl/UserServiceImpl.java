package com.dawn.bgSys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.common.JWTUtils;
import com.dawn.bgSys.dao.ICommonDao;
import com.dawn.bgSys.dao.IDepartmentDao;
import com.dawn.bgSys.dao.IUserDao;
import com.dawn.bgSys.dao.IUserTypeDao;
import com.dawn.bgSys.domain.Department;
import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.domain.UserType;
import com.dawn.bgSys.exception.OperateFailureException;
import com.dawn.bgSys.service.IUserService;
import com.dawn.bgSys.utils.TreeModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private IDepartmentDao departmentDao;

    @Autowired
    private TreeModel treeModel;

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
        Date loginDate=null;
        if(user.getLoginDate()==null) {
            loginDate=new Date();
        }
        user.setLoginDate(loginDate);
        user.setLastLogin(new Date());
        userDao.updateDateById(user);

        String jsonStr= JSON.toJSONString(user);
        JSONObject json=JSONObject.parseObject(jsonStr);

        Map params = new HashMap();
        long iat = new Date().getTime();
        params.put("iat", iat);
        long exp = new Date().getTime()+Long.valueOf(appExpTime);
        params.put("exp", exp);
        params.put("userId",user.getUserId());
        params.put("loginName",user.getLoginName());
        params.put("userName",user.getUserName());
        params.put("modifyFlag",user.getModifyFlag());
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

    @Transactional(propagation = Propagation.REQUIRED)
    public JSONObject updateUser(User user,String currentUserId) throws Exception {
        JSONObject result = new JSONObject();
        User checkUser=userDao.selectByLoginName(user.getLoginName());
        if(null!=checkUser&&!StringUtils.equals(user.getUserId(),checkUser.getUserId())) {
            throw new OperateFailureException("此登录名已被其他用户使用");
        }
        User checkUser2=userDao.selectByPrimaryKey(user.getUserId());
        int success=0;
        success=userDao.updateByPrimaryKey(user);
        if(!StringUtils.equals(user.getLoginName(),checkUser2.getLoginName())||!StringUtils.equals(user.getLoginPassword(),checkUser2.getLoginPassword())) {
            //如果登录名或密码修改，要更新修改标志modify_flag
            success=userDao.updateModifyFlag(user.getUserId());
            User user2=userDao.selectByPrimaryKey(user.getUserId());
            //如果修改的人和当前用户是同一个，需要返回更新的token给客户端，防止客户端修改密码后不能继续操作了
            if(StringUtils.equals(currentUserId,user.getUserId())) {
                Map params = new HashMap();
                long iat = new Date().getTime();
                params.put("iat", iat);
                long exp = new Date().getTime()+Long.valueOf(appExpTime);
                params.put("exp", exp);
                params.put("userId",user2.getUserId());
                params.put("loginName",user2.getLoginName());
                params.put("userName",user2.getUserName());
                params.put("modifyFlag",user2.getModifyFlag());
                String token= JWTUtils.signerToken(params, appTK);
                result.put("token",token);
            }
        }
        return result;
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

    @Transactional(propagation = Propagation.REQUIRED)
    public int addDepartment(Department dept) throws Exception {
        int success=0;
        success=departmentDao.insert(dept);
        treeModel.setsTableName("department_info");
        treeModel.setsIdField("department_id");
        treeModel.resetClassId(dept.getDepartmentId()+"");
        return success;
    }

    public JSONObject listDepartment(int currentPage,int pageSize,String orderByStr,String searchStr) {
        JSONObject result=new JSONObject();

        if(currentPage>0) {
            PageHelper.startPage(currentPage, pageSize);
        }
        if(StringUtils.length(orderByStr)>0) {
            PageHelper.orderBy(orderByStr);
        }
        searchStr="%"+searchStr+"%";
        List<Department> list = departmentDao.select(searchStr);
        PageInfo page = new PageInfo(list);

        result.put("items",list);
        result.put("currentPage",page.getPageNum());
        result.put("pageSize",page.getPageSize());
        result.put("total",page.getTotal());
        result.put("pages",page.getPages());

        return result;
    }

    public Department queryDepartmentById(String id) {
        Department dept=departmentDao.selectByPrimaryKey(Long.valueOf(id));
        return dept;
    }

    public int updateDepartment(Department dept) {
        int success=0;
        //System.out.println(dept.getParentId()+"<<<<<<<");
        if(dept.getParentId()!=-1) {
            String parentClassId= departmentDao.selectByPrimaryKey(dept.getParentId()).getClassId();
            //System.out.println("class_id="+parentClassId);
            if (parentClassId.indexOf(dept.getClassId()) == 0) {
                throw new OperateFailureException("不能选择子部门或自己作为父部门！请重新选择。");
            }
        }
        success=departmentDao.updateByPrimaryKey(dept);
        treeModel.setsTableName("department_info");
        treeModel.setsIdField("department_id");
        treeModel.resetClassId(dept.getDepartmentId()+"");
        return success;
    }

    public int deleteDepartment(List<String> ids) {
        int success=0;
        success=departmentDao.deleteByPrimaryKey(ids);
        return success;
    }

    public int doMoveDepartment(String departmentId,String move) {
        int success=1;
        treeModel.setsTableName("department_info");
        treeModel.setsIdField("department_id");
        boolean result=false;
        if(StringUtils.equals("1",move)) {
            result=treeModel.moveUp(departmentId);
        }else if(StringUtils.equals("-1",move)) {
            result=treeModel.moveDown(departmentId);
        }
        if(!result) {
            throw new OperateFailureException(treeModel.getPrompt());
        }
        return success;
    }
}
