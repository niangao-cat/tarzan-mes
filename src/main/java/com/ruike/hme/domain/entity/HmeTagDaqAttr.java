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
 * 数据项数据采集扩展属性表
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 09:52:44
 */
@ApiModel("数据项数据采集扩展属性表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_tag_daq_attr")
public class HmeTagDaqAttr extends AuditDomain {

    public static final String FIELD_TAG_DAQ_ATTR_ID = "tagDaqAttrId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_EQUIPMENT_CATEGORY = "equipmentCategory";
    public static final String FIELD_VALUE_FIELD = "valueField";
    public static final String FIELD_LIMIT_COND1 = "limitCond1";
    public static final String FIELD_COND1_VALUE = "cond1Value";
    public static final String FIELD_LIMIT_COND2 = "limitCond2";
    public static final String FIELD_COND2_VALUE = "cond2Value";
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


    @ApiModelProperty("主键ID")
    @Id
    private String tagDaqAttrId;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "数据项ID", required = true)
    @NotBlank
    private String tagId;
    @ApiModelProperty(value = "设备类别", required = true)
    @NotBlank
    private String equipmentCategory;
    @ApiModelProperty(value = "取值字段", required = true)
    @NotBlank
    private String valueField;
    @ApiModelProperty(value = "限制条件1")
    private String limitCond1;
    @ApiModelProperty(value = "条件1限制值")
    private String cond1Value;
    @ApiModelProperty(value = "限制条件2")
    private String limitCond2;
    @ApiModelProperty(value = "条件2限制值")
    private String cond2Value;
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
     * @return 主键ID
     */
    public String getTagDaqAttrId() {
        return tagDaqAttrId;
    }

    public void setTagDaqAttrId(String tagDaqAttrId) {
        this.tagDaqAttrId = tagDaqAttrId;
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
     * @return 数据项ID
     */
    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    /**
     * @return 设备类别
     */
    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }

    /**
     * @return 取值字段
     */
    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    /**
     * @return 限制条件1
     */
    public String getLimitCond1() {
        return limitCond1;
    }

    public void setLimitCond1(String limitCond1) {
        this.limitCond1 = limitCond1;
    }

    /**
     * @return 条件1限制值
     */
    public String getCond1Value() {
        return cond1Value;
    }

    public void setCond1Value(String cond1Value) {
        this.cond1Value = cond1Value;
    }

    /**
     * @return 限制条件2
     */
    public String getLimitCond2() {
        return limitCond2;
    }

    public void setLimitCond2(String limitCond2) {
        this.limitCond2 = limitCond2;
    }

    /**
     * @return 条件2限制值
     */
    public String getCond2Value() {
        return cond2Value;
    }

    public void setCond2Value(String cond2Value) {
        this.cond2Value = cond2Value;
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
