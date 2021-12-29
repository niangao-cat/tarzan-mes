package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleGroupActualVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7078526065252299381L;
    private String workcellId;
    private String eoId;
    private String referenceArea;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getReferenceArea() {
        return referenceArea;
    }

    public void setReferenceArea(String referenceArea) {
        this.referenceArea = referenceArea;
    }

    @Override
    public String toString() {
        return "MtAssembleGroupActualVO{" + "workcellId='" + workcellId + '\'' + ", eoId='" + eoId + '\''
                        + ", referenceArea='" + referenceArea + '\'' + '}';
    }
}
