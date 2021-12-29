package tarzan.actual.domain.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 数据收集实绩历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
@ApiModel("数据收集实绩历史表")

@ModifyAudit

@Table(name = "mt_data_record_his")
@CustomPrimary
public class MtDataRecordHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DATA_RECORD_HIS_ID = "dataRecordHisId";
    public static final String FIELD_DATA_RECORD_ID = "dataRecordId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_EO_STEP_ACTUAL_ID = "eoStepActualId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_COMPONENT_MATERIAL_ID = "componentMaterialId";
    public static final String FIELD_ASSEMBLE_CONFIRM_ID = "assembleConfirmId";
    public static final String FIELD_NC_CODE_ID = "ncCodeId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_TAG_VALUE = "tagValue";
    public static final String FIELD_TAG_CALCULATE_RESULT = "tagCalculateResult";
    public static final String FIELD_RECORD_REMARK = "recordRemark";
    public static final String FIELD_RECORD_DATE = "recordDate";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_TAG_CODE = "tagCode";
    public static final String FIELD_TAG_DESCRIPTION = "tagDescription";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_TAG_ENABLE_FLAG = "tagEnableFlag";
    public static final String FIELD_VALUE_TYPE = "valueType";
    public static final String FIELD_TRUE_VALUE = "trueValue";
    public static final String FIELD_FALSE_VALUE = "falseValue";
    public static final String FIELD_COLLECTION_METHOD = "collectionMethod";
    public static final String FIELD_VALUE_ALLOW_MISSING = "valueAllowMissing";
    public static final String FIELD_MINIMUM_VALUE = "minimumValue";
    public static final String FIELD_MAXIMAL_VALUE = "maximalValue";
    public static final String FIELD_UNIT = "unit";
    public static final String FIELD_MANDATORY_NUM = "mandatoryNum";
    public static final String FIELD_OPTIONAL_NUM = "optionalNum";
    public static final String FIELD_API_ID = "apiId";
    public static final String FIELD_TAG_GROUP_CODE = "tagGroupCode";
    public static final String FIELD_TAG_GROUP_DESCRIPTION = "tagGroupDescription";
    public static final String FIELD_TAG_GROUP_TYPE = "tagGroupType";
    public static final String FIELD_SOURCE_GROUP_ID = "sourceGroupId";
    public static final String FIELD_BUSINESS_TYPE = "businessType";
    public static final String FIELD_TAG_GROUP_STATUS = "tagGroupStatus";
    public static final String FIELD_COLLECTION_TIME_CONTROL = "collectionTimeControl";
    public static final String FIELD_USER_VERIFICATION = "userVerification";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 4929477604873202807L;

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
    @ApiModelProperty("主键")
    @Id
    @Where
    private String dataRecordHisId;
    @ApiModelProperty(value = "基表主键", required = true)
    @NotBlank
    @Where
    private String dataRecordId;
    @ApiModelProperty(value = "执行作业")
    @Where
    private String eoId;
    @ApiModelProperty(value = "物料ID")
    @Where
    private String materialId;
    @ApiModelProperty(value = "工艺ID")
    @Where
    private String operationId;
    @ApiModelProperty(value = "步骤实绩唯一标识")
    @Where
    private String eoStepActualId;
    @ApiModelProperty(value = "工艺路线步骤ID")
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "工作单元ID")
    @Where
    private String workcellId;
    @ApiModelProperty(value = "组件物料")
    @Where
    private String componentMaterialId;
    @ApiModelProperty(value = "装配实绩唯一标识")
    @Where
    private String assembleConfirmId;
    @ApiModelProperty(value = "不良代码")
    @Where
    private String ncCodeId;
    @ApiModelProperty(value = "数据组", required = true)
    @NotBlank
    @Where
    private String tagGroupId;
    @ApiModelProperty(value = "数据项", required = true)
    @NotBlank
    @Where
    private String tagId;
    @ApiModelProperty(value = "收集值（文件时为路径）", required = true)
    @NotBlank
    @Where
    private String tagValue;
    @ApiModelProperty(value = "判定结果")
    @Where
    private String tagCalculateResult;
    @ApiModelProperty(value = "备注")
    @Where
    private String recordRemark;
    @ApiModelProperty(value = "时间", required = true)
    @NotNull
    @Where
    private Date recordDate;
    @ApiModelProperty(value = "记录人ID", required = true)
    @NotBlank
    @Where
    private String userId;
    @ApiModelProperty(value = "数据项编号")
    @Where
    private String tagCode;
    @ApiModelProperty(value = "描述")
    @Where
    private String tagDescription;
    @ApiModelProperty(value = "备注")
    @Where
    private String remark;
    @ApiModelProperty(value = "状态")
    @Where
    private String tagEnableFlag;
    @ApiModelProperty(value = "数据类型")
    @Where
    private String valueType;
    @ApiModelProperty(value = "符合值")
    @Where
    private String trueValue;
    @ApiModelProperty(value = "不符合值")
    @Where
    private String falseValue;
    @ApiModelProperty(value = "数据收集方式")
    @Where
    private String collectionMethod;
    @ApiModelProperty(value = "允许缺失值")
    @Where
    private String valueAllowMissing;
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
    @ApiModelProperty(value = "转化API_ID")
    @Where
    private String apiId;
    @ApiModelProperty(value = "数据收集组编码")
    @Where
    private String tagGroupCode;
    @ApiModelProperty(value = "数据收集组描述")
    @Where
    private String tagGroupDescription;
    @ApiModelProperty(value = "收集组类型")
    @Where
    private String tagGroupType;
    @ApiModelProperty(value = "源数据收集组ID")
    @Where
    private String sourceGroupId;
    @ApiModelProperty(value = "业务类型")
    @Where
    private String businessType;
    @ApiModelProperty(value = "状态")
    @Where
    private String tagGroupStatus;
    @ApiModelProperty(value = "数据收集时点")
    @Where
    private String collectionTimeControl;
    @ApiModelProperty(value = "需要用户验证")
    @Where
    private String userVerification;
    @ApiModelProperty(value = "物料批ID")
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
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
     * @return 主键
     */
    public String getDataRecordHisId() {
        return dataRecordHisId;
    }

    public void setDataRecordHisId(String dataRecordHisId) {
        this.dataRecordHisId = dataRecordHisId;
    }

    /**
     * @return 基表主键
     */
    public String getDataRecordId() {
        return dataRecordId;
    }

    public void setDataRecordId(String dataRecordId) {
        this.dataRecordId = dataRecordId;
    }

    /**
     * @return 执行作业
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
     * @return 步骤实绩唯一标识
     */
    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    /**
     * @return 工艺路线步骤ID
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 工作单元ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 组件物料
     */
    public String getComponentMaterialId() {
        return componentMaterialId;
    }

    public void setComponentMaterialId(String componentMaterialId) {
        this.componentMaterialId = componentMaterialId;
    }

    /**
     * @return 装配实绩唯一标识
     */
    public String getAssembleConfirmId() {
        return assembleConfirmId;
    }

    public void setAssembleConfirmId(String assembleConfirmId) {
        this.assembleConfirmId = assembleConfirmId;
    }

    /**
     * @return 不良代码
     */
    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    /**
     * @return 数据组
     */
    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    /**
     * @return 数据项
     */
    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    /**
     * @return 收集值（文件时为路径）
     */
    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    /**
     * @return 判定结果
     */
    public String getTagCalculateResult() {
        return tagCalculateResult;
    }

    public void setTagCalculateResult(String tagCalculateResult) {
        this.tagCalculateResult = tagCalculateResult;
    }

    /**
     * @return 备注
     */
    public String getRecordRemark() {
        return recordRemark;
    }

    public void setRecordRemark(String recordRemark) {
        this.recordRemark = recordRemark;
    }

    /**
     * @return 时间
     */

    public Date getRecordDate() {
        if (recordDate != null) {
            return (Date) recordDate.clone();
        } else {
            return null;
        }
    }

    public void setRecordDate(Date recordDate) {
        if (recordDate == null) {
            this.recordDate = null;
        } else {
            this.recordDate = (Date) recordDate.clone();
        }
    }

    /**
     * @return 记录人ID
     */
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return 数据项编号
     */
    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    /**
     * @return 描述
     */
    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
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
     * @return 状态
     */
    public String getTagEnableFlag() {
        return tagEnableFlag;
    }

    public void setTagEnableFlag(String tagEnableFlag) {
        this.tagEnableFlag = tagEnableFlag;
    }

    /**
     * @return 数据类型
     */
    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
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
     * @return 转化API_ID
     */
    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    /**
     * @return 数据收集组编码
     */
    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    /**
     * @return 数据收集组描述
     */
    public String getTagGroupDescription() {
        return tagGroupDescription;
    }

    public void setTagGroupDescription(String tagGroupDescription) {
        this.tagGroupDescription = tagGroupDescription;
    }

    /**
     * @return 收集组类型
     */
    public String getTagGroupType() {
        return tagGroupType;
    }

    public void setTagGroupType(String tagGroupType) {
        this.tagGroupType = tagGroupType;
    }

    /**
     * @return 源数据收集组ID
     */
    public String getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(String sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    /**
     * @return 业务类型
     */
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * @return 状态
     */
    public String getTagGroupStatus() {
        return tagGroupStatus;
    }

    public void setTagGroupStatus(String tagGroupStatus) {
        this.tagGroupStatus = tagGroupStatus;
    }

    /**
     * @return 数据收集时点
     */
    public String getCollectionTimeControl() {
        return collectionTimeControl;
    }

    public void setCollectionTimeControl(String collectionTimeControl) {
        this.collectionTimeControl = collectionTimeControl;
    }

    /**
     * @return 需要用户验证
     */
    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
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
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
