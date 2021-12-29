package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtNcGroupDTO3 implements Serializable {
    private static final long serialVersionUID = 7260829994602035898L;

    @ApiModelProperty("唯一标识：表ID，主键，供其他表做外键")
    private String ncGroupId;
    @ApiModelProperty(value = "站点")
    @NotBlank
    private String siteId;
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
    @ApiModelProperty(value = "处置组")
    private String dispositionGroupId;
    @ApiModelProperty(value = "最大限制值")
    @NotNull
    private Long maxNcLimit;
    @ApiModelProperty(value = "次级代码特殊指令")
    private String secondaryCodeSpInstr;
    @ApiModelProperty(value = "需要次级代码才能关闭")
    @NotBlank
    private String secondaryReqdForClose;
    @ApiModelProperty(value = "是否启用（Y/N）", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();
    @ApiModelProperty("不良代码组扩展属性")
    private List<MtExtendAttrDTO3> ncGroupAttrList;

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

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public List<MtExtendAttrDTO3> getNcGroupAttrList() {
        return ncGroupAttrList;
    }

    public void setNcGroupAttrList(List<MtExtendAttrDTO3> ncGroupAttrList) {
        this.ncGroupAttrList = ncGroupAttrList;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
