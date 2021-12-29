package tarzan.order.api.dto;


import java.io.Serializable;


public class MtWorkOrderDTO4 implements Serializable {

    private static final long serialVersionUID = -1128301526084982196L;

    private String workOrderId;    //生产指令ID
    private String routerId;        //方法ID
    private String eventId;        
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getRouterId() {
        return routerId;
    }
    public void setRouterId(String routerId) {
        this.routerId = routerId;
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
        builder.append(", routerId=");
        builder.append(routerId);
        builder.append(", eventId=");
        builder.append(eventId);
        builder.append("]");
        return builder.toString();
    }
    
}
