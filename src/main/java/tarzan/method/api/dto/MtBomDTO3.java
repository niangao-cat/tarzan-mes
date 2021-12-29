package tarzan.method.api.dto;

import java.io.Serializable;

public class MtBomDTO3 implements Serializable {


    private static final long serialVersionUID = -8409291943314294351L;
    private String bomId;
    private String releasedFlag;
    public String getBomId() {
        return bomId;
    }
    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
    public String getReleasedFlag() {
        return releasedFlag;
    }
    public void setReleasedFlag(String releasedFlag) {
        this.releasedFlag = releasedFlag;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomDTO3 [bomId=");
        builder.append(bomId);
        builder.append(", releasedFlag=");
        builder.append(releasedFlag);
        builder.append("]");
        return builder.toString();
    }
    
}
