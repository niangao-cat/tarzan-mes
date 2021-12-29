package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019-10-22 17:32
 */
public class MtContainerVO29 implements Serializable {
    private static final long serialVersionUID = -8640330335741983634L;
    private String eventId;
    List<MtContainerVO32> containerList;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtContainerVO32> getContainerList() {
        return containerList;
    }

    public void setContainerList(List<MtContainerVO32> containerList) {
        this.containerList = containerList;
    }
}
