package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import lombok.Data;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 设备管理任务单行历史表
 *
 * @author sanfeng.zhang@china-hand.com 2021-03-04 15:46:14
 */
@ApiModel("设备管理任务单行历史表")
@VersionAudit
@ModifyAudit
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_eq_manage_task_doc_line_his")
@CustomPrimary
public class HmeEqManageTaskDocLineHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TASK_DOC_HISTORY_ID = "taskDocHisId";
    public static final String FIELD_TASK_DOC_ID = "taskDocId";
    public static final String FIELD_TASK_DOC_LINE_ID = "taskDocLineId";
    public static final String FIELD_MANAGE_TAG_ID = "manageTagId";
    public static final String FIELD_CHECK_VALUE = "checkValue";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_CHECK_DATE = "checkDate";
    public static final String FIELD_WKC_ID = "wkcId";
    public static final String FIELD_CHECK_BY = "checkBy";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

    //
// 数据库字段
// ------------------------------------------------------------------------------
    @ApiModelProperty(value = "租户id")
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "主键ID，标识唯一一条记录")
    @Id
    private String taskDocHisId;
    @ApiModelProperty(value = "单据头id")
    @NotNull
    private String taskDocId;
    @ApiModelProperty(value = "单据行id")
    @NotNull
    private String taskDocLineId;
    @ApiModelProperty(value = "项目id")
    @NotNull
    private String manageTagId;
    @ApiModelProperty(value = "检验值")
    @NotNull
    private String checkValue;
    @ApiModelProperty(value = "结果")
    @NotNull
    private String result;
    @ApiModelProperty(value = "检验日期")
    private Date checkDate;
    @ApiModelProperty(value = "点检工位")
    private String wkcId;
    @ApiModelProperty(value = "检验人")
    private Long checkBy;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @NotNull
    @Cid
    private Long cid;
    private String attributeCategory;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;

//
// 非数据库字段
// ------------------------------------------------------------------------------

//
// getter/setter
// ------------------------------------------------------------------------------


    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 主键
     */
    public String getTaskDocHisId() {
        return taskDocHisId;
    }

    public void setTaskDocHisId(String taskDocHisId) {
        this.taskDocHisId = taskDocHisId;
    }

    /**
     * @return 单据头id
     */
    public String getTaskDocId() {
        return taskDocId;
    }

    public void setTaskDocId(String taskDocId) {
        this.taskDocId = taskDocId;
    }

    /**
     * @return 单据行id
     */
    public String getTaskDocLineId() {
        return taskDocLineId;
    }

    public void setTaskDocLineId(String taskDocLineId) {
        this.taskDocLineId = taskDocLineId;
    }

    /**
     * @return 项目id
     */
    public String getManageTagId() {
        return manageTagId;
    }

    public void setManageTagId(String manageTagId) {
        this.manageTagId = manageTagId;
    }

    /**
     * @return 检验值
     */
    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    /**
     * @return 结果
     */
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return 检验日期
     */
    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    /**
     * @return 点检工位
     */
    public String getWkcId() {
        return wkcId;
    }

    public void setWkcId(String wkcId) {
        this.wkcId = wkcId;
    }

    /**
     * @return 检验人
     */
    public Long getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(Long checkBy) {
        this.checkBy = checkBy;
    }

    /**
     * @return 事件id
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }
}
