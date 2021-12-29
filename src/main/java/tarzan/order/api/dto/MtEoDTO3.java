package tarzan.order.api.dto;


import java.io.Serializable;



public class MtEoDTO3 implements Serializable {

    private static final long serialVersionUID = 1159655016005196442L;
    private String eoId;    //执行作业ID
    private String targetStatus;   //生产指令ID
    public String getEoId() {
        return eoId;
    }
    public void setEoId(String eoId) {
        this.eoId = eoId;
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
        builder.append("MtEoDTO3 [eoId=");
        builder.append(eoId);
        builder.append(", targetStatus=");
        builder.append(targetStatus);
        builder.append("]");
        return builder.toString();
    }
    
}
