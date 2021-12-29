package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomSubstituteVO2 implements Serializable {

    private static final long serialVersionUID = -164967246331281530L;
    private String bomId;
    private String bomComponentId;
    private String bomSubstituteGroupId;
    private String bomSubstituteId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }

    public String getBomSubstituteId() {
        return bomSubstituteId;
    }

    public void setBomSubstituteId(String bomSubstituteId) {
        this.bomSubstituteId = bomSubstituteId;
    }

}
