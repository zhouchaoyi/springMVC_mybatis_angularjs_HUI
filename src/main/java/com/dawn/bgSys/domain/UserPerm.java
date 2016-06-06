package com.dawn.bgSys.domain;

public class UserPerm {
    private Long permId;

    private String userId;

    private Long userGroupId;

    private int isUserGroup;

    private Long moduleId;

    private Byte canInherit;

    public Long getPermId() {
        return permId;
    }

    public void setPermId(Long permId) {
        this.permId = permId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public int getIsUserGroup() {
        return isUserGroup;
    }

    public void setIsUserGroup(int isUserGroup) {
        this.isUserGroup = isUserGroup;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Byte getCanInherit() {
        return canInherit;
    }

    public void setCanInherit(Byte canInherit) {
        this.canInherit = canInherit;
    }
}