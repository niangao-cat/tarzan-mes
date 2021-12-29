package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tangxiao
 */
public class MtAssembleConfirmActualVO24 implements Serializable {

    private static final long serialVersionUID = -7546194550666981255L;
    private List<MtAssembleConfirmActualVO25> actualList;
    private String bypassFlag;
    private String bypassBy;
    private String confirmFlag;
    private String confirmedBy;
    private String eventId;

    public List<MtAssembleConfirmActualVO25> getActualList() {
        return actualList;
    }

    public void setActualList(List<MtAssembleConfirmActualVO25> actualList) {
        this.actualList = actualList;
    }

    public String getBypassFlag() {
        return bypassFlag;
    }

    public void setBypassFlag(String bypassFlag) {
        this.bypassFlag = bypassFlag;
    }

    public String getBypassBy() {
        return bypassBy;
    }

    public void setBypassBy(String bypassBy) {
        this.bypassBy = bypassBy;
    }

    public String getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(String confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}


