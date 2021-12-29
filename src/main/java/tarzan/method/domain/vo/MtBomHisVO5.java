package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtBomHisVO5
 * @description
 * @date 2019年09月28日 10:40
 */
public class MtBomHisVO5 implements Serializable {
    private static final long serialVersionUID = -6221053850061533961L;

    private String bomHisId;//	装配清单历史ID
    private String eventId;//	事件ID

    public String getBomHisId() {
        return bomHisId;
    }

    public void setBomHisId(String bomHisId) {
        this.bomHisId = bomHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
