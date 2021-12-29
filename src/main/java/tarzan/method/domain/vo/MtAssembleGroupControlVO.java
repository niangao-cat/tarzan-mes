package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtAssembleGroupControlVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3284342254069723477L;
    private String assembleControlId;
    private String workcellId;

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

}
