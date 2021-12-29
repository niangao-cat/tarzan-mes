package tarzan.order.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO42 implements Serializable {

    private static final long serialVersionUID = -182942108042554518L;
    
    @ApiModelProperty(value = "组件ID")
    private String bomComponentId;
    @ApiModelProperty(value = "序号")
    private Long lineNumber;
    @JsonIgnore
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "需求数量")
    private Double componentQty;
    @ApiModelProperty(value = "需求数量")
    private Double uom;
    @ApiModelProperty(value = "损耗数量")
    private String lossdQty;
    @ApiModelProperty(value = "损耗数量")
    private Double lossQty;

    @ApiModelProperty(value = "单位")
    private String primaryUomCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "组件编码")
    private String materialCode;
    @ApiModelProperty(value = "组件名称")
    private String materialName;
    @ApiModelProperty(value = "组件类型编码")
    private String bomComponentType;
    @ApiModelProperty(value = "组件类型描述")
    private String bomComponentTypeDesc;
    @ApiModelProperty(value = "装配方式")
    private String assembleMethod;
    @ApiModelProperty(value = "装配方式描述")
    private String assembleMethodDesc;
    @ApiModelProperty(value = "来源库位")
    private String issuedLocatorId;
    @ApiModelProperty(value = "来源库位编码")
    private String issuedLocatorCode;
    @ApiModelProperty(value = "关键物料标识")
    private String keyMaterialFlag;
    @ApiModelProperty(value = "替代数量")
    private Double substituteQty;
    @ApiModelProperty(value = "装配数量")
    private Double assembleQty;
    @ApiModelProperty(value = "报废数量")
    private Double scrappedQty;
    @ApiModelProperty(value = "步骤顺序")
    private Long sequence;
    @ApiModelProperty(value = "步骤识别码")
    private String step;
    @ApiModelProperty(value = "步骤描述")
    private String stepDesc;
    @ApiModelProperty(value = "强制装配标识")
    private String assembleExcessFlag;
    @ApiModelProperty(value = "项目编号")
    private String lineAttribute10;
    @ApiModelProperty(value = "替代组件")
    private List<MtWorkOrderVO44> bomSubstituteList;
    @ApiModelProperty(value = "组件版本/卸货点")
    private String bomVersion;
    @ApiModelProperty(value = "反冲标识")
    private String recoilFlag;
    @ApiModelProperty(value = "物料生产类型")
    private String productionType;
    @ApiModelProperty(value = "SN升级标志")
    private String upgradeFlag;
    @ApiModelProperty(value = "总需求数")
    private BigDecimal totalComponentQty;
    @ApiModelProperty(value = "损耗百分比")
    private BigDecimal attritionChance;
    public Long getLineNumber() {
        return lineNumber;
    }
    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }
    public Double getComponentQty() {
        return componentQty;
    }
    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }
    public String getMaterialId() {
        return materialId;
    }
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
    public String getMaterialCode() {
        return materialCode;
    }
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    public String getMaterialName() {
        return materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public String getBomComponentId() {
        return bomComponentId;
    }
    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
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
    public Double getSubstituteQty() {
        return substituteQty;
    }
    public void setSubstituteQty(Double substituteQty) {
        this.substituteQty = substituteQty;
    }
    public Double getAssembleQty() {
        return assembleQty;
    }
    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
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
    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }
    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }
    public Double getQty() {
        return qty;
    }
    public void setQty(Double qty) {
        this.qty = qty;
    }
    public Double getScrappedQty() {
        return scrappedQty;
    }
    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public List<MtWorkOrderVO44> getBomSubstituteList() {
        return bomSubstituteList;
    }

    public void setBomSubstituteList(List<MtWorkOrderVO44> bomSubstituteList) {
        this.bomSubstituteList = bomSubstituteList;
    }

    public Double getUom() {
        return uom;
    }

    public void setUom(Double uom) {
        this.uom = uom;
    }

    public Double getLossQty() {
        return lossQty;
    }

    public void setLossQty(Double lossQty) {
        this.lossQty = lossQty;
    }

    public String getLossdQty() {
        return lossdQty;
    }

    public void setLossdQty(String lossdQty) {
        this.lossdQty = lossdQty;
    }


    public String getPrimaryUomCode() {
        return primaryUomCode;
    }

    public void setPrimaryUomCode(String primaryUomCode) {
        this.primaryUomCode = primaryUomCode;
    }

    public String getLineAttribute10() {
        return this.lineAttribute10;
    }

    public void setLineAttribute10(final String lineAttribute10) {
        this.lineAttribute10 = lineAttribute10;
    }

    public String getBomVersion() {
        return bomVersion;
    }

    public void setBomVersion(String bomVersion) {
        this.bomVersion = bomVersion;
    }

    public String getRecoilFlag() {
        return recoilFlag;
    }

    public void setRecoilFlag(String recoilFlag) {
        this.recoilFlag = recoilFlag;
    }

    public String getProductionType() {
        return productionType;
    }

    public void setProductionType(String productionType) {
        this.productionType = productionType;
    }

    public String getUpgradeFlag() {
        return upgradeFlag;
    }

    public void setUpgradeFlag(String upgradeFlag) {
        this.upgradeFlag = upgradeFlag;
    }

    public BigDecimal getTotalComponentQty() {
        return totalComponentQty;
    }

    public void setTotalComponentQty(BigDecimal totalComponentQty) {
        this.totalComponentQty = totalComponentQty;
    }

    public BigDecimal getAttritionChance() {
        return attritionChance;
    }

    public void setAttritionChance(BigDecimal attritionChance) {
        this.attritionChance = attritionChance;
    }
}
