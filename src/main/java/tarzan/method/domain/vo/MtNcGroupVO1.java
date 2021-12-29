package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtNcGroupVO1 implements Serializable {

    private static final long serialVersionUID = -9194603237652851559L;
    @ApiModelProperty("不良代码组ID")
    private String ncGroupId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("站点编码")
    private String siteCode;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty("不良代码组编码")
    private String ncGroupCode;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("优先级")
    private Long priority;
    @ApiModelProperty("是否需要被关闭")
    private String closureRequired;
    @ApiModelProperty("是否需要复核")
    private String confirmRequired;
    @ApiModelProperty("自动关闭事故")
    private String autoCloseIncident;
    @ApiModelProperty("自动关闭主代码")
    private String autoClosePrimary;
    @ApiModelProperty("可以是主代码")
    private String canBePrimaryCode;
    @ApiModelProperty("对所有工艺有效")
    private String validAtAllOperations;
    @ApiModelProperty("允许无处置")
    private String allowNoDisposition;
    @ApiModelProperty("是否需要记录组件")
    private String componentRequired;
    @ApiModelProperty("处置组")
    private String dispositionGroupId;
    @ApiModelProperty("最大限制值")
    private Long maxNcLimit;
    @ApiModelProperty("次级代码特殊指令")
    private String secondaryCodeSpInstr;
    @ApiModelProperty("需要次级代码才能关闭")
    private String secondaryReqdForClose;

    public String getNcGroupId() {
        return ncGroupId;
    }

    public void setNcGroupId(String ncGroupId) {
        this.ncGroupId = ncGroupId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getNcGroupCode() {
        return ncGroupCode;
    }

    public void setNcGroupCode(String ncGroupCode) {
        this.ncGroupCode = ncGroupCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getClosureRequired() {
        return closureRequired;
    }

    public void setClosureRequired(String closureRequired) {
        this.closureRequired = closureRequired;
    }

    public String getConfirmRequired() {
        return confirmRequired;
    }

    public void setConfirmRequired(String confirmRequired) {
        this.confirmRequired = confirmRequired;
    }

    public String getAutoCloseIncident() {
        return autoCloseIncident;
    }

    public void setAutoCloseIncident(String autoCloseIncident) {
        this.autoCloseIncident = autoCloseIncident;
    }

    public String getAutoClosePrimary() {
        return autoClosePrimary;
    }

    public void setAutoClosePrimary(String autoClosePrimary) {
        this.autoClosePrimary = autoClosePrimary;
    }

    public String getCanBePrimaryCode() {
        return canBePrimaryCode;
    }

    public void setCanBePrimaryCode(String canBePrimaryCode) {
        this.canBePrimaryCode = canBePrimaryCode;
    }

    public String getValidAtAllOperations() {
        return validAtAllOperations;
    }

    public void setValidAtAllOperations(String validAtAllOperations) {
        this.validAtAllOperations = validAtAllOperations;
    }

    public String getAllowNoDisposition() {
        return allowNoDisposition;
    }

    public void setAllowNoDisposition(String allowNoDisposition) {
        this.allowNoDisposition = allowNoDisposition;
    }

    public String getComponentRequired() {
        return componentRequired;
    }

    public void setComponentRequired(String componentRequired) {
        this.componentRequired = componentRequired;
    }

    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    public Long getMaxNcLimit() {
        return maxNcLimit;
    }

    public void setMaxNcLimit(Long maxNcLimit) {
        this.maxNcLimit = maxNcLimit;
    }

    public String getSecondaryCodeSpInstr() {
        return secondaryCodeSpInstr;
    }

    public void setSecondaryCodeSpInstr(String secondaryCodeSpInstr) {
        this.secondaryCodeSpInstr = secondaryCodeSpInstr;
    }

    public String getSecondaryReqdForClose() {
        return secondaryReqdForClose;
    }

    public void setSecondaryReqdForClose(String secondaryReqdForClose) {
        this.secondaryReqdForClose = secondaryReqdForClose;
    }


}
