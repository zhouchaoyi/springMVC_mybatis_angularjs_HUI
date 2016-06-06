package com.dawn.bgSys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.dao.IControlPanelDao;
import com.dawn.bgSys.dao.IModuleDao;
import com.dawn.bgSys.dao.IUserPermDao;
import com.dawn.bgSys.dao.IUserTypeDao;
import com.dawn.bgSys.domain.ControlPanel;
import com.dawn.bgSys.domain.Module;
import com.dawn.bgSys.domain.UserPerm;
import com.dawn.bgSys.domain.UserType;
import com.dawn.bgSys.exception.OperateFailureException;
import com.dawn.bgSys.service.IPermService;
import com.dawn.bgSys.utils.TreeModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IControlPanelDao controlPanelDao;

    @Autowired
    private IUserPermDao userPermDao;

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
    public JSONObject updateModule(Module module) {
        //验证代码是否重复
        checkRepeatCode(module);
        moduleDao.updateByPrimaryKeySelective(module);
        treeModel.setsTableName("module_info");
        treeModel.setsIdField("module_id");
        treeModel.resetClassId(module.getModuleId()+"");

        Module result = moduleDao.selectByPrimaryKey(module.getModuleId());

        String hasDiffStatusChild="0"; //是否有不同状态的子节点，如果有，前端需要重新加载子节点
        if(StringUtils.equals("1",result.getIsParent())) {
            String checkStatus = "";
            if(result.getStatus()==1) {
                checkStatus="0";
            }else if(result.getStatus()==0) {
                checkStatus="1";
            }
            List<Map> list = moduleDao.selectStatusByClassId(checkStatus, result.getClassId() + "%");
            if(null!=list&&list.size()>0) {
                hasDiffStatusChild="1";
            }
        }

        //更新子节点的状态
        if(StringUtils.equals("1",result.getIsParent())) {
            moduleDao.updateStatusByClassId(result.getStatus(),result.getClassId()+"%");
        }

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

        String jsonStr= JSON.toJSONString(result);
        JSONObject json=JSONObject.parseObject(jsonStr);
        json.put("hasDiffStatusChild",hasDiffStatusChild);
        return json;
    }

    public boolean checkRepeatCode(Module module) {
        boolean result=true;
        Module checkModule = moduleDao.selectByCode(module.getModuleCode());
        if(null!=checkModule&&StringUtils.equals(module.getModuleCode(),checkModule.getModuleCode())&&module.getModuleId()!=checkModule.getModuleId()) {
            throw new OperateFailureException("代码不能重复");
        }
        return result;
    }

    public JSONObject listModule(int currentPage,int pageSize,String orderByStr,String searchStr,String status,String parentClassId) {
        JSONObject result=new JSONObject();

        if(currentPage>0) {
            PageHelper.startPage(currentPage, pageSize);
        }
        if(StringUtils.length(orderByStr)>0) {
            PageHelper.orderBy(orderByStr);
        }
        searchStr="%"+searchStr+"%";
        List<Module> list = moduleDao.select(searchStr,status,parentClassId+"%",parentClassId);

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

    public JSONObject listModuleForPerm(int currentPage,int pageSize,String orderByStr,String searchStr,String permUserId,String permType) {
        JSONObject result=new JSONObject();

        if(currentPage>0) {
            PageHelper.startPage(currentPage, pageSize);
        }
        if(StringUtils.length(orderByStr)>0) {
            PageHelper.orderBy(orderByStr);
        }
        searchStr="%"+searchStr+"%";
        List<Map> list = moduleDao.selectForPerm(searchStr, permUserId, permType);

        //code转换成中文字
        List<UserType> listUserType = userTypeDao.listUserType();
        Map<String,String> map = new HashMap<String,String>();
        for(UserType userType : listUserType) {
            map.put(userType.getTypeCode(),userType.getTypeName());
        }
        for(Map hashMap : list) {
            if(hashMap.get("userType").toString().length()>0) {
                String[] aStr = hashMap.get("userType").toString().split(",");
                for(int i=0;i<aStr.length;i++) {
                    aStr[i]=map.get(aStr[i].trim());
                }
                hashMap.put("userType",StringUtils.join(aStr,","));
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

    public JSONObject listControlPanel(int currentPage,int pageSize,String orderByStr,String searchStr,String classLevel,String status,String parentClassId) {
        JSONObject result=new JSONObject();

        if(currentPage>0) {
            PageHelper.startPage(currentPage, pageSize);
        }
        if(StringUtils.length(orderByStr)>0) {
            PageHelper.orderBy(orderByStr);
        }
        searchStr="%"+searchStr+"%";

        String classLength=null;
        if(classLevel.length()>0) {
            classLength=(Integer.valueOf(classLevel)*10)+"";
        }
        if(parentClassId.length()>0) {
            parentClassId=parentClassId+"%";
        }
        List<ControlPanel> list = controlPanelDao.select(searchStr,classLength,status,parentClassId);

        //System.out.println(list.size());
        PageInfo page = new PageInfo(list);

        result.put("items",list);
        result.put("currentPage",page.getPageNum());
        result.put("pageSize",page.getPageSize());
        result.put("total",page.getTotal());
        result.put("pages",page.getPages());

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addControlPanel(ControlPanel panel) {
        int result=0;
        result=controlPanelDao.insertSelective(panel);
        treeModel.setsTableName("control_panel");
        treeModel.setsIdField("item_id");
        treeModel.resetClassId(panel.getItemId()+"");

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateControlPanel(ControlPanel panel) {
        int result=0;
        result=controlPanelDao.updateByPrimaryKeySelective(panel);
        treeModel.setsTableName("control_panel");
        treeModel.setsIdField("item_id");
        treeModel.resetClassId(panel.getItemId()+"");

        //更新子节点的状态
        ControlPanel panel2 = controlPanelDao.selectByPrimaryKey(panel.getItemId());
        if(panel2.getClassId().length()<=20) {
            controlPanelDao.updateStatusByClassId(panel2.getStatus(), panel2.getClassId()+"%");
        }

        return result;
    }

    public ControlPanel queryControlPanelById(String id) {
        ControlPanel result = controlPanelDao.selectByPrimaryKey(Long.valueOf(id));
        return result;
    }

    public int deleteControlPanel(List<String> ids) {
        int success=0;
        success=controlPanelDao.deleteByPrimaryKey(ids);
        return success;
    }

    public int moveControlPanel(String itemId,String move) {
        int success=1;
        treeModel.setsTableName("control_panel");
        treeModel.setsIdField("item_id");
        boolean result=false;
        if(StringUtils.equals("1",move)) {
            result=treeModel.moveUp(itemId);
        }else if(StringUtils.equals("-1",move)) {
            result=treeModel.moveDown(itemId);
        }
        if(!result) {
            throw new OperateFailureException(treeModel.getPrompt());
        }
        return success;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addPerm(String id,String type,String moduleIds) {
        int result=0;

        //删除权限
        userPermDao.delete(id, type);

        //添加权限
        if(StringUtils.length(moduleIds)>0) {
            if (StringUtils.equals("1", type)) { //用户组
                String[] aStr = moduleIds.split(",");
                for (String moduleId : aStr) {
                    UserPerm userPerm = new UserPerm();
                    userPerm.setModuleId(Long.valueOf(moduleId));
                    userPerm.setUserGroupId(Long.valueOf(id));
                    userPerm.setCanInherit(Byte.valueOf("1"));
                    userPerm.setIsUserGroup(Integer.valueOf(type));
                    result = userPermDao.insertSelective(userPerm);
                }
            }
        }
        return result;
    }
}