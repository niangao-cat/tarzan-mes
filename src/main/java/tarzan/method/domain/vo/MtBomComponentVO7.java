package tarzan.method.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import tarzan.iface.domain.entity.MtBomComponentIface;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MtBomComponentVO7 implements Serializable {
    private static final long serialVersionUID = 4966726776288494628L;

    private String bomComponentId;
    private String bomId;
    private Long lineNumber;
    private String materialId;
    private String bomComponentType;
    private Date dateFrom;
    private Date dateTo;
    private Double qty;
    private String keyMaterialFlag;
    private String assembleMethod;
    private String assembleAsReqFlag;
    private String attritionPolicy;
    private Double attritionChance;
    private Double attritionQty;
    private String copiedFromComponentId;
    private String issuedLocatorId;
    @ApiModelProperty(value = "接口对象-行", hidden = true)
    @JsonIgnore
    private MtBomComponentIface bomComponentIface;

    private List<MtBomReferencePointVO9> mtBomReferencePointList;
    private List<MtBomSubstituteGroupVO4> mtBomSubstituteGroupList;

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

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public Date getDateFrom() {
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getKeyMaterialFlag() {
        return keyMaterialFlag;
    }

    public void setKeyMaterialFlag(String keyMaterialFlag) {
        this.keyMaterialFlag = keyMaterialFlag;
    }

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public String getAssembleAsReqFlag() {
        return assembleAsReqFlag;
    }

    public void setAssembleAsReqFlag(String assembleAsReqFlag) {
        this.assembleAsReqFlag = assembleAsReqFlag;
    }

    public String getAttritionPolicy() {
        return attritionPolicy;
    }

    public void setAttritionPolicy(String attritionPolicy) {
        this.attritionPolicy = attritionPolicy;
    }

    public Double getAttritionChance() {
        return attritionChance;
    }

    public void setAttritionChance(Double attritionChance) {
        this.attritionChance = attritionChance;
    }

    public Double getAttritionQty() {
        return attritionQty;
    }

    public void setAttritionQty(Double attritionQty) {
        this.attritionQty = attritionQty;
    }

    public String getCopiedFromComponentId() {
        return copiedFromComponentId;
    }

    public void setCopiedFromComponentId(String copiedFromComponentId) {
        this.copiedFromComponentId = copiedFromComponentId;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public List<MtBomReferencePointVO9> getMtBomReferencePointList() {
        return mtBomReferencePointList;
    }

    public void setMtBomReferencePointList(List<MtBomReferencePointVO9> mtBomReferencePointList) {
        this.mtBomReferencePointList = mtBomReferencePointList;
    }

    public List<MtBomSubstituteGroupVO4> getMtBomSubstituteGroupList() {
        return mtBomSubstituteGroupList;
    }

    public void setMtBomSubstituteGroupList(List<MtBomSubstituteGroupVO4> mtBomSubstituteGroupList) {
        this.mtBomSubstituteGroupList = mtBomSubstituteGroupList;
    }

    public MtBomComponentIface getBomComponentIface() {
        return bomComponentIface;
    }

    public void setBomComponentIface(MtBomComponentIface bomComponentIface) {
        this.bomComponentIface = bomComponentIface;
    }
}
