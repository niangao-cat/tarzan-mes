package io.tarzan.common.domain.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 号码段定义组合规则表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
@ApiModel("号码段定义组合规则表")
@ModifyAudit

@Table(name = "mt_numrange_rule")
@CustomPrimary
public class MtNumrangeRule extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NUMRANGE_RULE_ID = "numrangeRuleId";
    public static final String FIELD_NUM_RULE = "numRule";
    public static final String FIELD_FIX_INPUT = "fixInput";
    public static final String FIELD_NUM_LEVEL = "numLevel";
    public static final String FIELD_NUM_CONNECT_INPUT_BOX = "numConnectInputBox";
    public static final String FIELD_NUM_LOWER_LIMIT = "numLowerLimit";
    public static final String FIELD_NUM_UPPER_LIMIT = "numUpperLimit";
    public static final String FIELD_NUM_ALERT = "numAlert";
    public static final String FIELD_NUM_ALERT_TYPE = "numAlertType";
    public static final String FIELD_NUM_RADIX = "numRadix";
    public static final String FIELD_NUM_INCREMENT = "numIncrement";
    public static final String FIELD_NUM_CURRENT = "numCurrent";
    public static final String FIELD_NUM_RESET_TYPE = "numResetType";
    public static final String FIELD_NUM_RESET_PERIOD = "numResetPeriod";
    public static final String FIELD_NUM_RESET_LASTDATE = "numResetLastdate";
    public static final String FIELD_DATE_FORMAT = "dateFormat";
    public static final String FIELD_TIME_FORMAT = "timeFormat";
    public static final String FIELD_CALL_STANDARD_OBJECT = "callStandardObject";
    public static final String FIELD_INCOME_VALUE_LENGTH = "incomeValueLength";
    public static final String FIELD_INCOME_VALUE_LENGTH_LIMIT = "incomeValueLengthLimit";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 588898265122693281L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("号码段定义组合规则表主键")
    @Id
    @Where
    private String numrangeRuleId;
    @ApiModelProperty(value = "号段组合规则选项")
    @Where
    private String numRule;
    @ApiModelProperty(value = "输入值")
    @Where
    private String fixInput;
    @ApiModelProperty(value = "序列号层级")
    @Where
    private String numLevel;
    @ApiModelProperty(value = "特定对象关联框")
    @Where
    private Long numConnectInputBox;
    @ApiModelProperty(value = "序列号段下限")
    @Where
    private String numLowerLimit;
    @ApiModelProperty(value = "序列号段上限")
    @Where
    private String numUpperLimit;
    @ApiModelProperty(value = "号段预警值")
    @Where
    private String numAlert;
    @ApiModelProperty(value = "号段预警类型")
    @Where
    private String numAlertType;
    @ApiModelProperty(value = "序列号段进制")
    @Where
    private String numRadix;
    @ApiModelProperty(value = "序列号段增量")
    @Where
    private Long numIncrement;
    @ApiModelProperty(value = "当前序号")
    @Where
    private String numCurrent;
    @ApiModelProperty(value = "序列号段重置周期类型")
    @Where
    private String numResetType;
    @ApiModelProperty(value = "序列号段重置周期时间")
    @Where
    private Long numResetPeriod;
    @ApiModelProperty(value = "序列号段上一次重置的日期")
    @Where
    private String numResetLastdate;
    @ApiModelProperty(value = "日期格式")
    @Where
    private String dateFormat;
    @ApiModelProperty(value = "时间格式")
    @Where
    private String timeFormat;
    @ApiModelProperty(value = "标准对象编码属性id")
    @Where
    private String callStandardObject;
    @ApiModelProperty(value = "传入值长度")
    @Where
    private Long incomeValueLength;
    @ApiModelProperty(value = "传入值长度上限")
    @Where
    private Long incomeValueLengthLimit;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 号码段定义组合规则表主键
     */
    public String getNumrangeRuleId() {
        return numrangeRuleId;
    }

    public void setNumrangeRuleId(String numrangeRuleId) {
        this.numrangeRuleId = numrangeRuleId;
    }

    /**
     * @return 号段组合规则选项
     */
    public String getNumRule() {
        return numRule;
    }

    public void setNumRule(String numRule) {
        this.numRule = numRule;
    }

    /**
     * @return 序列号层级
     */
    public String getNumLevel() {
        return numLevel;
    }

    public void setNumLevel(String numLevel) {
        this.numLevel = numLevel;
    }

    /**
     * @return 特定对象关联框
     */
    public Long getNumConnectInputBox() {
        return numConnectInputBox;
    }

    public void setNumConnectInputBox(Long numConnectInputBox) {
        this.numConnectInputBox = numConnectInputBox;
    }

    /**
     * @return 输入值
     */
    public String getFixInput() {
        return fixInput;
    }

    public void setFixInput(String fixInput) {
        this.fixInput = fixInput;
    }

    /**
     * @return 序列号段下限
     */
    public String getNumLowerLimit() {
        return numLowerLimit;
    }

    public void setNumLowerLimit(String numLowerLimit) {
        this.numLowerLimit = numLowerLimit;
    }

    /**
     * @return 序列号段上限
     */
    public String getNumUpperLimit() {
        return numUpperLimit;
    }

    public void setNumUpperLimit(String numUpperLimit) {
        this.numUpperLimit = numUpperLimit;
    }

    /**
     * @return 号段预警值
     */
    public String getNumAlert() {
        return numAlert;
    }

    public void setNumAlert(String numAlert) {
        this.numAlert = numAlert;
    }

    /**
     * @return 号段预警类型
     */
    public String getNumAlertType() {
        return numAlertType;
    }

    public void setNumAlertType(String numAlertType) {
        this.numAlertType = numAlertType;
    }

    /**
     * @return 序列号段进制
     */
    public String getNumRadix() {
        return numRadix;
    }

    public void setNumRadix(String numRadix) {
        this.numRadix = numRadix;
    }

    /**
     * @return 序列号段增量
     */
    public Long getNumIncrement() {
        return numIncrement;
    }

    public void setNumIncrement(Long numIncrement) {
        this.numIncrement = numIncrement;
    }

    /**
     * @return 当前序号
     */
    public String getNumCurrent() {
        return numCurrent;
    }

    public void setNumCurrent(String numCurrent) {
        this.numCurrent = numCurrent;
    }

    /**
     * @return 序列号段重置周期类型
     */
    public String getNumResetType() {
        return numResetType;
    }

    public void setNumResetType(String numResetType) {
        this.numResetType = numResetType;
    }

    /**
     * @return 序列号段重置周期时间
     */
    public Long getNumResetPeriod() {
        return numResetPeriod;
    }

    public void setNumResetPeriod(Long numResetPeriod) {
        this.numResetPeriod = numResetPeriod;
    }

    /**
     * @return 序列号段上一次重置的日期
     */
    public String getNumResetLastdate() {
        return numResetLastdate;
    }

    public void setNumResetLastdate(String numResetLastdate) {
        this.numResetLastdate = numResetLastdate;
    }

    /**
     * @return 日期格式
     */
    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return 时间格式
     */
    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    /**
     * @return 标准对象编码
     */
    public String getCallStandardObject() {
        return callStandardObject;
    }

    public void setCallStandardObject(String callStandardObject) {
        this.callStandardObject = callStandardObject;
    }

    /**
     * @return 传入值长度
     */
    public Long getIncomeValueLength() {
        return incomeValueLength;
    }

    public void setIncomeValueLength(Long incomeValueLength) {
        this.incomeValueLength = incomeValueLength;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getIncomeValueLengthLimit() {
        return incomeValueLengthLimit;
    }

    public void setIncomeValueLengthLimit(Long incomeValueLengthLimit) {
        this.incomeValueLengthLimit = incomeValueLengthLimit;
    }
}
