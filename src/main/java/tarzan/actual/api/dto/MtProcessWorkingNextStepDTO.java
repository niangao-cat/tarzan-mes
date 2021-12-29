package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 0012, 2020-02-12 19:08
 */
public class MtProcessWorkingNextStepDTO implements Serializable {
    private static final long serialVersionUID = -8141822246334582310L;

    @ApiModelProperty("工作单元ID-登入")
    private String workcellId;

    @ApiModelProperty("EO步骤实绩ID-卡片")
    private String eoStepActualId;
    @ApiModelProperty("当前状态-卡片")
    private String sourceStatus;
    @ApiModelProperty("目标状态-卡片")
    private String targetStatus;
    @ApiModelProperty(value = "投料套数-卡片")
    private Double kitQty;
    @ApiModelProperty("EO的工序装配标识-卡片")
    private String operationAssembleFlag;
    @ApiModelProperty("EO工艺路线实绩ID-卡片")
    private String eoRouterActualId;
    @ApiModelProperty("EO的物料ID-卡片")
    private String materialId;
    @ApiModelProperty("EO的工艺ID-卡片")
    private String operationId;
    @ApiModelProperty("EO的工艺路线ID-卡片")
    private String routerId;
    @ApiModelProperty("EO的工艺路线步骤ID-卡片")
    private String routerStepId;
    @ApiModelProperty("EO的工单ID-卡片")
    private String workOrderId;
    @ApiModelProperty("执行作业ID-卡片")
    private String eoId;
    @ApiModelProperty("EO的装配清单ID-卡片")
    private String bomId;

    @ApiModelProperty("卡片的完成步骤标识")
    private String doneFlag;
    @ApiModelProperty("卡片的完成步骤标识-配置项")
    private String nextStepWithoutDataCollect;
    @ApiModelProperty("下一步自动处理-配置项")
    private String nextStepAutomatically;
    @ApiModelProperty("未完成装配允许交付-配置项")
    private String nextStepWithoutAssembled;
    @ApiModelProperty("数据项不合格允许交付-配置项")
    private String nextStepWithNg;
    @ApiModelProperty("EO采集组类型匹配规则")
    private String eoDataCollect;

    @ApiModelProperty("数量-输入")
    private Double qty;
    @ApiModelProperty("选择的下一步骤ID-输入")
    private String routerNextStepId;

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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }

    public String getNextStepAutomatically() {
        return nextStepAutomatically;
    }

    public void setNextStepAutomatically(String nextStepAutomatically) {
        this.nextStepAutomatically = nextStepAutomatically;
    }

    public String getNextStepWithoutAssembled() {
        return nextStepWithoutAssembled;
    }

    public void setNextStepWithoutAssembled(String nextStepWithoutAssembled) {
        this.nextStepWithoutAssembled = nextStepWithoutAssembled;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    public String getDoneFlag() {
        return doneFlag;
    }

    public void setDoneFlag(String doneFlag) {
        this.doneFlag = doneFlag;
    }

    public String getNextStepWithoutDataCollect() {
        return nextStepWithoutDataCollect;
    }

    public void setNextStepWithoutDataCollect(String nextStepWithoutDataCollect) {
        this.nextStepWithoutDataCollect = nextStepWithoutDataCollect;
    }

    public String getNextStepWithNg() {
        return nextStepWithNg;
    }

    public void setNextStepWithNg(String nextStepWithNg) {
        this.nextStepWithNg = nextStepWithNg;
    }

    public Double getKitQty() {
        return kitQty;
    }

    public void setKitQty(Double kitQty) {
        this.kitQty = kitQty;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterNextStepId() {
        return routerNextStepId;
    }

    public void setRouterNextStepId(String routerNextStepId) {
        this.routerNextStepId = routerNextStepId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
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

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getEoDataCollect() {
        return eoDataCollect;
    }

    public void setEoDataCollect(String eoDataCollect) {
        this.eoDataCollect = eoDataCollect;
    }
}
