package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * dataRecordAndHisCreate-数据收集实绩创建使用VO
 * 
 * @author benjamin
 * @date 2019-07-02 11:23
 */
public class MtDataRecordVO implements Serializable {
    private static final long serialVersionUID = -7631851226316028469L;

    @ApiModelProperty(value = "执行作业")
    private String eoId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "步骤实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;
    @ApiModelProperty(value = "组件物料")
    private String componentMaterialId;
    @ApiModelProperty(value = "装配实绩唯一标识")
    private String assembleConfirmId;
    @ApiModelProperty(value = "不良代码")
    private String ncCodeId;
    @ApiModelProperty(value = "数据组")
    private String tagGroupId;
    @ApiModelProperty(value = "数据项")
    private String tagId;
    @ApiModelProperty(value = "收集值（文件时为路径）")
    private String tagValue;
    @ApiModelProperty(value = "判定结果")
    private String tagCalculateResult;
    @ApiModelProperty(value = "备注")
    private String recordRemark;
    @ApiModelProperty(value = "时间")
    private Date recordDate;
    @ApiModelProperty(value = "记录人ID")
    private String userId;
    @ApiModelProperty(value = "事件Id")
    private String eventId;
    @ApiModelProperty(value = "物料批")
    private String materialLotId;

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

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }
}
