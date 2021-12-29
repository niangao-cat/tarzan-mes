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
 * 生产数据采集行表
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:56:19
 */
@ApiModel("生产数据采集行表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_data_collect_line")
public class HmeDataCollectLine extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COLLECT_LINE_ID = "collectLineId";
    public static final String FIELD_COLLECT_HEADER_ID = "collectHeaderId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_SHIFT_ID = "shiftId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_TRUE_VALUE = "trueValue";
    public static final String FIELD_FALSE_VALUE = "falseValue";
    public static final String FIELD_MINIMUM_VALUE = "minimumValue";
    public static final String FIELD_MAXIMAL_VALUE = "maximalValue";
    public static final String FIELD_STANDARD = "standard";
    public static final String FIELD_GROUP_PURPOSE = "groupPurpose";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_REFERENCE_POINT = "referencePoint";
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
    @ApiModelProperty("生产数据采集行表主键")
    @Id
    private String collectLineId;
    @ApiModelProperty(value = "生产数据采集行表主键", required = true)
    @NotBlank
    private String collectHeaderId;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "班次ID")
    private String shiftId;
    @ApiModelProperty(value = "采集行物料ID")
    private String materialId;
    @ApiModelProperty(value = "数据收集组ID", required = true)
    @NotBlank
    private String tagGroupId;
    @ApiModelProperty(value = "数据项ID", required = true)
    @NotBlank
    private String tagId;
    @ApiModelProperty(value = "符合值")
    private String trueValue;
    @ApiModelProperty(value = "不符合值")
    private String falseValue;
    @ApiModelProperty(value = "最小值")
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "最大值")
    private BigDecimal maximalValue;
    @ApiModelProperty(value = "标准值")
    private String standard;
    @ApiModelProperty(value = "采集组用途", required = true)
    @NotBlank
    private String groupPurpose;
    @ApiModelProperty(value = "采集结果")
    private String result;
    @ApiModelProperty(value = "参考点")
    private String referencePoint;
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
     * @return 生产数据采集行表主键
     */
    public String getCollectLineId() {
        return collectLineId;
    }

    public void setCollectLineId(String collectLineId) {
        this.collectLineId = collectLineId;
    }

    /**
     * @return 生产数据采集行表主键
     */
    public String getCollectHeaderId() {
        return collectHeaderId;
    }

    public void setCollectHeaderId(String collectHeaderId) {
        this.collectHeaderId = collectHeaderId;
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
     * @return 班次ID
     */
    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return 采集行物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 数据收集组ID
     */
    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
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
     * @return 符合值
     */
    public String getTrueValue() {
        return trueValue;
    }

    public void setTrueValue(String trueValue) {
        this.trueValue = trueValue;
    }

    /**
     * @return 不符合值
     */
    public String getFalseValue() {
        return falseValue;
    }

    public void setFalseValue(String falseValue) {
        this.falseValue = falseValue;
    }

    /**
     * @return 最小值
     */
    public BigDecimal getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(BigDecimal minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * @return 最大值
     */
    public BigDecimal getMaximalValue() {
        return maximalValue;
    }

    public void setMaximalValue(BigDecimal maximalValue) {
        this.maximalValue = maximalValue;
    }

    /**
     * @return 标准值
     */
    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    /**
     * @return 采集组用途
     */
    public String getGroupPurpose() {
        return groupPurpose;
    }

    public void setGroupPurpose(String groupPurpose) {
        this.groupPurpose = groupPurpose;
    }

    /**
     * @return 采集结果
     */
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
