package tarzan.general.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据收集项分配收集组表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@ApiModel("数据收集项分配收集组表")

@ModifyAudit

@Table(name = "mt_tag_group_assign")
@CustomPrimary
public class MtTagGroupAssign extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TAG_GROUP_ASSIGN_ID = "tagGroupAssignId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_SERIAL_NUMBER = "serialNumber";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_COLLECTION_METHOD = "collectionMethod";
    public static final String FIELD_VALUE_ALLOW_MISSING = "valueAllowMissing";
    public static final String FIELD_TRUE_VALUE = "trueValue";
    public static final String FIELD_FALSE_VALUE = "falseValue";
    public static final String FIELD_MINIMUM_VALUE = "minimumValue";
    public static final String FIELD_MAXIMAL_VALUE = "maximalValue";
    public static final String FIELD_UNIT = "unit";
    public static final String FIELD_MANDATORY_NUM = "mandatoryNum";
    public static final String FIELD_OPTIONAL_NUM = "optionalNum";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("数据收集项分配收集组ID")
    @Id
    @Where
    private String tagGroupAssignId;
    @ApiModelProperty(value = "数据收集组ID", required = true)
    @NotBlank
    @Where
    private String tagGroupId;
    @ApiModelProperty(value = "序号", required = true)
    @NotNull
    @Where
    private Double serialNumber;
    @ApiModelProperty(value = "数据项ID", required = true)
    @NotBlank
    @Where
    private String tagId;
    @ApiModelProperty(value = "数据收集方式")
    @Where
    private String collectionMethod;
    @ApiModelProperty(value = "允许缺失值")
    @Where
    private String valueAllowMissing;
    @ApiModelProperty(value = "符合值")
    @Where
    private String trueValue;
    @ApiModelProperty(value = "不符合值")
    @Where
    private String falseValue;
    @ApiModelProperty(value = "最小值")
    @Where
    private Double minimumValue;
    @ApiModelProperty(value = "最大值")
    @Where
    private Double maximalValue;
    @ApiModelProperty(value = "计量单位")
    @Where
    private String unit;
    @ApiModelProperty(value = "必需的数据条数")
    @Where
    private Long mandatoryNum;
    @ApiModelProperty(value = "可选的数据条数")
    @Where
    private Long optionalNum;
    @Cid
    @Where
    private Long cid;

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
     * @return 数据收集项分配收集组ID
     */
    public String getTagGroupAssignId() {
        return tagGroupAssignId;
    }

    public void setTagGroupAssignId(String tagGroupAssignId) {
        this.tagGroupAssignId = tagGroupAssignId;
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
     * @return 序号
     */
    public Double getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Double serialNumber) {
        this.serialNumber = serialNumber;
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
     * @return 数据收集方式
     */
    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    /**
     * @return 允许缺失值
     */
    public String getValueAllowMissing() {
        return valueAllowMissing;
    }

    public void setValueAllowMissing(String valueAllowMissing) {
        this.valueAllowMissing = valueAllowMissing;
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
    public Double getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(Double minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * @return 最大值
     */
    public Double getMaximalValue() {
        return maximalValue;
    }

    public void setMaximalValue(Double maximalValue) {
        this.maximalValue = maximalValue;
    }

    /**
     * @return 计量单位
     */
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return 必需的数据条数
     */
    public Long getMandatoryNum() {
        return mandatoryNum;
    }

    public void setMandatoryNum(Long mandatoryNum) {
        this.mandatoryNum = mandatoryNum;
    }

    /**
     * @return 可选的数据条数
     */
    public Long getOptionalNum() {
        return optionalNum;
    }

    public void setOptionalNum(Long optionalNum) {
        this.optionalNum = optionalNum;
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

}
