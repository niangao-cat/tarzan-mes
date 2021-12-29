package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MtDataRecordVO5 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = -5213199039877031501L;
    @ApiModelProperty("主键")
    private String dataRecordId;
    @ApiModelProperty("执行作业")
    private String eoId;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("工艺")
    private String operationId;
    @ApiModelProperty("步骤实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty("工艺路线步骤Id")
    private String routerStepId;
    @ApiModelProperty("工作单元")
    private String workcellId;
    @ApiModelProperty("组件物料")
    private String componentMaterialId;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("装配实绩唯一标识")
    private String assembleConfirmId;
    @ApiModelProperty("不良代码")
    private String ncCodeId;
    @ApiModelProperty("数据组")
    private String tagGroupId;
    @ApiModelProperty("数据项")
    private String tagId;
    @ApiModelProperty("收集值（文件时为路径）")
    private String tagValue;
    @ApiModelProperty("判定结果")
    private String tagCalculateResult;
    @ApiModelProperty("备注")
    private String recordRemark;
    @ApiModelProperty("时间")
    private Date recordDate;
    @ApiModelProperty("用户Id")
    private String userId;
    @ApiModelProperty("数据项编码")
    private String tagCode;
    @ApiModelProperty("描述")
    private String tagDescription;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("状态")
    private String tagEnableFlag;
    @ApiModelProperty("数据类型")
    private String valueType;
    @ApiModelProperty("符合值")
    private String trueValue;
    @ApiModelProperty("不符合值")
    private String falseValue;
    @ApiModelProperty("数据收集方式")
    private String collectionMethod;
    @ApiModelProperty("允许缺失值")
    private String valueAllowMissing;
    @ApiModelProperty("最小值")
    private Double minimumValue;
    @ApiModelProperty("最大值")
    private Double maximalValue;
    @ApiModelProperty("计量单位")
    private String unit;
    @ApiModelProperty("必需的条目数")
    private Long mandatoryNum;
    @ApiModelProperty("可选的条目数")
    private Long optionalNum;
    @ApiModelProperty("转换API")
    private String apiId;
    @ApiModelProperty("数据收集组编码")
    private String tagGroupCode;
    @ApiModelProperty("数据收集组描述")
    private String tagGroupDescription;
    @ApiModelProperty("数据项收集组类型")
    private String tagGroupType;
    @ApiModelProperty("源数据收集组ID")
    private String sourceGroupId;
    @ApiModelProperty("业务类型")
    private String businessType;
    @ApiModelProperty("数据收集组状态")
    private String tagGroupStatus;
    @ApiModelProperty("数据收集时点")
    private String collectionTimeControl;
    @ApiModelProperty("需要用户验证")
    private String userVerification;
    @ApiModelProperty("记录时间从")
    private String recordDateFrom;
    @ApiModelProperty("记录时间至")
    private String recordDateTo;

    public String getDataRecordId() {
        return dataRecordId;
    }

    public void setDataRecordId(String dataRecordId) {
        this.dataRecordId = dataRecordId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getComponentMaterialId() {
        return componentMaterialId;
    }

    public void setComponentMaterialId(String componentMaterialId) {
        this.componentMaterialId = componentMaterialId;
    }

    public String getAssembleConfirmId() {
        return assembleConfirmId;
    }

    public void setAssembleConfirmId(String assembleConfirmId) {
        this.assembleConfirmId = assembleConfirmId;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getTagCalculateResult() {
        return tagCalculateResult;
    }

    public void setTagCalculateResult(String tagCalculateResult) {
        this.tagCalculateResult = tagCalculateResult;
    }

    public String getRecordRemark() {
        return recordRemark;
    }

    public void setRecordRemark(String recordRemark) {
        this.recordRemark = recordRemark;
    }

    public Date getRecordDate() {
        if (recordDate == null) {
            return null;
        } else {
            return (Date) recordDate.clone();
        }
    }

    public void setRecordDate(Date recordDate) {
        if (recordDate == null) {
            this.recordDate = null;
        } else {
            this.recordDate = (Date) recordDate.clone();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTagEnableFlag() {
        return tagEnableFlag;
    }

    public void setTagEnableFlag(String tagEnableFlag) {
        this.tagEnableFlag = tagEnableFlag;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getTrueValue() {
        return trueValue;
    }

    public void setTrueValue(String trueValue) {
        this.trueValue = trueValue;
    }

    public String getFalseValue() {
        return falseValue;
    }

    public void setFalseValue(String falseValue) {
        this.falseValue = falseValue;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getValueAllowMissing() {
        return valueAllowMissing;
    }

    public void setValueAllowMissing(String valueAllowMissing) {
        this.valueAllowMissing = valueAllowMissing;
    }

    public Double getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(Double minimumValue) {
        this.minimumValue = minimumValue;
    }

    public Double getMaximalValue() {
        return maximalValue;
    }

    public void setMaximalValue(Double maximalValue) {
        this.maximalValue = maximalValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getMandatoryNum() {
        return mandatoryNum;
    }

    public void setMandatoryNum(Long mandatoryNum) {
        this.mandatoryNum = mandatoryNum;
    }

    public Long getOptionalNum() {
        return optionalNum;
    }

    public void setOptionalNum(Long optionalNum) {
        this.optionalNum = optionalNum;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public String getTagGroupDescription() {
        return tagGroupDescription;
    }

    public void setTagGroupDescription(String tagGroupDescription) {
        this.tagGroupDescription = tagGroupDescription;
    }

    public String getTagGroupType() {
        return tagGroupType;
    }

    public void setTagGroupType(String tagGroupType) {
        this.tagGroupType = tagGroupType;
    }

    public String getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(String sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTagGroupStatus() {
        return tagGroupStatus;
    }

    public void setTagGroupStatus(String tagGroupStatus) {
        this.tagGroupStatus = tagGroupStatus;
    }

    public String getCollectionTimeControl() {
        return collectionTimeControl;
    }

    public void setCollectionTimeControl(String collectionTimeControl) {
        this.collectionTimeControl = collectionTimeControl;
    }

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }

    public String getRecordDateFrom() {
        return recordDateFrom;
    }

    public void setRecordDateFrom(String recordDateFrom) {
        this.recordDateFrom = recordDateFrom;
    }

    public String getRecordDateTo() {
        return recordDateTo;
    }

    public void setRecordDateTo(String recordDateTo) {
        this.recordDateTo = recordDateTo;
    }

}
