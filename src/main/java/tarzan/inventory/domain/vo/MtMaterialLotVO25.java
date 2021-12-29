package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-10-25 14:33
 */
public class MtMaterialLotVO25 implements Serializable {
    private static final long serialVersionUID = -2056778504868798013L;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("物料批集合")
    private List<MtMaterialLotVO20> materialLots;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtMaterialLotVO20> getMaterialLots() {
        return materialLots;
    }

    public void setMaterialLots(List<MtMaterialLotVO20> materialLots) {
        this.materialLots = materialLots;
    }
}
