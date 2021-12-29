package tarzan.instruction.api.dto;

import java.io.Serializable;

public class MtInstructionDocHisDTO implements Serializable {
    private static final long serialVersionUID = -8924292107088453077L;

    private String instructionDocId;
    private String instructionDocHisId;
    private String eventId;

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    public String getInstructionDocHisId() {
        return instructionDocHisId;
    }

    public void setInstructionDocHisId(String instructionDocHisId) {
        this.instructionDocHisId = instructionDocHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "MtInstructionDocHisDTO{" +
                "instructionDocId='" + instructionDocId + '\'' +
                ", instructionDocHisId='" + instructionDocHisId + '\'' +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
