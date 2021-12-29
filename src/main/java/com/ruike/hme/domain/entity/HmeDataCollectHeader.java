package com.ruike.hme.domain.entity;

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
 * 生产数据采集头表
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:56:19
 */
@ApiModel("生产数据采集头表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_data_collect_header")
public class HmeDataCollectHeader extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COLLECT_HEADER_ID = "collectHeaderId";
    public static final String FIELD_SHIFT_ID = "shiftId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_DATA_RECORD_CODE = "dataRecordCode";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_SITE_IN_BY = "siteInBy";
    public static final String FIELD_SITE_OUT_BY = "siteOutBy";
    public static final String FIELD_SITE_IN_DATE = "siteInDate";
    public static final String FIELD_SITE_OUT_DATE = "siteOutDate";
    public static final String FIELD_DATA_COLLECT_TYPE = "dataCollectType";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("生产数据采集头表主键")
    @Id
    private String collectHeaderId;
    @ApiModelProperty(value = "班次ID")
    private String shiftId;
    @ApiModelProperty(value = "采集物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "采集物料数量", required = true)
    @NotNull
    private BigDecimal qty;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "采集条码号", required = true)
    @NotBlank
    private String dataRecordCode;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "进站人ID", required = true)
    @NotNull
    private Long siteInBy;
    @ApiModelProperty(value = "出站人ID")
    private Long siteOutBy;
    @ApiModelProperty(value = "进站时间", required = true)
    @NotNull
    private Date siteInDate;
    @ApiModelProperty(value = "出站时间")
    private Date siteOutDate;
    @ApiModelProperty(value = "数据采集类型")
    private String dataCollectType;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "CID", required = true)
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
     * @return 生产数据采集头表主键
     */
    public String getCollectHeaderId() {
        return collectHeaderId;
    }

    public void setCollectHeaderId(String collectHeaderId) {
        this.collectHeaderId = collectHeaderId;
    }

    /**
     * @return 班次ID
     */
    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return 采集物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 采集物料数量
     */
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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
     * @return 采集条码号
     */
    public String getDataRecordCode() {
        return dataRecordCode;
    }

    public void setDataRecordCode(String dataRecordCode) {
        this.dataRecordCode = dataRecordCode;
    }

    /**
     * @return 工艺ID
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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
     * @return 进站人ID
     */
    public Long getSiteInBy() {
        return siteInBy;
    }

    public void setSiteInBy(Long siteInBy) {
        this.siteInBy = siteInBy;
    }

    /**
     * @return 出站人ID
     */
    public Long getSiteOutBy() {
        return siteOutBy;
    }

    public void setSiteOutBy(Long siteOutBy) {
        this.siteOutBy = siteOutBy;
    }

    /**
     * @return 进站时间
     */
    public Date getSiteInDate() {
        return siteInDate;
    }

    public void setSiteInDate(Date siteInDate) {
        this.siteInDate = siteInDate;
    }

    /**
     * @return 出站时间
     */
    public Date getSiteOutDate() {
        return siteOutDate;
    }

    public void setSiteOutDate(Date siteOutDate) {
        this.siteOutDate = siteOutDate;
    }

    /**
     * @return 数据采集类型
     */
    public String getDataCollectType() {
        return dataCollectType;
    }

    public void setDataCollectType(String dataCollectType) {
        this.dataCollectType = dataCollectType;
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

}
