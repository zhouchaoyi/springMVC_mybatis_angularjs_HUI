package com.dawn.bgSys.utils;

import com.dawn.bgSys.dao.ICommonDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouchaoyi on 2016/5/19.
 */
@Component
public class TreeModel {

    @Autowired
    private ICommonDao commonDao;

    public static final int CLS_ID_LENGTH = 10;
    public static final int DELETE_ALL = 1;
    public static final int DELETE_SELECTION = 2;
    public static final int DELETE_CHECK_SUBNODE = 4;
    private String sTableName;
    private String sIdField;
    private String sParentIdField;
    private String sClassIdField;
    private String sFilter;
    private String sPrompt;
    private int nSectionLength;
    private String sError = "";

    public String getsTableName() {
        return sTableName;
    }

    public void setsTableName(String sTableName) {
        this.sTableName = sTableName;
    }

    public String getsIdField() {
        return sIdField;
    }

    public void setsIdField(String sIdField) {
        this.sIdField = sIdField;
    }

    public String getsFilter() {
        return sFilter;
    }

    public void setsFilter(String sFilter) {
        this.sFilter = sFilter;
    }

    public String getError() {
        return this.sError;
    }

    public TreeModel() {
        this.sTableName = "";
        this.sIdField = "";
        this.sParentIdField = "parent_id";
        this.sClassIdField = "class_id";
        this.sFilter = "";
        this.sPrompt = "";
        this.nSectionLength = 10;
    }

    public TreeModel(String tableName, String idField, String parentIdField, String classIdField, String filter) {
        this.sTableName = "";
        this.sIdField = "";
        this.sParentIdField = "";
        this.sClassIdField = "";
        this.sFilter = "";
        this.sPrompt = "";
        this.nSectionLength = 10;
        this.sTableName = tableName;
        this.sIdField = idField;
        this.sParentIdField = parentIdField;
        this.sClassIdField = classIdField;
        this.sFilter = filter;
    }


    public TreeModel(String var1, String var2, String var3, String var4) {
        this(var1, var2, var3, var4, "");
    }

    public TreeModel(String var1, String var2, String var3) {
        this(var1, var2, "parent_id", "class_id", var3);
    }

    public TreeModel(String var1, String var2) {
        this(var1, var2, "parent_id", "class_id");
    }

//    public void setClassIdLength(int var1) {
//        this.nSectionLength = var1;
//    }

