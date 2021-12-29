package com.ruike.qms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 巡检单头历史表
 *
 * @author chaonan.hu@hand-china.com 2020-08-17 11:49:25
 */
@ApiModel("巡检单头历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_pqc_header_his")
public class QmsPqcHeaderHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_PQC_HEADER_HIS_ID = "pqcHeaderHisId";
    public static final String FIELD_PQC_HEADER_ID = "pqcHeaderId";
    public static final String FIELD_PQC_NUMBER = "pqcNumber";
    public static final String FIELD_WO_ID = "woId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_QC_BY = "qcBy";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_INSPECTION_STATUS = "inspectionStatus";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_INSPECTION_START_DATE = "inspectionStartDate";
    public static final String FIELD_INSPECTION_FINISH_DATE = "inspectionFinishDate";
    public static final String FIELD_INSPECTION_TIME = "inspectionTime";
    public static final String FIELD_INSPECTION_RESULT = "inspectionResult";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty("检验单头历史表主键")
    @Id
    private String pqcHeaderHisId;
    @ApiModelProperty(value = "检验单头表主键", required = true)
    @NotBlank
    private String pqcHeaderId;
    @ApiModelProperty(value = "检验单号", required = true)
    @NotBlank
    private String pqcNumber;
    @ApiModelProperty(value = "工单", required = true)
    @NotBlank
    private String woId;
    @ApiModelProperty(value = "EO_ID")
    private String eoId;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "检验员", required = true)
    @NotNull
    private Long qcBy;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "检验状态", required = true)
    @NotBlank
    private String inspectionStatus;
    @ApiModelProperty(value = "产线ID", required = true)
    @NotBlank
    private String prodLineId;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "建单日期", required = true)
    @NotNull
    private Date createdDate;
    @ApiModelProperty(value = "检验开始时间")
    private Date inspectionStartDate;
    @ApiModelProperty(value = "完成时间")
    private Date inspectionFinishDate;
    @ApiModelProperty(value = "检验时长（单位：小时）")
    private BigDecimal inspectionTime;
    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    private String eventId;
    @ApiModelProperty(value = "cid", required = true)
    @Cid
    private Long cid;
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
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 检验单头历史表主键
     */
    public String getPqcHeaderHisId() {
        return pqcHeaderHisId;
    }

    public void setPqcHeaderHisId(String pqcHeaderHisId) {
        this.pqcHeaderHisId = pqcHeaderHisId;
    }

    /**
     * @return 检验单头表主键
     */
    public String getPqcHeaderId() {
        return pqcHeaderId;
    }

    public void setPqcHeaderId(String pqcHeaderId) {
        this.pqcHeaderId = pqcHeaderId;
    }

    /**
     * @return 检验单号
     */
    public String getPqcNumber() {
        return pqcNumber;
    }

    public void setPqcNumber(String pqcNumber) {
        this.pqcNumber = pqcNumber;
    }

    /**
     * @return 工单
     */
    public String getWoId() {
        return woId;
    }

    public void setWoId(String woId) {
        this.woId = woId;
    }

    /**
     * @return EO_ID
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 物料批ID
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 检验员
     */
    public Long getQcBy() {
        return qcBy;
    }

    public void setQcBy(Long qcBy) {
        this.qcBy = qcBy;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 物料版本
     */
    public String getMaterialVersion() {
        return materialVersion;
    }

    public void setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
    }

    /**
     * @return 检验状态
     */
    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    /**
     * @return 产线ID
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    /**
     * @return 工位ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 班次
     */
    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    /**
     * @return 建单日期
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return 检验开始时间
     */
    public Date getInspectionStartDate() {
        return inspectionStartDate;
    }

    public void setInspectionStartDate(Date inspectionStartDate) {
        this.inspectionStartDate = inspectionStartDate;
    }

    /**
     * @return 完成时间
     */
    public Date getInspectionFinishDate() {
        return inspectionFinishDate;
    }

    public void setInspectionFinishDate(Date inspectionFinishDate) {
        this.inspectionFinishDate = inspectionFinishDate;
    }

    /**
     * @return 检验时长（单位：小时）
     */
    public BigDecimal getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(BigDecimal inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    /**
     * @return 检验结果
     */
    public String getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

}
