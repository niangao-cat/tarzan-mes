package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 15:41
 * @Description:
 */
public class MtEoRouterActualVO38 implements Serializable {
    private static final long serialVersionUID = -6081580961344837117L;

    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("EO工艺路线实绩数据集合")
    private List<MtEoRouterActualVO37> eoRouterActualList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtEoRouterActualVO37> getEoRouterActualList() {
        return eoRouterActualList;
    }

    public void setEoRouterActualList(List<MtEoRouterActualVO37> eoRouterActualList) {
        this.eoRouterActualList = eoRouterActualList;
    }
}
