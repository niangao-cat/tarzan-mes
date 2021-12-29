package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cb on 2020-10-26.
 */
public class MtEoStepActualVO48 implements Serializable {

    private static final long serialVersionUID = 3717438789640034674L;
    private String eventId;
    private String completeInconformityFlag;
    private List<MtEoStepActualVO47> eoStepActualList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }

    public List<MtEoStepActualVO47> getEoStepActualList() {
        return eoStepActualList;
    }

    public void setEoStepActualList(List<MtEoStepActualVO47> eoStepActualList) {
        this.eoStepActualList = eoStepActualList;
    }
}


