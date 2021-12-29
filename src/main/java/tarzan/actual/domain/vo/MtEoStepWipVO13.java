package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class MtEoStepWipVO13 implements Serializable {

    private static final long serialVersionUID = 5882539874072819409L;

    @ApiModelProperty(value = "事件")
    private String eventId;
    @ApiModelProperty(value = "步驟实绩列表")
    private List<MtEoStepWipVO14> eoStepWipList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtEoStepWipVO14> getEoStepWipList() {
        return eoStepWipList;
    }

    public void setEoStepWipList(List<MtEoStepWipVO14> eoStepWipList) {
        this.eoStepWipList = eoStepWipList;
    }
}


