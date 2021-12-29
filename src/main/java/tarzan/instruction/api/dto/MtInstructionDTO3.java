package tarzan.instruction.api.dto;

import java.io.Serializable;

public class MtInstructionDTO3 implements Serializable {
    private static final long serialVersionUID = -8717434029677999824L;

    private String instructionId;
    private String eventRequestId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
