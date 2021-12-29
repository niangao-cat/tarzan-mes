package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtAssemblePointControlVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1526831924168880409L;
    private String assembleControlId;
    private String assemblePointId;

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

}
