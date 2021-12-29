package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomSubstituteGroupVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -174138931659868724L;
    private String bomSubstituteGroupId;
    private String bomSubstituteId;
    private Double bomSubstituteUsage;

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

    public Double getBomSubstituteUsage() {
        return bomSubstituteUsage;
    }

    public void setBomSubstituteUsage(Double bomSubstituteUsage) {
        this.bomSubstituteUsage = bomSubstituteUsage;
    }

}
