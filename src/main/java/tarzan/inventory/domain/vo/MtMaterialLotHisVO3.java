package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2019/9/29 0:12
 * @Author: ${yiyang.xie}
 */
public class MtMaterialLotHisVO3 implements Serializable {
    private static final long serialVersionUID = 8616184409558287269L;
    /**
     * 物料批历史ID
     */
    private String materialLotHisId;
    /**
     * 事件ID
     */
    private String eventId;

    public String getMaterialLotHisId() {
        return materialLotHisId;
    }

    public void setMaterialLotHisId(String materialLotHisId) {
        this.materialLotHisId = materialLotHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
