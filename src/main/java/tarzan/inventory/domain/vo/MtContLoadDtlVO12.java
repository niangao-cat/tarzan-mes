package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019-10-22 17:36
 */
public class MtContLoadDtlVO12 implements Serializable {
    private static final long serialVersionUID = 227033901249809317L;
    private String eventId;
    List<MtContLoadDtlVO16> mtContLoadDtlList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtContLoadDtlVO16> getMtContLoadDtlList() {
        return mtContLoadDtlList;
    }

    public void setMtContLoadDtlList(List<MtContLoadDtlVO16> mtContLoadDtlList) {
        this.mtContLoadDtlList = mtContLoadDtlList;
    }
}
