package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleGroupActualVO3 implements Serializable {

    private static final long serialVersionUID = -9134341996317559888L;
    private String assembleGroupId;
    private String materialId;
    private String materialLotId;

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
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

}
