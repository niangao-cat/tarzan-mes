package tarzan.inventory.domain.vo;


import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Tangxiao
 */
public class MtMaterialLotVO42 implements Serializable {

    private static final long serialVersionUID = -136349634804431978L;
    @ApiModelProperty("事件ID")
    private String eventId;

    @ApiModelProperty("物料批列表")
    private List<MtMaterialLotVO41> materialLotList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtMaterialLotVO41> getMaterialLotList() {
        return materialLotList;
    }

    public void setMaterialLotList(List<MtMaterialLotVO41> materialLotList) {
        this.materialLotList = materialLotList;
    }
}
