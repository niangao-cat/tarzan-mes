package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendVO5;


/**
 * @author Leeloing
 * @date 2019/4/17 10:01
 */
public class MtEoAttrVO3 implements Serializable {
    private static final long serialVersionUID = -4707475927905834137L;

    private String eoId;
    private String eventId;
    private List<MtExtendVO5> attr;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtExtendVO5> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendVO5> attr) {
        this.attr = attr;
    }
}
