package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2019/9/28 19:51
 * @Author: ${yiyang.xie}
 */
public class MtNcRecordHisVO implements Serializable {
    private static final long serialVersionUID = -8821520853578569756L;
    /**
     * 数据收集实绩历史ID
     */
    private String dataRecordHisId;
    /**
     * 事件ID
     */
    private String eventId;

    public String getDataRecordHisId() {
        return dataRecordHisId;
    }

    public void setDataRecordHisId(String dataRecordHisId) {
        this.dataRecordHisId = dataRecordHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
