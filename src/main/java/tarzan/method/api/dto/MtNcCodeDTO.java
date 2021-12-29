package tarzan.method.api.dto;

import java.io.Serializable;

public class MtNcCodeDTO implements Serializable {

    private static final long serialVersionUID = -1165698263207708017L;
    private String ncCodeId;
    private String ncGroupId;
    private String siteId;
    private String ncCode;
    private String description;
    private String ncType;
    private String enableFlag;
    private Long priority;
    private String closureRequired;
    private String confirmRequired;
    private String autoCloseIncident;
    private String autoClosePrimary;
    private String canBePrimaryCode;
    private String validAtAllOperations;
    private String allowNoDisposition;
    private String componentRequired;
    private String dispositionGroupId;
    private Long maxNcLimit;
    private String secondaryCodeSpInstr;
    private String secondaryReqdForClose;

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
}
