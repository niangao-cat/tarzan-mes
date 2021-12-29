package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendVO5;



public class MtWorkOrderAttrVO3 implements Serializable {
    private static final long serialVersionUID = -6215238357985324832L;
    private String workOrderId;
    private String eventId;
    private List<MtExtendVO5> attr;

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

    public List<MtExtendVO5> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendVO5> attr) {
        this.attr = attr;
    }
}
