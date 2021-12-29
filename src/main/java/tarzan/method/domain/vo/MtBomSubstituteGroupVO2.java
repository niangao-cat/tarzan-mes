package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomSubstituteGroupVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8339544010579312444L;
    private String bomId;
    private String bomComponentId;
    private String bomSubstituteGroupId;

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

}
