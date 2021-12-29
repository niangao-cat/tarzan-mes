package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/22 14:08
 * @Author: ${yiyang.xie}
 */
public class MtEoBomVO3 implements Serializable {
    private static final long serialVersionUID = -2079126506053684648L;

    @ApiModelProperty("eoBom列表")
    private List<MtEoBomVO4> eoBomList;
    @ApiModelProperty("事件ID")
    private String eventId;

    public List<MtEoBomVO4> getEoBomList() {
        return eoBomList;
    }

    public void setEoBomList(List<MtEoBomVO4> eoBomList) {
        this.eoBomList = eoBomList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
