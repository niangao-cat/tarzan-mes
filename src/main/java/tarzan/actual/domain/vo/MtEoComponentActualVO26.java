package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/22 17:22
 * @Author: ${yiyang.xie}
 */
public class MtEoComponentActualVO26 implements Serializable {
    private static final long serialVersionUID = -17452591725255294L;
    @ApiModelProperty("批量列表")
    private List<MtEoComponentActualVO27> eoComponentActualList;
    @ApiModelProperty("事件ID")
    private String eventId;

    @ApiModelProperty("工艺")
    private String operationId;


    public List<MtEoComponentActualVO27> getEoComponentActualList() {
        return eoComponentActualList;
    }

    public void setEoComponentActualList(List<MtEoComponentActualVO27> eoComponentActualList) {
        this.eoComponentActualList = eoComponentActualList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
