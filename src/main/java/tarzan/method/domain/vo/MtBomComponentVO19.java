package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/9 16:27
 * @Author: ${yiyang.xie}
 */
public class MtBomComponentVO19 implements Serializable {
    private static final long serialVersionUID = 3009961682804953051L;

    @ApiModelProperty("装配清单组件行ID")
    private String bomComponentId;
    @ApiModelProperty("组件物料ID")
    private String materialId;
    @ApiModelProperty("组件用量")
    private Double componentQty;
    @ApiModelProperty("工艺路线步骤组件主键")
    private String routerOperationComponentId;
    @ApiModelProperty("工艺路线步骤主键")
    private String routerOperationId;
    @ApiModelProperty("工艺路线步骤")
    private String routerStepId;
    @ApiModelProperty("工艺Id")
    private String operationId;
    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("单位用量")
    private Double perQty;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

    public String getRouterOperationComponentId() {
        return routerOperationComponentId;
    }

    public void setRouterOperationComponentId(String routerOperationComponentId) {
        this.routerOperationComponentId = routerOperationComponentId;
    }

    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Double getPerQty() {
        return perQty;
    }

    public void setPerQty(Double perQty) {
        this.perQty = perQty;
    }
}