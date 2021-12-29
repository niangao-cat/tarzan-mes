package com.ruike.wms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 单据SN指定表
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:07:45
 */
@ApiModel("单据SN指定表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_instruction_sn_rel")
public class WmsInstructionSnRel extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WMS_INSTRUCTION_SN_REL_ID = "wmsInstructionSnRelId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_INSTRUCTION_DOC_ID = "instructionDocId";
    public static final String FIELD_INSTRUCTION_ID = "instructionId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
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


    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID")
    @Id
    @GeneratedValue
    private String wmsInstructionSnRelId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "单据ID", required = true)
    @NotBlank
    private String instructionDocId;
    @ApiModelProperty(value = "指令ID", required = true)
    @NotBlank
    private String instructionId;
    @ApiModelProperty(value = "物料批ID", required = true)
    @NotBlank
    private String materialLotId;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
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

    public WmsInstructionSnRel setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 主键ID
     */
    public String getWmsInstructionSnRelId() {
        return wmsInstructionSnRelId;
    }

    public WmsInstructionSnRel setWmsInstructionSnRelId(String wmsInstructionSnRelId) {
        this.wmsInstructionSnRelId = wmsInstructionSnRelId;
        return this;
    }

    /**
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public WmsInstructionSnRel setSiteId(String siteId) {
        this.siteId = siteId;
        return this;
    }

    /**
     * @return 单据ID
     */
    public String getInstructionDocId() {
        return instructionDocId;
    }

    public WmsInstructionSnRel setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
        return this;
    }

    /**
     * @return 指令ID
     */
    public String getInstructionId() {
        return instructionId;
    }

    public WmsInstructionSnRel setInstructionId(String instructionId) {
        this.instructionId = instructionId;
        return this;
    }

    /**
     * @return 物料批ID
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public WmsInstructionSnRel setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public WmsInstructionSnRel setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public WmsInstructionSnRel setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public WmsInstructionSnRel setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public WmsInstructionSnRel setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public WmsInstructionSnRel setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public WmsInstructionSnRel setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public WmsInstructionSnRel setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public WmsInstructionSnRel setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public WmsInstructionSnRel setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public WmsInstructionSnRel setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public WmsInstructionSnRel setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public WmsInstructionSnRel setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public WmsInstructionSnRel setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public WmsInstructionSnRel setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public WmsInstructionSnRel setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public WmsInstructionSnRel setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public WmsInstructionSnRel setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
