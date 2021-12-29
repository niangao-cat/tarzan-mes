package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author peng.yuan
 * @ClassName MtWorkOrderVO37
 * @description
 * @date 2019年11月29日 14:25
 */
public class MtWorkOrderVO37 implements Serializable {
    private static final long serialVersionUID = 2890609588835658244L;
    private List<MtWorkOrderVO36> woMessageList;
    private String eventId;

    public List<MtWorkOrderVO36> getWoMessageList() {
        return woMessageList;
    }

    public void setWoMessageList(List<MtWorkOrderVO36> woMessageList) {
        this.woMessageList = woMessageList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
