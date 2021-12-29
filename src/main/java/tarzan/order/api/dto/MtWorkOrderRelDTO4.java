package tarzan.order.api.dto;


import java.io.Serializable;


public class MtWorkOrderRelDTO4 implements Serializable {

    private static final long serialVersionUID = 1159655016005196442L;
    
    private String workOrderId; //生产指令ID
   
    private String relType;   //关系类型
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getRelType() {
        return relType;
    }
    public void setRelType(String relType) {
        this.relType = relType;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtWorkOrderRelDTO [workOrderId=");
        builder.append(workOrderId);
        builder.append(", relType=");
        builder.append(relType);
        builder.append("]");
        return builder.toString();
    }
    
}
