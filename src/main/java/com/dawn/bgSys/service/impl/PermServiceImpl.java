package com.dawn.bgSys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.dao.*;
import com.dawn.bgSys.domain.*;
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

import java.util.ArrayList;
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
    private IUserDao userDao;

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
        List<UserType> listUserType = userTypeDao.listUserType("%%");
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
        List<UserType> listUserType = userTypeDao.listUserType("%%");
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

    public JSONObject listModule(int currentPage,int pageSize,String orderByStr,String searchStr,String status,String parentClassId,String relationUrl) {
        JSONObject result=new JSONObject();

        if(currentPage>0) {
            PageHelper.startPage(currentPage, pageSize);
        }
        if(StringUtils.length(orderByStr)>0) {
            PageHelper.orderBy(orderByStr);
        }
        searchStr="%"+searchStr+"%";
        List<Module> list = null;
        if(StringUtils.length(relationUrl)==0) {
            list = moduleDao.select(searchStr, status, parentClassId + "%", parentClassId);

            //code转换成中文字
            List<UserType> listUserType = userTypeDao.listUserType("%%");
            Map<String, String> map = new HashMap<String, String>();
            for (UserType userType : listUserType) {
                map.put(userType.getTypeCode(), userType.getTypeName());
            }
            for (Module module : list) {
                if (module.getUserType().length() > 0) {
                    String[] aStr = module.getUserType().split(",");
                    for (int i = 0; i < aStr.length; i++) {
                        aStr[i] = map.get(aStr[i].trim());
                    }
                    module.setUserType(StringUtils.join(aStr, ","));
                }
            }
        }else {
            list = moduleDao.selectByRelationUrl("%"+relationUrl+"%");
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

        String userTypeStr="";
        if(StringUtils.equals("0",permType)) { //如果是用户获取用户类别
            User user = userDao.selectByPrimaryKey(permUserId);
            userTypeStr = user.getUserType();
        }

        if(currentPage>0) {
            PageHelper.startPage(currentPage, pageSize);
        }
        if(StringUtils.length(orderByStr)>0) {
            PageHelper.orderBy(orderByStr);
        }
        searchStr="%"+searchStr+"%";
        List<Map> list = null;

        if(StringUtils.equals("0",permType)) { //用户
            //获取模块信息，其中包含此用户是否有权限的标志
            list = moduleDao.selectUserAllPerm(searchStr, permUserId, "%,"+userTypeStr+",%");
            //获取用户的用户组权限
            List<Map> list2 = moduleDao.selectGroupPermForUser(permUserId);
            String groupPerm ="";
            if(null!=list2&&list2.size()>0) {
                String[] aStr = new String[list2.size()];
                for(int i=0;i<list2.size();i++) {
                    aStr[i]=list2.get(i).get("moduleCode").toString();
                }
                groupPerm = StringUtils.join(aStr,",");
            }
            //获取通用权限
            list2 = moduleDao.selectCommonPerm("%," + userTypeStr+",%");
            String commonPerm ="";
            if(null!=list2&&list2.size()>0) {
                String[] aStr = new String[list2.size()];
                for(int i=0;i<list2.size();i++) {
                    aStr[i]=list2.get(i).get("moduleCode").toString();
                }
                commonPerm = StringUtils.join(aStr,",");
            }
            //System.out.println("groupPerm="+groupPerm);
            //System.out.println("commonPerm="+commonPerm);
            UserType userType = userTypeDao.selectByCode(userTypeStr);
            String userTypeName=userType.getTypeName();

            for(Map map : list) {
                if(StringUtils.equals("1",map.get("hasPerm").toString())) {
                    map.put("permType", "0");
                    map.put("permRemark", "");
                    if (StringUtils.length(groupPerm) > 0) {
                        if (("," + groupPerm + ",").indexOf("," + map.get("moduleCode").toString() + ",") != -1) {
                            map.put("permType", "2"); //设置为组权限
                            map.put("permRemark", "");
                        }
                    }
                    if (StringUtils.length(commonPerm) > 0) {
                        if (("," + commonPerm + ",").indexOf("," + map.get("moduleCode").toString() + ",") != -1) {
                            map.put("permType", "1"); //设置为通用权限
                            map.put("permRemark", userTypeName);
                        }
                    }
                }
            }
        }else if(StringUtils.equals("1",permType)) { //用户组
            list = moduleDao.selectForPerm(searchStr, permUserId, permType);
        }

        //code转换成中文字
        List<UserType> listUserType = userTypeDao.listUserType("%%");
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
            }else if(StringUtils.equals("0", type)) { //用户
                String[] aStr = moduleIds.split(",");
                for (String moduleId : aStr) {
                    UserPerm userPerm = new UserPerm();
                    userPerm.setModuleId(Long.valueOf(moduleId));
                    userPerm.setUserId(id);
                    userPerm.setCanInherit(Byte.valueOf("0"));
                    userPerm.setIsUserGroup(Integer.valueOf(type));
                    result = userPermDao.insertSelective(userPerm);
                }
            }
        }
        return result;
    }

    public JSONObject listMenu(String userId) {
        User user = userDao.selectByPrimaryKey(userId);
        String userType = user.getUserType();
        List<Map> listPerm = moduleDao.selectAllPerm(userId,"%," + userType+",%");
        List<String> listModuleCode = new ArrayList<String>();
        for(Map map : listPerm) {
            String moduleCode = map.get("moduleCode").toString();
            //System.out.println("moduleCode="+moduleCode+"<<<<");
            listModuleCode.add(moduleCode);
        }
        List<Map> listMenu = moduleDao.selectMenu(listModuleCode);
//        for(Map map : listMenu) {
//            System.out.println("classId="+map.get("classId")+" itemName="+map.get("itemName")+" url="+map.get("url"));
//        }
        //将菜单list转换成两层结构，方便前端输出
        List<Map> list = new ArrayList<Map>();
        int listIndex=0;
        List<Map> childList=new ArrayList<Map>();
        for(int i=0;i<listMenu.size();i++) {
            Map map = listMenu.get(i);
            if(StringUtils.length(map.get("classId").toString())==10) {
                if(i!=0) {
                    list.get(listIndex++).put("child",childList);
                    childList=new ArrayList<Map>();
                }
                Map itemMap = new HashMap();
                itemMap.put("itemName",map.get("itemName").toString());
                itemMap.put("level","1");
                list.add(itemMap);
            }else {
                Map itemMap = new HashMap();
                itemMap.put("itemName",map.get("itemName").toString());
                if(StringUtils.length(map.get("classId").toString())==20) {
                    itemMap.put("level", "2");
                }else {
                    itemMap.put("level", "3");
                    itemMap.put("url", map.get("url").toString());
                }
                childList.add(itemMap);
            }
        }
        if(null!=list&&list.size()>0) {
            list.get(listIndex).put("child", childList);
        }

        JSONObject json=new JSONObject();
        json.put("menu",list);
        return json;
    }

    public boolean hasPerm(String userId,String moduleCode) {
        User user = userDao.selectByPrimaryKey(userId);
        String userType = user.getUserType();
        List<Map> listPerm = moduleDao.selectAllPerm(userId,"%," + userType+",%");
        for(Map map : listPerm) {
            if(StringUtils.equals(moduleCode,map.get("moduleCode").toString())) {
                return true;
            }
        }
        return false;
    }
}
