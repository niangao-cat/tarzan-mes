package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtAssemblePointControlVO5 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6483618903975468132L;
    private String assembleControlId;
    private String assembleGroupId;
    private String materialId;

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

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

}
