package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 在制盘点单历史
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@ApiModel("在制盘点单历史")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_wip_stocktake_doc_his")
public class HmeWipStocktakeDocHis extends AuditDomain {

    public static final String FIELD_STOCKTAKE_HIS_ID = "stocktakeHisId";
    public static final String FIELD_STOCKTAKE_ID = "stocktakeId";
    public static final String FIELD_STOCKTAKE_NUM = "stocktakeNum";
    public static final String FIELD_STOCKTAKE_STATUS = "stocktakeStatus";
    public static final String FIELD_STOCKTAKE_LAST_STATUS = "stocktakeLastStatus";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_AREA_ID = "areaId";
    public static final String FIELD_OPEN_FLAG = "openFlag";
    public static final String FIELD_MATERIAL_RANGE_FLAG = "materialRangeFlag";
    public static final String FIELD_ADJUST_TIMELY_FLAG = "adjustTimelyFlag";
    public static final String FIELD_MATERIAL_LOT_LOCK_FLAG = "materialLotLockFlag";
    public static final String FIELD_IDENTIFICATION = "identification";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_TENANT_ID = "tenantId";
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


    @ApiModelProperty("主键")
    @Id
//    @GeneratedValue
    private String stocktakeHisId;
    @ApiModelProperty(value = "盘点单ID", required = true)
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty(value = "在制盘点单据编号", required = true)
    @NotBlank
    private String stocktakeNum;
    @ApiModelProperty(value = "状态", required = true)
    @NotBlank
    private String stocktakeStatus;
    @ApiModelProperty(value = "上一状态，用于状态发生变更时找到上一状态的结果")
    private String stocktakeLastStatus;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "部门的区域ID，用于用户查看盘点当前区域", required = true)
    @NotBlank
    private String areaId;
    @ApiModelProperty(value = "是否明盘，Y为明盘，N为盲盘", required = true)
    @NotBlank
    private String openFlag;
    @ApiModelProperty(value = "是否按物料盘点，Y/N，不能为空", required = true)
    @NotBlank
    private String materialRangeFlag;
    @ApiModelProperty(value = "是否允许实时调整，Y/N，不能为空", required = true)
    @NotBlank
    private String adjustTimelyFlag;
    @ApiModelProperty(value = "物料批停用标识，Y/N，不能为空", required = true)
    @NotBlank
    private String materialLotLockFlag;
    @ApiModelProperty(value = "单据条码", required = true)
    @NotBlank
    private String identification;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotNull
    private String eventId;
    @ApiModelProperty(value = "CID", required = true)
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
     * @return 主键
     */
    public String getStocktakeHisId() {
        return stocktakeHisId;
    }

    public void setStocktakeHisId(String stocktakeHisId) {
        this.stocktakeHisId = stocktakeHisId;
    }

    /**
     * @return 盘点单ID
     */
    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    /**
     * @return 在制盘点单据编号
     */
    public String getStocktakeNum() {
        return stocktakeNum;
    }

    public void setStocktakeNum(String stocktakeNum) {
        this.stocktakeNum = stocktakeNum;
    }

    /**
     * @return 状态
     */
    public String getStocktakeStatus() {
        return stocktakeStatus;
    }

    public void setStocktakeStatus(String stocktakeStatus) {
        this.stocktakeStatus = stocktakeStatus;
    }

    /**
     * @return 上一状态，用于状态发生变更时找到上一状态的结果
     */
    public String getStocktakeLastStatus() {
        return stocktakeLastStatus;
    }

    public void setStocktakeLastStatus(String stocktakeLastStatus) {
        this.stocktakeLastStatus = stocktakeLastStatus;
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
     * @return 部门的区域ID，用于用户查看盘点当前区域
     */
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return 是否明盘，Y为明盘，N为盲盘
     */
    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    /**
     * @return 是否按物料盘点，Y/N，不能为空
     */
    public String getMaterialRangeFlag() {
        return materialRangeFlag;
    }

    public void setMaterialRangeFlag(String materialRangeFlag) {
        this.materialRangeFlag = materialRangeFlag;
    }

    /**
     * @return 是否允许实时调整，Y/N，不能为空
     */
    public String getAdjustTimelyFlag() {
        return adjustTimelyFlag;
    }

    public void setAdjustTimelyFlag(String adjustTimelyFlag) {
        this.adjustTimelyFlag = adjustTimelyFlag;
    }

    /**
     * @return 物料批停用标识，Y/N，不能为空
     */
    public String getMaterialLotLockFlag() {
        return materialLotLockFlag;
    }

    public void setMaterialLotLockFlag(String materialLotLockFlag) {
        this.materialLotLockFlag = materialLotLockFlag;
    }

    /**
     * @return 单据条码
     */
    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
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
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
