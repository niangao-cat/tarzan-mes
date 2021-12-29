package tarzan.instruction.api.dto;

import java.io.Serializable;

public class MtInstructionDocDTO3 implements Serializable {
    private static final long serialVersionUID = -8079823063669105581L;

    private String instructionDocId;
    private String eventRequestId;

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
