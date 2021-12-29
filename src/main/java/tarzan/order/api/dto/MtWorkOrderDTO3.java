package tarzan.order.api.dto;


import java.io.Serializable;


public class MtWorkOrderDTO3 implements Serializable {

    private static final long serialVersionUID = -1128301526084982196L;

    private String workOrderId;    //生产指令ID
    private String eventId;        //事件ID
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
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtWorkOrderDTO3 [workOrderId=");
        builder.append(workOrderId);
        builder.append(", eventId=");
        builder.append(eventId);
        builder.append(", bomId=");
        builder.append(bomId);
        builder.append("]");
        return builder.toString();
    }
    
}
