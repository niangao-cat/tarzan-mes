package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 17:09
 * @Description:
 */
public class MtEoStepActualVO40 implements Serializable {
    private static final long serialVersionUID = -804832158780552550L;

    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("更新数据集合")
    private List<MtEoStepActualVO39> eoStepActualList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtEoStepActualVO39> getEoStepActualList() {
        return eoStepActualList;
    }

    public void setEoStepActualList(List<MtEoStepActualVO39> eoStepActualList) {
        this.eoStepActualList = eoStepActualList;
    }
}
