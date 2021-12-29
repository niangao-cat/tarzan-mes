package tarzan.instruction.domain.vo;

import java.io.Serializable;

import tarzan.instruction.domain.entity.MtInstruction;

public class MtInstructionVO7 extends MtInstruction implements Serializable {

    private static final long serialVersionUID = -8413190550692013310L;
    private String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
