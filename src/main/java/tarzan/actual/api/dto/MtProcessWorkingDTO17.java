package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 0017, 2020-02-17 8:36
 */
public class MtProcessWorkingDTO17 implements Serializable {

    private static final long serialVersionUID = -7075529621225324230L;
    @ApiModelProperty(value = "序号")
    private Long lineNumber;
    @ApiModelProperty("组件ID")
    private String bomComponentId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("组件编码")
    private String bomComponentCode;
    @ApiModelProperty("组件名称")
    private String bomComponentName;
    @ApiModelProperty("组件类型")
    private String bomComponentType;
    @ApiModelProperty("组件类型描述")
    private String bomComponentTypeDesc;
    @ApiModelProperty("装配方式")
    private String assembleMethod;
    @ApiModelProperty("装配方式描述")
    private String assembleMethodDesc;
    @ApiModelProperty("组件数量")
    private Double qty;

    @ApiModelProperty("需求数量")
    private Double componentQty;
    @ApiModelProperty("已移除数量")
    private Double removedQty;

    @ApiModelProperty("条码管控标识")
    private String barcodeControlFlag;

    @ApiModelProperty("行")
    List<MtProcessWorkingDTO18> bomComponentList;

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
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

    public String getBomComponentCode() {
        return bomComponentCode;
    }

    public void setBomComponentCode(String bomComponentCode) {
        this.bomComponentCode = bomComponentCode;
    }

    public String getBomComponentName() {
        return bomComponentName;
    }

    public void setBomComponentName(String bomComponentName) {
        this.bomComponentName = bomComponentName;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public String getBomComponentTypeDesc() {
        return bomComponentTypeDesc;
    }

    public void setBomComponentTypeDesc(String bomComponentTypeDesc) {
        this.bomComponentTypeDesc = bomComponentTypeDesc;
    }

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public String getAssembleMethodDesc() {
        return assembleMethodDesc;
    }

    public void setAssembleMethodDesc(String assembleMethodDesc) {
        this.assembleMethodDesc = assembleMethodDesc;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

    public Double getRemovedQty() {
        return removedQty;
    }

    public void setRemovedQty(Double removedQty) {
        this.removedQty = removedQty;
    }

    public String getBarcodeControlFlag() {
        return barcodeControlFlag;
    }

    public void setBarcodeControlFlag(String barcodeControlFlag) {
        this.barcodeControlFlag = barcodeControlFlag;
    }

    public List<MtProcessWorkingDTO18> getBomComponentList() {
        return bomComponentList;
    }

    public void setBomComponentList(List<MtProcessWorkingDTO18> bomComponentList) {
        this.bomComponentList = bomComponentList;
    }
}
