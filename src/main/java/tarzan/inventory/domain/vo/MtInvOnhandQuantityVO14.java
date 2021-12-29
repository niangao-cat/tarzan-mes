package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtInvOnhandQuantityVO14 implements Serializable {


    private static final long serialVersionUID = 6300257908744778028L;
    @ApiModelProperty("事件类型编码")
    private String eventTypeCode;

    @ApiModelProperty("事件类型编码")
    private List<MtInvOnhandQuantityVO13> onhandList;

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public List<MtInvOnhandQuantityVO13> getOnhandList() {
        return onhandList;
    }

    public void setOnhandList(List<MtInvOnhandQuantityVO13> onhandList) {
        this.onhandList = onhandList;
    }
}
