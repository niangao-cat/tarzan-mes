package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * Created by slj on 2018-09-25.
 */
public class MtUomVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -9027033492896251268L;
    private String materialId;
    private Double sourceValue;
    private Double targetValue;
    private String sourceUomId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Double getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(Double sourceValue) {
        this.sourceValue = sourceValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public String getSourceUomId() {
        return sourceUomId;
    }

    public void setSourceUomId(String sourceUomId) {
        this.sourceUomId = sourceUomId;
    }

}
