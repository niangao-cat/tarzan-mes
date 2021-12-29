package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * dataRecordAndHisUpdate-数据收集实绩更新使用VO
 * 
 * @author benjamin
 * @date 2019-07-02 16:37
 */
public class MtDataRecordVO2 implements Serializable {
    private static final long serialVersionUID = -1827357436528861267L;

    /**
     * 主键
     * 
     */
    private String dataRecordId;
    /**
     * 执行作业
     */
    private String eoId;
    /**
     * 工艺ID
     */
    private String operationId;
    /**
     * 工作单元ID
     */
    private String workcellId;
    /**
     * 组件物料
     */
    private String componentMaterialId;
    /**
     * 装配实绩唯一标识
     */
    private String assembleConfirmId;
    /**
     * 不良代码
     */
    private String ncCodeId;
    /**
     * 数据组
     */
    private String tagGroupId;
    /**
     * 数据项
     */
    private String tagId;
    /**
     * 收集值（文件时为路径）
     */
    private String tagValue;
    /**
     * 判定结果
     */
    private String tagCalculateResult;
    /**
     * 备注
     */
    private String recordRemark;
    /**
     * 时间
     */
    private Date recordDate;
    /**
     * 记录人ID
     */
    private String userId;
    /**
     * 数据项编号
     */
    private String tagCode;
    /**
     * 描述
     */
    private String tagDescription;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private String tagStatus;
    /**
     * 数据类型
     */
    private String valueType;
    /**
     * 符合值
     */
    private String trueValue;
    /**
     * 不符合值
     */
    private String falseValue;
    /**
     * 数据收集方式
     */
    private String collectionMethod;
    /**
     * 允许缺失值
     */
    private String valueAllowMissing;
    /**
     * 最小值
     */
    private Double minimumValue;
    /**
     * 最大值
     */
    private Double maximalValue;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 必需的数据条数
     */
    private Long mandatoryNum;
    /**
     * 可选的数据条数
     */
    private Long optionalNum;
    /**
     * 转化API_ID
     */
    private String apiId;
    /**
     * 数据收集组编码
     */
    private String tagGroupCode;
    /**
     * 数据收集组描述
     */
    private String tagGroupDescription;
    /**
     * 收集组类型
     */
    private String tagGroupType;
    /**
     * 源数据收集组ID
     */
    private String sourceGroupId;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 状态
     */
    private String tagGroupStatus;
    /**
     * 数据收集时点
     */
    private String collectionTimeControl;
    /**
     * 需要用户验证
     */
    private String userVerification;
    /**
     * 物料Id
     */
    private String materialId;
    /**
     * 事件Id
     */
    private String eventId;

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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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

    public String getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(String tagStatus) {
        this.tagStatus = tagStatus;
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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
