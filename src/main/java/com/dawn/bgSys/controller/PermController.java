package com.dawn.bgSys.controller;

import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.common.JWTUtils;
import com.dawn.bgSys.common.Utils;
import com.dawn.bgSys.common.WebJsonUtils;
import com.dawn.bgSys.domain.*;
import com.dawn.bgSys.service.IPermService;
import com.dawn.bgSys.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * User: zhouchaoyi
 * Date: 15-9-23
 */
@Controller
@RequestMapping("/permMgmt")
public class PermController extends BaseController {
    /**
     * 日志工具
     */
    final Logger logger = Logger.getLogger(PermController.class);

    @Autowired
    private IPermService permService;

    @Value("${APP_T_K}")
    private String appTK;

    @Value("${APP_EXP_TIME}")
    private String appExpTime;


    @RequestMapping(value = "/addModule", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addModule(@RequestBody String jsonStr) throws Exception {
        String moduleName = WebJsonUtils.getStringValue(jsonStr, "moduleName", true);
        String moduleCode = WebJsonUtils.getStringValue(jsonStr, "moduleCode", true);
        String isPermOnly = WebJsonUtils.getStringValue(jsonStr, "isPermOnly", true);
        String userType = WebJsonUtils.getStringValue(jsonStr, "userType", false);
        String remark = WebJsonUtils.getStringValue(jsonStr, "remark", false);
        String url = WebJsonUtils.getStringValue(jsonStr, "url", false);
        String relationUrl = WebJsonUtils.getStringValue(jsonStr, "relationUrl", false);
        String bExt = WebJsonUtils.getBooleanValue(jsonStr,"bExt");
        String status = WebJsonUtils.getBooleanValue(jsonStr,"status");
        String parentId = WebJsonUtils.getStringValue(jsonStr, "parentId", true);

        Module module = new Module();
        module.setModuleName(moduleName);
        module.setModuleCode(moduleCode);
        module.setIsPermOnly(Byte.valueOf(isPermOnly));
        module.setUserType(userType);
        module.setRemark(remark);
        module.setUrl(url);
        module.setRelationUrl(relationUrl);
        module.setbExt(Byte.valueOf(bExt));
        module.setStatus(Byte.valueOf(status));
        module.setParentId(Long.valueOf(parentId));

        Module result = this.permService.addModule(module);
        //System.out.println("success="+success);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/listModule", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listModule(@RequestBody String jsonStr) throws Exception {
        int currentPage = WebJsonUtils.getIntValue(jsonStr, "currentPage", true);
        int pageSize = WebJsonUtils.getIntValue(jsonStr, "pageSize", true);
        String orderBy = WebJsonUtils.getStringValue(jsonStr, "orderBy", false);
        String searchStr = WebJsonUtils.getStringValue(jsonStr, "searchStr", false);
        if(StringUtils.length(orderBy)>0) {
            orderBy=Utils.transOrderByStr(orderBy);
        }
        JSONObject result = this.permService.listModule(currentPage,pageSize,orderBy,searchStr);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/deleteModule", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteModule(@RequestBody String jsonStr) throws Exception {
        String moduleId = WebJsonUtils.getStringValue(jsonStr, "moduleId", true);

        int result = this.permService.deleteModule(moduleId);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/queryModuleById", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryModuleById(@RequestBody String jsonStr) throws Exception {

        String id = WebJsonUtils.getStringValue(jsonStr, "id", true);

        Module result = this.permService.queryModuleById(id);

        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/updateModule", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateModule(@RequestBody String jsonStr) throws Exception {
        String moduleId = WebJsonUtils.getStringValue(jsonStr, "id", true);
        String moduleName = WebJsonUtils.getStringValue(jsonStr, "moduleName", true);
        String moduleCode = WebJsonUtils.getStringValue(jsonStr, "moduleCode", true);
        String isPermOnly = WebJsonUtils.getStringValue(jsonStr, "isPermOnly", true);
        String userType = WebJsonUtils.getStringValue(jsonStr, "userType", false);
        String remark = WebJsonUtils.getStringValue(jsonStr, "remark", false);
        String url = WebJsonUtils.getStringValue(jsonStr, "url", false);
        String relationUrl = WebJsonUtils.getStringValue(jsonStr, "relationUrl", false);
        String bExt = WebJsonUtils.getBooleanValue(jsonStr,"bExt");
        String status = WebJsonUtils.getBooleanValue(jsonStr,"status");
        String parentId = WebJsonUtils.getStringValue(jsonStr, "parentId", true);

        Module module = new Module();
        module.setModuleId(Long.valueOf(moduleId));
        module.setModuleName(moduleName);
        module.setModuleCode(moduleCode);
        module.setIsPermOnly(Byte.valueOf(isPermOnly));
        module.setUserType(userType);
        module.setRemark(remark);
        module.setUrl(url);
        module.setRelationUrl(relationUrl);
        module.setbExt(Byte.valueOf(bExt));
        module.setStatus(Byte.valueOf(status));
        module.setParentId(Long.valueOf(parentId));

        Module result = this.permService.updateModule(module);
        //System.out.println("success="+success);
        JSONObject json = new JSONObject();
        json.put("data", result);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }

    @RequestMapping(value = "/moveModule", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String moveModule(@RequestBody String jsonStr) throws Exception {
        String move = WebJsonUtils.getStringValue(jsonStr, "move", true);
        String moduleId= WebJsonUtils.getStringValue(jsonStr, "moduleId", true);

        int success = this.permService.moveModule(moduleId, move);

        JSONObject json = new JSONObject();
        json.put("data", success);
        json.put("status", Utils.getSubStatus("获取数据成功！"));
        return json.toString();
    }
}