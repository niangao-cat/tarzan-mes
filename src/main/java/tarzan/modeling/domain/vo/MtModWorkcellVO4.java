package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.Map;

public class MtModWorkcellVO4 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2914719493103920437L;
    private String workcellId;
    private String workcellCode;
    private String workcellName;
    private String description;
    private String workcellType;
    private String workcellLocation;
    private String enableFlag;
    private Map<String, Map<String, String>> _tls;

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

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
