package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 备料执行事务参数
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/28 10:01
 */
@ToString
public class WmsPrepareExecTrxReqVO {
    @ApiModelProperty("事件ID")
    String eventId;
    @ApiModelProperty("事务标记")
    String signFlag;
    @ApiModelProperty("目标站点")
    String targetSiteId;
    @ApiModelProperty("目标仓库")
    String targetWarehouseId;
    @ApiModelProperty("目标货位")
    String targetLocatorId;
    @ApiModelProperty("配送单ID")
    String instructionDocId;
    @ApiModelProperty("配送单行ID")
    String instructionId;
    @ApiModelProperty("容器ID")
    String containerId;

    public String getEventId() {
        return eventId;
    }

    public WmsPrepareExecTrxReqVO setEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getSignFlag() {
        return signFlag;
    }

    public WmsPrepareExecTrxReqVO setSignFlag(String signFlag) {
        this.signFlag = signFlag;
        return this;
    }

    public String getTargetSiteId() {
        return targetSiteId;
    }

    public WmsPrepareExecTrxReqVO setTargetSiteId(String targetSiteId) {
        this.targetSiteId = targetSiteId;
        return this;
    }

    public String getTargetWarehouseId() {
        return targetWarehouseId;
    }

    public WmsPrepareExecTrxReqVO setTargetWarehouseId(String targetWarehouseId) {
        this.targetWarehouseId = targetWarehouseId;
        return this;
    }

    public String getTargetLocatorId() {
        return targetLocatorId;
    }

    public WmsPrepareExecTrxReqVO setTargetLocatorId(String targetLocatorId) {
        this.targetLocatorId = targetLocatorId;
        return this;
    }

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public WmsPrepareExecTrxReqVO setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
        return this;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public WmsPrepareExecTrxReqVO setInstructionId(String instructionId) {
        this.instructionId = instructionId;
        return this;
    }

    public String getContainerId() {
        return containerId;
    }

    public WmsPrepareExecTrxReqVO setContainerId(String containerId) {
        this.containerId = containerId;
        return this;
    }
}
