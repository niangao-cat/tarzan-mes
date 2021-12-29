package tarzan.material.api.dto;

import java.io.Serializable;

public class MtUomDTO2 implements Serializable {
    private static final long serialVersionUID = 8787344248562479865L;

    private String sourceUomId;
    private String sourceValue;

    public String getSourceUomId() {
        return sourceUomId;
    }

    public void setSourceUomId(String sourceUomId) {
        this.sourceUomId = sourceUomId;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }
}
