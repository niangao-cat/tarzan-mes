package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 配送需求关系汇总
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/18 14:33
 */
@ToString
@EqualsAndHashCode
public class WmsDistributionDemandRelSumVO {
    @ApiModelProperty(value = "派工ID")
    private String woDispatchId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "工序Id")
    private String processId;

    public String getWoDispatchId() {
        return woDispatchId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public String getMaterialVersion() {
        return materialVersion;
    }

    public String getProcessId() {
        return processId;
    }

    public WmsDistributionDemandRelSumVO setWoDispatchId(String woDispatchId) {
        this.woDispatchId = woDispatchId;
        return this;
    }

    public WmsDistributionDemandRelSumVO setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
        return this;
    }

    public WmsDistributionDemandRelSumVO setMaterialId(String materialId) {
        this.materialId = materialId;
        return this;
    }

    public WmsDistributionDemandRelSumVO setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
        return this;
    }

    public WmsDistributionDemandRelSumVO setProcessId(String processId) {
        this.processId = processId;
        return this;
    }
}
