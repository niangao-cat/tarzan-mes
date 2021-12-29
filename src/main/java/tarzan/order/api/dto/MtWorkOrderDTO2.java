package tarzan.order.api.dto;


import java.io.Serializable;


public class MtWorkOrderDTO2 implements Serializable {

    private static final long serialVersionUID = -1128301526084982196L;

    private String workOrderId;    //生产指令ID
    private String eventId;        //时间ID
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
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
        builder.append("MtWorkOrderDTO2 [workOrderId=");
        builder.append(workOrderId);
        builder.append(", eventId=");
        builder.append(eventId);
        builder.append("]");
        return builder.toString();
    }
    
}
