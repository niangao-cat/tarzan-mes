package tarzan.actual.domain.vo;

import java.io.Serializable;

import tarzan.actual.domain.entity.MtEoComponentActualHis;

/**
 * @author Leeloing
 * @date 2019/3/12 16:19
 */
public class MtEoComponentActualVO3 extends MtEoComponentActualHis implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5744763780429781626L;

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    private String eventTypeCode;
}
