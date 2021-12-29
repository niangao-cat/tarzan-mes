package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/30 15:14
 * @Description:
 */
public class MtWorkOrderVO67 implements Serializable {
    private static final long serialVersionUID = 7118057805660508667L;

    private List<MtWorkOrderVO66> woUpdateInfoList;
    private String eventId;

    public List<MtWorkOrderVO66> getWoUpdateInfoList() {
        return woUpdateInfoList;
    }

    public void setWoUpdateInfoList(List<MtWorkOrderVO66> woUpdateInfoList) {
        this.woUpdateInfoList = woUpdateInfoList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
