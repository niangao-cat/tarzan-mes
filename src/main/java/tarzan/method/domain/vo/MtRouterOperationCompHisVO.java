package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-09-27 11:20
 */
public class MtRouterOperationCompHisVO implements Serializable {

    private static final long serialVersionUID = 328393721925360866L;
    @ApiModelProperty("艺路线步骤组历史ID")
    private String routerOperationComHisId;
    @ApiModelProperty("事件ID")
    private String eventId;

    public String getRouterOperationComHisId() {
        return routerOperationComHisId;
    }

    public void setRouterOperationComHisId(String routerOperationComHisId) {
        this.routerOperationComHisId = routerOperationComHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
