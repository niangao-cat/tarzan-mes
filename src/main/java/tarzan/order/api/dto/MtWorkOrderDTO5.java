package tarzan.order.api.dto;


import java.io.Serializable;



public class MtWorkOrderDTO5 implements Serializable {

    private static final long serialVersionUID = 1159655016005196442L;
    private String workOrderId;    //执行作业ID
    private String targetStatus;   //生产指令ID
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getTargetStatus() {
        return targetStatus;
    }
    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtWorkOrderDTO5 [workOrderId=");
        builder.append(workOrderId);
        builder.append(", targetStatus=");
        builder.append(targetStatus);
        builder.append("]");
        return builder.toString();
    }
    
}
