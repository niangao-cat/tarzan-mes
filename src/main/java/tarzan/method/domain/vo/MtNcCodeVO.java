package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/17 14:53
 */
public class MtNcCodeVO implements Serializable {

    private static final long serialVersionUID = -1342104463921703531L;
    @ApiModelProperty("不良代码ID")
    private String ncCodeId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("不良代码编码")
    private String ncCode;
    @ApiModelProperty("不良代码描述")
    private String description;
    @ApiModelProperty("不良代码组ID")
    private String ncGroupId;
    @ApiModelProperty("不良代码类别")
    private String ncType;
    @ApiModelProperty("是否可供车间使用")
    private String enableFlag;
    @ApiModelProperty("优先级")
    private Long priority;
    @ApiModelProperty("是否需要被关闭")
    private String closureRequired;
    @ApiModelProperty("是否需要复核")
    private String confirmRequired;
    @ApiModelProperty("自动关闭事故")
    private String autoCloseIncident;
    @ApiModelProperty("autoClosePrimary")
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNcCode() {
        return ncCode;
    }

    public void setNcCode(String ncCode) {
        this.ncCode = ncCode;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getValidAtAllOperations() {
        return validAtAllOperations;
    }

    public void setValidAtAllOperations(String validAtAllOperations) {
        this.validAtAllOperations = validAtAllOperations;
    }

    public String getAutoClosePrimary() {
        return autoClosePrimary;
    }

    public void setAutoClosePrimary(String autoClosePrimary) {
        this.autoClosePrimary = autoClosePrimary;
    }

    public String getAutoCloseIncident() {
        return autoCloseIncident;
    }

    public void setAutoCloseIncident(String autoCloseIncident) {
        this.autoCloseIncident = autoCloseIncident;
    }

    public String getSecondaryReqdForClose() {
        return secondaryReqdForClose;
    }

    public void setSecondaryReqdForClose(String secondaryReqdForClose) {
        this.secondaryReqdForClose = secondaryReqdForClose;
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

    public String getConfirmRequired() {
        return confirmRequired;
    }

    public void setConfirmRequired(String confirmRequired) {
        this.confirmRequired = confirmRequired;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getNcGroupId() {
        return ncGroupId;
    }

    public void setNcGroupId(String ncGroupId) {
        this.ncGroupId = ncGroupId;
    }

    public String getClosureRequired() {
        return closureRequired;
    }

    public void setClosureRequired(String closureRequired) {
        this.closureRequired = closureRequired;
    }

    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getCanBePrimaryCode() {
        return canBePrimaryCode;
    }

    public void setCanBePrimaryCode(String canBePrimaryCode) {
        this.canBePrimaryCode = canBePrimaryCode;
    }

    public String getNcType() {
        return ncType;
    }

    public void setNcType(String ncType) {
        this.ncType = ncType;
    }

    @Override
    public String toString() {
        return "MtNcCodeVO{" + "ncCodeId='" + ncCodeId + '\'' + ", siteId='" + siteId + '\'' + ", ncCode='" + ncCode
                        + '\'' + ", description='" + description + '\'' + ", ncGroupId='" + ncGroupId + '\''
                        + ", ncType='" + ncType + '\'' + ", enableFlag='" + enableFlag + '\'' + ", priority=" + priority
                        + ", closureRequired='" + closureRequired + '\'' + ", confirmRequired='" + confirmRequired
                        + '\'' + ", autoCloseIncident='" + autoCloseIncident + '\'' + ", autoClosePrimary='"
                        + autoClosePrimary + '\'' + ", canBePrimaryCode='" + canBePrimaryCode + '\''
                        + ", validAtAllOperations='" + validAtAllOperations + '\'' + ", allowNoDisposition='"
                        + allowNoDisposition + '\'' + ", componentRequired='" + componentRequired + '\''
                        + ", dispositionGroupId='" + dispositionGroupId + '\'' + ", maxNcLimit=" + maxNcLimit
                        + ", secondaryCodeSpInstr='" + secondaryCodeSpInstr + '\'' + ", secondaryReqdForClose='"
                        + secondaryReqdForClose + '\'' + '}';
    }
}
