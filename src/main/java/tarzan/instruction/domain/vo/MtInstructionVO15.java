package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class MtInstructionVO15 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = -4479980281142144402L;

    private Map<String, List<String>> actualList;

    private String eventId;

    public Map<String, List<String>> getActualList() {
        return actualList;
    }

    public void setActualList(Map<String, List<String>> actualList) {
        this.actualList = actualList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }



}
