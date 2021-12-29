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
 * 数据项展示规则维护行表
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:20
 */
@ApiModel("数据项展示规则维护行表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_tag_check_rule_line")
@CustomPrimary
public class TagCheckRuleLine extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LINE_ID = "lineId";
    public static final String FIELD_HEADER_ID = "headerId";
    public static final String FIELD_SOURCE_WORKCELL_ID = "sourceWorkcellId";
    public static final String FIELD_TAG_ID = "tagId";
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
    private String lineId;
    @ApiModelProperty(value = "展示数据项头表ID", required = true)
    @NotBlank
    private String headerId;
    @ApiModelProperty(value = "来源工序", required = true)
    @NotBlank
    private String sourceWorkcellId;
    @ApiModelProperty(value = "数据项ID", required = true)
    @NotBlank
    private String tagId;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @LovValue(lovCode = "enable_flag", meaningField = "enableFlagMeaning")
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
    @ApiModelProperty(value = "来源工序描述")
    @Transient
    private String workcellName;
    @ApiModelProperty(value = "数据项")
    @Transient
    private String tagCode;
    @ApiModelProperty(value = "是否有效含义")
    @Transient
    private String enableFlagMeaning;
    @Transient
    private String tagDescription;
//
// getter/setter
// ------------------------------------------------------------------------------

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public TagCheckRuleLine setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 主键
     */
    public String getLineId() {
        return lineId;
    }

    public TagCheckRuleLine setLineId(String lineId) {
        this.lineId = lineId;
        return this;
    }

    /**
     * @return 展示数据项头表ID
     */
    public String getHeaderId() {
        return headerId;
    }

    public TagCheckRuleLine setHeaderId(String headerId) {
        this.headerId = headerId;
        return this;
    }

    /**
     * @return 来源工序
     */
    public String getSourceWorkcellId() {
        return sourceWorkcellId;
    }

    public TagCheckRuleLine setSourceWorkcellId(String sourceWorkcellId) {
        this.sourceWorkcellId = sourceWorkcellId;
        return this;
    }

    /**
     * @return 数据项ID
     */
    public String getTagId() {
        return tagId;
    }

    public TagCheckRuleLine setTagId(String tagId) {
        this.tagId = tagId;
        return this;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public TagCheckRuleLine setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public TagCheckRuleLine setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public TagCheckRuleLine setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public TagCheckRuleLine setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public TagCheckRuleLine setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public TagCheckRuleLine setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public TagCheckRuleLine setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public TagCheckRuleLine setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public TagCheckRuleLine setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public TagCheckRuleLine setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public TagCheckRuleLine setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public TagCheckRuleLine setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public TagCheckRuleLine setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public TagCheckRuleLine setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public TagCheckRuleLine setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public TagCheckRuleLine setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public TagCheckRuleLine setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public TagCheckRuleLine setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }

    public String getEnableFlagMeaning() {
        return enableFlagMeaning;
    }

    public void setEnableFlagMeaning(String enableFlagMeaning) {
        this.enableFlagMeaning = enableFlagMeaning;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }
}
