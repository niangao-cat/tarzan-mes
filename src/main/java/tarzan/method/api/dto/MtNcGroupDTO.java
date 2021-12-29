package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;

public class MtNcGroupDTO implements Serializable {
    private static final long serialVersionUID = -7741925590324361918L;

    @ApiModelProperty("唯一标识：表ID，主键，供其他表做外键")
    private String ncGroupId;
    @ApiModelProperty(value = "站点")
    @NotBlank
    private String siteId;
    @ApiModelProperty("站点编码")
    private String siteCode;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty(value = "不良代码组编码")
    @NotBlank
    private String ncGroupCode;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "优先级:编号越大，优先级越高")
    @NotNull
    private Long priority;
    @ApiModelProperty(value = "是否需要被关闭")
    @NotBlank
    private String closureRequired;
    @ApiModelProperty(value = "是否需要复核")
    @NotBlank
    private String confirmRequired;
    @ApiModelProperty(value = "自动关闭事故")
    @NotBlank
    private String autoCloseIncident;
    @ApiModelProperty(value = "自动关闭主代码")
    @NotBlank
    private String autoClosePrimary;
    @ApiModelProperty(value = "可以是主代码")
    @NotBlank
    private String canBePrimaryCode;
    @ApiModelProperty(value = "对所有工艺有效")
    @NotBlank
    private String validAtAllOperations;
    @ApiModelProperty(value = "允许无处置")
    @NotBlank
    private String allowNoDisposition;
    @ApiModelProperty(value = "是否需要记录组件")
    @NotBlank
    private String componentRequired;
    @ApiModelProperty(value = "处置组Id")
    private String dispositionGroupId;
    @ApiModelProperty(value = "处置组")
    private String dispositionGroup;
    @ApiModelProperty(value = "处置组描述")
    private String dispositionGroupDesc;
    @ApiModelProperty(value = "最大限制值")
    @NotNull
    private Long maxNcLimit;
    @ApiModelProperty(value = "次级代码特殊指令")
    private String secondaryCodeSpInstr;
    @ApiModelProperty(value = "需要次级代码才能关闭")
    @NotBlank
    private String secondaryReqdForClose;

    @ApiModelProperty(value = "是否启用")
    private String enableFlag;

    @ApiModelProperty("不良代码组扩展属性")
    private List<MtExtendAttrDTO> ncGroupAttrList;

    @ApiModelProperty("工艺ID")
    private String operationId;

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

    public String getDispositionGroup() {
        return dispositionGroup;
    }

    public void setDispositionGroup(String dispositionGroup) {
        this.dispositionGroup = dispositionGroup;
    }

    public String getDispositionGroupDesc() {
        return dispositionGroupDesc;
    }

    public void setDispositionGroupDesc(String dispositionGroupDesc) {
        this.dispositionGroupDesc = dispositionGroupDesc;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getSecondaryReqdForClose() {
        return secondaryReqdForClose;
    }

    public void setSecondaryReqdForClose(String secondaryReqdForClose) {
        this.secondaryReqdForClose = secondaryReqdForClose;
    }

    public List<MtExtendAttrDTO> getNcGroupAttrList() {
        return ncGroupAttrList;
    }

    public void setNcGroupAttrList(List<MtExtendAttrDTO> ncGroupAttrList) {
        this.ncGroupAttrList = ncGroupAttrList;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
