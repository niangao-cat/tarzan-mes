package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2020-10-30 15:59
 **/
public class MtAssembleProcessActualVO11 implements Serializable {
    private static final long serialVersionUID = 2165544203653781792L;

    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("装配清单组件ID")
    private String bomComponentId;
    @ApiModelProperty("本次装配数量")
    private Double trxAssembleQty;
    @ApiModelProperty("强制装配标识")
    private String assembleExcessFlag;
    @ApiModelProperty("装配货位")
    private String locatorId;
    @ApiModelProperty("装配方式")
    private String assembleMethod;
    @ApiModelProperty("装配点")
    private String assemblePointId;
    @ApiModelProperty("装配点")
    private String assembleGroupId;
    @ApiModelProperty("参考区域")
    private String referenceArea;
    @ApiModelProperty("参考点")
    private String referencePoint;
    @ApiModelProperty("物料批")
    private String materialLotId;
    @ApiModelProperty("替代标识")
    private String substituteFlag;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getTrxAssembleQty() {
        return trxAssembleQty;
    }

    public void setTrxAssembleQty(Double trxAssembleQty) {
        this.trxAssembleQty = trxAssembleQty;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getReferenceArea() {
        return referenceArea;
    }

    public void setReferenceArea(String referenceArea) {
        this.referenceArea = referenceArea;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }
}
