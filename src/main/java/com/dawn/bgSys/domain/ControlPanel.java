package com.dawn.bgSys.domain;

import java.util.Date;

public class ControlPanel {
    private Long itemId;

    private String itemName;

    private String classId;

    private Long parentId;

    private Long rootId;

    private Byte isCommend;

    private Byte applyRange;

    private String userId;

    private String icon;

    private String keywords;

    private Byte contentType;

    private Long moduleId;

    private Long linkId;

    private Byte isCompleted;

    private String remark;

    private Date commendDateBegin;

    private Date commendDateEnd;

    private Byte status;

    private Byte isQuickLink;

    private String portalIcon;

    private String moduleCode;

    private String moduleName;

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId == null ? null : classId.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Byte getIsCommend() {
        return isCommend;
    }

    public void setIsCommend(Byte isCommend) {
        this.isCommend = isCommend;
    }

    public Byte getApplyRange() {
        return applyRange;
    }

    public void setApplyRange(Byte applyRange) {
        this.applyRange = applyRange;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public Byte getContentType() {
        return contentType;
    }

    public void setContentType(Byte contentType) {
        this.contentType = contentType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public Byte getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Byte isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCommendDateBegin() {
        return commendDateBegin;
    }

    public void setCommendDateBegin(Date commendDateBegin) {
        this.commendDateBegin = commendDateBegin;
    }

    public Date getCommendDateEnd() {
        return commendDateEnd;
    }

    public void setCommendDateEnd(Date commendDateEnd) {
        this.commendDateEnd = commendDateEnd;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIsQuickLink() {
        return isQuickLink;
    }

    public void setIsQuickLink(Byte isQuickLink) {
        this.isQuickLink = isQuickLink;
    }

    public String getPortalIcon() {
        return portalIcon;
    }

    public void setPortalIcon(String portalIcon) {
        this.portalIcon = portalIcon == null ? null : portalIcon.trim();
    }
}