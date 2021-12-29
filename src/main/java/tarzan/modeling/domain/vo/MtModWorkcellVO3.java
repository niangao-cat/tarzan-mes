package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModWorkcellVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2914719493103920437L;
    private String workcellId;
    private String workcellCode;
    private String workcellName;
    private String description;
    private String workcellType;
    private String workcellTypeDesc;
    private String workcellLocation;
    private String enableFlag;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkcellType() {
        return workcellType;
    }

    public void setWorkcellType(String workcellType) {
        this.workcellType = workcellType;
    }

    public String getWorkcellTypeDesc() {
        return workcellTypeDesc;
    }

    public void setWorkcellTypeDesc(String workcellTypeDesc) {
        this.workcellTypeDesc = workcellTypeDesc;
    }

    public String getWorkcellLocation() {
        return workcellLocation;
    }

    public void setWorkcellLocation(String workcellLocation) {
        this.workcellLocation = workcellLocation;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }


}
