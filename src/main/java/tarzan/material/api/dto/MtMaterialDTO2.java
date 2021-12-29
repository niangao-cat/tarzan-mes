package tarzan.material.api.dto;

import java.io.Serializable;

public class MtMaterialDTO2 implements Serializable {
    private static final long serialVersionUID = 5040480271781715241L;

    private String primaryUomId;
    private String secondaryUomId;
    private String primaryUomCode;
    private String secondaryUomCode;
    private String enable;

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

    public String getPrimaryUomCode() {
        return primaryUomCode;
    }

    public void setPrimaryUomCode(String primaryUomCode) {
        this.primaryUomCode = primaryUomCode;
    }

    public String getSecondaryUomCode() {
        return secondaryUomCode;
    }

    public void setSecondaryUomCode(String secondaryUomCode) {
        this.secondaryUomCode = secondaryUomCode;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
