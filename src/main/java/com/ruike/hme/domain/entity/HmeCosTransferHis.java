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

/**
 * 芯片转移记录表
 *
 * @author sanfeng.zhang@hand-china.com 2020-11-18 21:06:06
 */
@ApiModel("芯片转移记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_cos_transfer_his")
public class HmeCosTransferHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TRANSFER_HIS_ID = "transferHisId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_LOAD_SEQUENCE = "loadSequence";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_LOAD_ROW = "loadRow";
    public static final String FIELD_LOAD_COLUMN = "loadColumn";
    public static final String FIELD_SOURCE_MATERIAL_LOT_ID = "sourceMaterialLotId";
    public static final String FIELD_SOURCE_LOAD_ROW = "sourceLoadRow";
    public static final String FIELD_SOURCE_LOAD_COLUMN = "sourceLoadColumn";
    public static final String FIELD_COS_NUM = "cosNum";
    public static final String FIELD_LOT = "lot";
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


    @ApiModelProperty(value = "租户id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键id")
	@Id
    private String transferHisId;
    @ApiModelProperty(value = "站点",required = true)
    @NotBlank
    private String siteId;
   	@ApiModelProperty(value = "物料")
    private String materialId;
   	@ApiModelProperty(value = "货位")
    private String locatorId;
   	@ApiModelProperty(value = "序列号")
    private String loadSequence;
   	@ApiModelProperty(value = "条码id")
    private String materialLotId;
   	@ApiModelProperty(value = "行")
    private Long loadRow;
   	@ApiModelProperty(value = "列")
    private Long loadColumn;
   	@ApiModelProperty(value = "来源条码id")
    private String sourceMaterialLotId;
   	@ApiModelProperty(value = "来源行")
    private Long sourceLoadRow;
   	@ApiModelProperty(value = "来源列")
    private Long sourceLoadColumn;
   	@ApiModelProperty(value = "芯片数")
    private Long cosNum;
   	@ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "CID",required = true)
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
     * @return 租户id
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 主键id
     */
	public String getTransferHisId() {
		return transferHisId;
	}

	public void setTransferHisId(String transferHisId) {
		this.transferHisId = transferHisId;
	}
    /**
     * @return 站点
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 物料
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    /**
     * @return 货位
     */
	public String getLocatorId() {
		return locatorId;
	}

	public void setLocatorId(String locatorId) {
		this.locatorId = locatorId;
	}
    /**
     * @return 序列号
     */
	public String getLoadSequence() {
		return loadSequence;
	}

	public void setLoadSequence(String loadSequence) {
		this.loadSequence = loadSequence;
	}
    /**
     * @return 条码id
     */
	public String getMaterialLotId() {
		return materialLotId;
	}

	public void setMaterialLotId(String materialLotId) {
		this.materialLotId = materialLotId;
	}
    /**
     * @return 行
     */
	public Long getLoadRow() {
		return loadRow;
	}

	public void setLoadRow(Long loadRow) {
		this.loadRow = loadRow;
	}
    /**
     * @return 列
     */
	public Long getLoadColumn() {
		return loadColumn;
	}

	public void setLoadColumn(Long loadColumn) {
		this.loadColumn = loadColumn;
	}
    /**
     * @return 来源条码id
     */
	public String getSourceMaterialLotId() {
		return sourceMaterialLotId;
	}

	public void setSourceMaterialLotId(String sourceMaterialLotId) {
		this.sourceMaterialLotId = sourceMaterialLotId;
	}
    /**
     * @return 来源行
     */
	public Long getSourceLoadRow() {
		return sourceLoadRow;
	}

	public void setSourceLoadRow(Long sourceLoadRow) {
		this.sourceLoadRow = sourceLoadRow;
	}
    /**
     * @return 来源列
     */
	public Long getSourceLoadColumn() {
		return sourceLoadColumn;
	}

	public void setSourceLoadColumn(Long sourceLoadColumn) {
		this.sourceLoadColumn = sourceLoadColumn;
	}
    /**
     * @return 芯片数
     */
	public Long getCosNum() {
		return cosNum;
	}

	public void setCosNum(Long cosNum) {
		this.cosNum = cosNum;
	}
    /**
     * @return 批次
     */
	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
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