    public boolean resetClassId(String idFieldVal) {
        StringBuilder sql =new StringBuilder();
        sql.append("update " + this.sTableName + " set " + this.sParentIdField + "=-1 where " + this.sIdField + "=" + idFieldVal + " and (" + this.sParentIdField + " is null or " + this.sParentIdField + "=-1 or " + this.sParentIdField + "=0)" + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":""));
        //System.out.println(sql.toString());
        commonDao.update(sql.toString());

        sql =new StringBuilder();
        sql.append("select " + this.sParentIdField + "," + this.sClassIdField + " from " + this.sTableName + " where " + this.sIdField + "=" + idFieldVal + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":""));
        //System.out.println(sql.toString());
        List<Map> list = commonDao.select(sql.toString());
        String classIdStr = "";
        if(list != null && list.size() != 0 && null!=list.get(0)) {
            String parentId=list.get(0).get("parent_id").toString();
            String classId="";
            if(null!=list.get(0).get("class_id")) {
                classId = list.get(0).get("class_id").toString();
            }
            if(!parentId.equals("") && !parentId.equals("-1") && !parentId.equals(idFieldVal)) {
                sql =new StringBuilder();
                sql.append("select " + this.sClassIdField + " from " + this.sTableName + " where " + this.sIdField + "=" + parentId + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":""));
                //System.out.println(sql.toString());
                String parentClassId = commonDao.select(sql.toString()).get(0).get("class_id").toString();
                classIdStr = classId.indexOf(parentClassId) != 0?parentClassId + StringUtils.leftPad(idFieldVal,this.nSectionLength ,'0'):parentClassId + classId.substring(classId.length() - this.nSectionLength);
            } else {
                classIdStr = classId.length() != this.nSectionLength?StringUtils.leftPad(idFieldVal, this.nSectionLength, '0'):classId;
            }

            sql =new StringBuilder();
            sql.append("update " + this.sTableName + " set " + this.sClassIdField + "=\'" + classIdStr + "\' where " + this.sIdField + "=" + idFieldVal);
            //System.out.println(sql.toString());
            commonDao.update(sql.toString());
            if(classId.length() > 0) {
                sql =new StringBuilder();
                sql.append("update " + this.sTableName + " set " + this.sClassIdField + "=concat(\'" + classIdStr + "\', substring(" + this.sClassIdField + ", "+classId.length() + "+1,length("  +this.sClassIdField + ")"+"-" + classId.length() + ")) where " + this.sClassIdField + " like concat(\'" + classId + "\', \'%\')" + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":""));
                //System.out.println(sql.toString());
                commonDao.update(sql.toString());
            }

            return true;
        } else {
            return false;
        }
    }

//    public boolean setParent(String var1, String var2) {
//        SQLCommand var3 = new SQLCommand(this.connConfig);
//        boolean var4 = true;
//        var4 = var4 && var3.execute("update " + this.sTableName + " set " + this.sParentIdField + "=" + var2 + " where " + this.sIdField + "=" + var1);
//        var4 = var4 && this.resetClassId(var1);
//        return var4;
//    }
//
//    public boolean deleteTreeNode(String var1) {
//        return this.deleteTreeNode(var1, 1);
//    }
//
//    public boolean deleteTreeNode(String var1, int var2) {
//        SQLCommand var3 = new SQLCommand(this.connConfig);
//        SQLString var4 = SQLString.getInstance(this.connConfig);
//        String var5 = "select t1." + this.sIdField + " from " + this.sTableName + " t1 \n";
//        var5 = var5 + "inner join " + this.sTableName + " t2 on t1." + this.sClassIdField + " like " + var4.concat("t2." + this.sClassIdField, "\'%\'") + " \n";
//        var5 = var5 + "where t2." + this.sIdField + " in(" + var1 + ")";
//        String var6 = var3.queryCSV(var5, "\"", ",", ",");
//        SQLBlock var7 = SQLBlock.getInstance(this.connConfig);
//        var7.append("delete from " + this.sTableName + " where " + this.sIdField + " in(" + var6 + ")");
//        if(!var3.execute(var7.getOutput())) {
//            this.sError = this.sError + var3.getError();
//            return false;
//        } else {
//            return true;
//        }
//    }
//
    public boolean moveUp(String sIdField) {
        StringBuilder sql = new StringBuilder();
        sql.append("select " + this.sClassIdField + "," + this.sParentIdField + " from " + this.sTableName + " where " + this.sIdField + "=" + sIdField);
        List<Map> list = commonDao.select(sql.toString());
        if(list != null && list.size() != 0 && null!=list.get(0)) {
            String classId = list.get(0).get("class_id").toString();
            String parentId = list.get(0).get("parent_id").toString();
            if(null==parentId) {
                parentId="-1";
            }
            sql = new StringBuilder();
            sql.append("select " + this.sIdField + " from (select " + this.sIdField + "," + this.sClassIdField + " from " + this.sTableName + " where " + this.sClassIdField + "<\'" + classId + "\' and (" + this.sParentIdField + "=" + parentId + " or (" + this.sParentIdField + "=0 and " + parentId + "=0))" + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":"") + ") a order by " + this.sClassIdField + " desc");
            List<Map> list2 = commonDao.select(sql.toString());
            if(list2 == null || list2.size() == 0) {
                this.sPrompt = "所选节点已经处于最顶端，位置不能够上移";
                return false;
            } else {
                return this.changeClassId(sIdField, list2.get(0).get(this.sIdField).toString());
            }
        } else {
            return false;
        }
    }

    public boolean moveDown(String sIdField) {
        StringBuilder sql = new StringBuilder();
        sql.append("select " + this.sClassIdField + "," + this.sParentIdField + " from " + this.sTableName + " where " + this.sIdField + "=" + sIdField);
        List<Map> list = commonDao.select(sql.toString());
        if(list != null && list.size() != 0 && null!=list.get(0)) {
            String classId = list.get(0).get("class_id").toString();
            String parentId = list.get(0).get("parent_id").toString();
            if(null==parentId) {
                parentId="-1";
            }
            sql = new StringBuilder();
            sql.append("select " + this.sIdField + " from (select " + this.sIdField + "," + this.sClassIdField + " from " + this.sTableName + " where " + this.sClassIdField + ">\'" + classId + "\' and (" + this.sParentIdField + "=" + parentId + " or (" + this.sParentIdField + "=0 and " + parentId + "=0))" + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":"") + ") a order by " + this.sClassIdField);
            List<Map> list2 = commonDao.select(sql.toString());
            if(list2 == null || list2.size() == 0) {
                this.sPrompt = "所选节点已经处于最底端，位置不能够下移";
                return false;
            } else {
                return this.changeClassId(sIdField, list2.get(0).get(this.sIdField).toString());
            }
        } else {
            return false;
        }
    }
//
//    public String getNodePath(String var1, String var2) {
//        return this.getNodePath(var1, var2, "/");
//    }
//
//    public String getNodePath(String var1, String var2, String var3) {
//        SQLCommand var4 = new SQLCommand(this.connConfig);
//        SQLString var5 = SQLString.getInstance(this.connConfig);
//        if(var1 != null && var1.length() != 0) {
//            String var6 = "select f1." + var2 + " from " + (this.sFilter.length() > 0?"(select " + this.sIdField + "," + var2 + "," + this.sClassIdField + " from " + this.sTableName + " where " + this.sFilter + ")":this.sTableName) + " f1 \n";
//            var6 = var6 + "inner join " + (this.sFilter.length() > 0?"(select " + this.sIdField + "," + var2 + "," + this.sClassIdField + " from " + this.sTableName + " where " + this.sFilter + ")":this.sTableName) + " f2 on f2." + this.sClassIdField + " like " + var5.concat("f1." + this.sClassIdField, "\'%\'") + " \n";
//            var6 = var6 + "where f2." + this.sIdField + "=" + var1 + " \n";
//            var6 = var6 + "order by f1." + this.sClassIdField + " asc \n";
//            String var7 = var4.queryCSV(var6, "", var3, var3);
//            this.sError = this.sError + var4.getError();
//            return var3.equalsIgnoreCase("/")?"/" + var7:var7;
//        } else {
//            return "";
//        }
//    }
//
    private boolean changeClassId(String sIdField, String sIdField2) {
        StringBuilder sql = new StringBuilder();
        sql.append("select " + this.sClassIdField + " from " + this.sTableName + " where " + this.sIdField + "=" + sIdField + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":""));
        List<Map> list = commonDao.select(sql.toString());
        String classId=list.get(0).get("class_id").toString();

        sql = new StringBuilder();
        sql.append("select " + this.sClassIdField + " from " + this.sTableName + " where " + this.sIdField + "=" + sIdField2 + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":""));
        list = commonDao.select(sql.toString());
        String classId2=list.get(0).get("class_id").toString();

        sql = new StringBuilder();
        sql.append("update " + this.sTableName + " set " + this.sClassIdField + "= concat(\'A" + classId2 + "\',substring(" + this.sClassIdField + ",1+" + classId.length()+", length(" + this.sClassIdField + ")" + "-" + classId.length()+"))" + " where " + this.sClassIdField + " like \'" + classId + "%\'" + (this.sFilter.length() > 0?" and (" + this.sFilter + ")":""));
        commonDao.update(sql.toString());

        sql = new StringBuilder();
        sql.append("update " + this.sTableName + " set " + this.sClassIdField + "= concat(\'" + classId + "\', substring(" + this.sClassIdField + ",1+" + classId2.length()+", length(" + this.sClassIdField + ")" + "-" + classId2.length()+"))" + " where " + this.sClassIdField + " like \'" + classId2 + "%\'" + (this.sFilter.length() > 0 ? " and (" + this.sFilter + ")" : ""));
        commonDao.update(sql.toString());

        sql = new StringBuilder();
        sql.append("update " + this.sTableName + " set " + this.sClassIdField + "= substring(" + this.sClassIdField + ",2, length(" + this.sClassIdField + ")" + "-1)" + " where " + this.sClassIdField + " like \'A" + classId2 + "%\'" + (this.sFilter.length() > 0 ? " and (" + this.sFilter + ")" : ""));
        commonDao.update(sql.toString());

        return true;
    }

    public String getPrompt() {
        return this.sPrompt;
    }
}
