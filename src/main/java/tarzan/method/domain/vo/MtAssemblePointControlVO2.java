package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtAssemblePointControlVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7892240381153593562L;
    private String assembleControlId;
    private List<String> assemblePointIds;

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    public List<String> getAssemblePointIds() {
        return assemblePointIds;
    }

    public void setAssemblePointIds(List<String> assemblePointIds) {
        this.assemblePointIds = assemblePointIds;
    }

}
