package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2019/9/17 21:20
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtEoStepActualVO29 implements Serializable {

    private static final long serialVersionUID = -6550729817399304062L;

    /**
     * 事件ID
     */
    private String eventId;
    /**
     * 主键历史ID
     */
    private String eoStepActualHisId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEoStepActualHisId() {
        return eoStepActualHisId;
    }

    public void setEoStepActualHisId(String eoStepActualHisId) {
        this.eoStepActualHisId = eoStepActualHisId;
    }
}