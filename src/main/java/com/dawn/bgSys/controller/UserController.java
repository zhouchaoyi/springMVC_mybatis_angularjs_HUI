package com.dawn.bgSys.controller;

import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.common.JWTUtils;
import com.dawn.bgSys.common.PropertiesUtil;
import com.dawn.bgSys.common.Utils;
import com.dawn.bgSys.common.WebJsonUtils;
import com.dawn.bgSys.domain.Department;
import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.domain.UserGroup;
import com.dawn.bgSys.domain.UserType;
import com.dawn.bgSys.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * User: zhouchaoyi
 * Date: 15-9-23
 */
@Controller
@RequestMapping("/userMgmt")
public class UserController extends BaseController {
    /**
     * 日志工具
     */
    final Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Value("${APP_T_K}")
    private String appTK;

    @Value("${APP_EXP_TIME}")
    private String appExpTime;


    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String login(@RequestBody String jsonStr) throws Exception {

        String loginName = WebJsonUtils.getStringValue(jsonStr, "loginName", true);
        String password = WebJsonUtils.getStringValue(jsonStr, "password", true);

        JSONObject result = this.userService.queryUserByNameAndPass(loginName,password);

        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/loginByLocalAccount", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String loginByLocalAccount(@RequestBody String jsonStr) throws Exception {
        JSONObject json = new JSONObject();
        json.put("data", 1);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    /**
     * 根据Id获取用户类型
     *
     * @param jsonStr
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryUserTypeById", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryUserTypeById(@RequestBody String jsonStr) throws Exception {

        String typeId = WebJsonUtils.getStringValue(jsonStr, "typeId", true);

        JSONObject result = this.userService.queryUserTypeById(Long.parseLong(typeId));

        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/addUserType", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addUserType(@RequestBody String jsonStr) throws Exception {
        String typeCode = WebJsonUtils.getStringValue(jsonStr, "code", true);
        String typeName = WebJsonUtils.getStringValue(jsonStr, "name", true);
        String remark = WebJsonUtils.getStringValue(jsonStr, "remark", false);
        String status = WebJsonUtils.getBooleanValue(jsonStr,"status");

        UserType userType = new UserType();
        userType.setTypeCode(typeCode);
        userType.setTypeName(typeName);
        userType.setStatus(Byte.valueOf(status));
        userType.setRemark(remark);

        int success = this.userService.insertUserType(userType);
        //System.out.println("success="+success);
        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/updateUserType", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateUserType(@RequestBody String jsonStr) throws Exception {
        String typeId = WebJsonUtils.getStringValue(jsonStr, "id", true);
        String typeCode = WebJsonUtils.getStringValue(jsonStr, "code", true);
        String typeName = WebJsonUtils.getStringValue(jsonStr, "name", true);
        String remark = WebJsonUtils.getStringValue(jsonStr, "remark", false);
        String status = WebJsonUtils.getBooleanValue(jsonStr,"status");

        UserType userType = new UserType();
        userType.setTypeId(Long.valueOf(typeId));
        userType.setTypeCode(typeCode);
        userType.setTypeName(typeName);
        userType.setStatus(Byte.valueOf(status));
        userType.setRemark(remark);

        int success = this.userService.updateUserType(userType);
        //System.out.println("success="+success);
        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/deleteUserType", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteUserType(@RequestBody String jsonStr) throws Exception {
        String ids = WebJsonUtils.getStringValue(jsonStr, "ids", true);
        String[] array=ids.split(",");
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        int success = this.userService.deleteUserType(list);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listUserType", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listUserType() throws Exception {
        List<UserType> result = this.userService.listUserType();
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listUserByType", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listUserByType(@RequestBody String jsonStr) throws Exception {
        String userType = WebJsonUtils.getStringValue(jsonStr, "userType", false);
        int currentPage = WebJsonUtils.getIntValue(jsonStr, "currentPage", true);
        int pageSize = WebJsonUtils.getIntValue(jsonStr, "pageSize", true);
        String orderBy = WebJsonUtils.getStringValue(jsonStr, "orderBy", false);
        String searchStr = WebJsonUtils.getStringValue(jsonStr, "searchStr", false);
        if(StringUtils.length(orderBy)>0) {
            orderBy=Utils.transOrderByStr(orderBy);
        }
        JSONObject result = this.userService.listUserByType(userType,currentPage,pageSize,orderBy,searchStr);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/addUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addUser(@RequestBody String jsonStr) throws Exception {
        String loginName = WebJsonUtils.getStringValue(jsonStr, "loginName", true);
        String userName = WebJsonUtils.getStringValue(jsonStr, "userName", true);
        String loginPassword = WebJsonUtils.getStringValue(jsonStr, "loginPassword", true);
        String userType = WebJsonUtils.getStringValue(jsonStr, "userType", true);
        String sex = WebJsonUtils.getStringValue(jsonStr, "sex", true);
        String idCardType = WebJsonUtils.getStringValue(jsonStr, "idCardType", false);
        String idCard = WebJsonUtils.getStringValue(jsonStr, "idCard", false);
        String publicAccount = WebJsonUtils.getBooleanValue(jsonStr,"publicAccount");
        String status = WebJsonUtils.getBooleanValue(jsonStr,"status");

        User user = new User();
        user.setIdCard(idCard);
        user.setIdCardType(Long.valueOf(idCardType));
        user.setLoginName(loginName);
        user.setLoginPassword(loginPassword);
        user.setPublicAccount(Byte.valueOf(publicAccount));
        user.setSex(Long.valueOf(sex));
        user.setStatus(Byte.valueOf(status));
        user.setUserName(userName);
        user.setUserType(userType);
        user.setRegistedDate(new Date());

        int success = this.userService.insertUser(user);
        //System.out.println("success="+success);
        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/queryUserById", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryUserById(@RequestBody String jsonStr) throws Exception {

        String id = WebJsonUtils.getStringValue(jsonStr, "id", true);

        User result = this.userService.queryUserById(id);

        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/updateUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateUser(@RequestBody String jsonStr) throws Exception {
        String userId = WebJsonUtils.getStringValue(jsonStr, "id", true);
        String loginName = WebJsonUtils.getStringValue(jsonStr, "loginName", true);
        String userName = WebJsonUtils.getStringValue(jsonStr, "userName", true);
        //System.out.println("userName="+userName+"<<<<<<");
        String loginPassword = WebJsonUtils.getStringValue(jsonStr, "loginPassword", true);
        String userType = WebJsonUtils.getStringValue(jsonStr, "userType", true);
        String sex = WebJsonUtils.getStringValue(jsonStr, "sex", true);
        String idCardType = WebJsonUtils.getStringValue(jsonStr, "idCardType", false);
        String idCard = WebJsonUtils.getStringValue(jsonStr, "idCard", false);
        String publicAccount = WebJsonUtils.getBooleanValue(jsonStr,"publicAccount");
        String status = WebJsonUtils.getBooleanValue(jsonStr,"status");
        String token = WebJsonUtils.getStringValue(jsonStr, "token",true);

        User user = new User();
        user.setUserId(userId);
        user.setIdCard(idCard);
        user.setIdCardType(Long.valueOf(idCardType));
        user.setLoginName(loginName);
        user.setLoginPassword(loginPassword);
        user.setPublicAccount(Byte.valueOf(publicAccount));
        user.setSex(Long.valueOf(sex));
        user.setStatus(Byte.valueOf(status));
        user.setUserName(userName);
        user.setUserType(userType);
        user.setRegistedDate(new Date());

        Map<String,Object> map= JWTUtils.verifierToken(token, appTK);
        String currentUserId=map.get("userId").toString();

        JSONObject result = this.userService.updateUser(user,currentUserId);
        //System.out.println("success="+success);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/deleteUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteUser(@RequestBody String jsonStr) throws Exception {
        String ids = WebJsonUtils.getStringValue(jsonStr, "ids", true);
        String[] array=ids.split(",");
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        int success = this.userService.deleteUser(list);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/addDepartment", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addDepartment(@RequestBody String jsonStr) throws Exception {
        String departmentName = WebJsonUtils.getStringValue(jsonStr, "departmentName", true);
        int parentId = WebJsonUtils.getIntValue(jsonStr, "parentId", true);
        String departmentKey = WebJsonUtils.getStringValue(jsonStr, "departmentKey", false);
        String remark = WebJsonUtils.getStringValue(jsonStr, "remark", false);
        String isTypeOnly = WebJsonUtils.getBooleanValue(jsonStr,"isTypeOnly");

        Department dept = new Department();
        dept.setDepartmentName(departmentName);
        dept.setParentId(Long.valueOf(parentId));
        dept.setDepartmentKey(departmentKey);
        dept.setRemark(remark);
        dept.setIsTypeOnly(Byte.valueOf(isTypeOnly));

        int success = this.userService.addDepartment(dept);
        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listDepartment", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listDepartment(@RequestBody String jsonStr) throws Exception {
        int currentPage = WebJsonUtils.getIntValue(jsonStr, "currentPage", true);
        int pageSize = WebJsonUtils.getIntValue(jsonStr, "pageSize", true);
        String orderBy = WebJsonUtils.getStringValue(jsonStr, "orderBy", false);
        String searchStr = WebJsonUtils.getStringValue(jsonStr, "searchStr", false);
        if(StringUtils.length(orderBy)>0) {
            orderBy=Utils.transOrderByStr(orderBy);
        }
        JSONObject result = this.userService.listDepartment(currentPage,pageSize,orderBy,searchStr);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/queryDepartmentById", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryDepartmentById(@RequestBody String jsonStr) throws Exception {

        String id = WebJsonUtils.getStringValue(jsonStr, "id", true);

        Department result = this.userService.queryDepartmentById(id);

        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/updateDepartment", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateDepartment(@RequestBody String jsonStr) throws Exception {
        String departmentId = WebJsonUtils.getStringValue(jsonStr, "id", true);
        String departmentName = WebJsonUtils.getStringValue(jsonStr, "departmentName", true);
        int parentId = WebJsonUtils.getIntValue(jsonStr, "parentId", true);
        String classId = WebJsonUtils.getStringValue(jsonStr, "classId", true);
        String departmentKey = WebJsonUtils.getStringValue(jsonStr, "departmentKey", false);
        String remark = WebJsonUtils.getStringValue(jsonStr, "remark", false);
        String isTypeOnly = WebJsonUtils.getBooleanValue(jsonStr,"isTypeOnly");

        Department dept = new Department();
        dept.setDepartmentId(Long.valueOf(departmentId));
        dept.setDepartmentName(departmentName);
        dept.setParentId(Long.valueOf(parentId));
        dept.setClassId(classId);
        dept.setDepartmentKey(departmentKey);
        dept.setRemark(remark);
        dept.setIsTypeOnly(Byte.valueOf(isTypeOnly));

        int success = this.userService.updateDepartment(dept);
        //System.out.println("success="+success);
        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/deleteDepartment", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteDepartment(@RequestBody String jsonStr) throws Exception {
        String ids = WebJsonUtils.getStringValue(jsonStr, "ids", true);
        String[] array=ids.split(",");
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        int success = this.userService.deleteDepartment(list);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/doMoveDepartment", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doMoveDepartment(@RequestBody String jsonStr) throws Exception {
        String move = WebJsonUtils.getStringValue(jsonStr, "move", true);
        String departmentId= WebJsonUtils.getStringValue(jsonStr, "departmentId", true);

        int success = this.userService.doMoveDepartment(departmentId,move);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/addUserGroup", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addUserGroup(@RequestBody String jsonStr) throws Exception {
        String groupName = WebJsonUtils.getStringValue(jsonStr, "groupName", true);
        String groupKey = WebJsonUtils.getStringValue(jsonStr, "groupKey", false);
        String departmentId = WebJsonUtils.getStringValue(jsonStr, "departmentId", false);
        String remark = WebJsonUtils.getStringValue(jsonStr, "remark", false);
        String status = WebJsonUtils.getBooleanValue(jsonStr,"status");

        String token = WebJsonUtils.getStringValue(jsonStr, "token", true);
        Map<String,Object> map= JWTUtils.verifierToken(token, appTK);
        String currentUserId=map.get("userId").toString();

        UserGroup userGroup = new UserGroup();
        userGroup.setGroupName(groupName);
        userGroup.setGroupKey(groupKey);
        userGroup.setDepartmentId(Long.valueOf(departmentId));
        userGroup.setRemark(remark);
        userGroup.setStatus(Byte.valueOf(status));
        userGroup.setUserId(currentUserId);
        userGroup.setCreatedDate(new Date());

        int success = this.userService.addUserGroup(userGroup);
        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listUserGroup", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listUserGroup(@RequestBody String jsonStr) throws Exception {
        int currentPage = WebJsonUtils.getIntValue(jsonStr, "currentPage", true);
        int pageSize = WebJsonUtils.getIntValue(jsonStr, "pageSize", true);
        String orderBy = WebJsonUtils.getStringValue(jsonStr, "orderBy", false);
        String searchStr = WebJsonUtils.getStringValue(jsonStr, "searchStr", false);
        if(StringUtils.length(orderBy)>0) {
            orderBy=Utils.transOrderByStr(orderBy);
        }
        JSONObject result = this.userService.listUserGroup(currentPage, pageSize, orderBy, searchStr);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/queryUserGroupById", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryUserGroupById(@RequestBody String jsonStr) throws Exception {

        String id = WebJsonUtils.getStringValue(jsonStr, "id", true);

        UserGroup result = this.userService.queryUserGroupById(id);

        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/updateUserGroup", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateUserGroup(@RequestBody String jsonStr) throws Exception {
        String groupId = WebJsonUtils.getStringValue(jsonStr, "id", true);
        String groupName = WebJsonUtils.getStringValue(jsonStr, "groupName", true);
        String groupKey = WebJsonUtils.getStringValue(jsonStr, "groupKey", false);
        String departmentId = WebJsonUtils.getStringValue(jsonStr, "departmentId", false);
        String remark = WebJsonUtils.getStringValue(jsonStr, "remark", false);
        String status = WebJsonUtils.getBooleanValue(jsonStr,"status");

        String token = WebJsonUtils.getStringValue(jsonStr, "token", true);
        Map<String,Object> map= JWTUtils.verifierToken(token, appTK);
        String currentUserId=map.get("userId").toString();

        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(Long.valueOf(groupId));
        userGroup.setGroupName(groupName);
        userGroup.setGroupKey(groupKey);
        userGroup.setDepartmentId(Long.valueOf(departmentId));
        userGroup.setRemark(remark);
        userGroup.setStatus(Byte.valueOf(status));
        userGroup.setUserId(currentUserId);

        int success = this.userService.updateUserGroup(userGroup);
        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/deleteUserGroup", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteUserGroup(@RequestBody String jsonStr) throws Exception {
        String ids = WebJsonUtils.getStringValue(jsonStr, "ids", true);
        //System.out.println("ids="+ids+"<<<<<<");
        String[] array=ids.split(",");
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        int success = this.userService.deleteUserGroup(list);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/addUserGroupMember", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addUserGroupMember(@RequestBody String jsonStr) throws Exception {
        String ids = WebJsonUtils.getStringValue(jsonStr, "ids", true);
        String groupId = WebJsonUtils.getStringValue(jsonStr, "groupId", true);
        String[] array=ids.split(",");
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        int success = this.userService.addUserGroupMember(list, groupId);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listUserForUserGroup", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listUserForUserGroup(@RequestBody String jsonStr) throws Exception {
        String groupId = WebJsonUtils.getStringValue(jsonStr, "groupId", true);
        int currentPage = WebJsonUtils.getIntValue(jsonStr, "currentPage", true);
        int pageSize = WebJsonUtils.getIntValue(jsonStr, "pageSize", true);
        String orderBy = WebJsonUtils.getStringValue(jsonStr, "orderBy", false);
        String searchStr = WebJsonUtils.getStringValue(jsonStr, "searchStr", false);
        if(StringUtils.length(orderBy)>0) {
            orderBy=Utils.transOrderByStr(orderBy);
        }
        JSONObject result = this.userService.listUserForUserGroup(groupId, currentPage, pageSize, orderBy, searchStr);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listUserGroupMember", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listUserGroupMember(@RequestBody String jsonStr) throws Exception {
        String groupId = WebJsonUtils.getStringValue(jsonStr, "groupId", true);
        int currentPage = WebJsonUtils.getIntValue(jsonStr, "currentPage", true);
        int pageSize = WebJsonUtils.getIntValue(jsonStr, "pageSize", true);
        String orderBy = WebJsonUtils.getStringValue(jsonStr, "orderBy", false);
        String searchStr = WebJsonUtils.getStringValue(jsonStr, "searchStr", false);
        if(StringUtils.length(orderBy)>0) {
            orderBy=Utils.transOrderByStr(orderBy);
        }
        JSONObject result = this.userService.listUserGroupMember(groupId, currentPage, pageSize, orderBy, searchStr);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/deleteUserGroupMember", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteUserGroupMember(@RequestBody String jsonStr) throws Exception {
        String ids = WebJsonUtils.getStringValue(jsonStr, "ids", true);
        String groupId = WebJsonUtils.getStringValue(jsonStr, "groupId", true);
        String[] array=ids.split(",");
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        int success = this.userService.deleteUserGroupMember(list, groupId);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listUserJoinGroup", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listUserJoinGroup(@RequestBody String jsonStr) throws Exception {
        String userId = WebJsonUtils.getStringValue(jsonStr, "userId", true);
        int currentPage = WebJsonUtils.getIntValue(jsonStr, "currentPage", true);
        int pageSize = WebJsonUtils.getIntValue(jsonStr, "pageSize", true);
        String orderBy = WebJsonUtils.getStringValue(jsonStr, "orderBy", false);
        String searchStr = WebJsonUtils.getStringValue(jsonStr, "searchStr", false);
        if(StringUtils.length(orderBy)>0) {
            orderBy=Utils.transOrderByStr(orderBy);
        }
        JSONObject result = this.userService.listUserJoinGroup(userId, currentPage, pageSize, orderBy, searchStr);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listUserGroupForUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listUserGroupForUser(@RequestBody String jsonStr) throws Exception {
        String userId = WebJsonUtils.getStringValue(jsonStr, "userId", true);
        int currentPage = WebJsonUtils.getIntValue(jsonStr, "currentPage", true);
        int pageSize = WebJsonUtils.getIntValue(jsonStr, "pageSize", true);
        String orderBy = WebJsonUtils.getStringValue(jsonStr, "orderBy", false);
        String searchStr = WebJsonUtils.getStringValue(jsonStr, "searchStr", false);
        if(StringUtils.length(orderBy)>0) {
            orderBy=Utils.transOrderByStr(orderBy);
        }
        JSONObject result = this.userService.listUserGroupForUser(userId, currentPage, pageSize, orderBy, searchStr);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }


    @RequestMapping(value = "/joinGroup", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String joinGroup(@RequestBody String jsonStr) throws Exception {
        String ids = WebJsonUtils.getStringValue(jsonStr, "ids", true);
        String userId = WebJsonUtils.getStringValue(jsonStr, "userId", true);
        String[] array=ids.split(",");
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        int success = this.userService.joinGroup(list, userId);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/cancelJoinGroup", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String cancelJoinGroup(@RequestBody String jsonStr) throws Exception {
        String ids = WebJsonUtils.getStringValue(jsonStr, "ids", true);
        String userId = WebJsonUtils.getStringValue(jsonStr, "userId", true);
        String[] array=ids.split(",");
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        int success = this.userService.cancelJoinGroup(list, userId);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }
}