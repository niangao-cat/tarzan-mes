package tarzan.order.api.dto;


import java.io.Serializable;



public class MtEoDTO2 implements Serializable {


    private static final long serialVersionUID = 4290283009285094905L;
    private String eoId;    //执行作业ID
    private String workOrderId;   //生产指令ID
    public String getEoId() {
        return eoId;
    }
    public void setEoId(String eoId) {
        this.eoId = eoId;
    }
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtEoDTO2 [eoId=");
        builder.append(eoId);
        builder.append(", workOrderId=");
        builder.append(workOrderId);
        builder.append("]");
        return builder.toString();
    }
    
    
    

}
