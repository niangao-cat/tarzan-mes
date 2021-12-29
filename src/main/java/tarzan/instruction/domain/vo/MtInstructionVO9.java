package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtInstructionVO9 implements Serializable {

    private static final long serialVersionUID = -4883220209315298343L;

    private List<String> actualId;

    private String eventId;

    public List<String> getActualId() {
        return actualId;
    }

    public void setActualId(List<String> actualId) {
        this.actualId = actualId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }


}
