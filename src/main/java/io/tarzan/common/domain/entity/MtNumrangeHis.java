package io.tarzan.common.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 号码段定义历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@ApiModel("号码段定义历史表")
@ModifyAudit

@Table(name = "mt_numrange_his")
@CustomPrimary
public class MtNumrangeHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NUMRANGE_HIS_ID = "numrangeHisId";
    public static final String FIELD_NUMRANGE_ID = "numrangeId";
    public static final String FIELD_OBJECT_ID = "objectId";
    public static final String FIELD_NUMRANGE_GROUP = "numrangeGroup";
    public static final String FIELD_NUM_DESCRIPTION = "numDescription";
    public static final String FIELD_INPUT_BOX1 = "inputBox1";
    public static final String FIELD_INPUT_BOX2 = "inputBox2";
    public static final String FIELD_INPUT_BOX3 = "inputBox3";
    public static final String FIELD_INPUT_BOX4 = "inputBox4";
    public static final String FIELD_INPUT_BOX5 = "inputBox5";
    public static final String FIELD_NUM_EXAMPLE = "numExample";
    public static final String FIELD_OUTSIDE_NUM_FLAG = "outsideNumFlag";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";

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
    @ApiModelProperty("号码段定义历史表主键")
    @Id
    @Where
    private String numrangeHisId;
    @ApiModelProperty(value = "号码段定义表主键", required = true)
    @NotBlank
    @Where
    private String numrangeId;
    @ApiModelProperty(value = "号段组合规则选项", required = true)
    @NotBlank
    @Where
    private String objectId;
    @ApiModelProperty(value = "号段组号", required = true)
    @NotBlank
    @Where
    private String numrangeGroup;
    @ApiModelProperty(value = "序列号段下限", required = true)
    @NotBlank
    @Where
    private String numDescription;
    @ApiModelProperty(value = "号段组合规则输入框1", required = true)
    @NotBlank
    @Where
    private String inputBox1;
    @ApiModelProperty(value = "号段组合规则输入框2")
    @Where
    private String inputBox2;
    @ApiModelProperty(value = "号段组合规则输入框3")
    @Where
    private String inputBox3;
    @ApiModelProperty(value = "号段组合规则输入框4")
    @Where
    private String inputBox4;
    @ApiModelProperty(value = "号段组合规则输入框5")
    @Where
    private String inputBox5;
    @ApiModelProperty(value = "号段示例")
    @Where
    private String numExample;
    @ApiModelProperty(value = "外部输入编码标识")
    @Where
    private String outsideNumFlag;
    @ApiModelProperty(value = "是否有效")
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
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
     * @return 号段组合规则选项
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
     * @return 序列号段下限
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
