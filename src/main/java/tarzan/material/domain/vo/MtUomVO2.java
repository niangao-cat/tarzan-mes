package tarzan.material.domain.vo;

import java.io.Serializable;

public class MtUomVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8766864317396736214L;

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    private String uomId;

    public String getTargetUomId() {
        return targetUomId;
    }

    public void setTargetUomId(String targetUomId) {
        this.targetUomId = targetUomId;
    }

    private String targetUomId;

}
