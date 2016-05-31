package com.dawn.bgSys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.common.JWTUtils;
import com.dawn.bgSys.dao.*;
import com.dawn.bgSys.domain.*;
import com.dawn.bgSys.exception.OperateFailureException;
import com.dawn.bgSys.service.IPermService;
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

import java.util.*;

/**
 * Created by zhouchaoyi on 2016/4/5.
 */
@Service
public class PermServiceImpl implements IPermService {
    @Autowired
    private IModuleDao moduleDao;

    @Autowired
    private IUserTypeDao userTypeDao;

    @Autowired
    private TreeModel treeModel;

    @Value("${APP_T_K}")
    private String appTK;

    @Value("${APP_EXP_TIME}")
    private String appExpTime;


    @Transactional(propagation = Propagation.REQUIRED)
    public Module addModule(Module module) {
        //验证代码是否重复
        checkRepeatCode(module);
        moduleDao.insertSelective(module);
        treeModel.setsTableName("module_info");
        treeModel.setsIdField("module_id");
        treeModel.resetClassId(module.getModuleId()+"");

        Module result = moduleDao.selectByPrimaryKey(module.getModuleId());
        //code转换成中文字
        List<UserType> listUserType = userTypeDao.listUserType();
        Map<String,String> map = new HashMap<String,String>();
        for(UserType userType : listUserType) {
            map.put(userType.getTypeCode(),userType.getTypeName());
        }
        if(result.getUserType().length()>0) {
            String[] aStr = result.getUserType().split(",");
            for(int i=0;i<aStr.length;i++) {
                aStr[i]=map.get(aStr[i].trim());
            }
            result.setUserType(StringUtils.join(aStr,","));
        }

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Module updateModule(Module module) {
        //验证代码是否重复
        checkRepeatCode(module);
        moduleDao.updateByPrimaryKeySelective(module);
        treeModel.setsTableName("module_info");
        treeModel.setsIdField("module_id");
        treeModel.resetClassId(module.getModuleId()+"");

        Module result = moduleDao.selectByPrimaryKey(module.getModuleId());

        //code转换成中文字
        List<UserType> listUserType = userTypeDao.listUserType();
        Map<String,String> map = new HashMap<String,String>();
        for(UserType userType : listUserType) {
            map.put(userType.getTypeCode(),userType.getTypeName());
        }
        if(result.getUserType().length()>0) {
            String[] aStr = result.getUserType().split(",");
            for(int i=0;i<aStr.length;i++) {
                aStr[i]=map.get(aStr[i].trim());
            }
            result.setUserType(StringUtils.join(aStr,","));
        }

        return result;
    }

    public boolean checkRepeatCode(Module module) {
        boolean result=true;
        Module checkModule = moduleDao.selectByCode(module.getModuleCode());
        if(null!=checkModule&&StringUtils.equals(module.getModuleCode(),checkModule.getModuleCode())&&module.getModuleId()!=checkModule.getModuleId()) {
            throw new OperateFailureException("代码不能重复");
        }
        return result;
    }

    public JSONObject listModule(int currentPage,int pageSize,String orderByStr,String searchStr) {
        JSONObject result=new JSONObject();

        if(currentPage>0) {
            PageHelper.startPage(currentPage, pageSize);
        }
        if(StringUtils.length(orderByStr)>0) {
            PageHelper.orderBy(orderByStr);
        }
        searchStr="%"+searchStr+"%";
        List<Module> list = moduleDao.select(searchStr);

        //code转换成中文字
        List<UserType> listUserType = userTypeDao.listUserType();
        Map<String,String> map = new HashMap<String,String>();
        for(UserType userType : listUserType) {
            map.put(userType.getTypeCode(),userType.getTypeName());
        }
        for(Module module : list) {
            if(module.getUserType().length()>0) {
                String[] aStr = module.getUserType().split(",");
                for(int i=0;i<aStr.length;i++) {
                    aStr[i]=map.get(aStr[i].trim());
                }
                module.setUserType(StringUtils.join(aStr,","));
            }
        }
        //System.out.println(list.size());
        PageInfo page = new PageInfo(list);

        result.put("items",list);
        result.put("currentPage",page.getPageNum());
        result.put("pageSize",page.getPageSize());
        result.put("total",page.getTotal());
        result.put("pages",page.getPages());

        return result;
    }


    public int deleteModule(String moduleId) {
        int result=0;
        Module module = moduleDao.selectByPrimaryKey(Long.valueOf(moduleId));
        result = moduleDao.deleteByClassId(module.getClassId()+"%");
        return result;
    }

    public Module queryModuleById(String id) {
        Module result = moduleDao.selectByPrimaryKey(Long.valueOf(id));
        return result;
    }

    public int moveModule(String moduleId,String move) {
        int success=1;
        treeModel.setsTableName("module_info");
        treeModel.setsIdField("module_id");
        boolean result=false;
        if(StringUtils.equals("1",move)) {
            result=treeModel.moveUp(moduleId);
        }else if(StringUtils.equals("-1",move)) {
            result=treeModel.moveDown(moduleId);
        }
        if(!result) {
            throw new OperateFailureException(treeModel.getPrompt());
        }
        return success;
    }
}
