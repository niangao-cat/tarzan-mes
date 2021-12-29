package tarzan.order.api.dto;


import java.io.Serializable;



public class MtWorkOrderDTO6 implements Serializable {

    private static final long serialVersionUID = 1159655016005196442L;
    private String workOrderId;
    private String bomId;
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getBomId() {
        return bomId;
    }
    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtWorkOrderDTO6 [workOrderId=");
        builder.append(workOrderId);
        builder.append(", bomId=");
        builder.append(bomId);
        builder.append("]");
        return builder.toString();
    }
    
    
}
