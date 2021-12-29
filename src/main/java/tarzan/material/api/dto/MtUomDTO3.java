package tarzan.material.api.dto;

import java.io.Serializable;

public class MtUomDTO3 implements Serializable {
    private static final long serialVersionUID = 8158871868626798892L;
    private Double sourceValue;
    private String sourceUomId;
    private String targetUomId;

    public Double getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(Double sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getSourceUomId() {
        return sourceUomId;
    }

    public void setSourceUomId(String sourceUomId) {
        this.sourceUomId = sourceUomId;
    }

    public String getTargetUomId() {
        return targetUomId;
    }

    public void setTargetUomId(String targetUomId) {
        this.targetUomId = targetUomId;
    }
}
