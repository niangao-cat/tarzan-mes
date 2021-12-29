package tarzan.method.api.dto;

import java.io.Serializable;

public class MtBomDTO2 implements Serializable {


    private static final long serialVersionUID = 8800766281076328741L;
    private String bomId;
    private String status;
    public String getBomId() {
        return bomId;
    }
    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomDTO2 [bomId=");
        builder.append(bomId);
        builder.append(", status=");
        builder.append(status);
        builder.append("]");
        return builder.toString();
    }
    
}
