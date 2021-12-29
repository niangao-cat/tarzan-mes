package tarzan.actual.domain.vo;

import java.io.Serializable;

import tarzan.actual.domain.entity.MtWorkOrderActualHis;

/**
 * Created by slj on 2019-02-13.
 */
public class MtWorkOrderActualHisVO2 extends MtWorkOrderActualHis implements Serializable {

    private static final long serialVersionUID = -3460108400970996801L;

    private String eventType;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }



}
