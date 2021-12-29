package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tang xiao
 * @date 2020-08-31 15:36
 */
public class MtContLoadDtlVO22 implements Serializable {

    private static final long serialVersionUID = -7594534263153994735L;
    private String eventId;
    private List<MtContLoadDtlVO23> mtContLoadDtlList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtContLoadDtlVO23> getMtContLoadDtlList() {
        return mtContLoadDtlList;
    }

    public void setMtContLoadDtlList(List<MtContLoadDtlVO23> mtContLoadDtlList) {
        this.mtContLoadDtlList = mtContLoadDtlList;
    }
}
