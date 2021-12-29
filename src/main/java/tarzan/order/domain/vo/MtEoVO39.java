package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/11/26 17:57
 * @Description:
 */
public class MtEoVO39 implements Serializable {
    private static final long serialVersionUID = 6195872423305080271L;

    @ApiModelProperty("EO信息集合")
    private List<MtEoVO38> eoMessageList;
    @ApiModelProperty("事件ID")
    private String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtEoVO38> getEoMessageList() {
        return eoMessageList;
    }

    public void setEoMessageList(List<MtEoVO38> eoMessageList) {
        this.eoMessageList = eoMessageList;
    }
}

