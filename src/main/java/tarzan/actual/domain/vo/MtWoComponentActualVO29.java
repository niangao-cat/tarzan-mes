package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: changbu  2020/10/30 14:46
 */
public class MtWoComponentActualVO29 implements Serializable {

    private static final long serialVersionUID = -2013766312233583983L;

    private String eventId;
    private String operationId;
    private List<MtWoComponentActualVO30> woInfoList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<MtWoComponentActualVO30> getWoInfoList() {
        return woInfoList;
    }

    public void setWoInfoList(List<MtWoComponentActualVO30> woInfoList) {
        this.woInfoList = woInfoList;
    }
}
