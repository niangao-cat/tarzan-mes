package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtEoHisVO2
 * @description
 * @date 2019年09月28日 11:20
 */
public class MtEoHisVO2 implements Serializable {
    private static final long serialVersionUID = -7313273770421389338L;
    private String eoHisId;	//执行作业历史ID
    private String eventId;	//事件ID

    public String getEoHisId() {
        return eoHisId;
    }

    public void setEoHisId(String eoHisId) {
        this.eoHisId = eoHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
