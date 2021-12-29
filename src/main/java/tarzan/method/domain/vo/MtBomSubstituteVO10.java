package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtBomSubstituteVO10 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6091845147967180235L;
    private String bomSubstituteId;
    private List<String> bomSubstituteHisId;

    public String getBomSubstituteId() {
        return bomSubstituteId;
    }

    public void setBomSubstituteId(String bomSubstituteId) {
        this.bomSubstituteId = bomSubstituteId;
    }

    public List<String> getBomSubstituteHisId() {
        return bomSubstituteHisId;
    }

    public void setBomSubstituteHisId(List<String> bomSubstituteHisId) {
        this.bomSubstituteHisId = bomSubstituteHisId;
    }

}
