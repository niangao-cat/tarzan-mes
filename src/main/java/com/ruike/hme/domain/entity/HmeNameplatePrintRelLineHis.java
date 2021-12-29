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

/**
 * 铭牌打印内部识别码对应关系行历史表
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:14
 */
@ApiModel("铭牌打印内部识别码对应关系行历史表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_nameplate_print_rel_line_his")
public class HmeNameplatePrintRelLineHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NAMEPLATE_LINE_HIS_ID = "nameplateLineHisId";
    public static final String FIELD_NAMEPLATE_HEADER_HIS_ID = "nameplateHeaderHisId";
    public static final String FIELD_NAMEPLATE_LINE_ID = "nameplateLineId";
    public static final String FIELD_NAMEPLATE_HEADER_ID = "nameplateHeaderId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_CODE = "code";
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
    private String nameplateLineHisId;
    @ApiModelProperty(value = "编码")
    @NotBlank
    private String code;
    @ApiModelProperty(value = "头历史表主键ID", required = true)
    @NotBlank
    private String nameplateHeaderHisId;
    @ApiModelProperty(value = "头表主键ID", required = true)
    @NotBlank
    private String nameplateLineId;
    @ApiModelProperty(value = "头表主键ID", required = true)
    @NotBlank
    private String nameplateHeaderId;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "数量", required = true)
    @NotNull
    private BigDecimal qty;
    @ApiModelProperty(value = "有效性", required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "CID", required = true)
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
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public HmeNameplatePrintRelLineHis setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return 主键ID
     */
    public String getNameplateLineHisId() {
        return nameplateLineHisId;
    }

    public HmeNameplatePrintRelLineHis setNameplateLineHisId(String nameplateLineHisId) {
        this.nameplateLineHisId = nameplateLineHisId;
        return this;
    }

    /**
     * @return 头历史表主键ID
     */
    public String getNameplateHeaderHisId() {
        return nameplateHeaderHisId;
    }

    public HmeNameplatePrintRelLineHis setNameplateHeaderHisId(String nameplateHeaderHisId) {
        this.nameplateHeaderHisId = nameplateHeaderHisId;
        return this;
    }

    /**
     * @return 头表主键ID
     */
    public String getNameplateLineId() {
        return nameplateLineId;
    }

    public HmeNameplatePrintRelLineHis setNameplateLineId(String nameplateLineId) {
        this.nameplateLineId = nameplateLineId;
        return this;
    }

    /**
     * @return 头表主键ID
     */
    public String getNameplateHeaderId() {
        return nameplateHeaderId;
    }

    public HmeNameplatePrintRelLineHis setNameplateHeaderId(String nameplateHeaderId) {
        this.nameplateHeaderId = nameplateHeaderId;
        return this;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public HmeNameplatePrintRelLineHis setMaterialId(String materialId) {
        this.materialId = materialId;
        return this;
    }

    /**
     * @return 数量
     */
    public BigDecimal getQty() {
        return qty;
    }

    public HmeNameplatePrintRelLineHis setQty(BigDecimal qty) {
        this.qty = qty;
        return this;
    }

    /**
     * @return 有效性
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public HmeNameplatePrintRelLineHis setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public HmeNameplatePrintRelLineHis setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public HmeNameplatePrintRelLineHis setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public HmeNameplatePrintRelLineHis setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public HmeNameplatePrintRelLineHis setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public HmeNameplatePrintRelLineHis setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public HmeNameplatePrintRelLineHis setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public HmeNameplatePrintRelLineHis setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public HmeNameplatePrintRelLineHis setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public HmeNameplatePrintRelLineHis setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public HmeNameplatePrintRelLineHis setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public HmeNameplatePrintRelLineHis setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public HmeNameplatePrintRelLineHis setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public HmeNameplatePrintRelLineHis setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public HmeNameplatePrintRelLineHis setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public HmeNameplatePrintRelLineHis setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public HmeNameplatePrintRelLineHis setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public HmeNameplatePrintRelLineHis setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
