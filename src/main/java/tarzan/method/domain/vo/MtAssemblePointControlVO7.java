package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssemblePointControlVO7
 * @description
 * @date 2019年10月09日 16:02
 */
public class MtAssemblePointControlVO7 implements Serializable {
    private static final long serialVersionUID = -8568894105774026710L;

    private String assemblePointControlId;// 装配点控制ID
    private String assembleControlId;// 装配控制ID
    private String assemblePointId;// 装配点ID
    private String assemblePointCode;// 装配点编码
    private String assemblePointDescription;// 装配点描述
    private String materialId;// 物料ID
    private String materialCode;// 物料编码
    private String materialName;// 物料描述
    private String materialLotId;// 物料批ID
    private String materialLotCode;// 物料批编码
    private Double unitQty;// 装配单位用量
    private String referencePoint;// 参考点
    private String enableFlag;// 有效性

    public String getAssemblePointControlId() {
        return assemblePointControlId;
    }

    public void setAssemblePointControlId(String assemblePointControlId) {
        this.assemblePointControlId = assemblePointControlId;
    }

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getAssemblePointCode() {
        return assemblePointCode;
    }

    public void setAssemblePointCode(String assemblePointCode) {
        this.assemblePointCode = assemblePointCode;
    }

    public String getAssemblePointDescription() {
        return assemblePointDescription;
    }

    public void setAssemblePointDescription(String assemblePointDescription) {
        this.assemblePointDescription = assemblePointDescription;
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

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }

    public Double getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(Double unitQty) {
        this.unitQty = unitQty;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
