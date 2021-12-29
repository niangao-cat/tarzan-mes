package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * Created by slj on 2018-11-19.
 */
public class MtUomVO1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4405161859551902232L;
    private Double sourceValue;
    private Double targetValue;
    private String sourceUomId;
    private String targetUomId;

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

    public String getTargetUomId() {
        return targetUomId;
    }

    public void setTargetUomId(String targetUomId) {
        this.targetUomId = targetUomId;
    }

}
