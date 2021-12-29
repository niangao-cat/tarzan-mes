package tarzan.material.api.dto;

import java.io.Serializable;

public class MtUomDTO implements Serializable {
    private static final long serialVersionUID = 2241374506901294570L;
    private String locale;
    private String uomId;
    private String uomTypeDesc;
    private String uomType;

    private String uomCode;
    private String uomName;
    private String enableFlag;
    private String primaryFlag;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getUomTypeDesc() {
        return uomTypeDesc;
    }

    public void setUomTypeDesc(String uomTypeDesc) {
        this.uomTypeDesc = uomTypeDesc;
    }

    public String getUomType() {
        return uomType;
    }

    public void setUomType(String uomType) {
        this.uomType = uomType;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }
}
