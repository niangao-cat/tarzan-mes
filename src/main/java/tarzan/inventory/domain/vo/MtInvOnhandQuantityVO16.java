package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtInvOnhandQuantityVO16 implements Serializable {


    private static final long serialVersionUID = 9041815972596216268L;
    @ApiModelProperty("事件ID")
    private  String eventId;

    @ApiModelProperty("库存数量列表")
    private List<MtInvOnhandQuantityVO13> onhandList;

    public List<MtInvOnhandQuantityVO13> getOnhandList() {
        return onhandList;
    }

    public void setOnhandList(List<MtInvOnhandQuantityVO13> onhandList) {
        this.onhandList = onhandList;
    }

    public  String getEventId() {
        return eventId;
    }

    public void setEventId( String eventId) {
        this.eventId = eventId;
    }
}
