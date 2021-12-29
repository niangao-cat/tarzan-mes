package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * COS良率监控接口表
 *
 * @author wengang.qiang@hand-china.com 2021-09-30 14:14:20
 */
@ApiModel("COS良率监控接口表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_cos_monitor_iface")
public class ItfCosMonitorIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COS_MONITOR_IFACE_ID = "cosMonitorIfaceId";
    public static final String FIELD_MONITOR_DOC_NUM = "monitorDocNum";
    public static final String FIELD_RELEASE_TYPE = "releaseType";
    public static final String FIELD_RELEASE_LOT = "releaseLot";
    public static final String FIELD_DOC_STATUS = "docStatus";
    public static final String FIELD_CHECK_STATUS = "checkStatus";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_WAFER = "wafer";
    public static final String FIELD_TEST_QTY = "testQty";
    public static final String FIELD_TEST_PASS_RATE = "testPassRate";
    public static final String FIELD_PASS_DATE = "passDate";
    public static final String FIELD_PASS_BY = "passBy";
    public static final String FIELD_MATERIAL_LOT_CODE = "materialLotCode";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_MATERIAL_NAME = "materialName";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
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


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID")
    @Id
    private String cosMonitorIfaceId;
    @ApiModelProperty(value = "监控单据号", required = true)
    @NotBlank
    private String monitorDocNum;
    @ApiModelProperty(value = "放行类型", required = true)
    @NotBlank
    private String releaseType;
    @ApiModelProperty(value = "放行批次", required = true)
    @NotNull
    private Long releaseLot;
    @ApiModelProperty(value = "单据/盒子状态", required = true)
    @NotBlank
    private String docStatus;
    @ApiModelProperty(value = "审核状态", required = true)
    @NotBlank
    private String checkStatus;
    @ApiModelProperty(value = "cos_type", required = true)
    @NotBlank
    private String cosType;
    @ApiModelProperty(value = "wafer", required = true)
    @NotBlank
    private String wafer;
    @ApiModelProperty(value = "测试数量", required = true)
    @NotNull
    private Long testQty;
    @ApiModelProperty(value = "cos良率", required = true)
    @NotNull
    private BigDecimal testPassRate;
    @ApiModelProperty(value = "放行时间")
    private Date passDate;
    @ApiModelProperty(value = "放行人")
    private String passBy;
    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;
    @ApiModelProperty(value = "芯片物料编码")
    private String materialCode;
    @ApiModelProperty(value = "芯片物料描述")
    private String materialName;
    @ApiModelProperty(value = "处理时间")
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "cid", required = true)
    @NotNull
    @Cid
    private Long cid;
    @ApiModelProperty(value = "")
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
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public ItfCosMonitorIface setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 主键ID
     */
    public String getCosMonitorIfaceId() {
        return cosMonitorIfaceId;
    }

    public ItfCosMonitorIface setCosMonitorIfaceId(String cosMonitorIfaceId) {
        this.cosMonitorIfaceId = cosMonitorIfaceId;
        return this;
    }

    /**
     * @return 监控单据号
     */
    public String getMonitorDocNum() {
        return monitorDocNum;
    }

    public ItfCosMonitorIface setMonitorDocNum(String monitorDocNum) {
        this.monitorDocNum = monitorDocNum;
        return this;
    }

    /**
     * @return 放行类型
     */
    public String getReleaseType() {
        return releaseType;
    }

    public ItfCosMonitorIface setReleaseType(String releaseType) {
        this.releaseType = releaseType;
        return this;
    }

    /**
     * @return 放行批次
     */
    public Long getReleaseLot() {
        return releaseLot;
    }

    public ItfCosMonitorIface setReleaseLot(Long releaseLot) {
        this.releaseLot = releaseLot;
        return this;
    }

    /**
     * @return 单据/盒子状态
     */
    public String getDocStatus() {
        return docStatus;
    }

    public ItfCosMonitorIface setDocStatus(String docStatus) {
        this.docStatus = docStatus;
        return this;
    }

    /**
     * @return 审核状态
     */
    public String getCheckStatus() {
        return checkStatus;
    }

    public ItfCosMonitorIface setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
        return this;
    }

    /**
     * @return cos_type
     */
    public String getCosType() {
        return cosType;
    }

    public ItfCosMonitorIface setCosType(String cosType) {
        this.cosType = cosType;
        return this;
    }

    /**
     * @return wafer
     */
    public String getWafer() {
        return wafer;
    }

    public ItfCosMonitorIface setWafer(String wafer) {
        this.wafer = wafer;
        return this;
    }

    /**
     * @return 测试数量
     */
    public Long getTestQty() {
        return testQty;
    }

    public ItfCosMonitorIface setTestQty(Long testQty) {
        this.testQty = testQty;
        return this;
    }

    /**
     * @return cos良率
     */
    public BigDecimal getTestPassRate() {
        return testPassRate;
    }

    public ItfCosMonitorIface setTestPassRate(BigDecimal testPassRate) {
        this.testPassRate = testPassRate;
        return this;
    }

    /**
     * @return 放行时间
     */
    public Date getPassDate() {
        return passDate;
    }

    public ItfCosMonitorIface setPassDate(Date passDate) {
        this.passDate = passDate;
        return this;
    }

    /**
     * @return 放行人
     */
    public String getPassBy() {
        return passBy;
    }

    public ItfCosMonitorIface setPassBy(String passBy) {
        this.passBy = passBy;
        return this;
    }

    /**
     * @return 盒子号
     */
    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public ItfCosMonitorIface setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
        return this;
    }

    /**
     * @return 芯片物料编码
     */
    public String getMaterialCode() {
        return materialCode;
    }

    public ItfCosMonitorIface setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
        return this;
    }

    /**
     * @return 芯片物料描述
     */
    public String getMaterialName() {
        return materialName;
    }

    public ItfCosMonitorIface setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    /**
     * @return 处理时间
     */
    public Date getProcessDate() {
        return processDate;
    }

    public ItfCosMonitorIface setProcessDate(Date processDate) {
        this.processDate = processDate;
        return this;
    }

    /**
     * @return 处理消息
     */
    public String getProcessMessage() {
        return processMessage;
    }

    public ItfCosMonitorIface setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
        return this;
    }

    /**
     * @return 处理状态
     */
    public String getProcessStatus() {
        return processStatus;
    }

    public ItfCosMonitorIface setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
        return this;
    }

    /**
     * @return cid
     */
    public Long getCid() {
        return cid;
    }

    public ItfCosMonitorIface setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public ItfCosMonitorIface setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public ItfCosMonitorIface setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public ItfCosMonitorIface setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public ItfCosMonitorIface setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public ItfCosMonitorIface setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public ItfCosMonitorIface setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public ItfCosMonitorIface setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public ItfCosMonitorIface setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public ItfCosMonitorIface setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public ItfCosMonitorIface setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public ItfCosMonitorIface setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public ItfCosMonitorIface setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public ItfCosMonitorIface setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public ItfCosMonitorIface setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public ItfCosMonitorIface setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public ItfCosMonitorIface setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
