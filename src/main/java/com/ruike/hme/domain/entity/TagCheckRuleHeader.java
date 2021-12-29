package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据项展示规则维护头表
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:18
 */
@ApiModel("数据项展示规则维护头表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_tag_check_rule_header")
@CustomPrimary
public class TagCheckRuleHeader extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_HEADER_ID = "headerId";
    public static final String FIELD_BUSINESS_ID = "businessId";
    public static final String FIELD_ITEM_GROUP_ID = "itemGroupId";
    public static final String FIELD_RULE_CODE = "ruleCode";
    public static final String FIELD_RULE_DESCRIPTION = "ruleDescription";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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
    @ApiModelProperty("主键")
    @Id
    private String headerId;
    @ApiModelProperty(value = "应用事业部", required = true)
    @NotBlank
    private String businessId;
    @ApiModelProperty(value = "物料组编码")
    private String itemGroupId;
    @ApiModelProperty(value = "规则编码", required = true)
    @NotBlank
    private String ruleCode;
    @ApiModelProperty(value = "规则描述", required = true)
    @NotBlank
    private String ruleDescription;
    @ApiModelProperty(value = "类型", required = true)
    @NotBlank
    @LovValue(value = "HME.EXHIBITION_TYPE", meaningField = "typeMeaning")
    private String type;
    @ApiModelProperty(value = "当前工序")
    private String workcellId;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
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
    @ApiModelProperty(value = "是否有效描述")
    @Transient
    private String enableFlagMeaning;
    @ApiModelProperty(value = "应用事业部描述")
    @Transient
    private String areaName;
    @Transient
    @ApiModelProperty(value = "物料组编码")
    private String itemGroupCode;
    @Transient
    @ApiModelProperty(value = "物料组描述")
    private String itemGroupDescription;
    @ApiModelProperty(value = "当前工序描述")
    @Transient
    private String workcellName;
    @ApiModelProperty(value = "类型描述")
    @Transient
    private String typeMeaning;
//
// getter/setter
// ------------------------------------------------------------------------------

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public TagCheckRuleHeader setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 主键
     */
    public String getHeaderId() {
        return headerId;
    }

    public TagCheckRuleHeader setHeaderId(String headerId) {
        this.headerId = headerId;
        return this;
    }

    /**
     * @return 应用事业部
     */
    public String getBusinessId() {
        return businessId;
    }

    public TagCheckRuleHeader setBusinessId(String businessId) {
        this.businessId = businessId;
        return this;
    }

    /**
     * @return 物料组编码
     */
    public String getItemGroupId() {
        return itemGroupId;
    }

    public void setItemGroupId(String itemGroupId) {
        this.itemGroupId = itemGroupId;
    }


    public String getEnableFlagMeaning() {
        return enableFlagMeaning;
    }

    public void setEnableFlagMeaning(String enableFlagMeaning) {
        this.enableFlagMeaning = enableFlagMeaning;
    }

    /**
     * @return 规则编码
     */
    public String getRuleCode() {
        return ruleCode;
    }

    public TagCheckRuleHeader setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
        return this;
    }

    /**
     * @return 规则描述
     */
    public String getRuleDescription() {
        return ruleDescription;
    }

    public TagCheckRuleHeader setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
        return this;
    }

    /**
     * @return 类型
     */
    public String getType() {
        return type;
    }

    public TagCheckRuleHeader setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * @return 当前工序
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public TagCheckRuleHeader setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
        return this;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public TagCheckRuleHeader setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public TagCheckRuleHeader setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public TagCheckRuleHeader setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public TagCheckRuleHeader setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public TagCheckRuleHeader setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public TagCheckRuleHeader setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public TagCheckRuleHeader setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public TagCheckRuleHeader setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public TagCheckRuleHeader setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public TagCheckRuleHeader setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public TagCheckRuleHeader setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public TagCheckRuleHeader setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public TagCheckRuleHeader setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public TagCheckRuleHeader setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public TagCheckRuleHeader setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public TagCheckRuleHeader setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public TagCheckRuleHeader setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public TagCheckRuleHeader setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getItemGroupCode() {
        return itemGroupCode;
    }

    public void setItemGroupCode(String itemGroupCode) {
        this.itemGroupCode = itemGroupCode;
    }

    public String getItemGroupDescription() {
        return itemGroupDescription;
    }

    public void setItemGroupDescription(String itemGroupDescription) {
        this.itemGroupDescription = itemGroupDescription;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getTypeMeaning() {
        return typeMeaning;
    }

    public void setTypeMeaning(String typeMeaning) {
        this.typeMeaning = typeMeaning;
    }
}
