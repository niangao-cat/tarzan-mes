package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssemblePointControlVO6
 * @description
 * @date 2019年10月09日 16:00
 */
public class MtAssemblePointControlVO6 implements Serializable {
    private static final long serialVersionUID = 6018087626932817988L;

    private String assemblePointControlId;// 装配点控制ID
    private String assembleControlId;// 装配控制ID
    private String assemblePointId;// 装配点ID
    private String materialId;// 物料ID
    private String materialLotId;// 物料批ID
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
