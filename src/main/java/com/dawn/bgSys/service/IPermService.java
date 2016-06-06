package com.dawn.bgSys.service;

import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.domain.*;

import java.util.List;

/**
 * Created by zhouchaoyi on 2016/4/5.
 */
public interface IPermService {
    public Module addModule(Module module);

    public JSONObject updateModule(Module module);

    public JSONObject listModule(int currentPage,int pageSize,String orderBy,String searchStr,String status,String parentClassId);

    public JSONObject listModuleForPerm(int currentPage,int pageSize,String orderBy,String searchStr,String permUserId,String permType);

    public int deleteModule(String moduleId);

    public Module queryModuleById(String id);

    public int moveModule(String moduleId,String move);

    public JSONObject listControlPanel(int currentPage,int pageSize,String orderBy,String searchStr,String classLevel,String status,String parentClassId);

    public int addControlPanel(ControlPanel panel);

    public int updateControlPanel(ControlPanel panel);

    public ControlPanel queryControlPanelById(String id);

    public int deleteControlPanel(List<String> ids);

    public int moveControlPanel(String itemId,String move);

    public int addPerm(String id,String type,String moduleIds);
}
