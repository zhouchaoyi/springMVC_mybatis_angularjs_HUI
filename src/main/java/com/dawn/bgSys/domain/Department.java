package com.dawn.bgSys.domain;

public class Department {
    private Long departmentId;

    private String departmentName;

    private Long parentId;

    private String classId;

    private String remark;

    private String departmentKey;

    private Byte isTypeOnly;

    private Long isDepartment;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId == null ? null : classId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDepartmentKey() {
        return departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this.departmentKey = departmentKey == null ? null : departmentKey.trim();
    }

    public Byte getIsTypeOnly() {
        return isTypeOnly;
    }

    public void setIsTypeOnly(Byte isTypeOnly) {
        this.isTypeOnly = isTypeOnly;
    }

    public Long getIsDepartment() {
        return isDepartment;
    }

    public void setIsDepartment(Long isDepartment) {
        this.isDepartment = isDepartment;
    }
}