package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtBomComponentVO9 implements Serializable {

    private static final long serialVersionUID = 2695578336871995033L;
    private String bomComponentId; // 主键
    private Long lineNumber; // 序号
    private String materialId; // 物料组件id
    private String bomComponentType; // 组件类型
    private Date dateFrom; // 生效时间
    private Date dateTo; // 失效生产
    private Double qty; // 数量，六位小数
    private String keyMaterialFlag; // 关键物料标识
    private String assembleMethod; // 装配方式(投料/上料位反冲/库存反冲)
    private String assembleAsReqFlag; // 是否按需求数量装配
    private String attritionPolicy; // 损耗策略，1按固定值，2按百分比，3固定值+百分比
    private Double attritionChance; // 损耗百分比，两位小数
    private Double attritionQty; // 固定损耗值，六位小数
    private String issuedLocatorId;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
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

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

}
