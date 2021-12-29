package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/14 17:30
 * @Author: ${yiyang.xie}
 */
public class MtContainerHisVO3 implements Serializable {
    private static final long serialVersionUID = -6033039288893152997L;
    @ApiModelProperty("容器历史ID")
    private String containerHisId;
    @ApiModelProperty("事件ID")
    private String eventId;

    public String getContainerHisId() {
        return containerHisId;
    }

    public void setContainerHisId(String containerHisId) {
        this.containerHisId = containerHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
