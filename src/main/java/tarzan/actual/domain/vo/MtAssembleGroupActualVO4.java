package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtAssembleGroupActualVO4 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3198751323272585358L;
    private String workcellId;
    private String eoId;
    private String referenceArea;
    private String assembleGroupId;

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

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

}
