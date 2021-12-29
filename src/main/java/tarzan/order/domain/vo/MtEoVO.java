package tarzan.order.domain.vo;


import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import tarzan.order.domain.entity.MtEo;

public class MtEoVO extends MtEo {

    private static final long serialVersionUID = -4469180292848753817L;
    private String eventId;

    @ApiModelProperty("外部输入编码")
    private String outsideNum;
    @ApiModelProperty("传入值参数列表")
    private List<String> incomingValueList;

    public MtEoVO() {

    }

    public MtEoVO(String eoId, String eventId) {
        this.setEoId(eoId);
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
