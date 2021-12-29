package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/1 14:29
 * @Description:
 */
public class MtAssembleConfirmActualVO12 implements Serializable {
    private static final long serialVersionUID = -25216014696738569L;

    private String eoId;
    private String materialId;
    private String workcellId;
    private String operationId;
    private String substituteFlag;
    private String assembleExcessFlag;
    private String isAllLayer;

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

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getIsAllLayer() {
        return isAllLayer;
    }

    public void setIsAllLayer(String isAllLayer) {
        this.isAllLayer = isAllLayer;
    }
}
