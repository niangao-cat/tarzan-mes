package io.tarzan.common.domain.vo;

import java.io.Serializable;

public class MtNumrangeRuleVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5594641870015910286L;
    private String numRule;
    private String fixInput;
    private String numLevel;
    private Long numConnectInputBox;
    private String numLowerLimit;
    private String numUpperLimit;
    private String numAlert;
    private String numAlertType;
    private String numRadix;
    private Long numIncrement;
    private String numCurrent;
    private String numResetType;
    private Long numResetPeriod;
    private String dateFormat;
    private String timeFormat;
    private String callStandardObject;
    private Long incomeValueLength;

    public String getNumRule() {
        return numRule;
    }

    public void setNumRule(String numRule) {
        this.numRule = numRule;
    }

    public String getFixInput() {
        return fixInput;
    }

    public void setFixInput(String fixInput) {
        this.fixInput = fixInput;
    }

    public String getNumLevel() {
        return numLevel;
    }

    public void setNumLevel(String numLevel) {
        this.numLevel = numLevel;
    }

    public Long getNumConnectInputBox() {
        return numConnectInputBox;
    }

    public void setNumConnectInputBox(Long numConnectInputBox) {
        this.numConnectInputBox = numConnectInputBox;
    }

    public String getNumLowerLimit() {
        return numLowerLimit;
    }

    public void setNumLowerLimit(String numLowerLimit) {
        this.numLowerLimit = numLowerLimit;
    }

    public String getNumUpperLimit() {
        return numUpperLimit;
    }

    public void setNumUpperLimit(String numUpperLimit) {
        this.numUpperLimit = numUpperLimit;
    }

    public String getNumAlert() {
        return numAlert;
    }

    public void setNumAlert(String numAlert) {
        this.numAlert = numAlert;
    }

    public String getNumAlertType() {
        return numAlertType;
    }

    public void setNumAlertType(String numAlertType) {
        this.numAlertType = numAlertType;
    }

    public String getNumRadix() {
        return numRadix;
    }

    public void setNumRadix(String numRadix) {
        this.numRadix = numRadix;
    }

    public Long getNumIncrement() {
        return numIncrement;
    }

    public void setNumIncrement(Long numIncrement) {
        this.numIncrement = numIncrement;
    }

    public String getNumCurrent() {
        return numCurrent;
    }

    public void setNumCurrent(String numCurrent) {
        this.numCurrent = numCurrent;
    }

    public String getNumResetType() {
        return numResetType;
    }

    public void setNumResetType(String numResetType) {
        this.numResetType = numResetType;
    }

    public Long getNumResetPeriod() {
        return numResetPeriod;
    }

    public void setNumResetPeriod(Long numResetPeriod) {
        this.numResetPeriod = numResetPeriod;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getCallStandardObject() {
        return callStandardObject;
    }

    public void setCallStandardObject(String callStandardObject) {
        this.callStandardObject = callStandardObject;
    }

    public Long getIncomeValueLength() {
        return incomeValueLength;
    }

    public void setIncomeValueLength(Long incomeValueLength) {
        this.incomeValueLength = incomeValueLength;
    }

}
