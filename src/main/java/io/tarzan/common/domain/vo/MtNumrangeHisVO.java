package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-china.com
 * @ClassName MtNumrangeHisVO
 * @createTime 2019年08月19日 11:35:00
 */
public class MtNumrangeHisVO extends AuditDomain implements Serializable {

    private static final long serialVersionUID = -3497904565559456514L;

    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    @ApiModelProperty("号码段定义历史表主键")
    private String numrangeHisId;
    @ApiModelProperty(value = "号码段定义表主键", required = true)
    private String numrangeId;
    @ApiModelProperty(value = "编码对象id", required = true)
    private String objectId;
    @ApiModelProperty(value = "号段组号", required = true)
    private String numrangeGroup;
    @ApiModelProperty(value = "号段组描述", required = true)
    private String numDescription;
    @ApiModelProperty(value = "号段组合规则输入框1", required = true)
    private String inputBox1;
    @ApiModelProperty(value = "号段组合规则输入框2")
    private String inputBox2;
    @ApiModelProperty(value = "号段组合规则输入框3")
    private String inputBox3;
    @ApiModelProperty(value = "号段组合规则输入框4")
    private String inputBox4;
    @ApiModelProperty(value = "号段组合规则输入框5")
    private String inputBox5;
    @ApiModelProperty(value = "号段示例")
    private String numExample;
    @ApiModelProperty(value = "外部输入编码标识")
    private String outsideNumFlag;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
    @ApiModelProperty(value = "cid")
    private Long cid;
    @ApiModelProperty(value = "事件ID", required = true)
    private String eventId;
    @ApiModelProperty(value = "事件记录创建人id")
    private Long eventBy;
    @ApiModelProperty(value = "事件记录创建人")
    private String eventByName;
    @ApiModelProperty(value = "事件记录创建时间")
    private Date eventTime;
    @ApiModelProperty(value = "编码对象")
    private String objectCode;
    @ApiModelProperty(value = "编码对象描述")
    private String objectName;
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 编码对象
     */
    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    /**
     * @return 编码对象描述
     */
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * @return 事件记录创建人id
     */
    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    /**
     * @return 事件记录创建人
     */
    public String getEventByName() {
        return eventByName;
    }

    public void setEventByName(String eventByName) {
        this.eventByName = eventByName;
    }

    /**
     * @return 事件记录创建时间
     */
    public Date getEventTime() {
        if (eventTime == null) {
            return null;
        } else {
            return (Date) eventTime.clone();
        }
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }
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
     * @return 号码段定义历史表主键
     */
    public String getNumrangeHisId() {
        return numrangeHisId;
    }

    public void setNumrangeHisId(String numrangeHisId) {
        this.numrangeHisId = numrangeHisId;
    }

    /**
     * @return 号码段定义表主键
     */
    public String getNumrangeId() {
        return numrangeId;
    }

    public void setNumrangeId(String numrangeId) {
        this.numrangeId = numrangeId;
    }

    /**
     * @return 编码对象id
     */
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return 号段组号
     */
    public String getNumrangeGroup() {
        return numrangeGroup;
    }

    public void setNumrangeGroup(String numrangeGroup) {
        this.numrangeGroup = numrangeGroup;
    }

    /**
     * @return 号段组描述
     */
    public String getNumDescription() {
        return numDescription;
    }

    public void setNumDescription(String numDescription) {
        this.numDescription = numDescription;
    }

    /**
     * @return 号段组合规则输入框1
     */
    public String getInputBox1() {
        return inputBox1;
    }

    public void setInputBox1(String inputBox1) {
        this.inputBox1 = inputBox1;
    }

    /**
     * @return 号段组合规则输入框2
     */
    public String getInputBox2() {
        return inputBox2;
    }

    public void setInputBox2(String inputBox2) {
        this.inputBox2 = inputBox2;
    }

    /**
     * @return 号段组合规则输入框3
     */
    public String getInputBox3() {
        return inputBox3;
    }

    public void setInputBox3(String inputBox3) {
        this.inputBox3 = inputBox3;
    }

    /**
     * @return 号段组合规则输入框4
     */
    public String getInputBox4() {
        return inputBox4;
    }

    public void setInputBox4(String inputBox4) {
        this.inputBox4 = inputBox4;
    }

    /**
     * @return 号段组合规则输入框5
     */
    public String getInputBox5() {
        return inputBox5;
    }

    public void setInputBox5(String inputBox5) {
        this.inputBox5 = inputBox5;
    }

    /**
     * @return 号段示例
     */
    public String getNumExample() {
        return numExample;
    }

    public void setNumExample(String numExample) {
        this.numExample = numExample;
    }

    /**
     * @return 外部输入编码标识
     */
    public String getOutsideNumFlag() {
        return outsideNumFlag;
    }

    public void setOutsideNumFlag(String outsideNumFlag) {
        this.outsideNumFlag = outsideNumFlag;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return cid
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
