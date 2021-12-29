package tarzan.method.api.dto;

import java.io.Serializable;

public class MtAssemblePointControlDTO implements Serializable {

    private static final long serialVersionUID = 8352661687102215951L;
    private String assembleControlId;// 装配控制ID
    private String assemblePointId;// 装配点ID
    private String materialId;// 要装载的物料ID
    private String materialLotId;// 要装载的物料批ID
    private String referencePoint;// 要装载参考点

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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomComponentDTO3 [assembleControlId=");
        builder.append(assembleControlId);
        builder.append(", assemblePointId=");
        builder.append(assemblePointId);
        builder.append(", materialId=");
        builder.append(materialId);
        builder.append(", materialLotId=");
        builder.append(materialLotId);
        builder.append(", referencePoint=");
        builder.append(referencePoint);
        builder.append("]");
        return builder.toString();
    }

}
