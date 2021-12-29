package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/22 17:23
 * @Author: ${yiyang.xie}
 */
public class MtEoComponentActualVO27 implements Serializable {
    private static final long serialVersionUID = 4365826959175460019L;

    @ApiModelProperty("执行作业组件实绩ID")
    private String eoComponentActualId;
    @ApiModelProperty("执行作业")
    private String eoId;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("工艺")
    private String operationId;
    @ApiModelProperty("装配数量")
    private Double assembleQty;
    @ApiModelProperty("本次装配数量")
    private Double trxAssembleQty;
    @ApiModelProperty("报废数量")
    private Double scrappedQty;
    @ApiModelProperty("本次报废数量")
    private Double trxScrappedQty;
    @ApiModelProperty("组件类型")
    private String componentType;
    @ApiModelProperty("组件ID")
    private String bomComponentId;
    @ApiModelProperty("装配时装配清单ID")
    private String bomId;
    @ApiModelProperty("装配时步骤ID")
    private String routerStepId;
    @ApiModelProperty("是否强制装配")
    private String assembleExcessFlag;
    @ApiModelProperty("装配工艺路线类型")
    private String assembleRouterType;
    @ApiModelProperty("是否为替代装配")
    private String substituteFlag;
    @ApiModelProperty("第一次装配时间")
    private Date actualFirstTime;
    @ApiModelProperty("最后一次装配时间")
    private Date actualLastTime;

    public String getEoComponentActualId() {
        return eoComponentActualId;
    }

    public void setEoComponentActualId(String eoComponentActualId) {
        this.eoComponentActualId = eoComponentActualId;
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

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public Double getTrxAssembleQty() {
        return trxAssembleQty;
    }

    public void setTrxAssembleQty(Double trxAssembleQty) {
        this.trxAssembleQty = trxAssembleQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public Double getTrxScrappedQty() {
        return trxScrappedQty;
    }

    public void setTrxScrappedQty(Double trxScrappedQty) {
        this.trxScrappedQty = trxScrappedQty;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public Date getActualFirstTime() {
        if (actualFirstTime != null) {
            return (Date) actualFirstTime.clone();
        } else {
            return null;
        }
    }

    public void setActualFirstTime(Date actualFirstTime) {
        if (actualFirstTime == null) {
            this.actualFirstTime = null;
        } else {
            this.actualFirstTime = (Date) actualFirstTime.clone();
        }
    }

    public Date getActualLastTime() {
        if (actualLastTime != null) {
            return (Date) actualLastTime.clone();
        } else {
            return null;
        }
    }

    public void setActualLastTime(Date actualLastTime) {
        if (actualLastTime == null) {
            this.actualLastTime = null;
        } else {
            this.actualLastTime = (Date) actualLastTime.clone();
        }
    }
    public MtEoComponentActualVO27() {}

    public MtEoComponentActualVO27(String eoId, String materialId, String componentType, String bomComponentId, String bomId,
                                   String routerStepId) {
        this.eoId = eoId;
        this.materialId = materialId;
        this.componentType = componentType;
        this.bomComponentId = bomComponentId;
        this.bomId = bomId;
        this.routerStepId = routerStepId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoComponentActualVO27 that = (MtEoComponentActualVO27) o;
        return Objects.equals(eoId, that.eoId) && Objects.equals(materialId, that.materialId)
                && Objects.equals(componentType, that.componentType)
                && Objects.equals(bomComponentId, that.bomComponentId) && Objects.equals(bomId, that.bomId)
                && Objects.equals(routerStepId, that.routerStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eoId, materialId, componentType, bomComponentId, bomId, routerStepId);
    }
}
