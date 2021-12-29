package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/20 21:15
 */
public class MtMaterialVO2 implements Serializable {
    private static final long serialVersionUID = 5556723730892860742L;

    private String materialId;
    private String primaryUomId;
    private String secondaryUomId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }
}
