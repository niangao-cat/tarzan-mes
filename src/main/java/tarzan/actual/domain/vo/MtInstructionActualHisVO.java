package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtInstructionActualHisVO
 * @description
 * @date 2019年09月28日 11:56
 */
public class MtInstructionActualHisVO implements Serializable {
    private static final long serialVersionUID = 7333373968094413002L;
    private String actualHisId;//	指令实绩历史ID
    private String eventId;//	事件ID

    public String getActualHisId() {
        return actualHisId;
    }

    public void setActualHisId(String actualHisId) {
        this.actualHisId = actualHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
