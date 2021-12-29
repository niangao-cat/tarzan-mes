package tarzan.general.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtTagGroupObjectVO2
 *
 * @author: {xieyiyang}
 * @date: 2020/2/17 10:43
 * @description:
 */
public class MtTagGroupObjectVO2 implements Serializable {
    private static final long serialVersionUID = 5852668467426310496L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "执行作业ID")
    private String eoId;
    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;
    @ApiModelProperty(value = "装配清单组件ID")
    private String bomComponentId;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "模糊匹配标识")
    private String fuzzyMatchingFlag;


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

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
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

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getFuzzyMatchingFlag() {
        return fuzzyMatchingFlag;
    }

    public void setFuzzyMatchingFlag(String fuzzyMatchingFlag) {
        this.fuzzyMatchingFlag = fuzzyMatchingFlag;
    }
}
