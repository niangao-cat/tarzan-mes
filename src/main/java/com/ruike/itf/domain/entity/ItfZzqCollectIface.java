package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 准直器耦合接口表
 *
 * @author wenzhang.yu@hand-china 2020-12-16 13:43:44
 */
@ApiModel("准直器耦合接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_zzq_collect_iface")
public class ItfZzqCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_ZZQ_CENTER_X = "zzqCenterX";
    public static final String FIELD_ZZQ_CENTER_Y = "zzqCenterY";
    public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ZZQ_ATTRIBUTE1 = "zzqAttribute1";
    public static final String FIELD_ZZQ_ATTRIBUTE2 = "zzqAttribute2";
    public static final String FIELD_ZZQ_ATTRIBUTE3 = "zzqAttribute3";
    public static final String FIELD_ZZQ_ATTRIBUTE4 = "zzqAttribute4";
    public static final String FIELD_ZZQ_ATTRIBUTE5 = "zzqAttribute5";
    public static final String FIELD_ZZQ_ATTRIBUTE6 = "zzqAttribute6";
    public static final String FIELD_ZZQ_ATTRIBUTE7 = "zzqAttribute7";
    public static final String FIELD_ZZQ_ATTRIBUTE8 = "zzqAttribute8";
    public static final String FIELD_ZZQ_ATTRIBUTE9 = "zzqAttribute9";
    public static final String FIELD_ZZQ_ATTRIBUTE10 = "zzqAttribute10";
    public static final String FIELD_ZZQ_ATTRIBUTE11 = "zzqAttribute11";
    public static final String FIELD_ZZQ_ATTRIBUTE12 = "zzqAttribute12";
    public static final String FIELD_ZZQ_ATTRIBUTE13 = "zzqAttribute13";
    public static final String FIELD_ZZQ_ATTRIBUTE14 = "zzqAttribute14";
    public static final String FIELD_ZZQ_ATTRIBUTE15 = "zzqAttribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口表ID，主键")
    @Id
    @GeneratedValue
    private String interfaceId;
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "产品SN")
    private String sn;
    @ApiModelProperty(value = "质心X")
    private BigDecimal zzqCenterX;
    @ApiModelProperty(value = "质心Y")
    private BigDecimal zzqCenterY;
    @ApiModelProperty(value = "外围系统唯一标识")
    private String primaryKey;
    @ApiModelProperty(value = "处理时间", required = true)
    @NotNull
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "")
    private String zzqAttribute1;
    @ApiModelProperty(value = "")
    private String zzqAttribute2;
    @ApiModelProperty(value = "")
    private String zzqAttribute3;
    @ApiModelProperty(value = "")
    private String zzqAttribute4;
    @ApiModelProperty(value = "")
    private String zzqAttribute5;
    @ApiModelProperty(value = "")
    private String zzqAttribute6;
    @ApiModelProperty(value = "")
    private String zzqAttribute7;
    @ApiModelProperty(value = "")
    private String zzqAttribute8;
    @ApiModelProperty(value = "")
    private String zzqAttribute9;
    @ApiModelProperty(value = "")
    private String zzqAttribute10;
    @ApiModelProperty(value = "")
    private String zzqAttribute11;
    @ApiModelProperty(value = "")
    private String zzqAttribute12;
    @ApiModelProperty(value = "")
    private String zzqAttribute13;
    @ApiModelProperty(value = "")
    private String zzqAttribute14;
    @ApiModelProperty(value = "")
    private String zzqAttribute15;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 接口表ID，主键
     */
    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    /**
     * @return 设备资产编码
     */
    public String getAssetEncoding() {
        return assetEncoding;
    }

    public void setAssetEncoding(String assetEncoding) {
        this.assetEncoding = assetEncoding;
    }

    /**
     * @return 产品SN
     */
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * @return 质心X
     */
    public BigDecimal getZzqCenterX() {
        return zzqCenterX;
    }

    public void setZzqCenterX(BigDecimal zzqCenterX) {
        this.zzqCenterX = zzqCenterX;
    }

    /**
     * @return 质心Y
     */
    public BigDecimal getZzqCenterY() {
        return zzqCenterY;
    }

    public void setZzqCenterY(BigDecimal zzqCenterY) {
        this.zzqCenterY = zzqCenterY;
    }

    /**
     * @return 外围系统唯一标识
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return 处理时间
     */
    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    /**
     * @return 处理消息
     */
    public String getProcessMessage() {
        return processMessage;
    }

    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
    }

    /**
     * @return 处理状态(N / P / E / S : 正常 / 处理中 / 错误 / 成功)
     */
    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

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
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }


    public String getZzqAttribute1() {
        return zzqAttribute1;
    }

    public void setZzqAttribute1(String zzqAttribute1) {
        this.zzqAttribute1 = zzqAttribute1;
    }

    public String getZzqAttribute2() {
        return zzqAttribute2;
    }

    public void setZzqAttribute2(String zzqAttribute2) {
        this.zzqAttribute2 = zzqAttribute2;
    }

    public String getZzqAttribute3() {
        return zzqAttribute3;
    }

    public void setZzqAttribute3(String zzqAttribute3) {
        this.zzqAttribute3 = zzqAttribute3;
    }

    public String getZzqAttribute4() {
        return zzqAttribute4;
    }

    public void setZzqAttribute4(String zzqAttribute4) {
        this.zzqAttribute4 = zzqAttribute4;
    }

    public String getZzqAttribute5() {
        return zzqAttribute5;
    }

    public void setZzqAttribute5(String zzqAttribute5) {
        this.zzqAttribute5 = zzqAttribute5;
    }

    public String getZzqAttribute6() {
        return zzqAttribute6;
    }

    public void setZzqAttribute6(String zzqAttribute6) {
        this.zzqAttribute6 = zzqAttribute6;
    }

    public String getZzqAttribute7() {
        return zzqAttribute7;
    }

    public void setZzqAttribute7(String zzqAttribute7) {
        this.zzqAttribute7 = zzqAttribute7;
    }

    public String getZzqAttribute8() {
        return zzqAttribute8;
    }

    public void setZzqAttribute8(String zzqAttribute8) {
        this.zzqAttribute8 = zzqAttribute8;
    }

    public String getZzqAttribute9() {
        return zzqAttribute9;
    }

    public void setZzqAttribute9(String zzqAttribute9) {
        this.zzqAttribute9 = zzqAttribute9;
    }

    public String getZzqAttribute10() {
        return zzqAttribute10;
    }

    public void setZzqAttribute10(String zzqAttribute10) {
        this.zzqAttribute10 = zzqAttribute10;
    }


    public String getZzqAttribute11() {
        return zzqAttribute11;
    }

    public void setZzqAttribute11(String zzqAttribute11) {
        this.zzqAttribute11 = zzqAttribute11;
    }

    public String getZzqAttribute12() {
        return zzqAttribute12;
    }

    public void setZzqAttribute12(String zzqAttribute12) {
        this.zzqAttribute12 = zzqAttribute12;
    }

    public String getZzqAttribute13() {
        return zzqAttribute13;
    }

    public void setZzqAttribute13(String zzqAttribute13) {
        this.zzqAttribute13 = zzqAttribute13;
    }

    public String getZzqAttribute14() {
        return zzqAttribute14;
    }

    public void setZzqAttribute14(String zzqAttribute14) {
        this.zzqAttribute14 = zzqAttribute14;
    }

    public String getZzqAttribute15() {
        return zzqAttribute15;
    }

    public void setZzqAttribute15(String zzqAttribute15) {
        this.zzqAttribute15 = zzqAttribute15;
    }


}
