package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtNcCodeDTO4 implements Serializable {
    private static final long serialVersionUID = 4687261279986570514L;

    @ApiModelProperty("不良代码Id")
    private String ncCodeId;
    @ApiModelProperty(value = "不良代码组关联字段")
    private String ncGroupId;
    @ApiModelProperty(value = "站点为“生产类型”")
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "不良代码编码，前台通常显示此值")
    @NotBlank
    private String ncCode;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "值集已维护在表GEN_TYPE中，对应字段TYPE_GROUP为“NC”")
    @NotBlank
    private String ncType;
    @ApiModelProperty(value = "是否可供车间使用（Y/N）")
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "优先级")
    private Long priority;
    @ApiModelProperty(value = "是否需要被关闭")
    private String closureRequired;
    @ApiModelProperty(value = "是否需要复核")
    private String confirmRequired;
    @ApiModelProperty(value = "自动关闭事故")
    private String autoCloseIncident;
    @ApiModelProperty(value = "自动关闭主代码")
    private String autoClosePrimary;
    @ApiModelProperty(value = "可以是主代码")
    private String canBePrimaryCode;
    @ApiModelProperty(value = "对所有工艺有效")
    private String validAtAllOperations;
    @ApiModelProperty(value = "允许无处置")
    private String allowNoDisposition;
    @ApiModelProperty(value = "是否需要记录组件")
    private String componentRequired;
    @ApiModelProperty(value = "处置组")
    private String dispositionGroupId;
    @ApiModelProperty(value = "最大限制值")
    private Long maxNcLimit;
    @ApiModelProperty(value = "次级代码特殊指令")
    private String secondaryCodeSpInstr;
    @ApiModelProperty(value = "需要次级代码才能关闭")
    private String secondaryReqdForClose;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();
    @ApiModelProperty("不良代码扩展属性")
    private List<MtExtendAttrDTO3> ncCodeAttrList;

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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getNcCode() {
        return ncCode;
    }

    public void setNcCode(String ncCode) {
        this.ncCode = ncCode;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNcType() {
        return ncType;
    }

    public void setNcType(String ncType) {
        this.ncType = ncType;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public List<MtExtendAttrDTO3> getNcCodeAttrList() {
        return ncCodeAttrList;
    }

    public void setNcCodeAttrList(List<MtExtendAttrDTO3> ncCodeAttrList) {
        this.ncCodeAttrList = ncCodeAttrList;
    }
}
