package tarzan.general.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtTagGroupHisVO
 * @description
 * @date 2019年10月14日 16:52
 */
public class MtTagGroupHisVO implements Serializable {
    private static final long serialVersionUID = 5160393398254170378L;

    private String tagGroupHisId;// 数据收集组历史ID
    private String eventId;// 事件ID

    public String getTagGroupHisId() {
        return tagGroupHisId;
    }

    public void setTagGroupHisId(String tagGroupHisId) {
        this.tagGroupHisId = tagGroupHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
