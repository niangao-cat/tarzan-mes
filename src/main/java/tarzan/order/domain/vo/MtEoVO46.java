package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/29 19:07
 */
public class MtEoVO46 implements Serializable {
    private static final long serialVersionUID = 3542952664729697768L;

    @ApiModelProperty("EO信息集合")
    private List<MtEoVO45> eoMessageList;
    @ApiModelProperty("事件ID")
    private  String eventId;

    public List<MtEoVO45> getEoMessageList() {
        return eoMessageList;
    }

    public void setEoMessageList(List<MtEoVO45> eoMessageList) {
        this.eoMessageList = eoMessageList;
    }

    public  String getEventId() {
        return eventId;
    }

    public void setEventId( String eventId) {
        this.eventId = eventId;
    }
}
