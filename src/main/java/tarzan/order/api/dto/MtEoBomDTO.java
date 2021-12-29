package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 11:18 上午
 */
public class MtEoBomDTO implements Serializable {
    private static final long serialVersionUID = 1795646751756211893L;
    @ApiModelProperty(value = "序号")
    private Long lineNumber;
    @ApiModelProperty("BOM ID")
    private String bomId;
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
    @ApiModelProperty("需求数量")
    private Double componentQty;
    @ApiModelProperty("装配数量")
    private Double assembleQty;

    @ApiModelProperty("来源库位Id")
    private String issuedLocatorId;
    @ApiModelProperty("来源库位编码")
    private String issuedLocatorCode;

    @ApiModelProperty("关键物料标识")
    private String keyMaterialFlag;
    @ApiModelProperty("替代标识")
    private String substituteFlag;

    @ApiModelProperty(value = "替代数量")
    private Double substituteQty;
    @ApiModelProperty(value = "步骤顺序")
    private Long sequence;
    @ApiModelProperty(value = "步骤识别码")
    private String step;
    @ApiModelProperty(value = "步骤描述")
    private String stepDesc;
    @ApiModelProperty(value = "步骤ID")
    private String routerStepId;

    @ApiModelProperty("替代组件")
    private List<MtEoBomDTO2> substituteList;

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

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public String getIssuedLocatorCode() {
        return issuedLocatorCode;
    }

    public void setIssuedLocatorCode(String issuedLocatorCode) {
        this.issuedLocatorCode = issuedLocatorCode;
    }

    public String getKeyMaterialFlag() {
        return keyMaterialFlag;
    }

    public void setKeyMaterialFlag(String keyMaterialFlag) {
        this.keyMaterialFlag = keyMaterialFlag;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public List<MtEoBomDTO2> getSubstituteList() {
        return substituteList;
    }

    public void setSubstituteList(List<MtEoBomDTO2> substituteList) {
        this.substituteList = substituteList;
    }

    public String getBomComponentTypeDesc() {
        return bomComponentTypeDesc;
    }

    public void setBomComponentTypeDesc(String bomComponentTypeDesc) {
        this.bomComponentTypeDesc = bomComponentTypeDesc;
    }

    public String getAssembleMethodDesc() {
        return assembleMethodDesc;
    }

    public void setAssembleMethodDesc(String assembleMethodDesc) {
        this.assembleMethodDesc = assembleMethodDesc;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Double getSubstituteQty() {
        return substituteQty;
    }

    public void setSubstituteQty(Double substituteQty) {
        this.substituteQty = substituteQty;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
